package projeto.backend.core.vocabulary;
import java.time.LocalDateTime;

import projeto.backend.core.nlp.Language;
import projeto.backend.core.nlp.UniversalWordType;

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