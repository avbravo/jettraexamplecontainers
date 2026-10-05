package io.jettra.explorer.pages;

import io.jettra.explorer.model.IndexOverview;
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

@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class IndexesPage extends ExplorerTemplatePage {

    private String formDb = "ecommerce_db";
    private String formBucket = "customers";
    private String formIndexName = "";
    private String formIndexType = "BTree";
    private String formField = "";

    public IndexesPage() {
        this(new PageParameters());
    }

    public IndexesPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Administrador de Índices - JettraStore");
        super.onInitialize();
        if (this.formDb == null) this.formDb = "ecommerce_db";
        if (this.formBucket == null) this.formBucket = "customers";
        if (this.formIndexType == null) this.formIndexType = "BTree";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("indexFeedback");
        add(feedback);

        String action = getPageParameters().get("action");
        String targetIdx = getPageParameters().get("idx");
        if ("drop".equalsIgnoreCase(action) && targetIdx != null) {
            boolean dropped = service.dropIndex(targetIdx);
            if (dropped) {
                feedback.info("✓ Índice " + targetIdx + " eliminado exitosamente.");
            }
        }

        List<IndexOverview> indexList = service.getIndexes(null);
        add(new ListView<IndexOverview>("indexRows", Model.of(indexList)) {
            @Override
            protected void populateItem(ListItem<IndexOverview> item) {
                IndexOverview idx = item.getModelObject();
                item.add(new Label("idxDb", idx.databaseName()));
                item.add(new Label("idxBucket", idx.bucketName()));
                item.add(new Label("idxName", idx.indexName()));
                item.add(new Label("idxType", idx.indexType()));
                item.add(new Label("idxField", idx.targetField()));
                item.add(new Label("idxUnique", idx.unique() ? "SÍ (UNIQUE)" : "NO"));
                item.add(new Label("idxEntries", String.valueOf(idx.entriesCount())));

                item.add(Link.of("linkDropIdx", "/indexes?action=drop&idx=" + idx.indexName()));
            }
        });

        // Form to Create Index
        Form<Void> createIndexForm = new Form<>("createIndexForm");
        createIndexForm.method("POST");
        createIndexForm.action("/indexes");

        TextField<String> dbInput = new TextField<>("idxDbInput", Model.of(formDb));
        createIndexForm.add(dbInput);

        TextField<String> bucketInput = new TextField<>("idxBucketInput", Model.of(formBucket));
        createIndexForm.add(bucketInput);

        TextField<String> nameInput = new TextField<>("idxNameInput", Model.of(formIndexName));
        nameInput.placeholder("ej: idx_customers_email").required(true);
        createIndexForm.add(nameInput);

        TextField<String> typeInput = new TextField<>("idxTypeInput", Model.of(formIndexType));
        typeInput.placeholder("BTree, Hash, Vector HNSW, FullText");
        createIndexForm.add(typeInput);

        TextField<String> fieldInput = new TextField<>("idxFieldInput", Model.of(formField));
        fieldInput.placeholder("Campo a indexar (ej: email, precio)").required(true);
        createIndexForm.add(fieldInput);

        createIndexForm.add(Button.of("btnCreateIndex", "+ Construir Índice", () -> {
            String db = dbInput.getModelObject() != null ? dbInput.getModelObject().toString().trim() : "ecommerce_db";
            String b = bucketInput.getModelObject() != null ? bucketInput.getModelObject().toString().trim() : "customers";
            String n = nameInput.getModelObject() != null ? nameInput.getModelObject().toString().trim() : "";
            String t = typeInput.getModelObject() != null ? typeInput.getModelObject().toString().trim() : "BTree";
            String f = fieldInput.getModelObject() != null ? fieldInput.getModelObject().toString().trim() : "";

            if (n.isBlank() || f.isBlank()) {
                feedback.error("El nombre y el campo a indexar son requeridos.");
                return;
            }
            service.createIndex(db, b, n, t, f, false);
            feedback.info("✓ Índice " + n + " (" + t + ") creado en " + db + "." + b + ".");
            redirect("/indexes");
        }).variant(Button.Variant.LIME));

        add(createIndexForm);
    }
}
