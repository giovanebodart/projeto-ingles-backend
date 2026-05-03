package projeto.backend.core.nlp;

import java.util.List;

public record NlpServiceResponse(
    Language language, 
    List<NlpResult> results) {
    
}
