package projeto.backend.core.nlp;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import projeto.backend.infra.nlp.UniversalWordTypeDeserializer;

@JsonDeserialize(using = UniversalWordTypeDeserializer.class)
public enum UniversalWordType {
    MULTI_WORD_EXPRESSION,
    COLLOCATION,
    NAMED_ENTITY,
    TOKEN,
    UNKNOWN
}
