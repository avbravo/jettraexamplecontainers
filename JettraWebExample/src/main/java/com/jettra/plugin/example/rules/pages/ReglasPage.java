package com.jettra.plugin.example.rules.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.rules.model.ReglasModel;
import com.jettra.plugin.example.crud.repository.ReglasRepository;
import io.jettra.wui.complex.*;
import io.jettra.wui.components.*;

import io.jettra.wui.sync.*;
import io.jettra.server.JettraServer;
import io.jettra.core.inject.annotation.InjectProperties;
import io.jettra.report.Report;
import io.jettra.report.ReportViewer;
import java.util.*;

@JettraPageSincronized(SyncType.ALL)
public class ReglasPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;
    
    private Modal crudModal;
    private TextBox modalAction, modalId, inputId, inputSaldo, inputDescuento, inputSaldoNeto;
    private FormGroup groupId, groupSaldo, groupDescuento, groupSaldoNeto;
    private Paragraph deleteMsg;
    private Button modalSubmitBtn;
    private ReportViewer reportViewer;

    public ReglasPage() {
        super("Gestión Manual de Reglas y Cómputos");
    }

    @Override
    protected void initCenter(Center center, String username) {
        setupModal();
        Card card = new Card().setTitle("Listado de Reglas").setWidth("100%");
        Datatable table = new Datatable();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add", "Añadir")).setId("btnAdd")
                .setBackgroundColor("#238636").setStyle("font-size", "12px")
                .addClickListener(() -> openModal("save", new ReglasModel("", 0.0, 0.0, 0.0)));
                
        Button printBtn = new Button("🖨️ Imprimir").setId("btnPrint")
                .setBackgroundColor("#007bff").setStyle("font-size", "12px")
                .setOnclick("document.getElementById('reportModal_reglas_list').style.display='flex';");

        table.addHeaderRow(new Row(
            new TD("ID"), 
            new TD("Saldo"), 
            new TD("Descuento"), 
            new TD("Saldo Neto"),
            new TD().add(addBtn).add(printBtn)
        ));

        List<ReglasModel> all = ReglasRepository.findAll();
        all.forEach(r -> {
            Button editBtn = new Button("✏️").setId("btnEdit-" + r.getId())
                    .addClickListener(() -> openModal("save", r));
            Button delBtn = new Button("🗑️").setId("btnDel-" + r.getId()).setStyle("color", "red")
                    .addClickListener(() -> openModal("delete", r));
            table.addRow(new Row(
                new TD(r.getId()),
                new TD(r.getSaldo().toString()),
                new TD(r.getDescuento().toString()),
                new TD(r.getSaldoNeto().toString()),
                new TD().add(editBtn).add(delBtn)
            ));
        });

        setupReportViewer();
        center.add(new Div().setStyle("padding", "20px").add(card.add(table))).add(crudModal).add(reportViewer);
        
        // Manual JS for @Compute simulation
        String js = "function showToast(msg, type) {\n" +
                    "  let toast = document.getElementById('j-toast-reglas');\n" +
                    "  if(!toast) {\n" +
                    "    toast = document.createElement('div');\n" +
                    "    toast.id = 'j-toast-reglas';\n" +
                    "    toast.style = 'position:fixed;top:20px;right:20px;z-index:99999;padding:12px 20px;border-radius:8px;color:white;font-weight:bold;transition:all 0.3s;display:none;';\n" +
                    "    document.body.appendChild(toast);\n" +
                    "  }\n" +
                    "  toast.innerText = msg;\n" +
                    "  toast.style.backgroundColor = type === 'error' ? 'rgba(239, 68, 68, 0.9)' : 'rgba(16, 185, 129, 0.9)';\n" +
                    "  toast.style.display = 'block';\n" +
                    "  toast.style.opacity = '1';\n" +
                    "  setTimeout(() => { toast.style.opacity = '0'; setTimeout(() => toast.style.display='none', 300); }, 3000);\n" +
                    "}\n" +
                    "function updateSaldoNeto() {\n" +
                    "  const saldo = parseFloat(document.getElementById('inputSaldo').value) || 0;\n" +
                    "  const descuento = parseFloat(document.getElementById('inputDescuento').value) || 0;\n" +
                    "  const neto = saldo - descuento;\n" +
                    "  const target = document.getElementById('inputSaldoNeto');\n" +
                    "  if(target) target.value = neto.toFixed(2);\n" +
                    "  validateRealTime();\n" +
                    "}\n" +
                    "function validateRealTime() {\n" +
                    "  const saldo = parseFloat(document.getElementById('inputSaldo').value) || 0;\n" +
                    "  const descuento = parseFloat(document.getElementById('inputDescuento').value) || 0;\n" +
                    "  const inputD = document.getElementById('inputDescuento');\n" +
                    "  if(descuento > saldo) {\n" +
                    "    inputD.style.borderColor = '#f85149';\n" +
                    "    inputD.style.boxShadow = '0 0 8px rgba(248, 81, 73, 0.5)';\n" +
                    "    showToast('El descuento no puede ser mayor al saldo', 'error');\n" +
                    "  } else {\n" +
                    "    inputD.style.borderColor = '';\n" +
                    "    inputD.style.boxShadow = '';\n" +
                    "  }\n" +
                    "}\n" +
                    "function validateForm(e) {\n" +
                    "  const actionEl = document.getElementById('modalAction');\n" +
                    "  if(actionEl && actionEl.value === 'delete') return true;\n" +
                    "  const saldo = parseFloat(document.getElementById('inputSaldo').value) || 0;\n" +
                    "  const descuento = parseFloat(document.getElementById('inputDescuento').value) || 0;\n" +
                    "  if(descuento > saldo) {\n" +
                    "    showToast('Error: El descuento no puede ser mayor al saldo', 'error');\n" +
                    "    if(e) e.preventDefault();\n" +
                    "    return false;\n" +
                    "  }\n" +
                    "  return true;\n" +
                    "}\n" +
                    "setTimeout(() => {\n" +
                    "  const s = document.getElementById('inputSaldo');\n" +
                    "  const d = document.getElementById('inputDescuento');\n" +
                    "  if(s) s.addEventListener('input', updateSaldoNeto);\n" +
                    "  if(d) d.addEventListener('input', updateSaldoNeto);\n" +
                    "}, 500);\n";
        center.add(new Script(js));
    }

    private void openModal(String action, ReglasModel r) {
        crudModal.setDisplay("block");
        modalAction.setValue(action);
        modalId.setValue(r.getId());
        inputId.setValue(r.getId());
        inputSaldo.setValue(r.getSaldo().toString());
        inputDescuento.setValue(r.getDescuento().toString());
        inputSaldoNeto.setValue(r.getSaldoNeto().toString());
        
        boolean isDel = "delete".equals(action);
        boolean isNew = (r.getId() == null || r.getId().isEmpty());
        
        inputId.setReadonly(!isNew);
        inputSaldo.setReadonly(isDel);
        inputDescuento.setReadonly(isDel);
        
        deleteMsg.setStyle("display", isDel ? "block" : "none");
        modalSubmitBtn.setContent(isDel ? "Confirmar Eliminar" : "Guardar");
        modalSubmitBtn.setBackgroundColor(isDel ? "#da3633" : "#238636");
    }

    private void setupModal() {
        crudModal = new Modal("crudModal").setMaxWidth("500px").setZIndex("9999");
        Form form = new Form("reglasForm", JettraServer.resolvePath("/reglas")).setProperty("onsubmit", "return validateForm(event)");
        modalAction = new TextBox("hidden", "action").setId("modalAction");
        modalId = new TextBox("hidden", "reglasId");
        form.add(modalAction).add(modalId);

        form.add(groupId = new FormGroup()).add(new Label("id", "ID")).add(inputId = new TextBox("text", "id").setId("inputId"));
        form.add(groupSaldo = new FormGroup()).add(new Label("s", "Saldo")).add(inputSaldo = new TextBox("text", "saldo").setId("inputSaldo"));
        form.add(groupDescuento = new FormGroup()).add(new Label("d", "Descuento")).add(inputDescuento = new TextBox("text", "descuento").setId("inputDescuento"));
        form.add(groupSaldoNeto = new FormGroup()).add(new Label("sn", "Saldo Neto")).add(inputSaldoNeto = new TextBox("text", "saldoNeto").setId("inputSaldoNeto"));
        
        // Saldo Neto is always readonly because it's computed
        inputSaldoNeto.setReadonly(true)
                .setStyle("background-color", "rgba(48, 54, 61, 0.5)") // Specific dark glass background
                .setStyle("cursor", "not-allowed")
                .setStyle("color", "#ffffff") // High contrast white
                .setStyle("font-weight", "bold")
                .setStyle("border", "1px solid rgba(255, 255, 255, 0.1)");

        deleteMsg = new Paragraph("¿Está seguro de eliminar este registro?").setStyle("color", "#f85149").setStyle("display", "none");
        form.add(deleteMsg);

        Div actions = new Div().setStyle("display", "flex").setStyle("gap", "10px").setStyle("margin-top", "20px");
        actions.add(new Button("Cancelar").setType("button").setOnclick("document.getElementById('crudModal').style.display='none'; return false;"));
        actions.add(modalSubmitBtn = new Button("Guardar").setType("submit").setBackgroundColor("#238636"));
        crudModal.add(new Header(3, "Operación")).add(form.add(actions));
    }

    private void setupReportViewer() {
        Report report = new Report("Listado de Reglas de Descuento");
        report.setData(ReglasRepository.findAll());
        
        Report.Table table = new Report.Table();
        table.addColumn(new Report.Column("ID", "id", 100));
        table.addColumn(new Report.Column("Saldo", "saldo", 120));
        table.addColumn(new Report.Column("Descuento", "descuento", 120));
        table.addColumn(new Report.Column("Saldo Neto", "saldoNeto", 120));
        
        report.getDetail().addElement(table);
        report.getViewerOptions().setAllowPrint(true).setAllowPdf(true).setAllowExcel(true);
        
        this.reportViewer = report.createViewer("reglas_list");
    }

    @Override
    protected void onGet(java.util.Map<String, String> params) {
        if ("report".equals(params.get("action"))) {
            io.jettra.wui.mvc.JettraMVC.generateReport(this, ReglasModel.class, ReglasRepository.class, "pdf", false);
        }
    }

    @Override
    protected void onPost(java.util.Map<String, String> params) {
        String action = params.get("action");
        String idValue = params.get("id");
        if (idValue == null || idValue.isEmpty()) idValue = params.get("reglasId");

        if ("save".equals(action)) {
            try {
                Double saldo = Double.parseDouble(params.get("saldo"));
                Double descuento = Double.parseDouble(params.get("descuento"));
                Double neto = Double.parseDouble(params.get("saldoNeto"));
                ReglasModel model = new ReglasModel(idValue, saldo, descuento, neto);
                
                // Backend @Rules validation
                java.util.List<io.jettra.rules.core.RuleResult> results = io.jettra.rules.core.JettraRulesEngine.validate(model);
                boolean valid = true;
                StringBuilder errors = new StringBuilder();
                for (io.jettra.rules.core.RuleResult res : results) {
                    if (!res.isValid()) {
                        valid = false;
                        errors.append(res.getMessage()).append("\\n");
                    }
                }
                
                if (!valid) {
                    String jsError = "<script>alert('Error de validación:\\n" + errors.toString() + "'); history.back();</script>";
                    currentExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    currentExchange.sendResponseHeaders(200, jsError.getBytes().length);
                    currentExchange.getResponseBody().write(jsError.getBytes());
                    currentExchange.getResponseBody().close();
                    return;
                }
                
                ReglasRepository.save(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("delete".equals(action)) {
            ReglasRepository.delete(idValue);
        }
        try { redirect(currentExchange, JettraServer.resolvePath("/reglas")); } catch (Exception e) {}
    }
}
