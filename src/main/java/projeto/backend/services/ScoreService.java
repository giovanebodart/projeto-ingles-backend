package projeto.backend.core.nlp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import projeto.backend.BackendApplication;
import projeto.backend.services.CerfLoader;
import projeto.backend.services.CerfService;
import projeto.backend.services.StopWordLoader;

@Service
public class ScoreService {
    
    private final CerfLoader cerfLoader;
    private final CerfService cerfService;
    private final StopWordLoader stopWordLoader;

    public ScoreService(CerfService cerfService, StopWordLoader stopWordLoader, CerfLoader cerfLoader) {
        this.cerfService = cerfService;
        this.stopWordLoader = stopWordLoader;
        this.cerfLoader = cerfLoader;
    }

    public void calculateFinalScore(NlpServiceResponse response) {
        List<?> object = new ArrayList<>();
        double cerfScore = 0.0;
        double frequencyScore = 0.0;
        for (NlpResult result : response.results()) {
            for (Token token : result.tokens()) {
                cerfScore = calculateCefrScore(CerfLevel.B1, token);
                frequencyScore = calculateFrequencyScore(token);
            }
            for (Expression exp : result.expressions()) {
                if(exp.lemma() == null) continue;
                cerfScore = 100;
                frequencyScore = 100;
            }
        }
    }

    private double calculateCefrScore(CerfLevel userLevel, Token token) {
        CerfLevel level = cerfService.findLevel(token.lemma());
        int distance = Math.abs(userLevel.ordinal() - level.ordinal());
        double score = 0.0;
        switch (distance) {
            case -6 -> score = 1;
            case -5 -> score = 1;
            case -4 -> score = 1;
            case -3 -> score = 5; 
            case -2 -> score = 10;
            case -1 -> score = 50;
            case 0 -> score = 80;
            case 1 -> score = 100;
            case 2 -> score = 70;
            case 3 -> score = 20;
            case 4 -> score = 10;
            case 5 -> score = 5;
            case 6 -> score = 1;
            default -> score = 10;
        }
        return score;
    }

    private double calculateFrequencyScore(Token token){
        var stopWords = stopWordLoader.load(Language.EN, CerfLevel.B1);
        int rank = stopWords.getRanks().getOrDefault(token.lemma(), -1);
        int skip = stopWords.getSkip();
        if(rank == -1) return 85;
        int adjustedRank = rank - skip;
        if (adjustedRank <= 0) return 0;
        int maxRank = stopWords.getRanks().size();
        double normalized = Math.log(adjustedRank) / Math.log(maxRank);
        return normalized * 100;
    }

    private double calculateOccurrenceScore(Token token, List<?> object){
        // Implementation for calculating occurrence score
        return 0.0;
    }
}
