package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;

public class NavigationPage extends DashboardBasePage {
    public NavigationPage() { super("Navigation Components"); }
    
    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div(); container.setStyle("padding", "30px");
        
        Div headerRow = new Div(); headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        Header h1 = new Header(1, "Navigation Showcase"); h1.setStyle("margin", "0"); headerRow.add(h1);
        Button codeBtn = new Button("Code"); codeBtn.addClass("j-btn").setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)").setProperty("onclick", "document.getElementById('nav-code-modal').style.display = 'block'");
        headerRow.add(codeBtn); container.add(headerRow);
        
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("nav-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)").setStyle("backdrop-filter", "blur(10px)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("width", "90%").setStyle("max-width", "800px").setStyle("border", "1px solid var(--jettra-border)");
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        Div codeContainer = new Div(); codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px").setStyle("border-radius", "4px").setStyle("overflow-x", "auto").setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "Menu menu = new Menu();\n" +
                          "menu.addItem(\"Dashboard\", \"/dashboard\");\n" +
                          "menu.addItem(\"Settings\", \"/settings\");";
                          
        UIComponent pre = new UIComponent("pre") {}; pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {}; codeTag.setProperty("id", "nav-code").setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        pre.add(codeTag); codeContainer.add(pre); codeModal.add(codeContainer);
        Div modalActions = new Div(); modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        Button copyBtn = new Button("Copy"); copyBtn.addClass("j-btn").setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('nav-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        Button closeBtn = new Button("Close"); closeBtn.addClass("j-btn").setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)").setProperty("onclick", "document.getElementById('nav-code-modal').style.display = 'none'");
        modalActions.add(closeBtn).add(copyBtn); codeModal.add(modalActions); container.add(codeModal);

        container.add(new Header(2, "Sidebar Menu Example"));
        
        io.jettra.wui.complex.Menu menu = new io.jettra.wui.complex.Menu();
        menu.addItem("Sample Route 1", "#");
        menu.addItem("Sample Route 2", "#");
        
        Div menuWrapper = new Div(); menuWrapper.setStyle("width", "250px").setStyle("background", "var(--jettra-glass)").setStyle("padding", "10px").setStyle("border-radius", "8px");
        menuWrapper.add(menu);
        container.add(menuWrapper);
        
        center.add(container);
    }
}
