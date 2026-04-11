package projeto.ingles.controller;
import java.util.List;
import org.springframework.stereotype.Controller;
import lombok.extern.log4j.Log4j2;
import projeto.ingles.model.dto.FilterLemmaResponse;
import projeto.ingles.model.interfaces.Downloader;
import projeto.ingles.model.interfaces.LemmaFilter;
import projeto.ingles.model.interfaces.TextAnalyser;
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
    private final TextFilesUtilities textUtilities;
    private final TextAnalyser textAnalyser;
    private final LemmaFilter lemmaFilter;
    private final Transformer transformer;
    
    public TranscribeProcessController(Downloader downloader, Transcriber transcriber,TextFilesUtilities textUtilities, 
        TextAnalyser textAnalyser, LemmaFilter lemmaFilter, Transformer transformer) {
        this.downloader = downloader;
        this.transcriber = transcriber;
        this.textUtilities = textUtilities;
        this.textAnalyser = textAnalyser;
        this.lemmaFilter = lemmaFilter;
        this.transformer = transformer;
    }

    //Caoscast: https://youtu.be/7hsHDRCCZRk?si=3OFy6bp46qOoVjWf
    
    public FilterLemmaResponse execute(String videoUrl){
        downloader.downloadAudio(videoUrl);       
        transformer.transformAudioFormat(MP3_FORMAT);
        String text = transcriber.transcribeAudio(WAV_FORMAT);
        List<String> treatedText = textUtilities.treatText(text);
        return lemmaFilter.filter(1L, textAnalyser.analyzeText(treatedText).results());
    }
}
