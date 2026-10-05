package com.jettra.plugin.example.factura.repository;

import com.jettra.plugin.example.factura.model.ClienteModel;
import com.jettra.plugin.example.factura.model.FacturaModel;
import com.jettra.plugin.example.factura.model.LineaFacturaModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturaRepository {
    private static final List<FacturaModel> data = new ArrayList<>();

    static {
        List<LineaFacturaModel> lineas1 = new ArrayList<>();
        lineas1.add(new LineaFacturaModel(1L, "Laptop Dell", 800.0, 2, 1600.0));
        lineas1.add(new LineaFacturaModel(2L, "Mouse Logi", 25.0, 1, 25.0));
        
        ClienteModel cliente = ClienteRepository.findAll().isEmpty() ? 
            new ClienteModel("1", "Juan Pérez", "juan@example.com", "555-0000") : 
            ClienteRepository.findAll().get(0);

        data.add(new FacturaModel(10245L, cliente, LocalDate.of(2026, 5, 18), lineas1, 15.0, 1625.0, 0.0, 1868.75));
    }

    public static List<FacturaModel> findAll() {
        return new ArrayList<>(data);
    }

    public static FacturaModel findById(String id) {
        Long idLong = Long.parseLong(id);
        return data.stream().filter(a -> a.getIdFactura().equals(idLong)).findFirst().orElse(null);
    }

    public static void save(FacturaModel model) {
        FacturaModel existing = findById(model.getIdFactura().toString());
        if (existing != null) {
            existing.setClienteModel(model.getClienteModel());
            existing.setFechaEmision(model.getFechaEmision());
            existing.setLineaFacturaModel(model.getLineaFacturaModel());
            existing.setPorcentajeImpuesto(model.getPorcentajeImpuesto());
            existing.setSubtotal(model.getSubtotal());
            existing.setDescuento(model.getDescuento());
            existing.setTotal(model.getTotal());
        } else {
            data.add(model);
        }
    }

    public static void delete(String id) {
        Long idLong = Long.parseLong(id);
        data.removeIf(a -> a.getIdFactura().equals(idLong));
    }
    
    public static List<LineaFacturaModel> getLineas() {
        if (!data.isEmpty()) {
            return data.get(0).getLineaFacturaModel();
        }
        return new ArrayList<>();
    }
}
