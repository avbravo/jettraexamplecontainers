import java.nio.file.*;
public class Tester {
    public static void main(String[] args) throws Exception {
        String c = new String(Files.readAllBytes(Paths.get("src/main/java/com/jettra/example/pages/WebDesignerPage.java")));
        int i1 = c.indexOf("String scriptPart1 = \"\"\"") + 24;
        String s1 = c.substring(i1, c.indexOf("\"\"\";", i1));
        int i2 = c.indexOf("String scriptPart2 = \"\"\"") + 24;
        String s2 = c.substring(i2, c.indexOf("\"\"\";", i2));
        
        // Simular lo q hace Java TextBlock escape (\n \r etc, though minimal)
        String out = (s1 + s2).replace("\\\\n", "\\n").replace("\\\\u", "\\u").replace("\\\\/", "/");
        Files.write(Paths.get("out.js"), out.getBytes());
    }
}
