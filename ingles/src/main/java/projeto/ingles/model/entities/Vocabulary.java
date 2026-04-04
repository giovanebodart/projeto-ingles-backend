package projeto.ingles.model.entities;
import lombok.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vocabulary {

    private Long id;
    private Long userId;
    private String lemma;
    private ExpressionType type;
    private int occurrences;
    private double lastScore;
    private LocalDateTime firstSeenAt;
    private LocalDateTime lastSeenAt;
}