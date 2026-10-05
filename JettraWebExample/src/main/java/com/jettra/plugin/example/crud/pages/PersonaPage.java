package com.jettra.plugin.example.crud.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.crud.model.PersonaModel;
import com.jettra.plugin.example.repository.PersonaRepository;
import io.jettra.wui.complex.*;
import io.jettra.wui.components.*;
import io.jettra.wui.sync.*;
import io.jettra.server.JettraServer;
import io.jettra.core.inject.annotation.InjectProperties;
import java.util.*;

@JettraPageSincronized(SyncType.ALL)
public class PersonaPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;
    private int pageNumber = 1;
    private Modal crudModal;
    private TextBox modalAction, modalId, inputIdVisible, inputNombre, inputDireccion;
    private FormGroup groupId, groupNombre, groupDireccion;
    private Paragraph deleteMsg;
    private Button modalSubmitBtn;

    public PersonaPage() {
        super("Gestión de Personas (Optimizado)");
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
        Card card = new Card().setTitle(msg.getProperty("subtitle.persona")).setWidth("100%");
        Datatable table = new Datatable();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add")).setId("btnAdd")
                .setBackgroundColor("#238636").setStyle("font-size", "12px")
                .addClickListener(() -> openModal("save", new PersonaModel("", "", "")));

        io.jettra.report.Report reportConfig = new io.jettra.report.Report("Reporte de Personas");
        reportConfig.setData(PersonaRepository.findAll());
        reportConfig.getPageSettings().setOrientation(io.jettra.report.Report.PageSettings.Orientation.LANDSCAPE);
        reportConfig.getHeader().addElement(new io.jettra.report.Report.TextElement("LISTADO DE PERSONAS"));
        
        io.jettra.report.Report.Table rtable = new io.jettra.report.Report.Table();
        rtable.addColumn(new io.jettra.report.Report.Column("ID", "id", 80)
            .setFontColor("#FF0000").setBold(true).setFontName("Arial").setFontSize(10));
        rtable.addColumn(new io.jettra.report.Report.Column("NOMBRE", "nombre", 250));
        rtable.addColumn(new io.jettra.report.Report.Column("DIRECCIÓN", "direccion", 350));
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
            reportBtn.setOnclick("document.getElementById('reportModal_persona').style.display='flex'");
        } else {
            reportBtn.setOnclick("location.href='?action=report&format=pdf'");
        }

        table.addHeaderRow(new Row(new TD(msg.getProperty("th.id", "ID")), new TD(msg.getProperty("th.name")),
                new TD(msg.getProperty("th.address")), new TD().add(addBtn).add(reportBtn)));

        List<PersonaModel> all = PersonaRepository.findAll();
        all.stream().skip((pageNumber - 1) * 10L).limit(10).forEach(p -> {
            Button editBtn = new Button("✏️").setId("btnEdit-" + p.getId())
                    .addClickListener(() -> openModal("save", p));
            Button delBtn = new Button("🗑️").setId("btnDel-" + p.getId()).setStyle("color", "red")
                    .addClickListener(() -> openModal("delete", p));
            table.addRow(new Row(new TD(p.getId()).setStyle("font-family", "monospace").setStyle("font-size", "11px"),
                    new TD(p.getNombre()), new TD(p.getDireccion()), new TD().add(editBtn).add(delBtn)));
        });

        center.add(new Div().setStyle("padding", "20px").add(card.add(table))).add(crudModal).add(reportConfig.createViewer("persona"));
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
            List<PersonaModel> data = PersonaRepository.findAll();
            io.jettra.report.Report report = new io.jettra.report.Report("Reporte de Personas");
            report.setData(data);
            report.getPageSettings().setOrientation(io.jettra.report.Report.PageSettings.Orientation.LANDSCAPE);

            report.getHeader().addElement(new io.jettra.report.Report.TextElement("LISTADO DE PERSONAS"));

            io.jettra.report.Report.Table table = new io.jettra.report.Report.Table();
            table.addColumn(new io.jettra.report.Report.Column("ID", "id", 80)
                .setFontColor("#FF0000").setBold(true).setFontName("Arial").setFontSize(10));
            table.addColumn(new io.jettra.report.Report.Column("NOMBRE", "nombre", 250));
            table.addColumn(new io.jettra.report.Report.Column("DIRECCIÓN", "direccion", 350));
            report.getDetail().addElement(table);

            String fileName = "reporte_personas_" + System.currentTimeMillis() + "." + format;
            
            if ("pdf".equals(format)) report.exportToPdf(fileName);
            else if ("excel".equals(format)) report.exportToExcel(fileName);
            else if ("word".equals(format)) report.exportToWord(fileName);
            else if ("csv".equals(format)) report.exportToCsv(fileName);
            else report.exportToPdf(fileName);

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

    private void openModal(String action, PersonaModel p) {
        crudModal.setDisplay("block");
        modalAction.setValue(action);
        modalId.setValue(p.getId());
        inputIdVisible.setValue(p.getId());
        inputNombre.setValue(p.getNombre());
        inputDireccion.setValue(p.getDireccion());
        boolean isDel = "delete".equals(action);
        groupId.setStyle("display", (p.getId() == null || p.getId().isEmpty()) ? "none" : "block");
        groupNombre.setStyle("display", isDel ? "none" : "block");
        groupDireccion.setStyle("display", isDel ? "none" : "block");
        deleteMsg.setStyle("display", isDel ? "block" : "none");
        modalSubmitBtn.setContent(isDel ? msg.getProperty("btn.confirm.delete") : msg.getProperty("btn.save"));
        modalSubmitBtn.setBackgroundColor(isDel ? "#da3633" : "#238636");
    }



    private void setupModal() {
        crudModal = new Modal("crudModal").setMaxWidth("500px").setZIndex("9999");
        Form form = new Form("personaForm", JettraServer.resolvePath("/persona"));
        modalAction = new TextBox("hidden", "action");
        modalId = new TextBox("hidden", "personaId");
        form.add(modalAction).add(modalId);

        groupId = new FormGroup();
        groupId.add(new Label("idV", msg.getProperty("lbl.id", "ID")))
                .add(inputIdVisible = new TextBox("text", "idV").setReadonly(true).setStyle("opacity", "0.7"));
        form.add(groupId);

        form.add(groupNombre = new FormGroup()).add(new Label("n", msg.getProperty("lbl.name")))
                .add(inputNombre = new TextBox("text", "nombre"));
        form.add(groupDireccion = new FormGroup()).add(new Label("d", msg.getProperty("lbl.address")))
                .add(inputDireccion = new TextBox("text", "direccion"));

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
        String action = params.get("action"), id = params.get("personaId");
        if ("save".equals(action)) {
            PersonaRepository.save(new PersonaModel(id, params.get("nombre"), params.get("direccion")));
            JettraSyncManager.notifyChange("PersonaModel",
                    (id == null || id.isEmpty()) ? SyncType.CREATE : SyncType.UPDATE, "user");
        } else if ("delete".equals(action)) {
            PersonaRepository.delete(id);
            JettraSyncManager.notifyChange("PersonaModel", SyncType.DELETE, "user");
        }
        try {
            redirect(currentExchange, JettraServer.resolvePath("/persona?page=" + pageNumber));
        } catch (Exception e) {
        }
    }
}