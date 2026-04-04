package projeto.ingles.config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Log4j2
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "whisper")
public class WhisperConfig {

    private String executablePath;
    private String modelPath;
    private String language = "en";
    private Integer timeout = 180;
    private Integer threads = 4;

    @PostConstruct
    public void validate() throws IOException {
        validateExists("executablePath", executablePath);
        validateExists("modelPath", modelPath);
        log.atInfo().log("WhisperConfig inicializado:");
        log.atInfo().log("  executable : {}", getExecutablePath());
        log.atInfo().log("  model      : {}", getModelPath());
        log.atInfo().log("  language   : {}", language);
        log.atInfo().log("  threads    : {}", threads);
        log.atInfo().log("  timeout    : {}s", timeout);
    }

    public Path getExecutablePath() {
        return resolvePath(executablePath);
    }

    public Path getModelPath() {
        return resolvePath(modelPath);
    }

    private Path resolvePath(String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException("Caminho nao configurado: valor ausente ou vazio.");
        }
        return Paths.get(value).toAbsolutePath().normalize();
    }

    private void validateExists(String fieldName, String value) {
        Path path = resolvePath(value);
        if (!Files.exists(path)) {
            log.atWarn().log("Atençao: caminho '{}' não encontrado em: {}", fieldName, path);
        }
    }
}