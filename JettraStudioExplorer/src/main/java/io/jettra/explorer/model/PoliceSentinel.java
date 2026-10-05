package io.jettra.explorer.model;

import java.io.Serializable;

public record PoliceSentinel(
    String name,
    String title,
    String assignedNode,
    String status,
    int healthPercent,
    int alertsCount,
    String mission,
    String badgeColor
) implements Serializable {}
