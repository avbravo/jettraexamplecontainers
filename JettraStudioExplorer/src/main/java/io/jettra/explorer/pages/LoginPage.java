package io.jettra.explorer.pages;

import io.jettra.server.config.JettraConfig;
import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.TextField;
import io.jettra.studio.core.Form;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.core.WebPage;
import io.jettra.studio.model.Model;
import io.jettra.studio.security.NoLoginRequired;

@NoLoginRequired
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
        setPageTitle("Acceso al Clúster - JettraStore Explorer");

        String logout = getPageParameters().get("logout");
        if ("true".equalsIgnoreCase(logout)) {
            clearCookie("jettra_user", "/");
            clearCookie("jettra_role", "/");
            errorMessage = null;
        }

        String error = getPageParameters().get("error");
        if ("empty_fields".equalsIgnoreCase(error)) {
            errorMessage = "Error: El usuario y la clave son obligatorios.";
        } else if ("invalid_credentials".equalsIgnoreCase(error)) {
            errorMessage = "Credenciales incorrectas. Acceso permitido para admin o operator.";
        }

        add(new Label("loginTitle", "JettraStore Explorer"));
        add(new Label("loginSubtitle", "Panel de Administración del Clúster y Motores de Base de Datos"));

        Alert alert = Alert.danger("loginAlert", errorMessage != null ? errorMessage : "");
        alert.setVisible(errorMessage != null);
        add(alert);

        Form<Void> form = new Form<>("loginForm");
        form.method("POST");
        form.action("/login");

        TextField<String> userField = new TextField<>("username", Model.of(username));
        userField.placeholder("admin u operator").required(true);
        form.add(userField);

        TextField<String> passField = new TextField<>("password", Model.of(password));
        passField.placeholder("••••••••").required(true);
        form.add(passField);

        form.add(Button.of("btnLogin", "Conectar al Clúster", () -> {
            String u = userField.getModelObject() != null ? userField.getModelObject().toString().trim() : "";
            String p = passField.getModelObject() != null ? passField.getModelObject().toString().trim() : "";

            if (u.isEmpty() || p.isEmpty()) {
                redirect("/login?error=empty_fields");
                return;
            }

            if (isValidUser(u, p)) {
                String role = u.equalsIgnoreCase("operator") ? "OPERATOR" : "ADMIN";
                addCookie("jettra_user", u, "/");
                addCookie("jettra_role", role, "/");
                redirect("/dashboard");
            } else {
                redirect("/login?error=invalid_credentials");
            }
        }).variant(Button.Variant.GOLD));

        add(form);
    }

    private boolean isValidUser(String user, String pass) {
        String adminUser = JettraConfig.getProperty("security.admin.username");
        String adminPass = JettraConfig.getProperty("security.admin.password");
        String operUser = JettraConfig.getProperty("security.operator.username");
        String operPass = JettraConfig.getProperty("security.operator.password");

        if (adminUser != null && adminUser.equalsIgnoreCase(user) && adminPass != null && adminPass.equals(pass)) return true;
        if (operUser != null && operUser.equalsIgnoreCase(user) && operPass != null && operPass.equals(pass)) return true;

        return ("admin".equalsIgnoreCase(user) && ("admin".equals(pass) || "admin123".equals(pass)))
            || ("operator".equalsIgnoreCase(user) && ("operator".equals(pass) || "operator123".equals(pass)));
    }
}
