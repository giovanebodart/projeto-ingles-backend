package projeto.ingles.controller;

import java.nio.file.Path;

import org.springframework.stereotype.Controller;

import projeto.ingles.config.LemmaFilterConfig;
import projeto.ingles.model.dto.FilterLemmaResponse;
import projeto.ingles.model.interfaces.LemmaFilter;
import projeto.ingles.model.interfaces.TextAnalyser;
import projeto.ingles.model.interfaces.TextFilesUtilities;
import projeto.ingles.model.service.VocabularyService;

@Controller
public class AnalyzerProcessController {
    private final TextFilesUtilities textUtilities;
    private final TextAnalyser textAnalyser;
    private final LemmaFilter lemmaFilter;
    private final VocabularyService vocabularyService;
    private static final String FILE_FORMAT = ".txt";   

    public AnalyzerProcessController(TextFilesUtilities textUtilities, TextAnalyser textAnalyser, LemmaFilter lemmaFilter,
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
