package io.jettra.flux.designer.service;

import io.jettra.flux.designer.model.CanvasWidget;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.model.PaletteItem;
import io.jettra.flux.designer.service.JavaPageParserService.ParsedPageInfo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DesignerSessionState implements Serializable {

    private static final DesignerSessionState INSTANCE = new DesignerSessionState();

    private MavenProjectInfo currentProject;
    private CanvasWidget rootCanvas;
    private String selectedWidgetId;

    // Active loaded page metadata
    private String currentOpenedFilePath;
    private String currentOpenedClassName = "CardDemoPage";
    private String currentOpenedPackage = "com.flux.plugin.example.pages.apps";
    private String currentOpenedRoute = "/card-demo";
    private String currentOpenedRole = "ADMIN";
    private String currentExtendsClass = "TemplatePage";

    private final List<PaletteItem> palette = new ArrayList<>();

    private DesignerSessionState() {
        initDefaultPalette();
        initDefaultProject();
    }

    public static DesignerSessionState getInstance() {
        return INSTANCE;
    }

    private void initDefaultProject() {
        File exampleDir = new File("../JettraFluxExample");
        if (exampleDir.exists()) {
            this.currentProject = MavenProjectService.getInstance().inspectProject(exampleDir.getAbsolutePath());
            // Try to load CardDemoPage by default if it exists
            File defaultPage = new File(exampleDir, "src/main/java/com/flux/plugin/example/pages/apps/CardDemoPage.java");
            if (defaultPage.exists()) {
                openJavaPage(defaultPage);
            } else {
                initFallbackCanvas();
            }
        } else {
            initFallbackCanvas();
        }
    }

    private void initFallbackCanvas() {
        this.rootCanvas = new CanvasWidget("Card", "Card Principal", "Contenedor de Demostración");
        CanvasWidget col = new CanvasWidget("Column", "Columna de Contenido", "");
        col.addChild(new CanvasWidget("Header", "Título de la Vista", "Demostración de Componentes JettraFlux"));
        col.addChild(new CanvasWidget("Paragraph", "Descripción", "Seleccione un archivo en el explorador de proyecto o arrastre componentes de la paleta."));
        col.addChild(new CanvasWidget("ElevatedButton", "Botón", "Acción JettraFlux"));
        this.rootCanvas.addChild(col);
    }

    public boolean openJavaPage(File file) {
        if (file == null || !file.exists()) return false;
        ParsedPageInfo info = JavaPageParserService.getInstance().parseJavaPageFile(file);
        if (info == null) return false;

        this.currentOpenedFilePath = info.originalFilePath();
        this.currentOpenedClassName = info.className();
        this.currentOpenedPackage = info.packageName();
        this.currentOpenedRoute = info.routePath();
        this.currentOpenedRole = info.primaryRole();
        this.currentExtendsClass = info.extendsClass();
        this.rootCanvas = info.rootWidget();
        this.selectedWidgetId = (rootCanvas != null) ? rootCanvas.getId() : null;
        return true;
    }

    public boolean openJavaPageByPath(String path) {
        if (path == null || path.isBlank()) return false;
        return openJavaPage(new File(path.trim()));
    }

    private void initDefaultPalette() {
        palette.clear();
        // 1. Contenedores y Estructura
        palette.add(new PaletteItem("Card", "Card (Tarjeta)", "Contenedores", "fas fa-square", "Contenedor elevado con borde, padding y sombra de diseño", ""));
        palette.add(new PaletteItem("Row", "Row (Fila)", "Contenedores", "fas fa-arrows-alt-h", "Disposición horizontal flex con alineación", ""));
        palette.add(new PaletteItem("Column", "Column (Columna)", "Contenedores", "fas fa-arrows-alt-v", "Disposición vertical apilada de widgets", ""));
        palette.add(new PaletteItem("Grid", "Grid (Rejilla)", "Contenedores", "fas fa-th", "Rejilla responsiva con número dinámico de columnas", ""));
        palette.add(new PaletteItem("Panel", "Panel (Panel)", "Contenedores", "fas fa-window-maximize", "Panel contenedor con encabezado y cuerpo", ""));
        palette.add(new PaletteItem("Box", "Box (Caja)", "Contenedores", "fas fa-cube", "Caja contenedora flexible", ""));
        palette.add(new PaletteItem("Divider", "Divider (Separador)", "Contenedores", "fas fa-minus", "Línea horizontal divisoria", ""));
        palette.add(new PaletteItem("Center", "Center (Centrado)", "Contenedores", "fas fa-align-center", "Centrador absoluto de contenido", ""));

        // 2. Tipografía y Textos
        palette.add(new PaletteItem("Header", "Header (Encabezado)", "Tipografía", "fas fa-heading", "Encabezado H1-H4 de alta legibilidad", ""));
        palette.add(new PaletteItem("Paragraph", "Paragraph (Párrafo)", "Tipografía", "fas fa-paragraph", "Bloque de texto formateado con soporte HTML", ""));
        palette.add(new PaletteItem("Label", "Label (Etiqueta)", "Tipografía", "fas fa-tag", "Etiqueta descriptiva para formularios", ""));
        palette.add(new PaletteItem("Span", "Span (Texto Corto)", "Tipografía", "fas fa-font", "Fragmento inline estilizable", ""));
        palette.add(new PaletteItem("Badge", "Badge (Insignia)", "Tipografía", "fas fa-certificate", "Insignia pequeña de estado o recuento", ""));
        palette.add(new PaletteItem("Chip", "Chip (Píldora)", "Tipografía", "fas fa-capsules", "Píldora interactiva o de selección rápida", ""));
        palette.add(new PaletteItem("Alert", "Alert (Alerta)", "Tipografía", "fas fa-exclamation-triangle", "Mensaje destacado de información o advertencia", ""));

        // 3. Acciones y Botones
        palette.add(new PaletteItem("ElevatedButton", "ElevatedButton (Botón Elevado)", "Acciones", "fas fa-mouse-pointer", "Botón con relieve 3D, sombra y animación", ""));
        palette.add(new PaletteItem("OutlinedButton", "OutlinedButton (Botón Bordeado)", "Acciones", "far fa-square", "Botón transparente con borde sutil", ""));
        palette.add(new PaletteItem("Button", "Button (Botón Base)", "Acciones", "fas fa-play-circle", "Botón estándar de formulario", ""));
        palette.add(new PaletteItem("ActionIcon", "ActionIcon (Icono de Acción)", "Acciones", "fas fa-bolt", "Icono interactivo con evento JavaScript o callback", ""));

        // 4. Formularios y Entradas
        palette.add(new PaletteItem("TextField", "TextField (Campo de Texto)", "Formularios", "fas fa-edit", "Input de texto con validación y rings reactivos", ""));
        palette.add(new PaletteItem("PasswordField", "PasswordField (Clave Secreta)", "Formularios", "fas fa-key", "Campo de entrada con máscara de seguridad", ""));
        palette.add(new PaletteItem("TextArea", "TextArea (Área de Texto)", "Formularios", "fas fa-align-left", "Entrada multilínea expandible", ""));
        palette.add(new PaletteItem("Dropdown", "Dropdown (Desplegable)", "Formularios", "fas fa-caret-square-down", "Menú de selección única de opciones", ""));
        palette.add(new PaletteItem("Checkbox", "Checkbox (Casilla)", "Formularios", "fas fa-check-square", "Casilla de verificación booleana", ""));
        palette.add(new PaletteItem("ToggleSwitch", "ToggleSwitch (Interruptor)", "Formularios", "fas fa-toggle-on", "Interruptor deslizante On/Off", ""));

        // 5. Datos y Analítica
        palette.add(new PaletteItem("Table", "Table (Tabla de Datos)", "Datos", "fas fa-table", "Tabla estilizada para registros y entidades", ""));
        palette.add(new PaletteItem("StatCard", "StatCard (Métrica KPI)", "Datos", "fas fa-chart-line", "Tarjeta métrica ejecutiva con valor y tendencia", ""));
        palette.add(new PaletteItem("VisitorGraphCard", "VisitorGraphCard (Gráfico Tráfico)", "Datos", "fas fa-chart-area", "Gráfico visual de rendimiento de usuarios", ""));
        palette.add(new PaletteItem("TransactionHistoryCard", "TransactionHistoryCard (Transacciones)", "Datos", "fas fa-receipt", "Tarjeta de historial transaccional", ""));

        // 6. Metaverso y JettraPolice 3D
        palette.add(new PaletteItem("Police3DCanvas", "JettraPolice 3D Live Feed", "JettraPolice 3D", "fas fa-cube", "Viewport WebGL Three.js para supervisar nodos Raft y centinelas", ""));
        palette.add(new PaletteItem("PoliceSentinelCard", "Centinela Guard K9", "JettraPolice 3D", "fas fa-shield-alt", "Tarjeta de estado y salud de centinela autónomo", ""));
    }

    public MavenProjectInfo getCurrentProject() { return currentProject; }
    public void setCurrentProject(MavenProjectInfo project) { this.currentProject = project; }

    public CanvasWidget getRootCanvas() { return rootCanvas; }
    public void setRootCanvas(CanvasWidget rootCanvas) { this.rootCanvas = rootCanvas; }

    public String getSelectedWidgetId() { return selectedWidgetId; }
    public void setSelectedWidgetId(String selectedWidgetId) { this.selectedWidgetId = selectedWidgetId; }

    public String getCurrentOpenedFilePath() { return currentOpenedFilePath; }
    public void setCurrentOpenedFilePath(String path) { this.currentOpenedFilePath = path; }

    public String getCurrentOpenedClassName() { return currentOpenedClassName; }
    public void setCurrentOpenedClassName(String name) { this.currentOpenedClassName = name; }

    public String getCurrentOpenedPackage() { return currentOpenedPackage; }
    public void setCurrentOpenedPackage(String pkg) { this.currentOpenedPackage = pkg; }

    public String getCurrentOpenedRoute() { return currentOpenedRoute; }
    public void setCurrentOpenedRoute(String route) { this.currentOpenedRoute = route; }

    public String getCurrentOpenedRole() { return currentOpenedRole; }
    public void setCurrentOpenedRole(String role) { this.currentOpenedRole = role; }

    public String getCurrentExtendsClass() { return currentExtendsClass; }
    public void setCurrentExtendsClass(String ext) { this.currentExtendsClass = ext; }

    public List<PaletteItem> getPalette() { return Collections.unmodifiableList(palette); }

    public void addWidgetToCanvas(String type, String targetParentId) {
        CanvasWidget target = (targetParentId != null && !targetParentId.isBlank()) ? rootCanvas.findById(targetParentId) : rootCanvas;
        if (target == null) target = rootCanvas;

        String label = switch (type.toUpperCase()) {
            case "CARD" -> "Card Nuevo";
            case "ROW" -> "Fila Horizontal";
            case "COLUMN" -> "Columna Vertical";
            case "GRID" -> "Rejilla 2x2";
            case "HEADER" -> "Encabezado de Sección";
            case "PARAGRAPH" -> "Párrafo de Texto";
            case "LABEL" -> "Etiqueta";
            case "SPAN" -> "Texto Span";
            case "ELEVATEDBUTTON" -> "Botón de Acción";
            case "OUTLINEDBUTTON" -> "Botón Secundario";
            case "TEXTFIELD" -> "Entrada de Datos";
            case "STATCARD" -> "Métrica Clave";
            case "TABLE" -> "Tabla de Registros";
            case "POLICE3DCANVAS" -> "Lienzo 3D JettraPolice";
            default -> type;
        };

        CanvasWidget newWidget = new CanvasWidget(type, label, label);
        if ("Grid".equalsIgnoreCase(type)) {
            newWidget.setColumns(2);
        }
        target.addChild(newWidget);
        this.selectedWidgetId = newWidget.getId();
    }

    public boolean removeWidgetFromCanvas(String widgetId) {
        if (widgetId == null || widgetId.equalsIgnoreCase(rootCanvas.getId())) return false;
        boolean removed = rootCanvas.removeChild(widgetId);
        if (widgetId.equalsIgnoreCase(selectedWidgetId)) {
            selectedWidgetId = null;
        }
        return removed;
    }
}
