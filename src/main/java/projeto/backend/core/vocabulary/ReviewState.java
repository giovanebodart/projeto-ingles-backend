package projeto.backend.core.vocabulary;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_states")
public class ReviewState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_vocabulary_id", nullable = false)
    private UserVocabulary userVocabulary;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private int intervalDays;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private double easinessFactor;
    
    private Integer lastReviewQuality;

    @Column(nullable = false)
    private LocalDateTime nextReviewAt;
}