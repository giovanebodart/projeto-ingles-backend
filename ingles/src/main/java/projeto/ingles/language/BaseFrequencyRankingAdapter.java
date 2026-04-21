package projeto.ingles.language;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projeto.ingles.core.score.FrequencyRankingPort;

public abstract class BaseFrequencyRankingAdapter implements FrequencyRankingPort {

    protected final Map<String, Integer> ranking = new HashMap<>();
    protected final int totalWords;

    protected BaseFrequencyRankingAdapter(List<String> words) {
        int index = 1;
        for (String word : words) {
            ranking.put(normalize(word), index++);
        }
        this.totalWords = words.size();
    }

    protected String normalize(String lemma) {
        return lemma.toLowerCase();
    }

    @Override
    public int getRank(String lemma) {
        return ranking.getOrDefault(normalize(lemma), Integer.MAX_VALUE);
    }

    @Override
    public int getTotalWords() {
        return totalWords;
    }

    @Override
    public boolean isTooCommon(String lemma) {
        int rank = getRank(lemma);
        return rank > 0 && rank <= 1000; // top 1000 = comum
    }
}