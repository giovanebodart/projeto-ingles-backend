package projeto.ingles.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projeto.ingles.core.nlp.AnalyzeResponse;
import projeto.ingles.core.nlp.AnalyzeResults;
import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.score.*;
import projeto.ingles.core.vocabulary.UniversalWordType;
import projeto.ingles.core.vocabulary.VocabularyEntry;
import projeto.ingles.core.vocabulary.VocabularyRepositoryPort; 

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

    private final ScoreWeights config;
    private final FrequencyRankingRegistry rankingRegistry;  // ← resolve port por idioma
    private final VocabularyRepositoryPort vocabularyRepository;

    public FrequencyLemmaFilter(
            ScoreWeights config,
            FrequencyRankingRegistry rankingRegistry,
            VocabularyRepositoryPort vocabularyRepository) {
        this.config           = config;
        this.rankingRegistry  = rankingRegistry;
        this.vocabularyRepository = vocabularyRepository;
    }

    // ← Language agora é parâmetro explícito
    public FilterLemmaResponse filter(Long userId, Language language, List<AnalyzeResults> results) {
        long start = System.currentTimeMillis();
        log.info("Filtragem iniciada — usuário={} idioma={} expressões={}", 
            userId, language, results.size());

        // Busca vocabulário já filtrado por idioma
        Map<String, VocabularyEntry> existingVocabulary = vocabularyRepository
            .findByUserIdAndLanguage(userId, language)
            .stream()
            .collect(Collectors.toMap(VocabularyEntry::lemma, v -> v));

        // Resolve o ranking correto para o idioma recebido
        FrequencyRankingPort ranking = rankingRegistry.resolve(language);

        Set<ScoredLemma> scoredLemmas = results.parallelStream()
            .map(result -> scoreResult(result, language, existingVocabulary, ranking))
            .filter(score -> score != null)
            .sorted(Comparator.comparingDouble(ScoredLemma::getFinalScore).reversed())
            .collect(Collectors.toSet());

        log.info("Filtragem concluída em {} ms", System.currentTimeMillis() - start);
        return new FilterLemmaResponse(language, scoredLemmas);
    }

    private ScoredLemma scoreResult(
            AnalyzeResults result,
            Language language,
            Map<String, VocabularyEntry> existingVocabulary,
            FrequencyRankingPort ranking) {

        // Tenta primeiro nas expressões multi-palavra
        for (var expression : result.expressions()) {
            if (!isValidLemma(expression.lemma())) continue;
            return buildScore(
                expression.lemma(), expression.type(),
                expression.originalText(), language,
                existingVocabulary, ranking);
        }

        // Fallback para tokens simples
        for (var token : result.tokens()) {
            if (!isValidLemma(token.lemma())) continue;
            return buildScore(
                token.lemma(), UniversalWordType.TOKEN,
                token.originalText(), language,
                existingVocabulary, ranking);
        }

        return null;
    }

    private ScoredLemma buildScore(
            String lemma,
            UniversalWordType type,        
            String originalText,
            Language language,
            Map<String, VocabularyEntry> existingVocabulary,
            FrequencyRankingPort ranking) {

        if (ranking.isTooCommon(lemma)) return null;

        int rank       = ranking.getRank(lemma);
        int totalWords = ranking.getTotalWords();

        VocabularyEntry vocab = existingVocabulary.get(lemma);
        int personalOcc = vocab != null ? vocab.occurrences() : 0;

        double personalFrequencyScore = (personalOcc == 0)
            ? 1.0
            : Math.max(0.0, 1.0 - ((double) personalOcc / config.getDecayThreshold()));

        double globalFrequencyScore = (rank == Integer.MAX_VALUE)
            ? 1.0
            : (double) rank / totalWords;

        double finalScore = round(
            (globalFrequencyScore  * config.getGlobalFrequencyWeight()) +
            (personalFrequencyScore * config.getPersonalFrequencyWeight())
        );

        return ScoredLemma.builder()
            .lemma(lemma)
            .type(type)
            .originalText(originalText)
            .language(language)             
            .globalFrequencyScore(globalFrequencyScore)
            .personalFrequencyScore(personalFrequencyScore)
            .finalScore(finalScore)
            .personalOccurrences(personalOcc)
            .build();
    }

    private boolean isValidLemma(String lemma) {
        return lemma != null && !lemma.isBlank();
    }

    private double round(double value) {
        return new BigDecimal(value, new MathContext(3, RoundingMode.HALF_UP)).doubleValue();
    }
}