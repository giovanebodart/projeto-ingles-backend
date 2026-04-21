package projeto.ingles.infrastructure.nlp;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import projeto.ingles.core.nlp.*;

@Component
public class SpacyClient {

    private final WebClient webClient;

    public SpacyClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8000")
                .build();
    }

    public AnalyzeResponse analyzeText(AnalyzeRequest request) {
        return webClient.post()
                .uri("/analyze") 
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AnalyzeResponse.class)
                .block();
    }
}
