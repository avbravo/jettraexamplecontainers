package io.jettra.flux.designer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.security.widget.PageWidgetAllow;
import io.jettra.flux.core.Modifier;
import io.jettra.flux.core.Widget;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.service.DesignerSessionState;
import io.jettra.flux.pages.FluxBaseHandler;
import io.jettra.server.JettraServer;
import io.jettra.flux.widgets.*;

import java.util.Map;

@PageWidgetAllow(role = {jcf.AppRole.ADMIN, jcf.AppRole.MANAGER, jcf.AppRole.USER})
public abstract class TemplatePage extends FluxBaseHandler {

    protected DesignerSessionState sessionState = DesignerSessionState.getInstance();

    @Override
    protected Widget buildUI(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        MavenProjectInfo proj = sessionState.getCurrentProject();
        String projName = (proj != null) ? proj.getArtifactId() : "Ningún Proyecto";
        String projPath = (proj != null) ? proj.getProjectPath() : "No seleccionado";

        // Navigation Menu
        Widget menu = Left.of(
            SidebarLogo.of("fas fa-drafting-compass", "Flux Designer 3D"),
            SidebarCategory.of("DISEÑO Y COMPONENTES"),
            WidgetLet.of("Diseñador Visual").icon("fas fa-palette").url(JettraServer.resolvePath("/designer")),
            WidgetLet.of("Metaverso 3D Police").icon(Icon.CUBE).url(JettraServer.resolvePath("/metaverse")),

            SidebarCategory.of("PROYECTO MAVEN"),
            WidgetLet.of("Gestor de Proyecto & POM").icon("fas fa-project-diagram").url(JettraServer.resolvePath("/projects")),
            WidgetLet.of("Generador de Código Java").icon("fas fa-code").url(JettraServer.resolvePath("/codepreview"))
        ).modifier(new Modifier().cssClass("professional-left").style("background:#080c14; border-right:1px solid #1e293b;"));

        // Top Navigation Bar
        Widget projectChip = RawHtml.of(
            "<div style='display:inline-flex; align-items:center; gap:8px; background:#0f172a; border:1px solid #334155; padding:6px 14px; border-radius:8px; font-size:12px; color:#38bdf8;' title='" + projPath + "'>"
            + "<i class='fas fa-cube' style='color:#00d4ff;'></i>"
            + "<span>PROYECTO: <b style='color:#fff;'>" + projName + "</b></span>"
            + "</div>"
        );

        Widget userChip = RawHtml.of(
            "<div style='display:inline-flex; align-items:center; gap:10px; background:#1e293b; padding:4px 12px; border-radius:999px; border:1px solid #334155;'>"
            + "<span style='display:inline-block; width:10px; height:10px; border-radius:50%; background:#22c55e; box-shadow:0 0 8px #22c55e;'></span>"
            + "<span style='color:#f8fafc; font-weight:700; font-size:13px;'>developer</span>"
            + "</div>"
        );

        Widget topBar = Top.of(
            Row.of(
                ActionIcon.of(Icon.BARS, "toggleSidebar()"),
                Header.of(4, "JettraFlux Designer").modifier(new Modifier().style("color:#f8fafc; font-weight:800; margin:0;"))
            ).modifier(new Modifier().style("display:flex; align-items:center; gap:12px;")),
            Row.of(
                projectChip,
                ThemeChanged.of().current(currentTheme),
                userChip
            ).modifier(new Modifier().style("display:flex; align-items:center; gap:14px;"))
        );

        Widget customCss = RawHtml.of(
            "<style>"
            + "body { background: #06090f !important; color: #f1f5f9; font-family: 'Inter', system-ui, sans-serif; }"
            + ".designer-card { background: #0c111d; border: 1px solid #1e293b; border-radius: 12px; padding: 20px; box-shadow: 0 4px 20px rgba(0,0,0,0.5); margin-bottom: 20px; }"
            + ".palette-item { background: #131b2e; border: 1px solid #1e293b; padding: 10px 14px; border-radius: 8px; font-size: 13px; font-weight: 600; color: #e2e8f0; display: flex; align-items: center; gap: 10px; cursor: pointer; transition: 0.15s; margin-bottom: 8px; text-decoration: none; }"
            + ".palette-item:hover { background: #1e293b; border-color: #00d4ff; color: #00d4ff; transform: translateX(3px); }"
            + ".canvas-box { border: 2px dashed #334155; border-radius: 10px; padding: 16px; margin: 10px 0; background: rgba(15, 23, 42, 0.4); position: relative; transition: 0.2s; }"
            + ".canvas-box:hover { border-color: #00d4ff; background: rgba(15, 23, 42, 0.7); }"
            + ".canvas-box-selected { border-color: #22c55e !important; box-shadow: 0 0 15px rgba(34, 197, 94, 0.3); background: rgba(34, 197, 94, 0.04) !important; }"
            + ".btn-cyber { background: #0284c7; color: #fff; padding: 8px 16px; border-radius: 8px; font-weight: 700; text-decoration: none; display: inline-flex; align-items: center; gap: 8px; border: none; cursor: pointer; transition: 0.2s ease; }"
            + ".btn-cyber:hover { background: #0369a1; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(2, 132, 199, 0.4); }"
            + ".btn-danger { background: #dc2626; color: #fff; padding: 6px 12px; border-radius: 6px; font-weight: 700; text-decoration: none; border: none; cursor: pointer; display: inline-flex; align-items: center; gap: 4px; }"
            + ".btn-danger:hover { background: #b91c1c; }"
            + ".cyber-badge-info { background: rgba(56, 189, 248, 0.15); color: #38bdf8; border: 1px solid rgba(56, 189, 248, 0.3); padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; }"
            + ".cyber-badge-online { background: rgba(34, 197, 94, 0.15); color: #22c55e; border: 1px solid rgba(34, 197, 94, 0.3); padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; }"
            + ".cyber-input { background: #090d16; border: 1px solid #334155; color: #f8fafc; padding: 9px 14px; border-radius: 8px; font-size: 13px; width: 100%; outline: none; transition: 0.2s border-color; box-sizing: border-box; }"
            + ".cyber-input:focus { border-color: #00d4ff; box-shadow: 0 0 0 2px rgba(0, 212, 255, 0.2); }"
            + ".designer-table { width:100%; border-collapse: collapse; margin-top: 10px; font-size: 13px; }"
            + ".designer-table th { background: #1e293b; color: #94a3b8; padding: 12px 14px; text-align: left; font-weight: 700; border-bottom: 2px solid #334155; }"
            + ".designer-table td { padding: 12px 14px; border-bottom: 1px solid #1e293b; color: #e2e8f0; }"
            + "</style>"
        );

        Widget centerContent = Column.of(
            customCss,
            buildCenter(exchange, params, currentTheme)
        ).modifier(new Modifier().cssClass("professional-center").style("padding: 24px; min-height: calc(100vh - 120px); background: #06090f;"));

        Widget footer = Footer.of(
            Row.of(
                Paragraph.of("JettraFluxDesigner v1.0.0 • Diseñador Visual Reactivo para Java 25 & JettraEE"),
                Paragraph.of("Inspirado en la Arquitectura Tridimensional de JettraStorePolice3D")
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
