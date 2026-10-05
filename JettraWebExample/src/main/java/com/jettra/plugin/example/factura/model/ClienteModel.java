package com.jettra.plugin.example.factura.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import io.jettra.wui.core.annotations.JettraViewModel;

@JettraViewModel
public class ClienteModel {
    
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.id", label = "ID")
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.nombre", label = "Nombre Completo")
    private String nombre;
    
    @NotNull
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.email", label = "Correo Electrónico")
    private String email;
    
    @io.jettra.wui.core.annotations.PropertiesLabel(value = "lbl.telefono", label = "Teléfono")
    private String telefono;

    public ClienteModel() {}

    public ClienteModel(String id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
