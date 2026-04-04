package projeto.ingles.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import projeto.ingles.model.interfaces.AudioFilesUtilities;

@Component
@Log4j2
public class AudioFilesImpl implements AudioFilesUtilities{
    
    @Override
    public Path getAudioFile(String audioFormat) {
    Path storagePath = Path.of("storage\\audios");
        Path audioPath = null;
        try(Stream<Path> files = Files.list(storagePath)){
            audioPath = files
                 .filter(Files::isRegularFile)
                 .filter(file -> file.toString().endsWith(audioFormat))
                 .findFirst()
                 .orElseThrow(() -> new RuntimeException("Nenhum arquivo de áudio " + audioFormat + " foi encontrado."));
        }catch(IOException e){
            throw new RuntimeException("Erro ao acessar o diretório de armazenamento de áudios.", e);
        }
        return audioPath;
    }

    @Override
    public void removeAudio(String audioFormat) {
        try {
            Path audioPath = getAudioFile(audioFormat);
            Files.deleteIfExists(audioPath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover o arquivo de áudio original.", e);
        }
    }

    @Override
    public Path resolveAudioDirectory(String directory) {        
        Path dir = Path.of(System.getProperty("user.dir"), directory);
        try {
            Files.createDirectories(dir); 
            log.atInfo().log("Diretório de storage: {}", dir);
            return dir;
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de storage: " + dir, e);
        }
    }
}
