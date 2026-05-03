package projeto.backend.core.score;

import projeto.backend.core.nlp.Language;
import projeto.backend.core.nlp.UniversalWordType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScoredLemma {
    private final String lemma;
    private final String originalText;
    private final UniversalWordType type;          
    private final Language language;     
    private final double  globalFrequencyScore;
    private final double personalFrequencyScore;
    private final double finalScore;
    private final int personalOccurrences;
}