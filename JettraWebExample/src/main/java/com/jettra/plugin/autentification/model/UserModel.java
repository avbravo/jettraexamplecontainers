package com.jettra.plugin.autentification.model;

import com.jettra.plugin.autentification.entity.User;
import com.jettra.plugin.autentification.entity.Department;
import com.jettra.plugin.autentification.entity.Role;
import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.ViewSelectOne;
import io.jettra.wui.core.annotations.ViewSelectMany;
import io.jettra.wui.core.annotations.TableColumnField;
import java.util.UUID;
import java.util.Set;

@JettraViewModel
@ModelToRecordConversor(goal = User.class)
public class UserModel {

    @PropertiesInRecord
    @PropertiesLabel(value = "user.id", label = "ID")
    private UUID id;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "user.firstName", label = "First Name")
    private String firstName;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "user.lastName", label = "Last Name")
    private String lastName;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "user.email", label = "Email")
    private String email;

    @PropertiesInRecord
    @PropertiesLabel(value = "user.phone", label = "Phone")
    private String phone;

    @PropertiesInRecord
    @PropertiesLabel(value = "user.active", label = "Active")
    private Boolean active;

    @PropertiesInRecord
    @PropertiesLabel(value = "user.department", label = "Department")
    @ViewSelectOne(label = "name", source = "com.jettra.plugin.services.autentification.DepartmentService", method = "findAll")
    private Department department;

    @PropertiesInRecord
    @PropertiesLabel(value = "user.roles", label = "Roles")
    @ViewSelectMany(label = "name", source = "com.jettra.plugin.services.autentification.RoleService", method = "findAll")
    @TableColumnField(field = "name")
    private Set<Role> roles;

    public UserModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
