package projeto.ingles.model.interfaces;

import java.nio.file.Path;

public interface AudioFilesUtilities {
    
    Path getAudioFile(String audioFormat);
    void removeAudio(String audioFormat);
}
