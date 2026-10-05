# Arquitectura de JettraWebExample

Este proyecto es una aplicación de demostración que integra el motor backend **JettraServer** con el framework de interfaces de usuario **JettraWUI**, ambos optimizados sobre **Java 25**.

## Patrón de Arquitectura

El proyecto está diseñado bajo un modelo híbrido embebido que aglutina configuración, backend y front-end web puramente con sintaxis Java:

1. **Frontend Abstracto (JettraWUI)**: En el método `initUI()`, los componentes de presentación (como paneles, canvas 3D futuristas y formularios *Glassmorphism*) son definidos enteramente en código Java mediante las clases base de la abstracción WUI. No requiere manejo directo de HTML ni React o Angular separados por defecto en este modelo.
   
2. **Servidor HTTP Embebido (JettraServer)**: JettraServer interviene levantando la instancia central (`JettraServer.main`) y enrutará internamente las peticiones HTTP y devolverá los recursos DOM, pre-renderizando el código Java abstracto como front-end estricto para el navegador.

3. **Inyección de Configuración Central**: Toda la aplicación usa la inyección basada en anotaciones de JettraServer (`@JettraConfigProperty`) extrayendo valores estructurados desde archivos externos estáticos o de variables de entorno de Docker (`jettra-config.properties`).

## Gestión de Estado y Cierre de Sesión (Log out)

Para manejar la persistencia de usuarios e interrupciones dinámicas antes de salir:

1. **Autorización y Guardias (Auth Guard):**
   Las páginas que exijan acreditación implementan internamente la interface `HttpHandler` delegada por el servidor `JettraServer`. Revisan una cabecera nativa HTTP temporal inyectada como cookie (p.ej. `username`) antes de invocar la reconstrucción del DOM. Si la validación falla ("Guest"), se detiene el renderizado retornando al invocador una respuesta `302` hacia el controlador base `/`.

2. **Cronómetro y Componente Abstracto Client-Side:**
   La arquitectura permite enviar temporizadores basados puramente en WUI. Al inyectar el componente `SessionTimeoutDialog` parametrizado con `server.session.timeout` (extraído del properties), la aplicación lanza a fondo un bloque JavaScript asíncrono.
   - Envía advertencias tipo Modal al navegador faltando cierto margen (p.ej. 60 segundos finales) sin bloquear subprocesos back-end (`Non-blocking`).
   - Una vez finalizada la cuenta o bajo el click explícito al botón **Cerrar Sesión** en el `Top`, la aplicación salta al disparador de escape `/logout`.

3. **Cierre de Sesión Integral (`/logout` y timeouts exhaustivos):**
   El controlador maestro en *LoginPage* al recibir `/logout` revoca la validez de la cookie forzando su expiración con encabezados HTTP limpios (`Max-Age=0`), impidiendo así saltos de autorización directos hacia las pantallas protegidas como el `/dashboard` en un próximo reintento no logeado.
   Asimismo, el propio contenedor `JettraServer` lee dinámicamente el mismo time-out para ejecutar interrupciones seguras (usando sus propiedades Java `stop(delay)`).

**Validar cierre de sesion advertencia y cierre de sesion**