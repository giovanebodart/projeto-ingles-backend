package projeto.ingles.application;

import org.springframework.stereotype.Component;
import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.score.FrequencyRankingPort;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FrequencyRankingRegistry {

    private final Map<Language, FrequencyRankingPort> registry;

    public FrequencyRankingRegistry(List<FrequencyRankingPort> ports) {
        this.registry = ports.stream()
            .collect(Collectors.toMap(FrequencyRankingPort::getLanguage, Function.identity()));
    }

    public FrequencyRankingPort resolve(Language language) {
        FrequencyRankingPort port = registry.get(language);
        if (port == null) {
            throw new IllegalArgumentException(
                "Ranking de frequência não disponível para: " + language);
        }
        return port;
    }
}