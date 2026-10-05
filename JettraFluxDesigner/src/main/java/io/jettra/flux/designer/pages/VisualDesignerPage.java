package io.jettra.flux.designer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.designer.model.CanvasWidget;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.model.PaletteItem;
import io.jettra.flux.designer.model.ProjectFileNode;
import io.jettra.flux.designer.service.CodeGeneratorService;
import io.jettra.flux.designer.service.MavenProjectService;
import io.jettra.flux.widgets.RawHtml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Page(path = "/designer")
public class VisualDesignerPage extends TemplatePage {

    private final CodeGeneratorService codeGen = CodeGeneratorService.getInstance();
    private final MavenProjectService mavenService = MavenProjectService.getInstance();

    @Override
    protected String getTitle() {
        return "JettraFlux Designer • Estudio Visual & Drag and Drop";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String action = params.get("action");
        String widgetId = params.get("widgetId");

        if ("updateWidget".equalsIgnoreCase(action) && widgetId != null) {
            CanvasWidget w = sessionState.getRootCanvas().findById(widgetId);
            if (w != null) {
                if (params.containsKey("label")) w.setLabel(params.get("label"));
                if (params.containsKey("text")) w.setText(params.get("text"));
                if (params.containsKey("styles")) w.setStyles(params.get("styles"));
                if (params.containsKey("cssClasses")) w.setCssClasses(params.get("cssClasses"));
                if (params.containsKey("columns")) {
                    try { w.setColumns(Integer.parseInt(params.get("columns"))); } catch (Exception ignored) {}
                }
            }
        } else if ("saveToFile".equalsIgnoreCase(action)) {
            String filePath = sessionState.getCurrentOpenedFilePath();
            if (filePath != null && !filePath.isBlank()) {
                String javaCode = codeGen.generateJavaClass(
                    sessionState.getCurrentOpenedPackage(),
                    sessionState.getCurrentOpenedClassName(),
                    sessionState.getCurrentOpenedRoute(),
                    sessionState.getCurrentOpenedRole(),
                    sessionState.getRootCanvas()
                );
                codeGen.saveDirectlyToFile(filePath, javaCode);
                redirect(exchange, "/designer?saved=true");
                return true;
            }
        } else if ("switchFolder".equalsIgnoreCase(action)) {
            String newPath = params.get("newPath");
            if (newPath != null && !newPath.isBlank()) {
                MavenProjectInfo info = mavenService.inspectProject(newPath.trim());
                if (info != null) {
                    sessionState.setCurrentProject(info);
                    // Try to auto-open first page found
                    if (!info.getExistingPages().isEmpty()) {
                        sessionState.openJavaPageByPath(info.getExistingPages().get(0));
                    }
                }
            }
        }

        redirect(exchange, "/designer");
        return true;
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String openPage = params.get("openPage");
        if (openPage != null && !openPage.isBlank()) {
            sessionState.openJavaPageByPath(openPage);
        }

        String action = params.get("action");
        String addType = params.get("add");
        String targetParent = params.get("parent");
        String delId = params.get("del");
        String selId = params.get("sel");
        String activeTab = params.getOrDefault("leftTab", "tree"); // 'tree' or 'palette'

        if (addType != null && !addType.isBlank()) {
            sessionState.addWidgetToCanvas(addType, targetParent);
        } else if ("delete".equalsIgnoreCase(action) && delId != null) {
            sessionState.removeWidgetFromCanvas(delId);
        }

        if (selId != null && !selId.isBlank()) {
            sessionState.setSelectedWidgetId(selId);
        }

        String activeSelId = sessionState.getSelectedWidgetId();
        CanvasWidget selectedWidget = (activeSelId != null) ? sessionState.getRootCanvas().findById(activeSelId) : null;
        MavenProjectInfo currentProj = sessionState.getCurrentProject();
        List<PaletteItem> palette = sessionState.getPalette();

        String liveGeneratedCode = codeGen.generateJavaClass(
            sessionState.getCurrentOpenedPackage(),
            sessionState.getCurrentOpenedClassName(),
            sessionState.getCurrentOpenedRoute(),
            sessionState.getCurrentOpenedRole(),
            sessionState.getRootCanvas()
        );

        StringBuilder sb = new StringBuilder();

        // 1. Top Action Toolbar
        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div style='display:flex; align-items:center; gap:12px;'>")
          .append("    <h2 style='margin:0; font-size:20px; font-weight:800; color:#fff;'><i class='fas fa-drafting-compass' style='color:#00d4ff; margin-right:8px;'></i> JettraFlux Designer</h2>")
          .append("    <span class='cyber-badge-online' style='font-size:12px;'><i class='fas fa-file-code'></i> ")
          .append(sessionState.getCurrentOpenedClassName()).append(".java</span>")
          .append("  </div>")
          .append("  <div style='display:flex; gap:10px; align-items:center;'>")
          .append("    <button onclick='document.getElementById(\"folderModal\").style.display=\"flex\"' class='btn-cyber' style='background:#334155;'><i class='fas fa-folder-open'></i> Buscar Carpeta en Disco</button>");

        if (sessionState.getCurrentOpenedFilePath() != null) {
            sb.append("    <form method='POST' action='/designer' style='margin:0;'>")
              .append("      <input type='hidden' name='action' value='saveToFile'/>")
              .append("      <button type='submit' class='btn-cyber' style='background:#10b981;'><i class='fas fa-save'></i> Guardar en ").append(sessionState.getCurrentOpenedClassName()).append(".java</button>")
              .append("    </form>");
        }

        sb.append("    <a href='/codepreview' class='btn-cyber' style='background:#6366f1;'><i class='fas fa-external-link-alt'></i> Exportar Clase</a>")
          .append("  </div>")
          .append("</div>");

        if ("true".equalsIgnoreCase(params.get("saved"))) {
            sb.append("<div style='background:rgba(34, 197, 94, 0.15); border:1px solid #22c55e; color:#bbf7d0; padding:10px 16px; border-radius:8px; margin-bottom:16px; font-size:13px;'>")
              .append("  ✅ Archivo <b>").append(sessionState.getCurrentOpenedFilePath()).append("</b> actualizado con el código generado.")
              .append("</div>");
        }

        // 2. Main 3-Column Studio Layout
        sb.append("<div style='display:grid; grid-template-columns: 280px 1fr 340px; gap:18px;'>");

        // ================= COLUMN 1: LEFT SIDEBAR (Tabs: Tree Explorer / Palette) =================
        sb.append("<div class='designer-card' style='padding:14px; display:flex; flex-direction:column; max-height:85vh; overflow:hidden;'>")
          // Tab Headers
          .append("<div style='display:flex; gap:6px; border-bottom:1px solid #1e293b; padding-bottom:10px; margin-bottom:12px;'>")
          .append("  <a href='?leftTab=tree' class='btn-cyber' style='flex:1; justify-content:center; padding:6px; font-size:11px; ").append("tree".equals(activeTab) ? "background:#0284c7;" : "background:#1e293b; color:#94a3b8;").append("'><i class='fas fa-sitemap'></i> Explorador</a>")
          .append("  <a href='?leftTab=palette' class='btn-cyber' style='flex:1; justify-content:center; padding:6px; font-size:11px; ").append("palette".equals(activeTab) ? "background:#0284c7;" : "background:#1e293b; color:#94a3b8;").append("'><i class='fas fa-cubes'></i> Paleta Flux</a>")
          .append("</div>");

        // Tab Content: Tree or Palette
        if ("tree".equals(activeTab)) {
            sb.append("<div style='overflow-y:auto; flex:1; padding-right:4px;'>")
              .append("  <div style='font-size:11px; font-weight:700; color:#94a3b8; text-transform:uppercase; margin-bottom:8px;'><i class='fas fa-folder'></i> ").append(currentProj != null ? currentProj.getArtifactId() : "Archivos").append("</div>");

            if (currentProj != null && currentProj.getRootNode() != null) {
                renderFileTree(sb, currentProj.getRootNode());
            } else {
                sb.append("<p style='color:#64748b; font-size:12px;'>No hay proyecto cargado. Use el botón 'Buscar Carpeta' arriba.</p>");
            }
            sb.append("</div>");
        } else {
            sb.append("<div style='overflow-y:auto; flex:1; padding-right:4px;'>")
              .append("  <div style='font-size:11px; font-weight:700; color:#00d4ff; text-transform:uppercase; margin-bottom:8px;'>Arrastre al lienzo o haga clic:</div>");

            String currentCat = "";
            for (PaletteItem p : palette) {
                if (!p.category().equalsIgnoreCase(currentCat)) {
                    currentCat = p.category();
                    sb.append("<div style='font-size:10px; font-weight:800; color:#64748b; text-transform:uppercase; margin:10px 0 4px 2px;'>").append(currentCat).append("</div>");
                }
                sb.append("<div class='palette-item' draggable='true' ondragstart='event.dataTransfer.setData(\"text/plain\", \"").append(p.type()).append("\")'>")
                  .append("  <a href='?add=").append(p.type()).append("&parent=").append(activeSelId != null ? activeSelId : "").append("' style='text-decoration:none; color:inherit; display:flex; align-items:center; gap:8px; flex:1;'>")
                  .append("    <i class='").append(p.icon()).append("' style='color:#00d4ff; width:16px; text-align:center;'></i>")
                  .append("    <span style='font-size:12px;'>").append(p.displayName()).append("</span>")
                  .append("  </a>")
                  .append("  <i class='fas fa-grip-vertical' style='color:#475569; font-size:11px; cursor:grab;' title='Arrastrar y soltar al lienzo'></i>")
                  .append("</div>");
            }
            sb.append("</div>");
        }
        sb.append("</div>");

        // ================= COLUMN 2: CENTER CANVAS (HTML5 DRAG AND DROP) =================
        sb.append("<div class='designer-card' style='background:#040711; border:1px solid #1e293b; min-height:560px; display:flex; flex-direction:column; padding:18px;'>")
          .append("  <div style='display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid #1e293b; padding-bottom:12px; margin-bottom:16px;'>")
          .append("    <div style='display:flex; align-items:center; gap:8px;'>")
          .append("      <span style='display:inline-block; width:10px; height:10px; border-radius:50%; background:#22c55e; box-shadow:0 0 10px #22c55e;'></span>")
          .append("      <span style='font-size:13px; font-weight:700; color:#f8fafc;'>Lienzo Interactivo (Visual Canvas)</span>")
          .append("    </div>")
          .append("    <div style='font-size:11px; color:#64748b;'>Arrastre componentes aquí o dentro de cualquier contenedor</div>")
          .append("  </div>")

          .append("  <div id='canvasRootDrop' style='flex:1; overflow-y:auto;' ondragover='event.preventDefault(); this.style.borderColor=\"#00d4ff\";' ondragleave='this.style.borderColor=\"\";' ondrop='handleDrop(event, \"").append(sessionState.getRootCanvas().getId()).append("\")'>");

        renderInteractiveCanvas(sb, sessionState.getRootCanvas(), activeSelId);

        sb.append("  </div>")
          .append("</div>");

        // ================= COLUMN 3: RIGHT PANEL (INSPECTOR & LIVE CODE) =================
        sb.append("<div class='designer-card' style='display:flex; flex-direction:column; gap:16px;'>")
          // Section 1: Properties Inspector
          .append("<div>")
          .append("  <h3 style='margin:0 0 12px; font-size:13px; font-weight:800; color:#22c55e; text-transform:uppercase; letter-spacing:0.5px;'><i class='fas fa-sliders-h'></i> Propiedades</h3>");

        if (selectedWidget != null) {
            sb.append("<form method='POST' action='/designer' style='display:flex; flex-direction:column; gap:10px;'>")
              .append("  <input type='hidden' name='action' value='updateWidget'/>")
              .append("  <input type='hidden' name='widgetId' value='").append(selectedWidget.getId()).append("'/>")
              .append("  <div style='display:flex; justify-content:space-between; align-items:center;'>")
              .append("    <span class='cyber-badge-info'>").append(selectedWidget.getType()).append("</span>")
              .append("    <code style='font-size:11px; color:#64748b;'>").append(selectedWidget.getId()).append("</code>")
              .append("  </div>")
              .append("  <div><label style='font-size:11px; color:#94a3b8; font-weight:700;'>ETIQUETA</label><input type='text' name='label' value='").append(selectedWidget.getLabel() != null ? selectedWidget.getLabel().replace("'", "&#39;") : "").append("' class='cyber-input'/></div>")
              .append("  <div><label style='font-size:11px; color:#94a3b8; font-weight:700;'>TEXTO / VALOR</label><textarea name='text' rows='2' class='cyber-input' style='resize:vertical;'>").append(selectedWidget.getText() != null ? selectedWidget.getText() : "").append("</textarea></div>");

            if ("Grid".equalsIgnoreCase(selectedWidget.getType())) {
                sb.append("  <div><label style='font-size:11px; color:#94a3b8; font-weight:700;'>COLUMNAS</label><input type='number' name='columns' min='1' max='6' value='").append(selectedWidget.getColumns()).append("' class='cyber-input'/></div>");
            }

            sb.append("  <div><label style='font-size:11px; color:#94a3b8; font-weight:700;'>ESTILO CSS</label><input type='text' name='styles' value='").append(selectedWidget.getStyles() != null ? selectedWidget.getStyles() : "").append("' class='cyber-input' placeholder='padding:12px; color:#fff;'/></div>")
              .append("  <div><label style='font-size:11px; color:#94a3b8; font-weight:700;'>CLASE CSS</label><input type='text' name='cssClasses' value='").append(selectedWidget.getCssClasses() != null ? selectedWidget.getCssClasses() : "").append("' class='cyber-input' placeholder='card-shadow'/></div>")
              .append("  <button type='submit' class='btn-cyber' style='justify-content:center; padding:7px;'><i class='fas fa-check'></i> Aplicar</button>")
              .append("</form>");
        } else {
            sb.append("<p style='color:#64748b; font-size:12px;'>Seleccione un componente en el lienzo para inspeccionar sus atributos.</p>");
        }
        sb.append("</div>");

        // Section 2: Live Code Preview
        sb.append("<div style='border-top:1px solid #1e293b; padding-top:14px; flex:1; display:flex; flex-direction:column;'>")
          .append("  <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;'>")
          .append("    <span style='font-size:12px; font-weight:800; color:#38bdf8;'><i class='fas fa-code'></i> Código Java en Vivo</span>")
          .append("    <button type='button' class='btn-cyber' style='padding:2px 8px; font-size:10px;' onclick='navigator.clipboard.writeText(document.getElementById(\"liveCodePre\").innerText); alert(\"Código copiado\");'><i class='fas fa-copy'></i></button>")
          .append("  </div>")
          .append("  <pre id='liveCodePre' style='margin:0; flex:1; background:#040711; border:1px solid #1e293b; border-radius:8px; padding:10px; font-family:\"JetBrains Mono\", monospace; font-size:11px; color:#cbd5e1; overflow-y:auto; max-height:280px; white-space:pre-wrap;'>")
          .append(liveGeneratedCode.replace("<", "&lt;").replace(">", "&gt;"))
          .append("  </pre>")
          .append("</div>");

        sb.append("</div>"); // End Column 3

        sb.append("</div>"); // End Main Grid

        // Modal for Directory Switcher (Folder in disk)
        sb.append("<div id='folderModal' style='display:none; position:fixed; top:0; left:0; width:100vw; height:100vh; background:rgba(0,0,0,0.8); backdrop-filter:blur(8px); z-index:9999; justify-content:center; align-items:center;'>")
          .append("  <div class='designer-card' style='width:520px; max-width:90%; background:#0b0f19; border:1px solid #00d4ff;'>")
          .append("    <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;'>")
          .append("      <h3 style='margin:0; font-size:16px; color:#fff;'><i class='fas fa-folder-open' style='color:#00d4ff;'></i> Seleccionar Carpeta de Proyecto en Disco</h3>")
          .append("      <button type='button' onclick='document.getElementById(\"folderModal\").style.display=\"none\"' style='background:none; border:none; color:#94a3b8; font-size:18px; cursor:pointer;'>✕</button>")
          .append("    </div>")
          .append("    <form method='POST' action='/designer' style='display:flex; flex-direction:column; gap:12px;'>")
          .append("      <input type='hidden' name='action' value='switchFolder'/>")
          .append("      <div><label style='font-size:12px; color:#cbd5e1; font-weight:600;'>Ruta de Carpeta en Disco:</label>")
          .append("        <input type='text' id='folderPathInput' name='newPath' value='").append(currentProj != null ? currentProj.getProjectPath() : "").append("' required class='cyber-input'/>")
          .append("      </div>")
          .append("      <div style='font-size:11px; color:#94a3b8; margin-top:4px;'>Proyectos Rápidos en el Espacio de Trabajo:</div>")
          .append("      <div style='display:flex; flex-direction:column; gap:6px;'>")
          .append("        <button type='button' class='btn-cyber' style='background:#1e293b; justify-content:flex-start;' onclick='document.getElementById(\"folderPathInput\").value=\"/home/avbravo/NetBeansProjects/jettrastack_local/jettraexamples/JettraFluxExample\"'><i class='fas fa-cube' style='color:#00d4ff;'></i> JettraFluxExample (Ejemplos & Componentes)</button>")
          .append("        <button type='button' class='btn-cyber' style='background:#1e293b; justify-content:flex-start;' onclick='document.getElementById(\"folderPathInput\").value=\"/home/avbravo/NetBeansProjects/jettrastack_local/jettraexamples/JettraFluxExplorer\"'><i class='fas fa-server' style='color:#22c55e;'></i> JettraFluxExplorer (Administrador de Clúster)</button>")
          .append("        <button type='button' class='btn-cyber' style='background:#1e293b; justify-content:flex-start;' onclick='document.getElementById(\"folderPathInput\").value=\"/home/avbravo/NetBeansProjects/jettrastack_local/jettraexamples/JettraStudioExample\"'><i class='fas fa-paint-brush' style='color:#eab308;'></i> JettraStudioExample</button>")
          .append("      </div>")
          .append("      <div style='display:flex; justify-content:flex-end; gap:10px; margin-top:14px;'>")
          .append("        <button type='button' onclick='document.getElementById(\"folderModal\").style.display=\"none\"' class='btn-cyber' style='background:#334155;'>Cancelar</button>")
          .append("        <button type='submit' class='btn-cyber' style='background:#0284c7;'><i class='fas fa-check'></i> Abrir Proyecto</button>")
          .append("      </div>")
          .append("    </form>")
          .append("  </div>")
          .append("</div>");

        // Client-side Script for Drag and Drop
        sb.append("""
            <script>
            function handleDrop(event, parentId) {
                event.preventDefault();
                event.stopPropagation();
                let type = event.dataTransfer.getData("text/plain");
                if (type && type.trim().length > 0) {
                    window.location.href = '?add=' + encodeURIComponent(type) + '&parent=' + encodeURIComponent(parentId);
                }
            }
            </script>
            """);

        return RawHtml.of(sb.toString());
    }

