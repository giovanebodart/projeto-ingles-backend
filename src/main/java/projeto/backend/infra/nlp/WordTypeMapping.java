package projeto.backend.infra.nlp;

import projeto.backend.core.nlp.UniversalWordType;

import java.util.Map;

public interface WordTypeMapping {
    Map<String, UniversalWordType> mappings();

    default UniversalWordType resolve(String rawType) {
        if (rawType == null || rawType.isBlank()) return UniversalWordType.UNKNOWN;

        // 1. Tenta o mapeamento específico do idioma
        UniversalWordType mapped = mappings().get(rawType.trim().toUpperCase());
        if (mapped != null) return mapped;

        // 2. Fallback: tenta converter diretamente como UniversalWordType
        try {
            return UniversalWordType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UniversalWordType.UNKNOWN; // Tipo desconhecido, mas reconhecido como palavra
        }
    }
}