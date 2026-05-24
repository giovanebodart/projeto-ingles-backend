package projeto.backend.services;
import org.springframework.stereotype.Component;
import projeto.backend.core.UserVocabulary;
import projeto.backend.core.UserVocabularyRequest;
import projeto.backend.core.VocabularyTerm;

@Component
public class UserVocabularyMapper {

    private final VocabularyTermMapper termMapper;

    public UserVocabularyMapper(VocabularyTermMapper termMapper) {
        this.termMapper = termMapper;
    }

    /**
     * Converte uma entidade UserVocabulary para seu record de transferência.
     * O VocabularyTerm aninhado também é mapeado via VocabularyTermMapper.
     */
    public UserVocabularyRequest toRequest(UserVocabulary entity) {
        return new UserVocabularyRequest(
                termMapper.toRequest(entity.getTerm()),
                entity.getMasteryScore(),
                entity.getPersonalizedDifficulty(),
                entity.getPriorityScore(),
                entity.getOccurrence(),
                entity.getStatus(),
                entity.getFirstSeenAt(),
                entity.getLastSeenAt());
    }

    /**
     * Converte um UserVocabularyRequest para uma entidade persistível.
     *
     * Recebe o VocabularyTerm já persistido como parâmetro explícito —
     * o mapper não busca nem persiste dependências, isso é responsabilidade
     * do serviço que o chama.
     *
     * @param request  dados do vocabulário do usuário
     * @param userId   id do usuário dono do registro
     * @param term     entidade VocabularyTerm já existente no banco
     */
    public UserVocabulary toEntity(UserVocabularyRequest request, long userId, VocabularyTerm term) {
        UserVocabulary entity = new UserVocabulary();
        entity.setUserId(userId);
        entity.setTerm(term);
        entity.setMasteryScore(request.masteryScore());
        entity.setPersonalizedDifficulty(request.personalizedDifficulty());
        entity.setPriorityScore(request.priorityScore());
        entity.setOccurrence(request.occurrence());
        entity.setStatus(request.status());
        entity.setFirstSeenAt(request.firstSeenAt());
        entity.setLastSeenAt(request.lastSeenAt());
        return entity;
    }

    /**
     * Atualiza os campos mutáveis de um UserVocabulary existente a partir de um request.
     * Preserva id, userId e term — esses nunca mudam após a criação.
     *
     * Usado pelo pipeline após incremento de ocorrência ou após uma revisão.
     */
    public UserVocabulary updateEntity(UserVocabulary existing, UserVocabularyRequest request) {
        existing.setMasteryScore(request.masteryScore());
        existing.setPersonalizedDifficulty(request.personalizedDifficulty());
        existing.setPriorityScore(request.priorityScore());
        existing.setOccurrence(request.occurrence());
        existing.setStatus(request.status());
        existing.setLastSeenAt(request.lastSeenAt());
        // firstSeenAt nunca é atualizado — representa a primeira vez que o usuário viu o termo
        return existing;
    }
}