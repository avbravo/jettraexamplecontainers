package io.jettra.explorer.model;

import java.io.Serializable;

public record PoliceIncident(
    String timestamp,
    String sentinel,
    String description,
    String severity,
    String actionTaken
) implements Serializable {}
