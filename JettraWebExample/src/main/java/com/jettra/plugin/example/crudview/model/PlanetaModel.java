
package com.jettra.plugin.example.crudview.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class PlanetaModel {
    @NotNull
    @Size(min = 2, max = 5)
    @PropertiesLabel(value = "planeta.code", label = "Código")
    private String code;
    
    @NotNull
    @Size(min = 3, max = 100)
    @PropertiesLabel(value = "planeta.name", label = "Nombre")
    private String name;

    public PlanetaModel() {}

    public PlanetaModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
