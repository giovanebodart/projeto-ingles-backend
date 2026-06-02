package projeto.backend.core.dto;

import projeto.backend.core.RelationType;

public record SemanticRelationDTO(
        Long targetMeaningId,
        RelationType relationType,
        String nuanceExplanation
) {}