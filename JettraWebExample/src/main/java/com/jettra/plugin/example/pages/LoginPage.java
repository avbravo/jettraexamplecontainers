package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.LoginUI;

/**
 * Showcase page for the LoginUI component.
 */
public class LoginPage extends DashboardBasePage {

    public LoginPage() {
        super("LoginUI Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "LoginUI Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        io.jettra.wui.components.Button codeBtn = new io.jettra.wui.components.Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('loginui-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("loginui-code-modal");
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
        
        String javaCode = "// 1. Basic LoginUI\n" +
                          "LoginUI loginUI = new LoginUI();\n" +
                          "loginUI.setId(\"login-component\");\n" +
                          "loginUI.setStyle(\"max-width\", \"400px\");\n\n" +
                          "// Add elements inside the LoginUI\n" +
                          "loginUI.add(new Header(3, \"Login to continue\"));\n" +
                          "loginUI.add(new io.jettra.wui.components.TextBox(\"username\", \"Username\"));\n" +
                          "loginUI.add(new io.jettra.wui.components.TextBox(\"password\", \"Password\").setProperty(\"type\", \"password\"));\n" +
                          "loginUI.add(new io.jettra.wui.components.Button(\"Login\").addClass(\"j-btn-primary\"));";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "loginui-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        io.jettra.wui.components.Button copyBtn = new io.jettra.wui.components.Button("Copy code");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('loginui-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy code', 2000); })");
        
        io.jettra.wui.components.Button closeBtn = new io.jettra.wui.components.Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('loginui-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The LoginUI component is used to generate a specialized container for login interfaces.");
        container.add(p);

        // --- Basic Types ---
        container.add(new Header(3, "Basic Example"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("justify-content", "center").setStyle("margin-bottom", "30px");
        
        LoginUI loginUI = new LoginUI();
        loginUI.setStyle("width", "100%").setStyle("max-width", "400px").setStyle("padding", "20px").setStyle("border", "1px solid var(--jettra-accent)").setStyle("border-radius", "8px").setStyle("background", "rgba(0,0,0,0.5)");
        
        Header loginHeader = new Header(3, "Welcome Back");
        loginHeader.setStyle("text-align", "center").setStyle("margin-top", "0");
        loginUI.add(loginHeader);
        
        io.jettra.wui.components.TextBox usernameField = new io.jettra.wui.components.TextBox("user", "Username");
        usernameField.addClass("j-input").setStyle("width", "100%").setStyle("margin-bottom", "15px");
        loginUI.add(usernameField);
        
        io.jettra.wui.components.TextBox passwordField = new io.jettra.wui.components.TextBox("pass", "Password");
        passwordField.addClass("j-input").setProperty("type", "password").setStyle("width", "100%").setStyle("margin-bottom", "20px");
        loginUI.add(passwordField);
        
        io.jettra.wui.components.Button loginBtn = new io.jettra.wui.components.Button("Log in");
        loginBtn.addClass("j-btn-primary").setStyle("width", "100%");
        loginUI.add(loginBtn);
        
        row1.add(loginUI);
        container.add(row1);

        center.add(container);
    }
}
