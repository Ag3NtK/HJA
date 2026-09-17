package cartas;

public enum Suit {
	HEARTS('r', 'h'),
	DIAMONDS('r', 'd'),
	CLUBS('b', 'c'),
	SPADES('b', 's');	//	Color (b)lack y (r)ed

	private final char color;
	private final char symbol;

	Suit(char value, char symbol) {
		this.symbol = symbol;
		this.color = value;
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
	
	public char getColor() {
		return color;
	}
}



