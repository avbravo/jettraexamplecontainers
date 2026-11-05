package io.jettra.examples.store.factura.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo de línea de detalle de factura para JettraStore y REST endpoints.
 */
public class DetalleFactura {

    private String id;
    private String descripcion;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleFactura() {
    }

    public DetalleFactura(String id, String descripcion, int cantidad, double precioUnitario) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public DetalleFactura(String id, String descripcion, int cantidad, double precioUnitario, double subtotal) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("descripcion", descripcion);
        map.put("cantidad", cantidad);
        map.put("precioUnitario", precioUnitario);
        map.put("subtotal", subtotal);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static DetalleFactura fromMap(Map<String, Object> map) {
        if (map == null) return null;
        String id = (String) map.get("id");
        String desc = (String) map.get("descripcion");
        int cant = map.get("cantidad") instanceof Number n ? n.intValue() : 1;
        double pu = map.get("precioUnitario") instanceof Number n ? n.doubleValue() : 0.0;
        double sub = map.get("subtotal") instanceof Number n ? n.doubleValue() : (cant * pu);
        return new DetalleFactura(id, desc, cant, pu, sub);
    }
}

