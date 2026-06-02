package projeto.backend.core.dto;

import projeto.backend.core.Language;

public record NlpServiceRequest(
    Language language,
    String url
) {
    
}
