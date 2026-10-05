# Manejo de Propiedades e Internacionalización (i18n)

JettraStack ofrece un sistema automatizado para manejar archivos de propiedades e internacionalización mediante la anotación `@InjectProperties`.

## La anotación @InjectProperties

La anotación `@InjectProperties` permite inyectar un objeto `java.util.Properties` directamente en un campo de una clase `Page`. El sistema detecta automáticamente el idioma preferido del usuario basado en el parámetro `lang` de la URL.

### Uso Básico

Para utilizar esta funcionalidad, simplemente declare un campo de tipo `Properties` con la anotación:

```java
@InjectProperties(name = "messages")
private Properties msg;
```

### Funcionamiento Automático

1. **Búsqueda por Idioma**: Si el parámetro `lang=en` está presente en la URL, el sistema buscará el archivo `messages_en.properties` en el directorio `src/main/resources`.
2. **Fallback**: Si no se encuentra el archivo específico del idioma (por ejemplo, `messages_fr.properties`), el sistema intentará cargar el archivo base `messages.properties`.
3. **Inyección en el Ciclo de Vida**: La inyección ocurre automáticamente antes de que se llame al método `onInit(Map params)`, por lo que las propiedades ya están disponibles para su uso en `initCenter`.

## Archivos de Propiedades

Los archivos de propiedades deben estar en el classpath (usualmente en `src/main/resources`). Se recomienda seguir el formato:

- `messages_es.properties` (Español)
- `messages_en.properties` (Inglés)
- `messages.properties` (Archivo por defecto)

### Ejemplo de Contenido

```properties
title.pais=Mantenimiento de Países
btn.save=Guardar
btn.cancel=Cancelar
```

## Ejemplo en PaisPage.java

```java
public class PaisPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;

    @Override
    protected void initCenter(Center center, String username) {
        Header title = new Header(2, msg.getProperty("title.pais"));
        // ...
    }
}
```

## Beneficios

- **Sin Código Repetitivo**: Elimina la necesidad de métodos manuales `loadMessages`.
- **Gestión Transparente del Idioma**: El programador no tiene que preocuparse por detectar o cargar el archivo correcto.
- **Mantenibilidad**: Centraliza la configuración de textos en archivos externos.
