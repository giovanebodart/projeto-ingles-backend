package projeto.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import projeto.backend.core.CefrLevel;
import projeto.backend.core.Language;
import projeto.backend.core.NlpServiceRequest;
import projeto.backend.core.NlpServiceResponse;
import projeto.backend.core.UserVocabularyRequest;
import projeto.backend.core.UserVocabulary;
import projeto.backend.core.VocabularyStatus;
import projeto.backend.core.VocabularyTerm;
import projeto.backend.core.VocabularyTermRequest;
import projeto.backend.repositories.UserVocabularyRepository;
import projeto.backend.repositories.VocabularyTermRepository;

/**
 * Orquestra o fluxo completo de processamento de vocabulário a partir de uma URL.
 *
 * Etapas:
 *  1. Envia a URL ao serviço NLP e recebe tokens/expressions
 *  2. Estima o CefrLevel de cada token/expression via heurística
 *  3. Constrói a lista de VocabularyTermRequest
 *  4. Para cada termo: busca VocabularyTerm existente na base ou registra um novo;
 *     se o usuário ainda não viu o termo, cria um UserVocabulary apontando para ele
 *  5. Filtra a lista com base no histórico e nível atual do usuário
 */
@Service
public class VocabularyPipelineService {

    private static final Logger log = Logger.getLogger(VocabularyPipelineService.class.getName());

    private final SpacyNlpService nlpService;
    private final ScoreService scoreService;
    private final VocabularyTermRepository termRepository;
    private final UserVocabularyRepository userVocabularyRepository;
    private final VocabularyFilterService filterService;
    private final VocabularyTermMapper termMapper;
    private final UserVocabularyMapper userVocabularyMapper;

    public VocabularyPipelineService(SpacyNlpService nlpService, ScoreService scoreService,
            VocabularyTermRepository termRepository, UserVocabularyRepository userVocabularyRepository,
            VocabularyFilterService filterService, VocabularyTermMapper termMapper,
            UserVocabularyMapper userVocabularyMapper) {
        this.nlpService = nlpService;
        this.scoreService = scoreService;
        this.termRepository = termRepository;
        this.userVocabularyRepository = userVocabularyRepository;
        this.filterService = filterService;
        this.termMapper = termMapper;
        this.userVocabularyMapper = userVocabularyMapper;
    }

    /**
     * Executa o pipeline completo para uma URL enviada pelo usuário.
     *
     * @param url    URL do conteúdo a ser processado
     * @param userId identificador do usuário que enviou a requisição
     * @return lista filtrada de UserVocabularyRequest prontos para estudo
     */
    public List<UserVocabularyRequest> process(String url, long userId) {

        // --- Etapa 1: NLP ---
        // Envia a URL ao serviço Python/spaCy e recebe tokens e expressões anotados
        log.info("Iniciando pipeline para userId=%d url=%s".formatted(userId, url));
        NlpServiceResponse nlpResponse = nlpService.processText(new NlpServiceRequest(Language.EN, url));

        // --- Etapas 2 e 3: CefrLevel + construção de VocabularyTermRequest ---
        // ScoreService já encapsula as duas etapas: itera tokens/expressions,
        // estima CEFR via heurística e monta os VocabularyTermRequest
        List<VocabularyTermRequest> terms = scoreService.createVocabularyTerms(nlpResponse);
        log.info("NLP retornou %d termos para processar".formatted(terms.size()));

        // --- Etapa 4: persistência e criação de UserVocabulary ---
        // Para cada termo:
        //   a) busca VocabularyTerm existente pelo normalizedLemma + language, ou persiste um novo
        //   b) se o usuário ainda não tem registro para esse termo, cria UserVocabulary novo
        //   c) se já existe, apenas incrementa o contador de ocorrências
        List<UserVocabularyRequest> userVocabularies = terms.stream()
                .map(term -> resolveUserVocabulary(term, userId))
                .toList();

        // --- Etapa 5: filtragem por histórico e nível do usuário ---
        // Remove termos já dominados, abaixo do nível atual ou irrelevantes
        CefrLevel userLevel = resolveUserLevel(userId);
        List<UserVocabularyRequest> filtered = filterService.filter(userVocabularies, userId, userLevel);

        log.info("Pipeline concluído: %d termos após filtragem".formatted(filtered.size()));
        return filtered;
    }

    /**
     * Resolve o UserVocabulary para um termo e um usuário:
     *
     *  - Se o VocabularyTerm não existe na base → persiste
     *  - Se o usuário nunca viu o termo → cria UserVocabulary com status NEW
     *  - Se o usuário já viu o termo → incrementa occurrence e atualiza lastSeenAt
     *
     * @return UserVocabularyRequest representando o estado atual do termo para o usuário
     */
    private UserVocabularyRequest resolveUserVocabulary(VocabularyTermRequest term, long userId) {

        // Busca ou cria o VocabularyTerm canônico na base
        termRepository
                .findByNormalizedLemmaAndLanguageAndType(term.normalizedLemma(), term.language(), term.type())
                .orElseGet(() -> {
                    VocabularyTerm newTerm = new VocabularyTerm(
                            0,
                            term.lemma(),
                            term.normalizedLemma(),
                            term.type(),
                            term.pos(),
                            term.language(),
                            term.cefrLevel(),
                            term.frequency(),
                            term.features());
                    return termRepository.save(newTerm);
                });

        // Verifica se o usuário já tem um registro para esse termo
        return userVocabularyRepository
                .findByUserIdAndNormalizedLemmaAndLanguage(userId, term.normalizedLemma(),term.language())
                .map(userVocabulary -> incrementOccurrence(userVocabularyMapper.toRequest(userVocabulary)))
                .orElseGet(() -> {
                        return userVocabularyMapper.toRequest(userVocabularyRepository.save(createNewUserVocabularyRequest(term, userId)));
                });
    }

    /**
     * Cria um UserVocabularyRequest novo para um termo que o usuário nunca viu.
     * Scores iniciam em zero — serão calculados após a primeira revisão.
     */
    private UserVocabulary createNewUserVocabularyRequest(VocabularyTermRequest term, long userId) {
        LocalDateTime now = LocalDateTime.now();
        return new UserVocabulary(
                        0L,
                        userId,
                        termMapper.toEntity(term),
                        0.0,
                        0.0,
                        0.0,
                        1,
                        VocabularyStatus.NEW,
                        now,
                        now
                );
    }

    /**
     * Incrementa o contador de ocorrências e atualiza lastSeenAt
     * para um termo que o usuário já conhece.
     */
    private UserVocabularyRequest incrementOccurrence(UserVocabularyRequest existing) {
        return new UserVocabularyRequest(
                existing.term(),
                existing.masteryScore(),
                existing.personalizedDifficulty(),
                existing.priorityScore(),
                existing.occurrence() + 1,
                existing.status(),
                existing.firstSeenAt(),
                LocalDateTime.now());    // lastSeenAt atualizado
    }

    /**
     * Resolve o nível CEFR atual do usuário para uso na filtragem.
     * Busca no perfil do usuário; assume A1 como fallback para usuários novos.
     */
    private CefrLevel resolveUserLevel(long userId) {
        return userVocabularyRepository
                .findUserCefrLevel(userId)
                .orElse(CefrLevel.A1);
    }
}