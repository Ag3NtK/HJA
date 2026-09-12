package Cartas;

public class carta {
	private int palo;
	private int valor;
	private char valor_string;
	private char palo_string;
	
	public carta(char valor, char palo) {
		
		valor_string = valor;
		palo_string = palo;
		this.valor= valor_a_int(valor);
		this.palo= palo_a_int(palo);
		
	}
	//TODO hacer
	private int valor_a_int(char valor) {
		return 0;
	}
	//TODO hacer
	private int palo_a_int(char palo) {
		return 0;
	}
	
	public int get_palo() {
		return palo;
	}
	
	public int get_valor() {
		return valor;
	}
	
	public char get_palo_string() {
		return palo_string;
	}
	
	public char get_valor_string() {
		return valor_string;
	}
}