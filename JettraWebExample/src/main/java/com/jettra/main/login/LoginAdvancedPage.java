package com.jettra.main.login;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;

public class LoginAdvancedPage extends DashboardBasePage {

    public LoginAdvancedPage() {
        super("LoginAdvanced Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");

        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");

        Header h1 = new Header(1, "LoginAdvanced Component");
        h1.setStyle("margin", "0");
        headerRow.add(h1);

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal-logina').style.display = 'block'");
        headerRow.add(codeBtn);

        container.add(headerRow);

        // --- Code Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("code-modal-logina");
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

        String javaCode = "LoginAdvanced login = new LoginAdvanced(\"/loginSubmit\", \"/forgotPassword\", \"/register\", \"images/banner.png\", \"Title\");\n"
                + "page.add(login);";

        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {
        };
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {
        };
        codeTag.setProperty("id", "java-code-logina");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));

        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);

        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");

        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('java-code-logina').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");

        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal-logina').style.display = 'none'");

        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);

        container.add(codeModal);

        // --- Actual Component Demo ---
        container.add(new Header(2, "LoginAdvanced Example").setStyle("margin-top", "30px"));
        container.add(new Paragraph("A comprehensive login form with a side banner and links. We will not render it inline as it's a full-page layout component.").setStyle("margin-bottom", "20px"));

        center.add(container);
    }
}
