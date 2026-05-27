package projeto.backend.core;
import jakarta.persistence.*;

@Entity
@Table(name = "word_forms")
public class WordForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String surface;

    @Column(nullable = false)
    private String lemma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexical_unit_id", nullable = false)
    private LexicalUnit lexicalUnit;
}