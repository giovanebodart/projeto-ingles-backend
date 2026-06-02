package projeto.backend.core.dto;
import java.time.LocalDate;

public record SourceDTO(
        String url,
        LocalDate accessDate
) {}