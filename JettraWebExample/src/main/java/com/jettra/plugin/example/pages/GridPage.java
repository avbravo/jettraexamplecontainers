package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.complex.Grid;

public class GridPage extends DashboardBasePage {

    public GridPage() {
        super("Grid Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Grid Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal-grid').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Code Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("code-modal-grid");
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
        
        String javaCode = "Grid grid = new Grid(3, \"20px\"); // 3 columns, 20px gap\n" +
                          "grid.add(new Div().setContent(\"Col 1\"));\n" +
                          "grid.add(new Div().setContent(\"Col 2\"));\n" +
                          "grid.add(new Div().setContent(\"Col 3\"));";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "java-code-grid");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('java-code-grid').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal-grid').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        // --- Actual Component Demo ---
        container.add(new Header(2, "Grid Example").setStyle("margin-top", "30px"));
        container.add(new Paragraph("A CSS grid-based layout structure.").setStyle("margin-bottom", "20px"));
        
        Div row1 = new Div();
        row1.setStyle("margin-bottom", "30px");
        
        Grid grid = new Grid(3, "20px");
        
        Div b1 = new Div(); b1.setContent("Cell 1"); b1.setStyle("background", "var(--jettra-surface)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("text-align", "center");
        Div b2 = new Div(); b2.setContent("Cell 2"); b2.setStyle("background", "var(--jettra-surface)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("text-align", "center");
        Div b3 = new Div(); b3.setContent("Cell 3"); b3.setStyle("background", "var(--jettra-surface)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("text-align", "center");
        Div b4 = new Div(); b4.setContent("Cell 4"); b4.setStyle("background", "var(--jettra-surface)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("text-align", "center");
        Div b5 = new Div(); b5.setContent("Cell 5"); b5.setStyle("background", "var(--jettra-surface)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("text-align", "center");
        
        grid.add(b1).add(b2).add(b3).add(b4).add(b5);
        
        row1.add(grid);
        container.add(row1);
        
        center.add(container);
    }
}
