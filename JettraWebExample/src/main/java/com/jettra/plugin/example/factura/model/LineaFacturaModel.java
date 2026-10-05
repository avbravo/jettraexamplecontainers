package com.jettra.plugin.example.factura.model;

import io.jettra.rules.annotations.Compute;
import io.jettra.rules.enums.OperationType;
import io.jettra.wui.core.annotations.Hidden;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.ViewSelectOne;
import io.jettra.rules.annotations.Rules;
import io.jettra.rules.validations.NotNull;

@JettraViewModel
public class LineaFacturaModel { 
    @NotNull
    @Hidden
    @PropertiesLabel(value = "lineafactura.id", label = "ID")
    private Long id;

    @NotNull
    @ViewSelectOne(label = "nombre", source = "ArticuloRepository", method = "findAll")
    @PropertiesLabel(value = "lbl.producto", label = "Producto")
    private String productoId; // Storing the ID or Name
    
    @NotNull
    @Rules(apply="greater", than="0", message="El precio debe ser mayor a 0")
    @PropertiesLabel(value = "lbl.precio", label = "Precio Unit.($)")
    private Double precio;  
    
    @Rules(apply="greater", than="0", message="La cantidad debe ser mayor a 0")
    @PropertiesLabel(value = "lbl.cantidad", label = "Cantidad")
    private Integer cantidad;
    
    @Compute(operation=OperationType.MULT, fields={"precio", "cantidad"}, editable=false)
    @PropertiesLabel(value = "lineafactura.total", label = "Total ($)")
    private Double total;

    public LineaFacturaModel() {}

    public LineaFacturaModel(Long id, String productoId, Double precio, Integer cantidad, Double total) {
        this.id = id;
        this.productoId = productoId;
        this.precio = precio;
        this.cantidad = cantidad;
        this.total = total;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
