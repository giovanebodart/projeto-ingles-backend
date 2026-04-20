package projeto.ingles.core.vocabulary;
import java.util.List;

public record Result(String text, List<Token> tokens, List<Expression> expressions) {
} 