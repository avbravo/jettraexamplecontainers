package com.jettra.plugin.example.admin.entity;

import java.util.Set;

public record Rol(String nombre, Set<Permiso> permisos) {
    public Rol {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre de rol requerido");
        permisos = Set.copyOf(permisos);
    }
}
