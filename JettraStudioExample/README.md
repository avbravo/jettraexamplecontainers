# JettraStudioExample 🚀

**JettraStudioExample** es un proyecto de demostración completo e interactivo del framework **JettraStudio** en el ecosistema **Jettra**, aprovechando todas las capacidades de **Java 25+** y **Project Loom (Virtual Threads)**.

---

## 🎯 Componentes Demostrados

El proyecto presenta una cobertura completa de la suite de componentes de JettraStudio:

1. **Tipografía y Textos**:
   - `Label`: Texto plano seguro con escape HTML contra inyecciones XSS.
   - `MultiLineLabel`: Formateo automático de saltos de línea `\n` en `<br/>`.
2. **Botones e Interactividad**:
   - `Button` en todas sus variantes estéticas de JettraFlux: `GOLD`, `BLUE`, `LIME`, `RED`, `PURPLE`, `DARK`, `PRIMARY`.
   - Callbacks de acción desacoplados con lambdas Java.
3. **Formularios y Controles Reactivos**:
   - `Form<T>`: Manejo transparente de formularios POST / GET.
   - `TextField<T>`: Inputs de texto con placeholder y required.
   - `TextArea<T>`: Entradas multilínea con rows y cols.
   - `CheckBox`: Control booleano vinculado a propiedades.
   - `Select<T>`: Listas desplegables con colecciones tipadas.
   - `FeedbackPanel`: Mensajes de validación y estado.
4. **Presentación y Estructura**:
   - `Card`: Tarjetas de contenido con badge, título y subtítulo.
   - `Alert`: Notificaciones contextuales (`SUCCESS`, `INFO`, `WARNING`, `DANGER`).
   - `Modal`: Diálogos emergentes interactivos con apertura/cierre.
   - `Panel`: Componentes modulares reutilizables con su propio archivo HTML (`MetricCardPanel`).
   - `Link`: Enlaces de navegación interna y externa.
   - `Image`: Control de imágenes y dimensiones.
5. **Colecciones y Bucles Dinámicos**:
   - `ListView<T>` y `ListItem<T>`: Iteración sobre Records de Java 25 (`Product`).
   - Acciones dentro de filas (botones de compra y detalle).
6. **Modelos Reactivos**:
   - `Model.of(...)`: Referencias directas de objetos.
   - `PropertyModel.of(bean, "prop")`: Enlace reflexivo a JavaBeans y Records.
   - `LambdaModel.of(...)`: Expresiones lambda dinámicas (ej: reloj del servidor).
   - `ResourceModel.of("clave")`: Enlace a recursos i18n multilingües.
7. **Diseño Maestro y Temas**:
   - `BasePage` con punto de inserción `<jettras:child/>`.
   - `<jettras:theme-selector/>` integrado con los 14 temas de JettraFlux.

---

## 📂 Estructura del Proyecto

```
JettraStudioExample/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/io/jettra/examples/studio/
    │   │   ├── JettraStudioExampleApp.java       # Servidor HTTP Java 25 Loom
    │   │   ├── components/
    │   │   │   └── MetricCardPanel.java          # Panel reutilizable de métricas
    │   │   ├── model/
    │   │   │   └── Product.java                  # Record Java 25
    │   │   └── pages/
    │   │       ├── HomePage.java                 # Dashboard principal
    │   │       ├── ComponentsShowcasePage.java   # Vitrina exhaustiva de componentes
    │   │       └── CatalogPage.java              # Inventario y ListView reactivo
    │   └── resources/
    │       ├── messages.properties               # Textos de internacionalización
    │       ├── messages_es.properties
    │       ├── messages_en.properties
    │       └── io/jettra/examples/studio/
    │           ├── components/
    │           │   └── MetricCardPanel.html
    │           └── pages/
    │               ├── HomePage.html
    │               ├── ComponentsShowcasePage.html
    │               └── CatalogPage.html
    └── test/
        └── java/io/jettra/examples/studio/
            └── JettraStudioExampleTest.java      # Pruebas con JettraTest
```

---

## 🚀 Compilación y Ejecución

### 1. Compilar y Ejecutar Pruebas
```bash
mvn clean test
```

### 2. Iniciar el Servidor Web
```bash
mvn exec:java -Dexec.mainClass="io.jettra.examples.studio.JettraStudioExampleApp"
```

O directamente ejecutando la clase:
```bash
java -jar target/JettraStudioExample-1.0.0-SNAPSHOT.jar [puerto]
```

### 3. Rutas Disponibles en el Navegador
- **Dashboard Principal**: `http://localhost:8085/`
- **Vitrina de Componentes**: `http://localhost:8085/components`
- **Catálogo de Inventario**: `http://localhost:8085/catalog`
