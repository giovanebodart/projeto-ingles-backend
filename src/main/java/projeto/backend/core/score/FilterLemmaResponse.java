package projeto.backend.core.score;

import projeto.backend.core.nlp.Language;
import java.util.Set;

public record FilterLemmaResponse(
    Language language,        
    Set<ScoredLemma> lemmas
) {}