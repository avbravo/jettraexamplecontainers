# Guía de Implementación: CRUD de Deportes

Esta guía detalla la creación de un mantenimiento completo para la entidad `Deporte` utilizando el framework **JettraWUI**.

## 1. Modelo de Datos (`DeporteModel.java`)
Se utiliza una clase POJO anotada para validaciones automáticas.

```java
@JettraViewModel
public class DeporteModel {
    @NotNull
    @Size(min = 2, max = 5)
    private String code;
    
    @NotNull
    @Size(min = 3, max = 100)
    private String deporte;
    // Getters y Setters...
}
```

## 2. Repositorio (`DeporteRepository.java`)
Gestiona la persistencia de datos en memoria para este ejemplo.

```java
public class DeporteRepository {
    private static final List<DeporteModel> deportes = new ArrayList<>();
    // Métodos findAll, findByCode, save, delete...
}
```

## 3. Página de Usuario (`DeportePage.java`)
La interfaz de usuario implementa un patrón **Pure Java Event-Driven** con las siguientes características:

### Componentes Utilizados:
- **`Card`**: Proporciona un contenedor visual elegante con sombra, bordes redondeados y transiciones suaves para el contenido principal.
- **`Grid`**: Utilizado para organizar botones y acciones en una disposición de rejilla flexible (CSS Grid) sin necesidad de estilos manuales complejos.
- **`FormGroup`**: Simplifica la agrupación de etiquetas e inputs en formularios, aplicando automáticamente los estilos de espaciado estándar.
- **`Datatable`**: Para listar los registros con paginación integrada.
- **`Modal`**: Componente especializado para diálogos, gestionado fuera del flujo normal del DOM.
- **`JettraValidations`**: Aplicación de reglas de validación del modelo directamente en los inputs.
- **`JettraSyncManager`**: Notificación de cambios para sincronización en tiempo real.

### Lógica de Operaciones:
- **Añadir**: Limpia el modelo y muestra el modal en modo "save".
- **Editar**: Carga los datos del registro seleccionado en el modelo y bloquea el campo `code` (llave primaria).
- **Eliminar**: Muestra un mensaje de confirmación y oculta los campos de entrada.

## 4. Configuración de Mensajes (`messages_es.properties`)
Se añadieron las etiquetas necesarias para la internacionalización:

```properties
title.deporte=Gestión de Deportes
subtitle.deporte=Catálogo de Deportes (JettraWUI)
btn.add.deporte=Añadir Deporte
th.deporte=Deporte
lbl.code=Código:
lbl.deporte=Deporte:
modal.add.deporte.title=Nuevo Deporte
modal.edit.deporte.title=Editar Deporte
modal.delete.deporte.title=¿Eliminar Deporte?
msg.confirm.delete.deporte=¿Está seguro de que desea eliminar este deporte?
```

## 5. Ejecución
Para visualizar la página, acceda a la ruta configurada en el servidor (usualmente `/deporte`). El framework se encarga de renderizar el HTML semántico y gestionar los eventos del lado del servidor.
