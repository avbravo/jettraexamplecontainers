package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

/**
 * JettraWebDesigner: A visual designer for JettraWUI interfaces.
 */
public class WebDesignerPage extends DashboardBasePage {

    private static String viewModelName = "GenericModel";

    public WebDesignerPage() {
        super("Jettra Web Designer");
    }
    
    @Override
    protected void onPost(java.util.Map<String, String> params) {
        String code = params.get("generated-code-hidden");
        if (code != null && !code.isEmpty()) {
            System.out.println("======= SAVING GENERATED CODE =======");
            System.out.println(code);
            System.out.println("=====================================");
            // In a real production environment, this would write to a .java file
        }
        super.onPost(params);
    }

    @Override
    protected void initCenter(Center center, String username) {
        // Layout: 3 Columns
        // [ Palette (25%) ] | [ Canvas (50%) ] | [ Code View (25%) ]
        
        Form saveForm = new Form("save-form", "/webdesigner");

        Div container = new Div();
        container.setStyle("display", "flex").setStyle("height", "calc(100vh - 120px)").setStyle("gap", "15px");

        // 1. Sidebar (Palette + Project Explorer)
        Div sidebar = new Div();
        sidebar.setProperty("id", "designer-sidebar");
        sidebar.setStyle("flex", "0 0 280px").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "15px");

        Div palette = createCategorizedPalette();
        palette.setStyle("flex", "1").setStyle("background", "rgba(20,30,50,0.8)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("overflow-y", "auto");

        Div projectExplorer = createProjectExplorer();
        projectExplorer.setStyle("flex", "0 0 250px").setStyle("background", "rgba(10,20,30,0.9)").setStyle("border", "1px solid rgba(0,255,255,0.4)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("overflow-y", "auto");

        sidebar.add(palette).add(projectExplorer);

        // 2. Canvas
        Div canvasArea = new Div();
        canvasArea.setProperty("id", "canvas-area");
        canvasArea.setStyle("flex", "1").setStyle("background", "rgba(0,0,0,0.3)").setStyle("border", "2px dashed var(--jettra-accent)").setStyle("border-radius", "8px").setStyle("padding", "20px").setStyle("position", "relative").setStyle("overflow-y", "auto").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Div canvasHeader = new Div();
        canvasHeader.setProperty("id", "canvas-header");
        canvasHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px").setStyle("border-bottom", "1px solid rgba(0,255,255,0.1)").setStyle("padding-bottom", "10px");
        
        Header canvasTitle = new Header(4, "View");
        canvasTitle.setProperty("id", "canvas-title-text");
        canvasTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Div canvasHeaderActions = new Div();
        canvasHeaderActions.setStyle("display", "flex").setStyle("gap", "10px");
        
        // Button previewBtn = new Button("Preview \uD83D\uDC41\uFE0F");
        // previewBtn.addClass("j-btn-secondary");
        // previewBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        // previewBtn.setProperty("type", "button");
        // previewBtn.setProperty("onclick", "window.previewInterface()");

        Button crudBtn = new Button("CRUD \u26A1");
        crudBtn.addClass("j-btn-primary");
        crudBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        crudBtn.setProperty("type", "button");
        crudBtn.setProperty("onclick", "generateCRUD()");
        
        Button toggleSidebarBtn = new Button("\u2630"); // Sidebar toggle
        toggleSidebarBtn.addClass("j-btn");
        toggleSidebarBtn.setStyle("font-size", "14px").setStyle("padding", "5px 12px");
        toggleSidebarBtn.setProperty("type", "button");
        toggleSidebarBtn.setProperty("onclick", "window.toggleSidebar()");

        Button toggleCodeBtn = new Button("Code </>");
        toggleCodeBtn.addClass("j-btn");
        toggleCodeBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        toggleCodeBtn.setProperty("type", "button");
        toggleCodeBtn.setProperty("onclick", "window.toggleCodeView()");
        
        Button mobileBtn = new Button("\uD83D\uDCF1 Mobile"); // Phone icon
        mobileBtn.addClass("j-btn");
        mobileBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px").setStyle("border-color", "var(--jettra-accent)");
        mobileBtn.setProperty("type", "button");
        mobileBtn.setProperty("onclick", "window.toggleMobileView()");
        
        canvasHeaderActions.add(toggleSidebarBtn).add(mobileBtn).add(crudBtn).add(toggleCodeBtn);
        canvasHeader.add(canvasTitle).add(canvasHeaderActions);
        canvasArea.add(canvasHeader);

        Div canvasDropArea = new Div();
        canvasDropArea.setProperty("id", "canvas-drop-area");
        canvasDropArea.setProperty("ondragover", "allowDrop(event)");
        canvasDropArea.setProperty("ondrop", "drop(event)");
        canvasDropArea.setStyle("flex", "1").setStyle("display", "flex").setStyle("flex-direction", "column");

        Div canvasPlaceholder = new Div();
        canvasPlaceholder.addClass("canvas-placeholder");
        canvasPlaceholder.setContent("Start dragging components here to design your page");
        canvasPlaceholder.setStyle("color", "rgba(0,255,255,0.2)").setStyle("text-align", "center").setStyle("margin-top", "100px");
        canvasDropArea.add(canvasPlaceholder);

        canvasDropArea.add(new Div().setProperty("id", "mobile-notch").addClass("mobile-notch"));
        canvasArea.add(canvasDropArea);

        // --- NEW: Modal Tools Area ---
        Div modalToolsArea = new Div();
        modalToolsArea.setProperty("id", "modal-tools-area");
        modalToolsArea.setStyle("margin-top", "15px").setStyle("border-top", "1px dashed rgba(0,255,255,0.3)").setStyle("padding-top", "15px").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Header modalToolsTitle = new Header(5, "Modal Tools");
        modalToolsTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0 0 10px 0").setStyle("font-size", "12px").setStyle("text-transform", "uppercase");
        
        Div modalListContainer = new Div();
        modalListContainer.setProperty("id", "modal-list-container");
        modalListContainer.setStyle("display", "flex").setStyle("flex-wrap", "wrap").setStyle("gap", "15px").setStyle("min-height", "60px").setStyle("background", "rgba(0,0,0,0.2)").setStyle("border-radius", "8px").setStyle("padding", "10px").setStyle("border", "1px solid rgba(255,255,255,0.05)");
        
        modalToolsArea.add(modalToolsTitle).add(modalListContainer);
        canvasArea.add(modalToolsArea);
        // -----------------------------

        // 3. Code View
        Div codeView = new Div();
        codeView.setProperty("id", "code-view-container");
        codeView.setStyle("flex", "0 0 350px").setStyle("background", "rgba(10,20,30,0.9)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Header codeHeader = new Header(4, "Java Source Code");
        codeHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "10px");
        
        Div codeContainerWrapper = new Div();
        codeContainerWrapper.setStyle("flex", "1").setStyle("display", "flex").setStyle("flex-direction", "column");

        TextArea codeContainer = new TextArea("generated-code-display", "// Drag components to start generating code...");
        codeContainer.setProperty("id", "generated-code-display");
        codeContainer.setStyle("flex", "1").setStyle("background", "#050a10").setStyle("color", "#a9b7c6").setStyle("padding", "10px").setStyle("font-family", "monospace").setStyle("font-size", "11px").setStyle("white-space", "pre").setStyle("border-radius", "4px").setStyle("border", "1px solid #333").setStyle("overflow", "auto").setStyle("resize", "none").setStyle("outline", "none");
        
        codeContainerWrapper.add(codeContainer);

        // Hidden input to send code to server
        TextBox hiddenCode = new TextBox("generated-code-hidden", "");
        hiddenCode.setProperty("id", "generated-code-hidden");
        hiddenCode.setStyle("display", "none");

        Div actions = new Div();
        actions.setStyle("margin-top", "10px").setStyle("display", "flex").setStyle("gap", "10px");
        
        Button syncBtn = new Button("SYNC CANVAS");
        syncBtn.addClass("j-btn-secondary");
        syncBtn.setProperty("type", "button");
        syncBtn.setProperty("onclick", "window.syncCodeToCanvas()");

        Button saveBtn = new Button("GENERATE CLASS");
        saveBtn.addClass("j-btn-primary");
        saveBtn.setProperty("type", "submit");
        
        Button clearBtn = new Button("CLEAR ALL");
        clearBtn.addClass("j-btn-danger");
        clearBtn.setProperty("type", "button");
        clearBtn.setProperty("onclick", "window.clearDesigner()");
        
        actions.add(syncBtn).add(saveBtn).add(clearBtn);
        codeView.add(codeHeader).add(codeContainerWrapper).add(hiddenCode).add(actions);

        // 4. Property Inspector (Floating/Right Sidebar)
        Div inspector = new Div();
        inspector.setProperty("id", "property-inspector");
        inspector.setStyle("flex", "0 0 250px").setStyle("background", "rgba(20,35,55,0.9)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("display", "none").setStyle("flex-direction", "column");
        
        Div inspectorHeaderWrapper = new Div();
        inspectorHeaderWrapper.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "10px");
        
        Header inspectorHeader = new Header(4, "Properties");
        inspectorHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Span closeInspector = new Span("\u00D7");
        closeInspector.setStyle("color", "#ff4444").setStyle("font-size", "24px").setStyle("cursor", "pointer").setStyle("line-height", "1").setStyle("font-weight", "bold");
        closeInspector.setProperty("onclick", "document.getElementById('property-inspector').style.display='none'");
        
        inspectorHeaderWrapper.add(inspectorHeader).add(closeInspector);
        
        Div propList = new Div();
        propList.setProperty("id", "inspector-properties");
        propList.setStyle("flex", "1");

        inspector.add(inspectorHeaderWrapper).add(propList);

        // --- Add Tool Form (Restored to Palette) ---
        Div addToolPanel = new Div();
        addToolPanel.setStyle("padding", "15px").setStyle("background", "rgba(0,0,0,0.3)").setStyle("border-top", "1px solid var(--jettra-border)").setStyle("margin-top", "10px");
        Header addH = new Header(5, "A\u00F1adir Herramienta");
        addH.setStyle("font-size", "11px").setStyle("margin-bottom", "10px");
        
        TextBox toolNameInput = new TextBox("text", "Nueva...");
        toolNameInput.setId("new-tool-name").addClass("j-input").setStyle("margin-bottom", "10px").setStyle("font-size", "12px");
        
        Button addToolBtn = new Button("A\u00F1adir a Paleta");
        addToolBtn.addClass("j-btn-primary").setStyle("width", "100%").setStyle("font-size", "11px");
        addToolBtn.setProperty("onclick", "addNewToolToPalette()");
        
        addToolPanel.add(addH).add(toolNameInput).add(addToolBtn);
        palette.add(addToolPanel);

        container.add(sidebar).add(canvasArea).add(codeView).add(inspector);
        
        // 5. Event Editor Modal
        Modal eventModal = new Modal("event-editor-modal");
        eventModal.addClass("modal-3d-effect");
        eventModal.setStyle("display", "none").setStyle("background", "linear-gradient(145deg, rgba(30, 50, 80, 0.98), rgba(15, 25, 45, 1))")
                 .setStyle("backdrop-filter", "blur(25px)").setStyle("padding", "40px").setStyle("border-radius", "30px")
                 .setStyle("width", "750px").setStyle("border", "1px solid rgba(0, 255, 255, 0.5)")
                 .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                 .setStyle("box-shadow", "0 40px 100px -20px rgba(0, 0, 0, 0.8), inset 0 2px 2px 0 rgba(255, 255, 255, 0.2), 0 0 30px rgba(0, 255, 255, 0.3)");
        
        Header modalHeader = new Header(3, "\u26A1 Pro Event Handler Editor");
        modalHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("text-shadow", "0 0 15px rgba(0,255,255,0.6)").setStyle("font-weight", "800").setStyle("letter-spacing", "1px");
        
        TextArea codeInput = new TextArea("event-code-input", "e -> { \n    // Write your Java code here\n}");
        codeInput.setStyle("width", "100%").setStyle("height", "350px").setStyle("background", "rgba(0,0,0,0.7)").setStyle("color", "#0ff").setStyle("font-family", "'Fira Code', monospace").setStyle("padding", "20px").setStyle("border", "1px solid rgba(0,255,255,0.3)").setStyle("border-radius", "15px").setStyle("box-shadow", "inset 0 4px 20px rgba(0,0,0,0.7)");
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "15px").setStyle("margin-top", "25px");
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addClass("j-btn");
        cancelBtn.setStyle("border-radius", "8px").setStyle("background", "rgba(255,255,255,0.05)").setStyle("box-shadow", "0 4px 6px rgba(0,0,0,0.2)");
        cancelBtn.setProperty("type", "button");
        cancelBtn.setProperty("onclick", "document.getElementById('event-editor-modal').style.display = 'none'");
        
        Button saveEventBtn = new Button("Save Handler");
        saveEventBtn.addClass("j-btn-primary");
        saveEventBtn.setStyle("border-radius", "8px").setStyle("box-shadow", "0 4px 15px rgba(0,255,255,0.3)");
        saveEventBtn.setProperty("onclick", "saveEventHandler()");
        
        modalActions.add(cancelBtn).add(saveEventBtn);
        eventModal.add(modalHeader).add(codeInput).add(modalActions);
        
        // 6. 3D Confirmation Modal
        Modal confirmModal = new Modal("confirm-3d-modal");
        confirmModal.addClass("modal-3d-effect");
        confirmModal.setStyle("display", "none").setStyle("background", "linear-gradient(135deg, #1e293b, #0f172a)")
                   .setStyle("border", "2px solid rgba(0, 255, 255, 0.6)").setStyle("border-radius", "30px")
                   .setStyle("padding", "50px").setStyle("width", "500px")
                   .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                   .setStyle("box-shadow", "0 50px 100px -20px rgba(0, 0, 0, 0.8), inset 0 2px 2px rgba(255, 255, 255, 0.1), 0 0 40px rgba(0, 255, 255, 0.2)");
        
        Header confirmTitle = new Header(3, "Confirm Action");
        confirmTitle.setProperty("id", "confirm-title");
        confirmTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("text-align", "center");
        
        Paragraph confirmBody = new Paragraph("");
        confirmBody.setProperty("id", "confirm-body");
        confirmBody.setStyle("color", "#cbd5e1").setStyle("text-align", "center").setStyle("font-size", "16px").setStyle("margin", "20px 0");
        
        Div confirmActions = new Div();
        confirmActions.setStyle("display", "flex").setStyle("justify-content", "center").setStyle("gap", "20px").setStyle("margin-top", "30px");
        
        Button confirmNo = new Button("No, Cancel");
        confirmNo.addClass("j-btn");
        confirmNo.setStyle("width", "140px");
        confirmNo.setProperty("onclick", "window.close3DConfirm()");
        
        Button confirmYes = new Button("Yes, Open");
        confirmYes.addClass("j-btn-primary");
        confirmYes.setStyle("width", "140px");
        confirmYes.setProperty("id", "confirm-yes-btn");
        
        confirmActions.add(confirmNo).add(confirmYes);
        confirmModal.add(confirmTitle).add(confirmBody).add(confirmActions);

        // 7. Model Selection Row
        Div modelSelectionRow = new Div();
        modelSelectionRow.setStyle("display", "flex").setStyle("align-items", "center").setStyle("gap", "10px").setStyle("margin-bottom", "15px").setStyle("background", "rgba(0,255,255,0.05)").setStyle("padding", "10px").setStyle("border-radius", "10px").setStyle("border", "1px solid rgba(0,255,255,0.1)");
        
        Label modelLabel = new Label("Selected Model:");
        modelLabel.setStyle("font-size", "12px").setStyle("color", "var(--jettra-accent)");
        
        Div modelSelectContainer = new Div();
        modelSelectContainer.setProperty("id", "model-select-container");
        modelSelectContainer.setStyle("flex", "1");
        
        modelSelectionRow.add(modelLabel).add(modelSelectContainer);
        
        // Wrap everything into the main center
        center.add(eventModal);
        center.add(confirmModal);

        // 8. Preview Modal
        Modal previewModal = new Modal("preview-modal");
        previewModal.setStyle("display", "none").setStyle("background", "linear-gradient(135deg, #0f172a, #1e293b)").setStyle("position", "fixed").setStyle("top", "0").setStyle("left", "0").setStyle("width", "100vw").setStyle("height", "100vh").setStyle("z-index", "9999").setStyle("overflow-y", "auto");
        
        Div previewHeader = new Div();
        previewHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("padding", "15px 30px").setStyle("background", "rgba(0,0,0,0.5)").setStyle("border-bottom", "1px solid rgba(0, 255, 255, 0.2)");
        Header prevTitle = new Header(3, "Interface Preview");
        prevTitle.setStyle("margin", "0").setStyle("color", "var(--jettra-accent)");
        Button closePreviewBtn = new Button("Close Preview \u00D7");
        closePreviewBtn.addClass("j-btn-danger");
        closePreviewBtn.setStyle("font-weight", "bold");
        closePreviewBtn.setProperty("type", "button");
        closePreviewBtn.setProperty("onclick", "document.getElementById('preview-modal').style.display = 'none'");
        previewHeader.add(prevTitle).add(closePreviewBtn);

        Div previewContent = new Div();
        previewContent.setProperty("id", "preview-content-area");
        previewContent.setStyle("padding", "40px").setStyle("max-width", "1200px").setStyle("margin", "0 auto");

        previewModal.add(previewHeader).add(previewContent);
        center.add(previewModal);
        
        saveForm.add(modelSelectionRow).add(container);
        center.add(saveForm);

        // Add Designer Scripts and Styles
        setupDesignerAssets(center);
    }

