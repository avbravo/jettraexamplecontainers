package com.jettra.main.dashboard;

import io.jettra.wui.complex.Left;
import io.jettra.wui.core.JettraDashboardPage;

/**
 * Simplified DashboardBasePage leveraging JettraWUI integration. Most logic was
 * moved to JettraDashboardPage in io.jettra.wui.core.
 */
public abstract class DashboardBasePage extends JettraDashboardPage {

    public DashboardBasePage(String title) {
        super(title);
    }

    @Override
    protected void onInit(java.util.Map<String, String> params) {
        super.onInit(params);
        String loggedUser = getLoggedUser(currentExchange);
        if ("Guest".equals(loggedUser) || loggedUser.isEmpty()) return; // Already handled by super

        // Skip validation for dashboard itself
        if (this.getClass().getSimpleName().equals("DashboardPage") || this.getClass().getSimpleName().equals("ConsolePage")) {
            return; 
        }

        String pageName = this.getClass().getSimpleName(); 
        String feature = pageName.replace("Page", "").toUpperCase();

        com.jettra.plugin.autentification.entity.User u = com.jettra.plugin.autentification.services.UserService.findAll().stream()
            .filter(x -> x.firstName() != null && x.firstName().equalsIgnoreCase(loggedUser))
            .findFirst().orElse(null);

        if (u != null) {
            boolean hasPerm = false;
            if (u.roles() != null) {
                for (com.jettra.plugin.autentification.entity.Role r : u.roles()) {
                    if (r.permissions() != null) {
                        for (com.jettra.plugin.autentification.entity.Permission p : r.permissions()) {
                            if (p.name().equals(feature + "_ALL") || p.name().equals(feature + "_QUERY") || p.name().equals("ALL_ALL")) {
                                hasPerm = true;
                                break;
                            }
                        }
                    }
                    if (hasPerm) break;
                }
            }
            if (!hasPerm) {
                this.children.clear();
                try {
                    redirect(currentExchange, io.jettra.server.JettraServer.resolvePath("/dashboard?error=unauthorized"));
                } catch (Exception e) {}
                return;
            }
        }
    }

