package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.PoliceIncident;
import io.jettra.flux.explorer.model.PoliceSentinel;

import java.util.List;
import java.util.Map;

@Page(path = "/police")
public class PoliceSentinelPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Centinelas & Incidentes";
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        if ("simulate_alert".equals(action)) {
            clusterService.logIncident("Heap Sentinel", "Simulación de alerta: Presión en Heap superó el umbral de 85%", "WARN", "Compactación forzada");
        }

        List<PoliceSentinel> sentinels = clusterService.getSentinels();
        List<PoliceIncident> incidents = clusterService.getIncidents();

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Muro de Incidentes & Centinelas JettraPolice</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Monitoreo autónomo de fallos de nodo, saturación de memoria y quórum Raft</p>")
          .append("  </div>")
          .append("  <div style='display:flex; gap:10px;'>")
          .append("    <a href='?action=simulate_alert' class='btn-cyber' style='background:#f59e0b;'><i class='fas fa-bell'></i> Simular Evento de Centinela</a>")
          .append("  </div>")
          .append("</div>");

        // Sentinels 4 Cards
        sb.append("<div style='display:grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap:18px; margin-bottom:24px;'>");
        for (PoliceSentinel s : sentinels) {
            sb.append("<div class='explorer-card' style='border-top:3px solid ").append(s.badgeColor()).append("; margin-bottom:0;'>")
              .append("  <div style='display:flex; justify-content:space-between; align-items:flex-start;'>")
              .append("    <div>")
              .append("      <h3 style='margin:0; font-size:16px; font-weight:800; color:").append(s.badgeColor()).append(";'>").append(s.name()).append("</h3>")
              .append("      <div style='font-size:11px; color:#94a3b8; margin-top:2px;'>").append(s.title()).append("</div>")
              .append("    </div>")
              .append("    <span class='cyber-badge-online'>").append(s.status()).append("</span>")
              .append("  </div>")
              .append("  <p style='margin:12px 0; font-size:12px; color:#cbd5e1; min-height:36px;'>").append(s.mission()).append("</p>")
              .append("  <div style='display:flex; justify-content:space-between; align-items:center; font-size:12px; color:#94a3b8; border-top:1px solid #1e293b; padding-top:10px;'>")
              .append("    <span>Nodo: <b style='color:#fff;'>").append(s.assignedNode()).append("</b></span>")
              .append("    <span>Salud: <b style='color:#22c55e;'>").append(s.healthPercent()).append("%</b></span>")
              .append("  </div>")
              .append("</div>");
        }
        sb.append("</div>");

        // Incidents Wall Table
        sb.append("<div class='explorer-card'>")
          .append("<h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-list-alt' style='color:#00d4ff; margin-right:8px;'></i> Registro en Vivo de Telemetría e Incidentes</h3>")
          .append("<table class='explorer-table'>")
          .append("<thead><tr><th>Hora</th><th>Centinela Emisor</th><th>Descripción del Evento</th><th>Severidad</th><th>Acción Tomada</th></tr></thead><tbody>");

        for (PoliceIncident inc : incidents) {
            String badge = switch (inc.severity().toUpperCase()) {
                case "WARN" -> "<span class='cyber-badge-warn'>ADVERTENCIA</span>";
                case "CRITICAL" -> "<span class='cyber-badge-warn' style='background:rgba(239,68,68,0.2); color:#ef4444; border-color:#ef4444;'>CRÍTICO</span>";
                default -> "<span class='cyber-badge-online'>NORMAL / OK</span>";
            };

            sb.append("<tr>")
              .append("<td><code>").append(inc.timestamp()).append("</code></td>")
              .append("<td><b>").append(inc.sentinel()).append("</b></td>")
              .append("<td>").append(inc.description()).append("</td>")
              .append("<td>").append(badge).append("</td>")
              .append("<td><span style='color:#94a3b8; font-size:12px;'>").append(inc.actionTaken()).append("</span></td>")
              .append("</tr>");
        }

        sb.append("</tbody></table></div>");

        return RawHtml.of(sb.toString());
    }
}
