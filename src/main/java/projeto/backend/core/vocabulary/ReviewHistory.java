package projeto.backend.core.vocabulary;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_history")
public class ReviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_vocabulary_id", nullable = false)
    private UserVocabulary userVocabulary;

    @Column(nullable = false)
    private int quality;

    @Column(nullable = false)
    private int previousInterval;

    @Column(nullable = false)
    private int newInterval;

    @Column(nullable = false)
    private double previousEasinessFactor;

    @Column(nullable = false)
    private double newEasinessFactor;

    @Column(nullable = false)
    private LocalDateTime reviewedAt;
}