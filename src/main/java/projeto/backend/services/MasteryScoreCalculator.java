package projeto.backend.services;

import projeto.backend.core.ReviewStateRequest;
import org.springframework.stereotype.Component;

@Component
public class MasteryScoreCalculator {

    public double calculate(ReviewStateRequest state) {

        // Peso das repetições consecutivas sem falha (máx ~50 com 5 reps)
        double repetitionsWeight = state.repetitions() * 10;

        // Peso do fator de facilidade — EF alto = palavra bem consolidada
        double efWeight = state.easinessFactor() * 20;

        // ADICIONADO: bônus pela qualidade da última resposta
        // quality 5 → +10, quality 3 → +2, quality 0 → 0
        double qualityWeight = state.lastReviewQuality() * 2.0;

        // ADICIONADO: bônus pelo volume total de revisões feitas (até +10)
        // Representa a exposição acumulada à palavra ao longo do tempo
        double reviewCountWeight = Math.min(state.reviewCount() * 1.0, 10);

        double score = repetitionsWeight + efWeight + qualityWeight + reviewCountWeight;

        return Math.min(score, 100);
    }
}