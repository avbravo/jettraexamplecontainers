package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;

import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.SelectMany;
import io.jettra.wui.core.UIComponent;

/**
 * Showcase page for the newly improved SelectMany component.
 * Now it displays a modern scrollable list with multi-selection feedback.
 */
public class SelectManyPage extends DashboardBasePage {

    public SelectManyPage() {
        super("SelectMany Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "SelectMany Component");
        h1.setStyle("margin", "0").setStyle("color", "var(--jettra-accent)");
        headerRow.add(h1);
        
        Button codeBtn = new Button("View Source");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('selectmany-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("selectmany-code-modal");
        codeModal.setStyle("display", "none")
                 .setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)")
                 .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%")
                 .setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                 .setStyle("box-shadow", "0 10px 40px rgba(0,0,0,0.5)");
        
        Header modalH3 = new Header(3, "Java Code Examples");
        modalH3.setProperty("id", "selectmany-modal-header");
        modalH3.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)").setStyle("cursor", "move");
        codeModal.add(modalH3);
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-y", "auto")
                     .setStyle("max-height", "60vh")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// 1. Basic SelectMany Component\n" +
                          "SelectMany selectMany = new SelectMany(\"destinations\");\n" +
                          "selectMany.addOption(\"PTY\", \"Panamá\");\n" +
                          "selectMany.addOption(\"MAD\", \"Madrid\");\n" +
                          "selectMany.addOption(\"SJO\", \"San José\");\n" +
                          "selectMany.addOption(\"BOG\", \"Bogotá\");\n\n" +
                          "// 2. Setting Default Values\n" +
                          "selectMany.setDefault(\"MAD\");\n\n" +
                          "// 3. Custom Styling\n" +
                          "selectMany.setStyle(\"width\", \"400px\");\n" +
                          "selectMany.setStyle(\"max-height\", \"200px\");\n";
                          
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "selectmany-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('selectmany-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn);
        codeModal.add(modalActions);
        container.add(codeModal);
        
        Paragraph p = new Paragraph("The SelectMany component has been updated to provide a modern, touch-friendly list interface. Unlike standard dropdowns, it displays all options in a scrollable container with intuitive multi-selection feedback.");
        p.setStyle("font-size", "1.1rem").setStyle("margin-bottom", "25px").setStyle("opacity", "0.9");
        container.add(p);

        // --- Demo Area ---
        Div mainRow = new Div();
        mainRow.setStyle("display", "grid").setStyle("grid-template-columns", "repeat(auto-fit, minmax(300px, 1fr))").setStyle("gap", "30px");
        
        // Example 1: Basic
        Div card1 = new Div();
        card1.setStyle("padding", "25px").setStyle("background", "rgba(255,255,255,0.03)").setStyle("border", "1px solid var(--jettra-border)").setStyle("border-radius", "12px");
        card1.add(new Header(4, "Standard List Selection").setStyle("margin-top", "0"));
        card1.add(new Paragraph("Manage destinations with multiple selection support (no Ctrl key required).").setStyle("font-size", "0.9rem").setStyle("margin-bottom", "15px"));
        
        SelectMany cities = new SelectMany("ciudades");
        cities.addOption("PTY", "Panamá City");
        cities.addOption("MAD", "Madrid, Spain");
        cities.addOption("SJO", "San José, Costa Rica");
        cities.addOption("BOG", "Bogotá, Colombia");
        cities.addOption("MIA", "Miami, USA");
        cities.addOption("MEX", "Ciudad de México");
        cities.setDefault("MAD");
        
        card1.add(cities);
        mainRow.add(card1);
        
        // Example 2: Themed / Colors
        Div card2 = new Div();
        card2.setStyle("padding", "25px").setStyle("background", "rgba(255,255,255,0.03)").setStyle("border", "1px solid var(--jettra-border)").setStyle("border-radius", "12px");
        card2.add(new Header(4, "Permissions / Roles").setStyle("margin-top", "0"));
        card2.add(new Paragraph("A larger list with custom scroll height.").setStyle("font-size", "0.9rem").setStyle("margin-bottom", "15px"));
        
        SelectMany permissions = new SelectMany("roles");
        permissions.setStyle("max-height", "180px");
        permissions.addOption("admin", "Administrator");
        permissions.addOption("editor", "Editor");
        permissions.addOption("viewer", "Viewer");
        permissions.addOption("developer", "Developer");
        permissions.addOption("support", "Support Analyst");
        permissions.addOption("billing", "Billing Manager");
        
        card2.add(permissions);
        mainRow.add(card2);
        
        container.add(mainRow);
        
        center.add(container);
    }
}