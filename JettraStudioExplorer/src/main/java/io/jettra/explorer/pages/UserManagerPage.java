package io.jettra.explorer.pages;

import io.jettra.explorer.model.JettraUserAccount;
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

/**
 * Gestión de Usuarios, Roles Globales y Permisos de Bases de Datos.
 * Inspirado en el módulo Security Patrol de JettraStorePolice3D.
 */
@Secured(roles = {"ADMIN"}, loginUrl = "/login")
public class UserManagerPage extends ExplorerTemplatePage {

    private String newUsername = "";
    private String newPassword = "";
    private String newFullName = "";
    private String newRole = "OPERATOR";

    public UserManagerPage() {
        this(new PageParameters());
    }

    public UserManagerPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Usuarios & Seguridad - JettraStore Explorer");
        super.onInitialize();

        if (this.newUsername == null) this.newUsername = "";
        if (this.newPassword == null) this.newPassword = "";
        if (this.newFullName == null) this.newFullName = "";
        if (this.newRole == null) this.newRole = "OPERATOR";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("userFeedback");
        add(feedback);

        String action = getPageParameters().get("action");
        String targetUser = getPageParameters().get("username");

        if ("delete".equalsIgnoreCase(action) && targetUser != null && !targetUser.isBlank()) {
            boolean removed = service.deleteUser(targetUser.trim());
            if (removed) {
                feedback.info("✓ Cuenta de usuario '" + targetUser + "' eliminada exitosamente.");
            } else {
                feedback.warning("⚠ No se puede eliminar el superadministrador principal 'admin'.");
            }
        }

        // Users List
        List<JettraUserAccount> users = service.getUsers();
        add(new Label("totalUsersBadge", String.valueOf(users.size())));

        add(new ListView<JettraUserAccount>("userRows", Model.of(users)) {
            @Override
            protected void populateItem(ListItem<JettraUserAccount> item) {
                JettraUserAccount u = item.getModelObject();
                item.add(new Label("uName", u.getUsername()));
                item.add(new Label("uFullName", u.getFullName()));
                item.add(new Label("uRole", u.getGlobalRole()));
                item.add(new Label("uPermsCount", u.getDbPermissions().size() + " bases de datos"));
                item.add(Link.of("linkDeleteUser", "/users?action=delete&username=" + u.getUsername()));
            }
        });

        // Form to Add/Update User
        Form<Void> form = new Form<>("userForm");
        form.method("POST");
        form.action("/users");

        TextField<String> userInput = new TextField<>("uNameInput", Model.of(newUsername));
        userInput.placeholder("Nombre de usuario (ej: db_dev_01)").required(true);
        form.add(userInput);

        TextField<String> passInput = new TextField<>("uPassInput", Model.of(newPassword));
        passInput.placeholder("Contraseña segura").required(true);
        form.add(passInput);

        TextField<String> fullNameInput = new TextField<>("uFullNameInput", Model.of(newFullName));
        fullNameInput.placeholder("Nombre completo / Cargo").required(true);
        form.add(fullNameInput);

        TextField<String> roleInput = new TextField<>("uRoleInput", Model.of(newRole));
        roleInput.placeholder("ADMIN, OPERATOR o AUDITOR").required(true);
        form.add(roleInput);

        form.add(Button.of("btnSaveUser", "+ Guardar Usuario", () -> {
            String u = userInput.getModelObject() != null ? userInput.getModelObject().toString().trim() : "";
            String p = passInput.getModelObject() != null ? passInput.getModelObject().toString().trim() : "";
            String fn = fullNameInput.getModelObject() != null ? fullNameInput.getModelObject().toString().trim() : "";
            String r = roleInput.getModelObject() != null ? roleInput.getModelObject().toString().trim().toUpperCase() : "OPERATOR";

            if (u.isBlank() || p.isBlank()) {
                feedback.error("El usuario y la contraseña son obligatorios.");
                return;
            }

            JettraUserAccount acc = new JettraUserAccount(u, p, fn.isBlank() ? u : fn, r);
            acc.setDbPermission("ecommerce_db", "ADMIN".equalsIgnoreCase(r) ? "ADMIN" : "READ_WRITE");
            acc.setDbPermission("iot_telemetry", "ADMIN".equalsIgnoreCase(r) ? "ADMIN" : "READ_ONLY");

            service.saveUser(acc);
            feedback.info("✓ Usuario '" + u + "' guardado con rol " + r + " y permisos asignados.");
            redirect("/users");
        }).variant(Button.Variant.LIME));

        add(form);
    }
}
