package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Icon;

public class IconsPage extends DashboardBasePage {

    public IconsPage() {
        super("Icon Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Icon Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('icon-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        Modal codeModal = new Modal("icon-code-modal");
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
        
        String javaCode = "Icon homeIcon = new Icon(Icon.HOME).setSize(\"32px\").setColor(\"#00ffff\");\n" +
                          "Icon userIcon = new Icon(Icon.USER);\n" +
                          "Icon settingsIcon = new Icon(Icon.SETTINGS).setColor(\"#ff00ff\");\n\n" +
                          "// Custom SVG\n" +
                          "Icon custom = new Icon(\"<svg>...</svg>\");\n\n" +
                          "// Or Emoji\n" +
                          "Icon emoji = new Icon(\"🔥\").setSize(\"48px\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "icon-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('icon-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('icon-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        container.add(codeModal);
        
        Paragraph p = new Paragraph("A standardized Icon component to render SVGs and emojis easily with dynamic size and color styling.");
        container.add(p);

        // Demo 1
        container.add(new Header(3, "Predefined SVG Icons"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row1.add(new Icon(Icon.HOME).setSize("40px").setColor("var(--jettra-accent)"));
        row1.add(new Icon(Icon.USER).setSize("40px").setColor("#4ade80"));
        row1.add(new Icon(Icon.SETTINGS).setSize("40px").setColor("#a78bfa"));
        row1.add(new Icon(Icon.TRASH).setSize("40px").setColor("#f87171"));
        row1.add(new Icon(Icon.CHECK).setSize("40px").setColor("#34d399"));
        row1.add(new Icon(Icon.CLOSE).setSize("40px").setColor("#f43f5e"));
        row1.add(new Icon(Icon.PLUS).setSize("40px").setColor("#fbbf24"));
        row1.add(new Icon(Icon.MINUS).setSize("40px").setColor("#fbbf24"));
        
        container.add(row1);

        // Demo 2
        container.add(new Header(3, "Emoji Icons"));
        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row2.add(new Icon("🚀").setSize("40px"));
        row2.add(new Icon("⭐").setSize("40px"));
        row2.add(new Icon("🔥").setSize("40px"));
        row2.add(new Icon("🎨").setSize("40px"));
        
        container.add(row2);

        center.add(container);
    }
}
