package com.jettra.plugin.example.admin.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class RolModel {
    @NotNull
    @PropertiesLabel(value = "rol.nombre", label = "Nombre del Rol")
    private String nombre;

    @PropertiesLabel(value = "rol.permisos", label = "Permisos (separados por coma)")
    private String permisos;

    public RolModel() {}

    public RolModel(String nombre, String permisos) {
        this.nombre = nombre;
        this.permisos = permisos;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPermisos() { return permisos; }
    public void setPermisos(String permisos) { this.permisos = permisos; }
}
