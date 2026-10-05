package io.jettra.explorer.pages;

import io.jettra.explorer.model.RecordItem;
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
 * CRUD Completo de Unidades de Registros en JettraStore:
 * - Selector de Base de Datos y Bucket/Colección multimodelos.
 * - CREATE: Inserción de nueva unidad de registro.
 * - READ: Búsqueda, filtrado y vista previa de payloads.
 * - UPDATE: Edición y actualización de registros existentes.
 * - DELETE: Eliminación de unidad de registro por identificador.
 */
@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class RecordsPage extends ExplorerTemplatePage {

    private String activeDb = "ecommerce_db";
    private String activeBucket = "customers";
    private String searchQ = "";

    private String insertId = "";
    private String insertJson = "";

    private String editId = "";
    private String editJson = "";

    public RecordsPage() {
        this(new PageParameters());
    }

    public RecordsPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Explorador de Registros & Unidades - JettraStore");
        super.onInitialize();

        if (this.activeDb == null) this.activeDb = "ecommerce_db";
        if (this.activeBucket == null) this.activeBucket = "customers";
        if (this.searchQ == null) this.searchQ = "";
        if (this.insertId == null) this.insertId = "";
        if (this.insertJson == null) this.insertJson = "";
        if (this.editId == null) this.editId = "";
        if (this.editJson == null) this.editJson = "";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("recFeedback");
        add(feedback);

        String pDb = getPageParameters().get("db");
        String pBucket = getPageParameters().get("bucket");
        String pQ = getPageParameters().get("q");

        if (pDb != null && !pDb.isBlank()) this.activeDb = pDb.trim();
        if (pBucket != null && !pBucket.isBlank()) this.activeBucket = pBucket.trim();
        if (pQ != null) this.searchQ = pQ.trim();

        // Handle URL Actions (delete or edit)
        String action = getPageParameters().get("action");
        String recIdParam = getPageParameters().get("id");

        if ("delete".equalsIgnoreCase(action) && recIdParam != null && !recIdParam.isBlank()) {
            boolean removed = service.deleteRecord(activeDb, activeBucket, recIdParam.trim());
            if (removed) {
                feedback.info("✓ Registro '" + recIdParam + "' eliminado del bucket " + activeBucket + ".");
            } else {
                feedback.warning("⚠ No se pudo eliminar el registro '" + recIdParam + "'.");
            }
        } else if ("edit".equalsIgnoreCase(action) && recIdParam != null && !recIdParam.isBlank()) {
            Optional<RecordItem> found = service.getRecordById(activeDb, activeBucket, recIdParam.trim());
            if (found.isPresent()) {
                this.editId = found.get().id();
                this.editJson = found.get().preview();
            } else {
                this.editId = recIdParam.trim();
            }
        }

        add(new Label("targetInfo", activeDb + " → " + activeBucket));
        add(new Label("currentDbBadge", activeDb));
        add(new Label("currentBucketBadge", activeBucket));

        // Available Databases List Navigation
        List<String> dbs = service.getDatabaseNames();
        add(new ListView<String>("dbNavList", Model.of(dbs)) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String dName = item.getModelObject();
                item.add(Link.of("linkSelectDb", "/records?db=" + dName));
                item.add(new Label("dbNavName", dName));
                item.add(new Label("dbNavActiveBadge", dName.equalsIgnoreCase(activeDb) ? "◀ ACTIVA" : ""));
            }
        });

        // Available Buckets List Navigation for the active database
        List<String> buckets = service.getBucketNames(activeDb);
        add(new ListView<String>("bucketNavList", Model.of(buckets)) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String bName = item.getModelObject();
                item.add(Link.of("linkSelectBucket", "/records?db=" + activeDb + "&bucket=" + bName));
                item.add(new Label("bucketNavName", bName));
                item.add(new Label("bucketNavActiveBadge", bName.equalsIgnoreCase(activeBucket) ? "◀" : ""));
            }
        });

        // Search Form (READ)
        Form<Void> searchForm = new Form<>("recSearchForm");
        searchForm.method("GET");
        TextField<String> searchField = new TextField<>("recSearchInput", Model.of(searchQ));
        searchField.placeholder("Filtrar por ID o texto en JSON...");
        searchForm.add(searchField);
        searchForm.add(Button.of("btnSearchRec", "Buscar", () -> {
            String q = searchField.getModelObject() != null ? searchField.getModelObject().toString().trim() : "";
            redirect("/records?db=" + activeDb + "&bucket=" + activeBucket + "&q=" + q);
        }).variant(Button.Variant.BLUE));
        add(searchForm);

        add(Link.of("linkClearRecSearch", "/records?db=" + activeDb + "&bucket=" + activeBucket));

        // Records List (READ)
        List<RecordItem> records = service.getRecords(activeDb, activeBucket, searchQ, 0, 100);
        add(new Label("recordsCountBadge", String.valueOf(records.size())));

        add(new ListView<RecordItem>("recordRows", Model.of(records)) {
            @Override
            protected void populateItem(ListItem<RecordItem> item) {
                RecordItem r = item.getModelObject();
                item.add(new Label("recId", r.id()));
                item.add(new Label("recBucket", r.bucket()));
                item.add(new Label("recPayload", r.preview()));
                item.add(new Label("recType", r.type()));

                item.add(Link.of("linkEditRec", "/records?action=edit&db=" + activeDb + "&bucket=" + activeBucket + "&id=" + r.id()));
                item.add(Link.of("linkDeleteRec", "/records?action=delete&db=" + activeDb + "&bucket=" + activeBucket + "&id=" + r.id()));
            }
        });

        // CREATE: Form to Insert Record
        Form<Void> insertForm = new Form<>("insertRecordForm");
        insertForm.method("POST");
        insertForm.action("/records?db=" + activeDb + "&bucket=" + activeBucket);

        TextField<String> idInput = new TextField<>("newRecIdInput", Model.of(insertId));
        idInput.placeholder("ID único (ej: CUST-901)").required(true);
        insertForm.add(idInput);

        TextField<String> jsonInput = new TextField<>("newRecJsonInput", Model.of(insertJson));
        jsonInput.placeholder("JSON payload (ej: {\"name\":\"Demo Corp\"})").required(true);
        insertForm.add(jsonInput);

        insertForm.add(Button.of("btnInsertRec", "+ Insertar Registro", () -> {
            String rid = idInput.getModelObject() != null ? idInput.getModelObject().toString().trim() : "";
            String raw = jsonInput.getModelObject() != null ? jsonInput.getModelObject().toString().trim() : "";

            if (rid.isBlank() || raw.isBlank()) {
                feedback.error("El ID y el Payload del registro son obligatorios.");
                return;
            }

            service.insertRecord(activeDb, activeBucket, rid, raw);
            feedback.info("✓ Registro '" + rid + "' insertado con éxito en " + activeDb + "/" + activeBucket + ".");
            redirect("/records?db=" + activeDb + "&bucket=" + activeBucket);
        }).variant(Button.Variant.LIME));

        add(insertForm);

        // UPDATE: Form to Edit Record
        Form<Void> updateForm = new Form<>("updateRecordForm");
        updateForm.method("POST");
        updateForm.action("/records?db=" + activeDb + "&bucket=" + activeBucket);

        TextField<String> editIdInput = new TextField<>("editRecIdInput", Model.of(editId));
        editIdInput.placeholder("ID del registro a editar").required(true);
        updateForm.add(editIdInput);

        TextField<String> editJsonInput = new TextField<>("editRecJsonInput", Model.of(editJson));
        editJsonInput.placeholder("Nuevo payload JSON").required(true);
        updateForm.add(editJsonInput);

        updateForm.add(Button.of("btnUpdateRec", "✏️ Actualizar Registro", () -> {
            String uid = editIdInput.getModelObject() != null ? editIdInput.getModelObject().toString().trim() : "";
            String ujson = editJsonInput.getModelObject() != null ? editJsonInput.getModelObject().toString().trim() : "";

            if (uid.isBlank() || ujson.isBlank()) {
                feedback.error("Debe indicar el ID y el nuevo JSON para actualizar.");
                return;
            }

            service.updateRecord(activeDb, activeBucket, uid, ujson);
            feedback.info("✓ Registro '" + uid + "' actualizado correctamente.");
            redirect("/records?db=" + activeDb + "&bucket=" + activeBucket);
        }).variant(Button.Variant.BLUE));

        add(updateForm);
    }
}
