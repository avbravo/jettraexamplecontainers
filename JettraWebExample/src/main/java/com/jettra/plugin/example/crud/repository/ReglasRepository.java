package com.jettra.plugin.example.crud.repository;

import com.jettra.plugin.example.rules.model.ReglasModel;
import java.util.ArrayList;
import java.util.List;

public class ReglasRepository {
    private static final List<ReglasModel> data = new ArrayList<>();

    static {
        data.add(new ReglasModel("1", 1000.0, 100.0, 900.0));
        data.add(new ReglasModel("2", 500.0, 50.0, 450.0));
    }

    public static List<ReglasModel> findAll() {
        return new ArrayList<>(data);
    }

    public static ReglasModel findById(String id) {
        return data.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(ReglasModel model) {
        ReglasModel existing = findById(model.getId());
        if (existing != null) {
            existing.setSaldo(model.getSaldo());
            existing.setDescuento(model.getDescuento());
            existing.setSaldoNeto(model.getSaldoNeto());
        } else {
            data.add(model);
        }
    }

    public static void delete(String id) {
        data.removeIf(r -> r.getId().equals(id));
    }
}
