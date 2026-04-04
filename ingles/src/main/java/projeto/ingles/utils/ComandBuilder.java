package projeto.ingles.utils;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import projeto.ingles.config.ProcessConfig;

@Slf4j
@Component
public class ComandBuilder {

    public Process startComand(ProcessConfig config) {
        ProcessBuilder pb = new ProcessBuilder(config.getCommand());
        pb.redirectErrorStream(false);
        Path workDir = config.getWorkingDirectory();

        if (workDir != null && Files.exists(workDir)) {
            
            pb.directory(workDir.toFile());
        }

        try {
            return pb.start();
        } catch (Exception e) {
            log.atError().log("Erro ao iniciar processo: {}", e.getMessage());
            throw new RuntimeException("Erro ao iniciar processo: " + e.getMessage(), e);
        }
    } 
}   
