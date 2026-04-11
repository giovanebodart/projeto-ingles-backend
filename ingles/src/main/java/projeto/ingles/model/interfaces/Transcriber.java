package projeto.ingles.model.interfaces;

import reactor.core.publisher.Mono;

public interface Transcriber {
    
    String transcribeAudio(String audioFormat);
}
