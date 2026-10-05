package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.components.Card;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;

/**
 * Showcase page for the new Card component.
 */
public class CardPage extends DashboardBasePage {

    public CardPage() {
        super("Card Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Card Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('card-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("card-code-modal");
        
        // Modal style for centered, draggable, and scrolling
        codeModal.setStyle("display", "none")
                 .setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)")
                 .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%")
                 .setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                 .setStyle("box-shadow", "0 10px 40px rgba(0,0,0,0.5)");
        
        Header modalH3 = new Header(3, "Java Code Examples");
        modalH3.setProperty("id", "card-modal-header");
        modalH3.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)").setStyle("cursor", "move");
        codeModal.add(modalH3);

        // Draggable script
        io.jettra.wui.core.UIComponent dragScript = new io.jettra.wui.core.UIComponent("script") {};
        dragScript.setContent(
            "const cardMod = document.getElementById('card-code-modal');" +
            "const cardHrd = document.getElementById('card-modal-header');" +
            "let cardDragging = false; let cardX=0; let cardY=0;" +
            "cardHrd.onmousedown = (e) => { " +
            "  cardDragging=true; cardX=e.clientX; cardY=e.clientY;" +
            "  if (cardMod.style.transform.includes('translate')) {" +
            "    const rect = cardMod.getBoundingClientRect();" +
            "    cardMod.style.transform = 'none';" +
            "    cardMod.style.left = rect.left + 'px';" +
            "    cardMod.style.top = rect.top + 'px';" +
            "  }" +
            "};" +
            "window.addEventListener('mouseup', () => { cardDragging=false; });" +
            "window.addEventListener('mousemove', (e) => { " +
            "  if(!cardDragging) return;" +
            "  const dx = e.clientX - cardX; const dy = e.clientY - cardY; cardX=e.clientX; cardY=e.clientY;" +
            "  const rect = cardMod.getBoundingClientRect();" +
            "  cardMod.style.left = (rect.left + dx) + 'px';" +
            "  cardMod.style.top = (rect.top + dy) + 'px';" +
            "});"
        );
        codeModal.add(dragScript);
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-y", "auto")
                     .setStyle("max-height", "60vh")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// 1. Basic Card\\n" +
                          "Card basicCard = new Card()\\n" +
                          "    .setTitle(\"Basic Card\")\\n" +
                          "    .setSubtitle(\"Simple Information\")\\n" +
                          "    .setContentText(\"This is a basic card with no image and no actions.\")\\n" +
                          "    .setWidth(\"100%\");\\n\\n" +
                          "// 2. Image Card\\n" +
                          "Card imageCard = new Card()\\n" +
                          "    .setTitle(\"Scenic View\")\\n" +
                          "    .setSubtitle(\"Travel & Nature\")\\n" +
                          "    .setImageUrl(\"https://images.unsplash.com/photo-1506744626753-1fa44df14f28?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80\")\\n" +
                          "    .setContentText(\"Discover beautiful places around the world.\")\\n" +
                          "    .setWidth(\"100%\");\\n\\n" +
                          "// 3. Action Card\\n" +
                          "Card actionCard = new Card()\\n" +
                          "    .setTitle(\"User Registration\")\\n" +
                          "    .setSubtitle(\"Create an account\")\\n" +
                          "    .setContentText(\"Sign up to get access to premium features.\")\\n" +
                          "    .setWidth(\"100%\");\\n" +
                          "Button acceptBtn = new Button(\"Accept\");\\n" +
                          "acceptBtn.setStyle(Button.Style.PRIMARY);\\n" +
                          "Button dismissBtn = new Button(\"Dismiss\");\\n" +
                          "actionCard.add(acceptBtn).add(dismissBtn);";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "card-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "\n"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('card-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('card-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The Card component is used to represent content visually in a block.");
        container.add(p);

        Div grid = new Div();
        grid.setStyle("display", "grid").setStyle("grid-template-columns", "repeat(auto-fit, minmax(300px, 1fr))").setStyle("gap", "20px").setStyle("margin-top", "30px");
        
        // 1. Basic Card
        Card basicCard = new Card()
            .setTitle("Basic Card")
            .setSubtitle("Simple Information")
            .setContentText("This is a basic card with no image and no actions.")
            .setWidth("100%");
            
        // 2. Image Card
        Card imageCard = new Card()
            .setTitle("Scenic View")
            .setSubtitle("Travel & Nature")
            .setImageUrl("https://images.unsplash.com/photo-1549880338-65ddcdfd017b?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
            .setContentText("Discover beautiful places around the world.")
            .setWidth("100%");
            
        // 3. Action Card
        Card actionCard = new Card()
            .setTitle("User Registration")
            .setSubtitle("Create an account")
            .setContentText("Sign up to get access to premium features.")
            .setWidth("100%");
            
        Button acceptBtn = new Button("Accept");
        acceptBtn.primary();
        Button dismissBtn = new Button("Dismiss");
        
        actionCard.add(acceptBtn).add(dismissBtn);
        
        grid.add(basicCard);
        grid.add(imageCard);
        grid.add(actionCard);
        
        container.add(grid);
        center.add(container);
    }
}
