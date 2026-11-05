# JettraStoreExample 🚀 (Java 25 + Maven)

Proyecto de referencia y demostración completa de **JettraStore** implementando **JettraDriver**. Expone ejemplos funcionales con cada uno de los motores multimodelo integrados, operando en los 3 modos de almacenamiento nativos:
1. **`JVM_RAM`**: Modo memoria RAM estándar de la JVM (Heap & Stack).
2. **`DISK_MEMORY`**: Motor de disco directo Off-Heap LSM (`JettraMemory`).
3. **`OFF_HEAP`**: Motor de páginas ranuradas nativas Slotted-Page FFM Panama (`JettraStore-OffHeap`).

Además, integra un módulo empresarial completo de **Facturación (`example_factura_db`)** con operaciones CRUD completas y exposición de **Endpoints REST** mediante **JettraRest** sobre Virtual Threads.

---

## 📋 Características Principales

- **Java 25 LTS:** Aprovecha *Virtual Threads* (`Thread.ofVirtual()`), *Records*, *Pattern Matching*, y *Project Panama FFM API* (`MemorySegment`, `Arena`).
- **JettraDriver:** Controlador unificado (`io.jettra.examples.store.driver.JettraDriver`) con soporte de catálogo, conmutación dinámica de almacenamiento y acceso directo a todos los motores.
- **Los 3 Modos de Almacenamiento:**
  - `StorageMode.JVM_RAM`: Modo memoria RAM estándar de la JVM (Heap & Stack). Máxima velocidad in-memory para prototipado y cargas transitorias.
  - `StorageMode.DISK_MEMORY`: Motor de disco directo Off-Heap LSM con buffers mapeados en archivo (`JettraMemory`). Persistencia directa sin pausas de Garbage Collector.
  - `StorageMode.OFF_HEAP`: Motor de páginas ranuradas nativas Slotted-Page en memoria virtual no administrada (`JettraStore-OffHeap`). Cero pausas de GC y liberación determinista por ámbito RAII (`Arena`).
- **Módulo de Facturación Empresarial (`example_factura_db`):**
  - Implementación completa de CRUD sobre `facturas` y `detalles_factura`.
  - Paginación de resultados y cálculo de subtotales e impuestos.
  - Alternancia dinámica de modos de almacenamiento en caliente.
- **Endpoints REST con JettraRest (`/api/facturas`):**
  - `GET /api/facturas?page=1&size=10`: Listado paginado de facturas.
  - `GET /api/facturas/{id}`: Consulta de factura por ID.
  - `POST /api/facturas`: Creación de nueva factura.
  - `PUT /api/facturas/{id}`: Modificación de factura.
  - `DELETE /api/facturas/{id}`: Eliminación de factura.
  - `GET /api/facturas/storage-mode`: Consulta del modo de almacenamiento activo.
  - `POST /api/facturas/storage-mode/{mode}`: Conmutación de modo (`JVM_RAM`, `DISK_MEMORY`, `OFF_HEAP`).
- **Multimodelo Completo (9 Motores):** Document, KeyValue, Vector, Graph, TimeSeries, Geospatial, Columnar, Records y Off-Heap Memory.
- **JettraPolice Sentinel:** Monitoreo y protección proactiva Anti-OOM en tiempo real.

---

## 📂 Estructura del Proyecto

```
JettraStoreExample/
├── pom.xml
├── README.md
└── src/
    ├── main/java/io/jettra/examples/store/
    │   ├── JettraStoreExampleApp.java           # Ejecutor principal de demostración
    │   ├── driver/
    │   │   ├── JettraDriver.java                # Contrato de la API del Driver
    │   │   └── JettraDriverImpl.java            # Implementación sobre JettraClient
    │   ├── factura/
    │   │   ├── model/
    │   │   │   ├── Factura.java                 # Modelo de Factura
    │   │   │   └── DetalleFactura.java          # Línea de detalle de factura
    │   │   ├── service/
    │   │   │   └── FacturaService.java          # CRUD sobre example_factura_db en los 3 modos
    │   │   ├── rest/
    │   │   │   └── FacturaRestController.java   # Controlador REST con JettraRest
    │   │   └── FacturaCrudExample.java          # Demostración en consola del CRUD en 3 modos
    │   ├── engines/                             # Motores multimodelo (Document, KV, Vector, etc.)
    │   ├── storage/
    │   │   └── StorageModeComparisonExample.java # Comparativa entre JVM_RAM, DISK_MEMORY y OFF_HEAP
    │   ├── query/
    │   │   ├── SqlAndCursorExample.java         # Consultas SQL, Paginación y Cursores
    │   │   └── MassiveFindAllMillionRecordsExample.java # Consultas masivas sobre example_factura_db
    │   └── police/
    │       └── AntiOomStreamingSentinelExample.java # Monitoreo de memoria Anti-OOM
    └── test/java/io/jettra/examples/store/
        ├── FacturaCrudAndRestTest.java          # Pruebas de CRUD y Endpoints REST en 3 modos
        ├── JettraDriverEnginesTest.java         # Pruebas unitarias de todos los motores
        └── MassiveFindAllMillionRecordsTest.java # Pruebas con centinela JettraPolice
```

---

## 🚀 Compilación y Ejecución

### Prerrequisitos
- **Java 25+** (con soporte preview)
- **Maven 3.9+**

### 1. Compilar el proyecto
```bash
mvn clean compile
```

### 2. Ejecutar las pruebas unitarias e integración (incluye pruebas REST)
```bash
mvn test
```

### 3. Ejecutar la aplicación interactiva de consola
```bash
mvn exec:java
```

---

## 💻 Ejemplos de Uso

### 1. Selección y cambio de Modos de Almacenamiento en FacturaService
```java
try (JettraDriver driver = JettraDriver.open()) {
    FacturaService service = new FacturaService(driver);

    // Modo 1: JVM_RAM (Heap/Stack estándar)
    service.setStorageMode(StorageMode.JVM_RAM);

    // Modo 2: DISK_MEMORY (JettraMemory LSM Panama FFM)
    service.setStorageMode(StorageMode.DISK_MEMORY);

    // Modo 3: OFF_HEAP (JettraStore-OffHeap Slotted-Page Panama FFM)
    service.setStorageMode(StorageMode.OFF_HEAP);

    // CRUD
    Factura f = new Factura();
    f.setNumero("FAC-2026-001");
    f.setClienteNombre("Empresa Demo");
    f.addItem(new DetalleFactura("item_1", "Servidor Virtual", 2, 150.0));
    
    service.create(f);
    Optional<Factura> encontrada = service.findById(f.getId());
}
```

### 2. Iniciar Servidor REST con JettraRest y Virtual Threads
```java
JettraServer server = new JettraServer(8085);
FacturaService service = new FacturaService(driver);
FacturaRestController controller = new FacturaRestController(service);

JettraRestServer.register(server, controller);
server.start();

// El endpoint responderá en:
// GET  http://localhost:8085/api/facturas
// POST http://localhost:8085/api/facturas/storage-mode/OFF_HEAP
```
