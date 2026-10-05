package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.RecordItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/records")
public class RecordsPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Unidades de Registros";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String db = params.get("db");
        String bucket = params.get("bucket");
        String id = params.get("recordId");
        String json = params.get("payload");

        if (db != null && bucket != null && id != null && json != null) {
            clusterService.insertRecord(db.trim(), bucket.trim(), id.trim(), json.trim());
        }
        redirect(exchange, "/records?db=" + (db != null ? db : "") + "&bucket=" + (bucket != null ? bucket : ""));
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        List<String> dbs = clusterService.getDatabaseNames();
        String selectedDb = params.getOrDefault("db", dbs.isEmpty() ? "" : dbs.get(0));

        List<String> buckets = clusterService.getBucketNames(selectedDb);
        String selectedBucket = params.getOrDefault("bucket", buckets.isEmpty() ? "default" : buckets.get(0));

        String query = params.getOrDefault("q", "");
        String action = params.get("action");
        String delId = params.get("delId");

        if ("delete".equalsIgnoreCase(action) && delId != null) {
            clusterService.deleteRecord(selectedDb, selectedBucket, delId);
        }

        List<RecordItem> records = clusterService.getRecords(selectedDb, selectedBucket, query, 0, 100);

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Unidades de Almacenamiento & Registros</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Exploración en tiempo real de registros inmutables, documentos JSON y grafos</p>")
          .append("  </div>")
          .append("</div>");

        // Filter Bar (Database, Bucket, Search Query)
        sb.append("<div class='explorer-card' style='padding:14px 20px; margin-bottom:18px;'>")
          .append("  <form method='GET' action='/records' style='display:flex; gap:14px; align-items:center; flex-wrap:wrap;'>")
          .append("    <div style='display:flex; align-items:center; gap:8px;'>")
          .append("      <span style='font-size:12px; color:#94a3b8; font-weight:700;'>BASE:</span>")
          .append("      <select name='db' class='cyber-input' style='width:auto;' onchange='this.form.submit()'>");
        for (String d : dbs) {
            String sel = d.equalsIgnoreCase(selectedDb) ? "selected" : "";
            sb.append("<option value='").append(d).append("' ").append(sel).append(">").append(d).append("</option>");
        }
        sb.append("      </select>")
          .append("    </div>")

          .append("    <div style='display:flex; align-items:center; gap:8px;'>")
          .append("      <span style='font-size:12px; color:#94a3b8; font-weight:700;'>BUCKET:</span>")
          .append("      <select name='bucket' class='cyber-input' style='width:auto;' onchange='this.form.submit()'>");
        for (String b : buckets) {
            String sel = b.equalsIgnoreCase(selectedBucket) ? "selected" : "";
            sb.append("<option value='").append(b).append("' ").append(sel).append(">").append(b).append("</option>");
        }
        sb.append("      </select>")
          .append("    </div>")

          .append("    <div style='flex:1; min-width:200px;'>")
          .append("      <input type='text' name='q' value='").append(query).append("' class='cyber-input' placeholder='Buscar por clave primaria o contenido JSON...'/>")
          .append("    </div>")
          .append("    <button type='submit' class='btn-cyber'><i class='fas fa-search'></i> Filtrar</button>")
          .append("  </form>")
          .append("</div>");

        // Two Columns: Table & Insert Record Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:14px;'>")
          .append("    <h3 style='margin:0; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-th-list' style='color:#00d4ff; margin-right:8px;'></i> Registros en ").append(selectedDb).append("/").append(selectedBucket).append(" (").append(records.size()).append(")</h3>")
          .append("  </div>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Clave Primaria (ID)</th><th>Contenido / Resumen</th><th>Refs</th><th>Acciones</th></tr></thead><tbody>");

        if (records.isEmpty()) {
            sb.append("<tr><td colspan='4' style='text-align:center; color:#64748b; padding:24px;'>No se encontraron registros en este bucket.</td></tr>");
        } else {
            for (RecordItem r : records) {
                sb.append("<tr>")
                  .append("<td><code style='color:#00d4ff; font-weight:700;'>").append(r.id()).append("</code></td>")
                  .append("<td><div style='max-width:380px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; font-family:monospace; font-size:12px; color:#cbd5e1;'>").append(r.preview().replace("<", "&lt;")).append("</div></td>")
                  .append("<td><span class='cyber-badge-info'>").append(r.type()).append("</span></td>")
                  .append("<td>")
                  .append("  <a href='?db=").append(selectedDb).append("&bucket=").append(selectedBucket).append("&action=delete&delId=").append(r.id()).append("' class='btn-danger' style='padding:4px 8px; font-size:11px;' onclick=\"return confirm('¿Eliminar registro ").append(r.id()).append("?');\"><i class='fas fa-trash'></i></a>")
                  .append("</td></tr>");
            }
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Insert Record Form
        sb.append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-file-signature' style='color:#22c55e; margin-right:8px;'></i> Insertar Registro Unitario</h3>")
          .append("  <form method='POST' action='/records' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <input type='hidden' name='db' value='").append(selectedDb).append("'/>")
          .append("    <input type='hidden' name='bucket' value='").append(selectedBucket).append("'/>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>ID del Registro</label><input type='text' name='recordId' required class='cyber-input' placeholder='ej. DOC-9921'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Payload JSON / Vector</label><textarea name='payload' rows='6' required class='cyber-input' style='font-family:monospace; resize:vertical;' placeholder='{\\n  \"status\": \"ACTIVE\",\\n  \"value\": 120.5\\n}'></textarea></div>")
          .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-save'></i> Persistir en SSTable</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
