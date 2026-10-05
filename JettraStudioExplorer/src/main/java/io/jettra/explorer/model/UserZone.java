package io.jettra.explorer.model;

import java.io.Serializable;

public record UserZone(
    String zoneId,
    String name,
    String subnet,
    int activeUsers,
    double bandwidthMbps,
    String colorHex
) implements Serializable {}
