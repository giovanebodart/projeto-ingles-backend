package projeto.ingles.model.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.ingles.model.dto.FilterLemmaResponse;
import projeto.ingles.model.entities.Vocabulary;
import projeto.ingles.repository.VocabularyRepository;
import java.util.List;

@Slf4j
@Service
public class VocabularyService {

    private final VocabularyRepository repository;

    public VocabularyService(VocabularyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(Long userId, FilterLemmaResponse filtered) {
        log.atInfo().log("Salvando {} lemmas para usuário {}", filtered.scoredLemmas().size(), userId);
        long start = System.currentTimeMillis();    

        filtered.scoredLemmas().forEach(scored ->
            repository.upsert(
                userId,
                scored.getLemma(),
                scored.getType().name(),
                scored.getFinalScore()
            )
        );

        long end = System.currentTimeMillis();
        log.atInfo().log("Vocabulário persistido em {} ms", end - start);
    }

    @Transactional(readOnly = true)
    public List<Vocabulary> getVocabulary(Long userId) {
        return repository.findByUserIdOrderByLastScoreDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Vocabulary> getWeakLemmas(Long userId, double threshold) {
        return repository.findWeakLemmas(userId, threshold);
    }
}