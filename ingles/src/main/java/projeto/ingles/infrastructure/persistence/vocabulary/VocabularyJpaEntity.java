package projeto.ingles.infrastructure.persistence.vocabulary;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "vocabulary",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_user_lemma_language",
        columnNames = {"user_id", "lemma", "language"} 
    ),
    indexes = @Index(
        name = "idx_vocabulary_user_language",
        columnList = "user_id, language"
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false, length = 10)
    private String language;         

    @Column(nullable = false, length = 50)
    private String type;             

    @Column(nullable = false)
    private int occurrences;

    @Column(name = "last_score", nullable = false)
    private double lastScore;

    @Column(name = "first_seen_at", nullable = false, updatable = false)
    private LocalDateTime firstSeenAt;

    @Column(name = "last_seen_at", nullable = false)
    private LocalDateTime lastSeenAt;

    @PrePersist
    private void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        firstSeenAt = now;
        lastSeenAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        lastSeenAt = LocalDateTime.now();
    }
}