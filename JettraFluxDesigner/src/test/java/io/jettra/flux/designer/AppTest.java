package io.jettra.flux.designer;

import io.jettra.flux.designer.model.CanvasWidget;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.service.CodeGeneratorService;
import io.jettra.flux.designer.service.DesignerSessionState;
import io.jettra.flux.designer.service.JavaPageParserService;
import io.jettra.flux.designer.service.JavaPageParserService.ParsedPageInfo;
import io.jettra.flux.designer.service.MavenProjectService;
import io.jettra.test.annotation.JettraTest;
import io.jettra.test.core.JettraAssert;

import java.io.File;

public class AppTest {

    @JettraTest
    public void testParseCardDemoPage() {
        JavaPageParserService parser = JavaPageParserService.getInstance();
        JettraAssert.assertNotNull(parser, "JavaPageParserService no debe ser nulo");

        File cardPageFile = new File("../JettraFluxExample/src/main/java/com/flux/plugin/example/pages/apps/CardDemoPage.java");
        if (cardPageFile.exists()) {
            ParsedPageInfo info = parser.parseJavaPageFile(cardPageFile);
            JettraAssert.assertNotNull(info, "La información parseada no debe ser nula");
            JettraAssert.assertEquals("CardDemoPage", info.className(), "El nombre de la clase debe ser CardDemoPage");
            JettraAssert.assertEquals("com.flux.plugin.example.pages.apps", info.packageName(), "El paquete debe coincidir");
            JettraAssert.assertNotNull(info.rootWidget(), "El widget raíz parseado no debe ser nulo");

            // Verify that the parser identified the Card component
            boolean hasCard = "Card".equalsIgnoreCase(info.rootWidget().getType()) ||
                info.rootWidget().getChildren().stream().anyMatch(c -> "Card".equalsIgnoreCase(c.getType()));
            JettraAssert.assertTrue(hasCard, "El árbol parseado debe contener el componente Card");

            // Verify children inside Card
            CanvasWidget cardWidget = "Card".equalsIgnoreCase(info.rootWidget().getType())
                ? info.rootWidget()
                : info.rootWidget().getChildren().stream().filter(c -> "Card".equalsIgnoreCase(c.getType())).findFirst().orElse(null);
            JettraAssert.assertNotNull(cardWidget, "El widget Card debe existir");
            JettraAssert.assertTrue(!cardWidget.getChildren().isEmpty(), "El Card debe contener componentes hijos");
        }
    }

    @JettraTest
    public void testProjectFileTreeExplorer() {
        MavenProjectService service = MavenProjectService.getInstance();
        File exampleDir = new File("../JettraFluxExample");
        if (exampleDir.exists()) {
            MavenProjectInfo info = service.inspectProject(exampleDir.getAbsolutePath());
            JettraAssert.assertNotNull(info, "MavenProjectInfo no debe ser nulo");
            JettraAssert.assertNotNull(info.getRootNode(), "El árbol raíz de archivos no debe ser nulo");
            JettraAssert.assertTrue(info.getRootNode().isDirectory(), "La raíz debe ser un directorio");
            JettraAssert.assertTrue(!info.getRootNode().getChildren().isEmpty(), "Debe tener subdirectorios y archivos");
            JettraAssert.assertTrue(!info.getExistingPages().isEmpty(), "Debe detectar páginas Java en el proyecto");
        }
    }

    @JettraTest
    public void testDesignerSessionStateAndCanvas() {
        DesignerSessionState state = DesignerSessionState.getInstance();
        JettraAssert.assertNotNull(state, "DesignerSessionState no debe ser nulo");

        CanvasWidget root = state.getRootCanvas();
        JettraAssert.assertNotNull(root, "Lienzo raíz no debe ser nulo");

        int initialChildren = root.getChildren().size();
        state.addWidgetToCanvas("Card", root.getId());
        JettraAssert.assertTrue(root.getChildren().size() > initialChildren, "Debe agregarse un nuevo widget al lienzo");

        String selectedId = state.getSelectedWidgetId();
        JettraAssert.assertNotNull(selectedId, "Debe haber un widget seleccionado tras agregarlo");

        boolean removed = state.removeWidgetFromCanvas(selectedId);
        JettraAssert.assertTrue(removed, "El widget debe ser removido con éxito del árbol");
    }

    @JettraTest
    public void testCodeGeneratorDirectSave() {
        CodeGeneratorService codeGen = CodeGeneratorService.getInstance();
        CanvasWidget canvas = new CanvasWidget("Column", "Raíz", "");
        canvas.addChild(new CanvasWidget("Header", "Encabezado", "Panel de Control"));

        String javaCode = codeGen.generateJavaClass("com.demo.test", "TestExportPage", "/test-export", "ADMIN", canvas);
        JettraAssert.assertNotNull(javaCode, "El código generado no debe ser nulo");
        JettraAssert.assertTrue(javaCode.contains("public class TestExportPage extends FluxBaseHandler"), "Debe generar la clase");

        // Test saveDirectlyToFile
        File tempFile = new File("target/TestExportPage.java");
        boolean saved = codeGen.saveDirectlyToFile(tempFile.getAbsolutePath(), javaCode);
        JettraAssert.assertTrue(saved, "Debe guardar directamente en el archivo en disco");
        JettraAssert.assertTrue(tempFile.exists(), "El archivo guardado debe existir");
    }
}
