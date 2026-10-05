package io.jettra.flux.designer.service;

import io.jettra.flux.designer.model.MavenDependency;
import io.jettra.flux.designer.model.MavenProjectInfo;
import io.jettra.flux.designer.model.ProjectFileNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MavenProjectService {

    private static final MavenProjectService INSTANCE = new MavenProjectService();

    private MavenProjectService() {}

    public static MavenProjectService getInstance() {
        return INSTANCE;
    }

    public MavenProjectInfo inspectProject(String dirPath) {
        if (dirPath == null || dirPath.isBlank()) return null;
        File dir = new File(dirPath.trim());
        if (!dir.exists() || !dir.isDirectory()) return null;

        File pomFile = new File(dir, "pom.xml");
        if (!pomFile.exists()) return null;

        MavenProjectInfo info = new MavenProjectInfo();
        info.setProjectName(dir.getName());
        info.setProjectPath(dir.getAbsolutePath());
        info.setPomFilePath(pomFile.getAbsolutePath());

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(pomFile);
            doc.getDocumentElement().normalize();

            info.setGroupId(getTagValue("groupId", doc.getDocumentElement()));
            info.setArtifactId(getTagValue("artifactId", doc.getDocumentElement()));
            info.setVersion(getTagValue("version", doc.getDocumentElement()));

            NodeList depList = doc.getElementsByTagName("dependency");
            List<MavenDependency> dependencies = new ArrayList<>();
            for (int i = 0; i < depList.getLength(); i++) {
                Node node = depList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    String g = getTagValue("groupId", elem);
                    String a = getTagValue("artifactId", elem);
                    String v = getTagValue("version", elem);
                    String s = getTagValue("scope", elem);
                    if (g != null && a != null) {
                        dependencies.add(new MavenDependency(g, a, v != null ? v : "", s != null ? s : "compile"));
                    }
                }
            }
            info.setDependencies(dependencies);

            // Scan pages and build full file tree
            List<String> pages = new ArrayList<>();
            ProjectFileNode rootNode = buildFileTree(dir, dir.getAbsolutePath(), pages);
            info.setRootNode(rootNode);
            info.setExistingPages(pages);

        } catch (Exception e) {
            System.err.println("Error parsing pom.xml: " + e.getMessage());
        }

        return info;
    }

    private ProjectFileNode buildFileTree(File dir, String baseRootPath, List<String> pagesCollector) {
        ProjectFileNode node = new ProjectFileNode(dir.getName(), dir.getAbsolutePath(), getRelativePath(dir, baseRootPath), true, false);

        File[] files = dir.listFiles();
        if (files != null) {
            // Sort: directories first, then alphabetical
            Arrays.sort(files, Comparator.comparing((File f) -> !f.isDirectory()).thenComparing(File::getName));

            for (File f : files) {
                // Ignore heavy or hidden folders
                if (f.getName().startsWith(".") || "target".equalsIgnoreCase(f.getName()) || "node_modules".equalsIgnoreCase(f.getName())) {
                    continue;
                }

                if (f.isDirectory()) {
                    ProjectFileNode childDir = buildFileTree(f, baseRootPath, pagesCollector);
                    node.addChild(childDir);
                } else {
                    boolean isPage = f.getName().endsWith("Page.java");
                    if (isPage) {
                        pagesCollector.add(f.getAbsolutePath());
                    }
                    ProjectFileNode childFile = new ProjectFileNode(f.getName(), f.getAbsolutePath(), getRelativePath(f, baseRootPath), false, isPage);
                    node.addChild(childFile);
                }
            }
        }
        return node;
    }

    private String getRelativePath(File f, String baseRootPath) {
        if (f.getAbsolutePath().equals(baseRootPath)) return "";
        return f.getAbsolutePath().substring(baseRootPath.length() + 1);
    }

    public boolean addDependency(String dirPath, MavenDependency dep) {
        if (dirPath == null || dep == null) return false;
        File pomFile = new File(dirPath, "pom.xml");
        if (!pomFile.exists()) return false;

        try {
            String content = Files.readString(pomFile.toPath());
            if (content.contains("<artifactId>" + dep.artifactId() + "</artifactId>")) {
                return false;
            }

            StringBuilder depXml = new StringBuilder();
            depXml.append("        <dependency>\n")
                  .append("            <groupId>").append(dep.groupId()).append("</groupId>\n")
                  .append("            <artifactId>").append(dep.artifactId()).append("</artifactId>\n");
            if (dep.version() != null && !dep.version().isBlank()) {
                depXml.append("            <version>").append(dep.version()).append("</version>\n");
            }
            if (dep.scope() != null && !"compile".equalsIgnoreCase(dep.scope())) {
                depXml.append("            <scope>").append(dep.scope()).append("</scope>\n");
            }
            depXml.append("        </dependency>\n");

            int idx = content.indexOf("</dependencies>");
            if (idx > 0) {
                String updated = content.substring(0, idx) + depXml + content.substring(idx);
                Files.writeString(pomFile.toPath(), updated);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error adding dependency: " + e.getMessage());
        }
        return false;
    }

    public boolean removeDependency(String dirPath, String artifactId) {
        if (dirPath == null || artifactId == null) return false;
        File pomFile = new File(dirPath, "pom.xml");
        if (!pomFile.exists()) return false;

        try {
            String content = Files.readString(pomFile.toPath());
            int artIdx = content.indexOf("<artifactId>" + artifactId + "</artifactId>");
            if (artIdx < 0) return false;

            int startDep = content.lastIndexOf("<dependency>", artIdx);
            int endDep = content.indexOf("</dependency>", artIdx);
            if (startDep >= 0 && endDep > startDep) {
                String updated = content.substring(0, startDep).stripTrailing() + "\n" + content.substring(endDep + "</dependency>".length()).stripLeading();
                Files.writeString(pomFile.toPath(), updated);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error removing dependency: " + e.getMessage());
        }
        return false;
    }

    private String getTagValue(String tag, Element elem) {
        NodeList nl = elem.getElementsByTagName(tag);
        if (nl != null && nl.getLength() > 0) {
            Node n = nl.item(0).getFirstChild();
            if (n != null) return n.getNodeValue().trim();
        }
        return null;
    }
}
