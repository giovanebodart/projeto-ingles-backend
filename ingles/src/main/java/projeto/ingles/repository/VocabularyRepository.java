package projeto.ingles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projeto.ingles.model.entities.ExpressionType;
import projeto.ingles.model.entities.Vocabulary;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    List<Vocabulary> findByUserIdOrderByLastScoreDesc(Long userId);

    List<Vocabulary> findByUserIdAndTypeOrderByLastScoreDesc(Long userId, ExpressionType type);

    Optional<Vocabulary> findByUserIdAndLemma(Long userId, String lemma);

    @Modifying
    @Query(value = """
        INSERT INTO vocabulary (user_id, lemma, type, occurrences, last_score, first_seen_at, last_seen_at)
        VALUES (:userId, :lemma, :type, 1, :score, NOW(), NOW())
        ON CONFLICT (user_id, lemma)
        DO UPDATE SET
            occurrences   = vocabulary.occurrences + 1, 
            last_score    = EXCLUDED.last_score,
            last_seen_at  = NOW()
        """, nativeQuery = true)
    void upsert(
        @Param("userId") Long userId,
        @Param("lemma")  String lemma,
        @Param("type")   String type,
        @Param("score")  double score
    );

    @Query("SELECT v FROM Vocabulary v WHERE v.userId = :userId AND v.lastScore < :threshold ORDER BY v.lastScore ASC")
    List<Vocabulary> findWeakLemmas(@Param("userId") Long userId, @Param("threshold") double threshold);
}