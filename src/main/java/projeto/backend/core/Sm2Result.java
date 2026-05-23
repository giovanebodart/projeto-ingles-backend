package projeto.backend.core;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Sm2Result{
        int repetitions;
        int intervalDays;
        double easinessFactor;
        LocalDateTime nextReviewAt;
}