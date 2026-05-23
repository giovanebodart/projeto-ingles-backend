package projeto.backend.services;
import projeto.backend.core.ReviewStateRequest;
import org.springframework.stereotype.Component;

@Component
public class PersonalizedDifficultyCalculator {

    public double calculate(ReviewStateRequest state, int failedReviews) {

        double difficulty = 100 - (state.easinessFactor() * 30);

        difficulty += failedReviews * 5;

        return Math.max(0,Math.min(difficulty, 100));
    }
}