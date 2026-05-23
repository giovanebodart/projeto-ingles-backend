package projeto.backend.core;

import java.time.LocalDateTime;

public record UserVocabularyRequest(
    VocabularyTermRequest term,
    double masteryScore,
    double personalizedDifficulty,
    double priorityScore,
    int occurrence,
    VocabularyStatus status,
    LocalDateTime firstSeenAt,
    LocalDateTime lastSeenAt
) {
    
}
