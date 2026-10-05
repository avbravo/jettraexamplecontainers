package com.jettra.plugin.example.rules.model;

import io.jettra.rules.annotations.Compute;
import io.jettra.rules.annotations.Rules;
import io.jettra.rules.enums.OperationType;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class ReglasModel {
    @NotNull
    @io.jettra.wui.core.annotations.Hidden
    @PropertiesLabel(value = "reglas.id", label = "ID")
    private String id;

    @NotNull
    @Rules(apply="greater", than="0", message="El saldo debe ser positivo")
    @PropertiesLabel(value = "reglas.saldo", label = "Saldo")
    private Double saldo;

    @NotNull
    @Rules(apply="lessorequals", than="saldo", message="El descuento no puede ser mayor al saldo")
    @PropertiesLabel(value = "reglas.descuento", label = "Descuento")
    private Double descuento;

    @Compute(operation=OperationType.SUBTRACTION, fields={"saldo", "descuento"}, editable=false)
    @PropertiesLabel(value = "reglas.saldoneto", label = "Saldo Neto")
    private Double saldoNeto;

    public ReglasModel() {}

    public ReglasModel(String id, Double saldo, Double descuento, Double saldoNeto) {
        this.id = id;
        this.saldo = saldo;
        this.descuento = descuento;
        this.saldoNeto = saldoNeto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public Double getSaldoNeto() { return saldoNeto; }
    public void setSaldoNeto(Double saldoNeto) { this.saldoNeto = saldoNeto; }
}
