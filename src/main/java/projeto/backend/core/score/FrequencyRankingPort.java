package projeto.backend.core.score;
import projeto.backend.core.nlp.Language;

public interface FrequencyRankingPort {

    Language getLanguage();

    int getRank(String lemma);

    int getTotalWords();

    boolean isTooCommon(String lemma);
}