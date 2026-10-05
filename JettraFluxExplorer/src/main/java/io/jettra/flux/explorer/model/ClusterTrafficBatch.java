package io.jettra.flux.explorer.model;

import java.io.Serializable;

public record ClusterTrafficBatch(
    String batchId,
    String sourceNode,
    String targetNode,
    String trafficType,
    String transferRate,
    String status
) implements Serializable {}
