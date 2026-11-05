package io.jettra.examples.store;

import io.jettra.examples.store.driver.JettraDriver;
import io.jettra.examples.store.factura.model.DetalleFactura;
import io.jettra.examples.store.factura.model.Factura;
import io.jettra.examples.store.factura.rest.FacturaRestController;
import io.jettra.examples.store.factura.service.FacturaService;
import io.jettra.rest.server.JettraRestServer;
import io.jettra.server.JettraServer;
import io.jettra.store.core.StorageMode;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FacturaCrudAndRestTest {

    private static JettraDriver driver;
    private static FacturaService service;
    private static JettraServer restServer;
    private static final int PORT = 9188;
    private static HttpClient httpClient;

    @BeforeAll
    static void setup() {
        driver = JettraDriver.open();
        service = new FacturaService(driver);

        // Iniciar Servidor JettraEE / JettraRest con hilos virtuales
        restServer = new JettraServer(PORT);
        FacturaRestController controller = new FacturaRestController(service);
        JettraRestServer.register(restServer, controller);
        restServer.start();

        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    @AfterAll
    static void tearDown() {
        if (restServer != null) {
            restServer.stop();
        }
        if (driver != null) {
            driver.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. CRUD completo en modo JVM_RAM directamente con FacturaService")
    void testCrudJvmRam() {
        service.setStorageMode(StorageMode.JVM_RAM);
        assertEquals(StorageMode.JVM_RAM, service.getStorageMode());

        Factura f = new Factura();
        f.setId("fac_test_ram_01");
        f.setNumero("TEST-RAM-001");
        f.setClienteId("cli_1");
        f.setClienteNombre("Empresa Alfa");
        f.setEstado("TIMBRADA");
        f.addItem(new DetalleFactura("d1", "Servidor Virtual", 1, 100.0));

        Factura created = service.create(f);
        assertNotNull(created);
        assertEquals(107.0, created.getTotal(), 0.01); // 100 + 7%

        Optional<Factura> found = service.findById("fac_test_ram_01");
        assertTrue(found.isPresent());
        assertEquals("Empresa Alfa", found.get().getClienteNombre());

        f.setEstado("PAGADA");
        Optional<Factura> updated = service.update("fac_test_ram_01", f);
        assertTrue(updated.isPresent());
        assertEquals("PAGADA", updated.get().getEstado());

        boolean deleted = service.delete("fac_test_ram_01");
        assertTrue(deleted);
        assertTrue(service.findById("fac_test_ram_01").isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("2. CRUD completo en modo DISK_MEMORY (JettraMemory LSM Panama)")
    void testCrudDiskMemory() {
        service.setStorageMode(StorageMode.DISK_MEMORY);
        assertEquals(StorageMode.DISK_MEMORY, service.getStorageMode());

        Factura f = new Factura();
        f.setId("fac_test_disk_02");
        f.setNumero("TEST-DISK-002");
        f.setClienteId("cli_2");
        f.setClienteNombre("Empresa Beta");
        f.setEstado("EMITIDA");
        f.addItem(new DetalleFactura("d2", "Almacenamiento NVMe", 2, 200.0));

        Factura created = service.create(f);
        assertNotNull(created);
        assertEquals(428.0, created.getTotal(), 0.01); // 400 + 7%

        Optional<Factura> found = service.findById("fac_test_disk_02");
        assertTrue(found.isPresent());
        assertEquals("Empresa Beta", found.get().getClienteNombre());

        boolean deleted = service.delete("fac_test_disk_02");
        assertTrue(deleted);
    }

    @Test
    @Order(3)
    @DisplayName("3. CRUD completo en modo OFF_HEAP (JettraStore-OffHeap Slotted-Page Panama FFM)")
    void testCrudOffHeap() {
        service.setStorageMode(StorageMode.OFF_HEAP);
        assertEquals(StorageMode.OFF_HEAP, service.getStorageMode());

        Factura f = new Factura();
        f.setId("fac_test_offheap_03");
        f.setNumero("TEST-OFFHEAP-003");
        f.setClienteId("cli_3");
        f.setClienteNombre("Empresa Gamma Zero-Copy");
        f.setEstado("TIMBRADA");
        f.addItem(new DetalleFactura("d3", "Módulo Memoria Nativa", 1, 500.0));

        Factura created = service.create(f);
        assertNotNull(created);
        assertEquals(535.0, created.getTotal(), 0.01); // 500 + 7%

        Optional<Factura> found = service.findById("fac_test_offheap_03");
        assertTrue(found.isPresent());
        assertEquals("Empresa Gamma Zero-Copy", found.get().getClienteNombre());

        boolean deleted = service.delete("fac_test_offheap_03");
        assertTrue(deleted);
    }

    @Test
    @Order(4)
    @DisplayName("4. Endpoints REST JettraRest: Consulta y cambio dinámico de Storage Mode")
    void testRestStorageModeEndpoints() throws Exception {
        // GET /api/facturas/storage-mode
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/facturas/storage-mode"))
                .GET()
                .build();
        HttpResponse<String> getRes = httpClient.send(getReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getRes.statusCode());
        assertTrue(getRes.body().contains("example_factura_db"));

        // POST /api/facturas/storage-mode/OFF_HEAP
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/facturas/storage-mode/OFF_HEAP"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> postRes = httpClient.send(postReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, postRes.statusCode());
        assertTrue(postRes.body().contains("OFF_HEAP") || postRes.body().contains("OFF-HEAP"));
        assertEquals(StorageMode.OFF_HEAP, service.getStorageMode());
    }

    @Test
    @Order(5)
    @DisplayName("5. Endpoints REST JettraRest: CRUD HTTP sobre Facturas")
    void testRestCrudEndpoints() throws Exception {
        // CREATE: POST /api/facturas
        String jsonPayload = """
            {
                "id": "fac_rest_909",
                "numero": "FAC-REST-909",
                "fecha": "2026-11-03",
                "clienteId": "cli_rest_99",
                "clienteNombre": "Comercializadora REST S.A.",
                "subtotal": 1000.0,
                "impuesto": 70.0,
                "total": 1070.0,
                "estado": "TIMBRADA"
            }
            """;
        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/facturas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
        HttpResponse<String> createRes = httpClient.send(createReq, HttpResponse.BodyHandlers.ofString());
        assertTrue(createRes.statusCode() == 200 || createRes.statusCode() == 201);
        assertTrue(createRes.body().contains("FAC-REST-909"));

        // READ: GET /api/facturas/fac_rest_909
        HttpRequest readReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/facturas/fac_rest_909"))
                .GET()
                .build();
        HttpResponse<String> readRes = httpClient.send(readReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, readRes.statusCode());
        assertTrue(readRes.body().contains("Comercializadora REST S.A."));

        // LIST: GET /api/facturas?page=1&size=5
        HttpRequest listReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/facturas?page=1&size=5"))
                .GET()
                .build();
        HttpResponse<String> listRes = httpClient.send(listReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, listRes.statusCode());
        assertTrue(listRes.body().contains("total"));

        // DELETE: DELETE /api/facturas/fac_rest_909
        HttpRequest deleteReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/facturas/fac_rest_909"))
                .DELETE()
                .build();
        HttpResponse<String> deleteRes = httpClient.send(deleteReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteRes.statusCode());
        assertTrue(deleteRes.body().contains("DELETED"));
    }
}

