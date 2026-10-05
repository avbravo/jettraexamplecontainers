package com.jettra.plugin.example.repository;

import com.jettra.plugin.example.model.ArticuloModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArticuloRepository {
    private static final List<ArticuloModel> data = new ArrayList<>();

    static {
        data.add(new ArticuloModel("1", "Laptop XYZ", "electronic", 10000.0, 1, LocalDate.of(2026, 5, 17), 10000.0));
        data.add(new ArticuloModel("2", "Monitor 4K", "accessory", 456.0, 1, LocalDate.of(2026, 5, 17), 456.0));
        data.add(new ArticuloModel("3", "Teclado Mecánico", "office", 231.0, 1, LocalDate.of(2026, 5, 17), 231.0));
    }

    public static List<ArticuloModel> findAll() {
        return new ArrayList<>(data);
    }

    public static ArticuloModel findById(String id) {
        return data.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(ArticuloModel model) {
        ArticuloModel existing = findById(model.getId());
        if (existing != null) {
            existing.setNombre(model.getNombre());
            existing.setCategoria(model.getCategoria());
            existing.setPrecio(model.getPrecio());
            existing.setCantidad(model.getCantidad());
            existing.setFechaEntrega(model.getFechaEntrega());
            existing.setTotal(model.getTotal());
        } else {
            data.add(model);
        }
    }

    public static void delete(String id) {
        data.removeIf(a -> a.getId().equals(id));
    }
}
