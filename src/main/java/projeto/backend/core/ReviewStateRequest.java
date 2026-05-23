package projeto.backend.core;
import java.time.LocalDateTime;

public record ReviewStateRequest (
    UserVocabulary userVocabulary,
    int repetitions,
    int intervalDays,
    int reviewCount,
    double easinessFactor,
    int lastReviewQuality,
    LocalDateTime nextReviewAt
) {
}