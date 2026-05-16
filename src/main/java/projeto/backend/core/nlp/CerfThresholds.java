package projeto.backend.core.nlp;

public class CerfThresholds {
    
    public static int skipLines(CerfLevel level) {

        return switch (level) {
            case A1 -> 0;
            case A2 -> 500;
            case B1 -> 2000;
            case B2 -> 5000;
            case C1 -> 9500;
            case C2 -> 10000;
            default -> 0;
        };
    }
}
