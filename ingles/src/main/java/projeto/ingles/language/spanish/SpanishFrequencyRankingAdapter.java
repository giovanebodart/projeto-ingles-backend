package projeto.ingles.language.spanish;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

import projeto.ingles.core.nlp.Language;
import projeto.ingles.language.BaseFrequencyRankingAdapter;

@Component
public class SpanishFrequencyRankingAdapter extends BaseFrequencyRankingAdapter {

    private static final String FREQUENCY_LIST_PATH = "spanish/spanish_frequency.txt";
    public SpanishFrequencyRankingAdapter() {
        super(loadWords());
    }

    @Override
    public Language getLanguage() {
        return Language.ES;
    }

    @Override
    protected String normalize(String lemma) {
        return lemma.toLowerCase()
                    .replace("á","a")
                    .replace("é","e")
                    .replace("í","i")
                    .replace("ó","o")
                    .replace("ú","u");
    }

    private static List<String> loadWords() {
        try {
            return Files.readAllLines(Paths.get(FREQUENCY_LIST_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}