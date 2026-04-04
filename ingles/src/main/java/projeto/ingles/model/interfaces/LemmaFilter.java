package projeto.ingles.model.interfaces;
import projeto.ingles.model.entities.Result;
import projeto.ingles.model.dto.FilterLemmaResponse;
import java.util.List;

public interface LemmaFilter {

    /**
     * Recebe uma lista de expressões retornadas pelo microsserviço Python,
     * calcula o score de cada lemma e retorna apenas os que ultrapassam o threshold.
     *
     * @param userId      identificador do usuário (para frequência pessoal)
     * @param expressions lista de expressões extraídas pelo NLP
     * @return lemmas filtrados e ordenados por score decrescente
     */
    FilterLemmaResponse filter(Long userId, List<Result> results);
}