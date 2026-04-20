package projeto.ingles.infrastructure.process;
import java.nio.file.Path;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcessConfig {
    private final List<String> command;
    private final Path workingDirectory;   
}