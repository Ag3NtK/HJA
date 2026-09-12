package Cartas;
import java.util.*;
import Cartas.carta;

public class mano {
	private ArrayList<carta> mano; //mano dada
	private ArrayList<carta> mejor_mano; //cartas que conforman la mejor mano
	private int manita; //valor numerico de la mejor mano
	private boolean gutshot; //te falta una carta en el medio de la escalera
	private boolean open_ended; //te falta una carta en los extremos de la escalera
	private boolean flush_draw; //te falta una carta para el color

	public mano(ArrayList<carta> cartitas) {
		
		mano=cartitas; //ordenar aqui las cartas o antes de mandarlo?
		mejor_mano = new ArrayList<carta>();
		gutshot = false;
		flush_draw = false;
		open_ended = false;
		comprobar();
		
	}
	
	private void comprobar() {
		if(esColor()) {
			if(esEscalera()) {
				if(mano.get(1).get_valor()==13) {
					manita=9;
					return;
				}else {
					manita=8;
					return;
				}
			}else {
				manita=5;
				return;
			}
		}
		if(esPoker()) {
			manita=7;
			return;
		}
		if(esFull()) {
			manita=6;
			return;
		}
		if(esEscalera()) {
			manita=4;
			return;
		}
		if(esTrio()) {
			manita=3;
			return;
		}
		if(esDoblePareja()) {
			manita=2;
			return;
		}
		if(esPareja()) {
			manita=1;
			return;
		}
		mejor_mano.add(mano.get(0));
		manita = 0;
	}
	//TODO hacer
	private boolean esColor() {

		return true;
	}
	//TODO hacer
	private boolean esEscalera(){
		
		return true;
	}
	//TODO probar
	private boolean esPoker(){ 
		if(mano.get(0).get_valor() == mano.get(1).get_valor() &&
		   mano.get(0).get_valor() == mano.get(2).get_valor() &&
		   mano.get(0).get_valor() == mano.get(3).get_valor()) {
			for(int i = 0; i <= 3; i++) {
				mejor_mano.add(mano.get(i));
			}
			return true;
		}
		if(mano.get(1).get_valor() == mano.get(2).get_valor() &&
		   mano.get(1).get_valor() == mano.get(3).get_valor() &&
		   mano.get(1).get_valor() == mano.get(4).get_valor()) {
			for(int i = 1; i <= 4; i++) {
				mejor_mano.add(mano.get(i));
			}
			return true;
		}
		return false;
	}
	//TODO probar
	private boolean esFull() {
		if(mano.get(0).get_valor() == mano.get(1).get_valor() &&
		   mano.get(0).get_valor() == mano.get(2).get_valor() &&
		   mano.get(3).get_valor() == mano.get(4).get_valor()) {
			mejor_mano = mano;
			return true;
		}
		if(mano.get(0).get_valor() == mano.get(1).get_valor() &&
		   mano.get(2).get_valor() == mano.get(3).get_valor() &&
		   mano.get(2).get_valor() == mano.get(4).get_valor()) {
			mejor_mano = mano;
			return true;
		}
		return false;
	}
	//TODO probar
	private boolean esTrio() {
		for(int i = 0; i < 3; i++) {
			if(mano.get(i).get_valor() == mano.get(i+1).get_valor() && mano.get(i).get_valor() == mano.get(i+2).get_valor()) {
				mejor_mano.add(mano.get(i));
				mejor_mano.add(mano.get(i+1));
				mejor_mano.add(mano.get(i+2));
				return true;
			}
		}
		return false;
	}
	//TODO probar
	private boolean esDoblePareja() {
		for(int i = 0; i < 4; i++) {
			if(mano.get(i).get_valor() == mano.get(i+1).get_valor()) {
				for(int j = i+2; j < 4;j++) {
					if(mano.get(j).get_valor() == mano.get(j+1).get_valor()) {
						mejor_mano.add(mano.get(i));
						mejor_mano.add(mano.get(i+1));
						mejor_mano.add(mano.get(j));
						mejor_mano.add(mano.get(j+1));
						return true;
					}
				}
			}
		}
		return false;
	}
	//TODO probar
	private boolean esPareja() {
		for(int i = 0; i < 4; i++) {
			if(mano.get(i).get_valor() == mano.get(i+1).get_valor()) {
				mejor_mano.add(mano.get(i));
				mejor_mano.add(mano.get(i+1));
				return true;
			}
		}
		return false;
	}
	//TODO hacer (mandas en texto la mejor mano)
	public String mejorManoString() {
		return "hola";
	}
	
	public int mejorManoInt() {
		return manita;
	}
	
	public ArrayList<carta> mejorManoArray(){
		return mejor_mano;
	}
	//TODO hacer (texto que se mando en el caso de que haya un draw de cualquier tipo)
	public String infoMano() {
		return "hola";
	}
}