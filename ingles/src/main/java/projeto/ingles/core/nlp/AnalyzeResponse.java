package projeto.ingles.core.nlp;
import java.util.List;
import lombok.Builder;
import projeto.ingles.core.vocabulary.Result;

@Builder
public record AnalyzeResponse(List<Result> results) {
    
}
