package com.jettra.plugin.autentification.model;

import com.jettra.plugin.autentification.entity.Permission;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.PropertiesLabel;
import java.util.UUID;

@JettraViewModel
@ModelToRecordConversor(goal = Permission.class)
public class PermissionModel {

    @PropertiesInRecord
    @PropertiesLabel(value = "permission.id", label = "ID")
    private UUID id;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "permission.name", label = "Name")
    private String name;

    @PropertiesInRecord
    @PropertiesLabel(value = "permission.description", label = "Description")
    private String description;

    @PropertiesInRecord
    @PropertiesLabel(value = "permission.resourcePath", label = "Resource Path")
    private String resourcePath;

    public PermissionModel() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public String toString() {
        return name;
    }
}
