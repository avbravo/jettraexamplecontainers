package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.IndexOverview;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/indexes")
public class IndexesPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Índices & Rendimiento";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String db = params.get("db");
        String bucket = params.get("bucket");
        String name = params.get("name");
        String type = params.get("type");
        String field = params.get("field");
        boolean unique = "true".equalsIgnoreCase(params.get("unique"));

        if (db != null && bucket != null && name != null && field != null) {
            clusterService.createIndex(db.trim(), bucket.trim(), name.trim(), type != null ? type.trim() : "BTree", field.trim(), unique);
        }
        redirect(exchange, "/indexes?db=" + (db != null ? db : ""));
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        List<String> dbs = clusterService.getDatabaseNames();
        String selectedDb = params.getOrDefault("db", dbs.isEmpty() ? "" : dbs.get(0));

        String action = params.get("action");
        String dropIdx = params.get("drop");
        if ("drop".equalsIgnoreCase(action) && dropIdx != null) {
            clusterService.dropIndex(dropIdx);
        }

        List<IndexOverview> indexes = clusterService.getIndexes(selectedDb);
        List<String> buckets = clusterService.getBucketNames(selectedDb);

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Administrador de Índices & Aceleración</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Estructuras B-Tree, Hash, Inverted Text y Vector HNSW sobre Panama Memory</p>")
          .append("  </div>")
          .append("</div>");

        // Filter Bar (Database)
        sb.append("<div class='explorer-card' style='padding:14px 20px; margin-bottom:18px;'>")
          .append("  <form method='GET' action='/indexes' style='display:flex; gap:14px; align-items:center;'>")
          .append("    <span style='font-size:12px; color:#94a3b8; font-weight:700;'>BASE DE DATOS:</span>")
          .append("    <select name='db' class='cyber-input' style='width:auto;' onchange='this.form.submit()'>");
        for (String d : dbs) {
            String sel = d.equalsIgnoreCase(selectedDb) ? "selected" : "";
            sb.append("<option value='").append(d).append("' ").append(sel).append(">").append(d).append("</option>");
        }
        sb.append("    </select>")
          .append("  </form>")
          .append("</div>");

        // Two Columns: Table & Create Index Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-bolt' style='color:#00d4ff; margin-right:8px;'></i> Índices Activos en ").append(selectedDb).append("</h3>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Nombre de Índice</th><th>Bucket</th><th>Tipo</th><th>Campo / Atributo</th><th>Único</th><th>Acciones</th></tr></thead><tbody>");

        if (indexes.isEmpty()) {
            sb.append("<tr><td colspan='6' style='text-align:center; color:#64748b; padding:24px;'>No hay índices registrados para esta base.</td></tr>");
        } else {
            for (IndexOverview idx : indexes) {
                sb.append("<tr>")
                  .append("<td><b style='color:#00d4ff;'>").append(idx.indexName()).append("</b></td>")
                  .append("<td><code>").append(idx.bucketName()).append("</code></td>")
                  .append("<td><span class='cyber-badge-info'>").append(idx.indexType()).append("</span></td>")
                  .append("<td>").append(idx.targetField()).append("</td>")
                  .append("<td>").append(idx.unique() ? "<span class='cyber-badge-online'>SÍ</span>" : "<span style='color:#64748b;'>NO</span>").append("</td>")
                  .append("<td>")
                  .append("  <a href='?db=").append(selectedDb).append("&action=drop&drop=").append(idx.indexName()).append("' class='btn-danger' style='padding:4px 8px; font-size:11px;' onclick=\"return confirm('¿Descartar índice ").append(idx.indexName()).append("?');\"><i class='fas fa-trash'></i></a>")
                  .append("</td></tr>");
            }
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Create Index Form
        sb.append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-plus' style='color:#22c55e; margin-right:8px;'></i> Crear Nuevo Índice</h3>")
          .append("  <form method='POST' action='/indexes' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <input type='hidden' name='db' value='").append(selectedDb).append("'/>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Bucket Objetivo</label>")
          .append("      <select name='bucket' class='cyber-input'>");
        for (String b : buckets) {
            sb.append("<option value='").append(b).append("'>").append(b).append("</option>");
        }
        sb.append("      </select>")
          .append("    </div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Nombre del Índice</label><input type='text' name='name' required class='cyber-input' placeholder='idx_user_email'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Tipo de Índice</label>")
          .append("      <select name='type' class='cyber-input'>")
          .append("        <option value='BTree'>B-Tree Range Index</option>")
          .append("        <option value='Hash'>Hash Direct Lookup</option>")
          .append("        <option value='Vector HNSW'>Vector Cosine / Euclidean (HNSW)</option>")
          .append("        <option value='FullText'>Inverted Full-Text Search</option>")
          .append("      </select>")
          .append("    </div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Campo o Expresión JSON</label><input type='text' name='field' required class='cyber-input' placeholder='ej. email o balance'/></div>")
          .append("    <div style='display:flex; align-items:center; gap:8px;'>")
          .append("      <input type='checkbox' name='unique' value='true' id='chkUnique'/>")
          .append("      <label for='chkUnique' style='font-size:12px; color:#cbd5e1;'>Índice con Restricción Única (Unique)</label>")
          .append("    </div>")
          .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-check'></i> Construir Índice</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