    private void renderFileTree(StringBuilder sb, ProjectFileNode node) {
        if (node == null) return;
        if (node.isDirectory()) {
            sb.append("<details open style='margin-left:8px; margin-bottom:4px;'>")
              .append("<summary style='font-size:12px; color:#cbd5e1; cursor:pointer; list-style:none; display:flex; align-items:center; gap:6px;'>")
              .append("<i class='fas fa-folder' style='color:#f59e0b; font-size:11px;'></i> <b>").append(node.getName()).append("</b>")
              .append("</summary>")
              .append("<div style='border-left:1px dashed #334155; margin-left:4px; padding-left:6px;'>");

            for (ProjectFileNode child : node.getChildren()) {
                renderFileTree(sb, child);
            }

            sb.append("</div></details>");
        } else {
            String icon = "far fa-file";
            String color = "#94a3b8";
            String clickLink = null;

            if (node.isPage()) {
                icon = "fas fa-window-maximize";
                color = "#22c55e";
                clickLink = "?openPage=" + node.getAbsolutePath();
            } else if (node.getName().endsWith(".java")) {
                icon = "fab fa-java";
                color = "#38bdf8";
                clickLink = "?openPage=" + node.getAbsolutePath();
            } else if (node.getName().endsWith(".xml")) {
                icon = "fas fa-code";
                color = "#eab308";
            }

            sb.append("<div style='margin-left:14px; margin-top:2px; margin-bottom:2px; font-size:11px; display:flex; align-items:center; gap:6px;'>");
            if (clickLink != null) {
                sb.append("<a href='").append(clickLink).append("' style='text-decoration:none; color:inherit; display:flex; align-items:center; gap:6px;' title='Haga clic para diseñar esta clase'>")
                  .append("<i class='").append(icon).append("' style='color:").append(color).append("; font-size:11px;'></i> ")
                  .append("<span style='color:").append(node.isPage() ? "#38bdf8; font-weight:700;" : "#cbd5e1;").append("'>").append(node.getName()).append("</span>");
                if (node.isPage()) {
                    sb.append("<span class='cyber-badge-online' style='padding:1px 4px; font-size:9px;'>PAGE</span>");
                }
                sb.append("</a>");
            } else {
                sb.append("<i class='").append(icon).append("' style='color:").append(color).append("; font-size:11px;'></i> ")
                  .append("<span style='color:#94a3b8;'>").append(node.getName()).append("</span>");
            }
            sb.append("</div>");
        }
    }

