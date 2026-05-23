package projeto.backend.services;

import projeto.backend.core.ReviewStateRequest;
import projeto.backend.core.UserVocabularyRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class PriorityScoreCalculator {

    public double calculate(double mastery, double difficulty, UserVocabularyRequest vocabulary, ReviewStateRequest reviewState) {

        double urgency = calculateUrgency(reviewState);
        double lowMasteryBoost = 100 - mastery; // Quanto menor o domínio, maior o boost
        double difficultyBoost = difficulty;
        double occurrenceBoost = Math.min(vocabulary.occurrence() * 2, 20);

        return urgency * 0.4
             + lowMasteryBoost * 0.3
             + difficultyBoost * 0.2
             + occurrenceBoost * 0.1;
    }

    private double calculateUrgency(ReviewStateRequest state) {
        LocalDateTime now       = LocalDateTime.now();
        LocalDateTime scheduled = state.nextReviewAt();

        if (scheduled.isAfter(now)) {
            // Revisão ainda no prazo: urgência baixa
            return 20;
        }

        // CORRIGIDO: urgência gradual baseada em quantos dias de atraso existem.
        // Evita que um card atrasado 30 dias tenha a mesma urgência
        // que um atrasado há 1 minuto.
        long daysLate = ChronoUnit.DAYS.between(scheduled, now);

        if (daysLate == 0) return 40;       // atrasado menos de 1 dia
        if (daysLate <= 3) return 60;       // 1–3 dias de atraso
        if (daysLate <= 7) return 80;       // 4–7 dias de atraso
        return 100;                          // mais de 7 dias de atraso
    }
}