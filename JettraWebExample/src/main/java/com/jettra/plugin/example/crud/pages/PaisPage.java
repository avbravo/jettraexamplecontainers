package com.jettra.plugin.example.crud.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.crud.model.PaisModel;
import com.jettra.plugin.example.repository.PaisRepository;
import io.jettra.wui.complex.*;
import io.jettra.wui.components.*;
import io.jettra.wui.sync.*;
import io.jettra.server.JettraServer;
import io.jettra.core.inject.annotation.Inject;
import io.jettra.core.inject.annotation.InjectProperties;
import java.util.*;

@JettraPageSincronized(SyncType.ALL)
public class PaisPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;
    
    @Inject
    private PaisRepository paisRepository;

    private int pageNumber = 1;
    private Modal crudModal;
    private TextBox modalAction, modalCode, inputCode, inputNombre;
    private FormGroup groupCode, groupNombre;
    private Paragraph deleteMsg;
    private Button modalSubmitBtn;

    public PaisPage() {
        super("Gestión de Países (Optimizado)");
    }

    @Override
    protected void onInit(java.util.Map<String, String> params) {
        try {
            pageNumber = Integer.parseInt(params.getOrDefault("page", "1"));
        } catch (Exception e) {
        }
        super.onInit(params);
    }

    @Override
    protected void initCenter(Center center, String username) {
        setupModal();
        Card card = new Card().setTitle(msg.getProperty("subtitle.pais")).setWidth("100%");
        Datatable table = new Datatable();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add")).setId("btnAdd")
                .setBackgroundColor("#238636").setStyle("font-size", "12px")
                .addClickListener(() -> openModal("save", new PaisModel("", "")));

        io.jettra.report.Report reportConfig = new io.jettra.report.Report("Reporte de Países");
        reportConfig.setData(paisRepository.findAll());
        reportConfig.getHeader().addElement(new io.jettra.report.Report.TextElement("LISTADO DE PAÍSES"));
        
        io.jettra.report.Report.Table rtable = new io.jettra.report.Report.Table();
        rtable.addColumn(new io.jettra.report.Report.Column("CÓDIGO", "code", 100));
        rtable.addColumn(new io.jettra.report.Report.Column("NOMBRE", "name", 300));
        reportConfig.getDetail().addElement(rtable);

        reportConfig.getViewerOptions()
            .setShowViewer(true)
            .setAllowPrint(true)
            .setAllowPdf(true)
            .setAllowExcel(true)
            .setAllowWord(true)
            .setAllowCsv(true);

        Button reportBtn = new Button("📄 " + msg.getProperty("btn.report", "Reporte")).setId("btnReport")
                .setBackgroundColor("#007bff").setStyle("font-size", "12px").setStyle("margin-left", "10px");
                
        if (reportConfig.getViewerOptions().isShowViewer()) {
            reportBtn.setOnclick("document.getElementById('reportModal_pais').style.display='flex'");
        } else {
            reportBtn.setOnclick("location.href='?action=report&format=pdf'");
        }

        table.addHeaderRow(new Row(new TD(msg.getProperty("th.code", "Código")), new TD(msg.getProperty("th.name")),
                new TD().add(addBtn).add(reportBtn)));

        List<PaisModel> all = paisRepository.findAll();
        all.stream().skip((pageNumber - 1) * 10L).limit(10).forEach(p -> {
            Button editBtn = new Button("✏️").setId("btnEdit-" + p.getCode())
                    .addClickListener(() -> openModal("save", p));
            Button delBtn = new Button("🗑️").setId("btnDel-" + p.getCode()).setStyle("color", "red")
                    .addClickListener(() -> openModal("delete", p));
            table.addRow(new Row(new TD(p.getCode()).setStyle("font-family", "monospace").setStyle("font-size", "11px"),
                    new TD(p.getName()), new TD().add(editBtn).add(delBtn)));
        });

        center.add(new Div().setStyle("padding", "20px").add(card.add(table))).add(crudModal).add(reportConfig.createViewer("pais"));
    }

    @Override
    protected void onGet(java.util.Map<String, String> params) {
        if ("report".equals(params.get("action"))) {
            String format = params.get("format");
            if (format == null) format = "pdf";
            imprimirReporte(format);
        }
    }

    private void imprimirReporte(String format) {
        try {
            List<PaisModel> data = paisRepository.findAll();
            io.jettra.report.Report report = new io.jettra.report.Report("Reporte de Países");
            report.setData(data);

            // Header
            report.getHeader().addElement(new io.jettra.report.Report.TextElement("LISTADO DE PAÍSES"));

            // Table
            io.jettra.report.Report.Table table = new io.jettra.report.Report.Table();
            table.addColumn(new io.jettra.report.Report.Column("CÓDIGO", "code", 100));
            table.addColumn(new io.jettra.report.Report.Column("NOMBRE", "name", 300));
            report.getDetail().addElement(table);

            // Export and Send
            String fileName = "reporte_paises_" + System.currentTimeMillis() + "." + format;
            
            if ("pdf".equals(format)) report.exportToPdf(fileName);
            else if ("excel".equals(format)) report.exportToExcel(fileName);
            else if ("word".equals(format)) report.exportToWord(fileName);
            else if ("csv".equals(format)) report.exportToCsv(fileName);
            else report.exportToPdf(fileName); // default

            java.io.File file = new java.io.File(fileName);
            if (file.exists()) {
                byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                
                String contentType = "application/octet-stream";
                if ("pdf".equals(format)) contentType = "application/pdf";
                else if ("excel".equals(format)) contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                else if ("word".equals(format)) contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                else if ("csv".equals(format)) contentType = "text/csv";
                
                currentExchange.getResponseHeaders().set("Content-Type", contentType);
                currentExchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=" + file.getName());
                currentExchange.sendResponseHeaders(200, bytes.length);
                currentExchange.getResponseBody().write(bytes);
                currentExchange.getResponseBody().close();
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openModal(String action, PaisModel p) {
        crudModal.setDisplay("block");
        modalAction.setValue(action);
        modalCode.setValue(p.getCode());
        inputCode.setValue(p.getCode());
        inputNombre.setValue(p.getName());
        boolean isDel = "delete".equals(action);
        boolean isNew = (p.getCode() == null || p.getCode().isEmpty());
        
        // If it's a new country, the code field should be editable.
        // If it's an existing country, the code field is readonly (it's the PK).
        inputCode.setReadonly(!isNew);
        inputCode.setStyle("opacity", isNew ? "1" : "0.7");
        
        groupCode.setStyle("display", "block"); // Always show code for Pais
        groupNombre.setStyle("display", isDel ? "none" : "block");
        deleteMsg.setStyle("display", isDel ? "block" : "none");
        modalSubmitBtn.setContent(isDel ? msg.getProperty("btn.confirm.delete") : msg.getProperty("btn.save"));
        modalSubmitBtn.setBackgroundColor(isDel ? "#da3633" : "#238636");
    }


    private void setupModal() {
        crudModal = new Modal("crudModal").setMaxWidth("500px").setZIndex("9999");
        Form form = new Form("paisForm", JettraServer.resolvePath("/pais"));
        modalAction = new TextBox("hidden", "action");
        modalCode = new TextBox("hidden", "paisCode");
        form.add(modalAction).add(modalCode);

        groupCode = new FormGroup();
        groupCode.add(new Label("code", msg.getProperty("lbl.code", "Código")))
                .add(inputCode = new TextBox("text", "code"));
        form.add(groupCode);

        form.add(groupNombre = new FormGroup()).add(new Label("n", msg.getProperty("lbl.name")))
                .add(inputNombre = new TextBox("text", "nombre"));

        deleteMsg = new Paragraph(msg.getProperty("msg.confirm.delete")).setStyle("color", "#f85149")
                .setStyle("display", "none");
        form.add(deleteMsg);

        Div actions = new Div().setStyle("display", "flex").setStyle("gap", "10px").setStyle("margin-top", "20px");
        actions.add(new Button(msg.getProperty("btn.cancel")).setType("button")
                .setOnclick("document.getElementById('crudModal').style.display='none'; return false;"));
        actions.add(modalSubmitBtn = new Button(msg.getProperty("btn.save")).setType("submit")
                .setBackgroundColor("#238636"));
        crudModal.add(new Header(3, "Operación")).add(form.add(actions));
    }

    @Override
    protected void onPost(java.util.Map<String, String> params) {
        String action = params.get("action");
        String hiddenCode = params.get("paisCode");
        String inputCodeValue = params.get("code");
        String name = params.get("nombre");
        
        // The effective code is either the hidden one (edit/delete) or the input one (new)
        String finalCode = (hiddenCode == null || hiddenCode.isEmpty()) ? inputCodeValue : hiddenCode;

        if ("save".equals(action)) {
            paisRepository.save(new PaisModel(finalCode, name));
            JettraSyncManager.notifyChange("PaisModel",
                    (hiddenCode == null || hiddenCode.isEmpty()) ? SyncType.CREATE : SyncType.UPDATE, "user");
        } else if ("delete".equals(action)) {
            paisRepository.delete(finalCode);
            JettraSyncManager.notifyChange("PaisModel", SyncType.DELETE, "user");
        }
        try {
            redirect(currentExchange, JettraServer.resolvePath("/pais?page=" + pageNumber));
        } catch (Exception e) {
        }
    }
}