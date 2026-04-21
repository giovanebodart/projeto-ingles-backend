package projeto.ingles.infrastructure.storage;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Configuration
public class StorageConfig {

    @Value("${audio.storage.path}")
    private String storagePath;

    public Path getResolvedPath() {
        Path projectRoot = resolveProjectRoot();
        Path path = Path.of(storagePath);
        if (!path.isAbsolute()) {
            path = projectRoot.resolve(path).toAbsolutePath();
        }
        try {
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar o diretório de armazenamento: " + storagePath, e);
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