package projeto.backend.core;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "example_sentences")
public class ExampleSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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