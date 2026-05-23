package projeto.backend.core;

import java.util.List;

public record NlpServiceResponse(
    Language language, 
    List<NlpResult> results) {
    
}
