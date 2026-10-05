package com.jettra.plugin.example.factura.repository;

import com.jettra.plugin.example.factura.model.ClienteModel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClienteRepository {
    private static final List<ClienteModel> clientes = new ArrayList<>();

    static {
        clientes.add(new ClienteModel(UUID.randomUUID().toString(), "Carlos Sanchez", "carlos@example.com", "555-0101"));
        clientes.add(new ClienteModel(UUID.randomUUID().toString(), "Laura Gomez", "laura@example.com", "555-0102"));
    }

    public static List<ClienteModel> findAll() {
        return new ArrayList<>(clientes);
    }

    public static ClienteModel findById(String id) {
        return clientes.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(ClienteModel cliente) {
        if (cliente.getId() == null || cliente.getId().isEmpty()) {
            cliente.setId(UUID.randomUUID().toString());
            clientes.add(cliente);
        } else {
            ClienteModel existing = findById(cliente.getId());
            if (existing != null) {
                existing.setNombre(cliente.getNombre());
                existing.setEmail(cliente.getEmail());
                existing.setTelefono(cliente.getTelefono());
            } else {
                clientes.add(cliente);
            }
        }
    }

    public static void delete(String id) {
        clientes.removeIf(c -> c.getId().equals(id));
    }
}
