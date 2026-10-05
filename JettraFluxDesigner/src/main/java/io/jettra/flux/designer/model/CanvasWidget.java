package io.jettra.flux.designer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CanvasWidget implements Serializable {
    private String id;
    private String type; // Card, Row, Column, Header, Paragraph, ElevatedButton, TextField, Table, Police3D, etc.
    private String label;
    private String text;
    private String icon;
    private int columns = 2; // For Grid
    private String cssClasses = "";
    private String styles = "";
    private String parentId;
    private final List<CanvasWidget> children = new ArrayList<>();

    public CanvasWidget() {
        this.id = "w_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public CanvasWidget(String type, String label, String text) {
        this();
        this.type = type;
        this.label = label;
        this.text = text;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getColumns() { return columns; }
    public void setColumns(int columns) { this.columns = columns; }

    public String getCssClasses() { return cssClasses; }
    public void setCssClasses(String cssClasses) { this.cssClasses = cssClasses; }

    public String getStyles() { return styles; }
    public void setStyles(String styles) { this.styles = styles; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public List<CanvasWidget> getChildren() { return children; }
    public void addChild(CanvasWidget child) {
        child.setParentId(this.id);
        this.children.add(child);
    }

    public boolean removeChild(String childId) {
        if (childId == null) return false;
        boolean removed = children.removeIf(c -> c.getId().equalsIgnoreCase(childId));
        if (!removed) {
            for (CanvasWidget c : children) {
                if (c.removeChild(childId)) return true;
            }
        }
        return removed;
    }

    public CanvasWidget findById(String targetId) {
        if (targetId == null) return null;
        if (targetId.equalsIgnoreCase(this.id)) return this;
        for (CanvasWidget child : children) {
            CanvasWidget found = child.findById(targetId);
            if (found != null) return found;
        }
        return null;
    }
}
