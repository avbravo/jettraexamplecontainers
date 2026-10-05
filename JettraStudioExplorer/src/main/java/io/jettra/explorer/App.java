package io.jettra.explorer;

import com.sun.net.httpserver.HttpServer;
import io.jettra.explorer.pages.*;
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
 * Main Entry Point for JettraStudioExplorer & JettraStorePolice3D.
 * Unified Web Management, Telemetry and 3D Quantum Monitoring Suite for JettraStore.
 */
public class App {

    @JettraConfigProperty(name = "app.title")
    private String appTitle = "JettraStore Explorer & Police 3D";

    @JettraConfigProperty(name = "app.shorttitle")
    private String shortTitle = "JSEP3D";

    @JettraConfigProperty(name = "server.port")
    private String port = "8088";

    @JettraConfigProperty(name = "server.contextpath")
    private String contextpath = "";

    @JettraConfigProperty(name = "app.language")
    private String appLanguage = "es";

    @JettraConfigProperty(name = "app.theme")
    private String appTheme = "games";

    private HttpServer server;
    public static HttpServer serverInstance;

    public void initUI() {
        ConfigInjector.inject(this);
        System.out.println("🔧 [JettraConfig] Inicializando JettraStore Explorer & Police 3D:");
        System.out.println("   • Título:       " + appTitle);
        System.out.println("   • Puerto base:  " + port);
        System.out.println("   • Idioma base:  " + appLanguage);
        System.out.println("   • Tema base:    " + appTheme);

        if (appLanguage != null && !appLanguage.isBlank()) {
            LocalizationManager.getInstance().setCurrentLanguage(LanguageStudio.fromCode(appLanguage));
        }

        if (appTheme != null && !appTheme.isBlank()) {
            JettraTheme jt = JettraTheme.fromName(appTheme);
            if (jt != null) {
                StudioThemeManager.getInstance().setDefaultTheme(jt);
            }
        }
    }

    public void start(int listenPort) throws IOException {
        initUI();
        HttpServer s = HttpServer.create(new InetSocketAddress(listenPort), 0);
        // Java 25 Loom Virtual Threads Executor
        s.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        String prefix = (contextpath != null && !contextpath.isBlank())
            ? (contextpath.startsWith("/") ? contextpath : "/" + contextpath)
            : "";

        // Register handlers
        s.createContext(prefix + "/", StudioHandler.of(LoginPage.class));
        s.createContext(prefix + "/login", StudioHandler.of(LoginPage.class));
        s.createContext(prefix + "/police3d", StudioHandler.of(Police3DPage.class));
        s.createContext(prefix + "/police", StudioHandler.of(PoliceMonitorPage.class));
        s.createContext(prefix + "/dashboard", StudioHandler.of(ClusterDashboardPage.class));
        s.createContext(prefix + "/cluster", StudioHandler.of(ClusterDashboardPage.class));
        s.createContext(prefix + "/connections", StudioHandler.of(ConnectionsPage.class));
        s.createContext(prefix + "/query", StudioHandler.of(QueryConsolePage.class));
        s.createContext(prefix + "/databases", StudioHandler.of(DatabasesPage.class));
        s.createContext(prefix + "/engines", StudioHandler.of(EnginesPage.class));
        s.createContext(prefix + "/indexes", StudioHandler.of(IndexesPage.class));
        s.createContext(prefix + "/records", StudioHandler.of(RecordsPage.class));
        s.createContext(prefix + "/users", StudioHandler.of(UserManagerPage.class));
        s.createContext(prefix + "/backup", StudioHandler.of(BackupRestorePage.class));

        s.start();
        this.server = s;
        serverInstance = s;

        System.out.println("==================================================================");
        System.out.println("🛡️  " + appTitle + " iniciado exitosamente!");
        System.out.println("🌐 URL: http://localhost:" + listenPort + prefix + "/");
        System.out.println("------------------------------------------------------------------");
        System.out.println("   • Mundo 3D Cuántico:    http://localhost:" + listenPort + prefix + "/police3d");
        System.out.println("   • Centinelas Police:    http://localhost:" + listenPort + prefix + "/police");
        System.out.println("   • Conexiones Servidor:  http://localhost:" + listenPort + prefix + "/connections");
        System.out.println("   • Consola JettraSQL/QL: http://localhost:" + listenPort + prefix + "/query");
        System.out.println("   • Monitor Telemetría:   http://localhost:" + listenPort + prefix + "/dashboard");
        System.out.println("   • Bases de Datos CRUD:  http://localhost:" + listenPort + prefix + "/databases");
        System.out.println("   • Motores (Engines):    http://localhost:" + listenPort + prefix + "/engines");
        System.out.println("   • Gestión de Índices:   http://localhost:" + listenPort + prefix + "/indexes");
        System.out.println("   • Explorador Registros: http://localhost:" + listenPort + prefix + "/records");
        System.out.println("   • Usuarios & Permisos:  http://localhost:" + listenPort + prefix + "/users");
        System.out.println("   • Backup & Restore:     http://localhost:" + listenPort + prefix + "/backup");
        System.out.println("==================================================================");
        System.out.println("⚡ Hilos Virtuales Java 25 (Loom) activos y listos para peticiones.");
    }

    public void stop() {
        if (this.server != null) {
            this.server.stop(0);
            this.server = null;
        }
        if (serverInstance != null) {
            serverInstance.stop(0);
            serverInstance = null;
        }
    }

    public HttpServer getServer() {
        return this.server != null ? this.server : serverInstance;
    }

    public static void main(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--server.port=") || arg.startsWith("--port=")) {
                    JettraConfig.setProperty("server.port", arg.substring(arg.indexOf("=") + 1).trim());
                } else if ((arg.equals("-p") || arg.equals("--port")) && i + 1 < args.length) {
                    JettraConfig.setProperty("server.port", args[++i].trim());
                } else if (arg.startsWith("--server.contextpath=") || arg.startsWith("--contextpath=")) {
                    JettraConfig.setProperty("server.contextpath", arg.substring(arg.indexOf("=") + 1).trim());
                }
            }
        }

        App app = new App();
        int serverPort = 8088;
        try {
            if (app.port != null) serverPort = Integer.parseInt(app.port.trim());
        } catch (Exception ignored) {}

        try {
            app.start(serverPort);
        } catch (IOException e) {
            System.err.println("Error iniciando servidor JettraStore Explorer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getAppTitle() { return appTitle; }
    public String getPort() { return port; }
    public String getContextpath() { return contextpath; }
    public String getAppTheme() { return appTheme; }
    public String getAppLanguage() { return appLanguage; }
}
