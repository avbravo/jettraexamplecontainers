package com.jettra.plugin.example.repository;

import com.jettra.plugin.example.model.CategoriaModel;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private static final List<CategoriaModel> data = new ArrayList<>();

    static {
        data.add(new CategoriaModel("electronic", "Electrónica"));
        data.add(new CategoriaModel("accessory", "Accesorios"));
        data.add(new CategoriaModel("office", "Oficina"));
    }

    public static List<CategoriaModel> findAll() {
        return new ArrayList<>(data);
    }

    public static CategoriaModel findById(String id) {
        return data.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }
}
