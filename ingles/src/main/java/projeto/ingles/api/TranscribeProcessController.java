package projeto.ingles.api;
import org.springframework.stereotype.Controller;
import lombok.extern.log4j.Log4j2;
import projeto.ingles.infrastructure.ai.whisper.WhisperTranscriber;
import projeto.ingles.infrastructure.audio.AudioFilesImpl;
import projeto.ingles.infrastructure.audio.FfmpegTransformer;
import projeto.ingles.infrastructure.audio.YtdlpDownloader;

@Controller
@Log4j2
public class TranscribeProcessController {
    private static final String MP3_FORMAT = ".mp3";
    private static final String WAV_FORMAT = ".wav";
    private final YtdlpDownloader downloader;
    private final FfmpegTransformer transformer;
    private final WhisperTranscriber transcriber;
    private final AudioFilesImpl audioFiles;
    
    public TranscribeProcessController(YtdlpDownloader downloader, WhisperTranscriber transcriber, FfmpegTransformer transformer, 
        AudioFilesImpl audioFiles) {
        this.downloader = downloader;
        this.transcriber = transcriber;
        this.transformer = transformer;
        this.audioFiles = audioFiles;
    }

    //Caoscast: https://youtu.be/7hsHDRCCZRk?si=3OFy6bp46qOoVjWf
    
    public void execute(String videoUrl){
        try{
            downloader.downloadAudio(videoUrl);
            transformer.transformAudioFormat(MP3_FORMAT);
            transcriber.transcribeAudio(WAV_FORMAT);
        }finally{
            audioFiles.removeAudio(MP3_FORMAT);
            audioFiles.removeAudio(WAV_FORMAT);
        }
    }
}
