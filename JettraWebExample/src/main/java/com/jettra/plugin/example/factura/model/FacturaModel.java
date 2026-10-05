package com.jettra.plugin.example.factura.model;

import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.report.annotations.ModelReportDisabledHeader;
import io.jettra.report.annotations.ModelReportFooter;
import io.jettra.report.annotations.ModelReportHeader;
import io.jettra.report.annotations.ModelReportLabel;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.ViewDataTable;
import io.jettra.wui.core.annotations.ViewSelectOne;
import java.time.LocalDate;
import java.util.List;

@JettraViewModel
@ModelReportDisabledHeader
@ModelReportHeader(value = "REPORTE DE FACTURAS DE VENTAS (MASTER)", type = ModelReportHeader.ReportType.MASTER, orientation = ModelReportHeader.Orientation.CENTER, size = 16, textColor = "#1f6feb", style = {ModelReportHeader.Style.BOLD})
@ModelReportHeader(value = "REPORTE DE FACTURAS DE VENTAS (MASTER- DETALLE)", type = ModelReportHeader.ReportType.DETAILS, orientation = ModelReportHeader.Orientation.CENTER, size = 16, textColor = "#1f6feb", style = {ModelReportHeader.Style.BOLD})
@ModelReportHeader(value = "DETALLES COMPLEMENTARIOS DE FACTURAS", type = ModelReportHeader.ReportType.DETAILS, orientation = ModelReportHeader.Orientation.LEFT, size = 12, textColor = "#ff5500", style = {ModelReportHeader.Style.ITALIC})
@ModelReportFooter(value = "Página generada por el motor de reportes nativo JettraReport", type = ModelReportFooter.ReportType.MASTER, orientation = ModelReportFooter.Orientation.CENTER, size = 8, textColor = "#555555")
@ModelReportFooter(value = "Confidencial - Solo para uso interno", type = ModelReportFooter.ReportType.DETAILS, orientation = ModelReportFooter.Orientation.RIGHT, size = 8, textColor = "#ff0000", style = {ModelReportFooter.Style.BOLD})
public class FacturaModel {
    @NotNull
    @PropertiesLabel(value = "factura.id", label = "ID Factura")
    @ModelReportLabel(label = "Factura ID", section = ModelReportLabel.Section.HEADER, orientation = ModelReportLabel.Orientation.LEFT, textColor = "#0000FF", style = {ModelReportLabel.Style.BOLD, ModelReportLabel.Style.SUBLINE})
    private Long idFactura;


    @NotNull
    @ViewSelectOne(label = "nombre", fieldOnlyMasterTable = "nombre", source = "ClienteRepository", method = "findAll")
    @PropertiesLabel(value = "factura.cliente", label = "Cliente")
    @ModelReportLabel(label = "Cliente Beneficiario", section = ModelReportLabel.Section.HEADER, orientation = ModelReportLabel.Orientation.LEFT, font = "Helvetica", size = 12, style = {ModelReportLabel.Style.ITALIC})
    private ClienteModel clienteModel;
    
    
    @NotNull
    @PropertiesLabel(value = "factura.fechaEmision", label = "Fecha de Emisión")
    private LocalDate fechaEmision;

    @ViewDataTable(editableRowMaster = false,showRowInMasterTable = false,row="productoId, precio, cantidad, total", editablerow="productoId, cantidad", source="FacturaRepository", method="getLineas")
    @PropertiesLabel(value = "factura.lineas", label = "Detalle de Líneas")
    private List<LineaFacturaModel> lineaFacturaModel;

    @PropertiesLabel(value = "factura.porcentajeImpuesto", label = "Porcentaje Impuesto (%)")
    @ModelReportLabel(label = "Porcentaje Impuesto (%)", section = ModelReportLabel.Section.LASTPAGE, orientation = ModelReportLabel.Orientation.RIGHT)
    private Double porcentajeImpuesto;

    @PropertiesLabel(value = "factura.subtotal", label = "Subtotal")
     @ModelReportLabel(label = "Sub Total", section = ModelReportLabel.Section.LASTPAGE, orientation = ModelReportLabel.Orientation.RIGHT)
    private Double subtotal;

    @PropertiesLabel(value = "factura.descuento", label = "Descuento")
    @ModelReportLabel(label = "Descuento Aplicado", section = ModelReportLabel.Section.LASTPAGE, orientation = ModelReportLabel.Orientation.RIGHT, textColor = "#FF0000", style = {ModelReportLabel.Style.STRIKETHROUGH})
    private Double descuento;

    @PropertiesLabel(value = "factura.total", label = "Total")
    @ModelReportLabel(label = "Gran Total Factura", section = ModelReportLabel.Section.LASTPAGE, orientation = ModelReportLabel.Orientation.RIGHT, size = 14, style = {ModelReportLabel.Style.BOLD, ModelReportLabel.Style.SUBLINE})
    private Double total;

    public FacturaModel() {}

    public FacturaModel(Long idFactura, ClienteModel clienteModel, LocalDate fechaEmision, List<LineaFacturaModel> lineaFacturaModel, Double porcentajeImpuesto, Double subtotal, Double descuento, Double total) {
        this.idFactura = idFactura;
        this.clienteModel = clienteModel;
        this.fechaEmision = fechaEmision;
        this.lineaFacturaModel = lineaFacturaModel;
        this.porcentajeImpuesto = porcentajeImpuesto;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public ClienteModel getClienteModel() {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel) {
        this.clienteModel = clienteModel;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<LineaFacturaModel> getLineaFacturaModel() {
        return lineaFacturaModel;
    }

    public void setLineaFacturaModel(List<LineaFacturaModel> lineaFacturaModel) {
        this.lineaFacturaModel = lineaFacturaModel;
    }

    public Double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(Double porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    

    
    
}
