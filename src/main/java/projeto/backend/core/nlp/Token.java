package projeto.backend.core.nlp;

import java.util.Map;

public record Token(
    UniversalWordType type,
    String originalText,
    String lemma,
    PartOfSpeech pos,
    Map<String, Object> features) {
}
