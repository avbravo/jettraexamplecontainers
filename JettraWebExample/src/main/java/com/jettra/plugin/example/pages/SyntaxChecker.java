package com.jettra.plugin.example.pages;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

public class SyntaxChecker {
    public static void main(String[] args) throws Exception {
        WebDesignerPage page = new WebDesignerPage();
        // Reflection to get private field or intercept...
        // Actually, WebDesignerPage registers scripts to the center in initCenter.
        // Let's just instantiate and get scripts if possible, or read the exact same block
    }
}
