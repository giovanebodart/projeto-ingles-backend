package projeto.backend.infra.nlp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import projeto.backend.core.nlp.UniversalWordType;

import java.io.IOException;

/**
 * Deserializador de UniversalWordType.
 * Delega a resolução ao mapeamento do idioma registrado no WordTypeMappingRegistry.
 *
 * O idioma é passado via @JsonDeserialize ou configurado diretamente na instância:
 *   new UniversalWordTypeDeserializer("EN")
 */

public class UniversalWordTypeDeserializer extends StdDeserializer<UniversalWordType>
        implements ContextualDeserializer {

    private final String languageCode;

    public UniversalWordTypeDeserializer() {
        this("EN"); 
    }

    public UniversalWordTypeDeserializer(String languageCode) {
        super(UniversalWordType.class);
        this.languageCode = languageCode;
    }

    @Override
    public UniversalWordType deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String rawValue = p.getText();
        WordTypeMapping mapping = WordTypeMappingRegistry.get(languageCode);
        return mapping.resolve(rawValue);
    }

    /**
     * Permite criar instâncias com idioma diferente sem precisar de uma subclasse por idioma:
     *   objectMapper.registerModule(new SimpleModule()
     *       .addDeserializer(UniversalWordType.class, new UniversalWordTypeDeserializer("ES")));
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
        return this;
    }
}