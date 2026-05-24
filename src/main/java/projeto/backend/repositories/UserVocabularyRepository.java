package projeto.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.Language;
import projeto.backend.core.UserVocabulary;
import projeto.backend.core.VocabularyStatus;

@Repository
public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Long> {

    /**
     * Busca o registro de um usuário para um termo específico.
     * Reflete a unique constraint (user_id, term_id) da entidade.
     */
    Optional<UserVocabulary> findByUserIdAndTermId(long userId, long termId);

    /**
     * Retorna todos os vocabulários de um usuário em um determinado status.
     * Útil para buscar termos NEW, IN_REVIEW, etc.
     */
    List<UserVocabulary> findByUserIdAndStatus(long userId, VocabularyStatus status);

    /**
     * Retorna todos os vocabulários de um usuário para um idioma específico.
     */
    List<UserVocabulary> findByUserIdAndTermLanguage(long userId, Language language);

    Optional<UserVocabulary> findByUserIdAndNormalizedLemmaAndLanguage(long userId,String normalizedLemma, Language language);

    /**
     * Retorna os vocabulários de um usuário com priorityScore acima de um threshold,
     * ordenados por prioridade decrescente.
     * Usado para montar a fila de revisão da sessão.
     */
    List<UserVocabulary> findByUserIdAndPriorityScoreGreaterThanOrderByPriorityScoreDesc(
            long userId,
            double minPriority);

    /**
     * Estima o nível CEFR atual do usuário com base nos termos que ele já domina
     * (mastery >= 80). Retorna o maior CefrLevel encontrado entre esses termos,
     * representando a fronteira do conhecimento consolidado.
     *
     * Se o usuário não tiver nenhum termo dominado, retorna empty → pipeline usa A1.
     */
    @Query("""
            SELECT MAX(v.term.cefrLevel)
            FROM UserVocabulary v
            WHERE v.userId = :userId
              AND v.masteryScore >= 80
            """)
    Optional<CefrLevel> findUserCefrLevel(@Param("userId") long userId);

    /**
     * Retorna os N vocabulários com maior priorityScore para montagem
     * de uma sessão de revisão com tamanho fixo.
     */
    @Query("""
            SELECT v FROM UserVocabulary v
            WHERE v.userId = :userId
              AND v.status <> 'IGNORED'
            ORDER BY v.priorityScore DESC
            LIMIT :limit
            """)
    List<UserVocabulary> findTopByPriority(
            @Param("userId") long userId,
            @Param("limit") int limit);
}