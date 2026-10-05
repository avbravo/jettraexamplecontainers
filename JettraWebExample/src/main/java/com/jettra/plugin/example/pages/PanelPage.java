package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Panel;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Label;
import io.jettra.wui.components.TextBox;

public class PanelPage extends DashboardBasePage {

    public PanelPage() {
        super("Panel Component Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Panel Layout Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('panel-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);

        // --- Code Modal ---
        Modal codeModal = new Modal("panel-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)");
        
        codeModal.add(new Header(3, "Panel Implementation Example").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "Panel myPanel = new Panel(2); // 2-columns\n" +
                          "myPanel.add(0, new Label(\"Name:\"));\n" +
                          "myPanel.add(1, new TextBox(\"name\", \"Enter name\"));\n\n" +
                          "center.add(myPanel);";
                           
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "panel-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('panel-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('panel-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Code Modal ---

        container.add(new Paragraph("The Panel component provides a powerful grid-based system for building responsive form layouts and column structures."));
        
        container.add(new Header(2, "2-Column Form Layout Example"));
        Panel panel2 = new Panel(2);
        panel2.setStyle("background", "rgba(0,255,255,0.05)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("border", "1px solid var(--jettra-border)").setStyle("margin-bottom", "30px");
        
        panel2.add(0, new Label("Username"));
        panel2.add(0, new TextBox("user", "Enter username"));
        
        panel2.add(1, new Label("Password"));
        panel2.add(1, new TextBox("pass", "Enter password"));
        
        container.add(panel2);

        container.add(new Header(2, "3-Column Dashboard Cards Example"));
        Panel panel3 = new Panel(3);
        panel3.setStyle("margin-top", "20px").setStyle("gap", "20px");
        
        panel3.add(0, createSampleCard("Active Tasks", "🚀 1,280", "rgba(0,255,255,0.1)"));
        panel3.add(1, createSampleCard("Server Load", "📈 42%", "rgba(0,255,100,0.1)"));
        panel3.add(2, createSampleCard("System Uptime", "⭐ 99.9%", "rgba(255,255,0,0.1)"));
        
        container.add(panel3);
        
        center.add(container);
    }

    private Div createSampleCard(String title, String val, String bgColor) {
        Div card = new Div();
        card.addClass("j-component");
        card.setStyle("background", bgColor).setStyle("padding", "20px").setStyle("text-align", "center");
        card.add(new Header(3, title).setStyle("margin-top", "0"));
        card.add(new Paragraph(val).setStyle("font-size", "1.5rem").setStyle("font-weight", "bold").setStyle("color", "var(--jettra-accent)"));
        return card;
    }
}
