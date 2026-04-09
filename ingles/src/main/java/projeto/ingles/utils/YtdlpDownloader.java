package projeto.ingles.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;
import projeto.ingles.config.AudioStorageConfig;
import projeto.ingles.config.ProcessConfig;
import projeto.ingles.model.interfaces.Downloader;

@Component
@Log4j2
public class YtdlpDownloader implements Downloader {

    private final BufferCleaner bufferCleaner;
    private final ComandBuilder comandBuilder;
    private final AudioStorageConfig audioStorageConfig;

    public YtdlpDownloader(BufferCleaner bufferCleaner, ComandBuilder comandBuilder, AudioStorageConfig audioStorageConfig) {
        this.bufferCleaner = bufferCleaner;
        this.comandBuilder = comandBuilder;
        this.audioStorageConfig = audioStorageConfig;
    }

    @Override
    public void downloadAudio(String videoUrl){
        Path outputDir = audioStorageConfig.getResolvedPath();
        ProcessConfig config = ProcessConfig.builder()
            .command(buildYtdlpComand(videoUrl, outputDir))
            .workingDirectory(outputDir)
            .build();
        try {
            Process process = comandBuilder.startComand(config);
            StringBuilder stderrBuilder = new StringBuilder();
            StringBuilder stdoutBuilder = new StringBuilder();

            Thread stderrReader = new Thread(() -> bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getErrorStream())),
                stderrBuilder
            ));

            Thread stdoutReader = new Thread(() -> bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getInputStream())),
                stdoutBuilder
            ));
            
            log.atInfo().log("Download do áudio do vídeo: {} foi iniciado", videoUrl);
            stderrReader.start();
            stdoutReader.start();
            boolean success = process.waitFor(50, TimeUnit.SECONDS);
            stderrReader.join(5000);
            stdoutReader.join(5000);

            if(!success){
                process.destroyForcibly();
                throw new RuntimeException("Timeout durante o download do áudio.");
            }      

            if(process.exitValue() != 0){
                log.atInfo().log("Código de saída: {}", process.exitValue());
                log.atInfo().log("Stdout: {}", stdoutBuilder.toString());
                log.atInfo().log("Stderr: {}", stderrBuilder.toString());
                throw new RuntimeException("Erro ao executar.");
            } 
            log.atInfo().log("Download concluído com sucesso.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processo de download foi interrompido.", e);
        }
    }
    
    private List<String> buildYtdlpComand(String videoUrl, Path outputDir) {
        List<String> cmd = new LinkedList<>();
        cmd.add("yt-dlp");
        cmd.add("-x");
        cmd.add("--audio-format");
        cmd.add("mp3");
        cmd.add("-o");
        cmd.add(outputDir.resolve("%(title)s.%(ext)s").toString());
        cmd.add(videoUrl);
        return cmd;
    }
}   