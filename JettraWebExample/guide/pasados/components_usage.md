# JettraWUI Component Usage Guide

This guide provides examples and best practices for using the core components of the JettraWUI framework.

## 1. Buttons

JettraWUI supports both traditional and Fluent API styles for button creation.

### Traditional Style
```java
Button btn = new Button("Click Me");
btn.setId("myBtn");
btn.addClass("j-btn-primary");
btn.addClickListener(() -> {
    System.out.println("Clicked!");
});
```

### Fluent API Style
```java
Button btn = new Button("Save")
    .id("saveBtn")
    .addClass("j-btn-success")
    .style("margin-top", "10px")
    .addClickListener(() -> save());
```

---

## 2. Inputs

### TextBox (Standard Input)
```java
TextBox input = new TextBox("text", "username");
input.setId("username").addClass("j-input");
input.setProperty("placeholder", "Enter username");
```

### Spinner (Numeric Counter)
```java
Spinner spinner = new Spinner(0, 100, 5); // min, max, step
spinner.setValue(10);
```

### SelectOne (Dropdown)
```java
SelectOne<String> platform = new SelectOne<>("platform");
platform.addOption("Web", "web")
        .addOption("Mobile", "mobile")
        .addOption("Desktop", "desktop");
```

---

## 3. Layout Components

### TabView
Allows organizing content into multiple tabs.
```java
TabView tabView = new TabView("Settings");
tabView.addTab("Profile", new ProfileComponent());
tabView.addTab("Security", new SecurityComponent());
tabView.setOrientation(Orientation.SUPERIOR);
```

### Grid
Responsive grid system for layouts.
```java
Grid container = new Grid(2); // 2 columns
container.add(new Div("Col 1")).add(new Div("Col 2"));
```

---

## 4. Complex Components

### Datatable (MVC)
Used for displaying and interacting with data collections.
```java
Datatable table = new Datatable();
table.addHeaderRow(new Row(new TD("ID"), new TD("Name"), new TD("Actions")));

for (Item item : items) {
    table.addRow(new Row(
        new TD(item.getId()),
        new TD(item.getName()),
        new TD(createActions(item))
    ));
}
```

---

## 5. Advanced Features

### Model Validation
Apply validations directly from your model annotations.
```java
// In your Model class
public class UserModel {
    @NotNull @Email
    private String email;
}

// In your Page class
TextBox emailInput = new TextBox("email", "email");
JettraValidations.apply(emailInput, UserModel.class, "email");
```

### Page Synchronization
Keep different user sessions in sync with real-time notifications.
```java
@JettraPageSincronized(SyncType.ALL)
public class MyPage extends DashboardBasePage {
    // When data changes:
    JettraSyncManager.notifyChange("MyModel", SyncType.UPDATE, username);
}
```
