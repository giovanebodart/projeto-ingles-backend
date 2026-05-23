package projeto.backend.services;
import projeto.backend.core.ReviewStateRequest;

import org.springframework.stereotype.Component;

@Component
public class MasteryScoreCalculator {

    public double calculate(ReviewStateRequest state) {

        double repetitionsWeight =
                state.repetitions() * 10;

        double efWeight =
                state.easinessFactor() * 20;

        double score =
                repetitionsWeight + efWeight;

        return Math.min(score, 100);
    }
}