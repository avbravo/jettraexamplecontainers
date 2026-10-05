package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class MapPage extends DashboardBasePage {

    public MapPage() {
        super("Map Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Map Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('map-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        Modal codeModal = new Modal("map-code-modal");
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
        
        String javaCode = "Map locationMap = new Map(\"hq_map\");\\n" +
                          "locationMap.setCenter(40.7128, -74.0060, 13);\\n" +
                          "locationMap.setMarker(\"Our Headquarters\");\\n" +
                          "locationMap.setEnableSearch(true);\\n" +
                          "locationMap.setEnableRelief(true);\\n" +
                          "locationMap.addWaypoint(40.7308, -73.9973, \"Park\");\\n" +
                          "locationMap.addRoute(40.7128, -74.0060, 40.7308, -73.9973);\\n" +
                          "locationMap.setOnMapClick(\"L.marker([lat, lng]).addTo(window['map_hq_map']); window.show3DMessage('Map Interaction', 'Marker: '+lat+', '+lng);\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "map-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('map-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('map-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The Map component integrates Leaflet.js to render interactive geographic maps natively in JettraWUI.");
        container.add(p);
        container.add(new Divide());

        // --- Demo ---
        container.add(new Header(3, "Demo"));
        
        Map locationMap = new Map("hq_map");
        locationMap.setCenter(40.7128, -74.0060, 13);
        locationMap.setMarker("Our Headquarters - NY");
        locationMap.setEnableSearch(true);
        locationMap.setEnableRelief(true);
        locationMap.addWaypoint(40.7308, -73.9973, "Washington Square Park");
        locationMap.addRoute(40.7128, -74.0060, 40.7308, -73.9973);
        locationMap.setOnMapClick("L.marker([lat, lng]).addTo(window['map_hq_map']); window.show3DMessage('Map Interaction', 'Marker placed at: ' + lat.toFixed(4) + ', ' + lng.toFixed(4));");
        
        container.add(locationMap);
        
        center.add(container);
    }
}
