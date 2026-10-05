# JettraWebExample: Interactive Component Showcase

Welcome to the JettraWebExample project. This repository contains a comprehensive collection of live examples demonstrating the power and flexibility of the **JettraWUI** framework.

## 1. Project Structure
- `src/main/java/com/jettra/example/pages/`: Contains the implementation of each showcase page.
- `guide/`: Detailed documentation for specific features and patterns.

---

## 2. Examples Catalog

### Forms & Inputs
Demonstrations of data entry components with validation and event handling.
- **[TextBox & TextArea](../src/main/java/com/jettra/example/pages/TextBoxPage.java)**: Standard text inputs.
- **[SelectOne & SelectMany](../src/main/java/com/jettra/example/pages/SelectManyPage.java)**: Enhanced selection components with custom rendering.
- **[SelectOneIcon](../src/main/java/com/jettra/example/pages/SelectOneIconPage.java)**: Selection with visual icons and dynamic item addition.
- **[CheckBox & RadioButtons](../src/main/java/com/jettra/example/pages/RadioButtonPage.java)**: Binary and group selections.
- **[DatePicker & Time](../src/main/java/com/jettra/example/pages/DatePickerPage.java)**: Temporal data input.
- **[CreditCard](../src/main/java/com/jettra/example/pages/CreditCardPage.java)**: Specialized credit card input with 3D effects.
- **[OTPValidator](../src/main/java/com/jettra/example/pages/OTPValidatorPage.java)**: Multi-field secure code input.

### Layout & Containers
Building modern layouts using JettraWUI's layout engine.
- **[Grid & Div](../src/main/java/com/jettra/example/pages/GridPage.java)**: Fundamental layout building blocks.
- **[Card & Panel](../src/main/java/com/jettra/example/pages/PanelPage.java)**: Styled containers with glassmorphism and 3D features.
- **[TabView](../src/main/java/com/jettra/example/pages/TabViewPage.java)**: Tabbed navigation within a page.
- **[FormGroup](../src/main/java/com/jettra/example/pages/FormGroupPage.java)**: Semantic grouping of form elements.

### Data Visualization
Rich data representation components.
- **[DataTable](../src/main/java/com/jettra/example/pages/DataTablePage.java)**: Interactive tables with sorting and paging.
- **[Charts](../src/main/java/com/jettra/example/pages/ChartsBarPage.java)**: Bar, Pie, Line, Radar, and Doughnut charts powered by Chart.js.
- **[Tree](../src/main/java/com/jettra/example/pages/TreePage.java)**: Hierarchical data exploration.
- **[Timeline](../src/main/java/com/jettra/example/pages/TimelinePage.java)**: Chronological event display.

### Specialized Components
High-level components for specific use cases.
- **[Kanban & Board](../src/main/java/com/jettra/example/pages/KanbanPage.java)**: Visual task and project management.
- **[Schedule](../src/main/java/com/jettra/example/pages/SchedulePage.java)**: Full-featured calendar and event scheduler.
- **[Map](../src/main/java/com/jettra/example/pages/MapPage.java)**: Interactive geographic data.
- **[PDFViewer](../src/main/java/com/jettra/example/pages/PDFViewerPage.java)**: In-browser document viewing.
- **[WebDesigner](../src/main/java/com/jettra/example/pages/WebDesignerPage.java)**: A visual drag-and-drop editor for JettraWUI pages.

### Feedback & Utilities
- **[Alert & Notification](../src/main/java/com/jettra/example/pages/AlertPage.java)**: Real-time user feedback.
- **[Loading & ProgressBar](../src/main/java/com/jettra/example/pages/LoadingPage.java)**: Async state indicators.
- **[BarCode & QR](../src/main/java/com/jettra/example/pages/QRPage.java)**: Dynamic code generation.
- **[FileUpload & Downloader](../src/main/java/com/jettra/example/pages/FileUploadPage.java)**: Binary asset handling.

---

## 3. How to Run
1. Ensure you have the `JettraWUI` and `JettraServer` projects built.
2. Run the main class in `JettraWebExample`.
3. Open `http://localhost:8080` in your browser.
4. Navigate through the sidebar to explore different components.

---

## 4. Documentation Links
- **[Architecture Guide](arquitectura.md)**: Deep dive into how JettraWUI works.
- **[Component Usage](components_usage.md)**: Best practices for implementing UI elements.
- **[Properties Guide](properties.md)**: Configuring the framework via properties files.

---

*© 2026 JettraStack - All rights reserved.*
