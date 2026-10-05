package com.jettra.plugin.example.datatable.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.model.ArticuloModel;
import com.jettra.plugin.example.repository.ArticuloRepository;
import io.jettra.server.JettraServer;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.complex.Datatable;
import io.jettra.wui.core.UIComponent;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Showcase page for Editable Datatable with manual CRUD and JettraReport integration.
 */
public class DatatableEditablePage extends DashboardBasePage {

    public DatatableEditablePage() {
        super("DataTable Editable");
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String action = params.get("action");
        try {
            if ("add".equals(action) || "edit".equals(action)) {
                String id = params.get("id");
                if (id == null || id.trim().isEmpty()) {
                    id = String.valueOf(System.currentTimeMillis());
                }
                String nombre = params.get("nombre");
                String categoria = params.get("categoria");
                double precio = Double.parseDouble(params.get("precio"));
                int cantidad = Integer.parseInt(params.get("cantidad"));
                
                String fechaStr = params.get("fecha");
                LocalDate fecha = LocalDate.now();
                if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                    fecha = LocalDate.parse(fechaStr);
                }
                
                double total = precio * cantidad;
                ArticuloModel model = new ArticuloModel(id, nombre, categoria, precio, cantidad, fecha, total);
                ArticuloRepository.save(model);
            } else if ("delete".equals(action)) {
                String id = params.get("id");
                if (id != null) {
                    ArticuloRepository.delete(id);
                }
            }
            redirect(currentExchange, JettraServer.resolvePath("/datatableeditable"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onGet(Map<String, String> params) {
        if ("report".equals(params.get("action"))) {
            String format = params.get("format");
            if (format == null) format = "pdf";
            io.jettra.wui.mvc.JettraMVC.generateReport(this, ArticuloModel.class, ArticuloRepository.class, format, false);
        }
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Datatable Editable CRUD");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('datatable-editable-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("datatable-editable-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)");
        
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// Creating an Editable Datatable with manual CRUD\n" +
                          "Datatable table = new Datatable();\n" +
                          "table.addHeaderRow(\"Artículo\", \"Categoría\", \"Precio\", \"Cantidad\", \"Total\", \"Acciones\");\n\n" +
                          "for(ArticuloModel item : ArticuloRepository.findAll()) {\n" +
                          "    Row row = new Row();\n" +
                          "    row.add(new TD(item.getNombre()));\n" +
                          "    \n" +
                          "    SelectOne catBox = new SelectOne(\"cat_\" + item.getId());\n" +
                          "    catBox.addOption(\"tech\", \"Tecnología\");\n" +
                          "    row.add(new TD().add(catBox));\n" +
                          "    \n" +
                          "    TextBox priceBox = new TextBox(\"number\", \"price_\" + item.getId());\n" +
                          "    priceBox.setProperty(\"oninput\", \"updateRow('\" + item.getId() + \"')\");\n" +
                          "    row.add(new TD().add(priceBox));\n" +
                          "    \n" +
                          "    Button deleteBtn = new Button(\"🗑️\");\n" +
                          "    deleteBtn.setProperty(\"onclick\", \"triggerDelete('\" + item.getId() + \"')\");\n" +
                          "    row.add(new TD().add(deleteBtn));\n" +
                          "    table.addRow(row);\n" +
                          "}\n";
                           
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "dt-editable-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copiar código");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('dt-editable-java-code').innerText).then(() => { this.innerText='Copiado!'; setTimeout(() => this.innerText='Copiar código', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('datatable-editable-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---

        Paragraph p = new Paragraph("En JettraWUI, DataTable permite que las columnas sean editables integrando componentes de entrada directamente en sus celdas. Se pueden capturar eventos como 'oninput' para actualizar el estado de filas completas en tiempo real. Esta página implementa persistencia completa interactuando con ArticuloRepository y ofrece visor de reporte con JettraReport.");
        container.add(p);

        // --- Action Buttons Bar ---
        Div actionsBar = new Div();
        actionsBar.setStyle("display", "flex").setStyle("gap", "10px").setStyle("margin-bottom", "20px");
        
        Button addBtn = new Button("➕ Añadir Artículo");
        addBtn.addClass("j-btn").addClass("j-btn-primary");
        addBtn.setProperty("onclick", "openAddModal()");
        
        Button printBtn = new Button("🖨️ Imprimir / Reporte");
        printBtn.addClass("j-btn");
        printBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        printBtn.setProperty("onclick", "document.getElementById('reportModal_datatable').style.display = 'block'");
        
        actionsBar.add(addBtn).add(printBtn);
        container.add(actionsBar);

        // -- Editable Table Implementation --
        Datatable table = new Datatable();
        table.addHeaderRow("Artículo", "Categoría", "Precio Unit. ($)", "Cantidad", "Fecha de Entrega", "Total por Fila ($)", "Acciones");

        List<ArticuloModel> dataList = ArticuloRepository.findAll();
        
        for (int i = 0; i < dataList.size(); i++) {
            ArticuloModel item = dataList.get(i);
            Row row = new Row();
            row.setProperty("id", "row_" + item.getId());
            
            // Article Column
            row.add(new TD(item.getNombre()));
            
            // Category Column (SelectOne)
            SelectOne catBox = new SelectOne("cat_" + item.getId());
            catBox.setId("cat_" + item.getId());
            catBox.addOption("electronic", "Electrónica");
            catBox.addOption("accessory", "Accesorios");
            catBox.addOption("office", "Oficina");
            catBox.setProperty("value", item.getCategoria());
            catBox.setStyle("width", "120px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            catBox.setProperty("onchange", "updateCategory('" + item.getId() + "')");
            row.add(new TD().add(catBox));

            // Editable Price Column (TextBox)
            TextBox priceBox = new TextBox("number", "price_" + item.getId());
            priceBox.setId("price_" + item.getId());
            priceBox.setProperty("value", String.valueOf(item.getPrecio()));
            priceBox.setStyle("width", "100px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            priceBox.setProperty("oninput", "updateRow('" + item.getId() + "')");
            priceBox.setProperty("step", "0.01");
            row.add(new TD().add(priceBox));
            
            // Editable Quantity Column (TextBox)
            TextBox qtyBox = new TextBox("number", "qty_" + item.getId());
            qtyBox.setId("qty_" + item.getId());
            qtyBox.setProperty("value", String.valueOf(item.getCantidad()));
            qtyBox.setStyle("width", "80px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            qtyBox.setProperty("oninput", "updateRow('" + item.getId() + "')");
            qtyBox.setProperty("min", "1");
            row.add(new TD().add(qtyBox));

            // Editable Date Column (DatePicker)
            DatePicker dateBox = new DatePicker("date_" + item.getId(), "");
            dateBox.setId("date_" + item.getId());
            if (item.getFechaEntrega() != null) {
                dateBox.setValue(item.getFechaEntrega().toString());
            }
            dateBox.setStyle("width", "140px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            row.add(new TD().add(dateBox));

            // Calculated Total Column
            TD totalTd = new TD(String.valueOf(item.getTotal()));
            totalTd.setId("total_" + item.getId());
            totalTd.setStyle("font-weight", "bold").setStyle("color", "var(--jettra-accent)");
            row.add(totalTd);
            
            // Actions Column
            TD actionsTd = new TD();
            actionsTd.setStyle("display", "flex").setStyle("gap", "10px").setStyle("align-items", "center");
            
            Button editBtn = new Button("✏️");
            editBtn.addClass("j-btn");
            editBtn.setStyle("padding", "5px 10px").setStyle("font-size", "0.9rem");
            editBtn.setProperty("onclick", "openEditModal('" + item.getId() + "', '" + item.getNombre().replace("'", "\\'") + "', '" + item.getCategoria() + "', " + item.getPrecio() + ", " + item.getCantidad() + ", '" + (item.getFechaEntrega() != null ? item.getFechaEntrega().toString() : "") + "')");
            
            Button deleteBtn = new Button("🗑️");
            deleteBtn.addClass("j-btn");
            deleteBtn.setStyle("padding", "5px 10px").setStyle("font-size", "0.9rem").setStyle("border-color", "#ff4444").setStyle("color", "#ff4444");
            deleteBtn.setProperty("onclick", "triggerDelete('" + item.getId() + "')");
            
            actionsTd.add(editBtn).add(deleteBtn);
            row.add(actionsTd);
            
            table.addRow(row);
        }

        container.add(table);

        // Grand Total Section
        Div sumContainer = new Div().setStyle("margin-top", "20px").setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("padding-right", "20px");
        Header sumHeader = new Header(3, "Gran Total: $<span id='gran_total'>0.00</span>");
        sumHeader.setStyle("color", "#238636");
        sumContainer.add(sumHeader);
        container.add(sumContainer);

        // --- CRUD Modal Dialog ---
        io.jettra.wui.complex.Modal crudDialog = new io.jettra.wui.complex.Modal("datatable-crud-modal");
        crudDialog.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                  .setStyle("backdrop-filter", "blur(15px)")
                  .setStyle("padding", "25px").setStyle("border-radius", "12px")
                  .setStyle("width", "95%").setStyle("max-width", "500px")
                  .setStyle("border", "1px solid var(--jettra-border)")
                  .setStyle("z-index", "9000");
        
        Header modalTitle = new Header(3, "Añadir Artículo").setId("modal-crud-title");
        modalTitle.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)");
        crudDialog.add(modalTitle);
        
        Form crudForm = new Form("crud-form", JettraServer.resolvePath("/datatableeditable"));
        
        TextBox hiddenAction = new TextBox("hidden", "action");
        hiddenAction.setId("modal-action");
        TextBox hiddenId = new TextBox("hidden", "id");
        hiddenId.setId("modal-id");
        
        FormGroup nameGroup = new FormGroup().add(new Label("nombre", "Nombre")).add(new TextBox("text", "nombre").setId("modal-nombre").addClass("j-input"));
        
        SelectOne catSelect = new SelectOne("categoria");
        catSelect.setId("modal-categoria");
        catSelect.addClass("j-input");
        catSelect.addOption("electronic", "Electrónica");
        catSelect.addOption("accessory", "Accesorios");
        catSelect.addOption("office", "Oficina");
        FormGroup catGroup = new FormGroup().add(new Label("categoria", "Categoría")).add(catSelect);
        
        FormGroup priceGroup = new FormGroup().add(new Label("precio", "Precio Unitario ($)")).add(new TextBox("number", "precio").setId("modal-precio").addClass("j-input").setProperty("step", "0.01"));
        FormGroup qtyGroup = new FormGroup().add(new Label("cantidad", "Cantidad")).add(new TextBox("number", "cantidad").setId("modal-cantidad").addClass("j-input").setProperty("min", "1"));
        FormGroup dateGroup = new FormGroup().add(new Label("fecha", "Fecha de Entrega")).add(new DatePicker("fecha", "").setId("modal-fecha").addClass("j-input"));
        
        crudForm.add(hiddenAction).add(hiddenId).add(nameGroup).add(catGroup).add(priceGroup).add(qtyGroup).add(dateGroup);
        
        Div crudActions = new Div().setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px").setStyle("margin-top", "20px");
        Button cancelBtn = new Button("Cancelar").addClass("j-btn");
        cancelBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        cancelBtn.setProperty("type", "button");
        cancelBtn.setProperty("onclick", "document.getElementById('datatable-crud-modal').style.display = 'none'");
        
        Button saveBtn = new Button("Guardar").addClass("j-btn").addClass("j-btn-primary");
        saveBtn.setProperty("type", "submit");
        
        crudActions.add(cancelBtn).add(saveBtn);
        crudForm.add(crudActions);
        crudDialog.add(crudForm);
        container.add(crudDialog);

        // --- Native JettraReport Viewer Modal ---
        io.jettra.report.Report report = new io.jettra.report.Report("Reporte Manual de Artículos");
        report.setData(dataList);
        
        io.jettra.report.Report.Table repTable = new io.jettra.report.Report.Table();
        repTable.addColumn(new io.jettra.report.Report.Column("Artículo", "nombre", 150));
        repTable.addColumn(new io.jettra.report.Report.Column("Categoría", "categoria", 120));
        repTable.addColumn(new io.jettra.report.Report.Column("Precio ($)", "precio", 100));
        repTable.addColumn(new io.jettra.report.Report.Column("Cantidad", "cantidad", 80));
        repTable.addColumn(new io.jettra.report.Report.Column("Total ($)", "total", 100));
        
        report.getDetail().addElement(repTable);
        
        report.getViewerOptions().setShowViewer(true);
        report.getViewerOptions().setAllowPrint(true);
        report.getViewerOptions().setAllowPdf(true);
        report.getViewerOptions().setAllowExcel(true);
        report.getViewerOptions().setAllowCsv(true);
        
        io.jettra.report.ReportViewer reportViewer = report.createViewer("datatable");
        container.add(reportViewer);

        // Javascript for dynamic interactions
        String scriptContent = 
            "function updateCategory(id) {\n" +
            "    const catEl = document.getElementById('cat_' + id);\n" +
            "    const priceEl = document.getElementById('price_' + id);\n" +
            "    if (catEl && priceEl) {\n" +
            "        const cat = catEl.value;\n" +
            "        let newPrice = 0;\n" +
            "        if (cat === 'electronic') newPrice = 10000.00;\n" +
            "        else if (cat === 'accessory') newPrice = 456.00;\n" +
            "        else if (cat === 'office') newPrice = 231.00;\n" +
            "        priceEl.value = newPrice.toFixed(2);\n" +
            "        updateRow(id);\n" +
            "    }\n" +
            "}\n" +
            "function updateRow(id) {\n" +
            "    const priceEl = document.getElementById('price_' + id);\n" +
            "    const qtyEl = document.getElementById('qty_' + id);\n" +
            "    const totalEl = document.getElementById('total_' + id);\n" +
            "    if (priceEl && qtyEl && totalEl) {\n" +
            "        const price = parseFloat(priceEl.value) || 0;\n" +
            "        const qty = parseInt(qtyEl.value) || 0;\n" +
            "        const total = price * qty;\n" +
            "        totalEl.innerText = total.toFixed(2);\n" +
            "        \n" +
            "        const rowEl = document.getElementById('row_' + id);\n" +
            "        if(rowEl) {\n" +
            "            rowEl.style.backgroundColor = 'rgba(0, 255, 255, 0.1)';\n" +
            "            setTimeout(() => rowEl.style.backgroundColor = '', 500);\n" +
            "        }\n" +
            "        \n" +
            "        calculateGrandTotal();\n" +
            "    }\n" +
            "}\n" +
            "function calculateGrandTotal() {\n" +
            "    let grandTotal = 0;\n" +
            "    const rows = document.querySelectorAll('[id^=\"total_\"]');\n" +
            "    rows.forEach(row => {\n" +
            "        grandTotal += parseFloat(row.innerText) || 0;\n" +
            "    });\n" +
            "    const gtEl = document.getElementById('gran_total');\n" +
            "    if (gtEl) gtEl.innerText = grandTotal.toLocaleString('en-US', {minimumFractionDigits: 2});\n" +
            "}\n" +
            "function openAddModal() {\n" +
            "    document.getElementById('modal-crud-title').innerText = 'Añadir Artículo';\n" +
            "    document.getElementById('modal-action').value = 'add';\n" +
            "    document.getElementById('modal-id').value = '';\n" +
            "    document.getElementById('modal-nombre').value = '';\n" +
            "    document.getElementById('modal-categoria').value = 'electronic';\n" +
            "    document.getElementById('modal-precio').value = '0.00';\n" +
            "    document.getElementById('modal-cantidad').value = '1';\n" +
            "    document.getElementById('modal-fecha').value = new Date().toISOString().split('T')[0];\n" +
            "    document.getElementById('datatable-crud-modal').style.display = 'block';\n" +
            "}\n" +
            "function openEditModal(id, nombre, categoria, precio, cantidad, fecha) {\n" +
            "    document.getElementById('modal-crud-title').innerText = 'Editar Artículo';\n" +
            "    document.getElementById('modal-action').value = 'edit';\n" +
            "    document.getElementById('modal-id').value = id;\n" +
            "    document.getElementById('modal-nombre').value = nombre;\n" +
            "    document.getElementById('modal-categoria').value = categoria;\n" +
            "    document.getElementById('modal-precio').value = precio;\n" +
            "    document.getElementById('modal-cantidad').value = cantidad;\n" +
            "    document.getElementById('modal-fecha').value = fecha;\n" +
            "    document.getElementById('datatable-crud-modal').style.display = 'block';\n" +
            "}\n" +
            "function triggerDelete(id) {\n" +
            "    if (confirm('¿Está seguro de que desea eliminar este artículo?')) {\n" +
            "        const form = document.createElement('form');\n" +
            "        form.method = 'POST';\n" +
            "        form.action = '" + JettraServer.resolvePath("/datatableeditable") + "';\n" +
            "        \n" +
            "        const actionInput = document.createElement('input');\n" +
            "        actionInput.type = 'hidden';\n" +
            "        actionInput.name = 'action';\n" +
            "        actionInput.value = 'delete';\n" +
            "        \n" +
            "        const idInput = document.createElement('input');\n" +
            "        idInput.type = 'hidden';\n" +
            "        idInput.name = 'id';\n" +
            "        idInput.value = id;\n" +
            "        \n" +
            "        form.appendChild(actionInput);\n" +
            "        form.appendChild(idInput);\n" +
            "        document.body.appendChild(form);\n" +
            "        form.submit();\n" +
            "    }\n" +
            "}\n" +
            "// Run once on load to initialize Grand Total\n" +
            "document.addEventListener('DOMContentLoaded', calculateGrandTotal);";
            
        io.jettra.wui.components.Script js = new io.jettra.wui.components.Script(scriptContent);
        container.add(js);

        center.add(container);
    }
}
