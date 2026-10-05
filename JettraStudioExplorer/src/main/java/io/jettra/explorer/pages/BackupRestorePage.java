package io.jettra.explorer.pages;

import io.jettra.explorer.model.BackupItem;
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

@Secured(roles = {"ADMIN"}, loginUrl = "/login")
public class BackupRestorePage extends ExplorerTemplatePage {

    private String targetDb = "ecommerce_db";

    public BackupRestorePage() {
        this(new PageParameters());
    }

    public BackupRestorePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Backup & Restore - JettraStore Explorer");
        super.onInitialize();
        if (this.targetDb == null) this.targetDb = "ecommerce_db";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("bakFeedback");
        add(feedback);

        String paramDb = getPageParameters().get("db");
        if (paramDb != null && !paramDb.isBlank()) {
            this.targetDb = paramDb.trim();
        }

        // Handle Restore Action
        String action = getPageParameters().get("action");
        String bakId = getPageParameters().get("bakId");
        if ("restore".equalsIgnoreCase(action) && bakId != null) {
            boolean ok = service.restoreBackup(bakId);
            if (ok) {
                feedback.info("✓ Base de datos restaurada correctamente desde el snapshot " + bakId + ".");
            }
        }

        // Backup Snapshots List
        List<BackupItem> backups = service.getBackups();
        add(new ListView<BackupItem>("backupRows", Model.of(backups)) {
            @Override
            protected void populateItem(ListItem<BackupItem> item) {
                BackupItem b = item.getModelObject();
                item.add(new Label("bakId", b.id()));
                item.add(new Label("bakDb", b.database()));
                item.add(new Label("bakFile", b.fileName()));
                item.add(new Label("bakSize", b.sizeFormatted()));
                item.add(new Label("bakDate", b.createdAt()));
                item.add(new Label("bakDuration", b.durationMs() + " ms"));

                item.add(Link.of("linkRestore", "/backup?action=restore&bakId=" + b.id()));
            }
        });

        // Form to Create Instant Backup
        Form<Void> createBakForm = new Form<>("createBackupForm");
        createBakForm.method("POST");
        createBakForm.action("/backup");

        TextField<String> dbInput = new TextField<>("bakTargetDbInput", Model.of(targetDb));
        dbInput.placeholder("Base de datos a respaldar").required(true);
        createBakForm.add(dbInput);

        createBakForm.add(Button.of("btnGenerateBackup", "⚡ Generar Snapshot Ahora", () -> {
            String db = dbInput.getModelObject() != null ? dbInput.getModelObject().toString().trim() : "ecommerce_db";
            BackupItem item = service.createBackup(db);
            feedback.info("✓ Snapshot " + item.fileName() + " generado exitosamente (" + item.sizeFormatted() + ") en " + item.durationMs() + " ms.");
            redirect("/backup");
        }).variant(Button.Variant.GOLD));

        add(createBakForm);
    }
}
