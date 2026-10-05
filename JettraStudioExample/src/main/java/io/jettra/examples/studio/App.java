package io.jettra.examples.studio;

import com.sun.net.httpserver.HttpServer;
import io.jettra.examples.studio.pages.CatalogPage;
import io.jettra.examples.studio.pages.ComponentsShowcasePage;
import io.jettra.examples.studio.pages.DashboardPage;
import io.jettra.examples.studio.pages.LoginPage;
import io.jettra.flux.theme.JettraTheme;
import io.jettra.server.config.ConfigInjector;
import io.jettra.server.config.JettraConfig;
import io.jettra.server.config.JettraConfigProperty;
import io.jettra.studio.i18n.LanguageStudio;
import io.jettra.studio.i18n.LocalizationManager;
import io.jettra.studio.server.StudioHandler;
import io.jettra.studio.theme.StudioThemeManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Main Application class for JettraStudioExample, replicating the configuration injection
 * and CLI argument handling from JettraFlux App.java.
 */
public class App {

    @JettraConfigProperty(name = "app.title")
    private String appTitle = "JettraStudio Pro Showcase";

    @JettraConfigProperty(name = "app.shorttitle")
    private String shortTitle = "JS";

    @JettraConfigProperty(name = "server.port")
    private String port = "8085";

    @JettraConfigProperty(name = "server.contextpath")
    private String contextpath = "";

    @JettraConfigProperty(name = "app.language")
    private String appLanguage = "es";

    @JettraConfigProperty(name = "app.theme")
    private String appTheme = "games";

    public static HttpServer serverInstance;

    public void initUI() {
        ConfigInjector.inject(this);
        System.out.println("🔧 [JettraConfig] Configuración cargada desde jettra-config.properties:");
        System.out.println("   • Título:       " + appTitle + " (" + shortTitle + ")");
        System.out.println("   • Puerto base:  " + port);
        System.out.println("   • Context Path: " + (contextpath != null && !contextpath.isBlank() ? contextpath : "/"));
        System.out.println("   • Idioma base:  " + appLanguage);
        System.out.println("   • Tema base:    " + appTheme);

        // Configurar idioma por defecto
        if (appLanguage != null && !appLanguage.isBlank()) {
            LocalizationManager.getInstance().setCurrentLanguage(LanguageStudio.fromCode(appLanguage));
        }

        // Configurar tema por defecto
        if (appTheme != null && !appTheme.isBlank()) {
            JettraTheme jt = JettraTheme.fromName(appTheme);
            if (jt != null) {
                StudioThemeManager.getInstance().setDefaultTheme(jt);
            }
        }
    }

    public static void main(String[] args) {
        // Parse CLI arguments (same logic as JettraFlux App.java)
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--server.port=")) {
                    JettraConfig.setProperty("server.port", arg.substring("--server.port=".length()).trim());
                } else if (arg.startsWith("--port=") || arg.startsWith("-port=")) {
                    String val = arg.substring(arg.indexOf("=") + 1).trim();
                    JettraConfig.setProperty("server.port", val);
                } else if ((arg.equals("-p") || arg.equals("-port") || arg.equals("--port")) && i + 1 < args.length) {
                    JettraConfig.setProperty("server.port", args[++i].trim());
                } else if (arg.startsWith("--server.contextpath=")) {
                    JettraConfig.setProperty("server.contextpath", arg.substring("--server.contextpath=".length()).trim());
                } else if (arg.startsWith("--contextpath=") || arg.startsWith("-contextpath=")) {
                    String val = arg.substring(arg.indexOf("=") + 1).trim();
                    JettraConfig.setProperty("server.contextpath", val);
                } else if ((arg.equals("-c") || arg.equals("-contextpath") || arg.equals("--contextpath")) && i + 1 < args.length) {
                    JettraConfig.setProperty("server.contextpath", args[++i].trim());
                } else if (arg.startsWith("--") && arg.contains("=")) {
                    String[] parts = arg.substring(2).split("=", 2);
                    JettraConfig.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
        }

        App app = new App();
        app.initUI();

        int serverPort = 8085;
        try {
            serverPort = Integer.parseInt(app.port.trim());
        } catch (Exception ignored) {}

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            // Java 25 Loom Virtual Threads Executor
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

            String prefix = (app.contextpath != null && !app.contextpath.isBlank())
                ? (app.contextpath.startsWith("/") ? app.contextpath : "/" + app.contextpath)
                : "";

            // Register handlers
            server.createContext(prefix + "/", StudioHandler.of(LoginPage.class));
            server.createContext(prefix + "/login", StudioHandler.of(LoginPage.class));
            server.createContext(prefix + "/dashboard", StudioHandler.of(DashboardPage.class));
            server.createContext(prefix + "/components", StudioHandler.of(ComponentsShowcasePage.class));
            server.createContext(prefix + "/catalog", StudioHandler.of(CatalogPage.class));

            server.start();
            serverInstance = server;

            System.out.println("==================================================================");
            System.out.println("🚀 " + app.appTitle + " iniciado exitosamente!");
            System.out.println("🌐 URL: http://localhost:" + serverPort + prefix + "/");
            System.out.println("------------------------------------------------------------------");
            System.out.println("   • Formulario de Login:     http://localhost:" + serverPort + prefix + "/login");
            System.out.println("   • Dashboard (Top,Left...): http://localhost:" + serverPort + prefix + "/dashboard");
            System.out.println("   • Catálogo e Inventario:   http://localhost:" + serverPort + prefix + "/catalog");
            System.out.println("   • Vitrina de Componentes:  http://localhost:" + serverPort + prefix + "/components");
            System.out.println("==================================================================");
            System.out.println("⚡ Hilos Virtuales Java 25 (Loom) activos y listos para peticiones.");

        } catch (IOException e) {
            System.err.println("Error iniciando el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getAppTitle() { return appTitle; }
    public String getPort() { return port; }
    public String getContextpath() { return contextpath; }
    public String getAppTheme() { return appTheme; }
    public String getAppLanguage() { return appLanguage; }
}
