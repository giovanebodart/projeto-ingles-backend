package projeto.ingles.infrastructure.persistence.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.vocabulary.UniversalWordType;
import projeto.ingles.core.vocabulary.VocabularyEntry;
import projeto.ingles.core.vocabulary.VocabularyRepositoryPort;  // ← port do core

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VocabularyRepositoryAdapter implements VocabularyRepositoryPort {

    private final VocabularyJpaRepository jpa;

    
    public void upsert(Long userId, String lemma, Language language,
                       UniversalWordType type, double score) {
        jpa.upsert(userId, lemma, language.name(), type.name(), score);
    }

    @Override
    public Optional<VocabularyEntry> findByUserIdAndLemmaAndLanguage(
            Long userId, String lemma, Language language) {
        return jpa.findByUserIdAndLemmaAndLanguage(userId, lemma, language.name())
                  .map(VocabularyMapper::toDomain);
    }

    @Override
    public List<VocabularyEntry> findByUserIdAndLanguage(Long userId, Language language) {
        return jpa.findAllByUserIdAndLanguage(userId, language.name())
                  .stream()
                  .map(VocabularyMapper::toDomain)
                  .toList();
    }

    public List<VocabularyEntry> findWeakLemmas(Long userId, Language language, double threshold) {
        return jpa.findWeakLemmas(userId, language.name(), threshold)
                  .stream()
                  .map(VocabularyMapper::toDomain)
                  .toList();
    }

    @Override
    public VocabularyEntry save(VocabularyEntry entry) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<VocabularyEntry> findByUserId(Long userId) {
        return jpa.findAllByUserId(userId)
                   .stream()
                   .map(VocabularyMapper::toDomain)
                   .toList();
    }
}