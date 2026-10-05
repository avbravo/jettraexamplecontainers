package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;

public class ModalPage extends DashboardBasePage {

    public ModalPage() {
        super("Modal Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Modal Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code")
            .setStyle("border-color", "var(--jettra-accent)")
            .setStyle("color", "var(--jettra-accent)")
            .setOnclick("document.getElementById('code-modal-modal').style.display = 'flex'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Code Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("code-modal-modal")
                 .setZIndex("2000")
                 .setMaxWidth("850px")
                 .setPadding("25px")
                 .setBackgroundColor("rgba(20, 30, 40, 0.95)")
                 .setBorder("1px solid var(--jettra-accent)");
        
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "Modal m = new Modal(\"my-modal\")\n" +
                          "     .setPadding(\"40px\")\n" +
                          "     .setBackgroundColor(\"#1a1f26\")\n" +
                          "     .setMaxWidth(\"700px\")\n" +
                          "     .setZIndex(\"1050\")\n" +
                          "     .add(new Header(3, \"Title\"))\n" +
                          "     .add(new Button(\"Save\"));";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "java-code-modal");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy")
            .setOnclick("navigator.clipboard.writeText(document.getElementById('java-code-modal').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close")
            .setStyle("background", "transparent")
            .setStyle("border-color", "var(--jettra-border)")
            .setOnclick("document.getElementById('code-modal-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        // --- Actual Component Demo ---
        container.add(new Header(2, "Modal Example").setStyle("margin-top", "30px"));
        container.add(new Paragraph("A pop-up modal component that overlays the screen.").setStyle("margin-bottom", "20px"));
        
        Div row1 = new Div();
        row1.setStyle("margin-bottom", "30px");
        
        Button openBtn = new Button("Open Demo Modal")
            .primary()
            .setOnclick("document.getElementById('demo-modal').style.display = 'flex'");
        
        io.jettra.wui.complex.Modal demoModal = new io.jettra.wui.complex.Modal("demo-modal")
                 .setPadding("40px")
                 .setMaxWidth("600px")
                 .setBoxShadow("0 0 60px rgba(0,255,255,0.3)");
        
        demoModal.add(new Header(2, "Demo Warning"));
        demoModal.add(new Paragraph("Are you sure you want to perform this action?"));
        
        Button confirmBtn = new Button("Confirm")
            .primary()
            .setOnclick("document.getElementById('demo-modal').style.display = 'none'");
        
        Button cancelBtn = new Button("Cancel")
            .setOnclick("document.getElementById('demo-modal').style.display = 'none'");
        
        Div demoActions = new Div();
        demoActions.setStyle("display", "flex").setStyle("gap", "10px").setStyle("margin-top", "20px");
        demoActions.add(cancelBtn).add(confirmBtn);
        demoModal.add(demoActions);
        
        row1.add(openBtn).add(demoModal);
        container.add(row1);
        
        center.add(container);
    }
}
