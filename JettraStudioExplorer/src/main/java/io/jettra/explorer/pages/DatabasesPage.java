package io.jettra.explorer.pages;

import io.jettra.explorer.model.DatabaseOverview;
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
 * CRUD Completo de Bases de Datos en JettraStore:
 * - CREATE: Crear nueva base de datos.
 * - READ: Cargar dinámicamente bases de datos activas del servidor.
 * - UPDATE: Renombrar y migrar esquemas de base de datos.
 * - DELETE: Eliminar base de datos del clúster.
 */
@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class DatabasesPage extends ExplorerTemplatePage {

    private String newDbName = "";
    private String renameOldName = "";
    private String renameNewName = "";

    public DatabasesPage() {
        this(new PageParameters());
    }

    public DatabasesPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Bases de Datos - JettraStore Explorer");
        super.onInitialize();

        if (this.newDbName == null) this.newDbName = "";
        if (this.renameOldName == null) this.renameOldName = "";
        if (this.renameNewName == null) this.renameNewName = "";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("dbFeedback");
        add(feedback);

        // Process Action Delete or Edit
        String action = getPageParameters().get("action");
        String targetName = getPageParameters().get("name");

        if ("delete".equalsIgnoreCase(action) && targetName != null && !targetName.isBlank()) {
            boolean removed = service.deleteDatabase(targetName);
            if (removed) {
                feedback.info("✓ Base de datos '" + targetName + "' eliminada exitosamente de JettraStore.");
            } else {
                feedback.warning("⚠ No se pudo eliminar la base de datos '" + targetName + "'.");
            }
        } else if ("edit".equalsIgnoreCase(action) && targetName != null && !targetName.isBlank()) {
            this.renameOldName = targetName.trim();
        }

        // READ: Databases List loaded directly from JettraStore
        List<DatabaseOverview> databases = service.getDatabases();
        add(new Label("totalDbsCount", String.valueOf(databases.size())));

        add(new ListView<DatabaseOverview>("dbRows", Model.of(databases)) {
            @Override
            protected void populateItem(ListItem<DatabaseOverview> item) {
                DatabaseOverview db = item.getModelObject();
                item.add(new Label("dbName", db.name()));
                item.add(new Label("dbStatus", db.status()));
                item.add(new Label("dbEngines", String.valueOf(db.engineCount())));
                item.add(new Label("dbRecords", String.format("%,d", db.recordCount())));
                item.add(new Label("dbSize", db.sizeFormatted()));
                item.add(new Label("dbUpdated", db.lastUpdated()));

                item.add(Link.of("linkEngines", "/engines?db=" + db.name()));
                item.add(Link.of("linkRecords", "/records?db=" + db.name()));
                item.add(Link.of("linkEdit", "/databases?action=edit&name=" + db.name()));
                item.add(Link.of("linkDelete", "/databases?action=delete&name=" + db.name()));
            }
        });

        // CREATE: Form to Create Database
        Form<Void> createForm = new Form<>("createDbForm");
        createForm.method("POST");
        createForm.action("/databases");

        TextField<String> dbField = new TextField<>("newDbInput", Model.of(newDbName));
        dbField.placeholder("Nombre de la nueva BD (ej: analytics_cluster)").required(true);
        createForm.add(dbField);

        createForm.add(Button.of("btnCreateDb", "+ Crear Base de Datos", () -> {
            String name = dbField.getModelObject() != null ? dbField.getModelObject().toString().trim() : "";
            if (name.isBlank()) {
                feedback.error("El nombre de la base de datos es obligatorio.");
                return;
            }
            boolean created = service.createDatabase(name);
            if (created) {
                feedback.info("✓ Base de datos '" + name + "' creada correctamente en JettraStore.");
            } else {
                feedback.error("⚠ No se pudo crear la base de datos '" + name + "'.");
            }
            redirect("/databases");
        }).variant(Button.Variant.LIME));

        add(createForm);

        // UPDATE: Form to Rename Database
        Form<Void> renameForm = new Form<>("renameDbForm");
        renameForm.method("POST");
        renameForm.action("/databases");

        TextField<String> oldField = new TextField<>("renameOldInput", Model.of(renameOldName));
        oldField.placeholder("Nombre actual de la BD").required(true);
        renameForm.add(oldField);

        TextField<String> newField = new TextField<>("renameNewInput", Model.of(renameNewName));
        newField.placeholder("Nuevo nombre de la BD").required(true);
        renameForm.add(newField);

        renameForm.add(Button.of("btnRenameDb", "✏️ Renombrar Base de Datos", () -> {
            String oldVal = oldField.getModelObject() != null ? oldField.getModelObject().toString().trim() : "";
            String newVal = newField.getModelObject() != null ? newField.getModelObject().toString().trim() : "";

            if (oldVal.isBlank() || newVal.isBlank()) {
                feedback.error("Debe especificar el nombre actual y el nuevo nombre para renombrar.");
                return;
            }

            boolean ok = service.renameDatabase(oldVal, newVal);
            if (ok) {
                feedback.info("✓ Base de datos renombrada exitosamente de '" + oldVal + "' a '" + newVal + "'.");
            } else {
                feedback.error("⚠ No se pudo renombrar la base de datos '" + oldVal + "'. Verifique que exista.");
            }
            redirect("/databases");
        }).variant(Button.Variant.BLUE));

        add(renameForm);
    }
}
