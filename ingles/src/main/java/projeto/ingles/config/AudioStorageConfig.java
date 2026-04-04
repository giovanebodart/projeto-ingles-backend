package projeto.ingles.config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Configuration
public class AudioStorageConfig {

    @Value("${audio.storage.path}")
    private String audioStoragePath;

    private Path resolvedPath;

    @PostConstruct
    public void init() throws IOException {
        resolvedPath = Path.of(audioStoragePath);
        Files.createDirectories(resolvedPath);
        log.atInfo().log("Diretório de áudio configurado: {}", resolvedPath.toAbsolutePath());
    }
}