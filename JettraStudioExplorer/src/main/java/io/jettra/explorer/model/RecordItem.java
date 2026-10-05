package io.jettra.explorer.model;

public record RecordItem(
    String id,
    String bucket,
    String preview,
    String type
) {}
