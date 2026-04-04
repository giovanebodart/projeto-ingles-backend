package projeto.ingles.model.service;
import projeto.ingles.config.LemmaFilterConfig;
import projeto.ingles.model.entities.Expression;
import projeto.ingles.model.entities.Result;
import projeto.ingles.model.entities.ScoredLemma;
import projeto.ingles.model.interfaces.LemmaFilter;
import lombok.extern.slf4j.Slf4j;
import projeto.ingles.model.dto.FilterLemmaResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
                long start = System.currentTimeMillis();
                int totalUserOccurrences = 0;
                List<ScoredLemma> scored = new ArrayList<>();
                for(var result : results) {
                        for (var expression : result.expressions()) {
                                String lemma = expression.lemma();
                                if (lemma == null || lemma.isBlank()) continue;
                                if (config.isTooCommon(lemma)) {
                                        log.atInfo().log("Lemma '{}' descartado: rank global {} <= threshold {}",
                                                lemma, config.getRank(lemma), config.getGlobalFrequencyCutoffRank());
                                        continue;
                                }

                                int rank = config.getRank(lemma);
                                int totalWords = config.getSubtitlesRankMap().size();
                                double globalFrequencyScore = rank == Integer.MAX_VALUE
                                        ? 1.0  
                                        : (double) rank / totalWords;
                                int personalOccurrences = 0;

                                double personalFrequencyScore = totalUserOccurrences > 0
                                        ? Math.min((double) personalOccurrences / totalUserOccurrences * 10, 1.0)
                                        : 0.0;

                                double finalScore = (globalFrequencyScore * config.getGlobalFrequencyWeight())
                                        + (personalFrequencyScore * config.getPersonalFrequencyWeight());

                                if (finalScore < config.getMinimumScore()) {
                                        log.atInfo().log("Lemma '{}' descartado por score insuficiente: {}", lemma, finalScore);
                                        continue;
                                }
                                scored.add(ScoredLemma.builder()
                                        .lemma(lemma)
                                        .type(expression.type())
                                        .originalText(expression.text())
                                        .globalFrequencyScore(globalFrequencyScore)
                                        .personalFrequencyScore(personalFrequencyScore)
                                        .finalScore(finalScore)
                                        .personalOccurrences(personalOccurrences)
                                        .build());
                        }
                        scored.sort(Comparator.comparingDouble(ScoredLemma::getFinalScore).reversed());
                        long end = System.currentTimeMillis();
                        log.atInfo().log("Filtragem concluída para usuário {}: {}/{} lemmas aprovados em {} ms",
                                userId, scored.size(), result.expressions().size(), end - start);
                }
                return new FilterLemmaResponse(scored);        
        }
}
