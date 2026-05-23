package projeto.backend.services;

import projeto.backend.core.VocabularyTermRequest;

import org.springframework.stereotype.Component;

@Component
public class FinalScoreCalculator {

    public double calculate(VocabularyTermRequest term, double masteryScore
    ) {

        return term.frequency() * 0.4 + (100 - masteryScore) * 0.2;
    }
}