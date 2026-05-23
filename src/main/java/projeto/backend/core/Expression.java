package projeto.backend.core;
import java.util.List;

public record Expression(
    UniversalWordType type, 
    String originalText, 
    String lemma,
    double zipfFrequency, 
    List<Integer> tokens) {
}
