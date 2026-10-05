package io.jettra.examples.studio;

import com.sun.net.httpserver.HttpServer;
import io.jettra.examples.studio.pages.CatalogPage;
import io.jettra.examples.studio.pages.ComponentsShowcasePage;
import io.jettra.examples.studio.pages.DashboardPage;
import io.jettra.examples.studio.pages.HomePage;
import io.jettra.examples.studio.pages.LoginPage;
import io.jettra.studio.server.StudioHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Main Application Runner for JettraStudioExample.
 * Starts an embedded HTTP server powered by Java 25 Virtual Threads (Project Loom),
 * exposing all pages and components of JettraStudio.
 */
public class JettraStudioExampleApp {

    private static final int DEFAULT_PORT = 8085;
    private HttpServer server;

    public static void main(String[] args) throws IOException {
        App.main(args);
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        // Java 25 Loom Virtual Threads Executor
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        // Register JettraStudio Handlers
        server.createContext("/", StudioHandler.of(LoginPage.class));
        server.createContext("/login", StudioHandler.of(LoginPage.class));
        server.createContext("/dashboard", StudioHandler.of(DashboardPage.class));
        server.createContext("/home", StudioHandler.of(HomePage.class));
        server.createContext("/components", StudioHandler.of(ComponentsShowcasePage.class));
        server.createContext("/catalog", StudioHandler.of(CatalogPage.class));

        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    public HttpServer getServer() {
        return server;
    }
}
