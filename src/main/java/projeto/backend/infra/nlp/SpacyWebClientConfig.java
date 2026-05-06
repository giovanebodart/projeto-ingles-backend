package projeto.backend.infra.nlp;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import reactor.netty.http.client.HttpClient;

@Configuration
public class SpacyWebClientConfig {

    @Value("${spacy.uri}")
    private String spacyUri;

    @Bean
    WebClient spacyWebClient() {
        int maxBufferSize = 10 * 1024 * 1024; // 10MB

        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(config -> config
                .defaultCodecs()
                .maxInMemorySize(maxBufferSize)
            )
            .build();
        
        HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofMinutes(5));
        

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(strategies)
            .baseUrl(spacyUri)
            .build();
    }
}