    private Div createProjectExplorer() {
        Div explorer = new Div();
        
        Div explorerHeaderWrap = new Div();
        explorerHeaderWrap.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "10px");
        
        Header h = new Header(5, "Project Explorer");
        h.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Span clearExplorerBtn = new Span("\uD83D\uDDD1\uFE0F");
        clearExplorerBtn.setStyle("cursor", "pointer").setStyle("font-size", "14px").setStyle("transition", "transform 0.2s").setStyle("opacity", "0.8");
        clearExplorerBtn.setProperty("onclick", "window.clearProjectCache()");
        clearExplorerBtn.setProperty("title", "Limpiar selecci\u00F3n de proyecto");
        
        explorerHeaderWrap.add(h).add(clearExplorerBtn);
        explorer.add(explorerHeaderWrap);

        FolderSelector folderSel = new FolderSelector("fs-explorer");
        folderSel.setReferenceLocation("/").setReferenceContent("Root");
        folderSel.excludeTarget(true).style3D().setConfirmUpload(true, "Explorador de Proyecto", "\u00BFDesea cargar los archivos de esta carpeta?");
        folderSel.setStyle("width", "100%").setStyle("margin-top", "10px").setStyle("margin-bottom", "10px");
        
        // Custom ID for JS hook
        folderSel.setProperty("onchange", "loadFiles(this)");

        Div treeContainer = new Div();
        treeContainer.setProperty("id", "explorer-tree-view");
        treeContainer.setStyle("min-height", "100px");

        explorer.add(folderSel).add(treeContainer);
        
        // Link FolderSelector to project explorer logic
        folderSel.getFolderInput().setProperty("onchange", "window.loadFiles(this)");
        