    @Override
    protected void setupLeft(Left left, String username) {
        initMenuBuilder();

        String compIcon = "<svg width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='opacity:0.7;'><path d='M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z'></path></svg>";

        String lang = io.jettra.server.config.JettraConfig.getProperty("app.language");
        boolean es = "es".equals(lang != null ? lang.trim() : "en");

        // --- Navigation ---
        addCategory(es ? "Navegación" : "Navigation", new String[]{}, ""); // Empty to hold custom elements
        appendMenuItem(es ? "Panel Principal" : "Main Dashboard", "/dashboard", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line></svg>");

        // --- Administration ---
        if ("admin".equals(username) || "demo".equals(username) || "avbravo".equals(username)) {
            addCategory(es ? "Administración" : "Administration", new String[]{}, "");
//            appendMenuItem("Persona CRUD", "/persona", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle></svg>");
//            appendMenuItem("Pais CRUD (MVC)", "/pais", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><circle cx='12' cy='12' r='10'></circle><line x1='2' y1='12' x2='22' y2='12'></line></svg>");
//            appendMenuItem("Deporte CRUD", "/deporte", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><circle cx='12' cy='12' r='10'></circle><line x1='2' y1='12' x2='22' y2='12'></line></svg>");
//            appendMenuItem("Grupo CRUD", "/grupo", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle></svg>");
//            appendMenuItem("SubGrupo CRUD", "/subgrupo", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle></svg>");
//            appendMenuItem("Planeta CRUD (@CrudView)", "/planeta", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><circle cx='12' cy='12' r='10'></circle><line x1='2' y1='12' x2='22' y2='12'></line></svg>");
            appendMenuItem("Permisos", "/permiso", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z'></path></svg>");
            appendMenuItem("Roles", "/rol", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle></svg>");
            appendMenuItem("Perfiles", "/perfil", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='11' width='18' height='11' rx='2' ry='2'></rect><path d='M7 11V7a5 5 0 0 1 10 0v4'></path></svg>");
            appendMenuItem("Usuarios", "/usuario", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle><path d='M23 21v-2a4 4 0 0 0-3-3.87'></path><path d='M16 3.13a4 4 0 0 1 0 7.75'></path></svg>");
            appendMenuItem("Web Designer", "/webdesigner", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>");
            appendMenuItem("Kanban Board", "/kanban", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>");
            appendMenuItem("SwaggerUI", "/swagger-ui", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>");
        }

        // --- Categories and Components ---
        addCategory(es ? "@Rules" : "@Rules", new String[]{"Reglas", "ReglasViewCrud"}, compIcon);
        addCategory(es ? "Crud" : "Crud", new String[]{"Persona", "Pais", "Deporte"}, compIcon);
        addCategory(es ? "@CrudView" : "@CrudView", new String[]{"Grupo", "SubGrupo", "Planeta", "DatatableEditableCrudView","Cancion"}, compIcon);
        addCategory(es ? "Credentials" : "Credentials", new String[]{"Credential", "Permission", "Role", "Department", "User"}, compIcon);
        addCategory(es ? "Library" : "Library", new String[]{"Author", "Book", "Publisher","Reader"}, compIcon);
        addCategory(es ? "DataTable" : "DataTable", new String[]{"DataTable", "DatatableEditable"}, compIcon);
        addCategory(es ? "Master-Details" : "Master-Details", new String[]{"ViewDataTable"}, compIcon);
        addCategory(es ? "Tipografía" : "Typography", new String[]{"Header", "Paragraph", "Span", "Label", "Separator", "Icon", "Typography"}, compIcon);
        addCategory(es ? "Formularios" : "Forms", new String[]{"Button", "Catcha", "CheckBox", "CheckBoxGroup", "CreditCard", "Form", "FormGroup", "Forms", "Hidden", "NoEditable", "OTPValidator", "RadioButton", "RadioGroupButton", "ScheduleControl", "Spinner", "TextBox", "TextArea", "ToggleSwitch", "Tree"}, compIcon);
        addCategory(es ? "Select" : "Select", new String[]{"SelectOne", "SelectMany", "SelectOneIcon"}, compIcon);

        addCategory(es ? "Fecha y Tiempo" : "Date", new String[]{"Calendar", "DatePicker", "Organigram", "Schedule", "Time", "Timeline", "Clock"}, compIcon);
        addCategory(es ? "Navegación" : "Navigation", new String[]{"Link", "Menu", "MenuBar", "Navigation"}, compIcon);
        addCategory(es ? "Retroalimentación" : "Feedback", new String[]{"Alert", "Modal", "Notification", "SessionTimeout", "Feedback", "TrafficLight"}, compIcon);
        addCategory(es ? "Diseño de Página" : "Layout", new String[]{"Avatar", "AvatarGroup", "Board", "Card", "Carousel","Console", "Div", "Divide", "Draw", "FileUpload", "FolderSelector", "Grid", "Icon", "Image", "LayoutDisplay", "Loading", "LoginAdvanced", "LoginUI", "Panel", "ProgressBar", "TabView"}, compIcon);
        addCategory(es ? "Multimedia" : "Media", new String[]{"BarCode", "Downloader", "PDFViewer", "QR", "QRReader", "ViewMedia"}, compIcon);
        addCategory(es ? "Charts" : "Charts", new String[]{"ChartsBar", "ChartsDoughnut", "ChartsLine", "ChartsPie", "ChartsRadar"}, compIcon);
        addCategory(es ? "Maps" : "Maps", new String[]{"Map"}, compIcon);
        menuHtmlBuilder.append("<div style='margin-top:20px;'></div>");
        appendMenuItem(es ? "Cerrar Sesión" : "Logout", "/logout", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline></svg>");

        finishMenuBuilder(left);
    }
}
