package projeto.ingles.model.service;
import projeto.ingles.config.LemmaFilterConfig;
import projeto.ingles.model.entities.Expression;
import projeto.ingles.model.entities.ExpressionType;
import projeto.ingles.model.entities.Result;
import projeto.ingles.model.entities.ScoredLemma;
import projeto.ingles.model.entities.Token;
import projeto.ingles.model.entities.Vocabulary;
import projeto.ingles.model.interfaces.LemmaFilter;
import projeto.ingles.repository.VocabularyRepository;
import lombok.extern.slf4j.Slf4j;
import projeto.ingles.model.dto.FilterLemmaResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FrequencyLemmaFilter implements LemmaFilter {
    private final LemmaFilterConfig config;
    private final VocabularyRepository vocabularyRepository;

    public FrequencyLemmaFilter(LemmaFilterConfig config, VocabularyRepository vocabularyRepository) {
        this.config = config;
        this.vocabularyRepository = vocabularyRepository;
    }

    @Override
    public FilterLemmaResponse filter(Long userId, List<Result> results) {
        long start = System.currentTimeMillis();
        log.atInfo().log("Iniciando filtragem de lemmas para usuário {}: {} expressões recebidas",
           userId, results.size());
        Map<String, Vocabulary> existingVocabulary = vocabularyRepository.findByUserIdOrderByLastScoreDesc(userId)
            .stream()
            .collect(Collectors.toMap(Vocabulary::getLemma, v -> v));   
        List<ScoredLemma> scoredLemmas = results.parallelStream()  
            .map(result -> {
                for(var expression : result.expressions()) {
                    if (!isValidExpression(expression)) continue;
                    return buildScore(
                        expression.lemma(), expression.type(), expression.text(), existingVocabulary);
                }
                for(var token : result.tokens()) {
                    if (!isValidToken(token)) continue;
                    return buildScore(
                        token.lemma(), token.type(), token.text(), existingVocabulary);   
                }
                return null;
            })
            .filter(score -> score != null)
            .sorted(Comparator.comparingDouble(ScoredLemma::getFinalScore).reversed())  
            .collect(Collectors.toList());
        long end = System.currentTimeMillis();
        log.atInfo().log("Filtragem de lemmas concluída em {} ms", end - start);
        return new FilterLemmaResponse(scoredLemmas);
    }

    private boolean isValidExpression(Expression expression){
        String lemma = expression.lemma();
        if (lemma == null || lemma.isBlank()) return false; 
        return true;
    }

    private boolean isValidToken(Token token){
        String lemma = token.lemma().toLowerCase().trim();
        if (lemma == null || lemma.isBlank()) return false;
        return true;
    }

    private ScoredLemma buildScore(String lemma, ExpressionType type, String originalText, Map<String, Vocabulary> existingVocabulary) {
        if (config.isTooCommon(lemma)) return null;
        Vocabulary vocab = existingVocabulary.get(lemma);

        int personalOccurrences = vocab != null ? vocab.getOccurrences() : 0;

        double personalFrequencyScore = (personalOccurrences == 0) 
            ? 1.0
            : Math.max(0.0, 1.0 - ((double) personalOccurrences / config.getDecayThreshold()));

        double globalFrequencyScore = 1.0 - ((double) config.getRank(lemma) / config.getGlobalFrequencyCutoffRank());

        double finalScore = (globalFrequencyScore * config.getGlobalFrequencyWeight())
            + (personalFrequencyScore * config.getPersonalFrequencyWeight());
        
        return ScoredLemma.builder()
           .lemma(lemma)
           .type(type)
           .originalText(originalText)
           .personalFrequencyScore(personalFrequencyScore)
           .finalScore(finalScore)
           .personalOccurrences(personalOccurrences)
           .build();
    }
}
