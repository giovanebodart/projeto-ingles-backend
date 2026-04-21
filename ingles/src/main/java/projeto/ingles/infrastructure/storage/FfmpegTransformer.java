package projeto.ingles.infrastructure.storage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import projeto.ingles.infrastructure.process.BufferCleaner;
import projeto.ingles.infrastructure.process.ComandBuilder;
import projeto.ingles.infrastructure.process.ProcessConfig;

@Component
@Slf4j
public class FfmpegTransformer{

    private final AudioFilesManager audioFiles;
    private final ComandBuilder comandBuilder;
    private final BufferCleaner bufferCleaner;

    public FfmpegTransformer(AudioFilesManager audioFiles, ComandBuilder comandBuilder,BufferCleaner bufferCleaner) {
        this.audioFiles = audioFiles;
        this.comandBuilder = comandBuilder;
        this.bufferCleaner = bufferCleaner;
    }

    public void transformAudioFormat(String audioFormat) {
        Path audioPath = audioFiles.getAudioFile(audioFormat);
        log.atInfo().log("Iniciando transformaçao do áudio: {}", audioPath);
        String fileName = audioPath.getFileName().toString();
        ProcessConfig ffmpegConfig = ProcessConfig.builder()
            .command(buildFfmpegComand(fileName, audioFormat))
            .workingDirectory(audioPath.getParent())
            .build();

        try {
            
            Process process = comandBuilder.startComand(ffmpegConfig);
            Thread stdoutReader = new Thread(() -> bufferCleaner.captureStream(
                new BufferedReader(new InputStreamReader(process.getInputStream())),
                new StringBuilder()
            ));
            stdoutReader.start();
            boolean success = process.waitFor(30, TimeUnit.SECONDS);
            stdoutReader.join(5000);
            
            if(!success){
                process.destroy();
                throw new RuntimeException("Timeout durante a transformação do formato de áudio.");
            }       
            if(process.exitValue() != 0){
                log.atError().log("Código de saída: {}", process.exitValue());
                throw new RuntimeException("Erro ao executar.");
            } 
            log.atInfo().log("Transformaçao concluída com sucesso.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processo de transformação foi interrompido.", e);
        }
    }     

    private List<String> buildFfmpegComand(String filename, String audioFormat){
        List<String> cmd = new LinkedList<>();
        cmd.add("ffmpeg");
        cmd.add("-i");
        cmd.add(filename);
        cmd.add("-ar");
        cmd.add("16000");
        cmd.add("-ac");
        cmd.add("1");
        cmd.add("-c:a");
        cmd.add("pcm_s16le");
        cmd.add(filename.replace(audioFormat,".wav"));
        return cmd;
    }
}
