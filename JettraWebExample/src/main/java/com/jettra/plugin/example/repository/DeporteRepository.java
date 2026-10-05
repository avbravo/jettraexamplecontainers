package com.jettra.plugin.example.repository;

import com.jettra.plugin.example.crud.model.DeporteModel;
import java.util.ArrayList;
import java.util.List;

public class DeporteRepository {
    private static final List<DeporteModel> deportes = new ArrayList<>();

    static {
        deportes.add(new DeporteModel("FUT", "Fútbol"));
        deportes.add(new DeporteModel("TEN", "Tenis"));
        deportes.add(new DeporteModel("NAT", "Natación"));
    }

    public static List<DeporteModel> findAll() {
        return new ArrayList<>(deportes);
    }

    public static DeporteModel findByCode(String code) {
        return deportes.stream().filter(d -> d.getCode().equals(code)).findFirst().orElse(null);
    }

    public static void save(DeporteModel deporte) {
        DeporteModel existing = findByCode(deporte.getCode());
        if (existing != null) {
            existing.setDeporte(deporte.getDeporte());
        } else {
            deportes.add(deporte);
        }
    }

    public static void delete(String code) {
        deportes.removeIf(d -> d.getCode().equals(code));
    }
}
