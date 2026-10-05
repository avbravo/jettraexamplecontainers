package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Board;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.core.UIComponent;

public class BoardPage extends DashboardBasePage {

    public BoardPage() {
        super("Board Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "25px");
        
        Header h1 = new Header(1, "Board Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("View Code");
        codeBtn.addClass("j-btn-primary");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal-board').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Code Modal ---
        Modal codeModal = new Modal("code-modal-board");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(15px)")
                 .setStyle("padding", "30px").setStyle("border-radius", "15px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)");
        
        codeModal.add(new Header(3, "Board Implementation").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        String javaCode = "Board board = new Board(\"Project Alpha\", 3);\n" +
                          "board.addComponent(0, new Card(\"Task 1\", \"Initialize project\"));\n" +
                          "board.addComponent(1, new Card(\"Task 2\", \"In development\"));\n" +
                          "board.addComponent(2, new Card(\"Task 3\", \"Quality assurance\"));\n" +
                          "center.add(board);";
                          
        Div codeBox = new Div();
        codeBox.setStyle("background", "rgba(0,0,0,0.5)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("margin", "20px 0");
        UIComponent pre = new UIComponent("pre") {};
        pre.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        pre.setStyle("color", "#0ff").setStyle("font-family", "monospace");
        codeBox.add(pre);
        codeModal.add(codeBox);
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal-board').style.display = 'none'");
        codeModal.add(closeBtn);
        
        container.add(codeModal);

        // --- Demo ---
        Paragraph intro = new Paragraph("The Board component provides a high-level layout for organizing content into columns, perfect for management tools and complex dashboards.");
        intro.setStyle("margin-bottom", "30px").setStyle("opacity", "0.8");
        container.add(intro);

        Board demoBoard = new Board("Interactive Workspace", 3);
        
        // Col 1
        Div card1 = createDemoCard("🚀 Infrastructure", "Set up Docker containers and CI/CD pipelines.", "DevOps", "5d");
        Div card2 = createDemoCard("🎨 UI Design", "Premium 3D-styled theme for the main dashboard.", "Design", "3d");
        demoBoard.addComponent(0, card1);
        demoBoard.addComponent(0, card2);

        // Col 2
        Div card3 = createDemoCard("⚡ API Layer", "Implement GraphQL endpoint for user data.", "Backend", "4d");
        demoBoard.addComponent(1, card3);

        // Col 3
        Div card4 = createDemoCard("✅ Security Audit", "Final review of authentication protocols.", "Security", "2d");
        demoBoard.addComponent(2, card4);

        container.add(demoBoard);
        center.add(container);
    }

    private Div createDemoCard(String title, String desc, String group, String est) {
        Div card = new Div();
        card.setStyle("background", "rgba(255,255,255,0.05)").setStyle("padding", "15px").setStyle("border-radius", "12px")
            .setStyle("border", "1px solid rgba(255,255,255,0.1)").setStyle("box-shadow", "0 4px 6px rgba(0,0,0,0.1)");
        
        Header h = new Header(5, title);
        h.setStyle("margin", "0 0 8px 0").setStyle("color", "#fff");
        
        Paragraph p = new Paragraph(desc);
        p.setStyle("font-size", "0.85rem").setStyle("margin-bottom", "12px").setStyle("opacity", "0.7");
        
        Div footer = new Div();
        footer.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("font-size", "0.75rem");
        
        io.jettra.wui.components.Span g = new io.jettra.wui.components.Span(group);
        g.setStyle("color", "var(--jettra-accent)").setStyle("font-weight", "bold");
        
        io.jettra.wui.components.Span e = new io.jettra.wui.components.Span("⏱ " + est);
        e.setStyle("opacity", "0.6");
        
        footer.add(g).add(e);
        card.add(h).add(p).add(footer);
        return card;
    }
}
