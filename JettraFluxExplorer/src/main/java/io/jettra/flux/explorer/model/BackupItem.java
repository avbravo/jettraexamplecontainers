package io.jettra.flux.explorer.model;

public record BackupItem(
    String id,
    String database,
    String fileName,
    String sizeFormatted,
    String createdAt,
    long durationMs
) {}
