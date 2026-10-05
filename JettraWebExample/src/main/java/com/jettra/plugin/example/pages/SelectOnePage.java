package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.SelectOne;
import io.jettra.wui.components.Button;

public class SelectOnePage extends DashboardBasePage {

    public SelectOnePage() {
        super("SelectOne Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");

        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between")
                .setStyle("align-items", "center").setStyle("margin-bottom", "15px");

        Header h1 = new Header(1, "SelectOne Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('select-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);

        container.add(headerRow);

        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("select-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                .setStyle("backdrop-filter", "blur(10px)")
                .setStyle("padding", "20px").setStyle("border-radius", "8px")
                .setStyle("width", "90%").setStyle("max-width", "800px")
                .setStyle("border", "1px solid var(--jettra-border)");

        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color",
                "var(--jettra-accent)"));

        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");

        String javaCode = "SelectOne so = new SelectOne(\"country\");\nso.addOption(\"us\", \"United States\");\nso.addOption(\"es\", \"Spain\");\nso.addOption(\"mx\", \"Mexico\");";

        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {
        };
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {
        };
        codeTag.setProperty("id", "select-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));

        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);

        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");

        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick",
                "navigator.clipboard.writeText(document.getElementById('select-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");

        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('select-code-modal').style.display = 'none'");

        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);

        container.add(codeModal);
        // --- End Modal Dialog ---

        Paragraph p = new Paragraph("The SelectOne dropdown menu for picking one option.");
        container.add(p);

        // --- Demo ---
        container.add(new Header(3, "Demo"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");

        SelectOne so = new SelectOne("country");
        so.addOption("us", "United States");
        so.addOption("es", "Spain");
        so.addOption("mx", "Mexico");
        so.setAllowAddItem(true);

        row1.add(so);

        container.add(row1);

        center.add(container);
    }
}
