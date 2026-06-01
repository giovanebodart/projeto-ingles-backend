package projeto.backend.core;
import jakarta.persistence.*;

@Entity
@Table(name = "semantic_relations")
public class SemanticRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Meaning de origem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_meaning_id", nullable = false)
    private Meaning sourceMeaning;

    /**
     * Meaning alvo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_meaning_id", nullable = false)
    private Meaning targetMeaning;

    /**
     * Tipo da relação.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationType relationType;

    /**
     * Explicação opcional da nuance.
     *
     * Ex:
     * "More formal than 'buy'."
     */
    @Column(columnDefinition = "TEXT")
    private String nuanceExplanation;

    // getters/setters
}