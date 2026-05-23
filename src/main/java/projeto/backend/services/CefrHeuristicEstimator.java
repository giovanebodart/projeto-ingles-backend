package projeto.backend.services;

import org.springframework.stereotype.Component;

import projeto.backend.core.CefrLevel;

/**
 * Estima um {@link CefrLevel} aproximado para um token ou expressão
 * com base em características estruturais universais:
 *
 * <ul>
 *   <li>Número de sílabas estimadas (principal indicador de complexidade)</li>
 *   <li>Comprimento em caracteres</li>
 *   <li>Quantidade de palavras (expressões multi-word)</li>
 * </ul>
 *
 * Não depende de dicionário — útil como fallback quando o lemma
 * não é encontrado no {@link CerfService}.
 */
@Component
public class CefrHeuristicEstimator {

    /**
     * Estima o nível CEFR do token ou expressão fornecido.
     *
     * @param tokenOrExpression token, lemma ou expressão multi-word
     * @return {@link CefrLevel} estimado; nunca {@code null}
     */
    public CefrLevel estimate(String tokenOrExpression) {
        if (tokenOrExpression == null || tokenOrExpression.isBlank()) {
            return CefrLevel.UNKNOWN;
        }

        String normalized = tokenOrExpression.trim().toLowerCase();
        int score = computeScore(normalized);
        return mapToLevel(score);
    }

    private int computeScore(String text) {
        String[] words   = text.split("\\s+");
        int wordCount    = words.length;
        int totalSylls   = totalSyllables(words);
        int charLength   = text.replace(" ", "").length();

        int score = 0;

        // 1. Sílabas totais — principal driver de complexidade
        score += syllableScore(totalSylls);

        // 2. Expressões multi-word são inerentemente mais difíceis
        //    pois o significado frequentemente não é composicional
        if (wordCount >= 4) score += 3;
        else if (wordCount == 3) score += 2;
        else if (wordCount == 2) score += 1;

        // 3. Comprimento em caracteres como desempate para palavras longas
        if (charLength > 12) score += 2;
        else if (charLength > 8) score += 1;

        return score;
    }

    /** Soma as sílabas estimadas de cada palavra da expressão. */
    private int totalSyllables(String[] words) {
        int total = 0;
        for (String word : words) {
            total += estimateSyllables(word);
        }
        return total;
    }

    /**
     * Conta grupos de vogais contíguas como aproximação de sílabas.
     * Funciona razoavelmente para inglês, português, espanhol e francês.
     */
    private int estimateSyllables(String word) {
        final String VOWELS = "aeiouáéíóúàèìòùâêîôûãõäëïöüy";
        int count = 0;
        boolean prevVowel = false;
        for (char c : word.toCharArray()) {
            boolean isVowel = VOWELS.indexOf(c) >= 0;
            if (isVowel && !prevVowel) count++;
            prevVowel = isVowel;
        }
        return Math.max(1, count);
    }

    private int syllableScore(int syllables) {
        return switch (syllables) {
            case 1  -> 0;
            case 2  -> 2;
            case 3  -> 4;
            case 4  -> 6;
            case 5  -> 8;
            default -> syllables >= 6 ? 10 : 0;
        };
    }
    /**
     * Mapeamento de score → nível:
     *
     * <pre>
     *  0–1   → A1   (go, red, bus)
     *  2–3   → A2   (happy, water, always)
     *  4–6   → B1   (important, quickly, look after)
     *  7–9   → B2   (considerable, look forward to)
     *  10–12 → C1   (unfortunately, sophisticated)
     *  13+   → C2   (incomprehensible, idiosyncratic)
     * </pre>
     */
    private CefrLevel mapToLevel(int score) {
        if (score <= 1)  return CefrLevel.A1;
        if (score <= 3)  return CefrLevel.A2;
        if (score <= 6)  return CefrLevel.B1;
        if (score <= 9)  return CefrLevel.B2;
        if (score <= 12) return CefrLevel.C1;
        return CefrLevel.C2;
    }
}