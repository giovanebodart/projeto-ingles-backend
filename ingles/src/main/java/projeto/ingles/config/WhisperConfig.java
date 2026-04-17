package projeto.ingles.config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private Path projectRoot;

    public void init(){
        
    }

    @PostConstruct
    public void validate() throws IOException {

        if (!Path.of(executablePath).isAbsolute() || !Path.of(modelPath).isAbsolute()) {
            projectRoot = resolveProjectRoot();
            log.atInfo().log("Projeto raiz detectado em: {}", projectRoot);
        } else {
            projectRoot = Path.of("/"); 
        }
        validateExists("executablePath", executablePath);
        validateExists("modelPath", modelPath);
        log.atInfo().log("WhisperConfig inicializado:");
        log.atInfo().log("  executable : {}", getExecutablePath());
        log.atInfo().log("  model      : {}", getModelPath());
        log.atInfo().log("  language   : {}", language);
        log.atInfo().log("  threads    : {}", threads);
        log.atInfo().log("  timeout    : {}s", timeout);
    }

    public Path getExecutableDirectory(){   
        return getExecutablePath().getParent(); 
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
        Path p = Path.of(value);
        return (p.isAbsolute() ? p : projectRoot.resolve(p))
                .toAbsolutePath()
                .normalize();
    }

    private Path resolveProjectRoot() {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))) {
                return current;
            }
            current = current.getParent();
        }
        log.atWarn().log("pom.xml não encontrado, usando raiz do sistema como projectRoot");
        return Path.of("/");
    }

    private void validateExists(String fieldName, String value) {
        Path path = resolvePath(value);
        if (!Files.exists(path)) {
            log.atWarn().log("Atençao: caminho '{}' não encontrado em: {}", fieldName, path);
        }
    }
}