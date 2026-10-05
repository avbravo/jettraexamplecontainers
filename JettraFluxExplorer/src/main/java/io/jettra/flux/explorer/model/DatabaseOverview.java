package io.jettra.flux.explorer.model;

public record DatabaseOverview(
    String name,
    String status,
    int engineCount,
    long recordCount,
    String sizeFormatted,
    String lastUpdated
) {}
