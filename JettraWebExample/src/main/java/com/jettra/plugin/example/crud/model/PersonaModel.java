package com.jettra.plugin.example.crud.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;

public class PersonaModel {
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.id", label = "ID")
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.name", label = "Nombre")
    private String nombre;
    
    @NotNull
    @Size(min = 5, max = 200)
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.address", label = "Dirección")
    private String direccion;

    public PersonaModel() {}

    public PersonaModel(String id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
