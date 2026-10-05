package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.TrafficLight;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;

/**
 * Showcase page for the new TrafficLight component.
 */
public class TrafficLightPage extends DashboardBasePage {

    public TrafficLightPage() {
        super("TrafficLight Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "TrafficLight Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('tl-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("tl-code-modal");
        // We use the new defaults from Modal.java
        
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// 1. Basic TrafficLight (Red by default)\n" +
                          "TrafficLight tl = new TrafficLight();\n\n" +
                          "// 2. Setting Status\n" +
                          "tl.setStatus(TrafficLight.Status.GREEN);\n\n" +
                          "// 3. Fluent API\n" +
                          "new TrafficLight().setStatus(TrafficLight.Status.YELLOW).setStyle(\"margin\", \"20px\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "tl-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('tl-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('tl-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The TrafficLight component provides a visual status indicator using standard traffic light colors.");
        container.add(p);

        Div displayRow = new Div();
        displayRow.setStyle("display", "flex").setStyle("gap", "40px").setStyle("margin-top", "30px").setStyle("flex-wrap", "wrap");
        
        Div col1 = new Div().add(new Header(4, "Critical (RED)")).add(new TrafficLight().setStatus(TrafficLight.Status.RED));
        Div col2 = new Div().add(new Header(4, "Warning (YELLOW)")).add(new TrafficLight().setStatus(TrafficLight.Status.YELLOW));
        Div col3 = new Div().add(new Header(4, "Healthy (GREEN)")).add(new TrafficLight().setStatus(TrafficLight.Status.GREEN));
        
        displayRow.add(col1).add(col2).add(col3);
        container.add(displayRow);
        
        center.add(container);
    }
}
