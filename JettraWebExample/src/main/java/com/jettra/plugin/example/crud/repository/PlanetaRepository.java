package com.jettra.plugin.example.crud.repository;

import com.jettra.plugin.example.crudview.model.PlanetaModel;
import java.util.ArrayList;
import java.util.List;

public class PlanetaRepository {
    private static final List<PlanetaModel> planetas = new ArrayList<>();

    static {
        planetas.add(new PlanetaModel("MER", "Mercurio"));
        planetas.add(new PlanetaModel("VEN", "Venus"));
        planetas.add(new PlanetaModel("TIE", "Tierra"));
        planetas.add(new PlanetaModel("MAR", "Marte"));
    }

    public static List<PlanetaModel> findAll() {
        return new ArrayList<>(planetas);
    }

    public static PlanetaModel findByCode(String code) {
        return planetas.stream().filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
    }

    public static void save(PlanetaModel planeta) {
        PlanetaModel existing = findByCode(planeta.getCode());
        if (existing != null) {
            existing.setName(planeta.getName());
        } else {
            planetas.add(planeta);
        }
    }

    public static void delete(String code) {
        planetas.removeIf(p -> p.getCode().equals(code));
    }
}
