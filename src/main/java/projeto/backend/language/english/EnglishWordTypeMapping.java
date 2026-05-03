package projeto.backend.language.english;

import projeto.backend.core.nlp.UniversalWordType;
import projeto.backend.infra.nlp.WordTypeMapping;

import java.util.Map;
public class EnglishWordTypeMapping implements WordTypeMapping {

    private static final Map<String, UniversalWordType> MAPPINGS = Map.of(
            "PHRASAL_VERB",     UniversalWordType.MULTI_WORD_EXPRESSION,
            "IDIOM",            UniversalWordType.COLLOCATION,
            "TOKEN",            UniversalWordType.TOKEN
    );

    @Override
    public Map<String, UniversalWordType> mappings() {
        return MAPPINGS;
    }
}