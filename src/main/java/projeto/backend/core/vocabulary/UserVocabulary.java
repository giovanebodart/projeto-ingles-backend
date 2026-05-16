package projeto.backend.core.vocabulary;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_vocabulary",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "user_id",
                "term_id"
            }
        )
    }
)
public class UserVocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private VocabularyTerm term;

    @Column(name = "mastery_score", nullable = false)
    private double masteryScore;

    @Column(name = "personalized_difficulty", nullable = false)
    private double personalizedDifficulty;

    @Column(name = "priority_score", nullable = false)
    private double priorityScore;

    @Column(name = "occurrence", nullable = false)
    private int occurrence;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VocabularyStatus status;

    @Column(name = "first_seen_at", nullable = false)
    private LocalDateTime firstSeenAt;

    @Column(name = "last_seen_at", nullable = false)
    private LocalDateTime lastSeenAt;
}