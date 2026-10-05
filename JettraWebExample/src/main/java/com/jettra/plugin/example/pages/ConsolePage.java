package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Console;
import io.jettra.wui.components.Button;

/**
 * Showcase page for the Console component.
 */
public class ConsolePage extends DashboardBasePage {

    public ConsolePage() {
        super("Console Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Console Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('console-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("console-code-modal");
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
        
        String javaCode = "// 1. Basic Console\n" +
                          "Console console = new Console(\"miConsola\");\n" +
                          "console.setStyle(\"height\", \"250px\");\n" +
                          "container.add(console);\n\n" +
                          "// 2. Buttons to interact with Console\n" +
                          "Button infoBtn = new Button(\"Log Info\");\n" +
                          "infoBtn.setProperty(\"onclick\", \"jtConsoleLog('miConsola', 'This is an info message.', 'info')\");\n\n" +
                          "Button errBtn = new Button(\"Log Error\");\n" +
                          "errBtn.setProperty(\"onclick\", \"jtConsoleLog('miConsola', 'This is an error message!', 'error')\");\n\n" +
                          "Button clearBtn = new Button(\"Clear Console\");\n" +
                          "clearBtn.setProperty(\"onclick\", \"jtConsoleClear('miConsola')\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "console-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copiar código");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('console-java-code').innerText).then(() => { this.innerText='Copiado!'; setTimeout(() => this.innerText='Copiar código', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('console-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The Console component provides an in-browser log viewer where you can append lines using JS functions.");
        container.add(p);

        // --- Demo Console ---
        container.add(new Header(3, "Demo Console"));
        Console console = new Console("demoConsole");
        console.setStyle("height", "250px");
        console.setStyle("margin-bottom", "20px");
        container.add(console);
        
        Div btnRow = new Div();
        btnRow.setStyle("display", "flex").setStyle("gap", "10px").setStyle("flex-wrap", "wrap");
        
        Button btnInfo = new Button("Add Info Log");
        btnInfo.setProperty("onclick", "jtConsoleLog('demoConsole', 'System initializing...', 'info')");
        btnInfo.addClass("j-btn");
        
        Button btnWarn = new Button("Add Warning");
        btnWarn.setProperty("onclick", "jtConsoleLog('demoConsole', 'Memory usage high', 'warn')");
        btnWarn.addClass("j-btn");
        
        Button btnError = new Button("Add Error");
        btnError.setProperty("onclick", "jtConsoleLog('demoConsole', 'Failed to connect to database', 'error')");
        btnError.addClass("j-btn");
        
        Button btnClear = new Button("Clear Console");
        btnClear.setProperty("onclick", "jtConsoleClear('demoConsole')");
        btnClear.addClass("j-btn").setStyle("background-color", "#333");
        
        btnRow.add(btnInfo).add(btnWarn).add(btnError).add(btnClear);
        container.add(btnRow);
        
        center.add(container);
    }
}
