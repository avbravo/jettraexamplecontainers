package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.SelectMany;
import io.jettra.wui.core.UIComponent;

/**
 * Showcase page for the new SelectMany component.
 */
public class SelectManyPageOld extends DashboardBasePage {

    public SelectManyPageOld() {
        super("SelectMany Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "SelectMany Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('selectmany-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("selectmany-code-modal");
        
        // Modal style for centered, draggable, and scrolling (matching CardPage's new requirements)
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
        modalH3.setProperty("id", "selectmany-modal-header");
        modalH3.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)").setStyle("cursor", "move");
        codeModal.add(modalH3);
        
        // Draggable script
        UIComponent dragScript = new UIComponent("script") {};
        dragScript.setContent(
            "const smMod = document.getElementById('selectmany-code-modal');" +
            "const smHrd = document.getElementById('selectmany-modal-header');" +
            "let smDragging = false; let smX=0; let smY=0;" +
            "smHrd.onmousedown = (e) => { " +
            "  smDragging=true; smX=e.clientX; smY=e.clientY;" +
            "  if (smMod.style.transform.includes('translate')) {" +
            "    const rect = smMod.getBoundingClientRect();" +
            "    smMod.style.transform = 'none';" +
            "    smMod.style.left = rect.left + 'px';" +
            "    smMod.style.top = rect.top + 'px';" +
            "  }" +
            "};" +
            "window.addEventListener('mouseup', () => { smDragging=false; });" +
            "window.addEventListener('mousemove', (e) => { " +
            "  if(!smDragging) return;" +
            "  const dx = e.clientX - smX; const dy = e.clientY - smY; smX=e.clientX; smY=e.clientY;" +
            "  const rect = smMod.getBoundingClientRect();" +
            "  smMod.style.left = (rect.left + dx) + 'px';" +
            "  smMod.style.top = (rect.top + dy) + 'px';" +
            "});"
        );
        codeModal.add(dragScript);
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-y", "auto")
                     .setStyle("max-height", "60vh")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// 1. Basic SelectMany Component\n" +
                          "SelectMany selectMany = new SelectMany(\"ciudadesVisitar\");\n" +
                          "selectMany.addOption(\"PTY\", \"Panamá\");\n" +
                          "selectMany.addOption(\"MAD\", \"Madrid\");\n" +
                          "selectMany.addOption(\"SJO\", \"San José\");\n" +
                          "selectMany.addOption(\"BOG\", \"Bogotá\");\n\n" +
                          "// 2. Setting Default Selection\n" +
                          "selectMany.setDefault(\"MAD\");\n\n" +
                          "// 3. Styling\n" +
                          "selectMany.setStyle(\"width\", \"300px\");\n" +
                          "selectMany.setStyle(\"height\", \"150px\");\n\n" +
                          "// 4. Runtime Item Addition\n" +
                          "selectMany.setAllowAddItem(true);\n";
                          
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "selectmany-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copiar código");
        copyBtn.addClass("j-btn-primary");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('selectmany-java-code').innerText).then(() => { this.innerText='¡Copiado!'; setTimeout(() => this.innerText='Copiar código', 2000); })");
        
        Button closeBtn = new Button("Cerrar");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('selectmany-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The SelectMany component generates a standard `<select multiple>` element in HTML, enabling users to select various options simultaneously.");
        container.add(p);

        // --- Demo ---
        container.add(new Header(3, "SelectMany Example"));
        Div demoArea = new Div();
        demoArea.setStyle("padding", "20px").setStyle("background", "rgba(0,0,0,0.2)").setStyle("border", "1px solid rgba(255,255,255,0.1)").setStyle("border-radius", "8px").setStyle("margin-bottom", "30px");
        
        Paragraph instruction = new Paragraph("Help: Hold Ctrl/Cmd or Shift to select multiple elements.");
        instruction.setStyle("font-size", "12px").setStyle("color", "#aaa");
        demoArea.add(instruction);
        
        demoArea.add(new Header(4, "Available Options (Default: Madrid)"));
        SelectMany selectMany = new SelectMany("ciudades").setInline(false);
        selectMany.addOption("PTY", "Panamá");
        selectMany.addOption("MAD", "Madrid");
        selectMany.addOption("SJO", "San José");
        selectMany.addOption("BOG", "Bogotá");
        selectMany.addOption("MIA", "Miami");
        selectMany.addOption("MEX", "Ciudad de México");
        selectMany.addOption("BCN", "Barcelona");
        selectMany.addOption("ROM", "Roma");
        
        selectMany.setAllowAddItem(true);
        selectMany.setDefault("MAD");
        demoArea.add(selectMany);
        container.add(demoArea);
        
        center.add(container);
    }
}
