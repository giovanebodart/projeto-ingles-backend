package projeto.backend.core;
import jakarta.persistence.*;

@Entity
@Table(name = "example_sentences")
public class ExampleSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Frase de exemplo.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentence;

    /**
     * Tradução opcional.
     */
    @Column(columnDefinition = "TEXT")
    private String translation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meaning_id", nullable = false)
    private Meaning meaning;

    // getters/setters
}