package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.ConnectionProfile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/connections")
public class ConnectionsPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Conexiones de Clúster";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String id = params.get("id");
        String name = params.get("name");
        String hostPort = params.get("hostPort");
        String username = params.get("username");
        String password = params.get("password");

        if (id != null && !id.isBlank() && hostPort != null && !hostPort.isBlank()) {
            connMgr.saveOrUpdate(new ConnectionProfile(id.trim(), name != null ? name.trim() : id, hostPort.trim(), username != null ? username.trim() : "admin", password != null ? password.trim() : "", false));
        }
        redirect(exchange, "/connections?msg=saved");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        String targetId = params.get("id");
        String testFeedback = null;

        if ("activate".equalsIgnoreCase(action) && targetId != null) {
            connMgr.activate(targetId);
        } else if ("delete".equalsIgnoreCase(action) && targetId != null) {
            connMgr.delete(targetId);
        } else if ("test".equalsIgnoreCase(action) && targetId != null) {
            var prof = connMgr.findById(targetId);
            if (prof.isPresent()) {
                var res = connMgr.testConnection(prof.get());
                testFeedback = (res.success() ? "✅ " : "❌ ") + res.message() + " (" + res.latencyMs() + " ms)";
            }
        }

        List<ConnectionProfile> profiles = connMgr.getProfiles();
        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Administrador de Conexiones JettraStore</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Configure perfiles de acceso a servidores JettraStore y conmute de nodo en caliente</p>")
          .append("  </div>")
          .append("</div>");

        if (testFeedback != null) {
            sb.append("<div style='background:rgba(56, 189, 248, 0.15); border:1px solid #38bdf8; color:#e0f2fe; padding:12px 16px; border-radius:8px; margin-bottom:20px;'>")
              .append(testFeedback)
              .append("</div>");
        }

        // Two Columns: Table & Add Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-network-wired' style='color:#00d4ff; margin-right:8px;'></i> Perfiles Registrados</h3>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>ID</th><th>Nombre</th><th>Host:Puerto</th><th>Usuario</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>");

        for (ConnectionProfile p : profiles) {
            String statusBadge = p.isActive()
                ? "<span class='cyber-badge-online'>CONECTADO (ACTIVO)</span>"
                : "<span class='cyber-badge-info'>STANDBY</span>";

            sb.append("<tr>")
              .append("<td><code>").append(p.getId()).append("</code></td>")
              .append("<td><b>").append(p.getName()).append("</b></td>")
              .append("<td>").append(p.getHostPort()).append("</td>")
              .append("<td>").append(p.getUsername()).append("</td>")
              .append("<td>").append(statusBadge).append("</td>")
              .append("<td style='display:flex; gap:6px;'>")
              .append("  <a href='?action=test&id=").append(p.getId()).append("' class='btn-cyber' style='padding:4px 8px; font-size:11px;'><i class='fas fa-stethoscope'></i> Probar</a>");

            if (!p.isActive()) {
                sb.append("  <a href='?action=activate&id=").append(p.getId()).append("' class='btn-success' style='padding:4px 8px; font-size:11px;'><i class='fas fa-check'></i> Conectar</a>");
                sb.append("  <a href='?action=delete&id=").append(p.getId()).append("' class='btn-danger' style='padding:4px 8px; font-size:11px;'><i class='fas fa-trash'></i></a>");
            }

            sb.append("</td></tr>");
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Add Form Card
        sb.append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-plus-circle' style='color:#22c55e; margin-right:8px;'></i> Nuevo Perfil</h3>")
          .append("  <form method='POST' action='/connections' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Identificador Único</label><input type='text' name='id' required class='cyber-input' placeholder='conn_remote_01'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Nombre Descriptivo</label><input type='text' name='name' required class='cyber-input' placeholder='Clúster Central Europa'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Host:Puerto (IP o Dominio)</label><input type='text' name='hostPort' required class='cyber-input' placeholder='10.0.1.50:9010'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Usuario Administrador</label><input type='text' name='username' value='admin' class='cyber-input'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Contraseña</label><input type='password' name='password' class='cyber-input' placeholder='••••••••'/></div>")
          .append("    <button type='submit' class='btn-cyber' style='margin-top:6px; justify-content:center;'><i class='fas fa-save'></i> Guardar Perfil</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
