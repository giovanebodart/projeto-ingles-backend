package projeto.ingles.core.vocabulary;
import java.util.List;

import projeto.ingles.core.nlp.Interval;

public record Expression(
    UniversalWordType type, 
    String originalText, 
    String lemma,
    Interval interval,  
    List<Integer> tokens) {
}
