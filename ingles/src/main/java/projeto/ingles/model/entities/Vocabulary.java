package projeto.ingles.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "vocabulary",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_user_lemma",
        columnNames = {"user_id", "lemma"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String lemma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExpressionType type;

    @Column(nullable = false)
    private int occurrences;

    @Column(name = "last_score", nullable = false)
    private double lastScore;

    @Column(name = "first_seen_at", nullable = false, updatable = false)
    private LocalDateTime firstSeenAt;

    @Column(name = "last_seen_at", nullable = false)
    private LocalDateTime lastSeenAt;
}