package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.DatabaseOverview;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/databases")
public class DatabasesPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Bases de Datos";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String action = params.get("formAction");
        if ("create".equalsIgnoreCase(action)) {
            String name = params.get("databaseName");
            if (name != null && !name.isBlank()) {
                clusterService.createDatabase(name.trim());
            }
        } else if ("rename".equalsIgnoreCase(action)) {
            String oldName = params.get("oldName");
            String newName = params.get("newName");
            if (oldName != null && newName != null) {
                clusterService.renameDatabase(oldName.trim(), newName.trim());
            }
        }
        redirect(exchange, "/databases");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        String db = params.get("db");

        if ("delete".equalsIgnoreCase(action) && db != null) {
            clusterService.deleteDatabase(db);
        }

        List<DatabaseOverview> dbs = clusterService.getDatabaseOverviews();
        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Gestión de Bases de Datos</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Creación de esquemas multimodelo, administración de buckets y purgado seguro</p>")
          .append("  </div>")
          .append("</div>");

        // Two Columns: Database List & Create Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-database' style='color:#00d4ff; margin-right:8px;'></i> Bases de Datos en el Clúster</h3>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Nombre de Base</th><th>Buckets</th><th>Registros Totales</th><th>Tamaño Disco</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>");

        for (DatabaseOverview d : dbs) {
            sb.append("<tr>")
              .append("<td><b style='color:#00d4ff;'>").append(d.name()).append("</b></td>")
              .append("<td>").append(d.engineCount()).append(" buckets</td>")
              .append("<td>").append(String.format("%,d", d.recordCount())).append("</td>")
              .append("<td>").append(d.sizeFormatted()).append("</td>")
              .append("<td><span class='cyber-badge-online'>").append(d.status()).append("</span></td>")
              .append("<td style='display:flex; gap:6px;'>")
              .append("  <a href='/records?db=").append(d.name()).append("' class='btn-cyber' style='padding:4px 8px; font-size:11px;'><i class='fas fa-eye'></i> Unidades</a>")
              .append("  <a href='/engines?db=").append(d.name()).append("' class='btn-cyber' style='background:#6366f1; padding:4px 8px; font-size:11px;'><i class='fas fa-cog'></i> Motores</a>")
              .append("  <a href='?action=delete&db=").append(d.name()).append("' class='btn-danger' style='padding:4px 8px; font-size:11px;' onclick=\"return confirm('¿Seguro de eliminar la base de datos ").append(d.name()).append("?');\"><i class='fas fa-trash'></i></a>")
              .append("</td></tr>");
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Create & Rename Cards
        sb.append("<div style='display:flex; flex-direction:column; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-plus' style='color:#22c55e; margin-right:8px;'></i> Nueva Base de Datos</h3>")
          .append("  <form method='POST' action='/databases' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <input type='hidden' name='formAction' value='create'/>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Nombre del Espacio</label><input type='text' name='databaseName' required class='cyber-input' placeholder='ej. finanzas_db'/></div>")
          .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-check'></i> Crear Base de Datos</button>")
          .append("  </form>")
          .append("</div>")

          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-edit' style='color:#eab308; margin-right:8px;'></i> Renombrar Base de Datos</h3>")
          .append("  <form method='POST' action='/databases' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <input type='hidden' name='formAction' value='rename'/>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Base Actual</label>")
          .append("      <select name='oldName' class='cyber-input'>");
        for (DatabaseOverview d : dbs) {
            sb.append("<option value='").append(d.name()).append("'>").append(d.name()).append("</option>");
        }
        sb.append("      </select>")
          .append("    </div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Nuevo Nombre</label><input type='text' name='newName' required class='cyber-input' placeholder='ej. finanzas_v2'/></div>")
          .append("    <button type='submit' class='btn-cyber' style='background:#f59e0b; justify-content:center;'><i class='fas fa-exchange-alt'></i> Renombrar Base</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
