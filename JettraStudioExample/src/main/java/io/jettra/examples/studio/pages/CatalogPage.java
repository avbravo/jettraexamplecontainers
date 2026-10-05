package io.jettra.examples.studio.pages;

import io.jettra.examples.studio.model.Product;
import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.FeedbackPanel;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.ListItem;
import io.jettra.studio.components.ListView;
import io.jettra.studio.components.Modal;
import io.jettra.studio.components.Table;
import io.jettra.studio.components.TextField;
import io.jettra.studio.core.BasePage;
import io.jettra.studio.core.Form;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Catalog & Inventory Management Page demonstrating:
 * - ListView<Product> with dynamic ListItem bindings.
 * - Table<Product> tabular representation.
 * - Modal dialogs.
 * - Form submission for inserting new records.
 */
import io.jettra.studio.security.Secured;

@Secured(roles = {"ADMIN", "MANAGER"}, loginUrl = "/login")
public class CatalogPage extends BasePage {

    private List<Product> products;

    private String newProdName = "";
    private String newProdCategory = "General";
    private double newProdPrice = 100.0;
    private int newProdStock = 10;

    public CatalogPage() {
        this(new PageParameters());
    }

    public CatalogPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        if (this.products == null) {
            this.products = new ArrayList<>(List.of(
                new Product("PROD-101", "Servidor Edge Jettra", "Infraestructura", 15, 1250.00, true),
                new Product("PROD-102", "Licencia JettraStore Enterprise", "Software", 99, 499.99, true),
                new Product("PROD-103", "Gateway gRPC Loom", "Conectividad", 42, 280.50, true),
                new Product("PROD-104", "Sensor IoT Industrial", "Hardware", 8, 85.00, false),
                new Product("PROD-105", "Módulo de IA Vectorial", "Analítica", 27, 850.00, true)
            ));
        }
        setPageTitle("JettraStudio - Catálogo e Inventario de Productos");

        add(Link.of("linkHome", "/"));
        add(Link.of("linkShowcase", "/components"));

        add(new Label("catalogTitle", "Inventario Central y Catálogo de Productos"));
        add(Alert.info("catalogAlert", "Gestión reactiva en tiempo real sobre Java 25 Records y ListView."));

        FeedbackPanel feedback = new FeedbackPanel("catalogFeedback");
        add(feedback);

        // Modal para confirmaciones o detalles
        Modal detailsModal = Modal.of("detailsModal", "Detalle de Producto");
        add(detailsModal);

        // ListView dinámico para renderizar las filas de productos
        add(new ListView<Product>("productRows", Model.of(products)) {
            @Override
            protected void populateItem(ListItem<Product> item) {
                Product p = item.getModelObject();
                item.add(new Label("prodId", p.id()));
                item.add(new Label("prodName", p.name()));
                item.add(new Label("prodCategory", p.category()));
                item.add(new Label("prodStock", String.valueOf(p.stock())));
                item.add(new Label("prodPrice", p.getFormattedPrice()));
                item.add(new Label("prodStatus", p.getStatusBadge()));

                item.add(Button.of("btnBuy", "Adquirir", () -> {
                    feedback.info("¡Orden generada para el producto " + p.name() + " (" + p.getFormattedPrice() + ")!");
                }).variant(Button.Variant.GOLD));

                item.add(Button.of("btnView", "Detalles", () -> {
                    detailsModal.open();
                }).variant(Button.Variant.BLUE));
            }
        });

        // Formulario para añadir nuevo producto
        Form<Void> addForm = new Form<>("addForm");
        addForm.method("POST");

        addForm.add(new TextField<>("newNameInput", Model.of(newProdName))
            .placeholder("Nombre del producto")
            .required(true));

        addForm.add(new TextField<>("newCategoryInput", Model.of(newProdCategory))
            .placeholder("Categoría"));

        addForm.add(Button.of("btnAddProduct", "+ Agregar al Inventario", () -> {
            feedback.info("Simulación: Producto registrado en el catálogo central.");
        }).variant(Button.Variant.LIME));

        add(addForm);
    }
}
