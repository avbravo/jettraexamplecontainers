package io.jettra.flux.designer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.service.CodeGeneratorService;
import io.jettra.flux.widgets.RawHtml;

import java.io.IOException;
import java.util.Map;

@Page(path = "/codepreview")
public class CodePreviewPage extends TemplatePage {

    private final CodeGeneratorService codeGen = CodeGeneratorService.getInstance();

    @Override
    protected String getTitle() {
        return "JettraFlux Designer • Generador de Código";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String action = params.get("action");
        String pkg = params.get("packageName");
        String cls = params.get("className");
        String route = params.get("routePath");
        String role = params.get("role");

        MavenProjectInfo proj = sessionState.getCurrentProject();
        if ("export".equalsIgnoreCase(action) && proj != null && cls != null && !cls.isBlank()) {
            String javaCode = codeGen.generateJavaClass(pkg, cls, route, role, sessionState.getRootCanvas());
            boolean ok = codeGen.saveClassToProject(proj.getProjectPath(), pkg, cls, javaCode);
            redirect(exchange, "/codepreview?exported=" + ok + "&cls=" + cls);
            return true;
        }

        redirect(exchange, "/codepreview");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String pkg = params.getOrDefault("pkg", "com.flux.example.pages");
        String cls = params.getOrDefault("cls", "MiPanelPage");
        String route = params.getOrDefault("route", "/" + cls.toLowerCase());
        String role = params.getOrDefault("role", "ADMIN");

        String exported = params.get("exported");
        MavenProjectInfo proj = sessionState.getCurrentProject();

        String generatedJava = codeGen.generateJavaClass(pkg, cls, route, role, sessionState.getRootCanvas());

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'><i class='fas fa-code' style='color:#10b981; margin-right:8px;'></i> Generador de Código Java Funcional</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Transforma el árbol de componentes visuales en código fuente Java 25 limpio y compilable</p>")
          .append("  </div>")
          .append("  <div style='display:flex; gap:10px;'>")
          .append("    <a href='/designer' class='btn-cyber'><i class='fas fa-arrow-left'></i> Volver al Diseñador</a>")
          .append("  </div>")
          .append("</div>");

        if ("true".equalsIgnoreCase(exported)) {
            sb.append("<div style='background:rgba(34, 197, 94, 0.15); border:1px solid #22c55e; color:#bbf7d0; padding:12px 16px; border-radius:8px; margin-bottom:20px;'>")
              .append("  ✅ Clase <b>").append(cls).append(".java</b> generada y guardada exitosamente en el proyecto Maven <code>").append(proj != null ? proj.getArtifactId() : "").append("</code>.")
              .append("</div>");
        } else if ("false".equalsIgnoreCase(exported)) {
            sb.append("<div style='background:rgba(239, 68, 68, 0.15); border:1px solid #ef4444; color:#fca5a5; padding:12px 16px; border-radius:8px; margin-bottom:20px;'>")
              .append("  ❌ Error al escribir el archivo en el proyecto. Verifique que exista la carpeta src/main/java.")
              .append("</div>");
        }

        // Two Columns: Configuration & Java Source Code Preview
        sb.append("<div style='display:grid; grid-template-columns: 320px 1fr; gap:20px;'>")
          .append("<div class='designer-card'>")
          .append("  <h3 style='margin:0 0 16px; font-size:15px; font-weight:700; color:#fff;'><i class='fas fa-sliders-h' style='color:#00d4ff; margin-right:8px;'></i> Metadatos de la Clase</h3>")
          .append("  <form method='POST' action='/codepreview' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("    <input type='hidden' name='action' value='export'/>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Paquete Java (Package)</label><input type='text' name='packageName' value='").append(pkg).append("' required class='cyber-input'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Nombre de la Clase (Class)</label><input type='text' name='className' value='").append(cls).append("' required class='cyber-input'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Ruta URL (@Page path)</label><input type='text' name='routePath' value='").append(route).append("' required class='cyber-input'/></div>")
          .append("    <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Rol de Seguridad Requerido</label>")
          .append("      <select name='role' class='cyber-input'>")
          .append("        <option value='ADMIN' ").append("ADMIN".equals(role) ? "selected" : "").append(">ADMIN</option>")
          .append("        <option value='DEVELOPER' ").append("DEVELOPER".equals(role) ? "selected" : "").append(">DEVELOPER</option>")
          .append("        <option value='USER' ").append("USER".equals(role) ? "selected" : "").append(">USER</option>")
          .append("      </select>")
          .append("    </div>");

        if (proj != null) {
            sb.append("    <div style='background:#090d16; padding:10px; border-radius:8px; border:1px solid #1e293b; font-size:12px; color:#94a3b8;'>")
              .append("      <span>Destino: </span><br/><code style='color:#00d4ff; word-break:break-all;'>").append(proj.getProjectPath()).append("/src/main/java/").append(pkg.replace('.', '/')).append("/").append(cls).append(".java</code>")
              .append("    </div>")
              .append("    <button type='submit' class='btn-cyber' style='background:#10b981; justify-content:center;'><i class='fas fa-save'></i> Guardar en Proyecto Maven</button>");
        } else {
            sb.append("    <p style='color:#f59e0b; font-size:12px;'>⚠️ Primero seleccione un proyecto Maven en la pestaña Gestor de Proyecto para exportar directamente.</p>");
        }

        sb.append("  </form>")
          .append("</div>");

        // Code Preview Container
        sb.append("<div class='designer-card' style='background:#040711; overflow-x:auto;'>")
          .append("  <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; border-bottom:1px solid #1e293b; padding-bottom:8px;'>")
          .append("    <span style='font-size:12px; font-weight:700; color:#cbd5e1;'><i class='fas fa-file-code' style='color:#00d4ff; margin-right:6px;'></i> ").append(cls).append(".java</span>")
          .append("    <button type='button' class='btn-cyber' style='padding:4px 8px; font-size:11px;' onclick=\"navigator.clipboard.writeText(document.getElementById('codeSource').innerText); alert('¡Código copiado al portapapeles!');\"><i class='fas fa-copy'></i> Copiar</button>")
          .append("  </div>")
          .append("  <pre id='codeSource' style='margin:0; font-family:\"JetBrains Mono\", Consolas, monospace; font-size:13px; color:#e2e8f0; line-height:1.6;'>")
          .append(generatedJava.replace("<", "&lt;").replace(">", "&gt;"))
          .append("</pre>")
          .append("</div>")
          .append("</div>");

        return RawHtml.of(sb.toString());
    }
}
