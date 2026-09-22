package cartas;

import java.util.Comparator;

public class Carta {
	private Suit palo;
	private Rank valor;
	
	
	public Carta(String carta) {
		valor = Rank.fromSymbol(carta.charAt(0));
		palo = Suit.fromSymbol(carta.charAt(1));		
	}	
	public static final Comparator<Carta> POR_VALOR_DESC = 
	        Comparator.comparingInt(Carta::get_valor).reversed();
	
	public int get_palo() {
		return palo.getValue();
	}
	
	public int get_valor() {
		return valor.getValue();
	}
	//
	
	public String get_carta_String() {
		return "" + valor.getSymbol() + palo.getSymbol();
	}
	
	public String get_nombre_valor() {
		switch (this.valor.getSymbol()) {
	    case 'A':
	        return "Ace";
	    case 'K':
	        return "King";
	    case 'Q':
	        return "Queen";
	    case 'J':
	    	return "Jack";
	    case 'T':
	        return "Ten";
	    case '9':
	        return "Nine";
	    case '8':
	        return "Eight";
	    case '7':
	    	return "Seven";
	    case '6':
	        return "Six";
	    case '5':
	        return "Five";
	    case '4':
	        return "Four";
	    case '3':
	    	return "Three";
	    case '2':
	        return "Two";
	    default:
	    	return null;
		}
	}
}