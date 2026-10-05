package com.jettra.plugin.example.crud.repository;

import com.jettra.plugin.example.crudview.model.GrupoModel;
import java.util.ArrayList;
import java.util.List;

public class GrupoRepository {
    private static final List<GrupoModel> grupos = new ArrayList<>();

    static {
        grupos.add(new GrupoModel("G1", "Grupo de Desarrollo"));
        grupos.add(new GrupoModel("G2", "Grupo de Soporte"));
        grupos.add(new GrupoModel("G3", "Grupo de Ventas"));
    }

    public static List<GrupoModel> findAll() {
        return new ArrayList<>(grupos);
    }

    public static GrupoModel findById(String id) {
        return grupos.stream().filter(g -> g.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(GrupoModel grupo) {
        GrupoModel existing = findById(grupo.getId());
        if (existing != null) {
            existing.setName(grupo.getName());
        } else {
            grupos.add(grupo);
        }
    }

    public static void delete(String id) {
        grupos.removeIf(g -> g.getId().equals(id));
    }
}
