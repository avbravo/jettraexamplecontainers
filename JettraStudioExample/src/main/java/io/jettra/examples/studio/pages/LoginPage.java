package io.jettra.examples.studio.pages;

import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.core.Form;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.Modal;
import io.jettra.studio.components.TextField;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.core.WebPage;
import io.jettra.studio.model.Model;
import io.jettra.studio.model.ResourceModel;
import io.jettra.server.config.JettraConfig;

/**
 * Modern Login Page in JettraStudio, inspired by JettraFlux authentication flow.
 * Supports credentials verification, error banners/modals, and session redirection.
 */
public class LoginPage extends WebPage {

    private String username = "";
    private String password = "";
    private String errorMessage = null;

    public LoginPage() {
        this(new PageParameters());
    }

    public LoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Iniciar Sesión - JettraStudio Pro");

        // Handle logout query param
        String logout = getPageParameters().get("logout");
        if ("true".equalsIgnoreCase(logout)) {
            clearCookie("jettra_user", "/");
            clearCookie("jettra_role", "/");
            errorMessage = null;
        }

        // Handle error query param
        String error = getPageParameters().get("error");
        if ("empty_fields".equalsIgnoreCase(error)) {
            errorMessage = "Error: Nombre de usuario y contraseña son requeridos.";
        } else if ("invalid_credentials".equalsIgnoreCase(error)) {
            errorMessage = "Credenciales inválidas. Pruebe con admin / admin123 o demo / demo123.";
        }

        add(new Label("loginTitle", ResourceModel.of("login.title")));
        add(new Label("loginSubtitle", ResourceModel.of("login.subtitle")));

        // Alerta o Banner de error
        Alert errorAlert = Alert.danger("errorAlert", errorMessage != null ? errorMessage : "");
        errorAlert.setVisible(errorMessage != null);
        add(errorAlert);

        // Modal para alertas críticas
        Modal errorModal = Modal.of("errorModal", "Advertencia de Autenticación");
        add(errorModal);

        // Formulario de login
        Form<Void> form = new Form<>("loginForm");
        form.method("POST");

        TextField<String> userField = new TextField<>("username", Model.of(username));
        userField.placeholder("admin o demo");
        userField.required(true);
        form.add(userField);

        TextField<String> passField = new TextField<>("password", Model.of(password));
        passField.placeholder("••••••••");
        passField.required(true);
        form.add(passField);

        form.add(Button.of("btnLogin", "Iniciar Sesión", () -> {
            String u = userField.getModelObject() != null ? userField.getModelObject().toString() : "";
            String p = passField.getModelObject() != null ? passField.getModelObject().toString() : "";

            if (u == null || u.trim().isEmpty() || p == null || p.trim().isEmpty()) {
                redirect("/login?error=empty_fields");
                return;
            }

            if (isValidUser(u.trim(), p.trim())) {
                String role = u.equalsIgnoreCase("demo") ? "DEMO" : "ADMIN";
                addCookie("jettra_user", u.trim(), "/");
                addCookie("jettra_role", role, "/");
                redirect("/dashboard");
            } else {
                redirect("/login?error=invalid_credentials");
            }
        }).variant(Button.Variant.GOLD));

        add(form);

        add(Link.of("linkForgot", "#"));
        add(Link.of("linkHome", "/"));
    }

    private boolean isValidUser(String user, String pass) {
        // Check properties configured in jettra-config.properties
        String adminUser = JettraConfig.getProperty("security.admin.username");
        String adminPass = JettraConfig.getProperty("security.admin.password");
        String demoUser = JettraConfig.getProperty("security.demo.username");
        String demoPass = JettraConfig.getProperty("security.demo.password");

        if (adminUser != null && adminUser.equalsIgnoreCase(user) && adminPass != null && adminPass.equals(pass)) {
            return true;
        }
        if (demoUser != null && demoUser.equalsIgnoreCase(user) && demoPass != null && demoPass.equals(pass)) {
            return true;
        }
        // Default fallback credentials
        return ("admin".equalsIgnoreCase(user) && ("admin".equals(pass) || "admin123".equals(pass)))
            || ("demo".equalsIgnoreCase(user) && ("demo".equals(pass) || "demo123".equals(pass)));
    }
}
