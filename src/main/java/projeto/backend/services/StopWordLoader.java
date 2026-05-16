package projeto.backend.services;

import projeto.backend.core.nlp.CerfLevel;
import projeto.backend.core.nlp.CerfThresholds;
import projeto.backend.core.nlp.Language;
import projeto.backend.core.nlp.StopWords;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class StopWordLoader {

    private static final Logger log = Logger.getLogger(StopWordLoader.class.getName());
    private static final Path BASE_PATH = Path.of("data");

    public StopWords load(Language language, CerfLevel level) {
        return loadFromClasspath(getResourcePath(language), level);
    }

    private Path getResourcePath(Language language) {
        return BASE_PATH.resolve(language.name().toUpperCase()).resolve("stop_words.txt").toAbsolutePath();
    }

    private StopWords loadFromClasspath(Path resourcePath, CerfLevel level) {
        if (!Files.exists(resourcePath)) {
            log.warning("Stop words file not found for path: " + resourcePath);
            return new StopWords(0, Collections.emptyMap());
        }

        Map<String, Integer> ranks = new HashMap<>();
        int skip = CerfThresholds.skipLines(level);
        try (BufferedReader reader = Files.newBufferedReader(resourcePath)) {
            final int[] rank = {skip + 1};
            reader.lines()
                    .skip(skip)
                    .map(String::strip)
                    .map(String::toLowerCase)
                    .filter(line -> !line.isEmpty())
                    .forEach(word -> {
                        ranks.put(word, rank[0]++);
                    });
        } catch (IOException e) {
            log.warning("Error occurred while loading stop words from: " + resourcePath);
            return new StopWords(0, Collections.emptyMap());
        }
        StopWords stopWords = new StopWords(skip, ranks);
        log.info("Loaded %d stop words from: %s".formatted(ranks.size(), resourcePath.toAbsolutePath()));
        return stopWords;
    }
}