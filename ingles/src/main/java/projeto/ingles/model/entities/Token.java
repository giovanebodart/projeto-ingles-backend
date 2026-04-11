package projeto.ingles.model.entities;

public record Token(ExpressionType type, String text, String lemma, String pos, Integer start, Integer end) {
}
