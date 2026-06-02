package projeto.backend.core.dto;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.UniversalPOS;
import java.util.List;

public record MeaningDTO(
        UniversalPOS pos,
        String definition,
        String translation,
        String usageExplanation,
        CefrLevel cefr,
        List<ExampleSentenceDTO> examples,
        List<SemanticRelationDTO> semanticRelations
) {}