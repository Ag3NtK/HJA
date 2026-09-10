package Cartas;

public class carta {
	private char palo;
	private int valor;
	
	
	public carta(char valor, char palo) {
		
		this.valor= valor-'0';
		this.palo=palo;
		
	}
	
	public char get_palo() {
		
		return palo;
	}
	
	public int get_valor() {
		
		return valor;
	}
	
	
}

	
