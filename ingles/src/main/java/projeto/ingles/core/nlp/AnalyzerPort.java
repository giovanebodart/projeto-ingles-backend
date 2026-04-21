package projeto.ingles.core.nlp;

import java.util.List;

public interface AnalyzerPort {
    AnalyzeResponse analyze(List<String> texts, Language language);
}
