package com.jettra.plugin.example.crudview.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class GrupoModel {
    @NotNull
    @Size(min = 2, max = 5)
    @PropertiesLabel(value = "lbl.id", label = "ID")
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    @PropertiesLabel(value = "lbl.name", label = "Nombre Grupo")
    private String name;

    public GrupoModel() {}

    public GrupoModel(String code, String name) {
        this.id = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
