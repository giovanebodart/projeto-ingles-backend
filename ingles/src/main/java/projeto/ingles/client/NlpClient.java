package projeto.ingles.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import projeto.ingles.model.dto.AnalyzeRequest;
import projeto.ingles.model.dto.AnalyzeResponse;

@Component
public class NlpClient {

    private final WebClient webClient;

    public NlpClient(WebClient.Builder builder) {
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
