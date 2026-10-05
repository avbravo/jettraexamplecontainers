package com.jettra.plugin.example.admin.model;

import io.jettra.rules.validations.NotNull;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
public class UsuarioModel {
    @NotNull
    @PropertiesLabel(value = "usuario.id", label = "ID")
    private String id;

    @NotNull
    @PropertiesLabel(value = "usuario.username", label = "Username")
    private String username;

    @NotNull
    @PropertiesLabel(value = "usuario.nombre", label = "Nombre")
    private String nombre;

    @NotNull
    @PropertiesLabel(value = "usuario.email", label = "Email")
    private String email;

    @PropertiesLabel(value = "usuario.activo", label = "Activo")
    private Boolean activo;

    @PropertiesLabel(value = "usuario.perfil", label = "Perfil")
    private String perfil;

    @PropertiesLabel(value = "usuario.roles", label = "Roles")
    private String roles;

    public UsuarioModel() {}

    public UsuarioModel(String id, String username, String nombre, String email, Boolean activo, String perfil, String roles) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.perfil = perfil;
        this.roles = roles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}
