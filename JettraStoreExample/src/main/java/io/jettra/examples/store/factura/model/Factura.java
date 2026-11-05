package io.jettra.examples.store.factura.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo de Factura para la base de datos empresarial 'example_factura_db' en JettraStore.
 */
public class Factura {

    private String id;
    private String numero;
    private String fecha;
    private String clienteId;
    private String clienteNombre;
    private double subtotal;
    private double impuesto;
    private double total;
    private String estado;
    private List<DetalleFactura> items = new ArrayList<>();

    public Factura() {
    }

    public Factura(String id, String numero, String fecha, String clienteId, String clienteNombre,
                   double subtotal, double impuesto, double total, String estado) {
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleFactura> getItems() {
        return items;
    }

    public void setItems(List<DetalleFactura> items) {
        this.items = (items != null) ? items : new ArrayList<>();
    }

    public void addItem(DetalleFactura item) {
        if (this.items == null) this.items = new ArrayList<>();
        this.items.add(item);
        recalcularTotales();
    }

    public void recalcularTotales() {
        double sub = 0.0;
        if (items != null) {
            for (DetalleFactura it : items) {
                sub += it.getSubtotal();
            }
        }
        this.subtotal = sub;
        this.impuesto = Math.round(sub * 0.07 * 100.0) / 100.0; // 7% ITBMS
        this.total = this.subtotal + this.impuesto;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("_id", id != null ? id : "");
        map.put("id", id != null ? id : "");
        map.put("numero", numero);
        map.put("fecha", fecha);
        map.put("cliente_id", clienteId);
        map.put("cliente_nombre", clienteNombre);
        map.put("subtotal", subtotal);
        map.put("impuesto", impuesto);
        map.put("total", total);
        map.put("estado", estado);

        if (items != null && !items.isEmpty()) {
            List<Map<String, Object>> itemsList = new ArrayList<>();
            for (DetalleFactura it : items) {
                itemsList.add(it.toMap());
            }
            map.put("items", itemsList);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Factura fromMap(Map<String, Object> map) {
        if (map == null) return null;
        Factura f = new Factura();
        Object rawId = map.containsKey("_id") ? map.get("_id") : map.get("id");
        f.setId(rawId != null ? String.valueOf(rawId) : null);
        f.setNumero((String) map.get("numero"));
        f.setFecha((String) map.get("fecha"));
        f.setClienteId((String) (map.containsKey("cliente_id") ? map.get("cliente_id") : map.get("clienteId")));
        f.setClienteNombre((String) (map.containsKey("cliente_nombre") ? map.get("cliente_nombre") : map.get("clienteNombre")));

        f.setSubtotal(map.get("subtotal") instanceof Number n ? n.doubleValue() : 0.0);
        f.setImpuesto(map.get("impuesto") instanceof Number n ? n.doubleValue() : 0.0);
        f.setTotal(map.get("total") instanceof Number n ? n.doubleValue() : 0.0);
        f.setEstado((String) map.get("estado"));

        Object rawItems = map.get("items");
        if (rawItems instanceof List<?> list) {
            for (Object obj : list) {
                if (obj instanceof Map<?, ?> itMap) {
                    f.getItems().add(DetalleFactura.fromMap((Map<String, Object>) itMap));
                }
            }
        }
        return f;
    }
}

