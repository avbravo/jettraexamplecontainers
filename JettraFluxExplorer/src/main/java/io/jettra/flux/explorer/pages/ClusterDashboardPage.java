package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.ServerNode;

import java.util.List;
import java.util.Map;

@Page(path = "/dashboard")
public class ClusterDashboardPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Panel del Clúster";
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        List<ServerNode> nodes = clusterService.getNodes();
        int onlineCount = clusterService.getOnlineNodesCount();
        long totalTps = clusterService.getTotalTps();
        double avgLatency = clusterService.getAverageLatency();
        double avgCpu = clusterService.getAverageCpu();

        String selectedNodeId = params.getOrDefault("nodeId", "node-01");
        var telemetry = clusterService.getNodeInternalTelemetry(selectedNodeId);

        StringBuilder sb = new StringBuilder();

        // 1. Executive KPIs
        sb.append("<div style='display:grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 18px; margin-bottom: 24px;'>")
          .append("<div class='explorer-kpi'>")
          .append("  <div style='color:#94a3b8; font-size:12px; font-weight:700; text-transform:uppercase;'>Nodos en Línea</div>")
          .append("  <div style='font-size:28px; font-weight:900; color:#22c55e; margin:6px 0;'>").append(onlineCount).append(" / ").append(nodes.size()).append("</div>")
          .append("  <div style='font-size:12px; color:#64748b;'>Raft Quorum 100% Operativo</div>")
          .append("</div>")

          .append("<div class='explorer-kpi'>")
          .append("  <div style='color:#94a3b8; font-size:12px; font-weight:700; text-transform:uppercase;'>Rendimiento Transaccional</div>")
          .append("  <div style='font-size:28px; font-weight:900; color:#00d4ff; margin:6px 0;'>").append(String.format("%,d", totalTps)).append(" <span style='font-size:16px;'>TPS</span></div>")
          .append("  <div style='font-size:12px; color:#64748b;'>Escrituras atómicas WAL</div>")
          .append("</div>")

          .append("<div class='explorer-kpi'>")
          .append("  <div style='color:#94a3b8; font-size:12px; font-weight:700; text-transform:uppercase;'>Latencia Promedio P99</div>")
          .append("  <div style='font-size:28px; font-weight:900; color:#f59e0b; margin:6px 0;'>").append(String.format("%.2f", avgLatency)).append(" ms</div>")
          .append("  <div style='font-size:12px; color:#64748b;'>Sin pausas GC (Loom Threads)</div>")
          .append("</div>")

          .append("<div class='explorer-kpi'>")
          .append("  <div style='color:#94a3b8; font-size:12px; font-weight:700; text-transform:uppercase;'>Carga CPU Promedio</div>")
          .append("  <div style='font-size:28px; font-weight:900; color:#38bdf8; margin:6px 0;'>").append(String.format("%.1f", avgCpu)).append("%</div>")
          .append("  <div style='font-size:12px; color:#64748b;'>Optimizado para Java 25 Panama</div>")
          .append("</div>")
          .append("</div>");

        // 2. Action Bar
        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom: 20px; flex-wrap:wrap; gap:12px;'>")
          .append("<div>")
          .append("  <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Topología del Clúster JettraStore</h2>")
          .append("  <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Monitoreo en tiempo real de nodos, réplicas Raft y uso de memoria física</p>")
          .append("</div>")
          .append("<div style='display:flex; gap:10px;'>")
          .append("  <a href='/police3d' class='btn-cyber'><i class='fas fa-cube'></i> Vista JettraPolice 3D</a>")
          .append("  <a href='/databases' class='btn-cyber' style='background:#10b981;'><i class='fas fa-database'></i> Administrar Bases de Datos</a>")
          .append("  <a href='/query' class='btn-cyber' style='background:#6366f1;'><i class='fas fa-terminal'></i> Consola SQL</a>")
          .append("</div>")
          .append("</div>");

        // 3. Nodes Table
        sb.append("<div class='explorer-card'>")
          .append("<h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#e2e8f0;'><i class='fas fa-server' style='color:#00d4ff; margin-right:8px;'></i> Nodos Activos en el Clúster</h3>")
          .append("<table class='explorer-table'>")
          .append("<thead><tr>")
          .append("<th>ID Nodo</th><th>Nombre</th><th>Dirección Host:Puerto</th><th>Rol Raft</th><th>Estado</th><th>Latencia</th><th>CPU %</th><th>Memoria Heap</th><th>TPS</th><th>Acción</th>")
          .append("</tr></thead><tbody>");

        for (ServerNode n : nodes) {
            String roleBadge = "LEADER".equalsIgnoreCase(n.role())
                ? "<span class='cyber-badge-online'>LÍDER RAFT</span>"
                : "<span class='cyber-badge-info'>SEGUIDOR</span>";

            String isSel = n.id().equalsIgnoreCase(selectedNodeId) ? "style='background:rgba(0, 212, 255, 0.08); font-weight:bold;'" : "";

            sb.append("<tr ").append(isSel).append(">")
              .append("<td><code style='color:#00d4ff;'>").append(n.id()).append("</code></td>")
              .append("<td>").append(n.name()).append("</td>")
              .append("<td>").append(n.host()).append(":").append(n.port()).append("</td>")
              .append("<td>").append(roleBadge).append("</td>")
              .append("<td><span class='cyber-badge-online'>").append(n.status()).append("</span></td>")
              .append("<td>").append(n.latencyMs()).append(" ms</td>")
              .append("<td>").append(n.cpuPercent()).append("%</td>")
              .append("<td>").append(n.memoryUsedMb()).append(" / ").append(n.memoryTotalMb()).append(" MB</td>")
              .append("<td>").append(String.format("%,d", n.tps())).append("</td>")
              .append("<td><a href='?nodeId=").append(n.id()).append("' class='btn-cyber' style='padding:4px 8px; font-size:11px;'>Inspeccionar</a></td>")
              .append("</tr>");
        }

        sb.append("</tbody></table>")
          .append("</div>");

        // 4. Node Internal Telemetry Inspector (Loom, Panama FFM, SSTables, WAL)
        sb.append("<div class='explorer-card' style='border-left: 4px solid #00d4ff;'>")
          .append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;'>")
          .append("  <h3 style='margin:0; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-microchip' style='color:#00d4ff; margin-right:8px;'></i> Telemetría Interna en Detalle: ").append(telemetry.nodeName()).append(" (").append(telemetry.nodeId()).append(")</h3>")
          .append("  <span class='cyber-badge-online'>Loom Virtual Threads: ").append(telemetry.loomVirtualThreads()).append(" activos</span>")
          .append("</div>")
          .append("<div style='display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:16px;'>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8; font-weight:700;'>MEMORIA DIRECTA OFF-HEAP (PANAMA)</div>")
          .append("    <div style='font-size:18px; font-weight:800; color:#38bdf8; margin:4px 0;'>").append(telemetry.directMemoryUsedMb()).append(" / ").append(telemetry.directMemoryLimitMb()).append(" MB</div>")
          .append("    <div style='font-size:11px; color:#22c55e;'>Zero GC Pressure</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8; font-weight:700;'>MEMTABLE EN RAM</div>")
          .append("    <div style='font-size:18px; font-weight:800; color:#eab308; margin:4px 0;'>").append(telemetry.memTableMb()).append(" MB</div>")
          .append("    <div style='font-size:11px; color:#64748b;'>Skiplist Concurrente</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8; font-weight:700;'>ARCHIVOS SSTABLE EN DISCO</div>")
          .append("    <div style='font-size:18px; font-weight:800; color:#ec4899; margin:4px 0;'>").append(telemetry.sstableFiles()).append(" archivos</div>")
          .append("    <div style='font-size:11px; color:#64748b;'>Nivel 0 y Nivel 1 Compactados</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8; font-weight:700;'>CONSENSO RAFT (WAL)</div>")
          .append("    <div style='font-size:18px; font-weight:800; color:#10b981; margin:4px 0;'>Término ").append(telemetry.walTerm()).append(" | Índice ").append(telemetry.walIndex()).append("</div>")
          .append("    <div style='font-size:11px; color:#64748b;'>Latencia Disco: ").append(telemetry.ioDiskLatencyMs()).append(" ms</div>")
          .append("  </div>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
