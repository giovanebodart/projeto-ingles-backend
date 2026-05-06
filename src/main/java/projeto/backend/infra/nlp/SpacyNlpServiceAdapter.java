package projeto.backend.infra.nlp;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import projeto.backend.core.nlp.NlpServicePort;
import projeto.backend.core.nlp.NlpServiceRequest;
import projeto.backend.core.nlp.NlpServiceResponse;

@Service
public class SpacyNlpServiceAdapter implements NlpServicePort {

    private final SpacyWebClientConfig webClientConfig;

    public SpacyNlpServiceAdapter(SpacyWebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }
    
    @Override
    public NlpServiceResponse processText(NlpServiceRequest request) {
        var webClient = webClientConfig.spacyWebClient();
        System.out.println("Sending request to Spacy NLP Service: " + request.toString());
        return webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(NlpServiceResponse.class)
            .block();
    }
}
