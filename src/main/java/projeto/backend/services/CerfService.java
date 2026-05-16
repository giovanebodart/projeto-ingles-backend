package projeto.backend.services;
import jakarta.annotation.PostConstruct;
import projeto.backend.core.nlp.CerfLevel;
import projeto.backend.core.nlp.Language;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CerfService {

    private final CerfLoader loader;
    private Map<String, CerfLevel> words;

    public CerfService(CerfLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    public void init() {
        this.words = loader.load(Language.EN);
    }

    public CerfLevel findLevel(String lemma) {
        return words.getOrDefault(
                lemma.toLowerCase(),
                CerfLevel.UNKNOWN
        );
    }
}
