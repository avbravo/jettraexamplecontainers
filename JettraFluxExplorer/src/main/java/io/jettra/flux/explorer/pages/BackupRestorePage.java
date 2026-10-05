package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.BackupItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/backup")
public class BackupRestorePage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Copias de Seguridad";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String db = params.get("databaseName");
        if (db != null && !db.isBlank()) {
            clusterService.createBackup(db.trim());
        }
        redirect(exchange, "/backup?msg=created");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        String bId = params.get("id");
        String restoreNotice = null;

        if ("restore".equalsIgnoreCase(action) && bId != null) {
            boolean ok = clusterService.restoreBackup(bId);
            restoreNotice = ok ? "✅ Snapshot " + bId + " restaurado y verificado con éxito en SSTables." : "❌ Error al restaurar snapshot.";
        }

        List<BackupItem> backups = clusterService.getBackups();
        List<String> dbs = clusterService.getDatabaseNames();

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Copias de Seguridad & Snapshots Atómicos</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Respaldos consistentes Point-in-Time sin bloqueo de escrituras concurrentes</p>")
          .append("  </div>")
          .append("</div>");

        if (restoreNotice != null) {
            sb.append("<div style='background:rgba(34, 197, 94, 0.15); border:1px solid #22c55e; color:#bbf7d0; padding:12px 16px; border-radius:8px; margin-bottom:20px;'>")
              .append(restoreNotice)
              .append("</div>");
        }

        // Two Columns: Table & Generate Backup Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-archive' style='color:#00d4ff; margin-right:8px;'></i> Snapshots Disponibles en Clúster</h3>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>ID</th><th>Base de Datos</th><th>Archivo (.jbak)</th><th>Tamaño</th><th>Fecha Creación</th><th>Tiempo</th><th>Acción</th></tr></thead><tbody>");

        if (backups.isEmpty()) {
            sb.append("<tr><td colspan='7' style='text-align:center; color:#64748b; padding:24px;'>No se han generado snapshots aún.</td></tr>");
        } else {
            for (BackupItem b : backups) {
                sb.append("<tr>")
                  .append("<td><code>").append(b.id()).append("</code></td>")
                  .append("<td><b style='color:#00d4ff;'>").append(b.database()).append("</b></td>")
                  .append("<td>").append(b.fileName()).append("</td>")
                  .append("<td>").append(b.sizeFormatted()).append("</td>")
                  .append("<td>").append(b.createdAt()).append("</td>")
                  .append("<td>").append(b.durationMs()).append(" ms</td>")
                  .append("<td>")
                  .append("  <a href='?action=restore&id=").append(b.id()).append("' class='btn-cyber' style='background:#059669; padding:4px 8px; font-size:11px;' onclick=\"return confirm('¿Restaurar snapshot atómico ").append(b.id()).append("?');\"><i class='fas fa-undo'></i> Restaurar</a>")
                  .append("</td></tr>");
            }
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Generate Backup Form
        sb.append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-camera' style='color:#22c55e; margin-right:8px;'></i> Generar Snapshot Atómico</h3>")
          .append("  <form method='POST' action='/backup' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Base de Datos a Respaldar</label>")
          .append("      <select name='databaseName' class='cyber-input'>");
        for (String d : dbs) {
            sb.append("<option value='").append(d).append("'>").append(d).append("</option>");
        }
        sb.append("      </select>")
          .append("    </div>")
          .append("    <p style='font-size:12px; color:#94a3b8; margin:4px 0;'>El snapshot captura el estado consistente de todas las MemTables y referencias SSTable sin detener lecturas ni escrituras concurrentes.</p>")
          .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-download'></i> Iniciar Snapshot</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
