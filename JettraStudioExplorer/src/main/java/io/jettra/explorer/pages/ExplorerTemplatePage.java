package io.jettra.explorer.pages;

import io.jettra.explorer.model.ConnectionProfile;
import io.jettra.explorer.service.ConnectionManager;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.core.BasePage;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.markup.HtmlResourceLoader;

public abstract class ExplorerTemplatePage extends BasePage {

    private String userRole = "ADMIN";
    private String userName = "ClusterAdmin";

    public ExplorerTemplatePage() {
        this(new PageParameters());
    }

    public ExplorerTemplatePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (this.userRole == null) this.userRole = "ADMIN";
        if (this.userName == null) this.userName = "ClusterAdmin";

        add(new Label("headerTitle", getPageTitle() != null ? getPageTitle() : "JettraStore Explorer"));
        add(new Label("clusterStatusBadge", "● CLÚSTER EN LÍNEA (4 NODOS)"));

        var activeProf = ConnectionManager.getInstance().getActiveProfile().orElse(null);
        String serverLabel = activeProf != null ? "🔌 " + activeProf.getHost() + ":" + activeProf.getPort() : "🔌 127.0.0.1:9010";
        add(new Label("activeServerBadge", serverLabel));

        add(new Label("userNameBadge", userName));
        add(new Label("userRoleBadge", userRole));

        // LEFT Sidebar Navigation Links (Unified JettraStorePolice3D + JettraStudioExplorer)
        add(Link.of("navPolice3D", "/police3d"));
        add(Link.of("navPolice", "/police"));
        add(Link.of("navCluster", "/dashboard"));
        add(Link.of("navConnections", "/connections"));
        add(Link.of("navQuery", "/query"));
        add(Link.of("navDatabases", "/databases"));
        add(Link.of("navEngines", "/engines"));
        add(Link.of("navIndexes", "/indexes"));
        add(Link.of("navRecords", "/records"));
        add(Link.of("navUsers", "/users"));
        add(Link.of("navBackup", "/backup"));
        add(Link.of("navLogout", "/login?logout=true"));

        // FOOTER
        add(new Label("footerInfo", "JettraStore Explorer & Police 3D v1.0.0 | Arquitectura Loom Virtual Threads (Java 25) | JettraStoreDriver"));
    }

    @Override
    protected String loadMarkupForPage() {
        String childMarkup = (getCustomMarkup() != null)
            ? getCustomMarkup()
            : HtmlResourceLoader.getInstance().loadMarkup(this.getClass());

        String baseMarkup = HtmlResourceLoader.getInstance().loadMarkup(ExplorerTemplatePage.class);
        if (baseMarkup == null || baseMarkup.isBlank()) {
            baseMarkup = super.loadMarkupForPage();
        }

        if (baseMarkup != null && childMarkup != null) {
            return baseMarkup.replaceAll(
                "<(?:jettras:child|jettrat:child|jettra:child)\\s*(?:/>|>.*?</(?:jettras:child|jettrat:child|jettra:child)>)",
                java.util.regex.Matcher.quoteReplacement(childMarkup)
            );
        }
        return baseMarkup;
    }
}
