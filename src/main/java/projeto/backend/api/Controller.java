package projeto.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    
    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestParam("url") String url) {
        String transcription = "Transcrição do áudio em " + url;
        return ResponseEntity.ok(transcription);
    }
}
