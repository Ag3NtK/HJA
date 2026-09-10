package Cartas;
import java.util.*;
import Cartas.carta;

public class mano {
	private ArrayList<carta> mano;
	private String manita;// aqui luegoi queda marcado lo q es

	public mano(ArrayList<carta> cartitas) {
		
		mano=cartitas;
		comprobar();
		
	}
	
	private void comprobar() {
		
		if(esColor()) {
			if(esEscalera()) {
				if(mano.get(1).get_valor()==13) {
					manita="royal flush de los cojones";
				}
			}
		}
		
	}
	
	private boolean esColor() {
		
		return true;
	}
	private boolean esEscalera(){
		
		return true;
	}
	private boolean esPoker(){ 
		
		return true;
	}
	private boolean esFull() {
		return true;
	}
	private boolean esTrio() {
		return true;
	}
	private boolean esDoblePareja() {
		return true;
		
	}
	private boolean esPareja() {
		return true;
	}
	
	
}
