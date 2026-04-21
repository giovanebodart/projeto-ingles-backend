package projeto.ingles.application;

import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;

import projeto.ingles.core.nlp.AnalyzerPort;
import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.score.FilterLemmaResponse;
import projeto.ingles.infrastructure.storage.TextFilesManager;

@Service
public class TextAnalyzer {

    private final TextFilesManager textUtilities;
    private final AnalyzerPort analyzer;
    private final FrequencyLemmaFilter lemmaFilter;

    public TextAnalyzer(
        TextFilesManager textUtilities,
        AnalyzerPort analyzer,
        FrequencyLemmaFilter lemmaFilter
    ) {
        this.textUtilities = textUtilities;
        this.analyzer = analyzer;
        this.lemmaFilter = lemmaFilter;
    }

    public FilterLemmaResponse execute(Language language) {

        Path filePath = textUtilities.getFilePath(".txt");

        try {
            String fileContent = textUtilities.readTextFile(filePath);
            var treatedText = textUtilities.treatText(fileContent);

            var response = analyzer.analyze(
                treatedText,
                language
            );

            return lemmaFilter.filter(
                1L,
                language,
                response.results()
            );

        } finally {
            textUtilities.deleteFileIfExists(filePath);
        }
    }
}