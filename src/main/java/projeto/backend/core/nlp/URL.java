package projeto.backend.core.nlp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record URL( @Valid @NotNull String url) {
    
}
