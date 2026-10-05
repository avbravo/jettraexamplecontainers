package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.FileUpload;

/**
 * Showcase page for the FileUpload component.
 */
public class FileUploadPage extends DashboardBasePage {

    public FileUploadPage() {
        super("FileUpload Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "FileUpload Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('fileupload-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);

        // --- Code Modal ---
        Modal codeModal = new Modal("fileupload-code-modal");
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
        
        String javaCode = "// 1. Auto-upload upon Selection\n" +
                          "FileUpload autoUpload = new FileUpload(\"auto-upload\")\n" +
                          "    .setAutoUpload(true)\n" +
                          "    .setDestination(\"/home/user/auto-uploads\");\n\n" +
                          "// 2. Manual Upload (with Confirmation Button)\n" +
                          "FileUpload manualUpload = new FileUpload(\"manual-upload\")\n" +
                          "    .setAutoUpload(false)\n" +
                          "    .setDestination(\"/home/user/manual-uploads\");\n\n" +
                          "// 3. Restricted File types (Images Only)\n" +
                          "FileUpload imageUpload = new FileUpload(\"image-upload\").setAccept(\"image/*\");";
                           
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "fileupload-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('fileupload-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('fileupload-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Code Modal ---

        container.add(new Paragraph("The FileUpload component allows users to select files from their device."));
        
        container.add(new Header(3, "Auto-upload upon Selection"));
        FileUpload autoUpload = new FileUpload("auto-upload")
            .setAutoUpload(true)
            .setDestination("/home/user/auto-uploads");
        container.add(autoUpload);
        
        container.add(new Header(3, "Manual Upload (with Confirmation Button)"));
        FileUpload manualUpload = new FileUpload("manual-upload")
            .setAutoUpload(false)
            .setDestination("/home/user/manual-uploads");
        container.add(manualUpload);
        
        container.add(new Header(3, "Multiple File Selection"));
        FileUpload multiUpload = new FileUpload("multi-upload").setMultiple(true);
        container.add(multiUpload);

        container.add(new Header(3, "Restricted File types (Images Only)"));
        FileUpload imageUpload = new FileUpload("image-upload").setAccept("image/*");
        container.add(imageUpload);
        
        center.add(container);
    }
}
