package projeto.backend.core;
import jakarta.persistence.*;

@Entity
@Table(name = "pronunciations")
public class Pronunciation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * IPA notation.
     */
    @Column(nullable = false)
    private String ipa;

    /**
     * URL do áudio.
     */
    @Column(nullable = false)
    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexical_unit_id", nullable = false)
    private LexicalUnit lexicalUnit;

    // getters/setters
}