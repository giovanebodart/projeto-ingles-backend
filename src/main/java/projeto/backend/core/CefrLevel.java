package projeto.backend.core;

public enum CefrLevel {

    A1(1),
    A2(2),
    B1(3),
    B2(4),
    C1(5),
    C2(6),
    UNKNOWN(0);
    private final int difficulty;

    CefrLevel(int difficulty) {
        this.difficulty = difficulty;
    }
    
    public int difficulty() {
        return difficulty;
    }
}
