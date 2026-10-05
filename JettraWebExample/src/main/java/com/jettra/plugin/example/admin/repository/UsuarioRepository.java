    package com.jettra.plugin.example.admin.repository;

import com.jettra.plugin.example.admin.model.UsuarioModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository {
    private static final List<UsuarioModel> list = new ArrayList<>();

    static {
        list.add(new UsuarioModel(UUID.randomUUID().toString(), "admin", "Administrador del Sistema", "admin@jettra.com", true, "Aristides Villarreal", "ADMIN"));
        list.add(new UsuarioModel(UUID.randomUUID().toString(), "demo", "Usuario de Demostración", "demo@jettra.com", true, "Demo User", "USER"));
    }

    public static List<UsuarioModel> findAll() {
        return list;
    }

    public static Optional<UsuarioModel> findById(String id) {
        return list.stream().filter(x -> x.getId() != null && x.getId().equals(id)).findFirst();
    }

    public static void save(UsuarioModel model) {
        if (model.getId() == null || model.getId().isEmpty()) {
            model.setId(UUID.randomUUID().toString());
        } else {
            delete(model.getId());
        }
        list.add(model);
    }

    public static void delete(String id) {
        list.removeIf(x -> x.getId() != null && x.getId().equals(id));
    }
}
