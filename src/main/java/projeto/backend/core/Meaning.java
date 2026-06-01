package projeto.backend.core;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "meanings")
public class Meaning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * POS específico deste significado.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UniversalPOS pos;

    /**
     * Definição principal.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String definition;

    @Column(name = "translation", nullable = true, columnDefinition = "TEXT")
    private String translation;

    /**
     * Explicação pragmática/contextual.
     *
     * Ex:
     * "Usually used in informal speech."
     */
    @Column(columnDefinition = "TEXT")
    private String usageExplanation;

    /**
     * Dificuldade pedagógica do significado.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CefrLevel cefr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexical_unit_id", nullable = false)
    private LexicalUnit lexicalUnit;

    /**
     * Exemplos específicos deste significado.
     */
    @OneToMany(
            mappedBy = "meaning",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ExampleSentence> examples = new ArrayList<>();

    /**
     * Relações semânticas/pragmáticas
     * específicas deste significado.
     */
    @OneToMany(
            mappedBy = "sourceMeaning",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SemanticRelation> semanticRelations = new ArrayList<>();

    // getters/setters
}