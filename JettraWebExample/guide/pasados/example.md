# Guía Práctica de Interfaces con JettraWUI

Las interfaces en JettraWebExample obvian el uso de plantillas de texto en la medida de lo posible; todo se realiza a través de código Java para garantizar el tipeo estricto, la rehusabilidad y el encapsulamiento asíncrono.

## Definiendo un Componente Visual

Dado que JettraWUI provee facilidades abstractas para UI 3D y componentes 2D modernos, su ciclo clásico consiste en instanciar un tipo de Vista o `Page`, nutrirlo de módulos lógicos y adjuntarlo al contexto que despachará `JettraServer`.

**Paso 1: Configura tus variables globales**  
En el classpath (`jettra-config.properties`):
```properties
app.theme=cyberpunk
app.title=My Dashboard
```

**Paso 2: Inyecta e inicializa los bloques del DOM WUI**  
(Mostrando lógica simulada usando la inyección central dependiente)
```java
public void setupView() {
    Page3DFuturisticView view = new Page3DFuturisticView();
    FormLayout loginForm = new GlassmorphismForm(this.appTitle);
    
    Button loginBtn = new NeonButton("Ingresar");
    loginForm.add(loginBtn);
    view.add(loginForm);
    
    // El motor WUI finaliza serializando esta vista, lista para que el core la envíe al navegador:
    JettraWUIContext.render(view);
}
```

Espera la inicialización de puerto estipulado (por defecto `8080`) al lanzar la aplicación para acceder de inmediato al front-end renderizado por sus herramientas en el backend Java de cero latencia.
