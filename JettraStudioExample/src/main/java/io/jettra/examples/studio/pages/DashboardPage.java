package io.jettra.examples.studio.pages;

import io.jettra.flux.widgets.StatCard;
import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.Card;
import io.jettra.studio.components.FluxWidget;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.ResourceModel;
import io.jettra.studio.security.Secured;

/**
 * Operational Dashboard Page rendering the CENTER content within the 4-quadrant layout:
 * - Page-level security: @Secured
 * - StatCards (Conversion Rate, Avg Order Value, Order Quantity, Loom Threads).
 * - Direct integration of JettraFlux Widgets via FluxWidget.
 * - Visitor Growth Chart card.
 * - Transaction History Card.
 */
@Secured(roles = {"ADMIN", "MANAGER", "USER", "DEMO"}, loginUrl = "/login")
public class DashboardPage extends TemplatePage {

    public DashboardPage() {
        this(new PageParameters());
    }

    public DashboardPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Dashboard Operativo - JettraStudio Pro");
        super.onInitialize();

        // Alerta de bienvenida y estado
        add(Alert.success("dashboardAlert", "¡Conexión segura establecida! Servidor Loom procesando en Virtual Threads."));

        // Métricas / StatCards estándar
        add(new Label("statConversionTitle", ResourceModel.of("dashboard.stats.conversion")));
        add(new Label("statConversionValue", "0.80%"));
        add(new Label("statConversionChange", "+0.81%"));

        add(new Label("statAvgOrderTitle", ResourceModel.of("dashboard.stats.avgorder")));
        add(new Label("statAvgOrderValue", "$306.20"));
        add(new Label("statAvgOrderChange", "+4.20%"));

        add(new Label("statQuantityTitle", ResourceModel.of("dashboard.stats.quantity")));
        add(new Label("statQuantityValue", "1,620"));
        add(new Label("statQuantityChange", "-2.10%"));

        add(new Label("statThreadsTitle", ResourceModel.of("dashboard.stats.threads")));
        add(new Label("statThreadsValue", "250,000 req/s"));
        add(new Label("statThreadsChange", "99.98%"));

        // Integración de Widgets estilo JettraFlux dentro de JettraStudio
        add(FluxWidget.of("fluxRevenueCard", StatCard.of("MRR Total (Flux)", "+12.5%", "$620,076.00", true)));
        add(FluxWidget.of("fluxOrderCard", StatCard.of("Ticket Promedio (Flux)", "+4.2%", "$306.20", true)));

        // Visitor Growth Card
        Card visitorCard = Card.of("visitorCard", "Gráfico de Crecimiento & MRR")
            .subtitle("Proyección anual de ingresos y visitantes recurrentes");
        add(visitorCard);

        add(new Label("mrrTotal", "$620,076.00"));
        add(new Label("mrrAverage", "$1,120.00"));

        // Transaction History Card
        Card transactionCard = Card.of("transactionCard", "Historial de Transacciones")
            .subtitle("Últimas órdenes procesadas en tiempo real");
        add(transactionCard);

        // Botones de acción rápida
        add(Button.of("btnQuickNewOrder", "+ Nueva Orden", () -> {}).variant(Button.Variant.GOLD));
        add(Button.of("btnQuickCatalog", "Ir al Inventario", () -> {}).variant(Button.Variant.BLUE));
    }
}
