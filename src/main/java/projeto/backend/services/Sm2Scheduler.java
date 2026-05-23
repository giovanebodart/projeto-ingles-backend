package projeto.backend.services;
import projeto.backend.core.Sm2Result;
import projeto.backend.core.ReviewStateRequest;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class Sm2Scheduler {

    public Sm2Result calculate(ReviewStateRequest state, int quality) {
        int repetitions = state.repetitions();
        int interval = state.intervalDays();
        double ef = state.easinessFactor();

        if (quality < 3) {
            repetitions = 0;
            interval = 1;
        } else {
            if (repetitions == 0) {
                interval = 1;
            } else if (repetitions == 1) {
                interval = 6;
            } else {
                interval = (int) Math.round(interval * ef);
            }
            repetitions++;
        }

        ef = calculateEasinessFactor(ef, quality);

        if (ef < 1.3) {
            ef = 1.3;
        }

        LocalDateTime nextReview = LocalDateTime.now().plusDays(interval);

        return Sm2Result.builder()
                .repetitions(repetitions)
                .intervalDays(interval)
                .easinessFactor(ef)
                .nextReviewAt(nextReview)
                .build();
    }

    private double calculateEasinessFactor(double ef, int quality) {
        return ef + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
    }
}