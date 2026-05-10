package projeto.backend.app.nlp;
import org.springframework.stereotype.Component;
import projeto.backend.core.nlp.NlpServiceRequest;
import projeto.backend.core.nlp.NlpServiceResponse;
import projeto.backend.infra.nlp.SpacyNlpService;

@Component
public class TranscribeUseCase {

    private final SpacyNlpService nlpService;

    public TranscribeUseCase(SpacyNlpService nlpService) {
        this.nlpService = nlpService;
    }

    public NlpServiceResponse transcribe(NlpServiceRequest request) {
        var response = nlpService.processText(request);
        return response;
    }
}
