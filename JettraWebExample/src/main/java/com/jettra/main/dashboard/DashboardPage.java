package com.jettra.main.dashboard;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;

public class DashboardPage extends DashboardBasePage {

    public DashboardPage() {
        super("Dashboard");
    }

    @Override
    protected void initCenter(Center center, String username) {
        center.setContent("<h1>Main Workspace</h1><p>Status: All systems online. Jettra native servers operating at normal capacity. Welcome to the future of immersive dashboard control systems.</p>");
    }
}
