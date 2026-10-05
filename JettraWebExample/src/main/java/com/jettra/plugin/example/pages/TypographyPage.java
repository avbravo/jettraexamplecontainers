package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;

public class TypographyPage extends DashboardBasePage {

    public TypographyPage() {
        super("Typography Components");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Typography Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('typo-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("typo-code-modal");
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
        
        String javaCode = "// 1. Headers\n" +
                          "Header h2 = new Header(2, \"Heading 2\");\n\n" +
                          "// 2. Paragraph\n" +
                          "Paragraph p = new Paragraph(\"Text content\");\n\n" +
                          "// 3. Span\n" +
                          "Span s = new Span(\"Inline text\");\n\n" +
                          "// 4. Label\n" +
                          "Label lbl = new Label(\"forId\", \"Label Text\");\n\n" +
                          "// 5. Link\n" +
                          "Link link = new Link(\"https://jettra.io\", \"Click me\");";
                          
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "typo-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('typo-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('typo-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---

        // Components Preview
        container.add(new Header(2, "Headers"));
        container.add(new Header(1, "Heading 1"));
        container.add(new Header(2, "Heading 2"));
        container.add(new Header(3, "Heading 3"));
        container.add(new Header(4, "Heading 4"));
        
        UIComponent hr1 = new UIComponent("hr") {}; hr1.setStyle("border", "none").setStyle("border-top", "1px solid rgba(0, 255, 255, 0.2)").setStyle("margin", "20px 0");
        container.add(hr1);
        
        container.add(new Header(2, "Paragraph, Span, Label, Link"));
        container.add(new Paragraph("This is a standard paragraph component."));
        
        Paragraph styledP = new Paragraph("This paragraph contains a ");
        styledP.add(new Span("highlighted Span").setStyle("color", "var(--jettra-accent)"));
        styledP.add(new Span(" inside it."));
        container.add(styledP);
        
        container.add(new Label("", "This is a Label component"));
        
        container.add(new Paragraph("")); // Spacer
        
        container.add(new Link("#", "This is a Link component").setStyle("color", "var(--jettra-accent)"));
        
        center.add(container);
    }
}
