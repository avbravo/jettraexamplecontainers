package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;

public class FormsPage extends DashboardBasePage {

    public FormsPage() {
        super("Form Components");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Form Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('form-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("form-code-modal");
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
        
        String javaCode = "Form form = new Form(\"myForm\", \"/submit\");\n\n" +
                          "// 1. TextBox\n" +
                          "TextBox tb = new TextBox(\"text\", \"username\").setPlaceholder(\"Enter name\");\n\n" +
                          "// 2. SelectOne\n" +
                          "SelectOne sel = new SelectOne(\"role\");\n" +
                          "sel.addOption(\"admin\", \"Admin\");\n\n" +
                          "// 3. CheckBox\n" +
                          "CheckBox cb = new CheckBox(\"id\", \"name\", \"val\");\n\n" +
                          "// 4. ToggleSwitch\n" +
                          "ToggleSwitch ts = new ToggleSwitch(\"id\", \"name\", \"on\").setLabels(\"ON\", \"OFF\");\n\n" +
                          "// 5. Button\n" +
                          "Button btn = new Button(\"Submit\");\n\n" +
                          "form.add(tb).add(sel).add(cb).add(ts).add(btn);";
                          
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "form-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('form-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('form-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---

        container.add(new Header(2, "Basic Form"));
        container.add(new Paragraph("This form demonstrates inputs, checkboxes, toggle switches, selects, and buttons."));
        
        Form f = new Form("demoForm", "#");
        
        Div fRow1 = new Div(); fRow1.setStyle("margin-bottom", "15px");
        fRow1.add(new Label("tb1", "Text Box (TextBox.java)").setStyle("margin-bottom", "5px").setStyle("display", "block"));
        TextBox tb = new TextBox("text", "tb1").setPlaceholder("Type here...");
        fRow1.add(tb);
        f.add(fRow1);
        
        Div fRow2 = new Div(); fRow2.setStyle("margin-bottom", "15px");
        fRow2.add(new Label("cb1", "Check Box (CheckBox.java)").setStyle("margin-bottom", "5px").setStyle("display", "block"));
        CheckBox cb = new CheckBox("cb1", "cbname", "checked");
        fRow2.add(cb);
        f.add(fRow2);
        
        Div fRow3 = new Div(); fRow3.setStyle("margin-bottom", "15px");
        fRow3.add(new Label("ts1", "Toggle Switch (ToggleSwitch.java)").setStyle("margin-bottom", "5px").setStyle("display", "block"));
        ToggleSwitch ts = new ToggleSwitch("ts1", "tsname", "on").setLabels("ON", "OFF");
        fRow3.add(ts);
        f.add(fRow3);
        
        Div fRow4 = new Div(); fRow4.setStyle("margin-bottom", "15px");
        fRow4.add(new Label("sel1", "Select Box (SelectOne.java)").setStyle("margin-bottom", "5px").setStyle("display", "block"));
        SelectOne sel = new SelectOne("selectname");
        sel.addOption("1", "Option 1");
        sel.addOption("2", "Option 2");
        fRow4.add(sel);
        f.add(fRow4);
        
        Div fRow5 = new Div(); fRow5.setStyle("margin-bottom", "15px");
        fRow5.add(new Button("Submit Form").addClass("j-btn j-btn-primary"));
        f.add(fRow5);
        
        container.add(f);
        
        center.add(container);
    }
}
