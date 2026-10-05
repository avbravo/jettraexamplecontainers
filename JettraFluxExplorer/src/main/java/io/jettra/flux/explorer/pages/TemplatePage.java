package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.security.widget.PageWidgetAllow;
import io.jettra.flux.core.Modifier;
import io.jettra.flux.core.Widget;
import io.jettra.flux.pages.FluxBaseHandler;
import io.jettra.server.JettraServer;
import io.jettra.flux.widgets.*;
import io.jettra.flux.explorer.service.ConnectionManager;
import io.jettra.flux.explorer.service.StoreClusterService;

import java.io.IOException;
import java.util.Map;

@PageWidgetAllow(role = {jcf.AppRole.ADMIN, jcf.AppRole.OPERATOR, jcf.AppRole.ANALYST})
public abstract class TemplatePage extends FluxBaseHandler {

    protected ConnectionManager connMgr = ConnectionManager.getInstance();
    protected StoreClusterService clusterService = StoreClusterService.getInstance();

    @Override
    protected Widget buildUI(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains("action=toggle_multinode")) {
            boolean current = clusterService.isMultinodeActive();
            clusterService.setMultinodeActive(!current);
            try {
                exchange.getResponseHeaders().set("Location", exchange.getRequestURI().getPath());
                exchange.sendResponseHeaders(302, -1);
                return Scaffold.of().body(Paragraph.of("Redirecting..."));
            } catch (IOException ignored) {}
        }

        var activeProfile = connMgr.getActiveProfile().orElse(null);
        String hostPort = activeProfile != null ? activeProfile.getHostPort() : "127.0.0.1:9010";

        // Navigation Menu
        Widget menu = Left.of(
            SidebarLogo.of("fas fa-shield-alt", "JettraPolice 3D"),
            SidebarCategory.of("SUPERVISIÓN Y CONTROL"),
            WidgetLet.of("Dashboard Clúster").icon("fas fa-tachometer-alt").url(JettraServer.resolvePath("/dashboard")),
            WidgetLet.of("JettraPolice 3D").icon(Icon.CUBE).url(JettraServer.resolvePath("/police3d")),
            WidgetLet.of("Centinelas & Incidentes").icon("fas fa-shield-alt").url(JettraServer.resolvePath("/police")),
            WidgetLet.of("Conexiones de Clúster").icon("fas fa-plug").url(JettraServer.resolvePath("/connections")),

            SidebarCategory.of("ALMACENAMIENTO MULTIMODELO"),
            WidgetLet.of("Bases de Datos").icon("fas fa-database").url(JettraServer.resolvePath("/databases")),
            WidgetLet.of("Unidades de Registros").icon(Icon.LAYER_GROUP).url(JettraServer.resolvePath("/records")),
            WidgetLet.of("Motores (Engines)").icon(Icon.COG).url(JettraServer.resolvePath("/engines")),
            WidgetLet.of("Índices & Rendimiento").icon("fas fa-bolt").url(JettraServer.resolvePath("/indexes")),

            SidebarCategory.of("HERRAMIENTAS & SEGURIDAD"),
            WidgetLet.of("Consola JettraSQL / QL").icon("fas fa-terminal").url(JettraServer.resolvePath("/query")),
            WidgetLet.of("Usuarios & ACL").icon(Icon.USERS).url(JettraServer.resolvePath("/users")),
            WidgetLet.of("Copias de Seguridad").icon(Icon.SAVE).url(JettraServer.resolvePath("/backup"))
        ).modifier(new Modifier().cssClass("professional-left").style("background:#090d16; border-right:1px solid #1e293b;"));

        // Top Navigation Bar
        boolean isMulti = clusterService.isMultinodeActive();
        String multiText = isMulti ? "RAFT MULTINODO: ACTIVO" : "MONONODO: STANDALONE";
        String multiBg = isMulti ? "#059669" : "#d97706";

        Widget clusterStatusBadge = RawHtml.of(
            "<div style='display:inline-flex; align-items:center; gap:8px; background:#0f172a; border:1px solid #334155; padding:6px 14px; border-radius:8px; font-size:12px; font-weight:700; color:#00d4ff;'>"
            + "<span style='display:inline-block; width:8px; height:8px; border-radius:50%; background:#22c55e; box-shadow:0 0 10px #22c55e;'></span>"
            + "<span>CLÚSTER: " + hostPort + "</span>"
            + "</div>"
        );

        Widget multinodeBtn = RawHtml.of(
            "<a href='?action=toggle_multinode' style='display:inline-flex; align-items:center; gap:6px; background:" + multiBg + "; color:#fff; padding:6px 12px; border-radius:8px; font-size:12px; font-weight:700; text-decoration:none; box-shadow:0 2px 8px rgba(0,0,0,0.3);'>"
            + "<i class='fas fa-network-wired'></i> " + multiText
            + "</a>"
        );

