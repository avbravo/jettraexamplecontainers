package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class SchedulePage extends DashboardBasePage {

    public SchedulePage() {
        super("Schedule Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Schedule Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('schedule-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        Modal codeModal = new Modal("schedule-code-modal");
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
        
        String javaCode = "Schedule s = new Schedule();\\n" +
                          "s.addEvent(\"Daily Standup\", \"Mon\", \"9:00\", \"window.show3DMessage('Sync', 'Hi')\");\\n" +
                          "s.addEvent(\"Product Sync\", \"Mon\", \"11:00\");\\n" +
                          "s.addEvent(\"Review Session\", \"Thu\", \"14:00\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "schedule-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('schedule-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('schedule-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        Paragraph p = new Paragraph("The Schedule component generates a fast weekly agenda board built via 3D CSS grids natively in Java.");
        container.add(p);
        container.add(new Divide());

        container.add(new Header(3, "Demo"));
        
        Schedule sched = new Schedule();
        sched.addEvent("Daily Sync", "Mon", "9:00", "document.getElementById('eventTitle').value='Daily Sync'; document.getElementById('edit-event-modal').style.display='block'; window.show3DMessage('Event Editing', 'Opening Daily Sync for editing');");
        sched.addEvent("Client Meeting", "Mon", "13:00", "document.getElementById('eventTitle').value='Client Meeting'; document.getElementById('edit-event-modal').style.display='block'; window.show3DMessage('Event Editing', 'Opening Client Meeting for editing');");
        sched.addEvent("Review", "Wed", "10:00", "document.getElementById('eventTitle').value='Review'; document.getElementById('edit-event-modal').style.display='block'; window.show3DMessage('Event Editing', 'Opening Review for editing');");
        sched.addEvent("Deploy", "Fri", "16:00", "document.getElementById('eventTitle').value='Deploy'; document.getElementById('edit-event-modal').style.display='block'; window.show3DMessage('Event Editing', 'Deploying to prod 🚀');");
        
        container.add(sched);
        
        Button addEventBtn = new Button("⚡ Add Event");
        addEventBtn.addClass("j-btn-primary");
        addEventBtn.setProperty("onclick", "document.getElementById('eventTitle').value=''; document.getElementById('edit-event-modal').style.display='block'");
        
        container.add(new Div().setStyle("margin-top", "20px").add(addEventBtn));

        // Edit Event Modal
        // Edit Event Modal
        Modal editModal = new Modal("edit-event-modal");
        editModal.addClass("dash-modal-3d");
        editModal.setStyle("display", "none").setStyle("padding", "30px")
                 .setStyle("width", "90%").setStyle("max-width", "450px")
                 .setStyle("position", "absolute").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)");
                 
        editModal.add(new Header(3, "Add / Edit Event").setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("transform", "translateZ(30px)").setStyle("font-weight", "800").setStyle("text-shadow", "0 0 10px rgba(0,255,255,0.4)"));
        
        Div formLayout = new Div();
        formLayout.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "15px").setStyle("transform", "translateZ(20px)");
        
        TextBox titleBox = new TextBox("eventTitle", "Event Title");
        titleBox.setProperty("id", "eventTitle");
        formLayout.add(titleBox);
        
        SelectOne daySelect = new SelectOne("eventDay");
        daySelect.addOption("Mon", "Mon");
        daySelect.addOption("Tue", "Tue");
        daySelect.addOption("Wed", "Wed");
        daySelect.addOption("Thu", "Thu");
        daySelect.addOption("Fri", "Fri");
        formLayout.add(daySelect);
        
        Time evtTime = new Time("eventTime", "Time");
        evtTime.setEditable(true);
        evtTime.setType("time");
        formLayout.add(evtTime);
        
        editModal.add(formLayout);
        
        Div editActions = new Div();
        editActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px").setStyle("margin-top", "20px").setStyle("transform", "translateZ(30px)");
        
        Button cancelEditBtn = new Button("Cancel");
        cancelEditBtn.addClass("j-btn");
        cancelEditBtn.setStyle("background", "rgba(255,255,255,0.1)").setStyle("border", "1px solid var(--jettra-border)");
        cancelEditBtn.setProperty("onclick", "document.getElementById('edit-event-modal').style.display='none'");
        
        Button saveEditBtn = new Button("Save Event");
        saveEditBtn.addClass("dash-btn-3d");
        saveEditBtn.setProperty("onclick", "document.getElementById('edit-event-modal').style.display='none'; "
            + "var t=document.getElementById('eventTitle').value; "
            + "var p=document.createElement('div'); p.style='background:var(--jettra-accent);color:#000;padding:5px;margin-top:5px;border-radius:4px;cursor:pointer;'; p.innerText=t; p.onclick=function(){window.show3DMessage('Event', 'Edit '+t)}; "
            + "var sc=document.querySelector('div[style*=\\'weekly\\']') || document.querySelector('.j-schedule') || document.body; "
            + "if(sc) sc.appendChild(p); "
            + "window.show3DMessage('Event Saved', 'The event \\''+t+'\\' has been securely saved to the schedule.');");
        
        editActions.add(cancelEditBtn).add(saveEditBtn);
        editModal.add(editActions);
        
        container.add(editModal);
        
        center.add(container);
    }
}
