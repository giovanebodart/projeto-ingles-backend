package projeto.ingles.controller;
import org.springframework.stereotype.Controller;
import lombok.extern.log4j.Log4j2;
import projeto.ingles.model.interfaces.AudioFilesUtilities;
import projeto.ingles.model.interfaces.Downloader;
import projeto.ingles.model.interfaces.TextFilesUtilities;
import projeto.ingles.model.interfaces.Transcriber;
import projeto.ingles.model.interfaces.Transformer;

@Controller
@Log4j2
public class TranscribeProcessController {
    private static final String MP3_FORMAT = ".mp3";
    private static final String WAV_FORMAT = ".wav";
    private final Downloader downloader;
    private final Transcriber transcriber;
    private final Transformer transformer;
    private final AudioFilesUtilities audioFiles;
    
    public TranscribeProcessController(Downloader downloader, Transcriber transcriber, Transformer transformer, 
        AudioFilesUtilities audioFiles) {
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
