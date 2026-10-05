package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.EngineOverview;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/engines")
public class EnginesPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Motores de Almacenamiento";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String db = params.get("db");
        String engineType = params.get("engineType");
        String bucket = params.get("bucketName");

        if (db != null && engineType != null && bucket != null) {
            clusterService.createEngineBucket(db.trim(), engineType.trim(), bucket.trim());
        }
        redirect(exchange, "/engines?db=" + (db != null ? db : ""));
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        List<String> dbs = clusterService.getDatabaseNames();
        String selectedDb = params.getOrDefault("db", dbs.isEmpty() ? "" : dbs.get(0));

        List<EngineOverview> engines = clusterService.getEngines(selectedDb);

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Motores de Almacenamiento Multimodelo</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>8 motores integrados sobre la misma estructura de memoria y SSTables compartidas</p>")
          .append("  </div>")
          .append("</div>");

        // Filter Bar (Database)
        sb.append("<div class='explorer-card' style='padding:14px 20px; margin-bottom:18px;'>")
          .append("  <form method='GET' action='/engines' style='display:flex; gap:14px; align-items:center;'>")
          .append("    <span style='font-size:12px; color:#94a3b8; font-weight:700;'>BASE DE DATOS:</span>")
          .append("    <select name='db' class='cyber-input' style='width:auto;' onchange='this.form.submit()'>");
        for (String d : dbs) {
            String sel = d.equalsIgnoreCase(selectedDb) ? "selected" : "";
            sb.append("<option value='").append(d).append("' ").append(sel).append(">").append(d).append("</option>");
        }
        sb.append("    </select>")
          .append("  </form>")
          .append("</div>");

        // Two Columns: Engines Table & Create Bucket Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-cogs' style='color:#00d4ff; margin-right:8px;'></i> Buckets Activos en ").append(selectedDb).append("</h3>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Motor de Almacenamiento</th><th>Nombre del Bucket</th><th>Registros</th><th>Estado</th></tr></thead><tbody>");

        if (engines.isEmpty()) {
            sb.append("<tr><td colspan='4' style='text-align:center; color:#64748b; padding:24px;'>No hay motores instanciados en esta base de datos.</td></tr>");
        } else {
            for (EngineOverview e : engines) {
                sb.append("<tr>")
                  .append("<td><b style='color:#38bdf8;'>").append(e.engineType()).append("</b></td>")
                  .append("<td><code>").append(e.bucketName()).append("</code></td>")
                  .append("<td>").append(String.format("%,d", e.recordCount())).append("</td>")
                  .append("<td><span class='cyber-badge-online'>").append(e.status()).append("</span></td>")
                  .append("</tr>");
            }
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Create Bucket Form
        sb.append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-folder-plus' style='color:#22c55e; margin-right:8px;'></i> Instanciar Bucket / Motor</h3>")
          .append("  <form method='POST' action='/engines' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <input type='hidden' name='db' value='").append(selectedDb).append("'/>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Tipo de Motor Especializado</label>")
          .append("      <select name='engineType' class='cyber-input'>")
          .append("        <option value='DOCUMENT'>Document (JSON / BSON)</option>")
          .append("        <option value='KEY-VALUE'>Key-Value In-Memory</option>")
          .append("        <option value='VECTOR'>Vector (AI / Embeddings HNSW)</option>")
          .append("        <option value='GRAPH'>Graph (Property Graph)</option>")
          .append("        <option value='TIMESERIES'>TimeSeries (Métricas & IoT)</option>")
          .append("        <option value='GEOSPATIAL'>Geospatial (GeoJSON R-Tree)</option>")
          .append("        <option value='COLUMNAR'>Columnar (Analítica OLAP)</option>")
          .append("        <option value='RECORDS'>Java Records Native</option>")
          .append("      </select>")
          .append("    </div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Nombre del Bucket / Colección</label><input type='text' name='bucketName' required class='cyber-input' placeholder='ej. sensor_readings'/></div>")
          .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-bolt'></i> Activar Motor en Clúster</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
