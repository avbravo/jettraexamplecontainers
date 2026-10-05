package io.jettra.flux.designer.model;

import java.io.Serializable;

public record MavenDependency(
    String groupId,
    String artifactId,
    String version,
    String scope
) implements Serializable {

    public String coordinates() {
        return groupId + ":" + artifactId + (version != null && !version.isBlank() ? ":" + version : "");
    }
}
