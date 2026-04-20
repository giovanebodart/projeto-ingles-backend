package projeto.ingles.application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.ingles.core.score.FilterLemmaResponse;
import projeto.ingles.core.vocabulary.Vocabulary;
import projeto.ingles.infrastructure.persistence.VocabularyRepository;
import java.util.List;

@Slf4j
@Service
public class VocabularyService {

    private final VocabularyRepository repository;

    public VocabularyService(VocabularyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(Long userId, FilterLemmaResponse filterResponse) {
        long start = System.currentTimeMillis();
        if (filterResponse.scoredLemmas().isEmpty()) {
            log.atInfo().log("Nenhum lemma relevante encontrado para usuário {}", userId);
            return;
        }
        filterResponse.scoredLemmas().forEach(scored ->
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