package projeto.backend.core;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import projeto.backend.services.UniversalPOSDeserializer;

@JsonDeserialize(using = UniversalPOSDeserializer.class)
public enum UniversalPOS{
    NOUN, VERB, ADJ, ADV, PRON, DET, PROPN,
    ADP, NUM, AUX, CCONJ, SCONJ, PART, INTJ, PUNCT, SYM, X;

}
