package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;

/**
 * Showcase page for the new FormGroup component.
 */
public class FormGroupPage extends DashboardBasePage {

    public FormGroupPage() {
        super("FormGroup Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "FormGroup Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('formgroup-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("formgroup-code-modal");
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
        
        String javaCode = "// 1. Basic FormGroup usage\n" +
                          "FormGroup group = new FormGroup();\n" +
                          "group.add(new Label(\"name\", \"Full Name\"));\n" +
                          "group.add(new TextBox(\"text\", \"name\"));\n\n" +
                          "// 2. Chained FormGroup\n" +
                          "center.add(new FormGroup()\n" +
                          "    .add(new Label(\"email\", \"Email Address\"))\n" +
                          "    .add(new TextBox(\"email\", \"email\"))\n" +
                          ");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "formgroup-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy Code"); // Match user's request "copiar código"
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('formgroup-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy Code', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('formgroup-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The FormGroup component is a container that applies consistent spacing and structure to form elements (Labels, Inputs, etc.).");
        container.add(p);

        // --- Basic Usage ---
        container.add(new Header(3, "Standard Form Groups"));
        
        FormGroup g1 = new FormGroup();
        g1.add(new Label("name", "Name:").setStyle("color", "var(--jettra-accent)"));
        g1.add(new TextBox("text", "name").addClass("j-input").setStyle("width", "100%").setProperty("placeholder", "Enter your name..."));
        container.add(g1);

        FormGroup g2 = new FormGroup();
        g2.add(new Label("email", "Email:").setStyle("color", "var(--jettra-accent)"));
        g2.add(new TextBox("email", "email").addClass("j-input").setStyle("width", "100%").setProperty("placeholder", "user@example.com"));
        container.add(g2);

        // --- Chained Usage ---
        container.add(new Header(3, "Fluent API Construction"));
        container.add(new FormGroup()
            .add(new Label("comments", "Comments (Fluent API):").setStyle("color", "var(--jettra-accent)"))
            .add(new TextArea("comments", "comments").addClass("j-input").setStyle("width", "100%").setProperty("placeholder", "Write something..."))
        );
        
        center.add(container);
    }
}
