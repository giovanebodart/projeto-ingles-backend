package projeto.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.backend.core.NlpServiceRequest;
import projeto.backend.core.NlpServiceResponse;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
public class Controller {

    private final TranscribeController transcribeController;
    private final TestController testController;

    public Controller(TranscribeController transcribeController, TestController testController) {
        this.transcribeController = transcribeController;
        this.testController = testController;
    }
    
    @PostMapping("/transcribe")
    public ResponseEntity<NlpServiceResponse> transcribeAudio(@RequestBody NlpServiceRequest request) {
        return ResponseEntity.ok(transcribeController.transcribe(request));
    }

    @GetMapping("/test")
    public void test() {
        testController.test();
    }
    
}
