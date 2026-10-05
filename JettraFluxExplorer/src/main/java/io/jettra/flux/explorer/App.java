package io.jettra.flux.explorer;

import io.jettra.flux.explorer.pages.*;
import io.jettra.server.JettraServer;
import io.jettra.server.config.ConfigInjector;
import io.jettra.server.config.JettraConfig;
import io.jettra.server.config.JettraConfigProperty;
import io.jettra.server.discoverer.DiscoveredLoad;

@DiscoveredLoad
public class App {

    @JettraConfigProperty(name = "app.title")
    private String appTitle;
    @JettraConfigProperty(name = "server.port")
    private String port;
    @JettraConfigProperty(name = "server.contextpath")
    private String contextpath;

    public static JettraServer serverInstance;

    public void initUI() {
        ConfigInjector.inject(this);
        System.out.println("Iniciando JettraFlux Explorer: " + appTitle);
    }

    public static void main(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--server.port=")) {
                    JettraConfig.setProperty("server.port", arg.substring("--server.port=".length()).trim());
                } else if (arg.startsWith("--port=") || arg.startsWith("-port=")) {
                    String val = arg.substring(arg.indexOf('=') + 1).trim();
                    JettraConfig.setProperty("server.port", val);
                } else if (arg.startsWith("--server.contextpath=")) {
                    JettraConfig.setProperty("server.contextpath", arg.substring("--server.contextpath=".length()).trim());
                }
            }
        }

        App app = new App();
        app.initUI();

        JettraServer server = new JettraServer();
        if (app.port != null && !app.port.isBlank()) {
            try {
                server.setPort(Integer.parseInt(app.port.trim()));
            } catch (Exception ignored) {}
        }
        if (app.contextpath != null && !app.contextpath.isBlank()) {
            JettraServer.setContextPath(app.contextpath.trim());
        }
        serverInstance = server;

        // Register Pages
        server.addHandler("/", LoginPage.class);
        server.addHandler("/login", LoginPage.class);
        server.addHandler("/dashboard", ClusterDashboardPage.class);
        server.addHandler("/police3d", Police3DPage.class);
        server.addHandler("/police", PoliceSentinelPage.class);
        server.addHandler("/connections", ConnectionsPage.class);
        server.addHandler("/databases", DatabasesPage.class);
        server.addHandler("/records", RecordsPage.class);
        server.addHandler("/engines", EnginesPage.class);
        server.addHandler("/indexes", IndexesPage.class);
        server.addHandler("/query", QueryConsolePage.class);
        server.addHandler("/users", UserManagerPage.class);
        server.addHandler("/backup", BackupRestorePage.class);

        System.out.println("JettraFlux Explorer desplegado en http://localhost:" + server.getPort() + JettraServer.getContextPath());
        server.start();
    }
}
