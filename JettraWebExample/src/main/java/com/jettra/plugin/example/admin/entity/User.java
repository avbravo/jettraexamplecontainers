    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.jettra.plugin.example.admin.entity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author avbravo
 */
public record User(
        UUID id,
        String username,
        String nombre,
        String password,
        String email,
        boolean activo,
        Perfil perfil,
        Set<Rol> rol,
        Instant fechaCreacion
        ) {

    public User {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username requerido");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email requerido");
        }
        if (fechaCreacion == null) {
            fechaCreacion = Instant.now();
        }
        rol = Set.copyOf(rol);
    }

    public boolean tieneAccesoAEndpoint(String ruta) {
        return rol.stream()
                .flatMap(rol -> rol.permisos().stream())
                .anyMatch(permiso -> permiso.endpoint().equalsIgnoreCase(ruta));
    }

    public boolean tieneAccesoAComponente(String claseDestino) {
        return rol.stream()
                .flatMap(rol -> rol.permisos().stream())
                .anyMatch(permiso -> permiso.destinoJava().equalsIgnoreCase(claseDestino));
    }
}
