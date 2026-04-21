package projeto.ingles.core.score;

import projeto.ingles.core.nlp.Language;
import java.util.Set;

public record FilterLemmaResponse(
    Language language,        
    Set<ScoredLemma> lemmas
) {}