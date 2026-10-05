package io.jettra.explorer.model;

public record ServerNode(
    String id,
    String name,
    String host,
    int port,
    String role,
    String status,
    double latencyMs,
    double cpuPercent,
    int memoryUsedMb,
    int memoryTotalMb,
    long tps
) {
    public String getStatusBadgeClass() {
        return "ONLINE".equalsIgnoreCase(status) ? "badge-online" : "badge-warn";
    }
}
