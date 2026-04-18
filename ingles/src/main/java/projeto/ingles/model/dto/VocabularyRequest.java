package projeto.ingles.model.dto;

import java.time.LocalDateTime;

import projeto.ingles.model.entities.ExpressionType;

public record VocabularyRequest(
    Long userId,
    String lemma,
    ExpressionType type,
    int occurrences,
    double lastScore,
    LocalDateTime firstSeenAt,
    LocalDateTime lastSeenAt
) {

}
