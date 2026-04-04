package projeto.ingles.config;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

    private double globalFrequencyWeight = 0.6;
    private double personalFrequencyWeight = 0.4;
    private int globalFrequencyCutoffRank = 1500;
    private double minimumScore = 0.35;
    private String subtitlesFrequencyFile = "subtitles_frequency.txt";
    private Map<String, Integer> subtitlesRankMap = new HashMap<>();

    @PostConstruct
    public void loadSubtitlesFrequencyList() {
        int rank = 1;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(subtitlesFrequencyFile))))) {
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