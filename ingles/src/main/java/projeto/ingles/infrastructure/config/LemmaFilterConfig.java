package projeto.ingles.infrastructure.config;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "lemma.filter")
public class LemmaFilterConfig {

    @Value("${lemma.filter.global.frequency}")
    private double globalFrequencyWeight;
    @Value("${lemma.filter.personal.frequency}")
    private double personalFrequencyWeight;
    @Value("${lemma.filter.global.cutoff}")
    private int globalFrequencyCutoffRank;
    @Value("${lemma.filter.minimum.score}")
    private double minimumScoreThreshold;
    @Value("${lemma.filter.decay}")
    private int decayThreshold;
    private static final String FREQUENCY_FILE = "subtitles_frequency.txt";
    private Map<String, Integer> subtitlesRankMap = new HashMap<>();

    @PostConstruct
    public void loadSubtitlesFrequencyList() {
        int rank = 1;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(FREQUENCY_FILE))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    subtitlesRankMap.put(word, rank++);
                }
            }
        } catch (Exception e) {
            log.atError().log("Falha ao carregar subtitles frequency list: {}", e.getMessage());
        }
    }

    public int getRank(String lemma) {
        return subtitlesRankMap.getOrDefault(lemma.toLowerCase(), Integer.MAX_VALUE);
    }

    public boolean isTooCommon(String lemma) {
        return getRank(lemma) <= globalFrequencyCutoffRank;
    }
}