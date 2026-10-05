package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;

public class FeedbackPage extends DashboardBasePage {
    public FeedbackPage() { super("Feedback Components"); }
    
    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div(); container.setStyle("padding", "30px");
        
        Div headerRow = new Div(); headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        Header h1 = new Header(1, "Feedback Showcase"); h1.setStyle("margin", "0"); headerRow.add(h1);
        Button codeBtn = new Button("Code"); codeBtn.addClass("j-btn").setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)").setProperty("onclick", "document.getElementById('feedback-code-modal').style.display = 'block'");
        headerRow.add(codeBtn); container.add(headerRow);
        
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("feedback-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)").setStyle("backdrop-filter", "blur(10px)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("width", "90%").setStyle("max-width", "800px").setStyle("border", "1px solid var(--jettra-border)");
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        Div codeContainer = new Div(); codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px").setStyle("border-radius", "4px").setStyle("overflow-x", "auto").setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// 1. Alert\n" +
                          "Alert alert = new Alert();\n" +
                          "alert.setType(\"success\");\n" +
                          "alert.showMessage(\"Success Message\");\n\n" +
                          "// 2. Modal\n" +
                          "Modal modal = new Modal(\"modal-id\");\n\n" +
                          "// 3. Notification (Toast)\n" +
                          "Notification notif = new Notification();\n" +
                          "notif.setType(\"info\");\n" +
                          "notif.showMessage(\"Toast Message\");";
                          
        UIComponent pre = new UIComponent("pre") {}; pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {}; codeTag.setProperty("id", "feedback-code").setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        pre.add(codeTag); codeContainer.add(pre); codeModal.add(codeContainer);
        Div modalActions = new Div(); modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        Button copyBtn = new Button("Copy"); copyBtn.addClass("j-btn").setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('feedback-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        Button closeBtn = new Button("Close"); closeBtn.addClass("j-btn").setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)").setProperty("onclick", "document.getElementById('feedback-code-modal').style.display = 'none'");
        modalActions.add(closeBtn).add(copyBtn); codeModal.add(modalActions); container.add(codeModal);

        container.add(new Header(2, "Alerts & Modals"));
        
        Alert aInfo = new Alert(); aInfo.setType("info"); aInfo.showMessage("This is an info alert.");
        Alert aSuccess = new Alert(); aSuccess.setType("success"); aSuccess.showMessage("This is a success alert.");
        Alert aWarning = new Alert(); aWarning.setType("warning"); aWarning.showMessage("This is a warning alert.");
        Alert aError = new Alert(); aError.setType("error"); aError.showMessage("This is an error alert.");
        
        container.add(aInfo).add(aSuccess).add(aWarning).add(aError);
        
        center.add(container);
    }
}
