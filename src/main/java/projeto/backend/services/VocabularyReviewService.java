package projeto.backend.services;
import projeto.backend.core.ReviewStateRequest;
import projeto.backend.core.UserVocabularyRequest;
import org.springframework.stereotype.Service;

@Service
public class VocabularyReviewService {

    private final Sm2Scheduler scheduler;
    private final MasteryScoreCalculator masteryCalculator;
    private final PersonalizedDifficultyCalculator difficultyCalculator;
    private final PriorityScoreCalculator priorityCalculator;

    public VocabularyReviewService(Sm2Scheduler scheduler,MasteryScoreCalculator masteryCalculator,
            PersonalizedDifficultyCalculator difficultyCalculator,
            PriorityScoreCalculator priorityCalculator) {
        this.scheduler = scheduler;
        this.masteryCalculator = masteryCalculator;
        this.difficultyCalculator = difficultyCalculator;
        this.priorityCalculator = priorityCalculator;
    }

    /**
     * Processa uma revisão e retorna o estado atualizado do vocabulário.
     *
     * CORRIGIDO 1: resultado do SM-2 não é mais descartado.
     * CORRIGIDO 2: record é imutável — reconstruído com novos valores em vez de setters.
     * CORRIGIDO 3: método retorna UserVocabularyRequest em vez de void.
     *
     * @param vocabulary    estado atual do vocabulário do usuário
     * @param state         estado atual da revisão espaçada
     * @param quality       qualidade da resposta [0–5]
     * @param failedReviews número total de revisões com falha até agora
     * @return novo UserVocabularyRequest com mastery, difficulty e priority atualizados
     */
    public UserVocabularyRequest review( UserVocabularyRequest vocabulary, ReviewStateRequest state, int quality,
        int failedReviews) {

        // CORRIGIDO 1: resultado do SM-2 agora é capturado e utilizado
        ReviewStateRequest updatedState = scheduler.calculate(state, quality);

        // Calcula os novos scores com base no estado já atualizado
        double mastery    = masteryCalculator.calculate(updatedState);
        double difficulty = difficultyCalculator.calculate(updatedState, failedReviews);

        // CORRIGIDO 2: record é imutável — constrói novo objeto com valores atualizados
        // Se PriorityScoreCalculator recebesse os valores diretamente:
        double priority = priorityCalculator.calculate(mastery, difficulty, vocabulary, state);

        return new UserVocabularyRequest(
                vocabulary.term(),
                mastery,
                difficulty,
                priority,
                vocabulary.occurrence(),
                vocabulary.status(),
                vocabulary.firstSeenAt(),
                vocabulary.lastSeenAt());
    }
}