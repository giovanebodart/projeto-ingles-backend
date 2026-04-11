package projeto.ingles.model.service;
import projeto.ingles.config.LemmaFilterConfig;
import projeto.ingles.model.entities.ExpressionType;
import projeto.ingles.model.entities.Result;
import projeto.ingles.model.entities.ScoredLemma;
import projeto.ingles.model.interfaces.LemmaFilter;
import lombok.extern.slf4j.Slf4j;
import projeto.ingles.model.dto.FilterLemmaResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FrequencyLemmaFilter implements LemmaFilter {
    private final LemmaFilterConfig config;

    public FrequencyLemmaFilter(LemmaFilterConfig config) {
        this.config = config;
    }

    @Override
    public FilterLemmaResponse filter(Long userId, List<Result> results) {
        log.atInfo().log("Iniciando filtragem de lemmas para usuário {}: {} expressões recebidas",
           userId, results.size());
        int totalWords = config.getSubtitlesRankMap().size();
        long start = System.currentTimeMillis();
        int totalUserOccurrences = 0;
        List<ScoredLemma> scored = results.parallelStream()
        .flatMap(result -> {
            List<ScoredLemma> partial = new ArrayList<>();
            for (var expression : result.expressions()) {
                String lemma = expression.lemma();
                if (lemma == null || lemma.isBlank()) continue;

                ScoredLemma sl = buildScore(lemma, expression.type(), expression.text(), totalWords);
                if (sl != null) partial.add(sl);
            }

            for (var token : result.tokens()) {
                if (token.lemma() == null || token.lemma().isBlank()) continue;
                String lemma = token.lemma().toLowerCase().trim();
                ScoredLemma sl = buildScore(lemma, ExpressionType.TOKEN, token.text(), totalWords);
                if (sl != null) partial.add(sl);
            }
            return partial.stream();
        })
        .sorted(Comparator.comparingDouble(ScoredLemma::getFinalScore).reversed())
        .collect(Collectors.toList());    
        long end = System.currentTimeMillis();
        log.atInfo().log("Filtragem concluída para usuário {}: {} expressões filtradas em {} ms",
             userId, scored.size(), (end - start));
        return new FilterLemmaResponse(scored);    
    }
    private ScoredLemma buildScore(String lemma, ExpressionType type, String originalText, int totalWords) {
        if (config.isTooCommon(lemma)) return null;
        int rank = config.getRank(lemma);
        double globalFrequencyScore = (rank == Integer.MAX_VALUE)
            ? 1.0
            : (double) rank / totalWords;
        int personalOccurrences = 0;
        double personalFrequencyScore = 0.0;
        double finalScore = (globalFrequencyScore * config.getGlobalFrequencyWeight())
            + (personalFrequencyScore * config.getPersonalFrequencyWeight());
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
