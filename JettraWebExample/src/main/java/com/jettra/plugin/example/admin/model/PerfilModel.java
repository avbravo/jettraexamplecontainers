package com.jettra.plugin.example.admin.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class PerfilModel {
    @NotNull
    @PropertiesLabel(value = "perfil.nombreCompleto", label = "Nombre Completo")
    private String nombreCompleto;

    @PropertiesLabel(value = "perfil.avatarUrl", label = "URL del Avatar")
    private String avatarUrl;

    @PropertiesLabel(value = "perfil.zonaHoraria", label = "Zona Horaria")
    private String zonaHoraria;

    public PerfilModel() {}

    public PerfilModel(String nombreCompleto, String avatarUrl, String zonaHoraria) {
        this.nombreCompleto = nombreCompleto;
        this.avatarUrl = avatarUrl;
        this.zonaHoraria = zonaHoraria;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getZonaHoraria() { return zonaHoraria; }
    public void setZonaHoraria(String zonaHoraria) { this.zonaHoraria = zonaHoraria; }
}
