package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Divide;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;

public class DividePage extends DashboardBasePage {

    public DividePage() {
        super("Divide Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Header h1 = new Header(1, "Divide Component Showcase");
        container.add(h1);
        
        container.add(new Paragraph("The Divide component provides a stylish horizontal separator to organize content Sections."));
        
        container.add(new Header(3, "Section 1"));
        container.add(new Paragraph("This is the first segment of content. below this, you will see the Divide component in action."));
        
        container.add(new Divide());
        
        container.add(new Header(3, "Section 2"));
        container.add(new Paragraph("This is the second segment of content, separated from the first one by the Divide component."));
        
        center.add(container);
    }
}
