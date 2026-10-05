/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jettra.plugin.example.crud.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

/**
 *
 * @author avbravo
 */
@JettraViewModel
public class DeporteModel {
     @NotNull
    @Size(min = 2, max = 5)
    @PropertiesLabel(value = "deporte.code", label = "Código")
    private String code;
    
    @NotNull
    @Size(min = 3, max = 100)
    @PropertiesLabel(value = "deporte.name", label = "Deporte")
    private String deporte;

    public DeporteModel() {
    }

    public DeporteModel(String code, String deporte) {
        this.code = code;
        this.deporte = deporte;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    @Override
    public String toString() {
        return "DeporteModel{" + "code=" + code + ", deporte=" + deporte + '}';
    }
    
    
    
    
    
}
