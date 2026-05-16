package projeto.backend.core.nlp;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StopWords {
    
    private final int skip;
    private final Map<String, Integer> ranks;
}
