package projeto.ingles.core.score;
import projeto.ingles.core.nlp.Language;

public interface FrequencyRankingPort {

    Language getLanguage();

    int getRank(String lemma);

    int getTotalWords();

    boolean isTooCommon(String lemma);
}