import java.nio.file.*;
import java.nio.charset.*;

public class ConvertLog {
    public static void main(String[] args) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get("build_log.txt"));
        String content = new String(bytes, StandardCharsets.UTF_16LE);
        Files.write(Paths.get("log_utf8.txt"), content.getBytes(StandardCharsets.UTF_8));
    }
}
