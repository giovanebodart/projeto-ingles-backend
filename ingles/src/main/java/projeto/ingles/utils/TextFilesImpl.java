package projeto.ingles.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import projeto.ingles.model.interfaces.TextFilesUtilities;

@Component
public class TextFilesImpl implements TextFilesUtilities {

    @Override
    public List<String> treatText(String output) {
        output = output.replaceAll("\\[(BEG|TT|SOLM|EOT)_?\\d*\\]", "")
            .replaceAll("\\[\\s*\\]", "")
            .replaceAll("\\s{2,}", " ")
            .replaceAll("<[.]+>", "");
        String[] parts = output.split("(?<=[.!?])\\s+");
        List<String> segments = new ArrayList<>();
        for (String part : parts) {
            String cleaned = part.trim();
            if (!cleaned.isEmpty()) {
                segments.add(cleaned);
            }
        }
        return segments;
    }

    @Override
    public String readTextFile(Path filePath) {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void deleteFileIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
