package projeto.backend.core.nlp;

import java.util.Map;

public record Token(
    UniversalWordType type,
    String originalText,
    String lemma,
    UniversalPOS pos,
    Map<String, Object> features) {
}
