package com.jettra.plugin.example.admin.repository;

import com.jettra.plugin.example.admin.model.RolModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolRepository {
    private static final List<RolModel> list = new ArrayList<>();

    static {
        list.add(new RolModel("ADMIN", "LEER_USUARIOS,ESCRIBIR_USUARIOS,LEER_ROLES,ESCRIBIR_ROLES"));
        list.add(new RolModel("USER", "LEER_USUARIOS"));
    }

    public static List<RolModel> findAll() {
        return list;
    }

    public static Optional<RolModel> findById(String nombre) {
        return list.stream().filter(x -> x.getNombre() != null && x.getNombre().equals(nombre)).findFirst();
    }

    public static void save(RolModel model) {
        if (model.getNombre() != null) {
            delete(model.getNombre());
            list.add(model);
        }
    }

    public static void delete(String nombre) {
        list.removeIf(x -> x.getNombre() != null && x.getNombre().equals(nombre));
    }
}
