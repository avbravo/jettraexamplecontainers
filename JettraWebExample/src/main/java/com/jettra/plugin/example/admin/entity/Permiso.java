package com.jettra.plugin.example.admin.entity;

public record Permiso(
    String nombre,
    String endpoint,
    String destinoJava,
    String accion
) {
    public Permiso {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (endpoint == null || endpoint.isBlank()) throw new IllegalArgumentException("Endpoint requerido");
        if (destinoJava == null || destinoJava.isBlank()) throw new IllegalArgumentException("Destino Java requerido");
        
        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }
    }
}
