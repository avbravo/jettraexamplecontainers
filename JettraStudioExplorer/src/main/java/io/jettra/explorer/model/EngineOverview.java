package io.jettra.explorer.model;

public record EngineOverview(
    String databaseName,
    String engineType,
    String bucketName,
    long recordCount,
    String status
) {}
