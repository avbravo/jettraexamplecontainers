package com.jettra.plugin.example.admin.entity;

public record Perfil(String nombreCompleto, String avatarUrl, String zonaHoraria) {
    public Perfil(String nombreCompleto) {
        this(nombreCompleto, null, "UTC");
    }
}
