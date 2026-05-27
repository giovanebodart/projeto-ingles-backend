package projeto.backend.core;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "lexical_units")
public class LexicalUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255, name = "lemma")
    private String canonicalForm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UniversalWordType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Language language;

    @Column(nullable = false)
    private double frequency;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CefrLevel cefrLevel;

    @OneToMany(
            mappedBy = "lexical_units",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WordForm> forms = new ArrayList<>();

    @OneToMany(
            mappedBy = "lexical_units",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Meaning> meanings = new ArrayList<>();

    @OneToMany(
            mappedBy = "lexical_units",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pronunciation> pronunciations = new ArrayList<>();

    @OneToMany(
        mappedBy = "lexical_units",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Source source;
}