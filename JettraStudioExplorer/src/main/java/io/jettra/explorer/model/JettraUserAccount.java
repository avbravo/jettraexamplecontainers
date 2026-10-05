package io.jettra.explorer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JettraUserAccount implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String globalRole;
    private Map<String, String> dbPermissions = new HashMap<>();

    public JettraUserAccount() {}

    public JettraUserAccount(String username, String password, String fullName, String globalRole) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.globalRole = globalRole;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGlobalRole() { return globalRole; }
    public void setGlobalRole(String globalRole) { this.globalRole = globalRole; }

    public Map<String, String> getDbPermissions() { return dbPermissions; }
    public void setDbPermissions(Map<String, String> dbPermissions) { this.dbPermissions = dbPermissions; }

    public void setDbPermission(String db, String perm) {
        this.dbPermissions.put(db, perm);
    }
}
