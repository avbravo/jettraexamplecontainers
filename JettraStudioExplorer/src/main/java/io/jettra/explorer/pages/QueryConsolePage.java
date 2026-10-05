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

/**
 * Consola de Consultas JettraSQL & JettraQL en tiempo real.
 * Permite ejecutar consultas directas contra los motores multimodelos
 * (Document, Vector, Key-Value, TimeSeries, Graph) de JettraStore.
 */
@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class QueryConsolePage extends ExplorerTemplatePage {

    private String activeDb = "ecommerce_db";
    private String activeBucket = "customers";
    private String queryText = "";

    public QueryConsolePage() {
        this(new PageParameters());
    }

    public QueryConsolePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Consola JettraSQL / JettraQL - JettraStore Explorer");
        super.onInitialize();

        if (this.activeDb == null) this.activeDb = "ecommerce_db";
        if (this.activeBucket == null) this.activeBucket = "customers";
        if (this.queryText == null) this.queryText = "";

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("queryFeedback");
        add(feedback);

        String pDb = getPageParameters().get("db");
        String pBucket = getPageParameters().get("bucket");
        String pQ = getPageParameters().get("q");

        if (pDb != null && !pDb.isBlank()) this.activeDb = pDb.trim();
        if (pBucket != null && !pBucket.isBlank()) this.activeBucket = pBucket.trim();
        if (pQ != null) this.queryText = pQ.trim();

        add(new Label("queryTargetInfo", activeDb + " ➔ " + activeBucket));

        // Available Databases Switcher
        List<String> dbs = service.getDatabaseNames();
        add(new ListView<String>("queryDbList", Model.of(dbs)) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String d = item.getModelObject();
                item.add(Link.of("linkQueryDb", "/query?db=" + d));
                item.add(new Label("queryDbName", d));
                item.add(new Label("queryDbBadge", d.equalsIgnoreCase(activeDb) ? "◀" : ""));
            }
        });

        // Available Buckets Switcher
        List<String> buckets = service.getBucketNames(activeDb);
        add(new ListView<String>("queryBucketList", Model.of(buckets)) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String b = item.getModelObject();
                item.add(Link.of("linkQueryBucket", "/query?db=" + activeDb + "&bucket=" + b));
                item.add(new Label("queryBucketName", b));
                item.add(new Label("queryBucketBadge", b.equalsIgnoreCase(activeBucket) ? "◀" : ""));
            }
        });

        // Query Form
        Form<Void> form = new Form<>("queryConsoleForm");
        form.method("GET");

        TextField<String> qField = new TextField<>("queryInputField", Model.of(queryText));
        qField.placeholder("Ej: SELECT * WHERE tier == 'VIP' o CUST o emisor == 'Alfa'");
        form.add(qField);

        form.add(Button.of("btnRunSql", "⚡ Ejecutar JettraSQL", () -> {
            String q = qField.getModelObject() != null ? qField.getModelObject().toString().trim() : "";
            redirect("/query?db=" + activeDb + "&bucket=" + activeBucket + "&q=" + q + "&engine=sql");
        }).variant(Button.Variant.BLUE));

        form.add(Button.of("btnRunQl", "🚀 Ejecutar JettraQL", () -> {
            String q = qField.getModelObject() != null ? qField.getModelObject().toString().trim() : "";
            redirect("/query?db=" + activeDb + "&bucket=" + activeBucket + "&q=" + q + "&engine=ql");
        }).variant(Button.Variant.LIME));

        add(form);

        // Execute Query
        boolean isSql = !"ql".equalsIgnoreCase(getPageParameters().get("engine"));
        StoreClusterService.QueryResult qRes = service.executeQuery(activeDb, activeBucket, queryText, isSql);
        add(new Label("queryTelemetryBadge", qRes.message()));

        // Query Results Table
        add(new ListView<RecordItem>("queryResultRows", Model.of(qRes.records())) {
            @Override
            protected void populateItem(ListItem<RecordItem> item) {
                RecordItem r = item.getModelObject();
                item.add(new Label("resId", r.id()));
                item.add(new Label("resBucket", r.bucket()));
                item.add(new Label("resPayload", r.preview()));
                item.add(new Label("resType", r.type()));
                item.add(Link.of("linkResEdit", "/records?action=edit&db=" + activeDb + "&bucket=" + activeBucket + "&id=" + r.id()));
            }
        });
    }
}
