package projeto.ingles.model.interfaces;

import java.util.List;

import projeto.ingles.model.entities.Vocabulary;

public interface WordSearcher {
        
    List<Vocabulary> searchWord(List<String> words);

}
