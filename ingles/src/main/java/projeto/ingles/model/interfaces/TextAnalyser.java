package projeto.ingles.model.interfaces;
import java.util.List;
import projeto.ingles.model.dto.AnalyzeResponse;


public interface TextAnalyser {
    
    AnalyzeResponse analyzeText(List<String> texts);
 }
