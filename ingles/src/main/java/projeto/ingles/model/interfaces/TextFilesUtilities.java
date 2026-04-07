package projeto.ingles.model.interfaces;
import java.nio.file.Path;
import java.util.List;

public interface TextFilesUtilities {
    
    List<String> treatText(String text);
    String readTextFile(Path filePath);
}
