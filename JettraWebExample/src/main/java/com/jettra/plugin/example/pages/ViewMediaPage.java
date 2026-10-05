package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.ViewMedia;

public class ViewMediaPage extends DashboardBasePage {

    public ViewMediaPage() {
        super("ViewMedia Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "ViewMedia Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal-viewmedia').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Code Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("code-modal-viewmedia");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)");
        
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "ViewMedia video = new ViewMedia(\"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4\", true);\n" +
                          "video.setAutoplay(false);\n" +
                          "center.add(video);";
                          
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "java-code-viewmedia");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('java-code-viewmedia').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal-viewmedia').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        
        // --- Actual Component Demo ---
        container.add(new Header(2, "ViewMedia Example").setStyle("margin-top", "30px"));
        container.add(new Paragraph("The ViewMedia component embeds native HTML5 video and audio playback.").setStyle("margin-bottom", "20px"));
        
        Div row1 = new Div();
        row1.setStyle("margin-bottom", "30px").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "20px");
        
        ViewMedia videoPlayer = new ViewMedia("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", true);
        videoPlayer.setStyle("border-radius", "8px").setStyle("border", "1px solid var(--jettra-accent)").setStyle("width", "100%").setStyle("max-width", "600px");
        
        ViewMedia audioPlayer = new ViewMedia("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", false);
        audioPlayer.setStyle("width", "100%").setStyle("max-width", "400px");
        
        row1.add(new Header(4, "Video Player").setStyle("margin", "0"));
        row1.add(videoPlayer);
        
        row1.add(new Header(4, "Audio Player").setStyle("margin", "10px 0 0 0"));
        row1.add(audioPlayer);
        
        container.add(row1);
        
        center.add(container);
    }
}
