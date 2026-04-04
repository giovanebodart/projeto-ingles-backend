package projeto.ingles.model.dto;
import java.util.List;
import projeto.ingles.model.entities.ScoredLemma;

public record FilterLemmaResponse(List<ScoredLemma> scoredLemmas) {
    
}
