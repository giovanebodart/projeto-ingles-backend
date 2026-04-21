package projeto.ingles.infrastructure.persistence.vocabulary;

import projeto.ingles.core.nlp.Language;
import projeto.ingles.core.vocabulary.UniversalWordType;
import projeto.ingles.core.vocabulary.VocabularyEntry;

public class VocabularyMapper {

    public static VocabularyEntry toDomain(VocabularyJpaEntity entity) {
        return new VocabularyEntry(
            entity.getId(),
            entity.getUserId(),
            Language.valueOf(entity.getLanguage()),   
            entity.getLemma(),
            UniversalWordType.valueOf(entity.getType()),
            entity.getOccurrences(),
            entity.getLastScore(),
            entity.getFirstSeenAt(),
            entity.getLastSeenAt()
        );
    }

    public static VocabularyJpaEntity toEntity(VocabularyEntry entry) {
        return VocabularyJpaEntity.builder()
            .id(entry.id())
            .userId(entry.userId())
            .lemma(entry.lemma())
            .language(entry.language().name())        
            .type(entry.type().name())
            .occurrences(entry.occurrences())
            .lastScore(entry.lastScore())
            .firstSeenAt(entry.firstSeenAt())
            .lastSeenAt(entry.lastSeenAt())
            .build();
    }
}