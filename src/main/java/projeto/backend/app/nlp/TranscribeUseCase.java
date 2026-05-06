package projeto.backend.app.nlp;
import org.springframework.stereotype.Component;
import projeto.backend.core.nlp.NlpServicePort;
import projeto.backend.core.nlp.NlpServiceRequest;
import projeto.backend.core.nlp.NlpServiceResponse;

@Component
public class TranscribeUseCase {

    private final NlpServicePort nlpService;

    public TranscribeUseCase(NlpServicePort nlpService) {
        this.nlpService = nlpService;
    }

    public NlpServiceResponse transcribe(NlpServiceRequest request) {
        var response = nlpService.processText(request);
        return response;
    }
}
