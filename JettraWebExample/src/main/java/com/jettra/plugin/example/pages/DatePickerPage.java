package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class DatePickerPage extends DashboardBasePage {

    public DatePickerPage() {
        super("DatePicker Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "DatePicker Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('datepicker-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        Modal codeModal = new Modal("datepicker-code-modal");
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
        
        String javaCode = "DatePicker fp = new DatePicker(\"startDate\", \"Start Date\");\\n" +
                          "fp.setType(\"datetime-local\");\\n" +
                          "fp.setEditable(true).setMin(\"2026-01-01\").setMax(\"2026-12-31\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "datepicker-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('datepicker-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('datepicker-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        Paragraph p = new Paragraph("The DatePicker component uses native HTML5 controls for accessible, lightweight date selection.");
        container.add(p);
        container.add(new Divide());

        container.add(new Header(3, "Demo"));
        
        Div layout = new Div();
        layout.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "20px");
        
        Div block = new Div();
        block.setStyle("display", "flex").setStyle("gap", "20px").setStyle("align-items", "center");
        
        DatePicker dp = new DatePicker("dob", "Date of Birth");
        dp.setType("datetime-local");
        dp.setFormat("yyyy-MM-dd HH:mm");
        dp.setEditable(true);
        dp.setValue("2026-04-06T10:30");
        dp.setProperty("onclick", "this.showPicker()");
        dp.setOnChange("window.show3DMessage('Selected Date', 'You selected: ' + this.value)");
        
        SelectOne formatSelect = new SelectOne("dtFormatSel");
        formatSelect.addOption("Format: datetime-local", "datetime-local");
        formatSelect.addOption("Format: date", "date");
        formatSelect.addOption("Format: month", "month");
        formatSelect.setProperty("onchange", "document.getElementById('dob').setAttribute('type', this.value); window.show3DMessage('Format Changed', 'Switched DatePicker to ' + this.value + ' format.');");
        
        block.add(dp).add(formatSelect);
        layout.add(block);
        
        container.add(layout);
        
        center.add(container);
    }
}
