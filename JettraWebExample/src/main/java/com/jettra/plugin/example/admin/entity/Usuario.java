package com.jettra.plugin.example.admin.entity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record Usuario(
    UUID id,
    String username,
    String nombre,
    String password,
    String email,
    boolean activo,
    Perfil perfil,
    Set<Rol> roles,
    Instant fechaCreacion
) {
    public Usuario {
        if (id == null) id = UUID.randomUUID();
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username requerido");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email requerido");
        if (fechaCreacion == null) fechaCreacion = Instant.now();
        roles = Set.copyOf(roles);
    }

    public boolean tieneAccesoAEndpoint(String ruta) {
        return roles.stream()
                .flatMap(rol -> rol.permisos().stream())
                .anyMatch(permiso -> permiso.endpoint().equalsIgnoreCase(ruta));
    }

    public boolean tieneAccesoAComponente(String claseDestino) {
        return roles.stream()
                .flatMap(rol -> rol.permisos().stream())
                .anyMatch(permiso -> permiso.destinoJava().equalsIgnoreCase(claseDestino));
    }
}
