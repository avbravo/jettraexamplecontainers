package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class ChartsRadarPage extends DashboardBasePage {
    public ChartsRadarPage() {
        super("Charts Radar");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Header h1 = new Header(1, "ChartsRadar Component");
        Paragraph subtitle = new Paragraph("A fully responsive Radar Chart powered by Chart.js.");
        center.add(h1).add(subtitle).add(new Divide());

        ChartsRadar chart = new ChartsRadar("chartR");
        chart.setLabels("Speed", "Power", "Defense", "Agility");
        chart.addDataset("Player 1", new Number[]{80, 90, 70, 85}, 
             new String[]{"rgba(255, 99, 132, 0.5)"}, 
             new String[]{"rgba(255, 99, 132, 1)"});

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='block'");

        center.add(chart).add(codeBtn);

        // Code Modal
        Modal codeModal = new Modal("code-modal");
        codeModal.add(new Header(3, "ChartsRadar Code"));
        String codeStr = "ChartsRadar chart = new ChartsRadar(\"chartR\");\\n" +
                         "chart.setLabels(\"Speed\", \"Power\", \"Defense\", \"Agility\");\\n" +
                         "chart.addDataset(\"Player 1\", new Number[]{80, 90, 70, 85}, \\n" +
                         "     new String[]{\"rgba(255, 99, 132, 0.5)\"}, \\n" +
                         "     new String[]{\"rgba(255, 99, 132, 1)\"});";
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");

        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "chartsradar-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(codeStr.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div actions = new Div();
        actions.setStyle("margin-top", "15px").setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('chartsradar-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='none'");
        
        actions.add(closeBtn).add(copyBtn);
        codeModal.add(actions);
        center.add(codeModal);
    }
}
