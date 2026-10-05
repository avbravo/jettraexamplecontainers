package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.TextBox;
import io.jettra.wui.components.Label;

public class NoEditablePage extends DashboardBasePage {

    public NoEditablePage() {
        super("NoEditable Component Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "NoEditable Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        io.jettra.wui.components.Button codeBtn = new io.jettra.wui.components.Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('noeditable-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("noeditable-code-modal");
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
        
        String javaCode = "import io.jettra.wui.core.annotations.NoEditable;\n\n" +
                          "public class MyModel {\n" +
                          "    @NoEditable\n" +
                          "    private String status;\n" +
                          "\n" +
                          "    // ... getters y setters ...\n" +
                          "}\n\n" +
                          "// En el UI Componente de forma manual:\n" +
                          "TextBox statusBox = new TextBox(\"text\", \"status\");\n" +
                          "statusBox.setProperty(\"value\", \"Aprobado\");\n" +
                          "statusBox.setReadonly(true);\n" +
                          "statusBox.setStyle(\"background-color\", \"var(--jettra-bg-muted)\");\n" +
                          "statusBox.setStyle(\"color\", \"var(--jettra-text-muted)\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "noeditable-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        io.jettra.wui.components.Button copyBtn = new io.jettra.wui.components.Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('noeditable-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        io.jettra.wui.components.Button closeBtn = new io.jettra.wui.components.Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('noeditable-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The @NoEditable annotation sets a field as read-only. It is visible to the user but cannot be modified.");
        container.add(p);

        container.add(new Header(3, "NoEditable Field Example"));
        Div row1 = new Div();
        row1.setStyle("margin-bottom", "30px");
        
        row1.add(new Label("editable_id", "Campo Editable:"));
        TextBox editableBox = new TextBox("text", "editable_field");
        editableBox.setProperty("value", "Puede ser modificado");
        editableBox.setStyle("display", "block").setStyle("margin-bottom", "20px");
        row1.add(editableBox);
        
        row1.add(new Label("noeditable_id", "Campo No Editable (Readonly):"));
        TextBox readonlyBox = new TextBox("text", "readonly_field");
        readonlyBox.setProperty("value", "Este valor es de solo lectura");
        readonlyBox.setReadonly(true);
        readonlyBox.setStyle("background-color", "var(--jettra-bg-muted)").setStyle("color", "var(--jettra-text-muted)").setStyle("cursor", "not-allowed");
        readonlyBox.setStyle("display", "block");
        row1.add(readonlyBox);
        
        container.add(row1);

        center.add(container);
    }
}
