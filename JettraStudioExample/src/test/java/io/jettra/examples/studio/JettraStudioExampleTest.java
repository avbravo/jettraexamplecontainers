package io.jettra.examples.studio;

import io.jettra.examples.studio.pages.CatalogPage;
import io.jettra.examples.studio.pages.ComponentsShowcasePage;
import io.jettra.examples.studio.pages.DashboardPage;
import io.jettra.examples.studio.pages.HomePage;
import io.jettra.examples.studio.pages.LoginPage;
import io.jettra.test.annotation.NotRequiresRunningServer;
import io.jettra.test.annotation.Test;

import java.io.IOException;

import static io.jettra.test.core.JettraAssert.*;

@NotRequiresRunningServer
public class JettraStudioExampleTest {

    @Test
    public void testJettraConfigLoading() {
        App app = new App();
        app.initUI();

        assertNotNull(app.getAppTitle());
        assertTrue(app.getAppTitle().contains("JettraStudio"));
        assertNotNull(app.getPort());
        assertEquals("8085", app.getPort().trim());
        assertEquals("es", app.getAppLanguage().trim());
        assertEquals("games", app.getAppTheme().trim());
    }

    @Test
    public void testLoginPageRendering() {
        LoginPage page = new LoginPage();
        String html = page.renderPage();

        assertNotNull(html);
        assertTrue(html.contains("JettraStudio") || html.contains("Acceso"));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
        assertTrue(html.contains("<form"));
        assertTrue(html.contains("Iniciar Sesión"));
    }

    @Test
    public void testDashboardPageWithTopLeftCenterFooter() {
        DashboardPage page = new DashboardPage();
        String html = page.renderPage();

        assertNotNull(html);

        // 1. TOP Component
        assertTrue(html.contains("dashboard-top"));
        assertTrue(html.contains("topTitle") || html.contains("Dashboard"));
        assertTrue(html.contains("change_lang=es"));
        assertTrue(html.contains("change_lang=en"));

        // 2. LEFT Component (Sidebar)
        assertTrue(html.contains("dashboard-left"));
        assertTrue(html.contains("sidebar-logo"));
        assertTrue(html.contains("/dashboard"));
        assertTrue(html.contains("/catalog"));
        assertTrue(html.contains("/components"));

        // 3. CENTER Component
        assertTrue(html.contains("dashboard-center"));
        assertTrue(html.contains("statConversionValue") || html.contains("0.80%"));
        assertTrue(html.contains("statAvgOrderValue") || html.contains("$306.20"));
        assertTrue(html.contains("statQuantityValue") || html.contains("1,620"));
        assertTrue(html.contains("250,000 req/s"));
        assertTrue(html.contains("$620,076.00"));
        assertTrue(html.contains("Pago #28492"));

        // 4. FOOTER Component
        assertTrue(html.contains("dashboard-footer"));
        assertTrue(html.contains("JettraStudio"));
    }

    @Test
    public void testHomePageRendering() {
        HomePage page = new HomePage();
        String html = page.renderPage();

        assertNotNull(html);
        assertTrue(html.contains("JettraStudio"));
        assertTrue(html.contains("Usuarios Activos"));
        assertTrue(html.contains("12,480"));
        assertTrue(html.contains("Virtual Threads"));
        assertTrue(html.contains("/components"));
        assertTrue(html.contains("/catalog"));
    }

    @Test
    public void testComponentsShowcasePageRendering() {
        ComponentsShowcasePage page = new ComponentsShowcasePage();
        String html = page.renderPage();

        assertNotNull(html);
        assertTrue(html.contains("Gold Button"));
        assertTrue(html.contains("Blue Button"));
        assertTrue(html.contains("Lime Button"));
        assertTrue(html.contains("Red Button"));
        assertTrue(html.contains("AlexDeveloper"));
        assertTrue(html.contains("alex@jettra.io"));
        assertTrue(html.contains("Arquitecto Cloud"));
        assertTrue(html.contains("Ventana Emergente - JettraStudio Modal"));
        assertTrue(html.contains("Tipado Fuerte"));
    }

    @Test
    public void testCatalogPageRendering() {
        CatalogPage page = new CatalogPage();
        String html = page.renderPage();

        assertNotNull(html);
        assertTrue(html.contains("PROD-101"));
        assertTrue(html.contains("Servidor Edge Jettra"));
        assertTrue(html.contains("PROD-102"));
        assertTrue(html.contains("Licencia JettraStore Enterprise"));
        assertTrue(html.contains("PROD-103"));
        assertTrue(html.contains("Gateway gRPC Loom"));
        assertTrue(html.contains("Adquirir"));
        assertTrue(html.contains("Detalles"));
    }

    @Test
    public void testAppServerStartupAndStop() throws IOException {
        JettraStudioExampleApp app = new JettraStudioExampleApp();
        app.start(18086);
        assertNotNull(app.getServer());
        app.stop();
    }
}
