package projeto.ingles.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.score.FilterLemmaResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class Controller {

    private final TranscribeProcessController transcribeProcess;
    private final AnalyzerProcessController analyzerProcess;
    
    public Controller(TranscribeProcessController transcribeProcess, AnalyzerProcessController analyzerProcess) {
        this.transcribeProcess = transcribeProcess;
        this.analyzerProcess = analyzerProcess;
    } 

    @PostMapping("/transcribe")     
    public ResponseEntity<String> transcribeVideo(@RequestParam String url) { 
        transcribeProcess.execute(url);
        return ResponseEntity.ok(null);
    }
    
    @GetMapping("/analyze")
    public ResponseEntity<FilterLemmaResponse> analyzeText(@RequestParam Language language) {
        var response = analyzerProcess.execute(language);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
