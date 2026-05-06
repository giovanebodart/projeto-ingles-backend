package projeto.backend.infra.nlp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import projeto.backend.core.nlp.UniversalPOS;

import java.io.IOException;


public class UniversalPOSDeserializer extends StdDeserializer<UniversalPOS> {
    
    public UniversalPOSDeserializer() {
        super(UniversalPOS.class);
    }

    @Override
    public UniversalPOS deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String value = p.getText();

        if (value == null || value.isBlank()) {
            return UniversalPOS.X;
        }

        try {
            return UniversalPOS.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UniversalPOS.X; 
        }
    }
}