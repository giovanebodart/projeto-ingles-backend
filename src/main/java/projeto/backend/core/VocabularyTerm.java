package projeto.backend.core;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "vocabulary_terms",
    indexes = {
        @Index(name = "idx_term_lemma", columnList = "lemma"),
        @Index(name = "idx_term_normalized", columnList = "normalizedLemma")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "normalizedLemma",
                "language",
                "type"
            }
        )
    })
public class VocabularyTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String lemma;

    @Column(name = "normalized_lemma", nullable = false)
    private String normalizedLemma;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UniversalWordType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "pos")
    private UniversalPOS pos;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "cefr_level", nullable = false)
    private CefrLevel cefrLevel;

    @Column(name = "frequency_score", nullable = false)
    private double frequencyScore;

    @Column(name = "expression_score")
    private double expressionScore;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "vocabulary_term_features",
            joinColumns = @JoinColumn(name = "term_id")
    )
    @MapKeyColumn(name = "feature_key")
    @Column(name = "feature_value")
    private Map<String, String> features = new HashMap<>();

}