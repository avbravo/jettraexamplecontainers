package io.jettra.examples.studio.pages;

import io.jettra.studio.components.Button;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.core.BasePage;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.i18n.LanguageStudio;
import io.jettra.studio.i18n.LocalizationManager;
import io.jettra.studio.markup.HtmlResourceLoader;
import io.jettra.studio.model.ResourceModel;

/**
 * Master Template Page providing the 4-quadrant layout:
 * - TOP: Search, notifications, language switcher, theme selector, and user profile menu.
 * - LEFT: Sidebar with logo, navigation links, and categories.
 * - CENTER: Injected child content via <jettras:child/>.
 * - FOOTER: Footer with copyright and version.
 */
public abstract class TemplatePage extends BasePage {

    private String currentUser = "Admin";
    private String currentRole = "ADMIN";

    public TemplatePage() {
        this(new PageParameters());
    }

    public TemplatePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (this.currentUser == null) {
            this.currentUser = "Admin";
        }
        if (this.currentRole == null) {
            this.currentRole = "ADMIN";
        }

        // Check language switch query param
        String changeLang = getPageParameters().get("change_lang");
        if (changeLang != null) {
            if ("en".equalsIgnoreCase(changeLang)) {
                LocalizationManager.getInstance().setCurrentLanguage(LanguageStudio.EN);
                addCookie("jettra_language", "en", "/");
            } else if ("es".equalsIgnoreCase(changeLang)) {
                LocalizationManager.getInstance().setCurrentLanguage(LanguageStudio.ES);
                addCookie("jettra_language", "es", "/");
            }
        }

        // TOP Components
        add(new Label("topTitle", getPageTitle() != null ? getPageTitle() : "JettraStudio Dashboard"));
        add(new Label("userDisplayName", currentUser));
        add(new Label("userRoleBadge", currentRole));
        add(new Label("userAvatarInitial", currentUser.substring(0, 1).toUpperCase()));

        add(Link.of("langEs", "?change_lang=es"));
        add(Link.of("langEn", "?change_lang=en"));

        // LEFT Sidebar Navigation Links
        add(Link.of("navDashboard", "/dashboard"));
        add(Link.of("navCatalog", "/catalog"));
        add(Link.of("navCrud", "/crud"));
        add(Link.of("navComponents", "/components"));
        add(Link.of("navLogout", "/login?logout=true"));

        // FOOTER
        add(new Label("footerText", ResourceModel.of("footer.copyright")));
    }

    @Override
    protected String loadMarkupForPage() {
        // 1. Load child page markup
        String childMarkup = (getCustomMarkup() != null)
            ? getCustomMarkup()
            : HtmlResourceLoader.getInstance().loadMarkup(this.getClass());

        // 2. Load TemplatePage.html base layout
        String baseMarkup = HtmlResourceLoader.getInstance().loadMarkup(TemplatePage.class);
        if (baseMarkup == null || baseMarkup.isBlank()) {
            baseMarkup = super.loadMarkupForPage();
        }

        // 3. Inject child markup into <jettras:child/>
        if (baseMarkup != null && childMarkup != null) {
            return baseMarkup.replaceAll(
                "<(?:jettras:child|jettrat:child|jettra:child)\\s*(?:/>|>.*?</(?:jettras:child|jettrat:child|jettra:child)>)",
                java.util.regex.Matcher.quoteReplacement(childMarkup)
            );
        }
        return baseMarkup;
    }
}
