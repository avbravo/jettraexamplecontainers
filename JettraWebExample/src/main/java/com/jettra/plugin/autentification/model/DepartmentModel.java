package com.jettra.plugin.autentification.model;

import com.jettra.plugin.autentification.entity.Department;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.PropertiesLabel;
import java.util.UUID;

@JettraViewModel
@ModelToRecordConversor(goal = Department.class)
public class DepartmentModel {

    @PropertiesInRecord
    @PropertiesLabel(value = "department.id", label = "ID")
    private UUID id;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "department.name", label = "Name")
    private String name;

    @PropertiesInRecord
    @PropertiesLabel(value = "department.parentDepartmentId", label = "Parent Department ID")
    private UUID parentDepartmentId;

    public DepartmentModel() {}

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

    public UUID getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(UUID parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    @Override
    public String toString() {
        return name;
    }
}
