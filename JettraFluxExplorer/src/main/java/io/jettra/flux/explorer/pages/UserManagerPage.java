package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.JettraUserAccount;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/users")
public class UserManagerPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Usuarios & Seguridad";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String username = params.get("username");
        String pass = params.get("password");
        String desc = params.get("description");
        String role = params.get("role");

        if (username != null && pass != null && role != null) {
            JettraUserAccount account = new JettraUserAccount(username.trim(), pass.trim(), desc != null ? desc.trim() : "", role.trim());
            for (String db : clusterService.getDatabaseNames()) {
                account.setDbPermission(db, "ADMIN".equalsIgnoreCase(role) ? "ADMIN" : "READ_WRITE");
            }
            clusterService.saveUser(account);
        }
        redirect(exchange, "/users");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        String delUser = params.get("del");
        if ("delete".equalsIgnoreCase(action) && delUser != null) {
            clusterService.deleteUser(delUser);
        }

        List<JettraUserAccount> users = clusterService.getUsers();
        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Control de Acceso & Cuentas (ACL)</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Gestión de credenciales criptográficas y permisos granulares por base de datos</p>")
          .append("  </div>")
          .append("</div>");

        // Two Columns: Table & Create User Form
        sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-user-shield' style='color:#00d4ff; margin-right:8px;'></i> Usuarios JettraStore Registrados</h3>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Usuario</th><th>Descripción</th><th>Rol Global</th><th>Permisos Bases de Datos</th><th>Acción</th></tr></thead><tbody>");

        for (JettraUserAccount u : users) {
            String roleBadge = "ADMIN".equalsIgnoreCase(u.getGlobalRole())
                ? "<span class='cyber-badge-online'>SUPERADMIN</span>"
                : "<span class='cyber-badge-info'>" + u.getGlobalRole() + "</span>";

            sb.append("<tr>")
              .append("<td><b style='color:#fff;'>").append(u.getUsername()).append("</b></td>")
              .append("<td>").append(u.getFullName()).append("</td>")
              .append("<td>").append(roleBadge).append("</td>")
              .append("<td><span style='font-size:11px; color:#94a3b8;'>").append(u.getDbPermissions().keySet()).append("</span></td>")
              .append("<td>");

            if (!"admin".equalsIgnoreCase(u.getUsername())) {
                sb.append("  <a href='?action=delete&del=").append(u.getUsername()).append("' class='btn-danger' style='padding:4px 8px; font-size:11px;' onclick=\"return confirm('¿Revocar cuenta de ").append(u.getUsername()).append("?');\"><i class='fas fa-trash'></i></a>");
            } else {
                sb.append("  <span style='color:#64748b; font-size:11px;'>PROTEGIDO</span>");
            }
            sb.append("</td></tr>");
        }

        sb.append("  </tbody></table>")
          .append("</div>");

        // Add User Form
        sb.append("<div class='explorer-card'>")
          .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-user-plus' style='color:#22c55e; margin-right:8px;'></i> Crear Usuario de Clúster</h3>")
          .append("  <form method='POST' action='/users' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Usuario</label><input type='text' name='username' required class='cyber-input' placeholder='ej. secops_bot'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Contraseña</label><input type='password' name='password' required class='cyber-input' placeholder='••••••••'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Descripción / Cargo</label><input type='text' name='description' class='cyber-input' placeholder='Bot de Monitoreo CI/CD'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Rol Global</label>")
          .append("      <select name='role' class='cyber-input'>")
          .append("        <option value='OPERATOR'>OPERATOR (Lectura / Escritura)</option>")
          .append("        <option value='ANALYST'>ANALYST (Solo Lectura)</option>")
          .append("        <option value='ADMIN'>ADMIN (Control Total)</option>")
          .append("      </select>")
          .append("    </div>")
          .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-key'></i> Generar Credenciales</button>")
          .append("  </form>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
