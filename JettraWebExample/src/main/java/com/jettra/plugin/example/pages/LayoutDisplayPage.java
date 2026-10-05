package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;

public class LayoutDisplayPage extends DashboardBasePage {
    public LayoutDisplayPage() { super("Layout Display Components"); }
    
    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div(); container.setStyle("padding", "30px");
        
        Div headerRow = new Div(); headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        Header h1 = new Header(1, "Layout Component Showcase"); h1.setStyle("margin", "0"); headerRow.add(h1);
        Button codeBtn = new Button("Code"); codeBtn.addClass("j-btn").setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)").setProperty("onclick", "document.getElementById('layout-code-modal').style.display = 'block'");
        headerRow.add(codeBtn); container.add(headerRow);
        
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("layout-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)").setStyle("backdrop-filter", "blur(10px)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("width", "90%").setStyle("max-width", "800px").setStyle("border", "1px solid var(--jettra-border)");
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        Div codeContainer = new Div(); codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px").setStyle("border-radius", "4px").setStyle("overflow-x", "auto").setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "Div div = new Div(); // Basic container\n" +
                          "Center center = new Center(); // Page content container\n" +
                          "Div hr = new Div(); hr.setStyle(\"border-bottom\", \"1px solid var(--jettra-border)\"); // Visual line\n" +
                          "Image img = new Image(\"logo.png\", \"Logo\"); // Standard image element\n";
                          
        UIComponent pre = new UIComponent("pre") {}; pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {}; codeTag.setProperty("id", "layout-code").setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        pre.add(codeTag); codeContainer.add(pre); codeModal.add(codeContainer);
        Div modalActions = new Div(); modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        Button copyBtn = new Button("Copy"); copyBtn.addClass("j-btn").setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('layout-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        Button closeBtn = new Button("Close"); closeBtn.addClass("j-btn").setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)").setProperty("onclick", "document.getElementById('layout-code-modal').style.display = 'none'");
        modalActions.add(closeBtn).add(copyBtn); codeModal.add(modalActions); container.add(codeModal);

        container.add(new Header(2, "Divs and Layouts"));
        Div row = new Div(); row.setStyle("display", "flex").setStyle("gap", "10px");
        
        Div box1 = new Div(); box1.setStyle("width", "100px").setStyle("height", "100px").setStyle("background", "rgba(255, 0, 0, 0.4)");
        Div box2 = new Div(); box2.setStyle("width", "100px").setStyle("height", "100px").setStyle("background", "rgba(0, 0, 255, 0.4)");
        row.add(box1).add(box2);
        
        container.add(row);
        
        container.add(new Header(2, "TabView Short Demo").setStyle("margin-top", "30px"));
        io.jettra.wui.complex.TabView miniTv = new io.jettra.wui.complex.TabView("")
            .addTab("Quick Look", new Paragraph("Layouts can also be tabbed."))
            .addTab("More", new Paragraph("Check the TabView page for details."));
        container.add(miniTv);

        Div sep = new Div(); sep.setStyle("border-bottom", "1px solid var(--jettra-border)").setStyle("margin", "20px 0").setStyle("width", "100%");
        container.add(sep);
        
        center.add(container);
    }
}
