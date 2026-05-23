package projeto.backend.core;

import java.util.List;

public record NlpResult(
    String text, 
    List<Token> tokens, 
    List<Expression> expressions) {
    
}
