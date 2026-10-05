# JettraStudioExample 🚀

**JettraStudioExample** es el proyecto de demostración completo e interactivo del framework **JettraStudio** en el ecosistema **Jettra**, integrando los patrones arquitectónicos de **JettraFlux** adaptados a la pureza de componentes HTML5 + Java 25 y **Virtual Threads (Project Loom)**.

---

## 🌟 Características Integradas

### 1. Inyección de Configuración desde `jettra-config.properties`
- Archivo centralizado [`src/main/resources/jettra-config.properties`](src/main/resources/jettra-config.properties).
- Inyección automática mediante `@JettraConfigProperty` y `ConfigInjector.inject(this)`.
- Parámetros configurables:
  - `app.title` & `app.shorttitle`
  - `server.port` & `server.contextpath`
  - `app.language` & `app.theme`
  - Credenciales seguras para pruebas (`security.admin.*`, `security.demo.*`)

### 2. Clase `App.java` con Gestión de Argumentos CLI
- Ubicación: [`io.jettra.examples.studio.App`](src/main/java/io/jettra/examples/studio/App.java).
- Soporte para flags de línea de comandos en tiempo de ejecución:
  - `--server.port=XXXX`, `--port=XXXX`, `-p XXXX`
  - `--server.contextpath=XXXX`, `--contextpath=XXXX`, `-c XXXX`
- Arranque del servidor HTTP empotrado sobre **Hilos Virtuales de Java 25**.
- Configuración de tema e idioma base cargados desde archivo.

### 3. Formulario de Login Interactivo
- Páginas: [`LoginPage.java`](src/main/java/io/jettra/examples/studio/pages/LoginPage.java) y [`LoginPage.html`](src/main/resources/io/jettra/examples/studio/pages/LoginPage.html).
- Características:
  - Formulario reactivo `Form<Void>` con envío POST a `/login`.
  - Validación de campos requeridos y de credenciales inválidas con banner `Alert.danger`.
  - Soporte de cookies de sesión (`jettra_user`, `jettra_role`).
  - Redirección automática a `/dashboard` tras autenticación exitosa.
  - Cierre de sesión seguro con `?logout=true`.
  - Credenciales demo preconfiguradas: `admin` / `admin123` y `demo` / `demo123`.

### 4. Dashboard de 4 Cuadrantes (`Top`, `Left`, `Center`, `Footer`)
Basado en el diseño maestro [`TemplatePage`](src/main/java/io/jettra/examples/studio/pages/TemplatePage.java) y [`DashboardPage`](src/main/java/io/jettra/examples/studio/pages/DashboardPage.java):

- **TOP**:
  - Barra superior con botón toggle de sidebar.
  - Título contextual del Dashboard.
  - Iconos de notificaciones (Global 🌐, Personal ✉️, Canal 📢).
  - Selector dinámico de idioma con banderas (🇪🇸 / 🇺🇸) e interpolación i18n.
  - Selector de temas `<jettras:theme-selector/>` conectado a los 14 temas de JettraFlux.
  - Perfil de usuario con avatar, nombre, rol y botón de logout directo.
- **LEFT** (Sidebar):
  - Logo estilizado `JettraStudio Pro`.
  - Categorías organizadas: "Navegación", "Comercio & Datos", "Componentes UI", "Cuenta".
  - Enlaces de navegación con iconos visuales.
- **CENTER** (Contenido Principal):
  - Fila de 4 **StatCards**:
    - *Conversión*: `0.80%` (`+0.81%`)
    - *Valor Promedio de Orden*: `$306.20` (`+4.20%`)
    - *Cantidad de Pedidos*: `1,620` (`-2.10%`)
    - *Hilos Loom*: `250,000 req/s` (`99.98%`)
  - **Visitor Growth Card**: Métricas de MRR (`$620,076.00`), promedio por cliente (`$1,120.00`) y gráfico de barras CSS interactivo.
  - **Transaction History Card**: Listado de transacciones recientes con iconos de estado (pago, reembolso, suscripción).
- **FOOTER**:
  - Pie de página con copyright y año dinámico.

---

## 📂 Rutas Web Disponibles

| Ruta | Descripción |
|---|---|
| `/` o `/login` | Formulario de autenticación con validación y cookies |
| `/dashboard` | Dashboard maestro con diseño Top, Left, Center y Footer |
| `/catalog` | Inventario y catálogo dinámico con `ListView<Product>` |
| `/components` | Vitrina exhaustiva de todos los componentes JettraStudio |

---

## 🚀 Ejecución

### Ejecutar Pruebas Automatizadas
```bash
mvn clean test
```

### Iniciar la Aplicación
```bash
mvn exec:java -Dexec.mainClass="io.jettra.examples.studio.App"
```

O especificando puerto y contexto por consola:
```bash
mvn exec:java -Dexec.mainClass="io.jettra.examples.studio.App" -Dexec.args="--port=9090"
```
