package projeto.backend.core.dto;

import java.util.List;

import projeto.backend.core.Language;
import projeto.backend.core.NlpResult;

public record NlpServiceResponse(
    String url,
    Language language,
    List<NlpResult> results) {
    
}
