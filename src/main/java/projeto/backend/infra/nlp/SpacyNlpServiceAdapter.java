package projeto.backend.infra.nlp;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import projeto.backend.core.nlp.Language;
import projeto.backend.core.nlp.NlpServiceRequest;
import projeto.backend.core.nlp.NlpServiceResponse;
import projeto.backend.core.nlp.URL;

@Service
public class SpacyNlpServiceAdapter {

    private final SpacyWebClientConfig webClientConfig;

    public SpacyNlpServiceAdapter(SpacyWebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }
    
    public NlpServiceResponse analyze(Language language, URL url) {
        var request = new NlpServiceRequest(language.name(), url.url());
        var webClient = webClientConfig.spacyWebClient();

        return webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(NlpServiceResponse.class)
            .block();
    }
}
