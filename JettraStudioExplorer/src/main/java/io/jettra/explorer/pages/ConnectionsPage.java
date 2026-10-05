package io.jettra.explorer.pages;

import io.jettra.explorer.model.ConnectionProfile;
import io.jettra.explorer.service.ConnectionManager;
import io.jettra.explorer.service.StoreClusterService;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.FeedbackPanel;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.ListItem;
import io.jettra.studio.components.ListView;
import io.jettra.studio.components.TextField;
import io.jettra.studio.core.Form;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.Model;
import io.jettra.studio.security.Secured;

import java.util.List;
import java.util.Optional;

/**
 * Panel Administrador de Conexiones al Servidor JettraStore.
 * Permite solicitar URL, usuario, contraseña, probar conectividad en vivo
 * y conmutar el clúster activo para cargar dinámicamente sus bases de datos.
 */
@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class ConnectionsPage extends ExplorerTemplatePage {

    private String profileName = "";
    private String serverUrl = "127.0.0.1:9010";
    private String serverUser = "admin";
    private String serverPass = "admin-jettra";

    public ConnectionsPage() {
        this(new PageParameters());
    }

    public ConnectionsPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Administrador de Conexiones - JettraStore Explorer");
        super.onInitialize();

        if (this.profileName == null) this.profileName = "";
        if (this.serverUrl == null) this.serverUrl = "127.0.0.1:9010";
        if (this.serverUser == null) this.serverUser = "admin";
        if (this.serverPass == null) this.serverPass = "admin-jettra";

        ConnectionManager connManager = ConnectionManager.getInstance();
        StoreClusterService clusterService = StoreClusterService.getInstance();

        FeedbackPanel feedback = new FeedbackPanel("connFeedback");
        add(feedback);

        // Process URL Query Actions (activate, test, delete)
        String action = getPageParameters().get("action");
        String targetId = getPageParameters().get("id");

        if (action != null && targetId != null) {
            switch (action.toLowerCase()) {
                case "activate" -> {
                    boolean ok = connManager.activate(targetId);
                    if (ok) {
                        feedback.info("✓ Conexión conmutada exitosamente. Bases de datos del servidor sincronizadas.");
                    } else {
                        feedback.error("⚠ No se pudo conmutar a la conexión " + targetId + ".");
                    }
                }
                case "test" -> {
                    Optional<ConnectionProfile> opt = connManager.findById(targetId);
                    if (opt.isPresent()) {
                        var res = connManager.testConnection(opt.get());
                        if (res.success()) {
                            feedback.info("✓ Prueba exitosa: " + res.message() + " (" + res.latencyMs() + " ms).");
                        } else {
                            feedback.error("⚠ Falló prueba de conexión: " + res.message() + " (" + res.latencyMs() + " ms).");
                        }
                    }
                }
                case "delete" -> {
                    boolean deleted = connManager.delete(targetId);
                    if (deleted) {
                        feedback.info("✓ Perfil de conexión " + targetId + " eliminado.");
                    } else {
                        feedback.warning("⚠ No se pudo eliminar el perfil " + targetId + ".");
                    }
                }
            }
        }

        // Active Connection Status Info Card
        ConnectionProfile activeProfile = connManager.getActiveProfile()
            .orElse(clusterService.getCurrentProfile());
        add(new Label("activeConnName", activeProfile.getName()));
        add(new Label("activeConnUrl", activeProfile.getHost() + ":" + activeProfile.getPort()));
        add(new Label("activeConnUser", activeProfile.getUsername()));
        add(new Label("activeConnStatus", activeProfile.getLastStatus()));
        add(new Label("activeConnDbsCount", String.valueOf(clusterService.getDatabases().size())));

        // Form to Add or Update Connection Profile
        Form<Void> connForm = new Form<>("connForm");
        connForm.method("POST");
        connForm.action("/connections");

        TextField<String> nameField = new TextField<>("inputConnName", Model.of(profileName));
        nameField.placeholder("Ej: Clúster Producción Primario").required(true);
        connForm.add(nameField);

        TextField<String> urlField = new TextField<>("inputConnUrl", Model.of(serverUrl));
        urlField.placeholder("127.0.0.1:9010 o host:puerto").required(true);
        connForm.add(urlField);

        TextField<String> userField = new TextField<>("inputConnUser", Model.of(serverUser));
        userField.placeholder("admin").required(true);
        connForm.add(userField);

        TextField<String> passField = new TextField<>("inputConnPass", Model.of(serverPass));
        passField.placeholder("Contraseña").required(true);
        connForm.add(passField);

        // Button: Probar Conexión Directa
        connForm.add(Button.of("btnTestConn", "🔍 Probar Conexión", () -> {
            String url = urlField.getModelObject() != null ? urlField.getModelObject().toString().trim() : "";
            String user = userField.getModelObject() != null ? userField.getModelObject().toString().trim() : "";
            String pass = passField.getModelObject() != null ? passField.getModelObject().toString().trim() : "";

            if (url.isBlank() || user.isBlank()) {
                feedback.error("Debe especificar la URL del servidor y el usuario para probar la conexión.");
                return;
            }

            ConnectionProfile testProfile = new ConnectionProfile("temp_test", "Prueba Temporal", url, user, pass, false);
            var res = connManager.testConnection(testProfile);
            if (res.success()) {
                feedback.info("✓ " + res.message() + " (Latencia: " + res.latencyMs() + " ms). Servidor listo.");
            } else {
                feedback.error("⚠ Falló la conexión con JettraStore: " + res.message());
            }
        }).variant(Button.Variant.BLUE));

        // Button: Guardar & Conectar Ahora
        connForm.add(Button.of("btnSaveAndConnect", "⚡ Guardar & Conectar Ahora", () -> {
            String name = nameField.getModelObject() != null ? nameField.getModelObject().toString().trim() : "";
            String url = urlField.getModelObject() != null ? urlField.getModelObject().toString().trim() : "";
            String user = userField.getModelObject() != null ? userField.getModelObject().toString().trim() : "";
            String pass = passField.getModelObject() != null ? passField.getModelObject().toString().trim() : "";

            if (name.isBlank() || url.isBlank() || user.isBlank()) {
                feedback.error("Todos los campos de conexión son obligatorios.");
                return;
            }

            ConnectionProfile newProfile = new ConnectionProfile(
                "conn_" + System.currentTimeMillis(),
                name, url, user, pass, true
            );

            connManager.saveOrUpdate(newProfile);
            connManager.activate(newProfile.getId());

            feedback.info("✓ Conexión '" + name + "' guardada y activada. Bases de datos cargadas exitosamente.");
            redirect("/connections");
        }).variant(Button.Variant.LIME));

        add(connForm);

        // List of Registered Connection Profiles
        List<ConnectionProfile> profiles = connManager.getProfiles();
        add(new ListView<ConnectionProfile>("profileRows", Model.of(profiles)) {
            @Override
            protected void populateItem(ListItem<ConnectionProfile> item) {
                ConnectionProfile cp = item.getModelObject();
                item.add(new Label("rowConnName", cp.getName()));
                item.add(new Label("rowConnUrl", cp.getHost() + ":" + cp.getPort()));
                item.add(new Label("rowConnUser", cp.getUsername()));
                item.add(new Label("rowConnStatus", cp.getLastStatus()));
                item.add(new Label("rowConnLatency", cp.getLastLatencyMs() > 0 ? cp.getLastLatencyMs() + " ms" : "< 1 ms"));
                item.add(new Label("rowConnActiveBadge", cp.isActive() ? "● ACTIVO" : "DISPONIBLE"));

                item.add(Link.of("linkActivate", "/connections?action=activate&id=" + cp.getId()));
                item.add(Link.of("linkTest", "/connections?action=test&id=" + cp.getId()));
                item.add(Link.of("linkDelete", "/connections?action=delete&id=" + cp.getId()));
            }
        });
    }
}
