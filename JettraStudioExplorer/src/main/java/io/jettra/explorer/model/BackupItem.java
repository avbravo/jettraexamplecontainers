package io.jettra.explorer.model;

public record BackupItem(
    String id,
    String database,
    String fileName,
    String sizeFormatted,
    String createdAt,
    long durationMs
) {}
