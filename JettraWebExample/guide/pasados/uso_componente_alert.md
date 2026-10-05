# Uso del componente Alert en JettraWUI

El componente `Alert` ha sido creado dentro de `JettraWUI` para facilitar la visualización de mensajes de advertencia, error o información en el navegador, sin necesidad de recurrir a JavaScript externo ni HTML manual.

## Creación y Propiedades

La clase `io.jettra.wui.components.Alert` hereda de `UIComponent` y renderiza un elemento `<div>` estandarizado como una alerta.

### 1. Inicialización
Por defecto, la alerta inicializa oculta (`display: none`), previniendo artefactos visuales indeseados en la carga inicial de la página.

```java
import io.jettra.wui.components.Alert;

Alert miAlerta = new Alert();
```

### 2. Establecer el tipo de mensaje
El método `setType(String type)` permite configurar el esquema de colores de la alerta de acuerdo a la severidad del mensaje. Los valores soportados son:
* `"warning"`: Tonos amarillos/naranjas. (Advertencia)
* `"error"`: Tonos rojos. (Error)
* `"info"`: Tonos azules. (Información)
* `"success"`: Tonos verdes. (Éxito)

```java
miAlerta.setType("warning");
```

### 3. Mostrar un mensaje
Para desplegar un mensaje en la interfaz, se utiliza el método `showMessage(String message)`. Esto actualiza el contenido interno y hace que el componente sea visible.

```java
miAlerta.showMessage("Advertencia: Username y password no válidos");
```

### 4. Ocultar el mensaje
Si es necesario ocultar la advertencia posteriormente (por ejemplo, en una re-renderización), se expone el método `hide()`.

```java
miAlerta.hide();
```

## Ejemplo de Integración (LoginPage)

En el proyecto `JettraWebExample`, el componente `Login` fue actualizado para contener un `Alert` internamente (`getAlertMessage()`). Cuando ocurre un inicio de sesión fallido, redirigimos a la misma vista enviando una señal en la URL, la cual se detecta durante el método GET:

```java
// Dentro del método handle(...)
if ("GET".equals(exchange.getRequestMethod())) {
    this.children.clear(); // Limpiamos los hijos
    initUI(); // Inicializa de nuevo la GUI, incluyendo la alerta
    
    String query = exchange.getRequestURI().getQuery();
    if (query != null && query.contains("error=invalid_credentials")) {
        // Obtenemos la alerta desde el componente Login
        loginForm.getAlertMessage().showMessage("Advertencia: Username y password no válidos");
        loginForm.getAlertMessage().setType("warning");
    }
    
    // ...renderizado del html y response HTTP
}
```
