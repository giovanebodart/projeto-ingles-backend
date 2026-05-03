package projeto.backend.core.vocabulary;

import projeto.backend.core.nlp.Language;
import java.util.List;
import java.util.Optional;

public interface VocabularyRepositoryPort {
    VocabularyEntry save(VocabularyEntry entry);
    Optional<VocabularyEntry> findByUserIdAndLemmaAndLanguage(Long userId, String lemma, Language language);
    List<VocabularyEntry> findByUserId(Long userId);
    List<VocabularyEntry> findByUserIdAndLanguage(Long userId, Language language);
}
