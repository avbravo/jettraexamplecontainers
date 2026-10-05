package io.jettra.flux.explorer.service;

import io.jettra.driver.JettraClient;
import io.jettra.driver.config.JettraClientConfig;
import io.jettra.flux.explorer.model.ConnectionProfile;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Connection Manager managing JettraStore server profiles,
 * live health checks, and active cluster switching for JettraFluxExplorer.
 */
public class ConnectionManager implements Serializable {

    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private final List<ConnectionProfile> profiles = new CopyOnWriteArrayList<>();

    public record ConnectionTestResult(boolean success, String message, long latencyMs) {}

    private ConnectionManager() {
        initDefaultProfiles();
    }

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    private void initDefaultProfiles() {
        profiles.clear();
        profiles.add(new ConnectionProfile("conn_local", "JettraStore Local Master", "127.0.0.1:9010", "admin", "admin-jettra", true));
        profiles.add(new ConnectionProfile("conn_cluster_beta", "JettraStore Worker Alpha", "10.0.1.11:9011", "admin", "admin-jettra", false));
        profiles.add(new ConnectionProfile("conn_remote_prod", "JettraStore Clúster Producción", "192.168.1.100:9010", "admin", "admin-jettra", false));
    }

    public List<ConnectionProfile> getProfiles() {
        return Collections.unmodifiableList(profiles);
    }

    public Optional<ConnectionProfile> getActiveProfile() {
        return profiles.stream().filter(ConnectionProfile::isActive).findFirst();
    }

    public Optional<ConnectionProfile> findById(String id) {
        if (id == null) return Optional.empty();
        return profiles.stream().filter(p -> id.equalsIgnoreCase(p.getId())).findFirst();
    }

    public synchronized void saveOrUpdate(ConnectionProfile profile) {
        if (profile == null) return;
        int idx = -1;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getId().equalsIgnoreCase(profile.getId())) {
                idx = i;
                break;
            }
        }
        if (idx >= 0) {
            profiles.set(idx, profile);
        } else {
            profiles.add(profile);
        }
    }

    public synchronized boolean delete(String id) {
        if (id == null) return false;
        boolean removed = profiles.removeIf(p -> p.getId().equalsIgnoreCase(id));
        if (profiles.stream().noneMatch(ConnectionProfile::isActive) && !profiles.isEmpty()) {
            profiles.get(0).setActive(true);
        }
        return removed;
    }

    public synchronized boolean activate(String id) {
        if (id == null) return false;
        Optional<ConnectionProfile> opt = findById(id);
        if (opt.isEmpty()) return false;

        ConnectionProfile target = opt.get();
        for (ConnectionProfile p : profiles) {
            p.setActive(p.getId().equalsIgnoreCase(id));
        }

        // Reconnect StoreClusterService to the new active server profile
        StoreClusterService.getInstance().reconnect(target);
        return true;
    }

    public ConnectionTestResult testConnection(ConnectionProfile profile) {
        if (profile == null) {
            return new ConnectionTestResult(false, "Perfil de conexión nulo", 0);
        }
        long start = System.currentTimeMillis();
        String host = profile.getHost();
        int port = profile.getPort();

        try {
            JettraClientConfig cfg = JettraClientConfig.builder()
                .addClusterNode(host, port)
                .credentials(profile.getUsername(), profile.getPassword())
                .clusterMultinodeActive(true)
                .build();

            JettraClient testClient = JettraClient.connect(cfg);
            long latency = Math.max(1, System.currentTimeMillis() - start);
            profile.setLastLatencyMs(latency);
            profile.setLastStatus("ONLINE");
            return new ConnectionTestResult(true, "Conexión exitosa con JettraStore en " + host + ":" + port, latency);
        } catch (Exception e) {
            long latency = Math.max(1, System.currentTimeMillis() - start);
            profile.setLastLatencyMs(latency);
            profile.setLastStatus("OFFLINE");
            return new ConnectionTestResult(false, "Fallo al conectar: " + e.getMessage(), latency);
        }
    }
}
