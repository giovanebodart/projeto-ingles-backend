package projeto.backend.services;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import projeto.backend.config.SpacyWebClientConfig;
import projeto.backend.core.NlpServiceRequest;
import projeto.backend.core.NlpServiceResponse;

@Service
public class SpacyNlpService{

    private static final Logger log = Logger.getLogger(SpacyNlpService.class.getName());
    private final SpacyWebClientConfig webClientConfig;

    public SpacyNlpService(SpacyWebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }
    
    public NlpServiceResponse processText(NlpServiceRequest request) {
        var webClient = webClientConfig.spacyWebClient();
        log.info("Sending request to Spacy NLP Service: " + request.toString());
        return webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(NlpServiceResponse.class)
            .block();
    }
}
