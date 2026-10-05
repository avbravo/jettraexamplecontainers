package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.TabView;
import io.jettra.wui.complex.Datatable;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Row;
import io.jettra.wui.components.TD;

public class TabViewPage extends DashboardBasePage {

    public TabViewPage() {
        super("TabView Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "TabView Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal-tabview').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Code Modal Dialog ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("code-modal-tabview");
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
        
        String javaCode = "TabView tv = new TabView(\"Main Configuration\")\n" +
                          "    .setOrientation(TabView.Orientation.LEFT)\n" +
                          "    .addTab(\"Basic\", new Paragraph(\"General settings...\"))\n" +
                          "    .addTab(\"Advanced\", new Paragraph(\"Complex options...\"));";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "java-code-tabview");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('java-code-tabview').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal-tabview').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        // --- Actual Component Demos ---
        
        // Demo 1: Top Orientation (Default)
        container.add(new Header(2, "Default Orientation (Top)").setStyle("margin-top", "30px"));
        TabView tvSuperior = new TabView("User Profile")
            .addTab("Basic Info", new Paragraph("Welcome to your profile. Here you can edit your basic information."))
            .addTab("Settings", new Paragraph("Configure your notifications and privacy preferences."))
            .addTab("Security", new Button("Change Password").primary());
        container.add(tvSuperior);

        // Demo 2: Left Orientation
        container.add(new Header(2, "Left Orientation (Sidebar)").setStyle("margin-top", "40px"));
        TabView tvLeft = new TabView("System Admin")
            .setOrientation(TabView.Orientation.LEFT)
            .addTab("Users", new Paragraph("Manage system users and roles."))
            .addTab("Logs", new Paragraph("View application logs and errors."));
        
        // Add a Datatable inside a tab to show enhanced pagination
        Datatable dt = new Datatable();
        dt.setPrevLabel("Previous").setNextLabel("Next");
        dt.setPrevIcon("⬅️").setNextIcon("➡️");
        dt.addHeaderRow(new Row(new TD("ID"), new TD("Event"), new TD("Status")));
        dt.addRow(new Row(new TD("1"), new TD("Login"), new TD("Success")));
        dt.addRow(new Row(new TD("2"), new TD("Data Sync"), new TD("Pending")));
        dt.addRow(new Row(new TD("3"), new TD("Logout"), new TD("Success")));
        dt.addRow(new Row(new TD("4"), new TD("Error"), new TD("Critical")));
        dt.addRow(new Row(new TD("5"), new TD("Query"), new TD("Success")));
        dt.addRow(new Row(new TD("6"), new TD("Report"), new TD("Generated")));
        
        tvLeft.addTab("Records", dt);
        container.add(tvLeft);

        // Demo 3: Right and Bottom
        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-top", "40px");
        
        TabView tvRight = new TabView("Right Tabs")
            .setOrientation(TabView.Orientation.RIGHT)
            .addTab("Tab A", new Paragraph("This is on the right."))
            .addTab("Tab B", new Paragraph("Another one."));
            
        TabView tvBottom = new TabView("Bottom Tabs")
            .setOrientation(TabView.Orientation.BOTTOM)
            .addTab("Home", new Paragraph("Content on top..."))
            .addTab("Details", new Paragraph("...tabs on bottom."));
            
        row2.add(tvRight).add(tvBottom);
        container.add(row2);

        center.add(container);
    }
}
