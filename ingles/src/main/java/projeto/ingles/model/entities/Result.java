package projeto.ingles.model.entities;
import java.util.List;

public record Result(String text, List<Token> tokens, List<Expression> expressions) {
} 