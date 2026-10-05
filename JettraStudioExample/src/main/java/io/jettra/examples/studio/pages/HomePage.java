package io.jettra.examples.studio.pages;

import io.jettra.examples.studio.components.MetricCardPanel;
import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.Card;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.MultiLineLabel;
import io.jettra.studio.core.BasePage;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.LambdaModel;
import io.jettra.studio.model.Model;
import io.jettra.studio.model.ResourceModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main Dashboard page for JettraStudioExample.
 * Inherits the master layout and theme support from BasePage.
 */
import io.jettra.studio.security.NoLoginRequired;

@NoLoginRequired
public class HomePage extends BasePage {

    public HomePage() {
        this(new PageParameters());
    }

    public HomePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("JettraStudio - Suite de Demostración Completa");

        // Internacionalización con ResourceModel
        add(new Label("welcomeTitle", ResourceModel.of("app.welcome")));
        add(new Label("welcomeSubtitle", ResourceModel.of("app.subtitle")));

        // Alerta de estado del sistema
        add(Alert.success("systemAlert", "✨ JettraStudio v1.0.0 cargado con éxito. Hilos virtuales de Java 25 Loom activos."));

        // Paneles modulares de métricas
        add(new MetricCardPanel("metricUsers", "Usuarios Activos", "12,480", "+14.8%", "👥"));
        add(new MetricCardPanel("metricOrders", "Órdenes Hoy", "1,894", "+8.2%", "📦"));
        add(new MetricCardPanel("metricRevenue", "Ingresos", "$48,920.00", "+21.5%", "💰"));
        add(new MetricCardPanel("metricLoom", "Virtual Threads", "150,000 req/s", "+99.9%", "⚡"));

        // LambdaModel dinámico con fecha y hora del servidor
        add(new Label("serverTime", LambdaModel.of(
            () -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            null
        )));

        // Tarjeta informativa con Card component
        Card techCard = Card.of("techCard", "Especificaciones del Stack")
            .subtitle("Java 25 + JettraStudio + JettraFlux");
        add(techCard);

        add(new MultiLineLabel("stackDescription",
            "JettraStudio proporciona renderizado de componentes desacoplado.\n"
            + "Las plantillas HTML5 son nativas, sin expresiones crípticas.\n"
            + "14 temas de diseño de JettraFlux integrados dinámicamente."));

        // Enlaces de navegación rápida
        add(Link.of("linkShowcase", "/components"));
        add(Link.of("linkCatalog", "/catalog"));

        // Botones de acción con variantes de tema
        add(Button.of("btnExplorar", "Ver Catálogo de Componentes", () -> {})
            .variant(Button.Variant.GOLD));
        add(Button.of("btnInventario", "Gestionar Inventario", () -> {})
            .variant(Button.Variant.BLUE));
    }
}
