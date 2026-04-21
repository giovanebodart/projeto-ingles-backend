package projeto.ingles.core.nlp;
import java.util.List;
public record AnalyzeRequest(List<String> texts, String language) {}