    private void renderInteractiveCanvas(StringBuilder sb, CanvasWidget w, String activeSelId) {
        if (w == null) return;
        boolean isSelected = w.getId().equalsIgnoreCase(activeSelId);
        String selClass = isSelected ? "canvas-box canvas-box-selected" : "canvas-box";
        String type = w.getType() != null ? w.getType() : "Box";

        sb.append("<div class='").append(selClass).append("'")
          .append(" ondragover='event.preventDefault(); event.stopPropagation(); this.style.borderColor=\"#00d4ff\";'")
          .append(" ondragleave='event.preventDefault(); event.stopPropagation(); this.style.borderColor=\"\";'")
          .append(" ondrop='handleDrop(event, \"").append(w.getId()).append("\")'>")

          .append("  <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;'>")
          .append("    <a href='?sel=").append(w.getId()).append("' style='text-decoration:none; display:flex; align-items:center; gap:6px;'>")
          .append("      <span class='cyber-badge-info'>").append(type).append("</span>")
          .append("      <span style='font-size:12px; font-weight:700; color:#fff;'>").append(w.getLabel() != null ? w.getLabel() : "").append("</span>")
          .append("    </a>")
          .append("    <div style='display:flex; gap:6px;'>")
          .append("      <a href='?add=Paragraph&parent=").append(w.getId()).append("' title='Añadir Componente Hijo' class='btn-cyber' style='padding:2px 6px; font-size:10px;'><i class='fas fa-plus'></i></a>");

        if (!w.getId().equalsIgnoreCase(sessionState.getRootCanvas().getId())) {
            sb.append("      <a href='?action=delete&del=").append(w.getId()).append("' title='Eliminar' class='btn-danger' style='padding:2px 6px; font-size:10px;' onclick=\"return confirm('¿Eliminar ").append(type).append("?');\"><i class='fas fa-times'></i></a>");
        }

        sb.append("    </div>")
          .append("  </div>");

        // Visual presentation based on component type
        switch (type.toUpperCase()) {
            case "HEADER" -> sb.append("<h3 style='margin:4px 0; color:#38bdf8;'>").append(w.getText()).append("</h3>");
            case "PARAGRAPH" -> sb.append("<p style='margin:4px 0; color:#cbd5e1; font-size:13px;'>").append(w.getText()).append("</p>");
            case "LABEL" -> sb.append("<span style='font-size:12px; font-weight:700; color:#00d4ff; background:rgba(0,212,255,0.1); padding:2px 8px; border-radius:4px;'>").append(w.getText()).append("</span>");
            case "SPAN" -> sb.append("<span style='font-size:13px; color:#e2e8f0;'>").append(w.getText()).append("</span>");
            case "ELEVATEDBUTTON" -> sb.append("<button class='btn-cyber' type='button' style='pointer-events:none;'><i class='fas fa-play'></i> ").append(w.getText()).append("</button>");
            case "OUTLINEDBUTTON" -> sb.append("<button class='btn-cyber' type='button' style='background:transparent; border:1px solid #00d4ff; color:#00d4ff; pointer-events:none;'>").append(w.getText()).append("</button>");
            case "BUTTON" -> sb.append("<button class='btn-cyber' type='button' style='background:#334155; pointer-events:none;'>").append(w.getText()).append("</button>");
            case "TEXTFIELD" -> sb.append("<input type='text' disabled class='cyber-input' placeholder='").append(w.getText()).append("' style='max-width:320px;'/>");
            case "STATCARD" -> sb.append("<div style='background:#0f172a; padding:12px; border-radius:8px; border:1px solid #334155; width:180px;'><div style='font-size:11px; color:#94a3b8;'>").append(w.getLabel()).append("</div><div style='font-size:20px; font-weight:900; color:#00d4ff; margin-top:4px;'>").append(w.getText()).append("</div></div>");
            case "TABLE" -> sb.append("<table class='designer-table' style='width:100%;'><thead><tr><th>ID</th><th>Campo 1</th><th>Campo 2</th></tr></thead><tbody><tr><td><code>01</code></td><td>Dato Demo</td><td>Activo</td></tr></tbody></table>");
            case "POLICE3DCANVAS" -> sb.append("<div style='background:#020617; border:1px solid #00d4ff; border-radius:8px; padding:14px; text-align:center;'><i class='fas fa-cube' style='font-size:22px; color:#00d4ff; margin-bottom:6px;'></i><div style='font-size:12px; color:#e2e8f0; font-weight:700;'>JettraPolice 3D Live Viewport</div></div>");
            case "GRID" -> {
                sb.append("<div style='display:grid; grid-template-columns: repeat(").append(w.getColumns() > 0 ? w.getColumns() : 2).append(", 1fr); gap:10px; width:100%; margin-top:8px;'>");
                for (CanvasWidget child : w.getChildren()) {
                    renderInteractiveCanvas(sb, child, activeSelId);
                }
                sb.append("</div>");
                sb.append("</div>");
                return;
            }
            case "ROW" -> {
                sb.append("<div style='display:flex; gap:10px; align-items:center; flex-wrap:wrap; margin-top:8px;'>");
                for (CanvasWidget child : w.getChildren()) {
                    renderInteractiveCanvas(sb, child, activeSelId);
                }
                sb.append("</div>");
                sb.append("</div>");
                return;
            }
            default -> {
                for (CanvasWidget child : w.getChildren()) {
                    renderInteractiveCanvas(sb, child, activeSelId);
                }
            }
        }

        sb.append("</div>");
    }
}
