package io.jettra.examples.studio.pages;

import io.jettra.studio.components.Alert;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.Card;
import io.jettra.studio.components.CheckBox;
import io.jettra.studio.components.FeedbackPanel;
import io.jettra.studio.components.Image;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.Modal;
import io.jettra.studio.components.MultiLineLabel;
import io.jettra.studio.components.Select;
import io.jettra.studio.components.TextArea;
import io.jettra.studio.components.TextField;
import io.jettra.studio.core.BasePage;
import io.jettra.studio.core.Form;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.Model;
import io.jettra.studio.model.PropertyModel;

import java.util.List;

/**
 * Complete Showcase Page demonstrating every single JettraStudio component.
 */
public class ComponentsShowcasePage extends BasePage {

    // Simple bean for Form binding
    public static class UserProfile {
        private String username = "AlexDeveloper";
        private String email = "alex@jettra.io";
        private String bio = "Desarrollador Java 25 apasionado por arquitecturas modernas y Web reactive.";
        private String role = "Arquitecto Cloud";
        private boolean notificationsEnabled = true;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public boolean isNotificationsEnabled() { return notificationsEnabled; }
        public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    }

    private UserProfile profile;

    public ComponentsShowcasePage() {
        this(new PageParameters());
    }

    public ComponentsShowcasePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        if (this.profile == null) {
            this.profile = new UserProfile();
        }
        setPageTitle("JettraStudio - Catálogo Completo de Componentes");

        // --- 1. Tipografía y Labels ---
        add(new Label("showcaseTitle", "Catálogo Interactivo de Componentes"));
        add(new Label("simpleLabel", "Label estándar con escape automático de HTML contra inyecciones XSS."));
        add(new MultiLineLabel("multilineLabel",
            "Primera línea de texto descriptivo.\n"
            + "Segunda línea renderizada automáticamente con etiquetas <br/>.\n"
            + "Tercera línea con soporte tipográfico moderno."));

        // --- 2. Botones y Variantes ---
        add(Button.of("btnGold", "Gold Button").variant(Button.Variant.GOLD));
        add(Button.of("btnBlue", "Blue Button").variant(Button.Variant.BLUE));
        add(Button.of("btnLime", "Lime Button").variant(Button.Variant.LIME));
        add(Button.of("btnRed", "Red Button").variant(Button.Variant.RED));
        add(Button.of("btnPurple", "Purple Button").variant(Button.Variant.PURPLE));
        add(Button.of("btnDark", "Dark Button").variant(Button.Variant.DARK));
        add(Button.of("btnPrimary", "Primary Button").variant(Button.Variant.PRIMARY));

        // --- 3. Alertas Contextuales ---
        add(Alert.success("alertSuccess", "Operación completada exitosamente sin advertencias."));
        add(Alert.info("alertInfo", "Recuerda que los temas visuales cambian dinámicamente con el selector superior."));
        add(Alert.warning("alertWarning", "El servidor requiere revisión de índices de memoria en JettraStore."));
        add(Alert.danger("alertDanger", "Se detectó un intento de acceso no autenticado en el clúster."));

        // --- 4. Enlaces e Imágenes ---
        add(Link.of("linkHome", "/"));
        add(Link.of("linkCatalog", "/catalog"));
        add(Link.of("linkDocs", "https://github.com/avbravo"));

        add(Image.of("imgLogo", "https://raw.githubusercontent.com/avbravo/jettra/main/art/jettra-banner.png")
            .alt("Jettra Banner"));

        // --- 5. Modal Dialog ---
        Modal demoModal = Modal.of("demoModal", "Ventana Emergente - JettraStudio Modal");
        add(demoModal);
        add(Button.of("btnOpenModal", "Abrir Diálogo Modal", demoModal::open)
            .variant(Button.Variant.PURPLE));

        // --- 6. Formulario con Modelos Reactivos y FeedbackPanel ---
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        add(feedback);

        Form<UserProfile> userForm = new Form<>("userForm", Model.of(profile));
        userForm.method("POST");

        userForm.add(new TextField<>("usernameInput", PropertyModel.of(profile, "username"))
            .placeholder("Nombre de usuario")
            .required(true));

        userForm.add(new TextField<>("emailInput", PropertyModel.of(profile, "email"))
            .placeholder("correo@ejemplo.com")
            .required(true));

        userForm.add(new TextArea<>("bioInput", PropertyModel.of(profile, "bio"))
            .rows(3).cols(40));

        userForm.add(new Select<>("roleSelect", PropertyModel.of(profile, "role"),
            List.of("Desarrollador Java", "Arquitecto Cloud", "Especialista DevOps", "Data Engineer")));

        userForm.add(new CheckBox("notificationsCheck", PropertyModel.of(profile, "notificationsEnabled")));

        userForm.add(Button.of("btnSubmitForm", "Guardar Perfil", () -> {
            feedback.info("¡Perfil de " + profile.getUsername() + " actualizado correctamente!");
        }).variant(Button.Variant.GOLD));

        add(userForm);

        // --- 7. Tarjetas (Cards) ---
        add(Card.of("cardFeature1", "Tipado Fuerte")
            .subtitle("Componentes orientados a objetos con validación estricta."));
        add(Card.of("cardFeature2", "Arquitectura Autónoma")
            .subtitle("Diseño desacoplado y nativo sin dependencias externas."));
        add(Card.of("cardFeature3", "Virtual Threads")
            .subtitle("Máximo rendimiento con soporte nativo de Project Loom."));
    }
}
