package com.jettra.plugin.autentification.model;

import com.jettra.plugin.autentification.entity.Role;
import com.jettra.plugin.autentification.entity.Permission;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.ViewSelectMany;
import io.jettra.wui.core.annotations.TableColumnField;
import java.util.UUID;
import java.util.Set;

@JettraViewModel
@ModelToRecordConversor(goal = Role.class)
public class RoleModel {

    @PropertiesInRecord
    @PropertiesLabel(value = "role.id", label = "ID")
    private UUID id;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "role.name", label = "Name")
    private String name;

    @PropertiesInRecord
    @PropertiesLabel(value = "role.permissions", label = "Permissions")
    @ViewSelectMany(label = "name", source = "com.jettra.plugin.autentification.services.PermissionService", method = "findAll")
    @TableColumnField(field = "name")
    private Set<Permission> permissions;

    public RoleModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return name;
    }
}
