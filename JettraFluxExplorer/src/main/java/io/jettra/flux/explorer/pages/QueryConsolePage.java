package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.RecordItem;
import io.jettra.flux.explorer.service.StoreClusterService.QueryResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/query")
public class QueryConsolePage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Consola JettraSQL / QL";
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        List<String> dbs = clusterService.getDatabaseNames();
        String selectedDb = params.getOrDefault("db", dbs.isEmpty() ? "" : dbs.get(0));

        List<String> buckets = clusterService.getBucketNames(selectedDb);
        String selectedBucket = params.getOrDefault("bucket", buckets.isEmpty() ? "default" : buckets.get(0));

        String query = params.getOrDefault("query", "SELECT * FROM " + selectedBucket + " LIMIT 50;");
        boolean isSql = !"jettraql".equalsIgnoreCase(params.get("dialect"));

        QueryResult result = null;
        if (params.containsKey("run")) {
            result = clusterService.executeQuery(selectedDb, selectedBucket, query, isSql);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Consola Interactiva JettraSQL & JettraQL</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Motor de consultas analíticas y transaccionales compiladas a bytecode en tiempo de ejecución</p>")
          .append("  </div>")
          .append("</div>");

        // Query Form
        sb.append("<div class='explorer-card'>")
          .append("  <form method='GET' action='/query'>")
          .append("    <input type='hidden' name='run' value='true'/>")
          .append("    <div style='display:flex; gap:14px; align-items:center; margin-bottom:14px; flex-wrap:wrap;'>")
          .append("      <div style='display:flex; align-items:center; gap:8px;'>")
          .append("        <span style='font-size:12px; color:#94a3b8; font-weight:700;'>BASE:</span>")
          .append("        <select name='db' class='cyber-input' style='width:auto;'>");
        for (String d : dbs) {
            String sel = d.equalsIgnoreCase(selectedDb) ? "selected" : "";
            sb.append("<option value='").append(d).append("' ").append(sel).append(">").append(d).append("</option>");
        }
        sb.append("        </select>")
          .append("      </div>")

          .append("      <div style='display:flex; align-items:center; gap:8px;'>")
          .append("        <span style='font-size:12px; color:#94a3b8; font-weight:700;'>BUCKET:</span>")
          .append("        <select name='bucket' class='cyber-input' style='width:auto;'>");
        for (String b : buckets) {
            String sel = b.equalsIgnoreCase(selectedBucket) ? "selected" : "";
            sb.append("<option value='").append(b).append("' ").append(sel).append(">").append(b).append("</option>");
        }
        sb.append("        </select>")
          .append("      </div>")

          .append("      <div style='display:flex; align-items:center; gap:14px;'>")
          .append("        <label style='font-size:12px; color:#cbd5e1; cursor:pointer;'><input type='radio' name='dialect' value='sql' ").append(isSql ? "checked" : "").append("/> JettraSQL (ANSI Compatible)</label>")
          .append("        <label style='font-size:12px; color:#cbd5e1; cursor:pointer;'><input type='radio' name='dialect' value='jettraql' ").append(!isSql ? "checked" : "").append("/> JettraQL (Pipeline Reactivo)</label>")
          .append("      </div>")
          .append("    </div>")

          .append("    <div style='margin-bottom:14px;'>")
          .append("      <textarea name='query' rows='5' class='cyber-input' style='font-family:monospace; font-size:13px; resize:vertical;'>").append(query).append("</textarea>")
          .append("    </div>")

          .append("    <div style='display:flex; justify-content:space-between; align-items:center;'>")
          .append("      <div style='display:flex; gap:8px;'>")
          .append("        <button type='button' class='btn-cyber' style='background:#334155; font-size:11px;' onclick=\"document.querySelector('textarea[name=query]').value='SELECT * FROM " + selectedBucket + " WHERE tier = \\'PLATINUM\\';'\">Query Demo 1</button>")
          .append("        <button type='button' class='btn-cyber' style='background:#334155; font-size:11px;' onclick=\"document.querySelector('textarea[name=query]').value='KNN_SEARCH embeddings_v1 [0.2, 0.4, 0.8] K=5;'\">Vector KNN</button>")
          .append("      </div>")
          .append("      <button type='submit' class='btn-cyber' style='background:#0284c7;'><i class='fas fa-play'></i> Ejecutar Consulta</button>")
          .append("    </div>")
          .append("  </form>")
          .append("</div>");

        // Results Section
        if (result != null) {
            sb.append("<div class='explorer-card' style='border-top:3px solid #10b981;'>")
              .append("  <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;'>")
              .append("    <h3 style='margin:0; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-check-circle' style='color:#10b981; margin-right:8px;'></i> ").append(result.message()).append("</h3>")
              .append("    <span class='cyber-badge-online'>").append(String.format("%.3f ms", result.elapsedMicros() / 1000.0)).append("</span>")
              .append("  </div>");

            if (result.records().isEmpty()) {
                sb.append("<p style='color:#94a3b8; font-size:13px;'>La consulta no produjo registros coincidentes.</p>");
            } else {
                sb.append("  <table class='explorer-table'>")
                  .append("  <thead><tr><th>ID</th><th>Registro / Contenido</th><th>Referencias</th></tr></thead><tbody>");
                for (RecordItem r : result.records()) {
                    sb.append("<tr>")
                      .append("<td><code style='color:#00d4ff;'>").append(r.id()).append("</code></td>")
                      .append("<td><pre style='margin:0; font-family:monospace; font-size:12px; color:#cbd5e1; max-width:600px; white-space:pre-wrap;'>").append(r.preview().replace("<", "&lt;")).append("</pre></td>")
                      .append("<td><span class='cyber-badge-info'>").append(r.type()).append("</span></td>")
                      .append("</tr>");
                }
                sb.append("  </tbody></table>");
            }
            sb.append("</div>");
        }

        return RawHtml.of(sb.toString());
    }
}
