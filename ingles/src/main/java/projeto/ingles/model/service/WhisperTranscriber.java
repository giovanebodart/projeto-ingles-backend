package projeto.ingles.model.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import projeto.ingles.config.ProcessConfig;
import projeto.ingles.config.WhisperConfig;
import projeto.ingles.model.interfaces.AudioFilesUtilities;
import projeto.ingles.model.interfaces.Transcriber;
import projeto.ingles.utils.BufferCleaner;
import projeto.ingles.utils.ComandBuilder;
import projeto.ingles.utils.TextFilesImpl;

@Service
@Log4j2
public class WhisperTranscriber implements Transcriber{
    
    private final TextFilesImpl textFilesImpl;
    private final WhisperConfig whisperConfig;
    private final BufferCleaner bufferCleaner;
    private final ComandBuilder comandBuilder;
    private final AudioFilesUtilities audioFilesUtilities;

    public WhisperTranscriber(WhisperConfig whisperConfig, BufferCleaner bufferCleaner, ComandBuilder comandBuilder,
         AudioFilesUtilities audioFilesUtilities, TextFilesImpl textFilesImpl) {
        this.whisperConfig = whisperConfig;
        this.bufferCleaner = bufferCleaner;
        this.comandBuilder = comandBuilder;
        this.audioFilesUtilities = audioFilesUtilities;
        this.textFilesImpl = textFilesImpl;
    }

    @Override
    public String transcribeAudio(String audioFormat) {
        String transcript = "";
        log.atInfo().log("Transcriçao iniciada");
        Path audioPath = audioFilesUtilities.getAudioFile(audioFormat); 
        log.atInfo().log("Audio recebido: {}", audioPath);
        ProcessConfig config = ProcessConfig.builder()
            .command(buildWhisperComand(audioFormat, audioPath))
            .workingDirectory(whisperConfig.getExecutableDirectory())
            .build();
        long startTime = System.currentTimeMillis();
        try{  
            StringBuilder errorBuilder = new StringBuilder();
            Process process = comandBuilder.startComand(config);
            Path outputText = audioPath.toAbsolutePath();
            Thread stderr = new Thread(() ->{ bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getErrorStream())), 
                errorBuilder);
            });
            
            log.atInfo().log("Iniciando transcrição do áudio: {}", audioPath);
            stderr.start();
            boolean success = process.waitFor(180, TimeUnit.SECONDS);
            stderr.join(5000);

            if (!success) {
                process.destroyForcibly();
                throw new RuntimeException("Transcrição excedeu o timeout de " + whisperConfig.getTimeout() + "s");
            }
 
            if (process.exitValue() != 0) {
                log.error("Whisper stderr: {}", errorBuilder);
                throw new RuntimeException("Processo Whisper falhou com código: " + process.exitValue() +
                    ". Stderr: " + errorBuilder.toString().trim());
            }
            long endTime = System.currentTimeMillis();
            log.atInfo().log("Transcriçao concluída");
            log.atInfo().log("Tempo de execuçao: {} ms", endTime - startTime);
            if(outputText.endsWith(".txt")) transcript = textFilesImpl.readTextFile(outputText);
            return transcript;
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Transcrição interrompida.", e);
        } finally{
            audioFilesUtilities.removeAudio(audioFormat);
        }
    }

    private List<String> buildWhisperComand(String audioFormat, Path audioPath){
        List<String> cmd = new LinkedList<>();
        cmd.add(whisperConfig.getExecutablePath().toString());
        cmd.add("-m");
        cmd.add(whisperConfig.getModelPath().toString());
        cmd.add("-f");
        cmd.add(audioPath.toAbsolutePath().toString());
        cmd.add("-l");
        cmd.add(whisperConfig.getLanguage());
        cmd.add("--gpu-device");
        cmd.add("0");
        cmd.add("--flash-attn");
        cmd.add("-t");
        cmd.add(String.valueOf(whisperConfig.getThreads()));
        // cmd.add("--beam-size");
        // cmd.add("5");
        // cmd.add("--best-of");
        // cmd.add("5");
        // cmd.add("--no-prints");
        // cmd.add("--special");
        // cmd.add("false");
        cmd.add("--no-timestamps");
        // cmd.add("--suppress-nst");
        cmd.add("--output-txt");
        cmd.add("--output-file");
        cmd.add(audioPath.toAbsolutePath().toString().replace(audioFormat, ".txt"));
        return cmd;
    }
    
}
