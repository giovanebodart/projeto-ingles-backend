package projeto.backend.controllers;
import org.springframework.stereotype.Component;

import projeto.backend.core.nlp.NlpServiceRequest;
import projeto.backend.core.nlp.NlpServiceResponse;
import projeto.backend.services.SpacyNlpService;

@Component
public class TranscribeController {

    private final SpacyNlpService nlpService;

    public TranscribeController(SpacyNlpService nlpService) {
        this.nlpService = nlpService;
    }

    public NlpServiceResponse transcribe(NlpServiceRequest request) {
        var response = nlpService.processText(request);
        return response;
    }
}
