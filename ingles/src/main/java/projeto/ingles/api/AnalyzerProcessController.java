package projeto.ingles.api;
import java.nio.file.Path;
import org.springframework.stereotype.Controller;
import projeto.ingles.infrastructure.files.TextFilesImpl;
import projeto.ingles.infrastructure.nlp.SpacyTextAnalyzer;
import projeto.ingles.application.FrequencyLemmaFilter;
import projeto.ingles.application.VocabularyService;
import projeto.ingles.core.score.FilterLemmaResponse;


@Controller
public class AnalyzerProcessController {
    private final TextFilesImpl textUtilities;
    private final SpacyTextAnalyzer textAnalyser;
    private final FrequencyLemmaFilter lemmaFilter;
    private final VocabularyService vocabularyService;
    private static final String FILE_FORMAT = ".txt";   

    public AnalyzerProcessController(TextFilesImpl textUtilities, SpacyTextAnalyzer textAnalyser, FrequencyLemmaFilter lemmaFilter,
         VocabularyService vocabularyService) {
        this.textUtilities = textUtilities;
        this.textAnalyser = textAnalyser;
        this.lemmaFilter = lemmaFilter;
        this.vocabularyService = vocabularyService;
    }

    public FilterLemmaResponse execute() {
        Path filePath = textUtilities.getFilePath(FILE_FORMAT);
        try{
            String fileContent = textUtilities.readTextFile(filePath);
            var treatedText = textUtilities.treatText(fileContent);
            FilterLemmaResponse response = lemmaFilter.filter(1L, textAnalyser.analyzeText(treatedText).results());
            vocabularyService.saveAll(1L, response);
            return response;
        }finally {
            textUtilities.deleteFileIfExists(filePath);
        }
    }
    
}
