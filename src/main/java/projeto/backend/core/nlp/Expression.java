package projeto.backend.core.nlp;
import java.util.List;

public record Expression(
    UniversalWordType type, 
    String originalText, 
    String lemma, 
    List<Integer> tokens) {
}
