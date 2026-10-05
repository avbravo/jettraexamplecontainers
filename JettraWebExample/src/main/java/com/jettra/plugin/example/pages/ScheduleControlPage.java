package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class ScheduleControlPage extends DashboardBasePage {
    public ScheduleControlPage() {
        super("Schedule Control");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Header h1 = new Header(1, "ScheduleControl Component");
        Paragraph subtitle = new Paragraph("Select a date and time for an event down to the second.");
        center.add(h1).add(subtitle).add(new Divide());

        ScheduleControl schedule = new ScheduleControl("mySchedule");
        schedule.setValue("2026-04-10T12:00:00").setShowTimeRemaining(true);
        schedule.setOnTimeReached("window.show3DMessage('Event started!', 'The selected time has arrived.');");

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='block'");

        Div row = new Div();
        row.setStyle("display", "flex").setStyle("gap", "20px").setStyle("align-items", "center");
        row.add(schedule).add(codeBtn);

        center.add(row);

        // Code Modal
        Modal codeModal = new Modal("code-modal");
        codeModal.add(new Header(3, "ScheduleControl Code"));
        String codeStr = "ScheduleControl schedule = new ScheduleControl(\"mySchedule\");\\n" +
                         "schedule.setValue(\"2026-04-10T12:00:00\");\\n" +
                         "row.add(schedule);";
        
        TextArea codeArea = new TextArea("codeArea", "");
        codeArea.setStyle("width", "100%").setStyle("height", "150px").setStyle("font-family", "monospace");
        codeArea.setValue(codeStr.replace("\\n", "\n"));
        
        Button copyBtn = new Button("Copiar codigo");
        copyBtn.addClass("j-btn");
        copyBtn.setStyle("background", "var(--jettra-accent)").setStyle("color", "#fff");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(`" + codeStr + "`).then(() => alert('Code copied!'))");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='none'");
        
        Div actions = new Div();
        actions.setStyle("margin-top", "15px").setStyle("display", "flex").setStyle("gap", "10px");
        actions.add(copyBtn).add(closeBtn);
        
        codeModal.add(codeArea).add(actions);
        center.add(codeModal);
    }
}
