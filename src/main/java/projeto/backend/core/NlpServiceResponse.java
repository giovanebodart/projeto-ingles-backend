package projeto.backend.core;

import java.util.List;

public record NlpServiceResponse(
    String url,
    Language language,
    List<NlpResult> results) {
    
}
