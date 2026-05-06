package projeto.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.backend.app.nlp.TranscribeUseCase;
import projeto.backend.core.nlp.NlpServiceRequest;
import projeto.backend.core.nlp.NlpServiceResponse;

@RestController
@RequestMapping("/api")
public class Controller {

    private final TranscribeUseCase transcribeUseCase;

    public Controller(TranscribeUseCase transcribeUseCase) {
        this.transcribeUseCase = transcribeUseCase;
    }
    
    @PostMapping("/transcribe")
    public ResponseEntity<NlpServiceResponse> transcribeAudio(@RequestBody NlpServiceRequest request) {
        return ResponseEntity.ok(transcribeUseCase.transcribe(request));
    }
}
