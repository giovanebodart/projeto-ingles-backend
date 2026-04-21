package projeto.ingles.infrastructure.storage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AudioFilesManager{

    private final StorageConfig audioStorageConfig;

    public AudioFilesManager(StorageConfig audioStorageConfig) {
        this.audioStorageConfig = audioStorageConfig;
    }
    
    public Path getAudioFile(String audioFormat) {
        Path storagePath = audioStorageConfig.getResolvedPath();
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

    public void removeAudio(String audioFormat) {
        try {
            Path audioPath = getAudioFile(audioFormat);
            Files.deleteIfExists(audioPath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover o arquivo de áudio original.", e);
        }
    }
}
