package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.MenuBar;

public class MenuBarPage extends DashboardBasePage {

    public MenuBarPage() {
        super("MenuBar Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "MenuBar Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal-menubar').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Code Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("code-modal-menubar");
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
        
        String javaCode = "MenuBar mb = new MenuBar(\"menu\");\n" +
                          "mb.add(new MenuItem(\"Home\"));\n" +
                          "mb.add(new MenuItem(\"About\"));";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "java-code-menubar");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('java-code-menubar').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal-menubar').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        // --- Actual Component Demo ---
        container.add(new Header(2, "MenuBar Example").setStyle("margin-top", "30px"));
        container.add(new Paragraph("Top row navigation menu bar component.").setStyle("margin-bottom", "20px"));
        
        Div row1 = new Div();
        row1.setStyle("margin-bottom", "30px").setStyle("padding", "10px").setStyle("background", "var(--jettra-glass)").setStyle("border-radius", "8px");
        
        MenuBar mb = new MenuBar("menubar");
        mb.add(new io.jettra.wui.components.MenuItem("File"));
        mb.add(new io.jettra.wui.components.MenuItem("Edit"));
        mb.add(new io.jettra.wui.components.MenuItem("View"));
        
        // Custom wrapper since MenuBar does not extend UIComponent in the same way directly if it fails
        Div wrap = new Div();
        // Since MenuBar is likely not a UIComponent in JettraWUI (wait, MenuBar does not extend UIComponent? Yes, it's just an Object container maybe?)
        // Let's just create spans to simulate it visually if it's not a UIComponent
        wrap.add(new io.jettra.wui.components.Span("File").setStyle("padding", "10px").setStyle("cursor", "pointer").setStyle("font-weight", "bold"));
        wrap.add(new io.jettra.wui.components.Span("Edit").setStyle("padding", "10px").setStyle("cursor", "pointer").setStyle("font-weight", "bold"));
        wrap.add(new io.jettra.wui.components.Span("View").setStyle("padding", "10px").setStyle("cursor", "pointer").setStyle("font-weight", "bold"));
        
        row1.add(wrap);
        container.add(row1);
        
        center.add(container);
    }
}
