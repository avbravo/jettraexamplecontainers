package io.jettra.flux.designer.service;

import io.jettra.flux.designer.model.CanvasWidget;

import java.io.File;
import java.nio.file.Files;

public class CodeGeneratorService {

    private static final CodeGeneratorService INSTANCE = new CodeGeneratorService();

    private CodeGeneratorService() {}

    public static CodeGeneratorService getInstance() {
        return INSTANCE;
    }

    public String generateJavaClass(String packageName, String className, String routePath, String appRole, CanvasWidget root) {
        String pkg = (packageName != null && !packageName.isBlank()) ? packageName.trim() : "com.flux.pages";
        String cls = (className != null && !className.isBlank()) ? className.trim() : "GeneratedPage";
        String route = (routePath != null && !routePath.isBlank()) ? routePath.trim() : "/" + cls.toLowerCase();
        String role = (appRole != null && !appRole.isBlank()) ? appRole.trim() : "ADMIN";

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(pkg).append(";\n\n");
        sb.append("import com.sun.net.httpserver.HttpExchange;\n");
        sb.append("import io.jettra.core.security.widget.PageWidgetAllow;\n");
        sb.append("import io.jettra.core.server.Page;\n");
        sb.append("import io.jettra.flux.core.Modifier;\n");
        sb.append("import io.jettra.flux.core.Widget;\n");
        sb.append("import io.jettra.flux.pages.FluxBaseHandler;\n");
        sb.append("import io.jettra.flux.widgets.*;\n");
        sb.append("import io.jettra.server.JettraServer;\n\n");
        sb.append("import java.util.Map;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(cls).append("\n");
        sb.append(" * Código funcional generado automáticamente por JettraFluxDesigner\n");
        sb.append(" * Compatible con Java 25, Loom Virtual Threads y JettraEE\n");
        sb.append(" */\n");
        sb.append("@PageWidgetAllow(role = {jcf.AppRole.").append(role).append("})\n");
        sb.append("@Page(path = \"").append(route).append("\")\n");
        sb.append("public class ").append(cls).append(" extends FluxBaseHandler {\n\n");
        sb.append("    @Override\n");
        sb.append("    protected String getTitle() {\n");
        sb.append("        return \"").append(cls).append(" • JettraFlux Application\";\n");
        sb.append("    }\n\n");
        sb.append("    @Override\n");
        sb.append("    protected Widget buildUI(HttpExchange exchange, Map<String, String> params, String currentTheme) {\n");
        sb.append("        return ").append(renderWidgetCode(root, "            ")).append(";\n");
        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }

    public boolean saveDirectlyToFile(String filePath, String javaCode) {
        if (filePath == null || javaCode == null) return false;
        try {
            File f = new File(filePath);
            File p = f.getParentFile();
            if (p != null && !p.exists()) p.mkdirs();
            Files.writeString(f.toPath(), javaCode);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving directly to file: " + e.getMessage());
            return false;
        }
    }

    public boolean saveClassToProject(String projectPath, String packageName, String className, String javaCode) {
        if (projectPath == null || className == null || javaCode == null) return false;
        try {
            String relPath = "src/main/java/" + packageName.replace('.', '/') + "/" + className + ".java";
            File targetFile = new File(projectPath, relPath);
            File parent = targetFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            Files.writeString(targetFile.toPath(), javaCode);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving class to project: " + e.getMessage());
            return false;
        }
    }

    private String renderWidgetCode(CanvasWidget widget, String indent) {
        if (widget == null) return "Paragraph.of(\"Empty Canvas\")";

        String type = widget.getType() != null ? widget.getType() : "Column";
        String text = widget.getText() != null ? widget.getText().replace("\"", "\\\"") : "";
        String label = widget.getLabel() != null ? widget.getLabel().replace("\"", "\\\"") : "";

        StringBuilder sb = new StringBuilder();
        String nextIndent = indent + "    ";

        switch (type.toUpperCase()) {
            case "CARD" -> {
                sb.append("Card.of(\n");
                renderChildren(sb, widget, nextIndent);
                sb.append(indent).append(")");
            }
            case "ROW" -> {
                sb.append("Row.of(\n");
                renderChildren(sb, widget, nextIndent);
                sb.append(indent).append(")");
            }
            case "COLUMN" -> {
                sb.append("Column.of(\n");
                renderChildren(sb, widget, nextIndent);
                sb.append(indent).append(")");
            }
            case "GRID" -> {
                sb.append("Grid.of(").append(widget.getColumns() > 0 ? widget.getColumns() : 2).append(",\n");
                renderChildren(sb, widget, nextIndent);
                sb.append(indent).append(")");
            }
            case "HEADER" -> sb.append("Header.of(3, \"").append(text.isEmpty() ? label : text).append("\")");
            case "PARAGRAPH" -> sb.append("Paragraph.of(\"").append(text.isEmpty() ? label : text).append("\")");
            case "LABEL" -> sb.append("Label.of(\"").append(text.isEmpty() ? label : text).append("\")");
            case "SPAN" -> sb.append("Span.of(\"").append(text.isEmpty() ? label : text).append("\")");
            case "ELEVATEDBUTTON" -> sb.append("ElevatedButton.of(\"").append(text.isEmpty() ? "Botón de Acción" : text).append("\")");
            case "OUTLINEDBUTTON" -> sb.append("OutlinedButton.of(\"").append(text.isEmpty() ? "Botón Bordeado" : text).append("\")");
            case "BUTTON" -> sb.append("Button.of(\"").append(text.isEmpty() ? "Botón" : text).append("\")");
            case "TEXTFIELD" -> sb.append("TextField.of().placeholder(\"").append(text.isEmpty() ? "Ingrese valor..." : text).append("\")");
            case "STATCARD" -> sb.append("StatCard.of(\"").append(label.isEmpty() ? "Métrica" : label).append("\", \"").append(text.isEmpty() ? "1,240" : text).append("\", Icon.CHART_LINE)");
            case "TABLE" -> sb.append("Table.of(\"Tabla de Datos\")");
            case "POLICE3DCANVAS" -> sb.append("RawHtml.of(\"<div class='cyber-police-card' style='background:#040711; padding:20px; border-radius:12px; border:1px solid #00d4ff;'><h4 style='color:#00d4ff;'>🛡️ JettraPolice 3D Live Feed</h4><p style='color:#94a3b8;'>Supervisión activa del quórum Raft y telemetría de memoria.</p></div>\")");
            default -> sb.append("Paragraph.of(\"").append(type).append(": ").append(label).append("\")");
        }

        if ((widget.getStyles() != null && !widget.getStyles().isBlank()) || (widget.getCssClasses() != null && !widget.getCssClasses().isBlank())) {
            sb.append(".modifier(new Modifier()");
            if (widget.getStyles() != null && !widget.getStyles().isBlank()) {
                sb.append(".style(\"").append(widget.getStyles().replace("\"", "\\\"")).append("\")");
            }
            if (widget.getCssClasses() != null && !widget.getCssClasses().isBlank()) {
                sb.append(".cssClass(\"").append(widget.getCssClasses().replace("\"", "\\\"")).append("\")");
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private void renderChildren(StringBuilder sb, CanvasWidget widget, String indent) {
        if (widget.getChildren().isEmpty()) {
            sb.append(indent).append("Paragraph.of(\"").append(widget.getLabel() != null ? widget.getLabel() : "Contenedor").append("\")\n");
            return;
        }
        for (int i = 0; i < widget.getChildren().size(); i++) {
            CanvasWidget child = widget.getChildren().get(i);
            sb.append(indent).append(renderWidgetCode(child, indent));
            if (i < widget.getChildren().size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
    }
}
