package projeto.ingles.infrastructure.storage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TextFilesManager{
    private final StorageConfig storageConfig;

    public TextFilesManager(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

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

    public String readTextFile(Path filePath) {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void deleteFileIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getFilePath(String fileFormat) {
        Path storagePath = storageConfig.getResolvedPath();
        try {
            return Files.list(storagePath)
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(fileFormat))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum arquivo de texto " + fileFormat + " foi encontrado."));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao acessar o diretório de armazenamento de textos.", e);
        }
    }
}
