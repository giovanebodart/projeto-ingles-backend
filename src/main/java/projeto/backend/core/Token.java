package projeto.backend.core;
import java.util.Map;

public record Token(
    UniversalWordType type,
    String originalText,
    String lemma,
    UniversalPOS pos,
    double zipfFrequency,
    Map<String, String> features) {
}
