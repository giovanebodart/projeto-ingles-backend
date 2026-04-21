package projeto.ingles.language.english;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

import projeto.ingles.core.nlp.Language;
import projeto.ingles.language.BaseFrequencyRankingAdapter;

@Component
public class EnglishFrequencyRankingAdapter extends BaseFrequencyRankingAdapter {

    private static final String FREQUENCY_LIST_PATH = "src/main/resources/english_frequency_list.txt";
    public EnglishFrequencyRankingAdapter() {
        super(loadFrequencyList());
    }

    @Override
    public Language getLanguage() {
        return Language.EN;
    }
    
    private static List<String> loadFrequencyList(){
        try {
            return Files.readAllLines(Paths.get(FREQUENCY_LIST_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
