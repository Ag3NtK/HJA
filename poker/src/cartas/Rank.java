package cartas;

public enum Rank {
    DOS('2', 2),
    TRES('3', 3),
    CUATRO('4', 4),
    CINCO('5', 5),
    SEIS('6', 6),
    SIETE('7', 7),
    OCHO('8', 8),
    NUEVE('9', 9),
    DIEZ('T', 10),
    JACK('J', 11),
    QUEEN('Q', 12),
    KING('K', 13),
    ACE('A', 14);

    private final char symbol;
    private final int value;

    Rank(char symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }


    public static Rank fromSymbol(char symbol) {
        char upperSymbol = Character.toUpperCase(symbol);
        for (Rank rank : Rank.values()) {
            if (rank.symbol == upperSymbol) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Símbolo no válido: " + symbol);
    }
}