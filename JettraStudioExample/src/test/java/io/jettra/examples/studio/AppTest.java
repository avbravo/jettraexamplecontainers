package io.jettra.examples.studio;

import io.jettra.examples.studio.model.Product;
import io.jettra.examples.studio.model.ProductRepository;
import io.jettra.examples.studio.pages.CatalogPage;
import io.jettra.examples.studio.pages.ComponentsShowcasePage;
import io.jettra.examples.studio.pages.DashboardPage;
import io.jettra.examples.studio.pages.HomePage;
import io.jettra.examples.studio.pages.LoginPage;
import io.jettra.examples.studio.pages.ProductCrudPage;
import io.jettra.server.discoverer.DiscoveredLoad;
import io.jettra.studio.security.NoLoginRequired;
import io.jettra.studio.security.Secured;
import io.jettra.test.annotation.JettraTest;
import io.jettra.test.annotation.NotRequiresRunningServer;
import io.jettra.test.core.JettraAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static io.jettra.test.core.JettraAssert.*;

/**
 * Modern Test Suite for JettraStudioExample, following the JettraFlux architecture:
 * - Uses native JettraTest annotations (@DiscoveredLoad, @JettraTest, @NotRequiresRunningServer).
 * - Tests JettraConfig property injection.
 * - Tests Page-level Security (@Secured, @NoLoginRequired, and HTTP 302/403 validation).
 * - Tests Complete CRUD lifecycle (Create, Read, Update, Delete, Search) and ProductCrudPage rendering.
 * - Tests JettraFlux Widget integration inside JettraStudio (FluxWidget).
 * - Tests Server lifecycle with Virtual Threads (Loom).
 */
@DiscoveredLoad
@NotRequiresRunningServer
public class AppTest {

    @JettraTest
    public void testJettraConfigLoading() {
        App app = new App();
        app.initUI();

        assertNotNull(app.getAppTitle(), "El título de la aplicación no debe ser nulo");
        assertTrue(app.getAppTitle().contains("JettraStudio"), "El título debe contener JettraStudio");
        assertNotNull(app.getPort(), "El puerto no debe ser nulo");
        assertEquals("8085", app.getPort().trim(), "El puerto configurado debe ser 8085");
        assertEquals("es", app.getAppLanguage().trim(), "El idioma base debe ser es");
        assertEquals("games", app.getAppTheme().trim(), "El tema base debe ser games");
    }

    @JettraTest
    public void testPageSecurityAnnotations() {
        // Public pages
        assertTrue(LoginPage.class.isAnnotationPresent(NoLoginRequired.class), 
            "LoginPage debe estar anotada con @NoLoginRequired");
        assertTrue(HomePage.class.isAnnotationPresent(NoLoginRequired.class), 
            "HomePage debe estar anotada con @NoLoginRequired");

        // Secured pages
        assertTrue(DashboardPage.class.isAnnotationPresent(Secured.class), 
            "DashboardPage debe estar protegida con @Secured");
        assertTrue(CatalogPage.class.isAnnotationPresent(Secured.class), 
            "CatalogPage debe estar protegida con @Secured");
        assertTrue(ProductCrudPage.class.isAnnotationPresent(Secured.class), 
            "ProductCrudPage debe estar protegida con @Secured");

        Secured crudSecured = ProductCrudPage.class.getAnnotation(Secured.class);
        assertEquals("/login", crudSecured.loginUrl(), "La redirección de login debe apuntar a /login");
        assertTrue(List.of(crudSecured.roles()).contains("ADMIN"), "El rol ADMIN debe tener acceso a ProductCrudPage");
    }

    @JettraTest
    public void testLoginPageRendering() {
        LoginPage page = new LoginPage();
        String html = page.renderPage();

        assertNotNull(html, "El HTML de LoginPage no debe ser nulo");
        assertTrue(html.contains("JettraStudio") || html.contains("Acceso"), "Debe mostrar el título o acceso");
        assertTrue(html.contains("username"), "Debe contener el campo username");
        assertTrue(html.contains("password"), "Debe contener el campo password");
        assertTrue(html.contains("<form"), "Debe contener la etiqueta form");
        assertTrue(html.contains("Iniciar Sesión"), "Debe contener el botón Iniciar Sesión");
    }

