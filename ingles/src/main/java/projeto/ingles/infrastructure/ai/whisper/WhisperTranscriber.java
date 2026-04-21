package projeto.ingles.infrastructure.ai.whisper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import projeto.ingles.infrastructure.process.ProcessConfig;
import projeto.ingles.infrastructure.storage.AudioFilesManager;
import projeto.ingles.infrastructure.process.BufferCleaner;
import projeto.ingles.infrastructure.process.ComandBuilder;
import projeto.ingles.infrastructure.storage.TextFilesManager;

@Service
@Log4j2
public class WhisperTranscriber{
    
    private final TextFilesManager textFilesImpl;
    private final WhisperConfig whisperConfig;
    private final BufferCleaner bufferCleaner;
    private final ComandBuilder comandBuilder;
    private final AudioFilesManager audioFilesImpl;

    public WhisperTranscriber(WhisperConfig whisperConfig, BufferCleaner bufferCleaner, ComandBuilder comandBuilder,
         AudioFilesManager audioFilesImpl, TextFilesManager textFilesImpl) {
        this.whisperConfig = whisperConfig;
        this.bufferCleaner = bufferCleaner;
        this.comandBuilder = comandBuilder;
        this.audioFilesImpl = audioFilesImpl;
        this.textFilesImpl = textFilesImpl;
    }

    public String transcribeAudio(String audioFormat) {
        String transcript = "";
        Path audioPath = audioFilesImpl.getAudioFile(audioFormat);
        log.atInfo().log("Audio recebido: {}", audioPath);
        Path outputBasePath = buildTranscriptPath(audioPath);
        Path outputText = outputBasePath.resolveSibling(outputBasePath.getFileName() + ".txt");
        
        ProcessConfig config = ProcessConfig.builder()
            .command(buildWhisperComand(audioPath, outputBasePath))
            .workingDirectory(whisperConfig.getExecutableDirectory())
            .build();
        long startTime = System.currentTimeMillis();
        StringBuilder errorBuilder = new StringBuilder();
        StringBuilder outputBuilder = new StringBuilder();
        try{
            Process process = comandBuilder.startComand(config);
            Thread stdout = new Thread(() -> { bufferCleaner.captureStream(
                    new BufferedReader(new InputStreamReader(process.getInputStream())), 
                    outputBuilder);
            });
            Thread stderr = new Thread(() ->{ bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getErrorStream())), 
                errorBuilder);
            });
                
            log.atInfo().log("Iniciando transcriçao do áudio: {}", audioPath);
            stderr.start();
            stdout.start();
            boolean success = process.waitFor(whisperConfig.getTimeout(), TimeUnit.SECONDS);
            stderr.join(5000);
            stdout.join(5000);
    
            if (!success) {
                process.destroyForcibly();
                throw new RuntimeException("Transcriçao excedeu o timeout de " + whisperConfig.getTimeout() + "s");
            }
    
            if (process.exitValue() != 0) {
                log.error("Whisper stderr: {}", errorBuilder);
                throw new RuntimeException("Processo Whisper falhou com código: " + process.exitValue() +
                    ". Stderr: " + errorBuilder.toString().trim());
            }

            long endTime = System.currentTimeMillis();
            log.atInfo().log("Transcriçao concluída");
            log.atInfo().log("Tempo de execuçao: {} ms", endTime - startTime);
            if (Files.exists(outputText)) {
                transcript = textFilesImpl.readTextFile(outputText);
                log.atInfo().log("Transcriçao salva como: {}", outputText);
                return transcript;
            } else {
                log.atInfo().log("Whisper output: {}", outputBuilder);
                log.atError().log("Whisper stderr: {}", errorBuilder);
                throw new RuntimeException("Arquivo de transcriçao nao foi gerado: " + outputText);
            }
        }catch (Exception e) {
            log.error("Erro durante a transcriçao: {}", e.getMessage(), e);
            throw new RuntimeException("Erro durante a transcriçao: " + e.getMessage(), e);
        }
    }

    private List<String> buildWhisperComand(Path audioPath, Path outputBasePath){
        List<String> cmd = new LinkedList<>();
        cmd.add(whisperConfig.getExecutablePath().toString());
        cmd.add("-m");
        cmd.add(whisperConfig.getModelPath().toString());
        cmd.add("-f");
        cmd.add(audioPath.toAbsolutePath().toString());
        cmd.add("-l");
        cmd.add(whisperConfig.getLanguage());
        cmd.add("--flash-attn");
        cmd.add("-t");
        cmd.add(String.valueOf(whisperConfig.getThreads()));
        cmd.add("--no-timestamps");
        cmd.add("--output-txt");
        cmd.add("--output-file");
        cmd.add(outputBasePath.toAbsolutePath().toString());
        return cmd;
    }

    private Path buildTranscriptPath(Path audioPath) {
        String audioFileName = audioPath.getFileName().toString();
        String baseName = audioFileName.replaceAll(" ", "_").replaceFirst("\\.[^.]+$", "");
        return audioPath.resolveSibling(baseName);
    }
}