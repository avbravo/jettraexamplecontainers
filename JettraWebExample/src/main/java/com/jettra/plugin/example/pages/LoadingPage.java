package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Loading;

/**
 * Showcase page for the new Loading component.
 */
public class LoadingPage extends DashboardBasePage {

    public LoadingPage() {
        super("Loading Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Loading Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        io.jettra.wui.components.Button codeBtn = new io.jettra.wui.components.Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('loading-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("loading-code-modal");
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
        
        String javaCode = "// 1. Basic Loading Spinner\n" +
                          "Loading loader = new Loading();\n\n" +
                          "// 2. Setting Size\n" +
                          "Loading small = new Loading().setSize(Loading.Size.SM);\n" +
                          "Loading extralarge = new Loading().setSize(Loading.Size.XL);\n\n" +
                          "// 3. Setting Custom Color\n" +
                          "Loading customLoader = new Loading().setColor(\"#4f46e5\").setSize(Loading.Size.LG);";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "loading-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        io.jettra.wui.components.Button copyBtn = new io.jettra.wui.components.Button("Copiar codigo");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('loading-java-code').innerText).then(() => { this.innerText='Copiado!'; setTimeout(() => this.innerText='Copiar codigo', 2000); })");
        
        io.jettra.wui.components.Button closeBtn = new io.jettra.wui.components.Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('loading-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The Loading component visually indicates background processes, processing tasks, or waiting times.");
        container.add(p);

        // --- Basic Types ---
        container.add(new Header(3, "Basic Loading with Default Settings"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px").setStyle("align-items", "center");
        row1.add(new Loading());
        container.add(row1);

        // --- Sizes ---
        container.add(new Header(3, "Sizes (XS/SM to XL)"));
        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "30px").setStyle("margin-bottom", "30px").setStyle("align-items", "center");
        
        row2.add(new Loading().setSize(Loading.Size.SM));
        row2.add(new Loading().setSize(Loading.Size.MD));
        row2.add(new Loading().setSize(Loading.Size.LG));
        row2.add(new Loading().setSize(Loading.Size.XL));
        
        container.add(row2);

        // --- Colors ---
        container.add(new Header(3, "Colors"));
        Div row3 = new Div();
        row3.setStyle("display", "flex").setStyle("gap", "30px").setStyle("margin-bottom", "30px").setStyle("align-items", "center");
        
        row3.add(new Loading().setSize(Loading.Size.LG).setColor("#22c55e")); // Green
        row3.add(new Loading().setSize(Loading.Size.LG).setColor("#ef4444")); // Red
        row3.add(new Loading().setSize(Loading.Size.LG).setColor("#eab308")); // Yellow
        row3.add(new Loading().setSize(Loading.Size.LG).setColor("#3b82f6")); // Blue
        row3.add(new Loading().setSize(Loading.Size.LG).setColor("#a855f7")); // Purple
        
        container.add(row3);
        
        center.add(container);
    }
}
