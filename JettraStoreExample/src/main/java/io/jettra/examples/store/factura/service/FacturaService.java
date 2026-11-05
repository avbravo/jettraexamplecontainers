package io.jettra.examples.store.factura.service;

import io.jettra.examples.store.driver.JettraDriver;
import io.jettra.examples.store.factura.model.Factura;
import io.jettra.store.core.JettraDatabase;
import io.jettra.store.core.StorageMode;
import io.jettra.store.engine.models.DocumentEngine;
import io.jettra.store.engine.models.KeyValueEngine;
import io.jettra.store.sample.JettraStoreSamples;

import java.util.*;

/**
 * Servicio integral CRUD para el módulo de Facturación en la base de datos 'example_factura_db'.
 * Soporta de manera transparente los 3 modos de almacenamiento de JettraStore:
 *   - JVM_RAM     : Memoria RAM estándar de la JVM (Heap & Stack).
 *   - DISK_MEMORY : Motor de disco directo Off-Heap LSM (JettraMemory).
 *   - OFF_HEAP    : Motor de páginas ranuradas nativas Slotted-Page (JettraStore-OffHeap).
 */
public class FacturaService {

    public static final String DATABASE_NAME = "example_factura_db";
    public static final String COLLECTION_NAME = "facturas";

    private final JettraDriver driver;

    public FacturaService(JettraDriver driver) {
        this.driver = Objects.requireNonNull(driver, "JettraDriver no puede ser nulo");
        initDatabase();
    }

    /**
     * Inicializa la base de datos asegurando que contenga las colecciones requeridas.
     */
    private void initDatabase() {
        JettraDatabase db = driver.getDatabase(DATABASE_NAME);
        DocumentEngine docEngine = db.getDocumentEngine(COLLECTION_NAME);
        if (docEngine.count() == 0) {
            // Cargar datos de muestra iniciales si está vacía
            JettraStoreSamples.installSample(DATABASE_NAME, db, false);
        }
    }

    /**
     * Consulta el modo de almacenamiento activo para la base de datos 'example_factura_db'.
     */
    public StorageMode getStorageMode() {
        return driver.getStorageMode(DATABASE_NAME);
    }

    /**
     * Conmuta el modo de almacenamiento entre JVM_RAM, DISK_MEMORY u OFF_HEAP.
     */
    public void setStorageMode(StorageMode mode) {
        if (mode == null) mode = StorageMode.JVM_RAM;
        driver.setStorageMode(DATABASE_NAME, mode);
    }

    /**
     * Conmuta el modo a partir de una cadena (e.g. "JVM_RAM", "DISK_MEMORY", "OFF_HEAP", "OFF-HEAP").
     */
    public StorageMode setStorageMode(String modeStr) {
        StorageMode mode = StorageMode.fromString(modeStr);
        setStorageMode(mode);
        return mode;
    }

    /**
     * CREATE: Inserta una nueva factura en la colección.
     */
    public Factura create(Factura factura) {
        if (factura == null) {
            throw new IllegalArgumentException("La factura no puede ser nula");
        }
        if (factura.getId() == null || factura.getId().isBlank()) {
            factura.setId("fac_" + UUID.randomUUID().toString().substring(0, 8));
        }
        if (factura.getFecha() == null || factura.getFecha().isBlank()) {
            factura.setFecha(java.time.LocalDate.now().toString());
        }
        factura.recalcularTotales();

        DocumentEngine docEngine = driver.getDocumentEngine(DATABASE_NAME, COLLECTION_NAME);
        docEngine.insert(factura.getId(), factura.toMap());

        // Cachear en KeyValue engine para lecturas O(1)
        try {
            KeyValueEngine kv = driver.getKeyValueEngine(DATABASE_NAME, "cache_folios");
            kv.put(factura.getId(), ("FOLIO-" + factura.getNumero()).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception ignored) {}

        return factura;
    }

    /**
     * READ: Busca una factura por su identificador único.
     */
    public Optional<Factura> findById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        DocumentEngine docEngine = driver.getDocumentEngine(DATABASE_NAME, COLLECTION_NAME);
        Map<String, Object> map = docEngine.findById(id);
        if (map == null || map.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Factura.fromMap(map));
    }

    /**
     * UPDATE: Modifica los datos de una factura existente.
     */
    public Optional<Factura> update(String id, Factura factura) {
        if (id == null || factura == null) return Optional.empty();
        DocumentEngine docEngine = driver.getDocumentEngine(DATABASE_NAME, COLLECTION_NAME);
        Map<String, Object> existing = docEngine.findById(id);
        if (existing == null) {
            return Optional.empty();
        }

        factura.setId(id);
        factura.recalcularTotales();
        docEngine.update(id, factura.toMap());

        return Optional.of(factura);
    }

    /**
     * DELETE: Elimina una factura por su ID.
     */
    public boolean delete(String id) {
        if (id == null || id.isBlank()) return false;
        DocumentEngine docEngine = driver.getDocumentEngine(DATABASE_NAME, COLLECTION_NAME);
        boolean removed = docEngine.delete(id);

        try {
            KeyValueEngine kv = driver.getKeyValueEngine(DATABASE_NAME, "cache_folios");
            kv.remove(id);
        } catch (Exception ignored) {}

        return removed;
    }

    /**
     * LIST: Consulta paginada de facturas.
     */
    public List<Factura> list(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        DocumentEngine docEngine = driver.getDocumentEngine(DATABASE_NAME, COLLECTION_NAME);
        List<Map<String, Object>> all = docEngine.findAll();
        int fromIndex = (page - 1) * pageSize;
        if (fromIndex >= all.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        List<Map<String, Object>> sub = all.subList(fromIndex, toIndex);

        List<Factura> result = new ArrayList<>(sub.size());
        for (Map<String, Object> m : sub) {
            result.add(Factura.fromMap(m));
        }
        return result;
    }

    /**
     * COUNT: Retorna la cantidad total de facturas registradas.
     */
    public long count() {
        return driver.getDocumentEngine(DATABASE_NAME, COLLECTION_NAME).count();
    }
}

