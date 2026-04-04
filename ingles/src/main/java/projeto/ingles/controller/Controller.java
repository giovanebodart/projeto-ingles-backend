package projeto.ingles.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.ingles.model.dto.FilterLemmaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class Controller {

    private final TranscribeProcessController transcribeProcess;
    
    public Controller(TranscribeProcessController transcribeProcess) {
        this.transcribeProcess = transcribeProcess;
    } 

    @PostMapping("/transcribe")     
    public ResponseEntity<FilterLemmaResponse> transcribeVideo(@RequestParam String url) { 
        return ResponseEntity.status(HttpStatus.OK).body(transcribeProcess.execute(url)); 
    }    

}
