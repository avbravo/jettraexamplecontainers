package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Avatar;
import io.jettra.wui.complex.AvatarGroup;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;

/**
 * Showcase page for the new Avatar component.
 */
public class AvatarPage extends DashboardBasePage {

    public AvatarPage() {
        super("Avatar Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Avatar Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        io.jettra.wui.components.Button codeBtn = new io.jettra.wui.components.Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('avatar-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("avatar-code-modal");
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
        
        String javaCode = "// 1. Basic Avatar (Image, Label, Icon)\n" +
                          "Avatar.image(\"https://i.pravatar.cc/150?u=11\", \"User 1\").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE);\n" +
                          "Avatar.label(\"JD\", \"#4f46e5\").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE);\n" +
                          "Avatar.icon(\"<svg>...</svg>\").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE);\n\n" +
                          "// 2. Setting Shapes and Sizes\n" +
                          "Avatar.image(\"...\", \"User 2\").setShape(Avatar.Shape.ROUNDED).setSize(Avatar.Size.MD);\n\n" +
                          "// 3. Adding Badges\n" +
                          "Avatar.image(\"...\", \"Online\").setBadge(\"#22c55e\"); // Green dot\n\n" +
                          "// 4. Overlapping Avatar Group\n" +
                          "AvatarGroup group = new AvatarGroup();\n" +
                          "group.add(Avatar.image(\"https://i.pravatar.cc/150?u=21\", \"U1\").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));\n" +
                          "group.add(Avatar.label(\"+3\", \"#334155\").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "avatar-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        io.jettra.wui.components.Button copyBtn = new io.jettra.wui.components.Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('avatar-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        io.jettra.wui.components.Button closeBtn = new io.jettra.wui.components.Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('avatar-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---
        
        Paragraph p = new Paragraph("The Avatar component is used to represent a user or an entity through images, icons, or labels.");
        container.add(p);

        // --- Basic Types ---
        container.add(new Header(3, "Basic Types (Image, Label, Icon)"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row1.add(Avatar.image("https://i.pravatar.cc/150?u=11", "User 1").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        row1.add(Avatar.label("JD", "#4f46e5").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        row1.add(Avatar.icon("<svg width='32' height='32' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2'></path><circle cx='12' cy='7' r='4'></circle></svg>").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        container.add(row1);

        // --- Shapes ---
        container.add(new Header(3, "Shapes (Circle & Rounded)"));
        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row2.add(Avatar.image("https://i.pravatar.cc/150?u=2", "User 2").setShape(Avatar.Shape.CIRCLE).setSize(Avatar.Size.LG));
        row2.add(Avatar.image("https://i.pravatar.cc/150?u=3", "User 3").setShape(Avatar.Shape.ROUNDED).setSize(Avatar.Size.LG));
        container.add(row2);

        // --- Sizes ---
        container.add(new Header(3, "Sizes (XS to XL)"));
        Div row3 = new Div();
        row3.setStyle("display", "flex").setStyle("gap", "15px").setStyle("align-items", "center").setStyle("margin-bottom", "30px");
        
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=4", "XS").setSize(Avatar.Size.XS).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=5", "SM").setSize(Avatar.Size.SM).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=6", "MD").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=7", "LG").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=8", "XL").setSize(Avatar.Size.XL).setShape(Avatar.Shape.CIRCLE));
        container.add(row3);

        // --- Badges ---
        container.add(new Header(3, "With Status Badges"));
        Div row4 = new Div();
        row4.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row4.add(Avatar.image("https://i.pravatar.cc/150?u=9", "Online").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE).setBadge("#22c55e"));
        row4.add(Avatar.image("https://i.pravatar.cc/150?u=10", "Offline").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE).setBadge("#94a3b8"));
        row4.add(Avatar.image("https://i.pravatar.cc/150?u=11", "Busy").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE).setBadge("#ef4444"));
        container.add(row4);

        // --- Groups ---
        container.add(new Header(3, "Avatar Group (Overlapping)"));
        AvatarGroup group = new AvatarGroup();
        group.add(Avatar.image("https://i.pravatar.cc/150?u=21", "U1").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.image("https://i.pravatar.cc/150?u=22", "U2").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.image("https://i.pravatar.cc/150?u=23", "U3").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.image("https://i.pravatar.cc/150?u=24", "U4").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.label("+3", "#334155").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        container.add(group);
        
        center.add(container);
    }
}
