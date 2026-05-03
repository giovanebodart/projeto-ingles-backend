package projeto.backend.infra.nlp;

import java.util.HashMap;
import java.util.Map;

import projeto.backend.language.english.EnglishWordTypeMapping;

public class WordTypeMappingRegistry {

    private static final Map<String, WordTypeMapping> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("EN", new EnglishWordTypeMapping());
        // REGISTRY.put("ES", new SpanishWordTypeMapping());
        // REGISTRY.put("FR", new FrenchWordTypeMapping());
    }

    public static WordTypeMapping get(String languageCode) {
        WordTypeMapping mapping = REGISTRY.get(languageCode.toUpperCase());

        if (mapping == null) {
            throw new IllegalArgumentException(
                "Nenhum mapeamento de WordType registrado para o idioma: " + languageCode
            );
        }

        return mapping;
    }
}