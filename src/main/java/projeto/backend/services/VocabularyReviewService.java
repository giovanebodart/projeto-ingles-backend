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
        PersonalizedDifficultyCalculator difficultyCalculator,PriorityScoreCalculator priorityCalculator
    ) {
        this.scheduler = scheduler;
        this.masteryCalculator = masteryCalculator;
        this.difficultyCalculator = difficultyCalculator;
        this.priorityCalculator = priorityCalculator;
    }

    public void review(UserVocabularyRequest vocabulary,ReviewStateRequest state,int quality,int failedReviews) {
        scheduler.calculate(state,quality);
        double mastery = masteryCalculator.calculate(state);
        double difficulty = difficultyCalculator.calculate(state,failedReviews);
        vocabulary.setMasteryScore(mastery);
        vocabulary.setPersonalizedDifficulty(difficulty);
        double priority = priorityCalculator.calculate(vocabulary, state);
        vocabulary.setPriorityScore(priority);
    }
}