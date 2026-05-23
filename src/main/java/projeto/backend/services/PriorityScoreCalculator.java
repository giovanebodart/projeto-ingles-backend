package projeto.backend.services;
import projeto.backend.core.ReviewStateRequest;
import projeto.backend.core.UserVocabularyRequest;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class PriorityScoreCalculator {

    public double calculate( UserVocabularyRequest vocabulary,ReviewStateRequest reviewState){

        double urgency = calculateUrgency(reviewState);

        double lowMasteryBoost = 100 - vocabulary.masteryScore();

        double difficultyBoost = vocabulary.personalizedDifficulty();

        double occurrenceBoost = Math.min(vocabulary.occurrence() * 2, 20);

        return urgency * 0.4 + lowMasteryBoost * 0.3 + difficultyBoost * 0.2 + occurrenceBoost * 0.1;
    }

    private double calculateUrgency(ReviewStateRequest state) {
        if (state.nextReviewAt().isBefore(LocalDateTime.now())) {
            return 100;
        }
        return 20;
    }
}