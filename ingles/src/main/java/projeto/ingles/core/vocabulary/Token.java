package projeto.ingles.core.vocabulary;

import java.util.Map;

import projeto.ingles.core.nlp.Interval;
import projeto.ingles.core.nlp.PartOfSpeech;

public record Token(
    String originalText, 
    String lemma, 
    PartOfSpeech pos, 
    Interval interval,
    Map<String, Object> features) {
}
