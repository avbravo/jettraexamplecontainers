package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;

/**
 * Showcase page for the new QRReader component.
 */
public class QRReaderPage extends DashboardBasePage {

    public QRReaderPage() {
        super("QR Reader Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "QR Reader Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('qrreader-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("qrreader-code-modal");
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
        
        String javaCode = "// 1. Instantiating a QR Reader\n" +
                          "QRReader reader = new QRReader(\"myQrReader\");\n\n" +
                          "// 2. Setting Dimensions\n" +
                          "reader.setWidth(\"350px\").setHeight(\"300px\");\n\n" +
                          "// 3. Setting JavaScript Scan Callback\n" +
                          "reader.setOnScan(\"function(code) { \" +\n" +
                          "  \"  document.getElementById('scannedResult').value = code; \" +\n" +
                          "\"}\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "qrreader-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copiar código");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('qrreader-java-code').innerText).then(() => { this.innerText='Copiado!'; setTimeout(() => this.innerText='Copiar código', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('qrreader-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The QRReader component utilizes the client device's camera to decode QR codes in real-time. It features dynamic camera selection, target laser scanning animations, and a sound beep confirmation upon detection.");
        container.add(p);

        // --- Interactive Playground ---
        Div playground = new Div();
        playground.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("align-items", "center").setStyle("gap", "20px").setStyle("margin-top", "30px");

        // Target Textbox for scanned output
        Div formGroup = new Div().setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "5px").setStyle("width", "350px");
        formGroup.add(new Label("scannedResult", "Scanned QR Value").setStyle("color", "var(--jettra-accent)").setStyle("font-weight", "bold"));
        
        TextBox scannedResult = new TextBox("text", "scannedResult");
        scannedResult.setId("scannedResult");
        scannedResult.setProperty("placeholder", "Scanned QR code text will appear here...");
        scannedResult.setStyle("width", "100%").setStyle("padding", "10px").setStyle("border-radius", "8px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)").setStyle("color", "var(--jettra-text)");
        formGroup.add(scannedResult);
        playground.add(formGroup);

        // Instantating QRReader
        QRReader qrReader = new QRReader("playgroundQrScanner")
            .setWidth("350px")
            .setHeight("300px")
            .setOnScan("function(code) { " +
                       "  document.getElementById('scannedResult').value = code; " +
                       "}");
        playground.add(qrReader);
        container.add(playground);
        
        center.add(container);
    }
}
