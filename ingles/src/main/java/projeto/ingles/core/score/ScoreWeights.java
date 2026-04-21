package projeto.ingles.core.score;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
@ConfigurationProperties(prefix = "score.weight")
public class ScoreWeights {
    private double globalFrequencyWeight = 0.4;
    private double personalFrequencyWeight = 0.6;
    private int decayThreshold = 20;
}
