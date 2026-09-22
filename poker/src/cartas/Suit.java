package cartas;

public enum Suit {
	HEARTS(0, 'h'),
	DIAMONDS(1, 'd'),
	CLUBS(2, 'c'),
	SPADES(3, 's');	//	Color (b)lack y (r)ed

	private final int valor;
	private final char symbol;

	Suit(int value, char symbol) {
		this.symbol = symbol;
		this.valor = value;
	}

	public static Suit fromSymbol(char symbol) {
		char upperSymbol = Character.toLowerCase(symbol);
		for (Suit suit : Suit.values()) {
			if (suit.symbol == upperSymbol) {
				return suit;
			}
		}
		throw new IllegalArgumentException("Símbolo no válido: " + symbol);
	}

	public char getSymbol() {
		return symbol;
	}
	
	public int getValue() {
		return valor;
	}
}



