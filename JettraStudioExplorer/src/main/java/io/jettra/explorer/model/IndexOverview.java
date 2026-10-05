package io.jettra.explorer.model;

public record IndexOverview(
    String databaseName,
    String bucketName,
    String indexName,
    String indexType,
    String targetField,
    boolean unique,
    long entriesCount
) {}
