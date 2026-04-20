package projeto.ingles.infrastructure.process;
import java.io.BufferedReader;
import java.io.IOException;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class BufferCleaner {
    
    public void captureStream(BufferedReader reader, StringBuilder output) {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            log.warn("Erro ao ler stream do processo: {}", e.getMessage());
        }
    }
}
