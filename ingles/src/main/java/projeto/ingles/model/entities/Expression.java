package projeto.ingles.model.entities;
import java.util.List;

public record Expression(ExpressionType type, String text, String lemma, Integer start, Integer end, List<Integer> tokens) {
}
