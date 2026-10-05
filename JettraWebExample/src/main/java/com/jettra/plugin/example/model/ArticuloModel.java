package com.jettra.plugin.example.model;

import io.jettra.rules.annotations.Compute;
import io.jettra.rules.annotations.Rules;
import io.jettra.rules.enums.OperationType;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.*;
import java.time.LocalDate;

@JettraViewModel
public class ArticuloModel {
    @NotNull
    @Hidden
    @PropertiesLabel(value = "articulo.id", label = "ID")
    private String id;

    @NotNull
    @NoEditable
    @PropertiesLabel(value = "articulo.nombre", label = "Artículo")
    private String nombre;

    @NotNull
    @ViewSelectOne(label = "nombre", source = "CategoriaRepository", method = "findAll")
    @PropertiesLabel(value = "articulo.categoria", label = "Categoría")
    private String categoria;

    @NotNull
    @Rules(apply="greaterorequals", than="0", message="El precio unitario debe ser mayor o igual a 0")
    @PropertiesLabel(value = "articulo.precio", label = "Precio Unit. ($)")
    private Double precio;

    @NotNull
    @Rules(apply="greater", than="0", message="La cantidad debe ser mayor a 0")
    @PropertiesLabel(value = "articulo.cantidad", label = "Cantidad")
    private Integer cantidad;

    @NotNull
    @PropertiesLabel(value = "articulo.fechaEntrega", label = "Fecha de Entrega")
    private LocalDate fechaEntrega;

    @Compute(operation=OperationType.MULT, fields={"precio", "cantidad"}, editable=false)
    @PropertiesLabel(value = "articulo.total", label = "Total por Fila ($)")
    private Double total;

    public ArticuloModel() {}

    public ArticuloModel(String id, String nombre, String categoria, Double precio, Integer cantidad, LocalDate fechaEntrega, Double total) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fechaEntrega = fechaEntrega;
        this.total = total;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
