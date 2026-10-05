package io.jettra.flux.designer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectFileNode implements Serializable {
    private String name;
    private String absolutePath;
    private String relativePath;
    private boolean isDirectory;
    private boolean isPage;
    private final List<ProjectFileNode> children = new ArrayList<>();

    public ProjectFileNode() {}

    public ProjectFileNode(String name, String absolutePath, String relativePath, boolean isDirectory, boolean isPage) {
        this.name = name;
        this.absolutePath = absolutePath;
        this.relativePath = relativePath;
        this.isDirectory = isDirectory;
        this.isPage = isPage;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAbsolutePath() { return absolutePath; }
    public void setAbsolutePath(String absolutePath) { this.absolutePath = absolutePath; }

    public String getRelativePath() { return relativePath; }
    public void setRelativePath(String relativePath) { this.relativePath = relativePath; }

    public boolean isDirectory() { return isDirectory; }
    public void setDirectory(boolean directory) { isDirectory = directory; }

    public boolean isPage() { return isPage; }
    public void setPage(boolean page) { isPage = page; }

    public List<ProjectFileNode> getChildren() { return Collections.unmodifiableList(children); }
    public void addChild(ProjectFileNode child) { this.children.add(child); }
}
