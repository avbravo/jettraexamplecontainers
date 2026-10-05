package io.jettra.explorer;

import io.jettra.explorer.model.*;
import io.jettra.explorer.pages.*;
import io.jettra.explorer.service.ConnectionManager;
import io.jettra.explorer.service.StoreClusterService;
import io.jettra.server.discoverer.DiscoveredLoad;
import io.jettra.studio.security.NoLoginRequired;
import io.jettra.studio.security.Secured;
import io.jettra.test.annotation.JettraTest;
import io.jettra.test.annotation.NotRequiresRunningServer;
import io.jettra.test.core.JettraAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static io.jettra.test.core.JettraAssert.*;

@DiscoveredLoad
@NotRequiresRunningServer
public class AppTest {

    @JettraTest
    public void testConfigLoading() {
        App app = new App();
        app.initUI();

        assertNotNull(app.getAppTitle(), "El título no debe ser nulo");
        assertTrue(app.getAppTitle().contains("JettraStore Explorer"));
        assertEquals("8088", app.getPort().trim());
        assertEquals("es", app.getAppLanguage().trim());
        assertEquals("games", app.getAppTheme().trim());
    }

    @JettraTest
    public void testClusterServiceNodesMonitoring() {
        StoreClusterService service = StoreClusterService.getInstance();
        List<ServerNode> nodes = service.getNodes();

        assertNotNull(nodes);
        assertEquals(4, nodes.size(), "Deben monitorearse 4 nodos en el clúster");
        assertEquals(4, service.getOnlineNodesCount(), "Todos los nodos deben estar ONLINE");
        assertTrue(service.getTotalTps() > 50000, "El TPS global del clúster debe superar los 50k");
        assertTrue(service.getAverageLatency() > 0.0 && service.getAverageLatency() < 5.0, "Latencia promedio en rango normal");
    }

    @JettraTest
    public void testConnectionManagerAndProfiles() {
        ConnectionManager connManager = ConnectionManager.getInstance();
        assertNotNull(connManager.getProfiles());
        assertTrue(connManager.getProfiles().size() >= 2, "Deben existir perfiles por defecto");

        // 1. Create and save profile
        ConnectionProfile testProf = new ConnectionProfile(
            "conn_test_cluster", "Clúster Test Alpha", "127.0.0.1:9010", "admin", "admin-jettra", false
        );
        connManager.saveOrUpdate(testProf);

        Optional<ConnectionProfile> retrieved = connManager.findById("conn_test_cluster");
        assertTrue(retrieved.isPresent(), "El perfil guardado debe poder recuperarse por ID");
        assertEquals("Clúster Test Alpha", retrieved.get().getName());
        assertEquals("127.0.0.1", retrieved.get().getHost());
        assertEquals(9010, retrieved.get().getPort());

        // 2. Test connection ping
        var testResult = connManager.testConnection(retrieved.get());
        assertNotNull(testResult);
        assertTrue(testResult.success(), "La prueba de conexión directa debe ser exitosa");

        // 3. Activate profile
        boolean activated = connManager.activate("conn_test_cluster");
        assertTrue(activated, "Debe poder activarse la conexión");
        assertTrue(connManager.getActiveProfile().isPresent());
        assertEquals("conn_test_cluster", connManager.getActiveProfile().get().getId());

        // 4. Delete profile
        boolean deleted = connManager.delete("conn_test_cluster");
        assertTrue(deleted, "El perfil de prueba debe poder eliminarse");
    }

    @JettraTest
    public void testDatabasesFullCrud() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. READ: Dynamic database list from JettraStore
        List<DatabaseOverview> dbs = service.getDatabases();
        assertNotNull(dbs);
        assertTrue(service.getDatabaseNames().contains("ecommerce_db"), "Debe existir ecommerce_db cargada directamente");

