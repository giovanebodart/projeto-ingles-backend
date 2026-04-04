package projeto.ingles.model.entities;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScoredLemma {

    private final String lemma;
    private final ExpressionType type;       
    private final String originalText;       
    private final double globalFrequencyScore;   // quanto esse lemma é incomum globalmente (0-1, maior = mais raro)
    private final double personalFrequencyScore; // frequência nos dados do próprio usuário (0-1, maior = mais recorrente)
    private final double finalScore;             // score composto final
    // Quantas vezes esse lemma já apareceu nas transcrições do usuário
    private final int personalOccurrences;
}