package projeto.backend.core.nlp;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NlpServiceRequest(
    @JsonProperty("language") String language,
    @JsonProperty("url") String url
) {
    
}
