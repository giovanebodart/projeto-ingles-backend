package projeto.backend.services;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.Expression;
import projeto.backend.core.Language;
import projeto.backend.core.NlpResult;
import projeto.backend.core.NlpServiceResponse;
import projeto.backend.core.Token;
import projeto.backend.core.VocabularyTermRequest;

@Service
public class ScoreService {

    private static final Logger log = Logger.getLogger(ScoreService.class.getName());
    private final CefrHeuristicEstimator cefrHeuristicEstimator;

    public ScoreService(CefrHeuristicEstimator cefrHeuristicEstimator) {
        this.cefrHeuristicEstimator = cefrHeuristicEstimator;
    }

    public List<VocabularyTermRequest> createVocabularyTerms(NlpServiceResponse response) {
        List<VocabularyTermRequest> list = new ArrayList<>();
        for (NlpResult result : response.results()) {
            for (Token token : result.tokens()) {
                if(token.lemma() == null) continue;
                if(token.lemma().length() <= 2) continue; 
                CefrLevel cefrLevel = cefrHeuristicEstimator.estimate(token.lemma());
                list.add(new VocabularyTermRequest(
                    token.originalText(),
                    token.lemma().toLowerCase(),
                    token.type(),
                    token.pos(),
                    response.language(),
                    cefrLevel,
                    token.zipfFrequency(),
                    token.features()));
            }

            for (Expression exp : result.expressions()) {
                if(exp.lemma() == null) continue;
                if(exp.lemma().length() <= 2) continue;
                CefrLevel cefrLevel = cefrHeuristicEstimator.estimate(exp.lemma());
                list.add(new VocabularyTermRequest(
                    exp.originalText(),
                    exp.lemma().toLowerCase(),
                    exp.type(),
                    null,
                    response.language(),
                    cefrLevel,
                    exp.zipfFrequency(),
                    null));
            }
        }
        log.info("Foram processados %d tokens".formatted(list.size()));
        return list;
    }
}
