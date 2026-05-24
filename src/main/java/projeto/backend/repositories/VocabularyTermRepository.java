package projeto.backend.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.backend.core.Language;
import projeto.backend.core.UniversalWordType;
import projeto.backend.core.VocabularyTerm;

@Repository
public interface VocabularyTermRepository extends JpaRepository<VocabularyTerm, Long> {

    /**
     * Busca um termo pelo normalizedLemma + language + type.
     * Reflete a unique constraint da entidade.
     */
    Optional<VocabularyTerm> findByNormalizedLemmaAndLanguageAndType(
            String normalizedLemma,
            Language language,
            UniversalWordType type);

    /**
     * Verifica existência sem carregar a entidade inteira.
     * Útil antes de decidir se deve persistir um novo termo.
     */
    boolean existsByNormalizedLemmaAndLanguageAndType(
            String normalizedLemma,
            Language language,
            UniversalWordType type);
}