        // 2. CREATE: Create new database
        String dbName = "inventory_warehouse_db";
        boolean created = service.createDatabase(dbName);
        assertTrue(created, "Debe crearse la base de datos exitosamente");
        assertTrue(service.getDatabaseNames().contains(dbName));

        // Insert sample record to verify migration on rename
        service.insertRecord(dbName, "items", "ITEM-01", "{\"sku\":\"SKU-990\",\"qty\":50}");
        assertEquals(1, service.getRecords(dbName, "items", "", 0, 10).size());

        // 3. UPDATE / RENAME: Rename database
        String renamedDb = "inventory_archive_db";
        boolean renamed = service.renameDatabase(dbName, renamedDb);
        assertTrue(renamed, "Debe renombrarse la base de datos");
        assertFalse(service.getDatabaseNames().contains(dbName), "La BD con nombre anterior no debe existir");
        assertTrue(service.getDatabaseNames().contains(renamedDb), "La BD con nuevo nombre debe estar activa");

        // 4. DELETE: Drop database
        boolean deleted = service.deleteDatabase(renamedDb);
        assertTrue(deleted, "Debe eliminarse la base de datos");
        assertFalse(service.getDatabaseNames().contains(renamedDb), "La base de datos eliminada no debe existir");
    }

    @JettraTest
    public void testRecordUnitsFullCrud() {
        StoreClusterService service = StoreClusterService.getInstance();
        String db = "ecommerce_db";
        String bucket = "customers";
        String recordId = "CUST-UNIT-777";

        // 1. CREATE: Insert record unit
        String initialJson = "{\"name\":\"Unidad Alpha\",\"status\":\"PENDING\"}";
        service.insertRecord(db, bucket, recordId, initialJson);

        // 2. READ: Query record unit
        Optional<RecordItem> found = service.getRecordById(db, bucket, recordId);
        assertTrue(found.isPresent(), "El registro insertado debe existir en el bucket");
        assertEquals(recordId, found.get().id());
        assertTrue(found.get().preview().contains("Unidad Alpha"));

        // 3. UPDATE: Modify record payload
        String updatedJson = "{\"name\":\"Unidad Alpha\",\"status\":\"VIP_ACTIVE\"}";
        service.updateRecord(db, bucket, recordId, updatedJson);

        Optional<RecordItem> updated = service.getRecordById(db, bucket, recordId);
        assertTrue(updated.isPresent());
        assertTrue(updated.get().preview().contains("VIP_ACTIVE"), "El payload del registro debe reflejar la actualización");

        // 4. DELETE: Remove record unit
        boolean removed = service.deleteRecord(db, bucket, recordId);
        assertTrue(removed, "El registro debe eliminarse de JettraStore");

        Optional<RecordItem> afterDelete = service.getRecordById(db, bucket, recordId);
        assertTrue(afterDelete.isEmpty(), "El registro eliminado ya no debe encontrarse");
    }

    @JettraTest
    public void testPoliceSentinelsAndIncidents() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. Sentinels
        List<PoliceSentinel> sentinels = service.getSentinels();
        assertNotNull(sentinels);
        assertEquals(4, sentinels.size(), "Deben existir 4 centinelas policiales");
        assertTrue(sentinels.stream().anyMatch(s -> s.name().equals("Heap Sentinel")));
        assertTrue(sentinels.stream().anyMatch(s -> s.name().equals("Raft Quorum K9")));
        assertTrue(sentinels.stream().anyMatch(s -> s.name().equals("MemTable Purge Dog")));
        assertTrue(sentinels.stream().anyMatch(s -> s.name().equals("Security Patrol")));

        // 2. Incident logging
        int initialCount = service.getIncidents().size();
        service.logIncident("Security Patrol", "Intento de acceso no autorizado bloqueado", "WARN", "IP bloqueada temporalmente");
        assertEquals(initialCount + 1, service.getIncidents().size(), "El incidente debe agregarse al muro");
        assertEquals("Security Patrol", service.getIncidents().get(0).sentinel());

        // 3. Multinode toggle
        boolean before = service.isMultinodeActive();
        service.setMultinodeActive(!before);
        assertEquals(!before, service.isMultinodeActive());
        service.setMultinodeActive(before); // restore
    }

    @JettraTest
    public void testPolice3DSubworldAndTelemetry() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. Node Subworld Internal Telemetry
        var subworld = service.getNodeInternalTelemetry("node-01");
        assertNotNull(subworld);
        assertEquals("node-01", subworld.nodeId());
        assertTrue(subworld.heapUsedMb() > 0);
        assertTrue(subworld.directMemoryUsedMb() > 0);
        assertTrue(subworld.loomVirtualThreads() > 0);
        assertTrue(subworld.hostedDatabases().size() >= 2);

        // 2. Live Sessions (Personas)
        List<LiveUserSession> sessions = service.getLiveSessions();
        assertNotNull(sessions);
        assertTrue(sessions.size() >= 4, "Deben existir sesiones de usuario activas");

        // 3. User Zones (Edificios)
        List<UserZone> zones = service.getUserZones();
        assertNotNull(zones);
        assertEquals(4, zones.size(), "Deben existir 4 zonas de conexión por subred");

        // 4. Cluster Traffic (Camiones)
        List<ClusterTrafficBatch> traffic = service.getClusterTraffic();
        assertNotNull(traffic);
        assertTrue(traffic.size() >= 2, "Debe registrarse tráfico de réplicas en tránsito");
    }

    @JettraTest
    public void testQueryConsoleExecution() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. Query with filter
        StoreClusterService.QueryResult res = service.executeQuery("ecommerce_db", "customers", "VIP", true);
        assertNotNull(res);
        assertTrue(res.success());
        assertTrue(res.records().size() >= 1, "Debe encontrar registros que contienen VIP");

        // 2. Query empty
        StoreClusterService.QueryResult allRes = service.executeQuery("ecommerce_db", "customers", "", true);
        assertNotNull(allRes);
        assertTrue(allRes.records().size() >= 2);
    }

    @JettraTest
    public void testUserAccountsManagement() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. Initial users
        List<JettraUserAccount> users = service.getUsers();
        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("admin")));

        // 2. Save user
        JettraUserAccount devUser = new JettraUserAccount("dev_tester", "dev123", "Desarrollador QA", "OPERATOR");
        service.saveUser(devUser);
        assertTrue(service.getUsers().stream().anyMatch(u -> u.getUsername().equals("dev_tester")));

        // 3. Delete user
        boolean deleted = service.deleteUser("dev_tester");
        assertTrue(deleted, "El usuario de prueba debe poder eliminarse");
        assertFalse(service.getUsers().stream().anyMatch(u -> u.getUsername().equals("dev_tester")));

        // 4. Admin cannot be deleted
        boolean adminDeleted = service.deleteUser("admin");
        assertFalse(adminDeleted, "El usuario 'admin' no debe poder ser eliminado");
    }

    @JettraTest
    public void testIndexesManagement() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. List indexes
        List<IndexOverview> list = service.getIndexes("ecommerce_db");
        assertNotNull(list);
        assertTrue(list.size() >= 1, "Deben existir índices por defecto");

        // 2. Create index
        service.createIndex("ecommerce_db", "customers", "idx_custom_test", "BTree", "status", false);
        assertTrue(service.getIndexes("ecommerce_db").stream().anyMatch(i -> i.indexName().equals("idx_custom_test")));

        // 3. Drop index
        boolean dropped = service.dropIndex("idx_custom_test");
        assertTrue(dropped, "El índice debe eliminarse correctamente");
    }

    @JettraTest
    public void testBackupAndRestore() {
        StoreClusterService service = StoreClusterService.getInstance();

        // 1. Generate snapshot
        BackupItem backup = service.createBackup("ecommerce_db");
        assertNotNull(backup);
        assertTrue(backup.id().startsWith("BAK-"));
        assertTrue(backup.fileName().contains("ecommerce_db"));

        // 2. Restore
        boolean restored = service.restoreBackup(backup.id());
        assertTrue(restored, "La restauración del snapshot debe ser exitosa");
    }

    @JettraTest
    public void testPageSecurityAnnotations() {
        // Public pages
        assertTrue(LoginPage.class.isAnnotationPresent(NoLoginRequired.class));

        // Secured pages
        assertTrue(Police3DPage.class.isAnnotationPresent(Secured.class));
        assertTrue(PoliceMonitorPage.class.isAnnotationPresent(Secured.class));
        assertTrue(QueryConsolePage.class.isAnnotationPresent(Secured.class));
        assertTrue(UserManagerPage.class.isAnnotationPresent(Secured.class));
        assertTrue(ConnectionsPage.class.isAnnotationPresent(Secured.class));
        assertTrue(ClusterDashboardPage.class.isAnnotationPresent(Secured.class));
        assertTrue(DatabasesPage.class.isAnnotationPresent(Secured.class));
        assertTrue(EnginesPage.class.isAnnotationPresent(Secured.class));
        assertTrue(IndexesPage.class.isAnnotationPresent(Secured.class));
        assertTrue(RecordsPage.class.isAnnotationPresent(Secured.class));
        assertTrue(BackupRestorePage.class.isAnnotationPresent(Secured.class));

        Secured backupSecured = BackupRestorePage.class.getAnnotation(Secured.class);
        assertEquals(1, backupSecured.roles().length);
        assertEquals("ADMIN", backupSecured.roles()[0], "Solo ADMIN puede acceder a BackupRestorePage");

        Secured userSecured = UserManagerPage.class.getAnnotation(Secured.class);
        assertEquals("ADMIN", userSecured.roles()[0], "Solo ADMIN puede gestionar usuarios");
    }

    @JettraTest
    public void testPagesHtmlRendering() {
        // 1. LoginPage
        LoginPage loginPage = new LoginPage();
        String loginHtml = loginPage.renderPage();
        assertNotNull(loginHtml);
        assertTrue(loginHtml.contains("JettraStore Explorer"));

        // 2. Police3DPage
        Police3DPage p3dPage = new Police3DPage();
        String p3dHtml = p3dPage.renderPage();
        assertNotNull(p3dHtml);
        assertTrue(p3dHtml.contains("jettra3dCanvas"));
        assertTrue(p3dHtml.contains("Submundo Interior"));
        assertTrue(p3dHtml.contains("Loom-Master-Primary"));

        // 3. PoliceMonitorPage
        PoliceMonitorPage policePage = new PoliceMonitorPage();
        String policeHtml = policePage.renderPage();
        assertNotNull(policeHtml);
        assertTrue(policeHtml.contains("Centinelas Police"));
        assertTrue(policeHtml.contains("Heap Sentinel"));
        assertTrue(policeHtml.contains("Muro de Incidentes"));

        // 4. QueryConsolePage
        QueryConsolePage queryPage = new QueryConsolePage();
        String qHtml = queryPage.renderPage();
        assertNotNull(qHtml);
        assertTrue(qHtml.contains("Consola de Consultas JettraSQL"));
        assertTrue(qHtml.contains("Ejecutar JettraSQL"));

        // 5. UserManagerPage
        UserManagerPage userPage = new UserManagerPage();
        String uHtml = userPage.renderPage();
        assertNotNull(uHtml);
        assertTrue(uHtml.contains("Gestión de Usuarios"));
        assertTrue(uHtml.contains("admin"));

        // 6. ConnectionsPage
        ConnectionsPage connPage = new ConnectionsPage();
        String connHtml = connPage.renderPage();
        assertNotNull(connHtml);
        assertTrue(connHtml.contains("Administrador de Conexiones"));

        // 7. DatabasesPage
        DatabasesPage dbPage = new DatabasesPage();
        String dbHtml = dbPage.renderPage();
        assertNotNull(dbHtml);
        assertTrue(dbHtml.contains("ecommerce_db"));

        // 8. RecordsPage
        RecordsPage recPage = new RecordsPage();
        String recHtml = recPage.renderPage();
        assertNotNull(recHtml);
        assertTrue(recHtml.contains("CUST-101"));

        // 9. EnginesPage
        EnginesPage engPage = new EnginesPage();
        String engHtml = engPage.renderPage();
        assertNotNull(engHtml);
        assertTrue(engHtml.contains("Document (JSON)"));

        // 10. IndexesPage
        IndexesPage idxPage = new IndexesPage();
        String idxHtml = idxPage.renderPage();
        assertNotNull(idxHtml);
        assertTrue(idxHtml.contains("BTree"));

        // 11. BackupRestorePage
        BackupRestorePage bakPage = new BackupRestorePage();
        String bakHtml = bakPage.renderPage();
        assertNotNull(bakHtml);
        assertTrue(bakHtml.contains(".jbak"));
    }

    @JettraTest
    public void testServerLifecycleAndHttpSecurity() throws IOException {
        int testPort = 19100;
        App app = new App();
        app.start(testPort);
        assertNotNull(app.getServer());

        try {
            // 1. Unauthenticated access to /police3d -> 302 redirect to /login
            URI unauthConnUri = URI.create("http://localhost:" + testPort + "/police3d");
            HttpURLConnection cconn = (HttpURLConnection) unauthConnUri.toURL().openConnection();
            cconn.setInstanceFollowRedirects(false);
            cconn.connect();
            assertEquals(302, cconn.getResponseCode());
            String cLoc = cconn.getHeaderField("Location");
            assertNotNull(cLoc);
            assertTrue(cLoc.contains("/login"));
            cconn.disconnect();

            // 2. Unauthenticated access to /query -> 302 redirect
            URI queryUri = URI.create("http://localhost:" + testPort + "/query");
            HttpURLConnection qconn = (HttpURLConnection) queryUri.toURL().openConnection();
            qconn.setInstanceFollowRedirects(false);
            qconn.connect();
            assertEquals(302, qconn.getResponseCode());
            qconn.disconnect();

            // 3. Authenticated access to /police3d with ADMIN -> 200 OK
            HttpURLConnection aConn = (HttpURLConnection) unauthConnUri.toURL().openConnection();
            aConn.setRequestProperty("Cookie", "jettra_user=admin; jettra_role=ADMIN");
            aConn.connect();
            assertEquals(200, aConn.getResponseCode());
            aConn.disconnect();

            // 4. Authenticated access to /police with ADMIN -> 200 OK
            URI policeUri = URI.create("http://localhost:" + testPort + "/police");
            HttpURLConnection pconn = (HttpURLConnection) policeUri.toURL().openConnection();
            pconn.setRequestProperty("Cookie", "jettra_user=admin; jettra_role=ADMIN");
            pconn.connect();
            assertEquals(200, pconn.getResponseCode());
            pconn.disconnect();

            // 5. Authenticated access to /users with ADMIN -> 200 OK
            URI usersUri = URI.create("http://localhost:" + testPort + "/users");
            HttpURLConnection uconn = (HttpURLConnection) usersUri.toURL().openConnection();
            uconn.setRequestProperty("Cookie", "jettra_user=admin; jettra_role=ADMIN");
            uconn.connect();
            assertEquals(200, uconn.getResponseCode());
            uconn.disconnect();

            // 6. Authenticated access to /users with OPERATOR -> 403 Forbidden
            HttpURLConnection opConn = (HttpURLConnection) usersUri.toURL().openConnection();
            opConn.setRequestProperty("Cookie", "jettra_user=op; jettra_role=OPERATOR");
            opConn.connect();
            assertEquals(403, opConn.getResponseCode());
            opConn.disconnect();

        } finally {
            app.stop();
        }
    }
}
