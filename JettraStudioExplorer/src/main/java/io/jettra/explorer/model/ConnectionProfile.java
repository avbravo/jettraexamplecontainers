package io.jettra.explorer.model;

import java.io.Serializable;
import java.util.UUID;

public class ConnectionProfile implements Serializable {
    private String id;
    private String name;
    private String url;
    private String username;
    private String password;
    private boolean active;
    private long lastLatencyMs;
    private String lastStatus = "ONLINE";

    public ConnectionProfile() {
        this.id = "conn_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public ConnectionProfile(String id, String name, String url, String username, String password, boolean active) {
        this.id = (id != null && !id.isBlank()) ? id : "conn_" + UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
        this.active = active;
        this.lastStatus = active ? "ONLINE" : "STANDBY";
    }

    public String getHost() {
        if (url == null || url.isBlank()) return "127.0.0.1";
        String clean = url.replaceFirst("^[a-zA-Z0-9_+.-]+://", "").trim();
        if (clean.contains(":")) {
            return clean.split(":")[0];
        }
        if (clean.contains("/")) {
            return clean.split("/")[0];
        }
        return clean;
    }

    public int getPort() {
        if (url == null || url.isBlank()) return 9010;
        String clean = url.replaceFirst("^[a-zA-Z0-9_+.-]+://", "").trim();
        if (clean.contains(":")) {
            try {
                String p = clean.split(":")[1];
                if (p.contains("/")) p = p.split("/")[0];
                return Integer.parseInt(p);
            } catch (Exception ignored) {}
        }
        return 9010;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getLastLatencyMs() { return lastLatencyMs; }
    public void setLastLatencyMs(long lastLatencyMs) { this.lastLatencyMs = lastLatencyMs; }

    public String getLastStatus() { return lastStatus; }
    public void setLastStatus(String lastStatus) { this.lastStatus = lastStatus; }
}
