package projeto.backend.services;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.UserVocabularyRequest;
import org.springframework.stereotype.Component;

@Component
public class FinalScoreCalculator {

    public double calculate(UserVocabularyRequest term, double masteryScore) {

        // Frequência da palavra no idioma (Zipf): palavras mais frequentes
        // têm maior prioridade de estudo
        double frequencyScore = term.term().frequency() * 0.35;

        // Quanto menos dominada a palavra, maior a prioridade
        double masteryGap = (100 - masteryScore) * 0.30;

        // ADICIONADO: dificuldade intrínseca da palavra pelo nível CEFR
        // Palavras C1/C2 têm maior peso por serem mais difíceis de adquirir
        double cefrScore = mapCefrToScore(term.term().cefrLevel()) * 0.20;

        // ADICIONADO: ocorrência no texto do usuário — palavras que aparecem
        // mais vezes no contexto dele são mais relevantes para aprender
        double occurrenceScore = Math.min(term.occurrence() * 2.0, 20) * 0.15;

        // CORRIGIDO: pesos agora somam 1.0 (0.35 + 0.30 + 0.20 + 0.15)
        return frequencyScore + masteryGap + cefrScore + occurrenceScore;
    }

    // Mapeia nível CEFR para um score de 0–100
    private double mapCefrToScore(CefrLevel level) {
        if (level == null) return 0;
        return switch (level) {
            case A1      -> 10;
            case A2      -> 25;
            case B1      -> 45;
            case B2      -> 65;
            case C1      -> 80;
            case C2      -> 100;
            case UNKNOWN -> 50; // valor neutro quando nível é desconhecido
        };
    }
}