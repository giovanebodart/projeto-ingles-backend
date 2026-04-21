package projeto.ingles.infrastructure.nlp;

import java.util.List;

import org.springframework.stereotype.Component;

import projeto.ingles.core.nlp.AnalyzeRequest;
import projeto.ingles.core.nlp.AnalyzeResponse;
import projeto.ingles.core.nlp.AnalyzerPort;
import projeto.ingles.core.nlp.Language;

@Component
public class SpacyAnalyzer implements AnalyzerPort {

    private final SpacyClient client;

    public SpacyAnalyzer(SpacyClient client) {
        this.client = client;
    }

    @Override
    public AnalyzeResponse analyze(List<String> texts, Language language) {
        AnalyzeRequest request = new AnalyzeRequest(texts, language.name());
        return client.analyzeText(request);
    }
}