    @JettraTest
    public void testDashboardPageWithFluxWidgets() {
        DashboardPage page = new DashboardPage();
        String html = page.renderPage();

        assertNotNull(html, "El HTML de DashboardPage no debe ser nulo");

        // 1. TOP Component
        assertTrue(html.contains("dashboard-top"), "Debe incluir el contenedor TOP");
        assertTrue(html.contains("topTitle") || html.contains("Dashboard"), "Debe incluir el título del Dashboard");

        // 2. LEFT Component (Sidebar)
        assertTrue(html.contains("dashboard-left"), "Debe incluir el contenedor LEFT");
        assertTrue(html.contains("/dashboard"), "Debe enlazar a /dashboard");
        assertTrue(html.contains("/crud"), "Debe enlazar al CRUD /crud");
        assertTrue(html.contains("/catalog"), "Debe enlazar al catálogo /catalog");

        // 3. JettraFlux Widget integration
        assertTrue(html.contains("MRR Total (Flux)"), "Debe renderizar el StatCard nativo de JettraFlux");
        assertTrue(html.contains("Ticket Promedio (Flux)"), "Debe renderizar el StatCard de Ticket Promedio");

        // 4. CENTER Component
        assertTrue(html.contains("dashboard-center"), "Debe incluir el contenedor CENTER");
        assertTrue(html.contains("250,000 req/s"), "Debe reflejar métricas de Virtual Threads Loom");

        // 5. FOOTER Component
        assertTrue(html.contains("dashboard-footer"), "Debe incluir el FOOTER");
    }

    @JettraTest
    public void testProductCrudPageRenderingAndFluxWidget() {
        ProductCrudPage page = new ProductCrudPage();
        String html = page.renderPage();

        assertNotNull(html, "El HTML de ProductCrudPage no debe ser nulo");
        assertTrue(html.contains("Administración Integral de Productos"), "Debe mostrar el encabezado principal del CRUD");
        assertTrue(html.contains("Catálogo Loom CRUD"), "Debe renderizar el StatCard de JettraFlux en el CRUD");
        assertTrue(html.contains("searchForm"), "Debe contener el formulario de búsqueda/filtrado");
        assertTrue(html.contains("crudForm"), "Debe contener el formulario de alta/edición");
        assertTrue(html.contains("detailsModal"), "Debe contener el modal para detalles");
        assertTrue(html.contains("PROD-101"), "Debe listar el producto inicial PROD-101");
    }

    @JettraTest
    public void testCompleteCrudOperations() {
        ProductRepository repo = ProductRepository.getInstance();
        repo.resetToDefaults();

        int initialCount = repo.count();
        assertEquals(5, initialCount, "Debe inicializarse con 5 productos por defecto");

        // 1. CREATE
        Product newProd = new Product("PROD-999", "Base de Datos JettraStore Cluster", "Database", 50, 1999.00, true);
        repo.save(newProd);
        assertEquals(initialCount + 1, repo.count(), "La cantidad debe incrementarse tras crear un producto");

        // 2. READ
        Optional<Product> found = repo.findById("PROD-999");
        assertTrue(found.isPresent(), "El producto creado debe ser encontrado por ID");
        assertEquals("Base de Datos JettraStore Cluster", found.get().name());
        assertEquals("$1999.00", found.get().getFormattedPrice());
        assertEquals("ACTIVO", found.get().getStatusBadge());

        // SEARCH
        List<Product> searchResults = repo.search("JettraStore");
        assertTrue(searchResults.size() >= 2, "La búsqueda debe retornar coincidencias parciales");

        // 3. UPDATE
        Product updated = new Product("PROD-999", "Base de Datos JettraStore Cluster v2", "Database", 75, 2499.00, true);
        repo.update(updated);
        Optional<Product> checkUpdated = repo.findById("PROD-999");
        assertTrue(checkUpdated.isPresent());
        assertEquals("Base de Datos JettraStore Cluster v2", checkUpdated.get().name());
        assertEquals(75, checkUpdated.get().stock());

        // 4. DELETE
        boolean deleted = repo.delete("PROD-999");
        assertTrue(deleted, "El producto debe eliminarse exitosamente");
        assertFalse(repo.findById("PROD-999").isPresent(), "El producto eliminado no debe existir más");
        assertEquals(initialCount, repo.count(), "La cantidad debe retornar al tamaño original");
    }

    @JettraTest
    public void testComponentsShowcasePageRendering() {
        ComponentsShowcasePage page = new ComponentsShowcasePage();
        String html = page.renderPage();

        assertNotNull(html, "El HTML no debe ser nulo");
        assertTrue(html.contains("Gold Button"), "Debe contener Gold Button");
        assertTrue(html.contains("Blue Button"), "Debe contener Blue Button");
        assertTrue(html.contains("Ventana Emergente - JettraStudio Modal"), "Debe contener el modal");
    }

    @JettraTest
    public void testCatalogPageRendering() {
        CatalogPage page = new CatalogPage();
        String html = page.renderPage();

        assertNotNull(html, "El HTML de CatalogPage no debe ser nulo");
        assertTrue(html.contains("PROD-101"), "Debe contener PROD-101");
        assertTrue(html.contains("Servidor Edge Jettra"), "Debe contener el nombre del producto");
        assertTrue(html.contains("Adquirir"), "Debe contener el botón Adquirir");
    }

