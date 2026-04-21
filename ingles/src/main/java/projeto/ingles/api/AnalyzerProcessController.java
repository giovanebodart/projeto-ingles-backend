package projeto.ingles.api;
import java.nio.file.Path;
import org.springframework.stereotype.Controller;
import projeto.ingles.application.FrequencyLemmaFilter;
import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.score.FilterLemmaResponse;
import projeto.ingles.infrastructure.nlp.SpacyAnalyzer;
import projeto.ingles.infrastructure.storage.TextFilesManager;


@Controller
public class AnalyzerProcessController {
    private final TextFilesManager textUtilities;
    private final SpacyAnalyzer textAnalyser;
    private final FrequencyLemmaFilter lemmaFilter;
    private static final String FILE_FORMAT = ".txt";   

    public AnalyzerProcessController(TextFilesManager textUtilities, SpacyAnalyzer textAnalyser, FrequencyLemmaFilter lemmaFilter) {
        this.textUtilities = textUtilities;
        this.textAnalyser = textAnalyser;
        this.lemmaFilter = lemmaFilter;
    }

    public FilterLemmaResponse execute(Language language) {
        Path filePath = textUtilities.getFilePath(FILE_FORMAT);
        try{
            String fileContent = textUtilities.readTextFile(filePath);
            var treatedText = textUtilities.treatText(fileContent);
            FilterLemmaResponse response = lemmaFilter.filter(1L, language, textAnalyser.analyze(treatedText, language).results());
            return response;
        }finally {
            textUtilities.deleteFileIfExists(filePath);
        }
    }
    
}
