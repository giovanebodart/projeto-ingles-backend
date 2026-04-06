package projeto.ingles.config;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Configuration
public class AudioStorageConfig {

    @Value("${audio.storage.path}")
    private String audioStoragePath;

    public Path getResolvedPath() {
        Path projectRoot = resolveProjectRoot();
        Path audioPath = Path.of(audioStoragePath);
        if (!audioPath.isAbsolute()) {
            audioPath = projectRoot.resolve(audioPath).toAbsolutePath();
        }
        try {
            log.atInfo().log("Diretório de armazenamento de áudio resolvido em: {}", audioPath);
            return audioPath;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar o diretório de armazenamento de áudio: " + audioPath, e);
        }
    }

    private Path resolveProjectRoot() {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException(
            "Raiz do projeto não encontrada. Nenhum pom.xml localizado a partir de: " +
            System.getProperty("user.dir")
        );
    }
}