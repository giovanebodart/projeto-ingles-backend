package projeto.backend.core;
import java.util.Map;

public record VocabularyTermRequest(
    String lemma,
    String normalizedLemma,
    UniversalWordType type,
    UniversalPOS pos,
    Language language,
    CefrLevel cefrLevel,
    double frequency,
    Map<String, String> features) {
    
}