        Widget userChip = RawHtml.of(
            "<div style='display:inline-flex; align-items:center; gap:10px; background:#1e293b; padding:4px 12px; border-radius:999px; border:1px solid #334155;'>"
            + "<img src='https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=80&q=80' style='width:28px; height:28px; border-radius:50%; border:2px solid #00d4ff;'/>"
            + "<span style='color:#f8fafc; font-weight:700; font-size:13px;'>admin</span>"
            + "<a href='" + JettraServer.resolvePath("/login?logout=true") + "' title='Cerrar Sesión' style='color:#ef4444; margin-left:4px;'><i class='fas fa-sign-out-alt'></i></a>"
            + "</div>"
        );

        Widget topBar = Top.of(
            Row.of(
                ActionIcon.of(Icon.BARS, "toggleSidebar()"),
                Header.of(4, "JettraFlux Explorer").modifier(new Modifier().style("color:#f8fafc; font-weight:800; letter-spacing:0.5px; margin:0;"))
            ).modifier(new Modifier().style("display:flex; align-items:center; gap:12px;")),
            Row.of(
                clusterStatusBadge,
                multinodeBtn,
                ThemeChanged.of().current(currentTheme),
                userChip
            ).modifier(new Modifier().style("display:flex; align-items:center; gap:14px;"))
        );

        Widget customCss = RawHtml.of(
            "<style>"
            + "body { background: #07090e !important; color: #f1f5f9; font-family: 'Inter', system-ui, -apple-system, sans-serif; }"
            + ".explorer-card { background: #0f172a; border: 1px solid #1e293b; border-radius: 12px; padding: 20px; box-shadow: 0 4px 20px rgba(0,0,0,0.4); margin-bottom: 20px; }"
            + ".explorer-kpi { background: linear-gradient(145deg, #0f172a 0%, #1e293b 100%); border: 1px solid #334155; border-radius: 12px; padding: 18px; }"
            + ".explorer-table { width:100%; border-collapse: collapse; margin-top: 10px; font-size: 13px; }"
            + ".explorer-table th { background: #1e293b; color: #94a3b8; padding: 12px 14px; text-align: left; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px; border-bottom: 2px solid #334155; }"
            + ".explorer-table td { padding: 12px 14px; border-bottom: 1px solid #1e293b; color: #e2e8f0; }"
            + ".explorer-table tr:hover { background: rgba(56, 189, 248, 0.04); }"
            + ".btn-cyber { background: #0284c7; color: #fff; padding: 8px 16px; border-radius: 8px; font-weight: 700; text-decoration: none; display: inline-flex; align-items: center; gap: 8px; border: none; cursor: pointer; transition: 0.2s ease; }"
            + ".btn-cyber:hover { background: #0369a1; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(2, 132, 199, 0.4); }"
            + ".btn-danger { background: #dc2626; color: #fff; padding: 6px 12px; border-radius: 6px; font-weight: 700; text-decoration: none; border: none; cursor: pointer; display: inline-flex; align-items: center; gap: 4px; }"
            + ".btn-danger:hover { background: #b91c1c; }"
            + ".btn-success { background: #16a34a; color: #fff; padding: 6px 12px; border-radius: 6px; font-weight: 700; text-decoration: none; border: none; cursor: pointer; display: inline-flex; align-items: center; gap: 4px; }"
            + ".cyber-badge-online { background: rgba(34, 197, 94, 0.15); color: #22c55e; border: 1px solid rgba(34, 197, 94, 0.3); padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; }"
            + ".cyber-badge-warn { background: rgba(234, 179, 8, 0.15); color: #eab308; border: 1px solid rgba(234, 179, 8, 0.3); padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; }"
            + ".cyber-badge-info { background: rgba(56, 189, 248, 0.15); color: #38bdf8; border: 1px solid rgba(56, 189, 248, 0.3); padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; }"
            + ".cyber-input { background: #090d16; border: 1px solid #334155; color: #f8fafc; padding: 9px 14px; border-radius: 8px; font-size: 13px; width: 100%; outline: none; transition: 0.2s border-color; box-sizing: border-box; }"
            + ".cyber-input:focus { border-color: #00d4ff; box-shadow: 0 0 0 2px rgba(0, 212, 255, 0.2); }"
            + "</style>"
        );

        Widget centerContent = Column.of(
            customCss,
            buildCenter(exchange, params, currentTheme)
        ).modifier(new Modifier().cssClass("professional-center").style("padding: 24px; min-height: calc(100vh - 120px); background: #07090e;"));

        Widget footer = Footer.of(
            Row.of(
                Paragraph.of("JettraFlux Explorer v1.0.0 — Consola de Control de JettraStore y Supervisión JettraPolice 3D"),
                Paragraph.of("Loom Virtual Threads • Panama Foreign Function & Memory • Raft Multi-Paxos")
            ).modifier(new Modifier().style("display:flex; justify-content:space-between; width:100%; font-size:12px; color:#64748b; padding:12px 24px;"))
        );

        Widget body = Dashboard.of(
            topBar,
            menu,
            centerContent,
            footer
        );

        return Scaffold.of().body(body);
    }

    protected abstract Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme);
}
