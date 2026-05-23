package projeto.backend.core;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StopWords {
    private final Map<String, Integer> ranks;
}
