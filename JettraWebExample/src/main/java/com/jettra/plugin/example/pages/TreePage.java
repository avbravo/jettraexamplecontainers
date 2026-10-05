package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Tree;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;

/**
 * Showcase page for the Tree component showing countries by continent.
 */
public class TreePage extends DashboardBasePage {

    public TreePage() {
        super("Tree Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Tree Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('tree-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);

        // --- Code Modal ---
        Modal codeModal = new Modal("tree-code-modal");
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
        
        String javaCode = "// 1. Create a Tree\n" +
                          "Tree tree = new Tree();\n\n" +
                          "// 2. Create TreeItems\n" +
                          "Tree.TreeItem root = new Tree.TreeItem(\"World\");\n" +
                          "Tree.TreeItem continent = new Tree.TreeItem(\"America\");\n" +
                          "continent.addItem(new Tree.TreeItem(\"USA\"));\n" +
                          "continent.addItem(new Tree.TreeItem(\"Brazil\"));\n\n" +
                          "// 3. Assemble the hierarchy\n" +
                          "root.addItem(continent);\n" +
                          "tree.add(root);\n\n" +
                          "// 4. Add to container\n" +
                          "container.add(tree);";
                           
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "tree-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('tree-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('tree-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Code Modal ---

        container.add(new Paragraph("The Tree component displays hierarchical data in an interactive, expandable tree structure."));
        
        container.add(new Header(3, "Static Example (Countries by Continent)"));
        Tree tree1 = new Tree();
        tree1.setStyle("background", "rgba(0,0,0,0.2)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("border", "1px solid var(--jettra-border)").setStyle("margin-bottom", "30px");
        
        Tree.TreeItem world = new Tree.TreeItem("World");
        
        // America
        Tree.TreeItem america = new Tree.TreeItem("America");
        america.addItem(new Tree.TreeItem("USA"))
               .addItem(new Tree.TreeItem("Canada"))
               .addItem(new Tree.TreeItem("Mexico"))
               .addItem(new Tree.TreeItem("Brazil"))
               .addItem(new Tree.TreeItem("Argentina"));
        
        // Europe
        Tree.TreeItem europe = new Tree.TreeItem("Europe");
        europe.addItem(new Tree.TreeItem("Spain"))
              .addItem(new Tree.TreeItem("France"))
              .addItem(new Tree.TreeItem("Germany"))
              .addItem(new Tree.TreeItem("Italy"))
              .addItem(new Tree.TreeItem("UK"));
        
        // Asia
        Tree.TreeItem asia = new Tree.TreeItem("Asia");
        asia.addItem(new Tree.TreeItem("China"))
            .addItem(new Tree.TreeItem("Japan"))
            .addItem(new Tree.TreeItem("India"))
            .addItem(new Tree.TreeItem("South Korea"));
        
        // Africa
        Tree.TreeItem africa = new Tree.TreeItem("Africa");
        africa.addItem(new Tree.TreeItem("Egypt"))
              .addItem(new Tree.TreeItem("Nigeria"))
              .addItem(new Tree.TreeItem("South Africa"));
        
        world.addItem(america).addItem(europe).addItem(asia).addItem(africa);
        world.setExpanded(true);
        tree1.add(world);
        container.add(tree1);

        container.add(new Header(3, "Dynamic Random Example"));
        Tree tree2 = new Tree();
        tree2.setStyle("background", "rgba(0,255,255,0.03)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("border", "1px solid rgba(0,255,255,0.2)");
        
        Tree.TreeItem randomRoot = new Tree.TreeItem("Generated Geographical Data");
        generateContinentalTree(randomRoot);
        randomRoot.setExpanded(true);
        tree2.add(randomRoot);
        container.add(tree2);
        
        center.add(container);
    }

    private void generateContinentalTree(Tree.TreeItem parent) {
        String[] continents = {"America", "Europe", "Asia", "Africa", "Oceania"};
        String[][] countries = {
            {"USA", "Canada", "Mexico", "Brazil", "Argentina", "Chile", "Colombia"},
            {"Spain", "France", "Germany", "Italy", "UK", "Portugal", "Netherlands"},
            {"China", "Japan", "India", "South Korea", "Vietnam", "Thailand"},
            {"Egypt", "Nigeria", "South Africa", "Kenya", "Morocco"},
            {"Australia", "New Zealand", "Fiji"}
        };

        for (int i = 0; i < continents.length; i++) {
            Tree.TreeItem continent = new Tree.TreeItem(continents[i]);
            continent.setExpanded(true);
            parent.addItem(continent);
            
            // Randomly select countries
            int count = (int) (Math.random() * 4) + 2;
            java.util.List<String> countryList = new java.util.ArrayList<>(java.util.Arrays.asList(countries[i]));
            java.util.Collections.shuffle(countryList);
            
            for (int j = 0; j < Math.min(count, countryList.size()); j++) {
                continent.addItem(new Tree.TreeItem(countryList.get(j)));
            }
        }
    }
}
