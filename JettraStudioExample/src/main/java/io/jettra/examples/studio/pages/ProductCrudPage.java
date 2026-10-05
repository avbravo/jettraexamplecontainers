package io.jettra.examples.studio.pages;

import io.jettra.examples.studio.model.Product;
import io.jettra.examples.studio.model.ProductRepository;
import io.jettra.flux.widgets.StatCard;
import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.FeedbackPanel;
import io.jettra.studio.components.FluxWidget;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.ListItem;
import io.jettra.studio.components.ListView;
import io.jettra.studio.components.Modal;
import io.jettra.studio.components.TextField;
import io.jettra.studio.core.Form;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.Model;
import io.jettra.studio.security.Secured;

import java.util.List;
import java.util.Optional;

/**
 * Complete CRUD Management Page for Products in JettraStudio.
 * Demonstrates:
 * - Page-level security via @Secured(roles = {"ADMIN", "MANAGER"}).
 * - Direct integration of JettraFlux Widget via FluxWidget.
 * - Create, Read (List & Search), Update, and Delete operations.
 * - Dynamic Form submission and FeedbackPanel notification alerts.
 * - Modal dialog for record inspection.
 */
@Secured(roles = {"ADMIN", "MANAGER"}, loginUrl = "/login")
public class ProductCrudPage extends TemplatePage {

    private String searchQuery = "";
    private String formId = "";
    private String formName = "";
    private String formCategory = "Software";
    private String formStock = "10";
    private String formPrice = "99.99";
    private boolean formActive = true;
    private String formMode = "create"; // create or edit

    public ProductCrudPage() {
        this(new PageParameters());
    }

    public ProductCrudPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Gestión CRUD de Productos - JettraStudio");
        super.onInitialize();

        FeedbackPanel feedback = new FeedbackPanel("crudFeedback");
        add(feedback);

        ProductRepository repo = ProductRepository.getInstance();

        // 1. Process Actions from Query Parameters (Delete, Edit, Search)
        String action = getPageParameters().get("action");
        String targetId = getPageParameters().get("id");
        String q = getPageParameters().get("q");

        if (q != null) {
            this.searchQuery = q.trim();
        }

        if ("delete".equalsIgnoreCase(action) && targetId != null && !targetId.isBlank()) {
            boolean removed = repo.delete(targetId);
            if (removed) {
                feedback.info("✓ Producto " + targetId + " eliminado exitosamente del catálogo.");
            } else {
                feedback.warning("⚠ No se encontró el producto " + targetId + " para eliminar.");
            }
        } else if ("edit".equalsIgnoreCase(action) && targetId != null && !targetId.isBlank()) {
            Optional<Product> opt = repo.findById(targetId);
            if (opt.isPresent()) {
                Product p = opt.get();
                this.formId = p.id();
                this.formName = p.name();
                this.formCategory = p.category();
                this.formStock = String.valueOf(p.stock());
                this.formPrice = String.valueOf(p.price());
                this.formActive = p.active();
                this.formMode = "edit";
                feedback.info("ℹ Editando producto: " + p.id() + " - " + p.name());
            }
        }

        // 2. Integration of JettraFlux Widget via FluxWidget
        int totalProducts = repo.count();
        StatCard fluxStat = StatCard.of("Catálogo Loom CRUD", "Total Activos", totalProducts + " Items", true);
        add(FluxWidget.of("fluxProductStat", fluxStat));

        add(new Label("crudTitle", "Administración Integral de Productos"));
        add(new Label("crudSubtitle", "CRUD reactivo con Virtual Threads, persistencia en memoria y FluxWidget"));
        add(new Label("totalCountBadge", totalProducts + " Registros"));

        // 3. Search Form
        Form<Void> searchForm = new Form<>("searchForm");
        searchForm.method("GET");
        TextField<String> searchInput = new TextField<>("searchInput", Model.of(searchQuery));
        searchInput.placeholder("Buscar por ID, nombre o categoría...");
        searchForm.add(searchInput);
        searchForm.add(Button.of("btnSearch", "Filtrar", () -> {
            Object obj = searchInput.getModelObject();
            String val = obj != null ? obj.toString().trim() : "";
            redirect("/crud?q=" + val);
        }).variant(Button.Variant.BLUE));
        add(searchForm);

