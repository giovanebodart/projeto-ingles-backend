package projeto.backend.core;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "pronunciations")
public class Pronunciation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * IPA notation.
     */
    @Column(nullable = false)
    private String ipa;

    /**
     * URL do áudio.
     */
    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexical_unit_id", nullable = false)
    private LexicalUnit lexicalUnit;

    // getters/setters
}