package projeto.ingles.model.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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

@Service
@Log4j2
public class WhisperTranscriber implements Transcriber{
    
    private final WhisperConfig whisperConfig;
    private final BufferCleaner bufferCleaner;
    private final ComandBuilder comandBuilder;
    private final AudioFilesUtilities audioFilesUtilities;

    public WhisperTranscriber(WhisperConfig whisperConfig, BufferCleaner bufferCleaner, ComandBuilder comandBuilder,
         AudioFilesUtilities audioFilesUtilities) {
        this.whisperConfig = whisperConfig;
        this.bufferCleaner = bufferCleaner;
        this.comandBuilder = comandBuilder;
        this.audioFilesUtilities = audioFilesUtilities;
    }

    @Override
    public String transcribeAudio(String audioFormat) {
        log.atInfo().log("Transcription started"); 
        long startTime = System.currentTimeMillis();
        try{  
            StringBuilder outputBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();
            ProcessConfig config = ProcessConfig.builder()
                .command(buildWhisperComand(audioFormat))
                .workingDirectory(whisperConfig.getExecutable().getParent())
                .build();
            Process process = comandBuilder.startComand(config);
            Thread stdoutReader = new Thread(() -> bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getInputStream())),
                outputBuilder
            ));
            
            Thread stderr = new Thread(() ->{ bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getErrorStream())), 
                errorBuilder);
            });

            stdoutReader.start();
            stderr.start();
            boolean success = process.waitFor(180, TimeUnit.SECONDS);
            stdoutReader.join(5000);
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
            log.atInfo().log("Transcrição concluída");
            log.atInfo().log("Time to finish: {} ms", endTime - startTime);
            return outputBuilder.toString();
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Transcrição interrompida.", e);
        } finally{
            audioFilesUtilities.removeAudio(audioFormat);
        }
    }

    private List<String> buildWhisperComand(String audioFormat){
        List<String> cmd = new LinkedList<>();
        cmd.add(whisperConfig.getExecutablePath());
        cmd.add("-m");
        cmd.add(whisperConfig.getModelPath());
        cmd.add("-f");
        cmd.add(whisperConfig.getAudioPath().toString());
        cmd.add("-l");
        cmd.add(whisperConfig.getLanguage());
        cmd.add("--gpu-device");
        cmd.add("0");
        cmd.add("--flash-attn");
        cmd.add("-t");
        cmd.add("4");
        cmd.add("--beam-size");
        cmd.add("5");
        cmd.add("--best-of");
        cmd.add("5");
        cmd.add("--no-prints");
        cmd.add("--special");
        cmd.add("false");
        cmd.add("--no-timestamps");
        cmd.add("--suppress-nst");
        cmd.add("--output-file");
        cmd.add("");
        return cmd;
    }
    
}
