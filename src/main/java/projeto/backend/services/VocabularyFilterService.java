package projeto.backend.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.UserVocabularyRequest;
import projeto.backend.core.VocabularyStatus;

/**
 * Filtra a lista de UserVocabularyRequest com base no histórico
 * e no nível CEFR atual do usuário.
 *
 * Critérios de exclusão:
 *  - Termos já dominados (mastery >= 90)
 *  - Termos muito abaixo do nível atual do usuário (dois níveis abaixo)
 *  - Termos muito acima do nível atual do usuário (dois níveis acima)
 *  - Termos com status IGNORED (marcados pelo usuário como irrelevantes)
 */
@Service
public class VocabularyFilterService {

    private static final Logger log = Logger.getLogger(VocabularyFilterService.class.getName());

    private static final double MASTERY_THRESHOLD = 90.0;
    private static final int LEVEL_RANGE_BELOW  = 2;
    private static final int LEVEL_RANGE_ABOVE  = 2;

    public List<UserVocabularyRequest> filter(List<UserVocabularyRequest> vocabularies, long userId, CefrLevel userLevel) {

        List<UserVocabularyRequest> filtered = vocabularies.stream()
                .filter(v -> !isDominated(v))
                .filter(v -> !isIgnored(v))
                .filter(v -> isWithinLevelRange(v, userLevel))
                .toList();

        log.info("Filtragem userId=%d: %d → %d termos".formatted(userId, vocabularies.size(), filtered.size()));
        return filtered;
    }

    // Termo com mastery alta o suficiente para não precisar de revisão ativa
    private boolean isDominated(UserVocabularyRequest v) {
        return v.masteryScore() >= MASTERY_THRESHOLD;
    }

    // Usuário sinalizou explicitamente que não quer estudar esse termo
    private boolean isIgnored(UserVocabularyRequest v) {
        return v.status() == VocabularyStatus.IGNORED;
    }

    /**
     * Mantém apenas termos cujo CefrLevel esteja dentro da janela do usuário.
     *
     * Exemplo com userLevel = B1 (ordinal 2):
     *   Janela permitida: A2 (0) até C1 (4) — dois níveis para cada lado
     *   Excluídos: A1 (muito fácil) e C2 (muito difícil por ora)
     */
    private boolean isWithinLevelRange(UserVocabularyRequest v, CefrLevel userLevel) {
        CefrLevel termLevel = v.term().cefrLevel();

        // Termos sem nível definido passam pelo filtro (decisão conservadora)
        if (termLevel == null || termLevel == CefrLevel.UNKNOWN) return true;

        int userOrdinal = userLevel.ordinal();
        int termOrdinal = termLevel.ordinal();

        return termOrdinal >= userOrdinal - LEVEL_RANGE_BELOW
            && termOrdinal <= userOrdinal + LEVEL_RANGE_ABOVE;
    }
}