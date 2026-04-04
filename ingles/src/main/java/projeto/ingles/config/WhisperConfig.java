package projeto.ingles.config;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "whisper")
@Getter
@Setter
public class WhisperConfig {
    
    private String executablePath = "${whisper.executable-path}";
    private String modelPath = "${whisper.model-path}";
    private Integer timeout = 180;
    private String language = "${whisper.language}";
    private String audioPath;
    private Integer threads = 4;

    public Path getExecutable(){
        return Paths.get(executablePath);
    }
    
}
