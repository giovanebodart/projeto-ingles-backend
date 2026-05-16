package projeto.backend.services;

import org.springframework.stereotype.Component;

import projeto.backend.core.nlp.CerfLevel;
import projeto.backend.core.nlp.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class CerfLoader {

    private final Logger log = Logger.getLogger(CerfLoader.class.toString());
    private static final Path BASE_PATH = Path.of("data");

    private Path getCerfFile(Language language){
        return BASE_PATH.resolve(language.toString().toUpperCase()).resolve("ENGLISH_CERF_WORDS.csv").toAbsolutePath(); 
    }

    public Map<String, CerfLevel> load(Language language) {
        Path resourcePath = getCerfFile(language);
        Map<String, CerfLevel> map = new HashMap<>();
        if (!Files.exists(resourcePath)) {
            log.warning("CERF words file not found for path: " + resourcePath);
            return Collections.emptyMap();
        }
        try (BufferedReader reader = Files.newBufferedReader(resourcePath, StandardCharsets.UTF_8)) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }
                String word = parts[0].trim().toLowerCase();
                CerfLevel level = CerfLevel.valueOf(parts[1].trim().toUpperCase());
                map.put(word, level);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded %d CERF words for path: %s".formatted(map.size(), resourcePath.toString()));
        return Collections.unmodifiableMap(map);
    }
}
