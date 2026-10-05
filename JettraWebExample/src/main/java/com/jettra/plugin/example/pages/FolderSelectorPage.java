package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.FolderSelector;

public class FolderSelectorPage extends DashboardBasePage {

    public FolderSelectorPage() {
        super("FolderSelector Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Header h1 = new Header(1, "FolderSelector Component Showcase");
        container.add(h1);
        
        container.add(new Paragraph("The FolderSelector allows managing references to local folders and their contents."));
        
        Header h3 = new Header(3, "Browse and Manage Folder Reference");
        
        Div flexContainer = new Div();
        flexContainer.setStyle("display", "flex").setStyle("gap", "30px").setStyle("margin-top", "20px");
        
        Div leftColumn = new Div();
        leftColumn.setStyle("flex", "1");
        
        FolderSelector selector = new FolderSelector("my-fs")
            .setReferenceLocation("/var/www/html/assets")
            .setReferenceContent("Images, CSS, JS")
            .style3D()
            .setConfirmUpload(true, "Explorador de Archivos", "¿Seguro que desea cargar esta carpeta?")
            .setTreeTarget("my-tree-display");
        
        leftColumn.add(h3).add(selector);
        
        Div rightColumn = new Div();
        rightColumn.setStyle("flex", "1").setStyle("background", "rgba(0,0,0,0.2)").setStyle("padding", "20px").setStyle("border-radius", "15px").setStyle("border", "1px solid rgba(0,255,255,0.1)");
        
        Header hTree = new Header(4, "Resolved Folder Structure");
        hTree.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0");
        
        io.jettra.wui.complex.Tree tree = new io.jettra.wui.complex.Tree();
        tree.setId("my-tree-display");
        
        rightColumn.add(hTree).add(tree);
        
        flexContainer.add(leftColumn).add(rightColumn);
        container.add(flexContainer);
        
        center.add(container);
    }
}
