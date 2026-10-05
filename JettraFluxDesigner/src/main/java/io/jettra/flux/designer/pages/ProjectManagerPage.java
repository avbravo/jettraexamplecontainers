package io.jettra.flux.designer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.designer.model.MavenDependency;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.service.MavenProjectService;
import io.jettra.flux.widgets.RawHtml;

import java.io.IOException;
import java.util.Map;

@Page(path = "/projects")
public class ProjectManagerPage extends TemplatePage {

    private final MavenProjectService mavenService = MavenProjectService.getInstance();

    @Override
    protected String getTitle() {
        return "JettraFlux Designer • Gestor de Proyecto Maven";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String form = params.get("form");
        if ("openProject".equalsIgnoreCase(form)) {
            String path = params.get("projectPath");
            if (path != null && !path.isBlank()) {
                MavenProjectInfo info = mavenService.inspectProject(path.trim());
                if (info != null) {
                    sessionState.setCurrentProject(info);
                }
            }
        } else if ("addDep".equalsIgnoreCase(form)) {
            MavenProjectInfo info = sessionState.getCurrentProject();
            if (info != null) {
                String g = params.get("groupId");
                String a = params.get("artifactId");
                String v = params.get("version");
                String s = params.get("scope");
                if (g != null && a != null) {
                    mavenService.addDependency(info.getProjectPath(), new MavenDependency(g.trim(), a.trim(), v != null ? v.trim() : "", s != null ? s.trim() : "compile"));
                    sessionState.setCurrentProject(mavenService.inspectProject(info.getProjectPath()));
                }
            }
        }
        redirect(exchange, "/projects");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        String delArtifact = params.get("delArtifact");
        MavenProjectInfo current = sessionState.getCurrentProject();

        if ("removeDep".equalsIgnoreCase(action) && delArtifact != null && current != null) {
            mavenService.removeDependency(current.getProjectPath(), delArtifact.trim());
            sessionState.setCurrentProject(mavenService.inspectProject(current.getProjectPath()));
            current = sessionState.getCurrentProject();
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>Administrador de Proyecto Maven & Dependencias</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Abra cualquier proyecto Java Maven, gestione dependencias en pom.xml y enlace páginas JettraFlux</p>")
          .append("  </div>")
          .append("</div>");

        // Project Path Selector Card
        sb.append("<div class='designer-card'>")
          .append("  <form method='POST' action='/projects' style='display:flex; gap:12px; align-items:flex-end; flex-wrap:wrap;'>")
          .append("    <input type='hidden' name='form' value='openProject'/>")
          .append("    <div style='flex:1; min-width:320px;'>")
          .append("      <label style='font-size:12px; font-weight:700; color:#cbd5e1; display:block; margin-bottom:6px;'>Ruta Absoluta del Directorio del Proyecto Maven (con pom.xml)</label>")
          .append("      <input type='text' name='projectPath' value='").append(current != null ? current.getProjectPath() : "").append("' required class='cyber-input' placeholder='/home/usuario/proyectos/MiAppJettra'/>")
          .append("    </div>")
          .append("    <button type='submit' class='btn-cyber'><i class='fas fa-folder-open'></i> Cargar Proyecto</button>")
          .append("  </form>")
          .append("</div>");

        if (current != null) {
            // Project Metadata
            sb.append("<div style='display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:16px; margin-bottom:20px;'>")
              .append("<div class='designer-card' style='margin-bottom:0;'>")
              .append("  <div style='font-size:11px; color:#94a3b8;'>ARTIFACT ID</div>")
              .append("  <div style='font-size:18px; font-weight:800; color:#00d4ff;'>").append(current.getArtifactId()).append("</div>")
              .append("</div>")
              .append("<div class='designer-card' style='margin-bottom:0;'>")
              .append("  <div style='font-size:11px; color:#94a3b8;'>GROUP ID</div>")
              .append("  <div style='font-size:18px; font-weight:800; color:#38bdf8;'>").append(current.getGroupId()).append("</div>")
              .append("</div>")
              .append("<div class='designer-card' style='margin-bottom:0;'>")
              .append("  <div style='font-size:11px; color:#94a3b8;'>VERSIÓN</div>")
              .append("  <div style='font-size:18px; font-weight:800; color:#22c55e;'>").append(current.getVersion()).append("</div>")
              .append("</div>")
              .append("<div class='designer-card' style='margin-bottom:0;'>")
              .append("  <div style='font-size:11px; color:#94a3b8;'>PÁGINAS DETECTADAS</div>")
              .append("  <div style='font-size:18px; font-weight:800; color:#eab308;'>").append(current.getExistingPages().size()).append(" clases</div>")
              .append("</div>")
              .append("</div>");

            // Two Columns: Dependencies Table & Add Dependency Form
            sb.append("<div style='display:grid; grid-template-columns: 2fr 1fr; gap:20px;'>")
              .append("<div class='designer-card'>")
              .append("  <h3 style='margin:0 0 16px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-cubes' style='color:#00d4ff; margin-right:8px;'></i> Dependencias en pom.xml (").append(current.getDependencies().size()).append(")</h3>")
              .append("  <table class='designer-table'>")
              .append("  <thead><tr><th>GroupId</th><th>ArtifactId</th><th>Versión</th><th>Scope</th><th>Acción</th></tr></thead><tbody>");

            for (MavenDependency dep : current.getDependencies()) {
                sb.append("<tr>")
                  .append("<td><code>").append(dep.groupId()).append("</code></td>")
                  .append("<td><b style='color:#fff;'>").append(dep.artifactId()).append("</b></td>")
                  .append("<td>").append(dep.version().isEmpty() ? "<span style='color:#64748b;'>heredada</span>" : dep.version()).append("</td>")
                  .append("<td><span class='cyber-badge-info'>").append(dep.scope()).append("</span></td>")
                  .append("<td><a href='?action=removeDep&delArtifact=").append(dep.artifactId()).append("' class='btn-danger' style='padding:3px 6px; font-size:10px;' onclick=\"return confirm('¿Remover dependencia ").append(dep.artifactId()).append(" del pom.xml?');\"><i class='fas fa-trash'></i></a></td>")
                  .append("</tr>");
            }

            sb.append("  </tbody></table>")
              .append("</div>");

            // Add Dependency Form
            sb.append("<div class='designer-card'>")
              .append("  <h3 style='margin:0 0 14px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-plus' style='color:#22c55e; margin-right:8px;'></i> Agregar Dependencia</h3>")
              .append("  <form method='POST' action='/projects' style='display:flex; flex-direction:column; gap:12px;'>")
              .append("    <input type='hidden' name='form' value='addDep'/>")
              .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>GroupId</label><input type='text' name='groupId' value='io.jettra' required class='cyber-input'/></div>")
              .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>ArtifactId</label><input type='text' name='artifactId' required class='cyber-input' placeholder='ej. JettraReport o JettraStoreDriver'/></div>")
              .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Versión</label><input type='text' name='version' value='1.0.0-SNAPSHOT' class='cyber-input'/></div>")
              .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Scope</label>")
              .append("      <select name='scope' class='cyber-input'>")
              .append("        <option value='compile'>compile</option>")
              .append("        <option value='provided'>provided</option>")
              .append("        <option value='runtime'>runtime</option>")
              .append("        <option value='test'>test</option>")
              .append("      </select>")
              .append("    </div>")
              .append("    <button type='submit' class='btn-cyber' style='justify-content:center;'><i class='fas fa-save'></i> Insertar en pom.xml</button>")
              .append("  </form>")
              .append("</div>")
              .append("</div>");
        } else {
            sb.append("<div class='designer-card' style='text-align:center; padding:40px;'>")
              .append("  <p style='color:#94a3b8; font-size:15px;'>Ingrese la ruta de un proyecto Maven válido arriba para comenzar a gestionar sus dependencias y páginas.</p>")
              .append("</div>");
        }

        return RawHtml.of(sb.toString());
    }
}
