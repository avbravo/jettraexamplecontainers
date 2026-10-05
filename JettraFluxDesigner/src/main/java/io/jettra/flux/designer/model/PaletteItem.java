package io.jettra.flux.designer.model;

import java.io.Serializable;

public record PaletteItem(
    String type,
    String displayName,
    String category,
    String icon,
    String description,
    String defaultSnippet
) implements Serializable {}
