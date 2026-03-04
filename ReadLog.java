import java.nio.file.*;
import java.nio.charset.*;

public class ReadLog {
    public static void main(String[] args) throws Exception {
        System.out.println(new String(Files.readAllBytes(Paths.get("build_log.txt")), StandardCharsets.UTF_16LE));
    }
}