        add(Link.of("linkClearFilter", "/crud"));

        // 4. Details Modal
        Modal detailsModal = Modal.of("detailsModal", "Detalle Completo del Producto");
        add(detailsModal);

        // 5. Product List (ListView)
        List<Product> displayedProducts = (searchQuery != null && !searchQuery.isBlank())
            ? repo.search(searchQuery)
            : repo.findAll();

        add(new ListView<Product>("productRows", Model.of(displayedProducts)) {
            @Override
            protected void populateItem(ListItem<Product> item) {
                Product p = item.getModelObject();
                item.add(new Label("rowId", p.id()));
                item.add(new Label("rowName", p.name()));
                item.add(new Label("rowCategory", p.category()));
                item.add(new Label("rowStock", String.valueOf(p.stock())));
                item.add(new Label("rowPrice", p.getFormattedPrice()));
                item.add(new Label("rowStatus", p.getStatusBadge()));

                item.add(Link.of("linkEdit", "/crud?action=edit&id=" + p.id()));
                item.add(Link.of("linkDelete", "/crud?action=delete&id=" + p.id()));
                item.add(Button.of("btnDetail", "Ver", () -> {
                    detailsModal.open();
                }).variant(Button.Variant.BLUE));
            }
        });

        // 6. Create / Update Form
        Form<Void> crudForm = new Form<>("crudForm");
        crudForm.method("POST");
        crudForm.action("/crud");

        TextField<String> idField = new TextField<>("formIdInput", Model.of(formId));
        idField.placeholder("Ej: PROD-106").required(true);
        crudForm.add(idField);

        TextField<String> nameField = new TextField<>("formNameInput", Model.of(formName));
        nameField.placeholder("Nombre del producto").required(true);
        crudForm.add(nameField);

        TextField<String> catField = new TextField<>("formCategoryInput", Model.of(formCategory));
        catField.placeholder("Categoría (ej: Hardware, Software)");
        crudForm.add(catField);

        TextField<String> stockField = new TextField<>("formStockInput", Model.of(formStock));
        stockField.placeholder("Stock disponible");
        crudForm.add(stockField);

        TextField<String> priceField = new TextField<>("formPriceInput", Model.of(formPrice));
        priceField.placeholder("Precio USD");
        crudForm.add(priceField);

        crudForm.add(new Label("formModeLabel", "edit".equals(formMode) ? "Modo: Actualización" : "Modo: Nuevo Registro"));

        crudForm.add(Button.of("btnSaveProduct", "edit".equals(formMode) ? "Guardar Cambios" : "Crear Producto", () -> {
            String pId = idField.getModelObject() != null ? idField.getModelObject().toString().trim().toUpperCase() : "";
            String pName = nameField.getModelObject() != null ? nameField.getModelObject().toString().trim() : "";
            String pCat = catField.getModelObject() != null ? catField.getModelObject().toString().trim() : "General";
            int pStock = 0;
            double pPrice = 0.0;

            try {
                if (stockField.getModelObject() != null) {
                    pStock = Integer.parseInt(stockField.getModelObject().toString().trim());
                }
            } catch (Exception ignored) {}

            try {
                if (priceField.getModelObject() != null) {
                    pPrice = Double.parseDouble(priceField.getModelObject().toString().trim());
                }
            } catch (Exception ignored) {}

            if (pId.isBlank() || pName.isBlank()) {
                feedback.error("El ID y el Nombre del producto son obligatorios.");
                return;
            }

            Product newOrUpdated = new Product(pId, pName, pCat, pStock, pPrice, true);
            repo.save(newOrUpdated);
            feedback.info("✓ Producto " + pId + " guardado con éxito.");
            redirect("/crud");
        }).variant(Button.Variant.LIME));

        add(crudForm);
    }
}
