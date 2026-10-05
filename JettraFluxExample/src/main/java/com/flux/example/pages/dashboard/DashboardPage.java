package com.flux.example.pages.dashboard;

import io.jettra.flux.widgets.Column;
import io.jettra.flux.widgets.Row;
import io.jettra.flux.widgets.Card;
import io.jettra.flux.widgets.Header;
import io.jettra.flux.widgets.Paragraph;
import io.jettra.flux.widgets.Grid;
import io.jettra.flux.widgets.StatCard;
import io.jettra.flux.widgets.VisitorGraphCard;
import io.jettra.flux.widgets.TransactionHistoryCard;
import io.jettra.flux.widgets.Icon;
import com.flux.example.pages.template.TemplatePage;
import com.sun.net.httpserver.HttpExchange;
import io.jettra.flux.core.Widget;
import io.jettra.core.security.widget.PageWidgetAllow;
import io.jettra.server.JettraServer;

import java.util.Map;
import static io.jettra.flux.theme.OceanTheme.DashboardPage.CustomCSS;

@PageWidgetAllow(role = {jcf.AppRole.ADMIN, jcf.AppRole.MANAGER})
@io.jettra.core.server.Page(path = "/dashboard")
public class DashboardPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "Dashboard Analítico • JettraFlux Pro";
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        
        String modernCss = 
            "<style>" +
            "  .pro-dash-kpi { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; margin-bottom: 24px; }" +
            "  .pro-kpi-card { background: rgba(15, 23, 42, 0.7); backdrop-filter: blur(8px); border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 12px; padding: 18px; transition: transform 0.2s, border-color 0.2s; }" +
            "  .pro-kpi-card:hover { transform: translateY(-2px); border-color: rgba(56, 189, 248, 0.4); }" +
            "  .pro-action-bar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }" +
            "  .pro-quick-btn { padding: 8px 16px; border-radius: 8px; font-size: 0.85rem; font-weight: 700; text-decoration: none; display: inline-flex; align-items: center; gap: 6px; transition: all 0.15s; }" +
            "</style>";

        // Top Action & Welcome Bar
        String actionBarHtml = 
            "<div class='pro-action-bar'>" +
            "  <div>" +
            "    <h2 style='margin:0; font-size:1.6rem; font-weight:800; color:#fff; letter-spacing:-0.02em;'>Resumen Ejecutivo &amp; Métricas</h2>" +
            "    <p style='margin:4px 0 0 0; color:#94a3b8; font-size:0.9rem;'>Supervisión reactiva con Loom Virtual Threads y JettraFlux Components</p>" +
            "  </div>" +
            "  <div style='display:flex; gap:10px;'>" +
            "    <a href='" + JettraServer.resolvePath("/new-product") + "' class='pro-quick-btn' style='background:#0284c7; color:#fff;'>+ Nuevo Producto</a>" +
            "    <a href='" + JettraServer.resolvePath("/product-list") + "' class='pro-quick-btn' style='background:rgba(255,255,255,0.08); color:#cbd5e1; border:1px solid rgba(255,255,255,0.1);'>📦 Ver Catálogo</a>" +
            "    <a href='" + JettraServer.resolvePath("/order-history") + "' class='pro-quick-btn' style='background:rgba(255,255,255,0.08); color:#cbd5e1; border:1px solid rgba(255,255,255,0.1);'>📜 Pedidos</a>" +
            "  </div>" +
            "</div>";

        // Professional 4-KPI Row
        String kpiCardsHtml = 
            "<div class='pro-dash-kpi'>" +
            "  <div class='pro-kpi-card'>" +
            "    <span style='font-size:0.75rem; text-transform:uppercase; font-weight:700; color:#64748b; display:block;'>Facturación Total</span>" +
            "    <div style='font-size:1.45rem; font-weight:800; color:#fff; margin:4px 0;'>$148,250.00</div>" +
            "    <span style='font-size:0.75rem; font-weight:700; color:#22c55e;'>↑ +14.2% este mes</span>" +
            "  </div>" +
            "  <div class='pro-kpi-card'>" +
            "    <span style='font-size:0.75rem; text-transform:uppercase; font-weight:700; color:#64748b; display:block;'>Pedidos Activos</span>" +
            "    <div style='font-size:1.45rem; font-weight:800; color:#38bdf8; margin:4px 0;'>3,420</div>" +
            "    <span style='font-size:0.75rem; font-weight:700; color:#38bdf8;'>● 99.4% completados</span>" +
            "  </div>" +
            "  <div class='pro-kpi-card'>" +
            "    <span style='font-size:0.75rem; text-transform:uppercase; font-weight:700; color:#64748b; display:block;'>Clientes Recurrentes</span>" +
            "    <div style='font-size:1.45rem; font-weight:800; color:#eab308; margin:4px 0;'>1,280 VIP</div>" +
            "    <span style='font-size:0.75rem; font-weight:700; color:#eab308;'>★ 94% satisfacción</span>" +
            "  </div>" +
            "  <div class='pro-kpi-card'>" +
            "    <span style='font-size:0.75rem; text-transform:uppercase; font-weight:700; color:#64748b; display:block;'>Salud de Ejecución Loom</span>" +
            "    <div style='font-size:1.45rem; font-weight:800; color:#22c55e; margin:4px 0;'>0.85 ms</div>" +
            "    <span style='font-size:0.75rem; font-weight:700; color:#22c55e;'>⚡ 100% HEALTHY (Java 25)</span>" +
            "  </div>" +
            "</div>";

        // Main Visual Cards
        Widget mainChart = VisitorGraphCard.of(
            "Crecimiento MRR & Visitas Únicas", "2026",
            "$784,200", "MRR GROWTH (+24%)",
            "$1,450", "AVG. TICKET CLIENTE",
            45, 65, 55, 88, 40, 82, 70, 98, 62
        );

        Widget transactions = TransactionHistoryCard.of(
            "Transacciones Recientes",
            new TransactionHistoryCard.TransactionItem(Icon.CHECK, "#3b82f6", "Cobro aprobado #TR-28492", "Hoy 11:09 AM", "+$1,250.00", true),
            new TransactionHistoryCard.TransactionItem(Icon.REDO, "#ef4444", "Devolución autorizada #TR-94830", "Hoy 08:22 AM", "-$170.00", false),
            new TransactionHistoryCard.TransactionItem(Icon.PLUS, "#22c55e", "Nuevo cliente corporativo #TR-5849", "Ayer 02:56 PM", "+$3,450.00", true),
            new TransactionHistoryCard.TransactionItem(Icon.CHECK, "#3b82f6", "Pago suscripción Enterprise #TR-3382", "Ayer 06:11 AM", "+$5,800.00", true)
        );

        Widget layout = Column.of(
            Paragraph.of(modernCss),
            Paragraph.of(actionBarHtml),
            Paragraph.of(kpiCardsHtml),
            Grid.of(mainChart, transactions).modifier(new io.jettra.flux.core.Modifier().cssClass("oceantheme-main-grid"))
        );

        return Column.of(layout);
    }
}
