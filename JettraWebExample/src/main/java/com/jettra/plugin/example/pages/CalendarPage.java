package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class CalendarPage extends DashboardBasePage {

    public CalendarPage() {
        super("Calendar Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Calendar Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('calendar-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        Modal codeModal = new Modal("calendar-code-modal");
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
        
        String javaCode = "Calendar monthCal = new Calendar();\\n" +
                          "monthCal.setStyle(\"width\", \"400px\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "calendar-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('calendar-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('calendar-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        Paragraph p = new Paragraph("The Calendar component generates a localized month calendar built entirely in Java elements, requiring zero libraries.");
        container.add(p);
        container.add(new Divide());

        container.add(new Header(3, "Demo"));
        
        Calendar mp = new Calendar();
        mp.setStyle("width", "500px");
        
        container.add(mp);
        
        Button addEventBtn = new Button("⚡ Add Event");
        addEventBtn.addClass("dash-btn-3d");
        addEventBtn.setProperty("onclick", "document.getElementById('edit-cal-event-modal').style.display='block'");
        
        container.add(new Div().setStyle("margin-top", "20px").add(addEventBtn));

        // Add Event Modal (3D)
        Modal editModal = new Modal("edit-cal-event-modal");
        editModal.addClass("dash-modal-3d");
        editModal.setStyle("display", "none").setStyle("padding", "30px")
                 .setStyle("width", "90%").setStyle("max-width", "450px")
                 .setStyle("position", "absolute").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)");
                 
        editModal.add(new Header(3, "Add Event").setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("transform", "translateZ(30px)").setStyle("font-weight", "800"));
        
        Div formLayout = new Div();
        formLayout.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "15px").setStyle("transform", "translateZ(20px)");
        
        TextBox titleBox = new TextBox("calEventTitle", "Event Title");
        titleBox.setProperty("id", "calEventTitle");
        formLayout.add(titleBox);
        
        DatePicker dp = new DatePicker("calEventDate", "Date");
        dp.setType("date");
        formLayout.add(dp);
        
        editModal.add(formLayout);
        
        Div editActions = new Div();
        editActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px").setStyle("margin-top", "20px").setStyle("transform", "translateZ(30px)");
        
        Button cancelEditBtn = new Button("Cancel");
        cancelEditBtn.addClass("j-btn");
        cancelEditBtn.setStyle("background", "rgba(255,255,255,0.1)").setStyle("border", "1px solid var(--jettra-border)");
        cancelEditBtn.setProperty("onclick", "document.getElementById('edit-cal-event-modal').style.display='none'");
        
        Button saveEditBtn = new Button("Save Event");
        saveEditBtn.addClass("dash-btn-3d");
        saveEditBtn.setProperty("onclick", "document.getElementById('edit-cal-event-modal').style.display='none'; "
            + "var t=document.getElementById('calEventTitle').value; "
            + "var d=document.createElement('div'); d.style='background:var(--jettra-accent);color:#000;font-size:10px;padding:2px;margin-top:2px;border-radius:2px;'; d.innerText=t; "
            + "var cells=document.querySelectorAll('.calendar-day'); "
            + "if(cells.length > 0) { cells[Math.floor(Math.random()*cells.length)].appendChild(d); } "
            + "window.show3DMessage('Calendar Event', 'Event \\''+t+'\\' added to the calendar successfully!');");
        
        editActions.add(cancelEditBtn).add(saveEditBtn);
        editModal.add(editActions);
        
        container.add(editModal);
        
        center.add(container);
    }
}
