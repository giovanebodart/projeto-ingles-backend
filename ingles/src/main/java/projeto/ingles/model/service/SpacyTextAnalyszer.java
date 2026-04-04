package projeto.ingles.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import projeto.ingles.client.NlpClient;
import projeto.ingles.model.dto.AnalyzeRequest;
import projeto.ingles.model.dto.AnalyzeResponse;
import projeto.ingles.model.interfaces.TextAnalyser;

@Service
public class SpacyTextAnalyszer implements TextAnalyser{

    private final NlpClient client;

    public SpacyTextAnalyszer(NlpClient client) {
        this.client = client;
    }

    public AnalyzeResponse analyzeText(List<String> texts){
        AnalyzeRequest request = new AnalyzeRequest(texts);
        return client.analyzeText(request);
    }
}
