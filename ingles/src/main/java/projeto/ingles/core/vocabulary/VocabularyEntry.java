package projeto.ingles.core.vocabulary;
import java.time.LocalDateTime;

import projeto.ingles.core.nlp.Language;

public record VocabularyEntry(
    Long id,
    Long userId,
    Language language,
    String lemma, 
    UniversalWordType type,
    int occurrences,
    double lastScore,
    LocalDateTime firstSeenAt,
    LocalDateTime lastSeenAt) {
}