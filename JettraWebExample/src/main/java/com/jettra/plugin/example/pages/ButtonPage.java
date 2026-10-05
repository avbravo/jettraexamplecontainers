package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;

public class ButtonPage extends DashboardBasePage {

    public ButtonPage() {
        super("Button Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Button Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('button-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("button-code-modal");
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
        
        String javaCode = "Button primary = new Button(\"Primary Button\");\nprimary.addClass(\"j-btn j-btn-primary\");\n\nButton secondary = new Button(\"Secondary\");\nsecondary.addClass(\"j-btn j-btn-secondary\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "button-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        io.jettra.wui.components.Button copyBtn = new io.jettra.wui.components.Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('button-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        io.jettra.wui.components.Button closeBtn = new io.jettra.wui.components.Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('button-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The Button component is used to trigger actions.");
        container.add(p);

        // --- Demo ---
        container.add(new Header(3, "Demo"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        Button primary = new Button("Primary Button").setIcon("⭐");
        primary.addClass("j-btn j-btn-primary");
        Button secondary = new Button("Secondary").setIcon("💾");
        secondary.addClass("j-btn j-btn-secondary");
        row1.add(primary).add(secondary);
        
        container.add(row1);
        
        container.add(new Header(4, "Icon Selection Example"));
        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "20px").setStyle("align-items", "center").setStyle("margin-bottom", "30px");
        
        io.jettra.wui.components.SelectOne iconSelector = new io.jettra.wui.components.SelectOne("iconSel");
        iconSelector.addOption("⭐", "⭐ Star");
        iconSelector.addOption("💾", "💾 Save");
        iconSelector.addOption("🚀", "🚀 Rocket");
        iconSelector.addOption("🗑️", "🗑️ Trash");
        iconSelector.addOption("✏️", "✏️ Edit");
        iconSelector.addOption("✅", "✅ Check");
        iconSelector.setProperty("onchange", "document.getElementById('dynBtn').querySelector('.j-btn-icon').innerText = this.value");
        iconSelector.setStyle("width", "120px");
        
        Button dynamicBtn = new Button("Dynamic Icon").setIcon("⭐").id("dynBtn");
        dynamicBtn.addClass("j-btn j-btn-info");
        
        row2.add(iconSelector).add(dynamicBtn);
        container.add(row2);
        
        center.add(container);
    }
}
