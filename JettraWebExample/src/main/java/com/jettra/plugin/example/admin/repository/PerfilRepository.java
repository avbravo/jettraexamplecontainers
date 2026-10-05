package com.jettra.plugin.example.admin.repository;

import com.jettra.plugin.example.admin.model.PerfilModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PerfilRepository {
    private static final List<PerfilModel> list = new ArrayList<>();

    static {
        list.add(new PerfilModel("Aristides Villarreal", "/images/avatar1.png", "America/Panama"));
        list.add(new PerfilModel("Demo User", "/images/avatar2.png", "UTC"));
    }

    public static List<PerfilModel> findAll() {
        return list;
    }

    public static Optional<PerfilModel> findById(String nombreCompleto) {
        return list.stream().filter(x -> x.getNombreCompleto() != null && x.getNombreCompleto().equals(nombreCompleto)).findFirst();
    }

    public static void save(PerfilModel model) {
        if (model.getNombreCompleto() != null) {
            delete(model.getNombreCompleto());
            list.add(model);
        }
    }

    public static void delete(String nombreCompleto) {
        list.removeIf(x -> x.getNombreCompleto() != null && x.getNombreCompleto().equals(nombreCompleto));
    }
}
