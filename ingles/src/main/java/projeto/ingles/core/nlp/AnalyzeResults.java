package projeto.ingles.core.nlp;

import java.util.List;

import projeto.ingles.core.vocabulary.Expression;
import projeto.ingles.core.vocabulary.Token;

public record AnalyzeResults(
    String text, 
    Language language, 
    List<Token> tokens, 
    List<Expression> expressions){
}
