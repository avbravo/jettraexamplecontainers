package com.jettra.plugin.example.factura.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.core.UIComponent;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.report.annotations.ModelReportDisabledHeader;

/**
 * Example page showing the usage of @ViewDataTable annotation.
 */
@ModelReportDisabledHeader
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.plugin.example.factura.model.FacturaModel.class, repository = com.jettra.plugin.example.factura.repository.FacturaRepository.class, editable = true, autoRender = false, report = true, reportShowViewer = true, reportAllowPrint = true, reportAllowPdf = true, reportAllowExcel = true, reportAllowCsv = true, reportAllowWord = true)
public interface ViewDataTablePageDef {

    default void afterInitCenter(io.jettra.wui.complex.Center center, String username) {
Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Master-Detail @ViewDataTable");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('viewdatatable-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);

        // Render CrudView for Factura (which contains @ViewDataTable)
        io.jettra.wui.complex.CrudView crudComponent = new io.jettra.wui.complex.CrudView(com.jettra.plugin.example.factura.model.FacturaModel.class, com.jettra.plugin.example.factura.repository.FacturaRepository.class, null);
        crudComponent.setEditable(true);
        crudComponent.setReportEnabled(true);
        crudComponent.setReportShowViewer(true);
        crudComponent.setReportAllowPrint(true);
        crudComponent.setReportAllowPdf(true);
        crudComponent.setReportAllowExcel(true);
        crudComponent.setReportAllowCsv(true);
        crudComponent.setReportAllowWord(true);
        crudComponent.build();
        container.add(crudComponent);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("viewdatatable-code-modal");
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
        
        String javaCode = "@JettraViewModel\n" +
                          "public class FacturaModel {\n" +
                          "    private Long idFactura;\n" +
                          "    private LocalDate fechaEmision;\n" +
                          "\n" +
                          "    @ViewDataTable(row=\"id, producto, precio, cantidad, total\", editablerow=\"producto, cantidad\")\n" +
                          "    private List<LineaFacturaModel> lineaFacturaModel;\n" +
                          "}\n";
                           
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "viewdatatable-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copiar código");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('viewdatatable-java-code').innerText).then(() => { this.innerText='Copiado!'; setTimeout(() => this.innerText='Copiar código', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('viewdatatable-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);

        center.setContent(container.render());
    }
}
