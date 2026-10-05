package com.jettra.plugin.example.crud.repository;

import com.jettra.plugin.example.crud.repository.GrupoRepository;
import com.jettra.plugin.example.crud.model.DeporteModel;
import com.jettra.plugin.example.crudview.model.SubGrupoModel;
import com.jettra.plugin.example.crudview.model.GrupoModel;
import java.util.ArrayList;
import java.util.List;

public class SubGrupoRepository {
    private static final List<SubGrupoModel> subgrupos = new ArrayList<>();

    static {
        List<GrupoModel> grupos = GrupoRepository.findAll();
        subgrupos.add(new SubGrupoModel("SG1", "Java Backend", grupos.get(0), List.of(new DeporteModel("FUT", "Fútbol"))));
        subgrupos.add(new SubGrupoModel("SG2", "Vue Frontend", grupos.get(0),  List.of(new DeporteModel("TEN", "Tenis"))));
        subgrupos.add(new SubGrupoModel("SG3", "Help Desk", grupos.get(1), List.of(new DeporteModel("NAT", "Natación"))));
    }

    public static List<SubGrupoModel> findAll() {
        return new ArrayList<>(subgrupos);
    }

    public static SubGrupoModel findById(String id) {
        return subgrupos.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(SubGrupoModel subgrupo) {
        SubGrupoModel existing = findById(subgrupo.getId());
        if (existing != null) {
            existing.setName(subgrupo.getName());
            existing.setGrupoModel(subgrupo.getGrupoModel());
            existing.setDeportesModel(subgrupo.getDeportesModel());
        } else {
            subgrupos.add(subgrupo);
        }
    }

    public static void delete(String id) {
        subgrupos.removeIf(s -> s.getId().equals(id));
    }
}
