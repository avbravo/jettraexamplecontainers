package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class ChartsPiePage extends DashboardBasePage {
    public ChartsPiePage() {
        super("Charts Pie");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Header h1 = new Header(1, "ChartsPie Component");
        Paragraph subtitle = new Paragraph("A fully responsive Pie Chart powered by Chart.js.");
        center.add(h1).add(subtitle).add(new Divide());

        ChartsPie chart = new ChartsPie("chartP");
        chart.setLabels("Apple", "Banana", "Cherry");
        chart.addDataset("Fruits", new Number[]{50, 20, 30}, 
             new String[]{"#ff0000", "#ffff00", "#ff00ff"}, 
             new String[]{"#fff", "#fff", "#fff"});

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='block'");

        center.add(chart).add(codeBtn);

        // Code Modal
        Modal codeModal = new Modal("code-modal");
        codeModal.add(new Header(3, "ChartsPie Code"));
        String codeStr = "ChartsPie chart = new ChartsPie(\"chartP\");\\n" +
                         "chart.setLabels(\"Apple\", \"Banana\", \"Cherry\");\\n" +
                         "chart.addDataset(\"Fruits\", new Number[]{50, 20, 30}, \\n" +
                         "     new String[]{\"#ff0000\", \"#ffff00\", \"#ff00ff\"}, \\n" +
                         "     new String[]{\"#fff\", \"#fff\", \"#fff\"});";
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");

        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "chartspie-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(codeStr.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div actions = new Div();
        actions.setStyle("margin-top", "15px").setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('chartspie-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='none'");
        
        actions.add(closeBtn).add(copyBtn);
        codeModal.add(actions);
        center.add(codeModal);
    }
}
