package projeto.backend.app.nlp;
import org.springframework.stereotype.Component;

@Component
public class TranscribeUseCase {

    public String transcribe(String url) {
        return "Transcrição do áudio em " + url;
    }
}
