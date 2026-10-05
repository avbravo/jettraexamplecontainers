import java.nio.file.*;
public class ParseTest {
    public static void main(String[] args) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get("src/main/java/com/jettra/example/pages/WebDesignerPage.java")));
        int s1start = content.indexOf("String scriptPart1 = \"\"\"");
        int s1end = content.indexOf("\"\"\";", s1start);
        int s2start = content.indexOf("String scriptPart2 = \"\"\"");
        int s2end = content.indexOf("\"\"\";", s2start);
        
        String s1 = content.substring(s1start + 24, s1end);
        String s2 = content.substring(s2start + 24, s2end);
        
        Files.write(Paths.get("output.js"), (s1 + s2).getBytes());
    }
}
