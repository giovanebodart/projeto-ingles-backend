package projeto.ingles.infrastructure.nlp;

import java.util.List;
import org.springframework.stereotype.Service;
import projeto.ingles.core.nlp.*;

@Service
public class SpacyTextAnalyzer{

    private final NlpClient client;

    public SpacyTextAnalyzer(NlpClient client) {
        this.client = client;
    }

    public AnalyzeResponse analyzeText(List<String> texts){
        AnalyzeRequest request = new AnalyzeRequest(texts);
        return client.analyzeText(request);
    }
}
