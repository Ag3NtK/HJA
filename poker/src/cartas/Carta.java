package cartas;

public class Carta {
	private int palo;
	private int valor;
	private char valor_char;
	private char palo_char;
	
	public Carta(char valor, char palo) {
		
		valor_char = valor;
		palo_char = palo;
		//TODO control de errores en caso de que algunas de esta funciones devuelva -1
		this.valor= valor_a_int(valor);
		this.palo= palo_a_int(palo);
		
	}
	//TODO probar
	private int valor_a_int(char valor) {
		switch (palo) {
	    case 'A':
	        return 14;
	    case 'K':
	        return 13;
	    case 'Q':
	        return 12;
	    case 'J':
	    	return 11;
	    case 'T':
	        return 10;
	    case '9':
	        return 9;
	    case '8':
	        return 8;
	    case '7':
	    	return 7;
	    case '6':
	        return 6;
	    case '5':
	        return 5;
	    case '4':
	        return 4;
	    case '3':
	    	return 3;
	    case '2':
	        return 2;
	    default:
	    	return -1;
		}
	}
	//TODO probar
	private int palo_a_int(char palo) {
		switch (palo) {
		    case 'h':
		        return 0;
		    case 'd':
		        return 1;
		    case 'c':
		        return 2;
		    case 's':
		    	return 3;
		    default:
		    	return -1;
		}
	}
	
	public int get_palo() {
		return palo;
	}
	
	public int get_valor() {
		return valor;
	}
	// Estas 2 funciones hacen falta si tenemos la de abajo?
	public char get_palo_char() {
		return palo_char;
	}
	
	public char get_valor_char() {
		return valor_char;
	}
	//
	//TODO probar
	public String get_carta_String() {
		return "" + valor_char + palo_char;
	}
	//TODO probar
	public String get_nombre_valor() {
		switch (this.valor_char) {
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