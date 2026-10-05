package com.jettra.plugin.example.repository;

import com.jettra.plugin.example.crud.model.PersonaModel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonaRepository {
    private static final List<PersonaModel> personas = new ArrayList<>();

    static {
        personas.add(new PersonaModel(UUID.randomUUID().toString(), "Juan Perez", "Calle Falsa 123"));
        personas.add(new PersonaModel(UUID.randomUUID().toString(), "Maria Garcia", "Av Principal 45"));
    }

    public static List<PersonaModel> findAll() {
        return new ArrayList<>(personas);
    }

    public static PersonaModel findById(String id) {
        return personas.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(PersonaModel persona) {
        if (persona.getId() == null || persona.getId().isEmpty()) {
            persona.setId(UUID.randomUUID().toString());
            personas.add(persona);
        } else {
            PersonaModel existing = findById(persona.getId());
            if (existing != null) {
                existing.setNombre(persona.getNombre());
                existing.setDireccion(persona.getDireccion());
            } else {
                personas.add(persona);
            }
        }
    }

    public static void delete(String id) {
        personas.removeIf(p -> p.getId().equals(id));
    }
}
