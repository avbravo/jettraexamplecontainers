package io.jettra.flux.designer.service;

import io.jettra.flux.designer.model.CanvasWidget;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaPageParserService {

    private static final JavaPageParserService INSTANCE = new JavaPageParserService();

    public record ParsedPageInfo(
        String packageName,
        String className,
        String routePath,
        String primaryRole,
        String extendsClass,
        String originalFilePath,
        CanvasWidget rootWidget
    ) {}

    private JavaPageParserService() {}

    public static JavaPageParserService getInstance() {
        return INSTANCE;
    }

    public ParsedPageInfo parseJavaPageFile(File file) {
        if (file == null || !file.exists() || !file.getName().endsWith(".java")) {
            return null;
        }
        try {
            String content = Files.readString(file.toPath());
            return parseJavaPageContent(content, file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error parsing Java page file " + file.getName() + ": " + e.getMessage());
            return null;
        }
    }

    public ParsedPageInfo parseJavaPageContent(String content, String originalFilePath) {
        if (content == null || content.isBlank()) return null;

        String pkg = extractPattern(content, "package\\s+([a-zA-Z0-9_.]+);", 1, "com.flux.pages");
        String cls = extractPattern(content, "public\\s+class\\s+([a-zA-Z0-9_]+)", 1, "GeneratedPage");
        String route = extractPattern(content, "@Page\\s*\\(\\s*path\\s*=\\s*\"([^\"]+)\"\\)", 1, "/" + cls.toLowerCase());
        String role = extractPattern(content, "AppRole\\.([A-Z_]+)", 1, "ADMIN");
        String ext = extractPattern(content, "extends\\s+([a-zA-Z0-9_]+)", 1, "FluxBaseHandler");

        // Focus search within buildUI or buildCenter
        String methodBody = extractMethodBody(content);
        if (methodBody == null) {
            methodBody = content;
        }

        CanvasWidget root = new CanvasWidget("Column", cls, "Vista cargada de " + cls + ".java");

        List<CanvasWidget> widgets = parseWidgetsFromText(methodBody);
        if (widgets.isEmpty()) {
            // Default placeholder if no recognized widgets
            root.addChild(new CanvasWidget("Card", "Vista: " + cls, "Componente cargado de " + cls + ".java"));
        } else if (widgets.size() == 1 && ("Card".equalsIgnoreCase(widgets.get(0).getType()) || "Column".equalsIgnoreCase(widgets.get(0).getType()))) {
            root = widgets.get(0);
        } else {
            for (CanvasWidget w : widgets) {
                root.addChild(w);
            }
        }

        return new ParsedPageInfo(pkg, cls, route, role, ext, originalFilePath, root);
    }

    private List<CanvasWidget> parseWidgetsFromText(String text) {
        List<CanvasWidget> list = new ArrayList<>();
        if (text == null || text.isBlank()) return list;

        Pattern widgetStart = Pattern.compile("(?<![a-zA-Z0-9_])(Card|Row|Column|Grid|Panel|Box|Header|Paragraph|Label|Span|ElevatedButton|OutlinedButton|Button|TextField|StatCard|Table|Datatable|VisitorGraphCard|TransactionHistoryCard|Police3DCanvas)\\.of\\s*\\(");
        Matcher matcher = widgetStart.matcher(text);

        int currentIndex = 0;
        while (matcher.find(currentIndex)) {
            String type = matcher.group(1);
            int parenStart = matcher.end() - 1;
            int parenEnd = findMatchingClosingParen(text, parenStart);
            if (parenEnd > parenStart) {
                String inner = text.substring(parenStart + 1, parenEnd);
                CanvasWidget widget = buildWidget(type, inner, text, parenEnd);
                list.add(widget);
                currentIndex = parenEnd + 1;
            } else {
                currentIndex = matcher.end();
            }
        }
        return list;
    }

    private CanvasWidget buildWidget(String type, String innerArgs, String fullText, int parenEnd) {
        CanvasWidget widget = new CanvasWidget();
        widget.setType(type);

        // Check if there are nested widgets inside
        List<CanvasWidget> children = parseWidgetsFromText(innerArgs);
        if (!children.isEmpty()) {
            for (CanvasWidget child : children) {
                widget.addChild(child);
            }
            widget.setLabel("Contenedor " + type);
        }

        // Extract string arguments
        List<String> strings = extractAllStringLiterals(innerArgs);
        if (!strings.isEmpty()) {
            String primaryStr = strings.get(0);
            widget.setText(primaryStr);
            widget.setLabel(primaryStr.length() > 25 ? primaryStr.substring(0, 22) + "..." : primaryStr);
            if (strings.size() > 1 && "StatCard".equalsIgnoreCase(type)) {
                widget.setLabel(strings.get(0));
                widget.setText(strings.get(1));
            }
        } else if (children.isEmpty()) {
            widget.setLabel(type);
            widget.setText(type);
        }

        // Extract integer arguments (e.g. Header level or Grid columns)
        Matcher intMatcher = Pattern.compile("\\b(\\d+)\\b").matcher(innerArgs);
        if (intMatcher.find()) {
            try {
                int val = Integer.parseInt(intMatcher.group(1));
                if ("Grid".equalsIgnoreCase(type)) {
                    widget.setColumns(val);
                }
            } catch (Exception ignored) {}
        }

        // Extract chained modifiers after the closing parenthesis: .modifier(new Modifier().cssClass("...").style("..."))
        String chained = fullText.substring(parenEnd, Math.min(fullText.length(), parenEnd + 250));
        Matcher cssMatcher = Pattern.compile("cssClass\\s*\\(\\s*\"([^\"]+)\"\\)").matcher(chained);
        if (cssMatcher.find()) {
            widget.setCssClasses(cssMatcher.group(1));
        }
        Matcher styleMatcher = Pattern.compile("style\\s*\\(\\s*\"([^\"]+)\"\\)").matcher(chained);
        if (styleMatcher.find()) {
            widget.setStyles(styleMatcher.group(1));
        }

        return widget;
    }

    private int findMatchingClosingParen(String text, int openParenIdx) {
        int depth = 0;
        boolean inStr = false;
        for (int i = openParenIdx; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' && (i == 0 || text.charAt(i - 1) != '\\')) {
                inStr = !inStr;
            } else if (!inStr) {
                if (c == '(') depth++;
                else if (c == ')') {
                    depth--;
                    if (depth == 0) return i;
                }
            }
        }
        return -1;
    }

    private List<String> extractAllStringLiterals(String str) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"").matcher(str);
        while (m.find()) {
            list.add(m.group(1).replace("\\\"", "\""));
        }
        return list;
    }

    private String extractMethodBody(String content) {
        Pattern pattern = Pattern.compile("(?:buildCenter|buildUI)\\s*\\([^)]*\\)\\s*\\{");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            int openBrace = matcher.end() - 1;
            int depth = 0;
            boolean inStr = false;
            for (int i = openBrace; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inStr = !inStr;
                } else if (!inStr) {
                    if (c == '{') depth++;
                    else if (c == '}') {
                        depth--;
                        if (depth == 0) {
                            return content.substring(openBrace + 1, i);
                        }
                    }
                }
            }
        }
        return null;
    }

    private String extractPattern(String text, String regex, int group, String defaultVal) {
        Matcher m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            return m.group(group).trim();
        }
        return defaultVal;
    }
}
