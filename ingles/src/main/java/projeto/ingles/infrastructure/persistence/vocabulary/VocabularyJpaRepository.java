package projeto.ingles.infrastructure.persistence.vocabulary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyJpaRepository extends JpaRepository<VocabularyJpaEntity, Long> {

    // ← language em todas as queries
    List<VocabularyJpaEntity> findByUserIdAndLanguageOrderByLastScoreDesc(
        Long userId, String language
    );

    List<VocabularyJpaEntity> findByUserIdAndLanguageAndTypeOrderByLastScoreDesc(
        Long userId, String language, String type
    );

    Optional<VocabularyJpaEntity> findByUserIdAndLemmaAndLanguage(
        Long userId, String lemma, String language
    );

    List<VocabularyJpaEntity> findAllByUserId(
        Long userId
    );

    List<VocabularyJpaEntity> findAllByUserIdAndLanguage(
        Long userId, String language
    );

    // Upsert agora inclui language na constraint de conflito
    @Modifying
    @Query(value = """
        INSERT INTO vocabulary (user_id, lemma, language, type, occurrences, last_score, first_seen_at, last_seen_at)
        VALUES (:userId, :lemma, :language, :type, 1, :score, NOW(), NOW())
        ON CONFLICT (user_id, lemma, language)
        DO UPDATE SET
            occurrences  = vocabulary.occurrences + 1,
            last_score   = EXCLUDED.last_score,
            last_seen_at = NOW()
        """, nativeQuery = true)
    void upsert(
        @Param("userId")   Long userId,
        @Param("lemma")    String lemma,
        @Param("language") String language,
        @Param("type")     String type,
        @Param("score")    double score
    );

    @Query("""
        SELECT v FROM VocabularyJpaEntity v
        WHERE v.userId = :userId
          AND v.language = :language
          AND v.lastScore < :threshold
        ORDER BY v.lastScore ASC
        """)
    List<VocabularyJpaEntity> findWeakLemmas(
        @Param("userId")    Long userId,
        @Param("language")  String language,
        @Param("threshold") double threshold
    );
}