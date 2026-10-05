package com.jettra.plugin.example.admin.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class PermisoModel {
    @NotNull
    @PropertiesLabel(value = "permiso.nombre", label = "Nombre")
    private String nombre;

    @NotNull
    @PropertiesLabel(value = "permiso.endpoint", label = "Endpoint")
    private String endpoint;

    @NotNull
    @PropertiesLabel(value = "permiso.destino", label = "Destino Java")
    private String destinoJava;

    @NotNull
    @PropertiesLabel(value = "permiso.accion", label = "Acción")
    private String accion;

    public PermisoModel() {}

    public PermisoModel(String nombre, String endpoint, String destinoJava, String accion) {
        this.nombre = nombre;
        this.endpoint = endpoint;
        this.destinoJava = destinoJava;
        this.accion = accion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getDestinoJava() { return destinoJava; }
    public void setDestinoJava(String destinoJava) { this.destinoJava = destinoJava; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
}
