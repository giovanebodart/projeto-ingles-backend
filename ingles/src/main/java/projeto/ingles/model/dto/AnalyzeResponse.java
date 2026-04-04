package projeto.ingles.model.dto;
import java.util.List;
import lombok.Builder;
import projeto.ingles.model.entities.Result;

@Builder
public record AnalyzeResponse(List<Result> results) {
    
}
