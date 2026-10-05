package io.jettra.examples.studio.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe In-Memory Repository for Product CRUD operations.
 */
public class ProductRepository {

    private static final ProductRepository INSTANCE = new ProductRepository();
    private final Map<String, Product> storage = new ConcurrentHashMap<>();

    private ProductRepository() {
        resetToDefaults();
    }

    public static ProductRepository getInstance() {
        return INSTANCE;
    }

    public synchronized void resetToDefaults() {
        storage.clear();
        save(new Product("PROD-101", "Servidor Edge Jettra", "Infraestructura", 15, 1250.00, true));
        save(new Product("PROD-102", "Licencia JettraStore Enterprise", "Software", 99, 499.99, true));
        save(new Product("PROD-103", "Gateway gRPC Loom", "Conectividad", 42, 280.50, true));
        save(new Product("PROD-104", "Sensor IoT Industrial", "Hardware", 8, 85.00, false));
        save(new Product("PROD-105", "Módulo de IA Vectorial", "Analítica", 27, 850.00, true));
    }

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>(storage.values());
        list.sort((a, b) -> a.id().compareToIgnoreCase(b.id()));
        return list;
    }

    public Optional<Product> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id.trim().toUpperCase()));
    }

    public Product save(Product product) {
        if (product == null || product.id() == null) {
            throw new IllegalArgumentException("Product and Product ID must not be null");
        }
        storage.put(product.id().trim().toUpperCase(), product);
        return product;
    }

    public Product update(Product product) {
        return save(product);
    }

    public boolean delete(String id) {
        if (id == null) return false;
        return storage.remove(id.trim().toUpperCase()) != null;
    }

    public int count() {
        return storage.size();
    }

    public List<Product> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        String q = query.toLowerCase().trim();
        return storage.values().stream()
            .filter(p -> p.name().toLowerCase().contains(q)
                || p.category().toLowerCase().contains(q)
                || p.id().toLowerCase().contains(q))
            .sorted((a, b) -> a.id().compareToIgnoreCase(b.id()))
            .toList();
    }
}
