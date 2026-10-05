package com.jettra.plugin.acreditation.repository;

import com.jettra.plugin.acreditation.entity.Feature;
import io.jettra.plugin.accreditation.enumerations.TypeFeature;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FeatureRepository {
    private static final List<Feature> db = new ArrayList<>();

    static {
        // Navigation / Administration / Credentials (secured/restricted)
        addOptions("PANEL_PRINCIPAL", "Panel Principal", "/dashboard", TypeFeature.MENU);
        addOptions("PERMISOS", "Administrar Permisos", "/permiso", TypeFeature.MENU);
        addOptions("ROLES", "Administrar Roles", "/rol", TypeFeature.MENU);
        addOptions("PERFILES", "Administrar Perfiles", "/perfil", TypeFeature.MENU);
        addOptions("USUARIOS", "Administrar Usuarios", "/usuario", TypeFeature.MENU);
        addOptions("WEB_DESIGNER", "Web Designer", "/webdesigner", TypeFeature.MENU);
        addOptions("KANBAN_BOARD", "Kanban Board", "/kanban", TypeFeature.MENU);
        addOptions("SWAGGER_UI", "Swagger UI", "/swagger-ui", TypeFeature.MENU);

        // Credentials Category (secured/restricted)
        addOptions("CREDENTIAL", "Credential Page", "/credential", TypeFeature.MENU);
        addOptions("PERMISSION", "Options Page", "/permission", TypeFeature.MENU);
        addOptions("ROLE", "Role Page", "/role", TypeFeature.MENU);
        addOptions("DEPARTMENT", "Department Page", "/department", TypeFeature.MENU);
        addOptions("USER", "User Page", "/user", TypeFeature.MENU);

        // Rules Category
        addOptions("REGLAS", "Reglas Page", "/reglas", TypeFeature.MENU);
        addOptions("REGLAS_VIEW", "Reglas View Crud Page", "/reglasview", TypeFeature.MENU);

        // Crud Category
        addOptions("PERSONA", "Persona CRUD", "/persona", TypeFeature.MENU);
        addOptions("PAIS", "Pais CRUD", "/pais", TypeFeature.MENU);
        addOptions("DEPORTE", "Deporte CRUD", "/deporte", TypeFeature.MENU);

        // CrudView Category
        addOptions("GRUPO", "Grupo CRUD", "/grupo", TypeFeature.MENU);
        addOptions("SUBGRUPO", "SubGrupo CRUD", "/subgrupo", TypeFeature.MENU);
        addOptions("PLANETA", "Planeta CRUD", "/planeta", TypeFeature.MENU);
        addOptions("DATATABLE_EDITABLE_CRUD", "Datatable Editable Crud View", "/datatableeditablecrudview", TypeFeature.MENU);
        addOptions("CANCION", "Cancion CRUD", "/cancion", TypeFeature.MENU);

        // Library Category
        addOptions("AUTHOR", "Author Library", "/author", TypeFeature.MENU);
        addOptions("BOOK", "Book Library", "/book", TypeFeature.MENU);
        addOptions("PUBLISHER", "Publisher Library", "/publisher", TypeFeature.MENU);
        addOptions("READER", "Reader Library", "/reader", TypeFeature.MENU);

        // DataTable Category
        addOptions("DATATABLE", "DataTable Page", "/datatable", TypeFeature.MENU);
        addOptions("DATATABLE_EDITABLE", "Datatable Editable", "/datatableeditable", TypeFeature.MENU);

        // Master-Details Category
        addOptions("VIEW_DATATABLE", "View DataTable", "/viewdatatable", TypeFeature.MENU);
    }

    private static void addOptions(String name, String desc, String path,TypeFeature typeFeatureResource) {
        db.add(new Feature(UUID.nameUUIDFromBytes((name ).getBytes()), name , desc , path,typeFeatureResource));
        
    }

    public static List<Feature> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Feature record) {
        if (record.id() == null) {
            record = new Feature(UUID.randomUUID(), record.name(), record.description(), record.resourcePath(),record.typeFeature());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Feature> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
