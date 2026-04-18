package projeto.ingles.model.entities;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScoredLemma {

    private final String lemma;
    private final ExpressionType type; 
    private final String originalText;
    private final double globalFrequencyScore;   
    private final double personalFrequencyScore;
    private final double finalScore;
    private final int personalOccurrences;
}