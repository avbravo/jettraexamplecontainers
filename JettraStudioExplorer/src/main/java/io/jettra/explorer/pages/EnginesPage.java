package io.jettra.explorer.pages;

import io.jettra.explorer.model.EngineOverview;
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
public class EnginesPage extends ExplorerTemplatePage {

    private String currentDb = "ecommerce_db";
    private String newEngineType = "DOCUMENT";
    private String newBucketName = "";

    public EnginesPage() {
        this(new PageParameters());
    }

    public EnginesPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Motores de Almacenamiento - JettraStore");
        super.onInitialize();
        if (this.currentDb == null) this.currentDb = "ecommerce_db";
        if (this.newEngineType == null) this.newEngineType = "DOCUMENT";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("engineFeedback");
        add(feedback);

        String paramDb = getPageParameters().get("db");
        if (paramDb != null && !paramDb.isBlank()) {
            this.currentDb = paramDb.trim();
        }

        add(new Label("currentDbLabel", currentDb));

        List<EngineOverview> engines = service.getEngines(currentDb);
        add(new ListView<EngineOverview>("engineRows", Model.of(engines)) {
            @Override
            protected void populateItem(ListItem<EngineOverview> item) {
                EngineOverview eo = item.getModelObject();
                item.add(new Label("engineType", eo.engineType()));
                item.add(new Label("bucketName", eo.bucketName()));
                item.add(new Label("recordCount", String.valueOf(eo.recordCount())));
                item.add(new Label("engineStatus", eo.status()));

                item.add(Link.of("linkViewRecords", "/records?db=" + eo.databaseName() + "&bucket=" + eo.bucketName()));
            }
        });

        // Form to Create Bucket in Engine
        Form<Void> addBucketForm = new Form<>("addBucketForm");
        addBucketForm.method("POST");
        addBucketForm.action("/engines?db=" + currentDb);

        TextField<String> typeField = new TextField<>("engineTypeInput", Model.of(newEngineType));
        typeField.placeholder("DOCUMENT, KEY-VALUE, VECTOR, GRAPH, etc.");
        addBucketForm.add(typeField);

        TextField<String> bucketField = new TextField<>("bucketNameInput", Model.of(newBucketName));
        bucketField.placeholder("Nombre de la colección / bucket").required(true);
        addBucketForm.add(bucketField);

        addBucketForm.add(Button.of("btnAddBucket", "+ Registrar Bucket", () -> {
            String bName = bucketField.getModelObject() != null ? bucketField.getModelObject().toString().trim() : "";
            String eType = typeField.getModelObject() != null ? typeField.getModelObject().toString().trim() : "DOCUMENT";
            if (bName.isBlank()) {
                feedback.error("El nombre del bucket es requerido.");
                return;
            }
            service.createEngineBucket(currentDb, eType, bName);
            feedback.info("✓ Bucket " + bName + " agregado al motor " + eType + ".");
            redirect("/engines?db=" + currentDb);
        }).variant(Button.Variant.LIME));

        add(addBucketForm);
    }
}
