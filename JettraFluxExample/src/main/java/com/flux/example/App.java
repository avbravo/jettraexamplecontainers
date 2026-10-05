package com.flux.example;

import io.jettra.rest.server.JettraRestServer;
import io.jettra.server.JettraServer;
import io.jettra.server.config.ConfigInjector;
import io.jettra.server.config.JettraConfigProperty;
import io.jettra.server.discoverer.DiscoveredLoad;
import io.jettra.server.openapi.OpenApiHandler;
import io.jettra.server.openapi.SwaggerUIHandler;
import java.util.List;

/**
 * App!
 *
 */
/**
 * Hello world!
 *
 */
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
        IO.println("Iniciando aplicación Web: " + appTitle);
    }

    public static void main(String[] args) {
        if (args != null && args.length > 0 && args[0].equals("-console")) {
            io.jettra.server.autentification.SecurityCLI.main(args);
            return;
        }
        if (args != null && args.length > 0 && args[0].equals("-generate-flux-jettra-sh")) {
            io.jettra.server.JettraServer.generateMvnScripts();
            return;
        }

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--server.port=")) {
                    io.jettra.server.config.JettraConfig.setProperty("server.port", arg.substring("--server.port=".length()).trim());
                } else if (arg.startsWith("--port=") || arg.startsWith("-port=")) {
                    String val = arg.substring(arg.indexOf('=') + 1).trim();
                    io.jettra.server.config.JettraConfig.setProperty("server.port", val);
                } else if ((arg.equals("-p") || arg.equals("-port") || arg.equals("--port")) && i + 1 < args.length) {
                    io.jettra.server.config.JettraConfig.setProperty("server.port", args[++i].trim());
                } else if (arg.startsWith("--server.contextpath=")) {
                    io.jettra.server.config.JettraConfig.setProperty("server.contextpath", arg.substring("--server.contextpath=".length()).trim());
                } else if (arg.startsWith("--contextpath=") || arg.startsWith("-contextpath=")) {
                    String val = arg.substring(arg.indexOf('=') + 1).trim();
                    io.jettra.server.config.JettraConfig.setProperty("server.contextpath", val);
                } else if ((arg.equals("-c") || arg.equals("-contextpath") || arg.equals("--contextpath")) && i + 1 < args.length) {
                    io.jettra.server.config.JettraConfig.setProperty("server.contextpath", args[++i].trim());
                } else if (arg.startsWith("--") && arg.contains("=")) {
                    String[] parts = arg.substring(2).split("=", 2);
                    io.jettra.server.config.JettraConfig.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
        }

        App app = new App();
        app.initUI();

        IO.println("Levantando servidor de enrutamiento JettraServer empotrado...");
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

        // Configurar la ruta de redirección en ErrorPage, usando contextpath (y el puerto implícitamente por el host)
        io.jettra.flux.complex.ErrorPage.path = "http://localhost:" + server.getPort() + JettraServer.getContextPath();
        server.setErrorPage("/error");
        server.addHandler("/error", io.jettra.flux.complex.ErrorPage.class);
        server.addHandler("/swagger-ui", io.jettra.flux.complex.SwaggerUIPage.class);

        // Registro de Páginas JettraFlux
        server.addHandler("/", com.flux.example.pages.login.LoginPage.class);
        server.addHandler("/login", com.flux.example.pages.login.LoginPage.class);
        server.addHandler("/dashboard", com.flux.example.pages.dashboard.DashboardPage.class);
        server.addHandler("/forgot-password", com.flux.example.pages.login.ForgotPasswordPage.class);

        // Cargamos los controladores descubiertos automáticamente
        List<Class<?>> controllers = new java.util.ArrayList<>(io.jettra.server.discoverer.DiscoveredRegistry.getDiscoveredClasses(App.class));

        // Puedes agregar aquí manualmente las clases que tengan @Discovered(automatic=false)
        // o que no tengan la anotación
        // controllers.add(MiControladorManual.class);
//        controllers.add(com.flux.example.controller.ContenedorMaritimoController.class);
// Exponer el JSON de OpenAPI
        server.addHandler("/openapi.json", new OpenApiHandler(controllers));

        // Exponer la interfaz Swagger UI
        server.addHandler("/swagger-ui", new SwaggerUIHandler("/openapi.json"));

        // Registrar los controladores descubiertos en JettraRestServer
        JettraRestServer.registerDiscovered(server, App.class);

        // Registro manual para los que no se descubren automáticamente
//        JettraRestServer.register(server, AuthController.class);
        server.start();

    }

}
