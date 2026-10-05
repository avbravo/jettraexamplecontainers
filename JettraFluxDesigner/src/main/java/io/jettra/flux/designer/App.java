package io.jettra.flux.designer;

import io.jettra.flux.designer.pages.*;
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
        System.out.println("Iniciando JettraFlux Designer: " + appTitle);
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
        server.addHandler("/", VisualDesignerPage.class);
        server.addHandler("/designer", VisualDesignerPage.class);
        server.addHandler("/projects", ProjectManagerPage.class);
        server.addHandler("/codepreview", CodePreviewPage.class);
        server.addHandler("/metaverse", Police3DMetaversePage.class);
        server.addHandler("/login", LoginPage.class);

        System.out.println("JettraFlux Designer desplegado en http://localhost:" + server.getPort() + JettraServer.getContextPath());
        server.start();
    }
}
