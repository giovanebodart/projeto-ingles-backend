package projeto.backend.infra.nlp;
import java.util.*;
import java.util.stream.Collectors;

import projeto.backend.core.nlp.Expression;
import projeto.backend.core.nlp.NlpResult;
import projeto.backend.core.nlp.Token;

public class NlpResultCleaner {

    public List<NlpResult> clean(List<NlpResult> results) {

        return results.stream()
                .map(this::cleanResult)
                .toList();
    }

    private NlpResult cleanResult(NlpResult result) {

        List<Token> cleanedTokens = removeDuplicateAndCommonTokens(result.tokens());

        List<Expression> cleanedExpressions =
                removeDuplicateAndCommonExpressions(result.expressions());

        return new NlpResult(
                result.text(),
                cleanedTokens,
                cleanedExpressions
        );
    }

    private List<Token> removeDuplicateAndCommonTokens(List<Token> tokens) {

        Set<String> seen = new HashSet<>();

        return tokens.stream()
                .filter(token -> token.lemma() != null)
                .filter(token -> !isStopWord(token.lemma()))
                .filter(token -> seen.add(token.lemma().toLowerCase()))
                .toList();
    }

    private List<Expression> removeDuplicateAndCommonExpressions(
            List<Expression> expressions
    ) {

        Set<String> seen = new HashSet<>();

        return expressions.stream()
                .filter(exp -> exp.lemma() != null)
                .filter(exp -> !isStopWord(exp.lemma()))
                .filter(exp -> seen.add(exp.lemma().toLowerCase()))
                .toList();
    }

    private boolean isStopWord(String text) {
        return true;
    }
}       