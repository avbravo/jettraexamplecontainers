package io.jettra.flux.explorer;

import io.jettra.flux.explorer.model.ConnectionProfile;
import io.jettra.flux.explorer.model.DatabaseOverview;
import io.jettra.flux.explorer.model.RecordItem;
import io.jettra.flux.explorer.service.ConnectionManager;
import io.jettra.flux.explorer.service.StoreClusterService;
import io.jettra.test.annotation.JettraTest;
import io.jettra.test.core.JettraAssert;

import java.util.List;

public class AppTest {

    @JettraTest
    public void testConnectionManagerProfiles() {
        ConnectionManager mgr = ConnectionManager.getInstance();
        JettraAssert.assertNotNull(mgr, "ConnectionManager no debe ser nulo");

        List<ConnectionProfile> profiles = mgr.getProfiles();
        JettraAssert.assertTrue(!profiles.isEmpty(), "Debe haber perfiles de conexión iniciales");

        var activeOpt = mgr.getActiveProfile();
        JettraAssert.assertTrue(activeOpt.isPresent(), "Debe existir un perfil activo");

        // Test latency check
        var testRes = mgr.testConnection(activeOpt.get());
        JettraAssert.assertNotNull(testRes, "El resultado del test no debe ser nulo");
    }

    @JettraTest
    public void testClusterNodesAndTelemetry() {
        StoreClusterService service = StoreClusterService.getInstance();
        JettraAssert.assertNotNull(service, "StoreClusterService no debe ser nulo");

        var nodes = service.getNodes();
        JettraAssert.assertTrue(nodes.size() >= 4, "El clúster debe tener al menos 4 nodos");

        var telemetry = service.getNodeInternalTelemetry("node-01");
        JettraAssert.assertNotNull(telemetry, "La telemetría interna no debe ser nula");
        JettraAssert.assertTrue(telemetry.loomVirtualThreads() > 0, "Loom Virtual Threads debe ser mayor a 0");
    }

    @JettraTest
    public void testDatabaseAndRecordsCrud() {
        StoreClusterService service = StoreClusterService.getInstance();
        String testDb = "test_analytics_db";

        // Create DB
        boolean created = service.createDatabase(testDb);
        JettraAssert.assertTrue(created, "La base de datos debe crearse exitosamente");

        // Insert record
        service.insertRecord(testDb, "default", "REC-9001", "{\"metric\":\"cpu_load\", \"val\":42.5}");
        var recs = service.getRecords(testDb, "default", "REC-9001", 0, 10);
        JettraAssert.assertTrue(!recs.isEmpty(), "El registro debe ser recuperado");

        // Query Console Execution
        var qResult = service.executeQuery(testDb, "default", "REC-9001", true);
        JettraAssert.assertTrue(qResult.success(), "La consulta JettraSQL debe ser exitosa");

        // Cleanup
        service.deleteRecord(testDb, "default", "REC-9001");
        service.deleteDatabase(testDb);
    }

    @JettraTest
    public void testSentinelsAndIncidents() {
        StoreClusterService service = StoreClusterService.getInstance();
        var sentinels = service.getSentinels();
        JettraAssert.assertTrue(!sentinels.isEmpty(), "Debe haber centinelas JettraPolice activos");

        int initialIncidents = service.getIncidents().size();
        service.logIncident("Heap Sentinel", "Prueba de saturación simulada", "WARN", "Compactación");
        JettraAssert.assertTrue(service.getIncidents().size() > initialIncidents, "El muro de incidentes debe registrar el nuevo evento");
    }

    @JettraTest
    public void testBackupRestoreSnapshot() {
        StoreClusterService service = StoreClusterService.getInstance();
        var backup = service.createBackup("ecommerce_db");
        JettraAssert.assertNotNull(backup, "El snapshot no debe ser nulo");

        boolean restored = service.restoreBackup(backup.id());
        JettraAssert.assertTrue(restored, "La restauración del snapshot debe ser exitosa");
    }
}
