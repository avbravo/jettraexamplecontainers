# PersonaPage MVC Implementation Guide

This document details the configuration and usage of the `PersonaPage.java` component in the JettraWebExample application, following the MVC (Model-View-Controller) pattern.

## 1. Page Configuration

The class uses the `@InjectViewModel` annotation to manage data and `@JettraPageSincronized` for real-time synchronization.

```java
@JettraPageSincronized(SyncType.ALL)
public class PersonaPage extends DashboardBasePage {
    @InjectViewModel
    PersonaModel persona;
}
```

## 2. Main Components

### Add Button
The "Add" button uses an explicit ID and a `ClickListener` to reset the model and show the modal.

```java
Button addBtn = new Button("➕ Add Persona");
addBtn.setId("addBtn")
      .addClass("j-btn")
      .addClickListener(() -> {
          this.persona.setId("");
          this.persona.setNombre("");
          showModal("New Persona", "save");
      });
```

### Datatable and Actions
The list is implemented using `io.jettra.wui.complex.Datatable`. Each row includes buttons with unique IDs based on the persona ID.

```java
io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable();
// ...
Button editBtn = new Button("✏️");
editBtn.setId("edit-" + p.getId()).addClickListener(() -> {
    this.persona.setNombre(p.getNombre());
    showModal("Edit Persona", "save");
});
```

## 3. Modal and Form
The modal is a `Div` with `.modal` class, containing a `Form`.

```java
this.crudModal = new Div().id("crudModal").addClass("modal");
Form form = new Form("personaForm", JettraServer.resolvePath("/persona"));
// Inputs with JettraValidations
JettraValidations.apply(inputNombre, PersonaModel.class, "nombre");
```

## 4. Lifecycle Hooks

### `onInit`
Handles parameter resolution (pagination, language) before rendering.

### `onPost`
Processes the form submission, interacts with the repository, and notifies the `JettraSyncManager`.

```java
@Override
protected void onPost(Map<String, String> params) {
    if ("save".equals(params.get("action"))) {
        PersonaRepository.save(new PersonaModel(...));
        JettraSyncManager.notifyChange("PersonaModel", SyncType.UPDATE, user);
    }
}
```
