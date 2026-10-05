package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.CheckBoxGroup;
import io.jettra.wui.components.CheckBox;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Label;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.core.UIComponent;

/**
 * Showcase page for the new CheckBoxGroup component.
 */
public class CheckBoxGroupPage extends DashboardBasePage {

    public CheckBoxGroupPage() {
        super("CheckBoxGroup Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "CheckBoxGroup Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        io.jettra.wui.components.Button codeBtn = new io.jettra.wui.components.Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('checkboxgroup-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("checkboxgroup-code-modal");
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
        
        String javaCode = "// 1. Instanciar el Grupo\n" +
                          "CheckBoxGroup permissions = new CheckBoxGroup(\"permissions\");\n\n" +
                          "// 2. Crear los checkboxes con sus labels\n" +
                          "Div row1 = new Div();\n" +
                          "row1.setStyle(\"display\", \"flex\").setStyle(\"gap\", \"8px\");\n" +
                          "row1.add(new CheckBox(\"cbRead\", \"\", \"read\"));\n" +
                          "row1.add(new Label(\"Read Permission\"));\n\n" +
                          "Div row2 = new Div();\n" +
                          "row2.setStyle(\"display\", \"flex\").setStyle(\"gap\", \"8px\");\n" +
                          "row2.add(new CheckBox(\"cbWrite\", \"\", \"write\"));\n" +
                          "row2.add(new Label(\"Write Permission\"));\n\n" +
                          "// 3. Agregar los rows al grupo (Asigna el name='permissions' de manera recursiva)\n" +
                          "permissions.addCheckBox(row1);\n" +
                          "permissions.addCheckBox(row2);";
                          
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "checkboxgroup-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        io.jettra.wui.components.Button copyBtn = new io.jettra.wui.components.Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('checkboxgroup-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        io.jettra.wui.components.Button closeBtn = new io.jettra.wui.components.Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('checkboxgroup-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The CheckBoxGroup component simplifies grouping multiple CheckBoxes under the same 'name' attribute.");
        container.add(p);

        // --- Demo ---
        container.add(new Header(3, "Permissions Group (name='permissions')"));
        
        CheckBoxGroup permissions = new CheckBoxGroup("permissions");

        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "8px");
        row1.add(new CheckBox("chk1", "", "read"));
        row1.add(new Label("Read Permission"));
        permissions.addCheckBox(row1);

        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "8px");
        row2.add(new CheckBox("chk2", "", "write"));
        row2.add(new Label("Write Permission"));
        permissions.addCheckBox(row2);

        Div row3 = new Div();
        row3.setStyle("display", "flex").setStyle("gap", "8px");
        row3.add(new CheckBox("chk3", "", "execute"));
        row3.add(new Label("Execute Permission"));
        permissions.addCheckBox(row3);

        container.add(permissions);
        
        Paragraph note = new Paragraph("Inspect the elements above. Even though they are nested in Divs, the CheckBoxGroup recurses through its children to set the 'name' attribute on the actual <input type='checkbox'> elements to 'permissions'.");
        note.setStyle("color", "#aaa").setStyle("font-size", "14px").setStyle("margin-top", "20px");
        container.add(note);

        center.add(container);
    }
}
