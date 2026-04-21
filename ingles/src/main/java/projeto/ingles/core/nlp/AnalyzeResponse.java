package projeto.ingles.core.nlp;
import java.util.List;


public record AnalyzeResponse(
    List<AnalyzeResults> results) {
}
