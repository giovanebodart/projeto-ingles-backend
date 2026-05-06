package projeto.backend.core.nlp;

public interface NlpServicePort {
    NlpServiceResponse processText(NlpServiceRequest request);
}
