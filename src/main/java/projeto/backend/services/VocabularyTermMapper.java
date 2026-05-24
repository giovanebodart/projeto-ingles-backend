package projeto.backend.services;

import org.springframework.stereotype.Component;

import projeto.backend.core.VocabularyTerm;
import projeto.backend.core.VocabularyTermRequest;

@Component
public class VocabularyTermMapper {

    /**
     * Converte uma entidade VocabularyTerm para seu record de transferência.
     */
    public VocabularyTermRequest toRequest(VocabularyTerm entity) {
        return new VocabularyTermRequest(
                entity.getLemma(),
                entity.getNormalizedLemma(),
                entity.getType(),
                entity.getPos(),
                entity.getLanguage(),
                entity.getCefrLevel(),
                entity.getFrequency(),
                entity.getFeatures());
    }

    /**
     * Converte um VocabularyTermRequest para uma entidade persistível.
     * O id não é definido aqui — será gerado pelo banco na inserção.
     */
    public VocabularyTerm toEntity(VocabularyTermRequest request) {
        VocabularyTerm entity = new VocabularyTerm();
        entity.setLemma(request.lemma());
        entity.setNormalizedLemma(request.normalizedLemma());
        entity.setType(request.type());
        entity.setPos(request.pos());
        entity.setLanguage(request.language());
        entity.setCefrLevel(request.cefrLevel());
        entity.setFrequency(request.frequency());
        entity.setFeatures(request.features() != null ? request.features() : new java.util.HashMap<>());
        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade existente a partir de um request.
     * Preserva o id e campos imutáveis (normalizedLemma, language, type).
     *
     * Útil quando um mesmo termo é encontrado em outro contexto com
     * frequência ou cefrLevel diferente do que está persistido.
     */
    public VocabularyTerm updateEntity(VocabularyTerm existing, VocabularyTermRequest request) {
        existing.setLemma(request.lemma());
        existing.setPos(request.pos());
        existing.setCefrLevel(request.cefrLevel());
        existing.setFrequency(request.frequency());
        if (request.features() != null) {
            existing.setFeatures(request.features());
        }
        return existing;
    }
}