package projeto.backend.services;
import projeto.backend.core.ReviewStateRequest;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class Sm2Scheduler {

    public ReviewStateRequest calculate(ReviewStateRequest state, int quality) {

        // CORRIGIDO: validação do range de qualidade [0, 5]
        if (quality < 0 || quality > 5) {
            throw new IllegalArgumentException(
                "Quality deve estar entre 0 e 5, recebido: " + quality);
        }

        int repetitions = state.repetitions();
        int interval = state.intervalDays();
        double ef = state.easinessFactor();

        // CORRIGIDO: reviewCount é incrementado sempre, independente de acerto ou falha
        int reviewCount = state.reviewCount() + 1;

        if (quality < 3) {
            // Falha: reinicia repetições e intervalo
            repetitions = 0;
            interval    = 1;
        } else {
            // Acerto: progressão de intervalos 1 → 6 → n × EF
            if (repetitions == 0) {
                interval = 1;
            } else if (repetitions == 1) {
                interval = 6;
            } else {
                // CORRIGIDO: guarda mínima de 1 dia para evitar intervalo 0
                // caso intervalDays esteja corrompido no estado anterior
                interval = Math.max(1, (int) Math.round(interval * ef));
            }
            repetitions++;
        }

        // Recalcula EF para qualquer qualidade (inclusive falhas — penaliza o EF)
        ef = calculateEasinessFactor(ef, quality);
        if (ef < 1.3) {
            ef = 1.3;
        }

        // CORRIGIDO: usa a data agendada como base quando a revisão está atrasada,
        // evitando penalizar o usuário por ter feito a revisão tardiamente.
        // Se a revisão está no prazo, usa LocalDateTime.now() normalmente.
        LocalDateTime base = state.nextReviewAt().isBefore(LocalDateTime.now())
                ? state.nextReviewAt()
                : LocalDateTime.now();

        LocalDateTime nextReview = base.plusDays(interval);

        return new ReviewStateRequest(
            state.userVocabulary(),
            repetitions,
            interval,
            reviewCount, 
            ef,
            quality,
            nextReview
        );
    }

    private double calculateEasinessFactor(double ef, int quality) {
        return ef + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
    }
}