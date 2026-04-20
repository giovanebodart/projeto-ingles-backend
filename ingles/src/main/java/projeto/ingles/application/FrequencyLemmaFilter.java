package projeto.ingles.application;
import projeto.ingles.infrastructure.config.*;
import projeto.ingles.core.score.*;
import projeto.ingles.core.vocabulary.Result;
import projeto.ingles.core.vocabulary.Vocabulary;
import projeto.ingles.core.vocabulary.ExpressionType;
import projeto.ingles.infrastructure.persistence.VocabularyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FrequencyLemmaFilter {
    private final LemmaFilterConfig config;
    private final VocabularyRepository vocabularyRepository;

    public FrequencyLemmaFilter(LemmaFilterConfig config, VocabularyRepository vocabularyRepository) {
        this.config = config;
        this.vocabularyRepository = vocabularyRepository;
    }

    public FilterLemmaResponse filter(Long userId, List<Result> results) {
        long start = System.currentTimeMillis();
        log.atInfo().log("Iniciando filtragem de lemmas para usuário {}: {} expressões recebidas",
           userId, results.size());
        Map<String, Vocabulary> existingVocabulary = vocabularyRepository.findByUserIdOrderByLastScoreDesc(userId)
            .stream()
            .collect(Collectors.toMap(Vocabulary::getLemma, v -> v));
        Set<ScoredLemma> scoredLemmas = results.parallelStream()
            .map(result -> {
                for(var expression : result.expressions()) {
                    if (!isValidLemma(expression.lemma())) continue;
                    return buildScore(
                        expression.lemma(), expression.type(), expression.text(), existingVocabulary);
                }
                
                for(var token : result.tokens()) {
                    if (!isValidLemma(token.lemma())) continue;
                    return buildScore(
                        token.lemma(), token.type(), token.text(), existingVocabulary);   
                }
                return null;
            })
            .filter(score -> score != null)
            .sorted(Comparator.comparingDouble(ScoredLemma::getFinalScore).reversed())  
            .collect(Collectors.toSet());
        long end = System.currentTimeMillis();
        log.atInfo().log("Filtragem de lemmas concluída em {} ms", end - start);
        return new FilterLemmaResponse(scoredLemmas);
    }

    private boolean isValidLemma(String lemma){
        if (lemma == null || lemma.isBlank()) return false; 
        return true;
    }

    private ScoredLemma buildScore(String lemma, ExpressionType type, String originalText, Map<String, Vocabulary> existingVocabulary) {
        if (config.isTooCommon(lemma)) return null;
        int rank = config.getRank(lemma);
        int totalWords = config.getSubtitlesRankMap().size();

        Vocabulary vocab = existingVocabulary.get(lemma);

        int personalOccurrences = vocab != null ? vocab.getOccurrences() : 0;

        double personalFrequencyScore = (personalOccurrences == 0)
            ? 1.0
            : Math.max(0.0, 1.0 - ((double) personalOccurrences / config.getDecayThreshold()));


        double globalFrequencyScore = (rank == Integer.MAX_VALUE)
            ? 1.0
            : (double) rank / totalWords;

        double finalScore = (globalFrequencyScore * config.getGlobalFrequencyWeight())
            + (personalFrequencyScore * config.getPersonalFrequencyWeight());
        
        MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
        BigDecimal finalScoreRounded = new BigDecimal(finalScore, mc);
        finalScore = finalScoreRounded.doubleValue();

        return ScoredLemma.builder()
           .lemma(lemma)
           .type(type)
           .originalText(originalText)
           .globalFrequencyScore(globalFrequencyScore)
           .personalFrequencyScore(personalFrequencyScore)
           .finalScore(finalScore)
           .personalOccurrences(personalOccurrences)
           .build();
    }
}
