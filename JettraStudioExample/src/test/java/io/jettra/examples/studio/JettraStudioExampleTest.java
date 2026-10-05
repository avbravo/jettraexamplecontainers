package io.jettra.examples.studio;

import io.jettra.examples.studio.pages.CatalogPage;
import io.jettra.examples.studio.pages.ComponentsShowcasePage;
import io.jettra.examples.studio.pages.HomePage;
import io.jettra.test.annotation.NotRequiresRunningServer;
import io.jettra.test.annotation.Test;

import java.io.IOException;

import static io.jettra.test.core.JettraAssert.*;

@NotRequiresRunningServer
public class JettraStudioExampleTest {

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
        // Verificación de variantes de botones
        assertTrue(html.contains("Gold Button"));
        assertTrue(html.contains("Blue Button"));
        assertTrue(html.contains("Lime Button"));
        assertTrue(html.contains("Red Button"));
        assertTrue(html.contains("Purple Button"));

        // Verificación de inputs de formulario
        assertTrue(html.contains("AlexDeveloper"));
        assertTrue(html.contains("alex@jettra.io"));
        assertTrue(html.contains("Arquitecto Cloud"));

        // Verificación de diálogo modal y cards
        assertTrue(html.contains("Ventana Emergente - JettraStudio Modal"));
        assertTrue(html.contains("Tipado Fuerte"));
    }

    @Test
    public void testCatalogPageRendering() {
        CatalogPage page = new CatalogPage();
        String html = page.renderPage();

        assertNotNull(html);
        // Verificación de ListView con productos
        assertTrue(html.contains("PROD-101"));
        assertTrue(html.contains("Servidor Edge Jettra"));
        assertTrue(html.contains("PROD-102"));
        assertTrue(html.contains("Licencia JettraStore Enterprise"));
        assertTrue(html.contains("PROD-103"));
        assertTrue(html.contains("Gateway gRPC Loom"));

        // Botones de acción dentro de filas
        assertTrue(html.contains("Adquirir"));
        assertTrue(html.contains("Detalles"));
    }

    @Test
    public void testAppServerStartupAndStop() throws IOException {
        JettraStudioExampleApp app = new JettraStudioExampleApp();
        // Iniciar en puerto dinámico 18085
        app.start(18085);
        assertNotNull(app.getServer());
        app.stop();
    }
}
