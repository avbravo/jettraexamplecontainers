package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.CreditCard;

/**
 * Showcase page for the new CreditCard component.
 */
public class CreditCardPage extends DashboardBasePage {

    public CreditCardPage() {
        super("CreditCard Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "CreditCard Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('creditcard-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("creditcard-code-modal");
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
        
        String javaCode = "// 1. Basic CreditCard\\n" +
                          "CreditCard cc = new CreditCard(\"payment-form\")\\n" +
                          "    .setFormAction(\"/api/process\")\\n" +
                          "    .setSubmitText(\"Pay $99.00\");\\n\\n" +
                          "// 2. With Custom Buttons\\n" +
                          "CreditCard ccCustom = new CreditCard(\"custom-form\");\\n" +
                          "Button processBtn = new Button(\"Checkout\");\\n" +
                          "processBtn.setStyle(Button.Style.PRIMARY);\\n" +
                          "ccCustom.add(processBtn);";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "creditcard-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('creditcard-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('creditcard-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The CreditCard component generates a stylish input form with a live 3D glassmorphic card preview.");
        container.add(p);

        Div grid = new Div();
        grid.setStyle("display", "grid").setStyle("grid-template-columns", "repeat(auto-fit, minmax(380px, 1fr))").setStyle("gap", "30px").setStyle("margin-top", "30px");
        
        // 1. Basic CreditCard
        Div demo1 = new Div();
        demo1.add(new Header(4, "Basic Form").setStyle("color", "var(--jettra-accent)"));
        CreditCard cc = new CreditCard("demo-cc1")
            .setSubmitText("Complete Purchase");
        demo1.add(cc);
            
        // 2. Custom Buttons CreditCard
        Div demo2 = new Div();
        demo2.add(new Header(4, "Custom Actions").setStyle("color", "var(--jettra-accent)"));
        CreditCard customCc = new CreditCard("demo-cc2");
        Button submitBtn = new Button("Save Card");
        submitBtn.addClass("j-btn-primary");
        submitBtn.setStyle("width", "100%").setStyle("margin-top", "10px");
        customCc.add(submitBtn);
        demo2.add(customCc);
        
        grid.add(demo1);
        grid.add(demo2);
        
        container.add(grid);
        center.add(container);
    }
}
