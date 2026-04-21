package projeto.ingles.core.nlp;

public enum UniversalPOS implements PartOfSpeech {
    NOUN, VERB, ADJ, ADV, PRON, DET,
    ADP, NUM, AUX, CCONJ, SCONJ, PART, INTJ, PUNCT, SYM, X;

    @Override
    public String code() { return name(); }
}