    @JettraTest
    public void testLoginButtonSubmitEventAndCookies() throws IOException {
        int testPort = 18092;
        App app = new App();
        app.start(testPort);

        try {
            // 1. Submit POST with valid credentials
            URI loginUri = URI.create("http://localhost:" + testPort + "/login");
            HttpURLConnection conn = (HttpURLConnection) loginUri.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String body = "username=admin&password=admin123&btnLogin=Iniciar+Sesión";
            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            assertEquals(302, code, "El botón Iniciar Sesión debe procesar el evento y redirigir con 302");
            String location = conn.getHeaderField("Location");
            assertNotNull(location);
            assertTrue(location.contains("/dashboard"), "Debe redirigir al /dashboard tras iniciar sesión exitosamente");

            // Verify cookies (case-insensitive header search)
            List<String> cookies = null;
            for (var entry : conn.getHeaderFields().entrySet()) {
                if ("Set-Cookie".equalsIgnoreCase(entry.getKey())) {
                    cookies = entry.getValue();
                    break;
                }
            }
            assertNotNull(cookies, "Debe emitir cookies de autenticación");
            boolean userCookieFound = false;
            for (String c : cookies) {
                if (c.contains("jettra_user=admin")) userCookieFound = true;
            }
            assertTrue(userCookieFound, "La cookie jettra_user=admin debe estar presente");
            conn.disconnect();

            // 2. Submit POST with invalid credentials
            HttpURLConnection failConn = (HttpURLConnection) loginUri.toURL().openConnection();
            failConn.setRequestMethod("POST");
            failConn.setDoOutput(true);
            failConn.setInstanceFollowRedirects(false);
            failConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String failBody = "username=admin&password=wrongpassword&btnLogin=Iniciar+Sesión";
            try (java.io.OutputStream os = failConn.getOutputStream()) {
                os.write(failBody.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }

            assertEquals(302, failConn.getResponseCode());
            String failLocation = failConn.getHeaderField("Location");
            assertTrue(failLocation.contains("error=invalid_credentials"), "Credenciales inválidas deben redirigir con parámetro error");
            failConn.disconnect();

        } finally {
            app.stop();
        }
    }

    @JettraTest
    public void testHttpServerLifecycleAndSecurityEnforcement() throws IOException {
        int testPort = 18090;
        App app = new App();
        app.start(testPort);
        assertNotNull(app.getServer(), "La instancia de HttpServer debe estar iniciada");

        try {
            // 1. Unauthenticated request to /crud -> must redirect 302 to /login
            URI unauthUri = URI.create("http://localhost:" + testPort + "/crud");
            HttpURLConnection unauthConn = (HttpURLConnection) unauthUri.toURL().openConnection();
            unauthConn.setInstanceFollowRedirects(false);
            unauthConn.connect();
            int unauthCode = unauthConn.getResponseCode();
            assertEquals(302, unauthCode, "Ruta protegida sin autenticación debe responder 302 Redirect");
            String location = unauthConn.getHeaderField("Location");
            assertNotNull(location);
            assertTrue(location.contains("/login"), "La redirección debe apuntar al login");
            unauthConn.disconnect();

            // 2. Request to public /login -> must return 200 OK
            URI loginUri = URI.create("http://localhost:" + testPort + "/login");
            HttpURLConnection loginConn = (HttpURLConnection) loginUri.toURL().openConnection();
            loginConn.connect();
            assertEquals(200, loginConn.getResponseCode(), "La página de login debe ser accesible públicamente (200)");
            loginConn.disconnect();

            // 3. Authenticated request with ADMIN role -> must return 200 OK
            URI authUri = URI.create("http://localhost:" + testPort + "/crud");
            HttpURLConnection authConn = (HttpURLConnection) authUri.toURL().openConnection();
            authConn.setRequestProperty("Cookie", "jettra_user=admin; jettra_role=ADMIN");
            authConn.connect();
            assertEquals(200, authConn.getResponseCode(), "Usuario con rol ADMIN debe tener acceso (200 OK)");
            authConn.disconnect();

            // 4. Authenticated request with UNAUTHORIZED role (GUEST) -> must return 403 Forbidden
            URI forbiddenUri = URI.create("http://localhost:" + testPort + "/crud");
            HttpURLConnection forbiddenConn = (HttpURLConnection) forbiddenUri.toURL().openConnection();
            forbiddenConn.setRequestProperty("Cookie", "jettra_user=guest; jettra_role=GUEST");
            forbiddenConn.connect();
            assertEquals(403, forbiddenConn.getResponseCode(), "Usuario con rol no autorizado debe recibir 403 Forbidden");
            forbiddenConn.disconnect();

        } finally {
            app.stop();
        }
    }
}