        return explorer;
    }

    private Div createCategorizedPalette() {
        Div palette = new Div();
        Header h = new Header(4, "Visual Palette");
        h.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px").setStyle("text-align", "center");
        palette.add(h);

        // Typography
        addPaletteCategory(palette, "Typography", new String[]{"Header", "Paragraph", "Span", "Label", "Separator", "Divide"});
        // Forms
        addPaletteCategory(palette, "Forms", new String[]{"FormGroup", "Button", "CheckBox", "CheckBoxGroup", "CreditCard", "RadioButton", "RadioGroupButton", "ScheduleControl", "SelectOne", "SelectMany", "SelectOneIcon", "TextBox", "TextArea", "ToggleSwitch", "FileUpload", "FolderSelector", "OTPValidator", "Catcha"});
        // Date
        addPaletteCategory(palette, "Date", new String[]{"DatePicker", "Time", "Calendar", "Schedule", "Organigram", "Timeline"});
        // Navigation
        addPaletteCategory(palette, "Navigation", new String[]{"Link", "Menu", "MenuBar", "MenuItem"});
        // Feedback
        addPaletteCategory(palette, "Feedback", new String[]{"ProgressBar", "Spinner", "Loading", "Alert", "Notification", "Clock", "TrafficLight"});
        // Media & Files
        addPaletteCategory(palette, "Media", new String[]{"Downloader", "PDFViewer", "ViewMedia", "BarCode", "QRReader", "Draw"});
        // Charts
        addPaletteCategory(palette, "Charts", new String[]{"ChartsBar", "ChartsDoughnut", "ChartsLine", "ChartsPie", "ChartsRadar"});
        // Layout & Display
        addPaletteCategory(palette, "Layout", new String[]{"Grid", "Panel", "Board", "Card", "Avatar", "Carousel", "Datatable", "ViewDataTable", "TabView", "Tab", "Modal", "Tree", "TreeItem", "Div", "LayoutDisplay", "Map", "LoginUI", "IndexUI"});

        return palette;
    }

    private void addPaletteCategory(Div palette, String name, String[] components) {
        Div cat = new Div();
        cat.addClass("palette-category");
        cat.setStyle("margin-bottom", "15px");
        
        Header head = new Header(5, name);
        head.setStyle("color", "#0ff").setStyle("font-size", "12px").setStyle("text-transform", "uppercase").setStyle("border-bottom", "1px solid rgba(0,255,255,0.2)").setStyle("padding-bottom", "5px").setStyle("margin-bottom", "10px");
        cat.add(head);

        Div grid = new Div();
        grid.setStyle("display", "grid").setStyle("grid-template-columns", "1fr 1fr").setStyle("gap", "8px");

        for (String comp : components) {
            Div item = new Div();
            item.addClass("palette-item");
            item.setProperty("draggable", "true");
            item.setProperty("ondragstart", "drag(event)");
            item.setProperty("data-type", comp);
            item.setContent(comp);
            item.setStyle("padding", "6px").setStyle("background", "rgba(0,255,255,0.05)").setStyle("border", "1px solid rgba(0,255,255,0.2)").setStyle("border-radius", "4px").setStyle("cursor", "move").setStyle("color", "#eee").setStyle("font-size", "11px").setStyle("text-align", "center").setStyle("transition", "all 0.2s");
            grid.add(item);
        }
        cat.add(grid);
        palette.add(cat);
    }
    private void setupDesignerAssets(Center center) {
        Style style = new Style("""
            .palette-item:hover { background: rgba(0,255,255,0.2) !important; border-color: #0ff !important; color: #fff !important; transform: scale(1.05); }
            .canvas-item { position: relative; margin-bottom: 20px; border: 1px transparent dashed; transition: border 0.2s; min-height: 20px; padding: 10px; border-radius: 4px; cursor: pointer; }
            .canvas-item:hover { border-color: rgba(0,255,255,0.4); background: rgba(0,255,255,0.02); }
            .canvas-item.selected { border-color: var(--jettra-accent) !important; background: rgba(0,255,255,0.05) !important; box-shadow: 0 0 10px var(--jettra-glow); }
            .canvas-item .delete-tool { position: absolute; top: -8px; right: -8px; background: #ff4444; color: white; width: 18px; height: 18px; border-radius: 50%; display: none; justify-content: center; align-items: center; cursor: pointer; font-size: 10px; z-index: 100; box-shadow: 0 2px 5px rgba(0,0,0,0.5); }
            .canvas-item:hover .delete-tool { display: flex; }
            .inspector-row { margin-bottom: 15px; background: rgba(255,255,255,0.02); padding: 8px; border-radius: 6px; border-left: 3px solid transparent; transition: all 0.3s; }
            .inspector-row:hover { border-left-color: var(--jettra-accent); background: rgba(255,255,255,0.05); }
            .inspector-label { display: block; font-size: 11px; color: #9aa; margin-bottom: 6px; text-transform: uppercase; letter-spacing: 0.5px; }
            .inspector-input { width: 100%; background: #0a0f15; border: 1px solid #334155; color: #fff; padding: 8px; border-radius: 6px; font-size: 13px; outline: none; transition: border-color 0.2s; }
            .inspector-input:focus { border-color: var(--jettra-accent); box-shadow: 0 0 0 1px rgba(0,255,255,0.2); }
            .project-file { padding: 6px 10px; font-size: 12px; color: #94a3b8; cursor: pointer; border-radius: 6px; display: flex; align-items: center; gap: 10px; transition: all 0.2s; }
            .project-file:hover { background: rgba(0,255,255,0.1); color: #fff; transform: translateX(5px); }
            .tree-node { padding: 4px 8px; transition: all 0.2s; border-radius: 4px; }
            .tree-node:hover { background: rgba(0,255,255,0.1); color: #fff !important; }
            .file-page { color: #facc15 !important; font-weight: 600; text-shadow: 0 0 5px rgba(250,204,21,0.2); }
            .file-model { color: #4ade80 !important; font-weight: 600; text-shadow: 0 0 5px rgba(74,222,128,0.2); }
            .canvas-container { border: 2px dashed rgba(255,255,255,0.1); padding: 15px; border-radius: 12px; position: relative; min-height: 80px; transition: all 0.3s; }
            .canvas-container:hover { border-color: var(--jettra-accent); background: rgba(0,255,255,0.02); }
            .view-model-row { margin-bottom: 20px; padding: 12px; background: rgba(0,255,255,0.05); border-radius: 8px; border: 1px solid rgba(0,255,255,0.1); }
            .btn-event { background: linear-gradient(135deg, var(--jettra-accent), #0891b2); color: #000; border: none; padding: 6px 12px; border-radius: 6px; font-size: 11px; weight: 600; cursor: pointer; transition: all 0.2s; display: flex; align-items: center; gap: 5px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.3); }
            .btn-event:hover { transform: translateY(-2px); box-shadow: 0 10px 15px -3px rgba(0,255,255,0.4); }
            .btn-event:active { transform: translateY(0); }
            
            /* Enhanced 3D Styles for Modals */
            .modal-3d-effect {
                transform-style: preserve-3d;
                perspective: 2000px;
                transition: transform 0.1s ease-out, opacity 0.3s ease-out;
                animation: modalDeepAppear 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            }
            @keyframes modalDeepAppear {
                from { 
                    opacity: 0; 
                    transform: translate(-50%, -30%) scale(0.8) rotateX(-25deg); 
                }
                to { 
                    opacity: 1; 
                    transform: translate(-50%, -50%) scale(1) rotateX(0); 
                }
            }
            
            .canvas-container.modal-container-mock {
                background: rgba(30, 50, 80, 0.3);
                border: 2px solid var(--jettra-accent);
                box-shadow: 0 10px 30px rgba(0,0,0,0.5);
                min-height: 120px;
            }
            
            /* 3D Effects for Modals */
            #confirm-3d-modal, #event-editor-modal {
                transform-style: preserve-3d;
                perspective: 1000px;
                transition: transform 0.1s ease-out, opacity 0.3s ease-out;
                animation: modalAppear 0.4s ease-out;
            }
            .modal-content-3d {
                transform: translateZ(50px);
                transform-style: preserve-3d;
            }
            .btn-3d {
                background: linear-gradient(135deg, var(--jettra-accent), #0891b2);
                border: none;
                box-shadow: 0 4px 0 #044e5e, 0 8px 15px rgba(0,0,0,0.4);
                transition: all 0.1s;
                transform: translateZ(20px);
            }
            .btn-3d:active {
                box-shadow: 0 2px 0 #044e5e, 0 4px 8px rgba(0,0,0,0.4);
                transform: translateY(2px) translateZ(10px);
            }
            @keyframes modalAppear {
                from { opacity: 0; transform: translate(-50%, -60%) scale(0.9) rotateX(-15deg); }
                to { opacity: 1; transform: translate(-50%, -50%) scale(1) rotateX(0); }
            }
            .glass-panel {
                background: linear-gradient(145deg, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.9));
                backdrop-filter: blur(16px);
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            @keyframes spin { 100% { transform: rotate(360deg); } }
            .mobile-designer-frame {
                width: 375px !important;
                margin: 20px auto !important;
                border: 12px solid #1a1a1a !important;
                border-top: 45px solid #1a1a1a !important;
                border-bottom: 45px solid #1a1a1a !important;
                border-radius: 50px !important;
                height: 720px !important;
                box-shadow: 0 0 0 2px #333, 0 30px 60px rgba(0,0,0,0.8) !important;
                overflow-y: auto !important;
                background: #000 !important;
                position: relative;
                transition: all 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            }
            .mobile-designer-frame::before { 
                content: ''; position: absolute; top: -30px; left: 50%; transform: translateX(-50%); width: 80px; height: 6px; background: #333; border-radius: 10px; 
            }
            .mobile-designer-frame::after { 
                content: ''; position: absolute; bottom: -35px; left: 50%; transform: translateX(-50%); width: 35px; height: 35px; border: 2px solid #333; border-radius: 50%; 
            }
            .mobile-notch {
                position: absolute;
                top: 0;
                left: 50%;
                transform: translateX(-50%);
                width: 150px;
                height: 25px;
                background: #1a1a1a;
                border-bottom-left-radius: 20px;
                border-bottom-right-radius: 20px;
                z-index: 1000;
                display: none;
            }
            .mobile-designer-frame .mobile-notch { display: block; }
            @keyframes scan-line-anim {
                0% { top: 10px; }
                50% { top: 170px; }
                100% { top: 10px; }
            }
        """);
        
        StringBuilder sb = new StringBuilder();
        sb.append("""
            window.viewModelName = '---VIEWMODEL_NAME---';
            window.availableModels = [];
            window.modelFields = [];
            window.currentModel = null;
            window.selectedItem = null;
            
            // Designer GLOBALS
            function drag(ev) {
                console.log("DRAG START:", ev.currentTarget.getAttribute("data-type"));
                const type = ev.currentTarget.getAttribute("data-type");
                ev.dataTransfer.setData("type", type);
                ev.dataTransfer.setData("text/plain", type);
                ev.dataTransfer.effectAllowed = "copyMove";
            };
            function allowDrop(ev) {
                ev.preventDefault();
                ev.dataTransfer.dropEffect = "copy";
            };
            function drop(ev) {
                console.log("DROP EVENT FIRED");
                ev.preventDefault();
                ev.stopPropagation();
                const type = ev.dataTransfer.getData("type") || ev.dataTransfer.getData("text/plain");
                console.log("DROP TYPE:", type);
                const canvas = document.getElementById('canvas-drop-area');
                if (canvas && type) {
                    window.addComponentToCanvas(type, canvas);
                }
            };
            
            // Expose to window for backwards compatibility with inline attributes
            window.drag = drag;
            window.allowDrop = allowDrop;
            window.drop = drop;

            window.isSyncing = false;
            window.activeEventProperty = null;
            window.projectFilesMap = {};

            setInterval(() => {
                document.querySelectorAll('.live-clock').forEach(el => {
                    el.innerText = new Date().toLocaleTimeString();
                });
            }, 1000);

            window.toggleCodeView = function() {
                const cv = document.getElementById('code-view-container');
                if(cv.style.display === 'none') {
                    cv.style.display = 'flex';
                } else {
                    cv.style.display = 'none';
                }
            };

            window.toggleSidebar = function() {
                const sidebar = document.getElementById('designer-sidebar');
                if (sidebar.style.display === 'none') {
                    sidebar.style.display = 'flex';
                } else {
                    sidebar.style.display = 'none';
                }
            };

            function setupCanvasHandlers() {
                const canvas = document.getElementById('canvas-drop-area');
                if (!canvas) {
                    console.error("Canvas drop area NOT found!");
                    return;
                }
                console.log("Setting up canvas handlers...");
                
                document.body.addEventListener('dragover', function(ev) {
                    if (ev.target.closest('#canvas-drop-area') || ev.target.closest('#modal-list-container') || ev.target.closest('#code-view-container')) {
                        ev.preventDefault();
                        ev.dataTransfer.dropEffect = "copy";
                    }
                });

                document.body.addEventListener('drop', function(ev) {
                    const canvasSection = ev.target.closest('#canvas-drop-area');
                    const modalSection = ev.target.closest('#modal-list-container');
                    const codeSection = ev.target.closest('#code-view-container');
                    
                    if (!canvasSection && !modalSection && !codeSection) return;
                    
                    ev.preventDefault();
                    ev.stopPropagation();
                    
                    const type = ev.dataTransfer.getData("type");
                    const moveId = ev.dataTransfer.getData("move-id");
                    const projectFile = ev.dataTransfer.getData("project-file");
                    const target = ev.target.closest('.canvas-container') || canvasSection || modalSection;
                    
                    if (projectFile) {
                        if (projectFile.endsWith('Page.java') || projectFile.endsWith('Model.java')) {
                            window.loadFileContent(projectFile);
                        } else if (projectFile.endsWith('.java')) {
                             window.loadFileContent(projectFile);
                        }
                        if (projectFile.endsWith('Model.java')) {
                             window.selectModel(projectFile.split('/').pop().replace('.java', ''));
                        }
                        return;
                    }

                    if (codeSection && !canvasSection && !modalSection) {
                        // If specifically dropped in code section, we already loaded content via projectFile check above
                        return;
                    }

                    if (target) {
                        const targetWrapper = target.closest('.canvas-item');
                        if (targetWrapper && targetWrapper.getAttribute('data-type') === 'RadioGroupButton') {
                            let drpType = type;
                            if (moveId) {
                                const el = document.getElementById(moveId);
                                if (el) drpType = el.getAttribute('data-type');
                            }
                            if (drpType && drpType !== 'RadioButton') {
                                window.show3DMessage("Invalid Action", "Only RadioButton components can be nested inside a RadioGroupButton.");
                                return;
                            }
                        }
                        if (targetWrapper && targetWrapper.getAttribute('data-type') === 'CheckBoxGroup') {
                            let drpType = type;
                            if (moveId) {
                                const el = document.getElementById(moveId);
                                if (el) drpType = el.getAttribute('data-type');
                            }
                            if (drpType && drpType !== 'CheckBox') {
                                window.show3DMessage("Invalid Action", "Only CheckBox components can be nested inside a CheckBoxGroup.");
                                return;
                            }
                        }
                    }

                    if (moveId) {
                        const el = document.getElementById(moveId);
                        if (el && target !== el && !el.contains(target)) {
                            target.appendChild(el);
                            window.updateGeneratedCode();
                        }
                    } else if (type) {
                        window.addComponentToCanvas(type, target);
                    }
                });

                const inspector = document.getElementById('property-inspector');
                if (inspector) {
                    inspector.addEventListener('keydown', function(ev) {
                        if (ev.key === 'Enter' || ev.keyCode === 13) {
                            ev.preventDefault();
                            return false;
                        }
                    });
                }
            }

            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', () => {
                    setupCanvasHandlers();
                    window.restoreFilesFromCache();
                });
            } else {
                setupCanvasHandlers();
                window.restoreFilesFromCache();
            }

            window.toggleMobileView = function() {
                const canvas = document.getElementById('canvas-drop-area');
                canvas.classList.toggle('mobile-designer-frame');
                if (canvas.classList.contains('mobile-designer-frame')) {
                    this.show3DMessage("Mobile Mode", "Designer optimized for 375px (iPhone SE/8/etc)");
                }
            };
            
            window.addComponentToCanvas = function(type, parent, externalProps) {
                const canvas = document.getElementById('canvas-drop-area');
                const targetParent = parent || canvas;
                if (!targetParent) return;

                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.style.display = 'none';

                if (type === 'Modal' && targetParent.id !== 'modal-list-container' && !targetParent.closest('#modal-list-container')) {
                    const modalIdGen = 'modal_' + Math.floor(Math.random()*10000);
                    window.addComponentToCanvas('Button', targetParent, { text: "Open Modal", columns: 2, events: {onClick: `e -> { \\n    document.getElementById("${modalIdGen}").style.display = "block"; \\n}`}, binding: "" });
                    const mt = document.getElementById('modal-list-container');
                    if(mt) window.addComponentToCanvas('Modal', mt, Object.assign({idGen: modalIdGen}, externalProps || {}));
                    return;
                }

                const wrapper = document.createElement('div');
                wrapper.className = 'canvas-item';
                wrapper.id = 'ci_' + Date.now() + Math.floor(Math.random()*1000);
                wrapper.setAttribute('data-type', type);
                
                let defaultProps = Object.assign({text: type, columns: 2, events: {}, binding: "", styles: {}, classes: []}, externalProps || {});
                wrapper.setAttribute('data-props', JSON.stringify(defaultProps));
                wrapper.setAttribute('draggable', 'true');
                
                // Apply classes
                if (defaultProps.classes) {
                    defaultProps.classes.forEach(c => wrapper.classList.add(c));
                }
                // Apply styles for realistic rendering
                if (defaultProps.styles) {
                    for (let s in defaultProps.styles) {
                        wrapper.style[s] = defaultProps.styles[s];
                    }
                }
                
                wrapper.ondragstart = function(ev) {
                    ev.dataTransfer.setData("move-id", ev.target.id);
                    ev.dataTransfer.effectAllowed = "move";
                    ev.stopPropagation();
                };
                
                wrapper.onclick = (e) => { e.stopPropagation(); selectElement(wrapper); };
                wrapper.oncontextmenu = (e) => { e.preventDefault(); e.stopPropagation(); selectElement(wrapper); showInspector(); };
                
                let content = '';
                switch(type) {
                    case 'Clock': content = '<div class="j-component live-clock" style="font-size: 20px; font-weight: bold; color: #0f0; background: #000; padding: 10px; border-radius: 8px; border: 2px solid #333; display:inline-block; font-family: monospace;">' + new Date().toLocaleTimeString() + '</div>'; break;
                    case 'Header': content = '<h2 style="margin:0; color:var(--jettra-text);">New Header</h2>'; break;
                    case 'Paragraph': content = '<p style="margin:0; color:var(--jettra-text);">Sample text...</p>'; break;
                    case 'Span': content = '<span style="color:var(--jettra-text);">Span Text</span>'; break;
                    case 'Label': content = '<label style="color:var(--jettra-text); font-weight:bold;">Label Text</label>'; break;
                    case 'Divide': content = '<div style="border-top:1px solid var(--jettra-border); margin:15px 0; width:100%"></div>'; break;
                    case 'Separator': content = '<hr style="border: 0; border-top: 1px dashed var(--jettra-border); margin:15px 0; width:100%;">'; break;
                    case 'Button': content = '<button class="j-btn j-btn-primary" type="button">Interactive Button</button>'; break;
                    case 'TextBox': content = '<input type="text" class="j-input" placeholder="TextBox..." onfocus="this.blur()">'; break;
                    case 'TextArea': content = '<textarea class="j-input" placeholder="TextArea..." rows="3" onfocus="this.blur()"></textarea>'; break;
                    case 'CheckBox': content = '<div style="display:flex; align-items:center; gap:8px;"><input type="checkbox" checked onfocus="this.blur()"/><label>CheckBox</label></div>'; break;
                    case 'RadioButton': content = '<div style="display:flex; align-items:center; gap:8px;"><input type="radio" checked onfocus="this.blur()"/><label>RadioButton</label></div>'; break;
                    case 'RadioGroupButton': content = '<div class="canvas-container" style="padding:10px; border:1px dashed var(--jettra-accent); min-height:60px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px;">Radio Group</label></div>'; break;
                    case 'CheckBoxGroup': content = '<div class="canvas-container" style="padding:10px; border:1px dashed var(--jettra-accent); min-height:60px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px;">CheckBox Group</label></div>'; break;
                    case 'ScheduleControl': content = '<input type="datetime-local" class="j-input" onfocus="this.blur()"/>'; break;
                    case 'SelectOne': content = '<div class="select-wrapper"><label style="display:block; font-size:11px; margin-bottom:4px; color:var(--jettra-accent)">Select</label><select class="j-input" onfocus="this.blur()"><option>Option 1...</option></select></div><span style="display:none">SelectOne</span>'; break;
                    case 'SelectMany': content = '<div class="select-wrapper"><label style="display:block; font-size:11px; margin-bottom:4px; color:var(--jettra-accent)">Select Multiple</label><select class="j-input" multiple="multiple" style="height:80px" onfocus="this.blur()"><option>Option 1...</option></select></div><span style="display:none">SelectMany</span>'; break;
                    case 'SelectOneIcon': content = '<select class="j-input" onfocus="this.blur()"><option>\u2B50 Option 1...</option></select><span style="display:none">SelectOneIcon</span>'; break;
                    case 'Spinner': content = '<div class="j-spinner-wrapper" style="display:inline-flex; align-items:center; border:1px solid var(--jettra-border); border-radius:8px; background:rgba(0,0,0,0.3); overflow:hidden;"><button type="button" class="j-spinner-btn j-spinner-minus" style="width:40px;height:40px;background:rgba(255,255,255,0.05);border:none;color:var(--jettra-accent);font-size:1.2rem;font-weight:bold;">-</button><div class="j-spinner-display" style="min-width:60px;text-align:center;font-family:monospace;font-size:1.1rem;color:var(--jettra-text);">0</div><button type="button" class="j-spinner-btn j-spinner-plus" style="width:40px;height:40px;background:rgba(255,255,255,0.05);border:none;color:var(--jettra-accent);font-size:1.2rem;font-weight:bold;">+</button></div>'; break;
                    case 'ToggleSwitch': content = '<div style="display:flex; align-items:center; gap:8px;"><div style="width:40px;height:20px;background:var(--jettra-accent);border-radius:10px;position:relative;"><div style="width:16px;height:16px;background:#fff;border-radius:50%;position:absolute;top:2px;right:2px;"></div></div><label>ToggleSwitch</label></div>'; break;
                    case 'FileUpload': content = '<div style="border:1px dashed var(--jettra-accent); padding:20px; text-align:center; border-radius:8px; color:var(--jettra-text);"><div style="font-size:24px; margin-bottom:10px;">\u2601\uFE0F</div><span>Click or drag files here to upload</span></div>'; break;
                    case 'FolderSelector': content = '<div style="border:1px dashed var(--jettra-accent); padding:20px; text-align:center; border-radius:8px; color:var(--jettra-text);"><div style="font-size:24px; margin-bottom:10px;">\uD83D\uDCC1</div><span>Select Directory</span></div>'; break;
                    case 'OTPValidator': content = '<div class=\"j-component\" style=\"display:flex; justify-content:center; gap:5px; padding:10px;\"><input disabled style=\"width:30px; height:40px; text-align:center;\" value=\"*\"/><input disabled style=\"width:30px; height:40px; text-align:center;\" value=\"*\"/><input disabled style=\"width:30px; height:40px; text-align:center;\" value=\"*\"/><input disabled style=\"width:30px; height:40px; text-align:center;\" value=\"*\"/></div>'; break;
                    case 'Catcha': content = '<div class=\"j-component\" style=\"padding:10px; border:1px solid #aaa; border-radius:4px; display:inline-flex; align-items:center; gap:10px; background:#f9f9f9;\"><input type=\"checkbox\" disabled/> <span style=\"color:#333; font-family:sans-serif\">I&#39;m not a robot</span></div>'; break;
                    case 'CreditCard': content = '<div class="j-component" style="padding:15px; border:1px solid rgba(0,255,255,0.2); border-radius:12px; background:linear-gradient(145deg, #1e293b, #0f172a); min-height:100px; display:flex; flex-direction:column; gap:10px; width:280px;"><div style="font-family:monospace; font-size:16px; color:#fff; letter-spacing:2px; margin-top:20px;">\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022</div><div style="display:flex; justify-content:space-between; color:#94a3b8; font-size:10px;"><span class="cc-name-mock">NAME SURNAME</span><span>MM/YY</span></div><button class="j-btn j-btn-primary" style="margin-top:10px; width:100%; border-radius:8px; padding:10px;" disabled>Pay Now</button><div class="canvas-container" style="min-height:30px; border:1px dashed rgba(255,255,255,0.1); margin-top:5px; padding:5px;"></div></div>'; break;
                    case 'Link': content = '<a href="javascript:void(0)" style="color:var(--jettra-accent); text-decoration:underline;"><span>Link Text</span></a>'; break;
                    case 'Menu': content = '<div style="background:rgba(0,0,0,0.4); padding:10px; border-radius:4px; display:inline-block;"><div style="padding:8px 15px; cursor:pointer;"><span>Menu Item</span></div></div>'; break;
                    case 'MenuBar': content = '<div class="canvas-container" style="background:rgba(0,0,0,0.4); padding:10px; border-radius:4px; display:flex; gap:15px; min-height:45px; border:1px dashed rgba(255,255,255,0.2);"></div>'; break;
                    case 'MenuItem': content = '<div class="j-component" style="padding:10px; cursor:pointer; border-bottom:1px solid rgba(255,255,255,0.1); background:rgba(0,0,0,0.2);"><span>Menu Item</span></div>'; break;
                    case 'Loading': content = '<div class="j-loading" style="display:inline-flex; align-items:center; justify-content:center; padding:10px;"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--jettra-accent)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" /></svg></div>'; break;
                    case 'Alert': content = '<div style="background:rgba(255,0,0,0.1); border-left:4px solid #ff4444; padding:15px; border-radius:4px; color:#ff4444; display:flex; align-items:center; gap:10px;"><b>\\u26A0\\uFE0F</b><span>Alert Message</span></div>'; break;
                    case 'Notification': content = '<div style="background:rgba(0,255,255,0.1); border:1px solid var(--jettra-accent); padding:15px; border-radius:8px; color:var(--jettra-text); max-width:300px; box-shadow:0 4px 12px rgba(0,0,0,0.3);"><span>Notification Message</span></div>'; break;
                    case 'TrafficLight': content = '<div class="j-component" style="display:inline-flex; flex-direction:column; gap:8px; padding:10px; background:#222; border-radius:15px; border:1px solid #444; width:fit-content;"><div style="width:25px;height:25px;border-radius:50%;background:#ff4444;box-shadow:0 0 10px #ff4444;"></div><div style="width:25px;height:25px;border-radius:50%;background:#111;"></div><div style="width:25px;height:25px;border-radius:50%;background:#111;"></div></div>'; break;
                    case 'Downloader': content = '<div style="display:inline-flex; align-items:center; gap:8px; padding:6px 12px; background:rgba(255,255,255,0.1); border-radius:4px; cursor:pointer;"><b>\uD83D\uDCBE</b><span>Download File</span></div>'; break;
                    case 'PDFViewer': content = '<div style="border:1px solid #aaa; background:#eee; color:#333; height:200px; display:flex; align-items:center; justify-content:center; border-radius:4px;"><span>PDF Document Preview</span></div>'; break;
                    case 'ViewMedia': content = '<div style="background:#000; color:#fff; height:200px; display:flex; align-items:center; justify-content:center; border-radius:4px;"><span>\u25B6 Media Player</span></div>'; break;
                    case 'BarCode': content = '<div class="j-component" style="padding:10px; text-align:center; border:1px dashed #fff;"><span style="font-family:monospace; font-size:24px; letter-spacing:2px">||| | || |||</span><br><span>BarCode</span></div>'; break;
                    case 'QRReader': content = '<div style="width:300px; display:flex; flex-direction:column; align-items:center; gap:10px; padding:15px; border-radius:12px; background:rgba(30,41,59,0.9); border:1px solid rgba(0,255,255,0.4); box-shadow:0 4px 20px rgba(0,0,0,0.5);"><div style="width:100%; display:flex; justify-content:space-between; align-items:center; font-size:10px; color:#0ff; font-weight:bold;"><span>QR SCANNER</span><select disabled style="background:#0f172a; color:#fff; border:1px solid rgba(0,255,255,0.2); font-size:8px; padding:2px; border-radius:4px;"><option>Mock Camera</option></select></div><div style="width:100%; height:180px; position:relative; overflow:hidden; border-radius:8px; border:1px solid rgba(0,255,255,0.3); background:#000; display:flex; align-items:center; justify-content:center;"><div style="font-size:24px; color:rgba(255,255,255,0.4);">📷</div><div style="position:absolute; left:0; right:0; height:2px; background:#ff0055; box-shadow:0 0 8px 1px #ff0055; animation:scan-line-anim 3s infinite linear;"></div></div><button disabled style="width:100%; padding:6px; background:#0284c7; color:#fff; border:none; border-radius:6px; font-weight:bold; font-size:11px;">Start Scanning</button></div>'; break;
                    case 'Carousel': content = '<div style="display:flex; gap:10px; overflow:hidden; padding:10px; background:rgba(0,0,0,0.2); border-radius:8px;"><div style="width:150px; height:80px; background:rgba(0,255,255,0.1); border:1px solid rgba(0,255,255,0.3); border-radius:8px; display:flex; align-items:center; justify-content:center;"><span>Slide 1</span></div></div>'; break;
                    case 'TabView': content = '<div style="border:1px solid rgba(0,255,255,0.2); border-radius:8px; overflow:hidden;"><div style="display:flex; background:rgba(0,0,0,0.3); border-bottom:1px solid rgba(0,255,255,0.2);" class="tab-headers"><span style="padding:10px; color:#aaa; font-size:11px;">[Tab Headers]</span></div><div class="canvas-container" style="padding:10px; min-height:100px; background:rgba(0,255,255,0.02);"></div></div>'; break;
                    case 'Tab': content = '<div class="canvas-container" style="border:1px dashed var(--jettra-accent); min-height:80px; padding:10px; position:relative; background:rgba(0,0,0,0.4); margin-bottom:10px;"><span style="position:absolute; top:-12px; left:10px; background:var(--jettra-accent); color:#000; padding:2px 8px; border-radius:4px; font-size:10px; font-weight:bold;">Tab Title</span></div>'; break;
                    case 'Tree': content = '<div class="canvas-container" style="padding:10px; border:1px solid rgba(255,255,255,0.1); border-radius:4px; background:rgba(0,0,0,0.2); min-height:50px;"></div>'; break;
                    case 'TreeItem': content = '<div class="canvas-container" style="padding-left:15px; border-left:1px dashed rgba(0,255,255,0.2); min-height:30px; margin-top:5px; position:relative;"><span style="position:absolute; left:-10px; top:5px; font-size:10px; color:var(--jettra-accent);">\u25B6</span><span style="display:inline-block; margin-bottom:5px; color:#fff;">Tree Node</span></div>'; break;
                    case 'Div': content = '<div class="canvas-container" style="border:1px dashed rgba(255,255,255,0.2); min-height:50px; padding:10px; border-radius:4px; position:relative;"><span style="position:absolute;top:2px;left:5px;font-size:9px;color:rgba(255,255,255,0.3)">Div Container</span></div>'; break;
                    case 'LayoutDisplay': content = '<div class="canvas-container" style="border:2px solid rgba(0,255,255,0.2); min-height:100px; padding:15px; border-radius:8px; position:relative;"><span style="position:absolute;top:5px;left:10px;font-size:10px;color:rgba(0,255,255,0.5);font-weight:bold;">LayoutDisplay</span></div>'; break;
                    case 'DatePicker': content = '<div style="display:flex; flex-direction:column; gap:5px;"><label style="font-weight:500; font-size:0.9rem;">Date Selection</label><input type="datetime-local" class="j-input" onfocus="this.blur()"/></div>'; break;
                    case 'Time': content = '<div style="display:flex; flex-direction:column; gap:5px;"><label style="font-weight:500; font-size:0.9rem;">Time Selection</label><input type="time" class="j-input" onfocus="this.blur()"/></div>'; break;
                    case 'Map': content = '<div style="width:100%; height:200px; background:rgba(0,255,0,0.1); border:1px dashed #0f0; border-radius:8px; display:flex; align-items:center; justify-content:center; color:#0f0;"><span>\uD83D\uDDFA Leaflet Map Area</span></div>'; break;
                    case 'Calendar': content = '<div style="width:100%; min-height:150px; background:rgba(0,0,0,0.3); border:1px solid var(--jettra-border); border-radius:8px; padding:10px;"><div style="text-align:center; color:var(--jettra-accent); margin-bottom:5px;">[Month Calendar Placeholder]</div><div style="display:grid; grid-template-columns:repeat(7, 1fr); gap:2px; opacity:0.5;"><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div></div></div>'; break;
                    case 'Schedule': content = '<div style="width:100%; min-height:100px; background:rgba(0,0,0,0.3); border:1px solid var(--jettra-border); border-radius:8px; padding:10px;"><div style="text-align:center; color:var(--jettra-accent); margin-bottom:5px;">[Weekly Schedule Placeholder]</div><div style="border-left:2px solid var(--jettra-border); padding-left:10px;"><div style="background:var(--jettra-accent); color:#000; font-size:10px; width:80px; padding:2px; margin-bottom:5px;">Event</div></div></div>'; break;
                    case 'Timeline': content = '<div style="border-left:2px solid var(--jettra-border); padding-left:20px; position:relative; min-height:100px;"><div style="position:absolute; left:-6px; top:10px; width:10px; height:10px; border-radius:50%; background:var(--jettra-accent);"></div><div style="background:rgba(255,255,255,0.05); padding:10px; border-radius:4px; margin-top:5px;">Timeline Node</div></div>'; break;
                    case 'Organigram': content = '<div style="text-align:center; padding:10px;"><div style="display:inline-block; padding:10px; border:1px solid var(--jettra-accent); border-radius:6px; background:rgba(0,255,255,0.05);">Root Node</div><div style="border-left:1px dashed var(--jettra-border); height:20px; margin:0 auto; width:1px;"></div><div style="display:inline-block; padding:10px; border:1px solid var(--jettra-border); border-radius:6px; opacity:0.8;">Child Node</div></div>'; break;
                    case 'Panel': 
                        content = '<div class="j-component j-panel" style="padding:0; overflow:hidden;"><div class="j-panel-header" style="background:rgba(0,255,255,0.05); padding:10px 15px; border-bottom:1px solid var(--jettra-border); font-weight:bold; color:var(--jettra-accent)">Panel Title</div><div class="canvas-container j-panel-body" style="padding:15px; display:flex; flex-direction:column; gap:10px; min-height:50px;"></div></div>'; 
                        break;
                    case 'FormGroup':
                        content = '<div class="canvas-container" style="padding:15px; border:1px dashed var(--jettra-accent); min-height:60px; background:rgba(0,255,255,0.02); border-radius:10px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px; font-size:10px; text-transform:uppercase;">Form Group</label></div>';
                        break;
                    case 'Grid':
                        content = '<div class="canvas-container" style="display:grid; grid-template-columns:1fr 1fr; gap:15px; min-height:50px;"></div>'; 
                        break;
                    case 'Modal':
                        content = '<div class="canvas-container modal-container-mock j-component" style="padding:20px; border-radius:12px; min-height:100px; transform:translateZ(10px); box-shadow:0 15px 40px rgba(0,0,0,0.5);"><h3 style="margin-top:0; color:var(--jettra-accent); border-bottom:1px solid rgba(0,255,255,0.2); padding-bottom:10px;">Modal Component</h3></div>';
                        break;
                    case 'Board':
                        content = '<div class="canvas-container board-container-mock j-component" style="padding:20px; background:rgba(15,23,42,0.4);"><h2 style="margin-top:0; color:var(--jettra-accent); font-size:18px; margin-bottom:15px;">New Board</h2><div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:15px; min-height:100px;"><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div></div></div>';
                        break;
                    case 'Card':
                        content = '<div class="j-component j-card" style="padding:0; border:1px solid var(--jettra-accent); border-radius:8px; background:rgba(0,0,0,0.4); display:flex; flex-direction:column; gap:10px;"><div style="padding:15px; border-bottom:1px solid rgba(255,255,255,0.1);"><h3 style="margin:0;color:var(--jettra-accent);" class="card-title-mock">Card Title</h3><p style="margin:0;font-size:12px;color:#aaa" class="card-subtitle-mock"></p><p style="margin:5px 0 0;font-size:13px;color:rgba(255,255,255,0.8)" class="card-content-mock"></p></div><div class="canvas-container" style="flex:1; border:0; min-height:50px; padding:15px; background:transparent;"></div></div>';
                        break;
                    case 'ViewDataTable':
                        content = '<div style="border:1px dashed var(--jettra-accent); padding:10px; min-height:80px; border-radius:8px; background:rgba(0,0,0,0.2);"><label style="font-size:11px; color:var(--jettra-accent); margin-bottom:5px; display:block; text-transform:uppercase;">@ViewDataTable Details</label><table style="width:100%; border-collapse:collapse; color:#fff; font-size:10px;"><thead><tr><th style="border-bottom:1px solid rgba(255,255,255,0.1); padding:4px;">Col 1</th><th style="border-bottom:1px solid rgba(255,255,255,0.1); padding:4px;">Col 2</th><th style="border-bottom:1px solid rgba(255,255,255,0.1); padding:4px;">Actions</th></tr></thead><tbody><tr><td>[ Data ]</td><td><input type="text" style="width:100%; background:rgba(0,0,0,0.3); border:1px solid rgba(255,255,255,0.1); color:#fff; font-size:10px; padding:2px;"></td><td><button style="background:transparent; border:none; color:#f85149;">🗑️</button></td></tr></tbody></table><button style="margin-top:10px; background:#1f6feb; border:none; color:#fff; border-radius:4px; font-size:10px; padding:4px 8px;">➕ Add Line</button></div>';
                        break;
                    case 'Datatable':
                        content = '<div class="j-datatable-container j-component" style="padding:0; border-radius:12px; overflow:hidden; border:1px solid rgba(0,255,255,0.1);"><table style="width:100%; border-collapse:collapse; text-align:left; font-size:0.9rem;"><thead style="background:rgba(0,255,255,0.05); border-bottom:1px solid var(--jettra-accent);"><tr><th style="padding:12px; color:var(--jettra-text);">Col 1</th><th style="padding:12px; color:var(--jettra-text);">Col 2</th></tr></thead><tbody><tr style="border-bottom:1px solid var(--jettra-border);"><td class="canvas-container" style="padding:10px; color:var(--jettra-text); border-left:1px dashed rgba(255,255,255,0.1); min-height:40px;"></td><td class="canvas-container" style="padding:10px; color:var(--jettra-text); border-left:1px dashed rgba(255,255,255,0.1); min-height:40px;"></td></tr></tbody></table></div>';
                        break;
                    case 'Image':
                        content = '<div class="j-component" style="padding:0; overflow:hidden; display:flex; align-items:center; justify-content:center; background:rgba(0,0,0,0.2); min-height:100px; border-radius:8px;"><span style="color:#666;">[Image Placeholder]</span></div>';
                        break;
                    case 'Avatar':
                        content = '<div class="j-avatar j-avatar-circle j-avatar-md" style="background:var(--jettra-accent); color:#000; display:flex; align-items:center; justify-content:center; font-weight:bold">AV</div>';
                        break;
                    case 'LoginUI':
                        content = '<div class="j-loginui" style="background:rgba(20, 30, 50, 0.85); padding:20px; border-radius:10px; border:1px solid cyan; text-align:center;"><h3 style="color:cyan; margin-top:0;">Login UI Component</h3><div style="background:#0f172a; padding:10px; margin-bottom:10px; color:white;">Username</div><div style="background:#0f172a; padding:10px; margin-bottom:10px; color:white;">Password</div><div style="background:cyan; padding:10px; color:#0f172a; font-weight:bold; border-radius:5px;">Login</div></div>';
                        break;
                    case 'IndexUI':
                        content = '<div class="j-indexui" style="background:rgba(20, 30, 50, 0.85); padding:20px; border-radius:10px; border:1px solid cyan; text-align:center; min-height: 150px;"><h3 style="color:cyan; margin-top:0;">Index UI Base</h3><div style="background:#0f172a; padding:10px; margin-bottom:10px; color:white;">Welcome to IndexPage</div><div style="background:cyan; padding:10px; color:#0f172a; font-weight:bold; border-radius:5px;">Enter</div></div>';
                        break;
                    case 'ProgressBar':
                        content = '<div class="j-progressbar-container" style="width:100%; height:12px; background:rgba(255,255,255,0.05); border-radius:6px; overflow:hidden; border:1px solid var(--jettra-border)">' +
                                  '<div class="j-progressbar-fill" style="width:60%; height:100%; background:var(--jettra-accent); box-shadow:0 0 10px var(--jettra-accent)"></div>' +
                                  '</div>';
                        break;
                    case 'ChartsBar': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #36a2eb"><span style="color:#36a2eb;font-size:2rem;">\uD83D\uDCCA Bar Chart</span></div>'; break;
                    case 'ChartsDoughnut': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:50%;border:2px dashed #ff6384;width:100px;margin:auto;"><span style="color:#ff6384;font-size:2rem;">\uD83C\uDF69</span></div>'; break;
                    case 'ChartsLine': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #4bc0c0"><span style="color:#4bc0c0;font-size:2rem;">\uD83D\uDCC8 Line Chart</span></div>'; break;
                    case 'ChartsPie': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:50%;border:1px solid #ffcd56;width:100px;margin:auto;"><span style="color:#ffcd56;font-size:2rem;">\uD83E\uDDB7</span></div>'; break;
                    case 'ChartsRadar': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #9966ff"><span style="color:#9966ff;font-size:2rem;">\uD83D\uDD78\uFE0F Radar Chart</span></div>'; break;
                    default: content = `<div class="canvas-container j-component" style="padding:10px; text-align:center; color:#888;">${type} Component</div>`;
                }

                wrapper.innerHTML = content + '<div class="delete-tool" onclick="this.parentElement.remove(); window.updateGeneratedCode();">\u00D7</div>';
                targetParent.appendChild(wrapper);
                window.updateGeneratedCode();
            };

            window.selectElement = function(el) {
                if (window.selectedItem) window.selectedItem.classList.remove('selected');
                window.selectedItem = el;
                window.selectedItem.classList.add('selected');
                window.updateInspector();
            };

            window.showInspector = function() { 
                const inspector = document.getElementById('property-inspector');
                if (inspector) inspector.style.display = 'flex'; 
            };

            window.updateInspector = function() {
                if (!window.selectedItem) return;
                const type = window.selectedItem.getAttribute('data-type');
                const props = JSON.parse(window.selectedItem.getAttribute('data-props'));
                const inspector = document.getElementById('inspector-properties');
                if (!inspector) return;
                
                let html = `
                    <div class="view-model-row">
                        <span class="inspector-label">Active View Model</span>
                        <select class="inspector-input" onchange="window.selectModel(this.value)">
                            <option value="">None</option>
                            ${window.availableModels.map(m => `<option value="${m.name}" ${window.viewModelName === m.name ? 'selected' : ''}>${m.name}</option>`).join('')}
                        </select>
                        <div style="margin-top:5px; font-size:11px; color:#4ade80; font-weight:bold;">Modelo Actual: ${window.viewModelName || 'Ninguno'}</div>
                    </div>
                `;

                html += `
                    <div class="inspector-row">
                        <span class="inspector-label">Component Type</span>
                        <div style="color:var(--jettra-accent); font-weight:bold">${type}</div>
                    </div>
                    <div class="inspector-row">
                        <span class="inspector-label">Component ID</span>
                        <input type="text" class="inspector-input" value="${props.id || ''}" placeholder="Comp ID" onchange="window.updateProp('id', this.value)">
                    </div>
                    <div class="inspector-row">
                        <span class="inspector-label">Text Content</span>
                        <input type="text" class="inspector-input" value="${props.text || ''}" onchange="window.updateProp('text', this.value)">
                    </div>
                `;

                if (type === 'SelectOne' || type === 'SelectMany') {
                    const labelEl = window.selectedItem.querySelector('label');
                    const selectEl = window.selectedItem.querySelector('select');
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Label</span>
                            <input type="text" class="inspector-input" value="${labelEl ? labelEl.innerText : 'Select'}" onchange="window.updateProp('label', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Options (val:lab,...)</span>
                            <input type="text" class="inspector-input" value="${Array.from(selectEl.querySelectorAll('option')).map(o => o.value + ':' + o.innerText).join(',')}" onchange="window.updateProp('options', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Default Value</span>
                            <input type="text" class="inspector-input" value="${selectEl.dataset.default || ''}" onchange="window.updateProp('default', this.value)">
                        </div>
                    `;
                }

                // Global icon picker
                const iconComponents = ['Button', 'SelectOneIcon', 'Menu', 'MenuItem', 'Avatar', 'Alert', 'Notification', 'Link', 'Tab', 'TreeItem'];
                if (iconComponents.includes(type)) {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Icon</span>
                            <div style="display:grid; grid-template-columns: repeat(5, 1fr); gap:5px; margin-top:5px;">
                                ${['\uD83D\uDC64','\uD83D\uDC65','\uD83C\uDFE0','\u2619','\uD83D\uDD14','\uD83D\uDCE7','\uD83D\uDCC1','\uD83D\uDCCA','\uD83D\uDE80','\uD83C\uDF19','\u2600\uFE0F','\u2795','\u270F\uFE0F','\uD83D\uDDD1\uFE0F','\u2705'].map(icon => 
                                    `<div class="icon-preset" style="cursor:pointer; padding:5px; text-align:center; border:1px solid ${props.icon === icon ? 'var(--jettra-accent)' : 'rgba(255,255,255,0.1)'}" onclick="window.updateProp('icon', '${icon}')">${icon}</div>`
                                ).join('')}
                            </div>
                            <input type="text" class="inspector-input" style="margin-top:5px" placeholder="Custom icon/unicode" value="${props.icon || ''}" onchange="window.updateProp('icon', this.value)">
                        </div>
                    `;
                }

                if (type === 'Button') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Button Style</span>
                            <select class="inspector-input" onchange="window.updateProp('btnStyle', this.value)">
                                <option value="PRIMARY" ${props.btnStyle === 'PRIMARY' ? 'selected' : ''}>PRIMARY</option>
                                <option value="SECONDARY" ${props.btnStyle === 'SECONDARY' ? 'selected' : ''}>SECONDARY</option>
                                <option value="HELP" ${props.btnStyle === 'HELP' ? 'selected' : ''}>HELP</option>
                                <option value="DANGER" ${props.btnStyle === 'DANGER' ? 'selected' : ''}>DANGER</option>
                                <option value="INFO" ${props.btnStyle === 'INFO' ? 'selected' : ''}>INFO</option>
                            </select>
                        </div>
                        <div class="inspector-row"><span class="inspector-label">Bg Color</span><input type="text" class="inspector-input" value="${props.backgroundColor || ''}" onchange="window.updateProp('backgroundColor', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">JS Onclick</span><input type="text" class="inspector-input" value="${props.onclick || ''}" onchange="window.updateProp('onclick', this.value)"></div>
                        <div class="inspector-row">
                            <span class="inspector-label">Type</span>
                            <select class="inspector-input" onchange="window.updateProp('type', this.value)">
                                <option value="button" ${props.type === 'button' ? 'selected' : ''}>button</option>
                                <option value="submit" ${props.type === 'submit' ? 'selected' : ''}>submit</option>
                                <option value="reset" ${props.type === 'reset' ? 'selected' : ''}>reset</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'TextBox') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Value</span><input type="text" class="inspector-input" value="${props.value || ''}" onchange="window.updateProp('value', this.value)"></div>
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Read Only</span>
                            <input type="checkbox" ${props.readonly ? 'checked' : ''} onchange="window.updateProp('readonly', this.checked)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Display</span>
                            <select class="inspector-input" onchange="window.updateProp('display', this.value)">
                                <option value="block" ${props.display === 'block' ? 'selected' : ''}>block</option>
                                <option value="none" ${props.display === 'none' ? 'selected' : ''}>none</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'Modal') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Padding</span><input type="text" class="inspector-input" value="${props.padding || '30px'}" onchange="window.updateProp('padding', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Background Color</span><input type="text" class="inspector-input" value="${props.backgroundColor || '#161b22'}" onchange="window.updateProp('backgroundColor', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Border</span><input type="text" class="inspector-input" value="${props.border || '1px solid #30363d'}" onchange="window.updateProp('border', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Max Width</span><input type="text" class="inspector-input" value="${props.maxWidth || '90vw'}" onchange="window.updateProp('maxWidth', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Z-Index</span><input type="text" class="inspector-input" value="${props.zIndex || '1000'}" onchange="window.updateProp('zIndex', this.value)"></div>
                    `;
                }

                // Components that support model binding
                const bindableComponents = ['TextBox', 'TextArea', 'Label', 'SelectOne', 'SelectMany', 'CheckBox', 'RadioButton', 'DatePicker', 'Time', 'Spinner'];
                if (bindableComponents.includes(type)) {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Bind to Model Field</span>
                            <select class="inspector-input" onchange="window.updateProp('binding', this.value)">
                                <option value="">No Binding</option>
                                ${window.modelFields.map(f => `<option value="${f.name}" ${props.binding === f.name ? 'selected' : ''}>${f.name}</option>`).join('')}
                            </select>
                        </div>
                    `;
                }
""").append("""

                if (type === 'Map') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Initial Latitude (lat)</span>
                            <input type="number" step="0.0001" class="inspector-input" value="${props.lat || 40.7128}" placeholder="e.g. 40.7128" onchange="updateProp('lat', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Initial Longitude (lng)</span>
                            <input type="number" step="0.0001" class="inspector-input" value="${props.lng || -74.0060}" placeholder="e.g. -74.0060" onchange="updateProp('lng', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Zoom Level</span>
                            <input type="number" class="inspector-input" value="${props.zoom || 13}" min="1" max="25" onchange="updateProp('zoom', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Marker Title</span>
                            <input type="text" class="inspector-input" value="${props.marker || ''}" placeholder="Main Location" onchange="updateProp('marker', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Enable Search</span>
                            <select class="inspector-input" onchange="updateProp('enableSearch', this.value === 'true')">
                                <option value="false" ${props.enableSearch ? '' : 'selected'}>False</option>
                                <option value="true" ${props.enableSearch ? 'selected' : ''}>True</option>
                            </select>
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Enable Relief</span>
                            <select class="inspector-input" onchange="window.updateProp('enableRelief', this.value === 'true')">
                                <option value="false" ${props.enableRelief ? '' : 'selected'}>False</option>
                                <option value="true" ${props.enableRelief ? 'selected' : ''}>True</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'Spinner') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Value</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.value || 0}" onchange="updateProp('value', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Min</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.min || 0}" onchange="updateProp('min', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Max</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.max || 100}" onchange="updateProp('max', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Step</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.step || 1}" onchange="updateProp('step', this.value)">
                        </div>
                    `;
                }

                if (type === 'RadioGroupButton' || type === 'CheckBoxGroup') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Group Name</span>
                            <input type="text" class="inspector-input" value="${props.name || (type === 'CheckBoxGroup' ? 'checkboxGroup1' : 'radioGroup1')}" onchange="updateProp('name', this.value)">
                        </div>
                    `;
                }

                if (type.startsWith('Chart')) {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Labels (comma sep.)</span>
                            <input type="text" class="inspector-input" value="${props.labels || 'Label 1, Label 2'}" onchange="updateProp('labels', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Dataset Label</span>
                            <input type="text" class="inspector-input" value="${props.datasetLabel || 'Data'}" onchange="updateProp('datasetLabel', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Data (comma sep. numbers)</span>
                            <input type="text" class="inspector-input" value="${props.data || '10, 20'}" onchange="updateProp('data', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Bg Color</span>
                            <input type="color" class="inspector-input" value="${props.bgColor || '#00ffff'}" onchange="updateProp('bgColor', this.value)">
                        </div>
                    `;
                }

                if (type === 'ScheduleControl') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Initial View</span>
                            <select class="inspector-input" onchange="updateProp('view', this.value)">
                                <option value="day" ${props.view === 'day' ? 'selected' : ''}>Day</option>
                                <option value="week" ${props.view === 'week' ? 'selected' : ''}>Week</option>
                                <option value="month" ${props.view === 'month' ? 'selected' : ''}>Month</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'Card') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Title</span><input type="text" class="inspector-input" value="${props.title || ''}" onchange="updateProp('title', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Subtitle</span><input type="text" class="inspector-input" value="${props.subtitle || ''}" onchange="updateProp('subtitle', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Content</span><textarea class="inspector-input" onchange="updateProp('content', this.value)">${props.content || ''}</textarea></div>
                        <div class="inspector-row"><span class="inspector-label">Image URL</span><input type="text" class="inspector-input" value="${props.imageUrl || ''}" onchange="updateProp('imageUrl', this.value)"></div>
                    `;
                }

                if (type === 'QR') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Text</span><input type="text" class="inspector-input" value="${props.text || 'https://jettra.io'}" onchange="updateProp('text', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Width</span><input type="number" class="inspector-input" value="${props.width || 128}" onchange="updateProp('width', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Height</span><input type="number" class="inspector-input" value="${props.height || 128}" onchange="updateProp('height', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Dark Color</span><input type="color" class="inspector-input" value="${props.colorDark || '#000000'}" onchange="updateProp('colorDark', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Light Color</span><input type="color" class="inspector-input" value="${props.colorLight || '#ffffff'}" onchange="updateProp('colorLight', this.value)"></div>
                    `;
                }

                if (type === 'BarCode') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Text</span><input type="text" class="inspector-input" value="${props.text || '123456789012'}" onchange="updateProp('text', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Format</span><input type="text" class="inspector-input" value="${props.format || 'CODE128'}" onchange="updateProp('format', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Line Color</span><input type="color" class="inspector-input" value="${props.lineColor || '#000000'}" onchange="updateProp('lineColor', this.value)"></div>
                    `;
                }

                if (type === 'QRReader') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Width</span><input type="text" class="inspector-input" value="${props.width || '350px'}" onchange="updateProp('width', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Height</span><input type="text" class="inspector-input" value="${props.height || '300px'}" onchange="updateProp('height', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">OnScan Callback</span><input type="text" class="inspector-input" value="${props.onScan || 'function(code) { console.log(code); }'}" onchange="updateProp('onScan', this.value)"></div>
                    `;
                }

                if (type === 'OTPValidator') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Digits</span><input type="number" class="inspector-input" value="${props.amountOfDigits || 6}" onchange="updateProp('amountOfDigits', this.value)"></div>
                    `;
                }

                if (type === 'Catcha') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Images To Validate</span><input type="number" class="inspector-input" value="${props.amountOfImagesToValidate || 3}" onchange="updateProp('amountOfImagesToValidate', this.value)"></div>
                    `;
                }

                if (type === 'TabView') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Orientation</span>
                            <select class="inspector-input" onchange="window.updateProp('orientation', this.value)">
                                <option value="TOP" ${props.orientation === 'TOP' ? 'selected' : ''}>TOP</option>
                                <option value="BOTTOM" ${props.orientation === 'BOTTOM' ? 'selected' : ''}>BOTTOM</option>
                                <option value="LEFT" ${props.orientation === 'LEFT' ? 'selected' : ''}>LEFT</option>
                                <option value="RIGHT" ${props.orientation === 'RIGHT' ? 'selected' : ''}>RIGHT</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'CreditCard') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Form Action</span><input type="text" class="inspector-input" value="${props.formAction || ''}" onchange="updateProp('formAction', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Submit Text</span><input type="text" class="inspector-input" value="${props.submitText || 'Pay Now'}" onchange="updateProp('submitText', this.value)"></div>
                    `;
                }

                if (type === 'ViewDataTable') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Columns (comma separated)</span><input type="text" class="inspector-input" value="${props.columnsList || 'col1, col2'}" onchange="updateProp('columnsList', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Editable Columns (comma separated)</span><input type="text" class="inspector-input" value="${props.editableCols || 'col1'}" onchange="updateProp('editableCols', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Source</span><input type="text" class="inspector-input" value="${props.source || 'Repository'}" onchange="updateProp('source', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Method</span><input type="text" class="inspector-input" value="${props.method || 'getAll'}" onchange="updateProp('method', this.value)"></div>
                    `;
                }

                if (type === 'Datatable') {
                    const columnsArr = props.columnsList || ['Col 1', 'Col 2'];
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Datatable Columns</span>
                            <div style="display:flex; flex-direction:column; gap:5px; margin-bottom:5px;">
                                ${columnsArr.map((col, idx) => `
                                    <div style="display:flex; gap:5px; align-items:center;">
                                        <input type="text" class="inspector-input" style="flex:1" value="${col}" onchange="window.updateTableColumn(${idx}, this.value)">
                                        <button class="j-btn j-btn-danger" style="padding:4px 8px; font-size:10px; flex:0 0 auto;" onclick="window.removeTableColumn(${idx})">X</button>
                                    </div>
                                `).join('')}
                            </div>
                            <button class="j-btn j-btn-primary" style="width:100%; font-size:11px; padding:6px; margin-top:5px;" onclick="window.addTableColumn()">+ Add Column</button>
                        </div>
                    `;
                }

                if (type === 'Clock') {
                    html += `
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Show Remaining Time</span>
                            <input type="checkbox" ${props.showTimeRemaining ? 'checked' : ''} onchange="updateProp('showTimeRemaining', this.checked)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">On Time Reached (JS)</span>
                            <input type="text" class="inspector-input" value="${props.onTimeReached || ""}" placeholder="alert('Time is up!');" onchange="updateProp('onTimeReached', this.value)">
                        </div>
                    `;
                }

                // Common properties
                html += `
                    <div style="margin-top:15px; border-top:1px solid rgba(255,255,255,0.1); padding-top:10px">
                        <div class="inspector-row">
                            <span class="inspector-label">Update (IDs)</span>
                            <input type="text" class="inspector-input" value="${props.update || ""}" onchange="updateProp('update', this.value)">
                        </div>
                    </div>
                `;

                if (type === 'Avatar') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Avatar Text</span>
                            <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
                        </div>
                    `;
                }

                if (type === 'ProgressBar') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Value</span>
                            <input type="number" class="inspector-input" value="${props.value || 60}" onchange="updateProp('value', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Max</span>
                            <input type="number" class="inspector-input" value="${props.max || 100}" onchange="updateProp('max', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Color</span>
                            <input type="color" class="inspector-input" value="${props.color || '#00ffff'}" onchange="updateProp('color', this.value)">
                        </div>
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Indeterminate</span>
                            <input type="checkbox" ${props.indeterminate ? 'checked' : ''} onchange="updateProp('indeterminate', this.checked)">
                        </div>
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Show %</span>
                            <input type="checkbox" ${props.showPercent !== false ? 'checked' : ''} onchange="updateProp('showPercent', this.checked)">
                        </div>
                    `;
                }

                const supportedEvents = ['onClick', 'onChange', 'onAction'];
                html += `
                    <div style="margin-top:20px; border-top:1px solid rgba(0,255,255,0.1); padding-top:15px">
                        <span class="inspector-label" style="color:var(--jettra-accent); font-weight:bold">\u26A1 Event Handlers</span>
                        <div style="display:flex; flex-direction:column; gap:8px; margin-top:10px">
                            ${supportedEvents.map(ev => `
                                <div class="inspector-row" style="cursor:pointer; display:flex; justify-content:space-between; align-items:center" onclick="window.openEventEditor('${ev}')">
                                    <span style="font-size:11px; color:#ccc">${ev}</span>
                                    <span style="font-size:10px; color:var(--jettra-accent)">Configure \u21E2</span>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                `;

                inspector.innerHTML = html;
            };

            window.openEventEditor = function(evName) {
                if (!window.selectedItem) return;
                window.activeEventProperty = evName;
                const props = JSON.parse(window.selectedItem.getAttribute('data-props'));
                const currentCode = props.events[evName] || "e -> { \\n    // Enter code here \\n}";
                
                document.getElementById('event-code-input').value = currentCode.replace(/\\\\n/g, '\\n');
                const modal = document.getElementById('event-editor-modal');
                modal.style.display = 'block';
                window.apply3DTracking(modal);
            };

            window.saveEventHandler = function() {
                if (!window.selectedItem || !window.activeEventProperty) return;
                const props = JSON.parse(window.selectedItem.getAttribute('data-props'));
                const newCode = document.getElementById('event-code-input').value;
                props.events[window.activeEventProperty] = newCode.replace(/\\n/g, '\\\\n');
                window.selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                document.getElementById('event-editor-modal').style.display = 'none';
                window.updateGeneratedCode();
                window.updateInspector();
            };

            window.parseModelFieldsContent = function(content) {
                const fieldRegex = /(?:private|protected|public)?\\s+([\\w<>]+)\\s+(\\w+);/g;
                let match;
                window.modelFields = [];
                const seenFields = new Set();
                while ((match = fieldRegex.exec(content)) !== null) {
                    const type = match[1];
                    const name = match[2];
                    if (!seenFields.has(name)) {
                        window.modelFields.push({ type: type, name: name });
                        seenFields.add(name);
                    }
                }
                window.updateInspector();
                window.updateGeneratedCode();
            };

            window.selectModel = function(name) {
                window.viewModelName = name;
                const model = window.availableModels.find(m => m.name === name);
                if (model) {
                    window.currentModel = model;
                    window.parseModelFieldsContent(model.content);
                    window.show3DMessage("Model Attached", "Se ha vinculado el modelo " + name + " a la vista.");
                } else {
                    window.currentModel = null;
                    window.modelFields = [];
                    window.updateInspector();
                    window.updateGeneratedCode();
                }
                window.updateModelSelect();
            };

            window.updateTableColumn = function(idx, value) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                if (!props.columnsList) props.columnsList = ['Col 1', 'Col 2'];
                props.columnsList[idx] = value;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                window.renderTableComponent(selectedItem, props);
            };
            
            window.removeTableColumn = function(idx) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                if (!props.columnsList) props.columnsList = ['Col 1', 'Col 2'];
                if (props.columnsList.length <= 1) return; // Evitar tabla sin columnas
                props.columnsList.splice(idx, 1);
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                window.renderTableComponent(selectedItem, props);
                window.updateInspector();
            };
            
            window.addTableColumn = function() {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                if (!props.columnsList) props.columnsList = ['Col 1', 'Col 2'];
                props.columnsList.push('New Col');
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                window.renderTableComponent(selectedItem, props);
                window.updateInspector();
            };
            
            window.renderTableComponent = function(el, props) {
                const cols = props.columnsList || ['Col 1', 'Col 2'];
                const theadTr = el.querySelector('thead tr');
                if (theadTr) {
                    theadTr.innerHTML = cols.map(c => `<th style="padding:12px; color:var(--jettra-text);">${c}</th>`).join('');
                }
                const tbody = el.querySelector('tbody');
                if (tbody) {
                    const rows = tbody.querySelectorAll('tr');
                    rows.forEach(tr => {
                        let existingTds = Array.from(tr.querySelectorAll('td'));
                        tr.innerHTML = '';
                        cols.forEach((c, i) => {
                            if (i < existingTds.length) {
                                tr.appendChild(existingTds[i]);
                            } else {
                                let td = document.createElement('td');
                                td.className = 'canvas-container';
                                td.style = 'padding:10px; border-left:1px dashed rgba(255,255,255,0.1); min-height:40px;';
                                tr.appendChild(td);
                            }
                        });
                    });
                }
                window.updateGeneratedCode();
            };

            window.updateProp = function(key, value) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                props[key] = value;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                const type = selectedItem.getAttribute('data-type');
                if (key === 'text') {
                    if (type === 'TextBox' || type === 'TextArea') {
                        const inEl = selectedItem.querySelector('input, textarea');
                        if (inEl) {
                            inEl.placeholder = value;
                            inEl.value = value;
                        }
                    } else if (type === 'Button') {
                        const el = selectedItem.querySelector('button');
                        if (el) el.innerHTML = (props.icon ? props.icon + ' ' : '') + value;
                    } else {
                        const el = selectedItem.querySelector('h3, h2, h1, p, label, .j-avatar, .j-panel-header, span');
                        if (el) el.innerText = value;
                        if (type === 'Avatar') {
                            props.icon = "";
                            selectedItem.setAttribute('data-props', JSON.stringify(props));
                            setTimeout(window.updateInspector, 10);
                        }
                    }
                }
                
                if (key === 'label') {
                    const label = selectedItem.querySelector('label');
                    if (label) label.innerText = value;
                }
                if (key === 'options') {
                    const select = selectedItem.querySelector('select');
                    if (select) {
                        select.innerHTML = value.split(',').map(pair => {
                            const [v, l] = pair.split(':');
                            return `<option value="${v||''}">${l||v||''}</option>`;
                        }).join('');
                    }
                }
                if (key === 'default') {
                    const select = selectedItem.querySelector('select');
                    if (select) {
                        select.dataset.default = value;
                        Array.from(select.options).forEach(opt => {
                            if (opt.value === value) opt.selected = true;
                            else opt.selected = false;
                        });
                    }
                }
                
                if (key === 'icon') {
                    if (type === 'Avatar') {
                        const el = selectedItem.querySelector('.j-avatar');
                        if (el) el.innerHTML = value;
                        props.text = "";
                        selectedItem.setAttribute('data-props', JSON.stringify(props));
                        setTimeout(window.updateInspector, 10);
                    } else if (type === 'Button') {
                        const el = selectedItem.querySelector('button');
                        if (el) el.innerHTML = (value ? value + ' ' : '') + (props.text || 'Interactive Button');
                    }
                }

                if (key === 'btnStyle' && type === 'Button') {
                    const el = selectedItem.querySelector('button');
                    if (el) {
                        el.className = 'j-btn'; // reset
                        el.classList.add('j-btn-' + value.toLowerCase());
                    }
                }

                if (type === 'Card') {
                    if (key === 'title') { const tEl = selectedItem.querySelector('.card-title-mock'); if (tEl) tEl.innerText = value; }
                    if (key === 'subtitle') { const sEl = selectedItem.querySelector('.card-subtitle-mock'); if (sEl) sEl.innerText = value; }
                    if (key === 'content') { const cEl = selectedItem.querySelector('.card-content-mock'); if (cEl) cEl.innerText = value; }
                }

                if (type === 'TabView') {
                    if (key === 'text') { const el = selectedItem.querySelector('.tab-headers span'); if (el) el.innerText = '[' + value + ']'; }
                }

                if (type === 'CreditCard') {
                    if (key === 'submitText') {
                        const btnEl = selectedItem.querySelector('button');
                        if (btnEl) btnEl.innerText = value;
                    }
                }

                if (type === 'ProgressBar') {
                    const fill = selectedItem.querySelector('.j-progressbar-fill');
                    if (key === 'value' || key === 'max') {
                        const val = parseInt(selectedItem.getAttribute('data-props-value') || props.value || 60);
                        const mx = parseInt(selectedItem.getAttribute('data-props-max') || props.max || 100);
                        const pct = (key === 'value' ? value : val) / (key === 'max' ? value : mx) * 100;
                        fill.style.width = pct + '%';
                    }
                    if (key === 'color') {
                        fill.style.background = value;
                        fill.style.boxShadow = `0 0 10px ${value}`;
                    }
                    if (key === 'indeterminate') {
                        if (value) fill.classList.add('j-progress-indeterminate');
                        else fill.classList.remove('j-progress-indeterminate');
                    }
                    if (key === 'showPercent') {
                        const lbl = selectedItem.querySelector('.j-progressbar-label');
                        if (lbl) lbl.style.display = value ? 'block' : 'none';
                    }
                }
                if (key === 'columns' && (type === 'Panel' || type === 'Grid')) {
                    const container = selectedItem.querySelector('.canvas-container');
                    if (container) container.style.gridTemplateColumns = `repeat(${value}, 1fr)`;
                }
                if (type === 'Spinner' && key === 'value') {
                    const disp = selectedItem.querySelector('.j-spinner-display');
                    if (disp) disp.innerText = value;
                }
                if (type.startsWith('Chart') && key === 'bgColor') {
                    selectedItem.style.borderColor = value;
                    const span = selectedItem.querySelector('span');
                    if (span) span.style.color = value;
                }
                window.updateGeneratedCode();
            };

            window.jettraFileCache = {};

            window.restoreFilesFromCache = function() {
                const cachedTree = localStorage.getItem('jettra_designer_tree');
                const cachedFilesStr = localStorage.getItem('jettra_designer_files');
                
                if (cachedTree && cachedFilesStr) {
                    const viewer = document.getElementById('explorer-tree-view');
                    if (viewer) viewer.innerHTML = cachedTree;
                    try {
                        window.jettraFileCache = JSON.parse(cachedFilesStr);
                        availableModels = [];
                        Object.keys(window.jettraFileCache).forEach(path => {
                            if (path.endsWith('Model.java')) {
                                const name = path.split('/').pop().replace('.java','');
                                availableModels.push({ name: name, content: window.jettraFileCache[path] });
                            }
                        });
                        window.updateModelSelect();
                    } catch(e) {}
                }
            };

            window.clearProjectCache = function() {
                localStorage.removeItem('jettra_designer_tree');
                localStorage.removeItem('jettra_designer_files');
                window.jettraFileCache = {};
                availableModels = [];
                const viewer = document.getElementById('explorer-tree-view');
                if (viewer) viewer.innerHTML = '';
            window.updateGeneratedCode();
            };
        """).append("""

            window.loadFiles = function(input) {
                const viewer = document.getElementById('explorer-tree-view');
                if (!viewer) return;
                
                viewer.innerHTML = '<div style="padding:15px; color:var(--jettra-accent);"><i class="jt-icon-loading"></i> Procesando Proyecto...</div>';
                
                const files = (input.tagName === 'INPUT' ? input : input.querySelector('input')).files;
                console.log("Loading files:", files ? files.length : 0);
                if (!files || files.length === 0) {
                    viewer.innerHTML = '<div style="color:#666; padding:10px;">Selecci\\u00F3n vac\\u00EDa.</div>';
                    return;
                }

                const tree = { _children: {} };
                const readPromises = [];
                window.jettraFileCache = {};
                availableModels = [];

                for (let i = 0; i < files.length; i++) {
                    const f = files[i];
                    const relPath = f.webkitRelativePath || f.name;
                    if (relPath.includes('/target/') || relPath.includes('/.git/')) continue;

                    // Use a more robust regex for path separator (matches \\ and /)
                    const parts = relPath.split(/[\\\\/]/);
                    let curr = tree;
                    for (let j = 0; j < parts.length; j++) {
                        const p = parts[j];
                        if (!curr._children[p]) {
                            curr._children[p] = { _children: {}, _path: relPath, _isFile: (j === parts.length - 1) };
                        }
                        curr = curr._children[p];
                    }

                    if (f.name.endsWith('.java') || f.name.endsWith('.md') || f.name.endsWith('.css') || f.name.endsWith('.html')) {
                        readPromises.push(new Promise((resolve) => {
                            const reader = new FileReader();
                            reader.onload = (e) => {
                                const content = e.target.result;
                                window.jettraFileCache[relPath] = content;
                                if (f.name.endsWith('Model.java')) {
                                    const mName = f.name.replace('.java','');
                                    if (!availableModels.find(m => m.name === mName)) {
                                        availableModels.push({ name: mName, content: content });
                                    }
                                }
                                resolve();
                            };
                            reader.onerror = () => resolve();
                            reader.readAsText(f);
                        }));
                    }
                }

                function renderTree(node, depth = 0) {
                    try {
                        const keys = Object.keys(node._children).sort((a,b) => a.localeCompare(b));
                        let html = "";
                        keys.forEach(key => {
                            const child = node._children[key];
                            const isPage = key.endsWith('Page.java');
                            const isModel = key.endsWith('Model.java');
                            const draggable = isPage ? 'draggable="true" ondragstart="window.dragProjectFile(event)" data-path="'+child._path.replace(/\\\\/g, '/')+'"' : '';
                            const pageClass = isPage ? 'file-page' : (isModel ? 'file-model' : '');
                             
                            let icon = child._isFile ? '\uD83D\uDCC4' : '\uD83D\uDCC1';
                            if (key.endsWith('.java')) icon = '\u2615';
                            else if (key.endsWith('.md')) icon = '\uD83D\uDCDD';
                            else if (key.endsWith('.css') || key.endsWith('.html')) icon = '\uD83C\uDF10';
                            let color = child._isFile ? '#a9b7c6' : '#e5c07b';

                            html += `<div class="tree-node ${pageClass}" ${draggable} style="padding-left:${depth * 12}px; cursor:pointer; color:${color}; font-size:11px; margin-bottom:4px; display:flex; align-items:center; gap:5px;"`;
                            if (child._isFile) {
                                html += ` onclick="window.loadFileContent('${child._path.replace(/\\\\/g, '/')}')"`;
                            }
                            html += `><span style="font-size:14px">${icon}</span> <span>${key}</span></div>`;
                            
                            if (!child._isFile) {
                                html += renderTree(child, depth + 1);
                            }
                        });
                        return html;
                    } catch(e) {
                        console.error("Error rendering tree:", e);
                        return "Error: " + e.message;
                    }
                }

                Promise.all(readPromises).then(() => {
                    console.log("Files cached. Available models:", availableModels.length);
                    const finalHtml = renderTree(tree);
                    viewer.innerHTML = finalHtml || '<div style="color:#666; padding:10px;">No se encontraron archivos visualizables.</div>';
                    window.updateModelSelect();
                    try {
                        localStorage.setItem('jettra_designer_tree', finalHtml);
                        localStorage.setItem('jettra_designer_files', JSON.stringify(window.jettraFileCache));
                    } catch(e) {}
                });
            };

            window.dragProjectFile = function(ev) {
                ev.dataTransfer.setData("project-file", ev.currentTarget.getAttribute("data-path"));
                ev.dataTransfer.effectAllowed = "copy";
            };

            window.loadFileContent = function(path) {
                console.log("Loading file content for:", path);
                const content = window.jettraFileCache[path];
                if (!content) {
                    console.error("No content found for path:", path);
                    return;
                }
                
                const display = document.getElementById('generated-code-display');
                if (display) {
                    display.value = content;
                    window.syncCodeToCanvas();
                }
            };

            window.openClass = function(name) {
                const isPage = name.endsWith('Page.java');
                const isModel = name.endsWith('Model.java');

                if (isModel) {
                    window.selectModel(name.replace('.java', ''));
                    return;
                }

                if (isPage) {
                    const canvas = document.getElementById('canvas-drop-area');
                    const fullPathKey = Object.keys(window.jettraFileCache).find(k => k.endsWith('/' + name) || k === name);
                    const content = fullPathKey ? window.jettraFileCache[fullPathKey] : undefined;
                    
                    const doOpen = () => {
                        if (content) window.loadFileContent(fullPathKey);
                        else window.show3DMessage("Aviso", "No se encontr\u00F3 contenido para " + name);
                    };

                    if (canvas.querySelectorAll('.canvas-item').length > 0) {
                        window.show3DConfirm("Abrir Clase", "\u00BFCerrar dise\u00F1o actual y abrir " + name + "?", doOpen);
                    } else {
                        doOpen();
                    }
                }
            };

            window.updateModelSelect = function() {
                const container = document.getElementById('model-select-container');
                if (!container) return;
                
                if (window.availableModels.length === 0) {
                    container.innerHTML = '<span style="color:#666; font-size:11px">No models found in project</span>';
                    return;
                }
                
                let html = '<select class="j-input" style="padding:4px; font-size:11px" onchange="window.selectModel(this.value)">';
                html += '<option value="">-- Select Model --</option>';
                window.availableModels.forEach(m => {
                    const selected = window.viewModelName === m.name ? 'selected' : '';
                    html += `<option value="${m.name}" ${selected}>${m.name}</option>`;
                });
                html += '</select>';
                html += `<div style="margin-top:5px; font-size:11px; color:#4ade80; font-weight:bold; text-shadow: 0 0 5px rgba(74,222,128,0.2);">Modelo Actual: ${window.viewModelName || 'Ninguno'}</div>`;
                container.innerHTML = html;
            };

            window.show3DConfirm = function(title, body, onConfirm) {
                const modal = document.getElementById('global-3d-message-modal');
                if (modal) {
                    document.getElementById('global-3d-title').innerText = title;
                    document.getElementById('global-3d-body').innerText = body;
                    const footer = modal.querySelector('div:last-child');
                    if (footer) {
                        footer.innerHTML = `
                            <button class="dash-btn-3d" onclick="document.getElementById('global-3d-message-modal').style.display='none'; window._lastConfirm();">Aceptar</button>
                            <button class="dash-btn-3d" style="background:#444; margin-left:10px" onclick="document.getElementById('global-3d-message-modal').style.display='none'">Cancelar</button>
                        `;
                        window._lastConfirm = onConfirm;
                    }
                    modal.style.display = 'block';
                } else {
                    if (confirm(body)) onConfirm();
                }
            };

            window.generateCRUD = function() {
                const canvas = document.getElementById('canvas-drop-area');
                if (!canvas) return;
                if (!currentModel) {
                    window.show3DMessage("Error", "Por favor, seleccione un modelo para analizar.");
                    return;
                }
                
                const mName = currentModel.name;
                const baseName = mName.replace("Model", "");
                const repoName = baseName + "Repository";
                
                // Detailed analysis of model fields
                let tblHeaders = [];
                let tblRows = [];
                let modalFieldsCode = "";
                
                // Requirement: Add specific imports
                let imports = new Set([
                    "io.jettra.wui.components.*",
                    "io.jettra.wui.validations.JettraValidations",
                    "io.jettra.wui.complex.Center",
                    "io.jettra.wui.core.annotations.InjectViewModel",
                    "io.jettra.wui.sync.JettraSyncManager",
                    "io.jettra.wui.sync.JettraPageSincronized",
                    "io.jettra.wui.sync.SyncType",
                    "io.jettra.wui.core.annotations.InjectProperties",
                    "io.jettra.server.JettraServer",
                    "java.io.IOException",
                    "java.util.List",
                    "java.util.Map",
                    "java.util.Properties",
                    "com.jettra.example.dashboard.DashboardBasePage",
                    "com.jettra.example.model." + mName,
                    "com.jettra.example.repository." + repoName
                ]);

                // Requirement: Repository check
                const repoFile = Object.keys(window.jettraFileCache).find(k => k.endsWith(repoName + ".java"));
                if (!repoFile) {
                    window.show3DMessage("Aviso", "No se encontró la clase " + repoName + ".java en el proyecto actual.");
                }

                modelFields.forEach(field => {
                    const cName = field.name.charAt(0).toUpperCase() + field.name.slice(1);
                    const vName = field.name;
                    const isList = field.type.includes('List');
                    const isCustom = /^[A-Z]/.test(field.type) && !['String','Integer','Double','Boolean','Long','Date'].includes(field.type);
                    
                    tblHeaders.push(`msg.getProperty("th.${vName}", "${cName}")`);
                    tblRows.push(`new io.jettra.wui.components.TD(String.valueOf(p.get${cName}()))`);
                    
                    modalFieldsCode += `        FormGroup g_${vName} = new FormGroup()\\n`;
                    modalFieldsCode += `            .add(new Label("${vName}", msg.getProperty("th.${vName}", "${cName}")));\\n`;
                    
                    if (isList) {
                        modalFieldsCode += `        SelectMany sel_${vName} = new SelectMany("${vName}").setInline(true);\\n`;
                        modalFieldsCode += `        sel_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalFieldsCode += `        JettraValidations.apply(sel_${vName}, ${mName}.class, "${vName}");\\n`;
                        modalFieldsCode += `        g_${vName}.add(sel_${vName});\\n`;
                    } else if (isCustom) {
                        modalFieldsCode += `        SelectOne sel_${vName} = new SelectOne("${vName}", "");\\n`;
                        modalFieldsCode += `        sel_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalFieldsCode += `        JettraValidations.apply(sel_${vName}, ${mName}.class, "${vName}");\\n`;
                        modalFieldsCode += `        g_${vName}.add(sel_${vName});\\n`;
                    } else if (field.type === 'Date') {
                        modalFieldsCode += `        DatePicker dp_${vName} = new DatePicker("${vName}", "${cName}");\\n`;
                        modalFieldsCode += `        dp_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalFieldsCode += `        JettraValidations.apply(dp_${vName}, ${mName}.class, "${vName}");\\n`;
                        modalFieldsCode += `        g_${vName}.add(dp_${vName});\\n`;
                    } else {
                        modalFieldsCode += `        TextBox tb_${vName} = new TextBox("text", "${vName}");\\n`;
                        modalFieldsCode += `        tb_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalFieldsCode += `        JettraValidations.apply(tb_${vName}, ${mName}.class, "${vName}");\\n`;
                        modalFieldsCode += `        g_${vName}.add(tb_${vName});\\n`;
                    }
                    modalFieldsCode += `        form.add(g_${vName});\\n\\n`;
                });

                let code = `package com.jettra.example.pages;\\n\\n`;
                Array.from(imports).sort().forEach(imp => code += `import ${imp};\\n`);
                code += `\\n@JettraPageSincronized(SyncType.ALL)\\n`;
                code += `public class ${baseName}Page extends DashboardBasePage {\\n\\n`;
                code += `    @InjectViewModel\\n    ${mName} model;\\n\\n`;
                code += `    @io.jettra.wui.core.annotations.Inject\\n    private ${repoName} repository;\\n\\n`;
                code += `    @InjectProperties(name = "messages")\\n    private Properties msg;\\n\\n`;
                code += `    private String lang = "es";\\n\\n`;
                code += `    private Div crudModal;\\n    private Header modalTitle;\\n    private TextBox modalAction;\\n    private Button modalSubmitBtn;\\n\\n`;
                code += `    public ${baseName}Page() {\\n        super("Mantenimiento de ${baseName} (Pure MVC)");\\n    }\\n\\n`;
                code += `    @Override\\n    protected void onInit(Map<String, String> params) {\\n`;
                code += `        String lStr = params.get("lang");\\n`;
                code += `        if (lStr != null) this.lang = lStr;\\n`;
                code += `        super.onInit(params);\\n`;
                code += `    }\\n\\n`;
                code += `    @Override\\n    protected void initCenter(Center center, String username) {\\n`;
                code += `        Style customStyles = new Style("\\n" +\\n`;
                code += `            "            .crud-table { width: 100%; border-collapse: collapse; margin-top: 20px; color: #fff; }\\n" +\\n`;
                code += `            "            .crud-table th, .crud-table td { padding: 12px; border: 1px solid rgba(0,255,255,0.3); text-align: left; }\\n" +\\n`;
                code += `            "            .crud-table th { background: rgba(0,255,255,0.1); color: #0ff; }\\n" +\\n`;
                code += `            "            \\n" +\\n`;
                code += `            "            .modal { display: none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.85); backdrop-filter: blur(10px); justify-content: center; align-items: center;}\\n" +\\n`;
                code += `            "            .modal-content { background-color: #0d1117; padding: 30px; border: 2px solid #0ff; width: 450px; border-radius: 12px; box-shadow: 0 0 50px rgba(0,255,255,0.4); color: #fff;}\\n" +\\n`;
                code += `            "            .form-group { margin-bottom: 20px; }\\n" +\\n`;
                code += `            "            .form-group label { display: block; margin-bottom: 8px; color: #0ff; }\\n" +\\n`;
                code += `            "            .modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 25px; }\\n" +\\n`;
                code += `            "            ");\\n\\n`;
                code += `        center.add(customStyles);\\n        \\n`;
                code += `        Div mainContent = new Div();\\n`;
                code += `        mainContent.setStyle("padding", "20px");\\n\\n`;
                code += `        Header title = new Header(2, msg.getProperty("subtitle.${baseName.toLowerCase()}", "Mantenimiento de ${baseName}"))\\n`;
                code += `            .setStyle("color", "var(--jettra-accent)")\\n`;
                code += `            .setStyle("margin-bottom", "20px");\\n`;
                code += `        mainContent.add(title);\\n\\n`;
                code += `        setupModal(); // Initialize components before using them in listeners\\n\\n`;
                code += `        Button addBtn = new Button("\\u2795 " + msg.getProperty("btn.add.${baseName.toLowerCase()}", "A\\u00F1adir ${baseName}"))\\n`;
                code += `            .setId("addBtn")\\n`;
                code += `            .addClass("j-btn")\\n`;
                code += `            .setStyle("background-color", "#0f5132")\\n`;
                code += `            .addClickListener(() -> {\\n`;
                modelFields.forEach(field => {
                    const cName = field.name.charAt(0).toUpperCase() + field.name.slice(1);
                    if (field.type === 'String') code += `                this.model.set${cName}("");\\n`;
                    else if (field.type === 'Integer' || field.type === 'Long' || field.type === 'Double') code += `                this.model.set${cName}(null);\\n`;
                    else code += `                this.model.set${cName}(null);\\n`;
                });
                code += `                showModal(msg.getProperty("modal.add.${baseName.toLowerCase()}.title", "Nuevo ${baseName}"), "save");\\n`;
                code += `            });\\n`;
                code += `        mainContent.add(addBtn);\\n\\n`;
                code += `        // Table\\n`;
                code += `        io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable()\\n`;
                code += `            .addHeaderRow(\\n`;
                code += `                ${tblHeaders.join(',\\n                ')},\\n`;
                code += `                msg.getProperty("th.actions", "Acciones")\\n`;
                code += `            );\\n\\n`;
                code += `        List<${mName}> all = repository.findAll();\\n`;
                code += `        for (${mName} p : all) {\\n`;
                code += `            io.jettra.wui.components.TD actionsTd = new io.jettra.wui.components.TD()\\n`;
                code += `                .setStyle("display", "flex")\\n`;
                code += `                .setStyle("gap", "10px");\\n`;
                code += `            \\n`;
                
                const keyGetter = modelFields.length > 0 ? "get" + modelFields[0].name.charAt(0).toUpperCase() + modelFields[0].name.slice(1) + "()" : "getId()";
                
                code += `            Button editBtn = new Button("\\u270F\\uFE0F")\\n`;
                code += `                .addClass("j-btn")\\n`;
                code += `                .setId("edit-" + p.${keyGetter})\\n`;
                code += `                .addClickListener(() -> {\\n`;
                modelFields.forEach(field => {
                    const cName = field.name.charAt(0).toUpperCase() + field.name.slice(1);
                    code += `                    this.model.set${cName}(p.get${cName}());\\n`;
                });
                code += `                    showModal(msg.getProperty("modal.edit.${baseName.toLowerCase()}.title", "Editar ${baseName}"), "save");\\n`;
                code += `                });\\n`;
                code += `            \\n`;
                code += `            Button deleteBtn = new Button("\\uD83D\uDDD1\\uFE0F")\\n`;
                code += `                .addClass("j-btn")\\n`;
                code += `                .setId("del-" + p.${keyGetter})\\n`;
                code += `                .setStyle("color", "#ff5555")\\n`;
                code += `                .setStyle("border-color", "#ff5555")\\n`;
                code += `                .setStyle("background", "rgba(255,0,0,0.1)")\\n`;
                code += `                .addClickListener(() -> {\\n`;
                modelFields.forEach(field => {
                    const cName = field.name.charAt(0).toUpperCase() + field.name.slice(1);
                    code += `                    this.model.set${cName}(p.get${cName}());\\n`;
                });
                code += `                    showModal(msg.getProperty("modal.delete.${baseName.toLowerCase()}.title", "\\u00BFEliminar ${baseName}?"), "delete");\\n`;
                code += `                });\\n`;
                code += `            \\n`;
                code += `            actionsTd.add(editBtn).add(deleteBtn);\\n`;
                code += `            table.addRow(new io.jettra.wui.components.Row(\\n`;
                code += `                ${tblRows.join(',\\n                ')},\\n`;
                code += `                actionsTd\\n`;
                code += `            ));\\n`;
                code += `        }\\n`;
                code += `        mainContent.add(table);\\n        center.add(mainContent);\\n    }\\n\\n`;
                
                code += `    private void showModal(String title, String action) {\\n`;
                code += `        this.crudModal.setStyle("display", "flex");\\n`;
                code += `        this.modalTitle.setContent(title);\\n`;
                code += `        this.modalAction.setProperty("value", action);\\n`;
                code += `        \\n`;
                code += `        if ("delete".equals(action)) {\\n`;
                code += `            this.modalSubmitBtn.setContent(msg.getProperty("btn.confirm", "\\u00A1Confirmar!"));\\n`;
                code += `            this.modalSubmitBtn.setStyle("background-color", "rgba(255,0,0,0.6)");\\n`;
                code += `        } else {\\n`;
                code += `            this.modalSubmitBtn.setContent(msg.getProperty("btn.save", "Guardar"));\\n`;
                code += `            this.modalSubmitBtn.setStyle("background-color", "");\\n`;
                code += `        }\\n    }\\n\\n`;

                code += `    private void setupModal() {\\n`;
                code += `        this.crudModal = new Div();\\n`;
                code += `        this.crudModal.setId("crudModal").addClass("modal");\\n\\n`;
                code += `        Div modalBody = new Div();\\n`;
                code += `        modalBody.addClass("modal-content");\\n\\n`;
                code += `        this.modalTitle = new Header(3, msg.getProperty("modal.operation", "Operaci\\u00F3n"));\\n`;
                code += `        this.modalTitle.setId("modalTitle");\\n\\n`;
                code += `        Form form = new Form("${baseName.toLowerCase()}Form", JettraServer.resolvePath("/${baseName.toLowerCase()}"));\\n`;
                code += `        this.modalAction = new TextBox("hidden", "modalAction");\\n`;
                code += `        this.modalAction.setId("modalAction");\\n\\n`;
                code += modalFieldsCode;
                code += `        Div groupActions = new Div()\\n`;
                code += `            .addClass("modal-actions");\\n\\n`;
                code += `        Button cancelBtn = new Button(msg.getProperty("btn.close", "CERRAR"))\\n`;
                code += `            .setProperty("type", "button")\\n`;
                code += `            .addClass("j-btn")\\n`;
                code += `            .setStyle("background", "#555")\\n`;
                code += `            .setStyle("border", "none")\\n`;
                code += `            .setProperty("onclick", "document.getElementById('crudModal').style.display='none'; return false;");\\n\\n`;
                code += `        this.modalSubmitBtn = new Button(msg.getProperty("btn.save", "Guardar"))\\n`;
                code += `            .setId("modalSubmitBtn")\\n`;
                code += `            .addClass("j-btn")\\n`;
                code += `            .setProperty("type", "submit");\\n\\n`;
                code += `        groupActions.add(cancelBtn).add(this.modalSubmitBtn);\\n`;
                code += `        form.add(this.modalAction);\\n`;
                modelFields.forEach(field => {
                    const vName = field.name;
                    code += `        form.add(g_${vName});\\n`;
                });
                code += `        form.add(groupActions);\\n`;
                code += `        modalBody.add(this.modalTitle).add(form);\\n`;
                code += `        this.crudModal.add(modalBody);\\n`;
                code += `        this.add(this.crudModal);\\n    }\\n\\n`;

                code += `    @Override\\n    protected void onPost(Map<String, String> params) {\\n`;
                code += `        String action = params.get("modalAction");\\n`;
                modelFields.forEach(field => {
                    code += `        String ${field.name} = params.get("${field.name}");\\n`;
                });
                code += `        \\n`;
                code += `        boolean changed = false;\\n`;
                code += `        if (action != null) {\\n`;
                code += `            if (action.equals("save")) {\\n`;
                code += `                repository.save(model);\\n`;
                code += `                changed = true;\\n`;
                code += `            } else if (action.equals("delete")) {\\n`;
                code += `                repository.delete(model.${keyGetter});\\n`;
                code += `                changed = true;\\n`;
                code += `            }\\n`;
                code += `            JettraSyncManager.notifyChange("${mName}", SyncType.UPDATE, getLoggedUser(currentExchange));\\n`;
                code += `        }\\n\\n`;
                code += `        if (changed) {\\n`;
                code += `            try {\\n`;
                code += `                redirect(currentExchange, JettraServer.resolvePath("/${baseName.toLowerCase()}?lang=" + lang));\\n`;
                code += `            } catch (IOException e) {\\n`;
                code += `                System.err.println("Error during redirect: " + e.getMessage());\\n`;
                code += `            }\\n`;
                code += `        }\\n    }\\n}\\n`;

                document.getElementById('generated-code-display').value = code;
                document.getElementById('generated-code-hidden').value = code;
                
                // Switch tab to code
                const tab = document.querySelector('[data-tab="code"]');
                if (tab) tab.click();
                
                window.syncCodeToCanvas();
                
                window.show3DMessage("CRUD Generado", "Se ha generado la arquitectura MVC completa para " + baseName);
            };

            window.initDesigner = function() {
                window.jettraFileCache = {};
            };

            window.clearDesigner = function() {
                window.show3DConfirm("Limpiar", "\u00BFBorrar elementos?", () => {
                    document.getElementById('canvas-drop-area').innerHTML = '<div class="canvas-placeholder">Start dragging...</div>';
                    window.selectedItem = null;
                    window.updateInspector();
                    window.updateGeneratedCode();
                });
            };

            window.updateGeneratedCode = function() {
                if (window.isSyncing) return;
                const display = document.getElementById('generated-code-display');
                const hidden = document.getElementById('generated-code-hidden');
                if (!display || !hidden) return;

                const canvasItems = document.querySelectorAll('#canvas-drop-area > .canvas-item, .canvas-container > .canvas-item');
                if (canvasItems.length === 0) {
                    display.value = "// No components added yet";
                    return;
                }

                let code = `package com.jettra.example.pages;\\n\\nimport io.jettra.wui.complex.*;\\nimport io.jettra.wui.components.*;\\n\\n`;
                code += `public class GeneratedPage extends DashboardBasePage {\\n`;
                code += `    private ${window.viewModelName} model = ${window.viewModelName}.getInstance();\\n\\n`;
                code += `    protected void initCenter(Center center, String username) {\\n`;
                
                function walk(items, container) {
                    let out = "";
                    items.forEach(it => {
                        const type = it.getAttribute('data-type');
                        const props = JSON.parse(it.getAttribute('data-props'));
                        const v = props.id && props.id.trim() !== "" ? props.id : (type.toLowerCase() + "_" + Math.floor(Math.random()*10000));
                        
                        const chainProps = () => {
                            let c = "";
                            if (props.id && props.id.trim() !== "") c += `.setId("${props.id}")`;
                            if (props.backgroundColor) c += `\\n            .setBackgroundColor("${props.backgroundColor}")`;
                            if (props.padding) c += `\\n            .setPadding("${props.padding}")`;
                            if (props.border) c += `\\n            .setBorder("${props.border}")`;
                            if (props.maxWidth) c += `\\n            .setMaxWidth("${props.maxWidth}")`;
                            if (props.zIndex) c += `\\n            .setZIndex("${props.zIndex}")`;
                            if (props.display && props.display !== "block") c += `\\n            .setDisplay("${props.display}")`;
                            if (props.onclick) c += `\\n            .setOnclick("${props.onclick}")`;
                            if (props.type && props.type !== "button") c += `\\n            .setType("${props.type}")`;
                            if (props.value) c += `\\n            .setValue("${props.value}")`;
                            if (props.readonly) c += `\\n            .setReadonly(true)`;
                            
                            if (props.binding && props.binding.trim() !== "") {
                                if (type === 'TextBox' || type === 'TextArea') {
                                    c += `\\n            .bind("${props.binding}")`;
                                }
                            }
                            if (props.update) c += `.setUpdate("${props.update}")`;
                            if (props.cssClass) c += `.addClass("${props.cssClass}")`;
                            
                            // Events
                            Object.keys(props.events || {}).forEach(ev => {
                                const eventCode = props.events[ev].replace(/\\\\n/g, '\\n');
                                c += `\\n            .${ev}(${eventCode})`;
                            });
                            return c;
                        };

                        const simpleTypes = ['Divide', 'Separator', 'Header', 'Paragraph', 'Span', 'Label', 'Clock', 'ProgressBar', 'Spinner', 'Catcha', 'Clock', 'Capture', 'Catcha', 'CAPTURE', 'TrafficLight'];
                        
                        if (simpleTypes.includes(type)) {
                            let init = "";
                            if (type === 'Divide') init = `new Divide()`;
                            else if (type === 'Separator') init = `new Separator()`;
                            else if (type === 'Header') init = `new Header(${props.level || 1}, "${props.text}")`;
                            else if (type === 'Paragraph') init = `new Paragraph("${props.text}")`;
                            else if (type === 'Span') init = `new Span("${props.text}")`;
                            else if (type === 'Label') init = `new Label("${props.text}")`;
                            else if (type === 'Clock') init = `new Clock("${props.text || 'Clock'}")`;
                            else if (type === 'ProgressBar') init = `new ProgressBar(${props.value || 0}, ${props.max || 100})`;
                            else if (type === 'Spinner') init = `new Spinner("${v}", ${props.value || 0})`;
                            else init = `new ${type}()`;

                            if (init) {
                                out += `        ${container}.add(${init}${chainProps()});\\n`;
                                return;
                            }
                        }

                        switch(type) {
                            case 'QRReader':
                                out += `        ${container}.add(new QRReader("${v}")${chainProps()}`;
                                if (props.width) out += `\\n            .setWidth("${props.width}")`;
                                if (props.height) out += `\\n            .setHeight("${props.height}")`;
                                if (props.onScan) out += `\\n            .setOnScan("${props.onScan.replace(/"/g, '\\\\"')}")`;
                                out += `);\\n`;
                                break;
                            case 'Button': 
                                out += `        ${container}.add(new Button("${props.text}")${chainProps()}`;
                                if (props.btnStyle) out += `.setStyle(Button.Style.${props.btnStyle})`;
                                if (props.icon) out += `.setIcon("${props.icon}")`;
                                out += `);\\n`;
                                break;
                            case 'TextBox':
                            case 'TextArea':
                                out += `        ${container}.add(new ${type}("${props.text || type}", "${props.text || type}")${chainProps()});\\n`;
                                break;
                            case 'Avatar':
                                out += `        ${container}.add(new Avatar()${chainProps()}`;
                                if (props.text) out += `.setText("${props.text}")`;
                                if (props.icon) out += `.setIcon("${props.icon}")`;
                                out += `);\\n`;
                                break;
                            case 'LoginUI':
                                out += `        ${container}.add(new LoginUI()${chainProps()});\\n`;
                                break;
                            case 'SelectOne':
                            case 'SelectMany':
                            case 'SelectOneIcon':
                                out += `        ${container}.add(new ${type}("${v}")${chainProps()}`;
                                if (props.options) {
                                    const opts = props.options.split(',');
                                    opts.forEach(o => {
                                        const parts = o.split(':');
                                        const optVal = parts[0] ? parts[0].trim() : 'val';
                                        const optLabel = parts[1] ? parts[1].trim() : optVal;
                                        out += `\\n            .addOption("${optVal}", "${optLabel}")`;
                                    });
                                }
                                out += `);\\n`;
                                break;
                            case 'Modal': {
                                const mId = props.idGen || "modal_" + Math.floor(Math.random()*1000);
                                out += `        Modal ${v} = new Modal("${mId}")${chainProps()};\\n`;
                                const kidsM = it.querySelectorAll(':scope > .canvas-container > .canvas-item');
                                if (kidsM.length > 0) out += walk(kidsM, v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'TabView': {
                                out += `        TabView ${v} = new TabView("${props.text || 'TabView'}")${chainProps()}`;
                                if (props.orientation) out += `.setOrientation(TabView.Orientation.${props.orientation})`;
                                out += `;\\n`;
                                const kidsT = it.querySelectorAll(':scope > .tabview-wrapper > .canvas-container > .canvas-item');
                                if (kidsT.length > 0) out += walk(kidsT, v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'Card': {
                                out += `        Card ${v} = new Card()${chainProps()}`;
                                if (props.title) out += `.setTitle("${props.title}")`;
                                if (props.subtitle) out += `.setSubtitle("${props.subtitle}")`;
                                if (props.content) out += `.setContentText("${props.content}")`;
                                out += `;\\n`;
                                const kidsCard = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                                if (kidsCard.length > 0) out += walk(kidsCard, v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'FormGroup':
                            case 'Panel':
                            case 'Grid':
                            case 'Board':
                            case 'Div':
                            case 'TabView':
                            case 'Datatable':
                            case 'ViewDataTable': {
                                if (type === 'ViewDataTable') type = 'Div'; // Fallback to Div in UI code
                                out += `        ${type} ${v} = new ${type}()${chainProps()};\\n`;
                                if (type === 'TabView' && props.orientation) {
                                    out += `        ${v}.setOrientation(TabView.Orientation.${props.orientation});\\n`;
                                }
                                if (type === 'Datatable') {
                                    if (props.columnsList && props.columnsList.length > 0) {
                                        const colsStr = props.columnsList.map(c => `"${c}"`).join(", ");
                                        out += `        ${v}.addHeaderRow(${colsStr});\\n`;
                                    }
                                    const cells = it.querySelectorAll('tbody td');
                                    if (cells.length > 0) {
                                        out += `        Row row_${v} = new Row();\\n`;
                                        cells.forEach((td, idx) => {
                                            let tdVar = v + "_td_" + idx;
                                            out += `        TD ${tdVar} = new TD();\\n`;
                                            const kids = td.querySelectorAll(':scope > .canvas-item, :scope > div > .canvas-item');
                                            if (kids.length > 0) out += walk(kids, tdVar);
                                            out += `        row_${v}.add(${tdVar});\\n`;
                                        });
                                        out += `        ${v}.addRow(row_${v});\\n`;
                                    }
                                } else {
                                    const kids = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                                    if (kids.length > 0) out += walk(kids, v);
                                }
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            default: {
                                out += `        ${container}.add(new ${type}("${props.text || v}")${chainProps()});\\n`;
                            }
                        }
                    });
                    return out;
                }
                code += walk(document.querySelectorAll('#canvas-drop-area > .canvas-item'), "center");
                code += walk(document.querySelectorAll('#modal-list-container > .canvas-item'), "center");
                code += `    }\\n}`;
                
                display.value = code;
                hidden.value = code;
            };

            window.syncCodeToCanvas = function() {
                window.isSyncing = true;
                const display = document.getElementById('generated-code-display');
                if (!display) {
                    window.isSyncing = false;
                    return;
                }
                const code = display.value;
                const canvas = document.getElementById('canvas-drop-area');
                
                canvas.innerHTML = '';
                const mtArea = document.getElementById('modal-list-container');
                if (mtArea) mtArea.innerHTML = '';
                
                let foundAny = false;
                let varMap = {}; 

                const parseJettraText = (txt) => {
                    if (!txt) return "";
                    let result = "";
                    let parts = txt.split("+");
                    parts.forEach(p => {
                        let pt = p.trim();
                        let propMatch = /msg\\.getProperty\\s*\\(\\s*"[^"]*?"\\s*,\\s*"([^"]*?)"\\s*\\)/.exec(pt);
                        if (propMatch) {
                            result += propMatch[1];
                        } else {
                            let strMatch = /"([^"]*?)"/.exec(pt);
                            if (strMatch) result += strMatch[1];
                            else result += pt.replace(/"/g, '').trim();
                        }
                    });
                    return result;
                };

                const splitArgs = (argStr) => {
                    let results = [];
                    let current = "";
                    let parenCount = 0;
                    let inQuote = false;
                    for (let i = 0; i < argStr.length; i++) {
                        let c = argStr[i];
                        if (c === '"' && argStr[i-1] !== '\\\\') inQuote = !inQuote;
                        if (!inQuote) {
                            if (c === '(') parenCount++;
                            if (c === ')') parenCount--;
                            if (c === ',' && parenCount === 0) {
                                results.push(current.trim());
                                current = "";
                                continue;
                            }
                        }
                        current += c;
                    }
                    if (current) results.push(current.trim());
                    return results;
                };

                const extractBalanced = (str, token, fromPos = 0) => {
                    let pos = str.indexOf(token, fromPos);
                    if (pos === -1) return null;
                    let start = pos + token.length;
                    let count = 1;
                    let i = start;
                    while (i < str.length && count > 0) {
                        if (str[i] === '(') count++;
                        else if (str[i] === ')') count--;
                        i++;
                    }
                    return count === 0 ? str.substring(start, i - 1) : null;
                };

                let rawLines = code.split(/\\r?\\n/);
                let lines = [];
                let currentBuffer = "";
                rawLines.forEach(l => {
                    let t = l.trim();
                    if (t.length === 0 || t.startsWith("//")) return;
                    // Join lines that start with . or if the previous line doesn't end with a statement terminator
                    if (t.startsWith(".") || (currentBuffer.length > 0 && !currentBuffer.endsWith(";") && !currentBuffer.endsWith("{") && !currentBuffer.endsWith("}"))) {
                        currentBuffer += " " + t;
                    } else {
                        if (currentBuffer) lines.push(currentBuffer);
                        currentBuffer = t;
                    }
                });
                if (currentBuffer) lines.push(currentBuffer);
                
                // Extract Style content
                let styleContent = "";
                let inStyleStr = false;
                lines.forEach(line => {
                    if (line.includes('new Style(\"\"\"')) { inStyleStr = true; return; }
                    if (inStyleStr && line.includes('\"\"\");')) { inStyleStr = false; return; }
                    if (inStyleStr) styleContent += line + "\\n";
                });
                if (styleContent) {
                    let styleEl = document.getElementById('designer-custom-css');
                    if (!styleEl) {
                        styleEl = document.createElement('style');
                        styleEl.id = 'designer-custom-css';
                        document.head.appendChild(styleEl);
                    }
                    styleEl.innerHTML = styleContent;
                }
                
                let currentModalArea = mtArea;

                lines.forEach(line => {
                    let cleanLine = line.trim();
                    if (cleanLine.startsWith("//") || cleanLine.length === 0) return;

                    let currentVar = null;
                    
                    // Match object instantiation (including member assignments)
                    let mInstMatch = /(?:(?:[a-zA-Z0-9_.]+\\.)?([A-Z][a-zA-Z0-9_]*)\\s+)?(?:this\\.)?([a-zA-Z0-9_]+)\\s*=\\s*new\\s+(?:[a-zA-Z0-9_.]+\\.)?([A-Z][a-zA-Z0-9_]*)\\s*\\(/.exec(cleanLine);
                    
                    if (mInstMatch) {
                        foundAny = true;
                        let type = mInstMatch[1] || mInstMatch[3];
                        let vname = mInstMatch[2];
                        let args = extractBalanced(cleanLine, mInstMatch[0], mInstMatch.index);
                        
                        currentVar = vname;
                        
                        let tempParent = document.createElement('div');
                        window.addComponentToCanvas(type, tempParent);
                        let el = tempParent.lastElementChild;
                        if (el) {
                            el.setAttribute('data-var', vname);
                            varMap[vname] = el;
                            
                            let pObj = JSON.parse(el.getAttribute('data-props') || "{}");
                            pObj.id = vname;
                            if (!pObj.styles) pObj.styles = {};
                            if (!pObj.classes) pObj.classes = [];

                            // Extract chained properties in the same line
                            const sMatches = [...cleanLine.matchAll(/\\.setStyle\\("([^"]+)",\\s*"([^"]+)"\\)/g)];
                            sMatches.forEach(sm => { pObj.styles[sm[1]] = sm[2]; el.style[sm[1]] = sm[2]; });
                            const cMatches = [...cleanLine.matchAll(/\\.addClass\\("([^"]+)"\\)/g)];
                            cMatches.forEach(cm => { pObj.classes.push(cm[1]); el.classList.add(cm[1]); });

                            if (args && args.trim().length > 0) {
                                let cleanParams = splitArgs(args);
                                if (cleanParams.length > 0) {
                                    if (['Header','Paragraph','Button','Clock','Span','Label','Alert','Notification','Link','Downloader','CheckBox','RadioButton','ToggleSwitch','MenuItem','TreeItem','Tab','Card'].includes(type)) {
                                        let textArg = cleanParams.length > 1 ? cleanParams[1] : cleanParams[0];
                                        pObj.text = parseJettraText(textArg);
                                    }
                                    if (type === 'Modal') {
                                        pObj.idGen = cleanParams[0].replace(/"/g, '');
                                        pObj.text = cleanParams.length > 1 ? parseJettraText(cleanParams[1]) : pObj.idGen;
                                    }
                                    if (type === 'Board') pObj.text = cleanParams.length > 1 ? parseJettraText(cleanParams[1]) : (cleanParams.length > 0 ? parseJettraText(cleanParams[0]) : 'New Board');
                                    if (['Panel','Grid','Div','LayoutDisplay','TabView','Tree','MenuBar','FormGroup'].includes(type)){
                                        if (cleanParams[0] && !isNaN(parseInt(cleanParams[0].replace(/"/g, '')))) pObj.columns = parseInt(cleanParams[0].replace(/"/g, ''));
                                    }
                                    if (type === 'ProgressBar') { pObj.value = parseInt(cleanParams[0])||0; pObj.max = parseInt(cleanParams[1])||100; }
                                }
                            }
                            el.setAttribute('data-props', JSON.stringify(pObj));
                            const inner = el.querySelector('h3, h2, h1, p, button, label, .j-avatar, .j-panel-header, span');
                            if (pObj.text && inner) inner.innerText = pObj.text;
                        }
                    } else {
                        let firstVarMatch = /^(?:this\\.)?([a-zA-Z0-9_]+)\\./.exec(cleanLine);
                        if (firstVarMatch) currentVar = firstVarMatch[1];
                    }

                    if (!currentVar && !cleanLine.includes(".add")) return; 

                    let parentEl = varMap[currentVar];
                    if (currentVar === 'center' || currentVar === 'mainContent' || currentVar === 'crudModal' || currentVar === 'this' || !currentVar) {
                         if (!parentEl) parentEl = canvas;
                    }
                    
                    // Ensure we don't drop in ourselves if something goes wrong
                    if (parentEl === varMap[currentVar] && parentEl && parentEl === varMap[currentVar]) {
                         // Ok
                    }

                    // Special case for modal logic in PaisPage.java
                    if (cleanLine.includes('this.add(this.crudModal)')) {
                         varMap['crudModal'] = varMap['crudModal'] || document.querySelector('[data-var="crudModal"]');
                         if (varMap['crudModal']) canvas.appendChild(varMap['crudModal']);
                         return;
                    }

                    // Support addHeaderRow for Datatable
                    if (cleanLine.includes(".addHeaderRow")) {
                        let argContent = extractBalanced(cleanLine, ".addHeaderRow(", cleanLine.indexOf(".addHeaderRow"));
                        if (argContent !== null && parentEl && parentEl.getAttribute('data-type') === 'Datatable') {
                            let rawArgs = splitArgs(argContent);
                            let cols = rawArgs.map(arg => parseJettraText(arg));
                            
                            let props = JSON.parse(parentEl.getAttribute('data-props') || "{}");
                            props.columnsList = cols;
                            parentEl.setAttribute('data-props', JSON.stringify(props));
                            window.renderTableComponent(parentEl, props);
                        }
                    }

                    // Match all .add() calls
                    let addRegex = /\\.add\\s*\\(/g;
                    let addMatch;
                    while ((addMatch = addRegex.exec(cleanLine)) !== null) {
                        let childArg = extractBalanced(cleanLine, addMatch[0], addMatch.index);
                        if (!childArg) continue;
                        childArg = childArg.trim();
                        
                        if (parentEl) {
                           if (childArg.startsWith("new ")) {
                               let inlineTypeMatch = /new\\s+(?:[a-zA-Z0-9_.]+\\.)?([A-Z][a-zA-Z0-9_]*)\\s*\\(/.exec(childArg);
                               if (inlineTypeMatch) {
                                   let type = inlineTypeMatch[1];
                                   let args = extractBalanced(childArg, inlineTypeMatch[0]);
                                   let targetContainer = parentEl.querySelector('.canvas-container') || parentEl;
                                   
                                   window.addComponentToCanvas(type, targetContainer);
                                   let el = targetContainer.lastElementChild;
                                   if(el) {
                                       let props = JSON.parse(el.getAttribute('data-props') || "{}");
                                       if (args && args.trim().length > 0) {
                                          let cleanParams = splitArgs(args);
                                          if (['Header','Paragraph','Button','Clock','Span','Label','Card'].includes(type)) {
                                             let textArg = cleanParams.length > 1 ? cleanParams[1] : cleanParams[0];
                                             props.text = parseJettraText(textArg);
                                          }
                                       }
                                       el.setAttribute('data-props', JSON.stringify(props));
                                       const inner = el.querySelector('h3, h2, h1, p, button, label, span');
                                       if (props.text && inner) inner.innerText = props.text;
                                   }
                               }
                           } else if(!childArg.includes(".") || childArg.startsWith("this.") || childArg.startsWith("p.")) {
                               let vName = childArg.replace("this.", "").replace("p.", "");
                               let childEl = varMap[vName];
                               if (childEl) {
                                   let targetContainer = parentEl;
                                   if (parentEl !== canvas && parentEl.classList.contains('canvas-item')) {
                                       let innerCont = parentEl.querySelector('.canvas-container');
                                       if (!innerCont) {
                                           let possibleContainers = parentEl.querySelectorAll('div');
                                           if (possibleContainers.length > 0) targetContainer = possibleContainers[possibleContainers.length-1];
                                       } else {
                                           targetContainer = innerCont;
                                       }
                                   }
                                   targetContainer.appendChild(childEl);
                               }
                           }
                        }
                    }

                    // Match all .setXxx() / .addClass() / .setId() calls
                    let propMatches = [...cleanLine.matchAll(/\\.(set|add|bind)([A-Z][a-zA-Z0-9_]*)\\s*\\((.*?)\\)/g)];
                    propMatches.forEach(mProp => {
                       let prefix = mProp[1];
                       let method = mProp[2];
                       let args = mProp[3].replace(/"/g, '').trim();
                       let el = varMap[currentVar];
                       if (el) {
                           let props = JSON.parse(el.getAttribute('data-props') || "{}");
                           if (method === 'Content' || method === 'Text' || (prefix === 'set' && method === 'Id')) props.text = args;
                           if (method === 'Id') props.id = args;
                           if (method === 'Class') props.cssClass = args;
                           if (method === 'Update') props.update = args;
                           if (method === 'Binding' || prefix === 'bind') props.binding = args;
                           
                           el.setAttribute('data-props', JSON.stringify(props));
                           if (props.text) {
                               const inner = el.querySelector('h3, h2, h1, p, button, label, span');
                               if (inner) inner.innerText = props.text;
                           }
                       }
                    });
                });
                
                Object.values(varMap).forEach(el => {
                    if (!canvas.contains(el) && el.parentElement && !el.parentElement.classList.contains('canvas-container')) {
                        if (el.getAttribute('data-type') === 'Modal' && mtArea) mtArea.appendChild(el);
                        else canvas.appendChild(el);
                    }
                });

                if (Object.keys(varMap).length === 0 && !foundAny) {
                    canvas.innerHTML = '<div class="canvas-placeholder">Start dragging components here to design</div>';
                }
                
                document.getElementById('generated-code-hidden').value = code;
                display.value = code;
                window.show3DMessage("Sync Complete", "Canvas has been rebuilt exactly from the structured Java source code.");
                setTimeout(() => { window.isSyncing = false; }, 100);
            };

            window.previewInterface = function() {
                const canvas = document.getElementById('canvas-drop-area');
                const pcontent = document.getElementById('preview-content-area');
                if (!canvas || !pcontent) return;
                
                pcontent.innerHTML = canvas.innerHTML;
                
                const draggables = pcontent.querySelectorAll('[draggable]');
                draggables.forEach(d => {
                    d.removeAttribute('draggable');
                    d.oncontextmenu = null;
                    d.onclick = null;
                    d.classList.remove('selected');
                });
                
                const placeholders = pcontent.querySelectorAll('.canvas-placeholder');
                placeholders.forEach(p => p.remove());

                const deleteTools = pcontent.querySelectorAll('.delete-tool');
                deleteTools.forEach(d => d.remove());
                
                const items = pcontent.querySelectorAll('.canvas-item, .canvas-container, .board-container-mock, .modal-container-mock');
                items.forEach(c => {
                    c.classList.remove('canvas-item');
                    c.classList.remove('canvas-container');
                    c.classList.remove('selected');
                    if (c.style.border && c.style.border.includes('dashed')) {
                        c.style.border = 'none';
                    }
                });
                
                const header = pcontent.querySelector('#canvas-header');
                if (header) header.style.display = 'none';

                document.getElementById('preview-modal').style.display = 'block';
            };

        """);
         
         Script script = new Script(sb.toString().replace("---VIEWMODEL_NAME---", viewModelName));
        
        center.add(style);
        center.add(script);
    }
}
