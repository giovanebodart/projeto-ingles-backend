package projeto.ingles.infrastructure.nlp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpacyClientConfig {
     
    @Bean
    public WebClient config() { 
        WebClient.Builder builder = WebClient.builder();
        return builder.baseUrl("${spacy.uri}").build();
        
    }

}
