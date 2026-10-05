package io.jettra.flux.explorer.model;

import java.io.Serializable;

public record LiveUserSession(
    String username,
    String ipAddress,
    String targetDatabase,
    String activeQuery,
    double latencyMs,
    String zoneName
) implements Serializable {}
