package projeto.backend.core.dto;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.Language;
import projeto.backend.core.UniversalWordType;

import java.util.List;

public record LexicalUnitDTO(
        String canonicalForm,
        UniversalWordType type,
        Language language,
        double frequency,
        CefrLevel cefrLevel,
        List<MeaningDTO> meanings,
        List<PronunciationDTO> pronunciations,
        List<SourceDTO> sources
) {}