package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class OrganigramPage extends DashboardBasePage {

    public OrganigramPage() {
        super("Organigram Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Organigram Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('org-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        Modal codeModal = new Modal("org-code-modal");
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
        
        String javaCode = "Organigram org = new Organigram();\\n" +
                          "org.setRoot(\"CEO\", \"Alice\");\\n" +
                          "Organigram.OrgNode dev = org.getRoot().addChild(\"Head Dev\", \"Bob\");\\n" +
                          "dev.addChild(\"Frontend\", \"Charlie\", \"window.show3DMessage('Team', 'Frontend')\");\\n" +
                          "dev.addChild(\"Backend\", \"David\");";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "org-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('org-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('org-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        Paragraph p = new Paragraph("The Organigram component builds an interactive tree utilizing specialized CSS layout nodes seamlessly wrapped via Java.");
        container.add(p);
        container.add(new Divide());

        container.add(new Header(3, "Demo"));
        
        Organigram org = new Organigram();
        org.setRoot("CEO", "JettraStack Founder");
        
        Organigram.OrgNode tech = org.getRoot().addChild("CTO", "Tech Lead");
        tech.addChild("Frontend Dev", "React/Java", "document.getElementById('nodeTitle').value='Frontend Dev'; document.getElementById('nodeName').value='React/Java'; document.getElementById('edit-node-modal').style.display='block'; window.show3DMessage('Frontend Team', 'Frontend focused on UI/UX');");
        tech.addChild("Backend Dev", "Spring/Java", "document.getElementById('nodeTitle').value='Backend Dev'; document.getElementById('nodeName').value='Spring/Java'; document.getElementById('edit-node-modal').style.display='block'; window.show3DMessage('Backend Team', 'Backend focused on APIs and Logic');");
        
        Organigram.OrgNode ops = org.getRoot().addChild("COO", "Operations");
        ops.addChild("HR", "Recruitment", "document.getElementById('nodeTitle').value='HR'; document.getElementById('nodeName').value='Recruitment'; document.getElementById('edit-node-modal').style.display='block'; window.show3DMessage('Human Resources', 'Managing company culture and hiring');");
        
        container.add(org);
        
        Button addNodeBtn = new Button("⚡ Add Node");
        addNodeBtn.addClass("j-btn-primary");
        addNodeBtn.setProperty("onclick", "document.getElementById('nodeTitle').value=''; document.getElementById('nodeName').value=''; document.getElementById('edit-node-modal').style.display='block'");
        
        container.add(new Div().setStyle("margin-top", "20px").add(addNodeBtn));

        // Edit Node Modal
        // Edit Node Modal
        Modal editModal = new Modal("edit-node-modal");
        editModal.addClass("dash-modal-3d");
        editModal.setStyle("display", "none").setStyle("padding", "30px")
                 .setStyle("width", "90%").setStyle("max-width", "450px")
                 .setStyle("position", "absolute").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)");
                 
        editModal.add(new Header(3, "Add / Edit Node").setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("transform", "translateZ(30px)").setStyle("font-weight", "800"));
        
        Div formLayout = new Div();
        formLayout.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "15px").setStyle("transform", "translateZ(20px)");
        
        TextBox titleBox = new TextBox("nodeTitle", "Title / Role");
        titleBox.setProperty("id", "nodeTitle");
        formLayout.add(titleBox);
        
        TextBox nameBox = new TextBox("nodeName", "Person Name");
        nameBox.setProperty("id", "nodeName");
        formLayout.add(nameBox);
        
        SelectOne parentSelect = new SelectOne("parentNode");
        parentSelect.addOption("CEO", "CEO");
        parentSelect.addOption("CTO", "CTO");
        parentSelect.addOption("COO", "COO");
        formLayout.add(parentSelect);
        
        editModal.add(formLayout);
        
        Div editActions = new Div();
        editActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px").setStyle("margin-top", "20px").setStyle("transform", "translateZ(30px)");
        
        Button cancelEditBtn = new Button("Cancel");
        cancelEditBtn.addClass("j-btn");
        cancelEditBtn.setStyle("background", "rgba(255,255,255,0.1)").setStyle("border", "1px solid var(--jettra-border)");
        cancelEditBtn.setProperty("onclick", "document.getElementById('edit-node-modal').style.display='none'");
        
        Button saveEditBtn = new Button("Save Node");
        saveEditBtn.addClass("dash-btn-3d");
        saveEditBtn.setProperty("onclick", "document.getElementById('edit-node-modal').style.display='none'; "
            + "var t=document.getElementById('nodeTitle').value; "
            + "var n=document.createElement('div'); n.style='display:inline-block;padding:10px;border:1px solid var(--jettra-border);border-radius:6px;opacity:0.8;margin-top:10px;margin-left:10px;'; n.innerText=t; "
            + "var o=document.querySelector('.j-organigram') || document.body; "
            + "if(o) o.appendChild(n); "
            + "window.show3DMessage('Node Saved', 'Node \\''+t+'\\' added securely to Organigram.');");
        
        editActions.add(cancelEditBtn).add(saveEditBtn);
        editModal.add(editActions);
        
        container.add(editModal);
        
        center.add(container);
    }
}
