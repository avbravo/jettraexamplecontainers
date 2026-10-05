package io.jettra.flux.designer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MavenProjectInfo implements Serializable {
    private String projectName;
    private String projectPath;
    private String pomFilePath;
    private String groupId;
    private String artifactId;
    private String version;
    private ProjectFileNode rootNode;
    private final List<MavenDependency> dependencies = new ArrayList<>();
    private final List<String> existingPages = new ArrayList<>();

    public MavenProjectInfo() {}

    public MavenProjectInfo(String projectName, String projectPath, String pomFilePath, String groupId, String artifactId, String version) {
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.pomFilePath = pomFilePath;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getProjectPath() { return projectPath; }
    public void setProjectPath(String projectPath) { this.projectPath = projectPath; }

    public String getPomFilePath() { return pomFilePath; }
    public void setPomFilePath(String pomFilePath) { this.pomFilePath = pomFilePath; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public ProjectFileNode getRootNode() { return rootNode; }
    public void setRootNode(ProjectFileNode rootNode) { this.rootNode = rootNode; }

    public List<MavenDependency> getDependencies() { return Collections.unmodifiableList(dependencies); }
    public void setDependencies(List<MavenDependency> list) {
        this.dependencies.clear();
        if (list != null) this.dependencies.addAll(list);
    }
    public void addDependency(MavenDependency dep) { this.dependencies.add(dep); }

    public List<String> getExistingPages() { return Collections.unmodifiableList(existingPages); }
    public void setExistingPages(List<String> pages) {
        this.existingPages.clear();
        if (pages != null) this.existingPages.addAll(pages);
    }
    public void addExistingPage(String page) { this.existingPages.add(page); }
}
