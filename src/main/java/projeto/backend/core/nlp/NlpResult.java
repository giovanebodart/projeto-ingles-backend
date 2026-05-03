package projeto.backend.core.nlp;

import java.util.List;

public record NlpResult(
    String text, 
    List<Token> tokens, 
    List<Expression> expressions) {
    
}
