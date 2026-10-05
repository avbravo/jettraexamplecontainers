package com.jettra.plugin.autentification.repository;

import com.jettra.plugin.autentification.entity.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PermissionRepository {
    private static final List<Permission> db = new ArrayList<>();

    static {
        // Navigation / Administration / Credentials (secured/restricted)
        addPerm("PANEL_PRINCIPAL", "Panel Principal", "/dashboard");
        addPerm("PERMISOS", "Administrar Permisos", "/permiso");
        addPerm("ROLES", "Administrar Roles", "/rol");
        addPerm("PERFILES", "Administrar Perfiles", "/perfil");
        addPerm("USUARIOS", "Administrar Usuarios", "/usuario");
        addPerm("WEB_DESIGNER", "Web Designer", "/webdesigner");
        addPerm("KANBAN_BOARD", "Kanban Board", "/kanban");
        addPerm("SWAGGER_UI", "Swagger UI", "/swagger-ui");

        // Credentials Category (secured/restricted)
        addPerm("CREDENTIAL", "Credential Page", "/credential");
        addPerm("PERMISSION", "Permission Page", "/permission");
        addPerm("ROLE", "Role Page", "/role");
        addPerm("DEPARTMENT", "Department Page", "/department");
        addPerm("USER", "User Page", "/user");

        // Rules Category
        addPerm("REGLAS", "Reglas Page", "/reglas");
        addPerm("REGLAS_VIEW", "Reglas View Crud Page", "/reglasview");

        // Crud Category
        addPerm("PERSONA", "Persona CRUD", "/persona");
        addPerm("PAIS", "Pais CRUD", "/pais");
        addPerm("DEPORTE", "Deporte CRUD", "/deporte");

        // CrudView Category
        addPerm("GRUPO", "Grupo CRUD", "/grupo");
        addPerm("SUBGRUPO", "SubGrupo CRUD", "/subgrupo");
        addPerm("PLANETA", "Planeta CRUD", "/planeta");
        addPerm("DATATABLE_EDITABLE_CRUD", "Datatable Editable Crud View", "/datatableeditablecrudview");
        addPerm("CANCION", "Cancion CRUD", "/cancion");

        // Library Category
        addPerm("AUTHOR", "Author Library", "/author");
        addPerm("BOOK", "Book Library", "/book");
        addPerm("PUBLISHER", "Publisher Library", "/publisher");
        addPerm("READER", "Reader Library", "/reader");

        // DataTable Category
        addPerm("DATATABLE", "DataTable Page", "/datatable");
        addPerm("DATATABLE_EDITABLE", "Datatable Editable", "/datatableeditable");

        // Master-Details Category
        addPerm("VIEW_DATATABLE", "View DataTable", "/viewdatatable");
    }

    private static void addPerm(String name, String desc, String path) {
        db.add(new Permission(UUID.nameUUIDFromBytes((name + "_ALL").getBytes()), name + "_ALL", desc + " All", path));
        db.add(new Permission(UUID.nameUUIDFromBytes((name + "_QUERY").getBytes()), name + "_QUERY", desc + " Query", path));
        db.add(new Permission(UUID.nameUUIDFromBytes((name + "_CREATE").getBytes()), name + "_CREATE", desc + " Create", path));
        db.add(new Permission(UUID.nameUUIDFromBytes((name + "_EDIT").getBytes()), name + "_EDIT", desc + " Edit", path));
        db.add(new Permission(UUID.nameUUIDFromBytes((name + "_REMOVE").getBytes()), name + "_REMOVE", desc + " Remove", path));
    }

    public static List<Permission> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Permission record) {
        if (record.id() == null) {
            record = new Permission(UUID.randomUUID(), record.name(), record.description(), record.resourcePath());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Permission> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
