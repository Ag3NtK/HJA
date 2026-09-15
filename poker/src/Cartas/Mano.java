package Cartas;
import java.util.*;

public class Mano {
	private ArrayList<Carta> mano; //mano dada
	private ArrayList<Carta> mejor_mano; //cartas que conforman la mejor mano
	private int manita; //valor numerico de la mejor mano
	private Carta kicker; //(TODO añadirlo a todas las comprobaciones necesarias) carta más alta para solucionar empates 
	private boolean gutshot; //te falta una carta en el medio de la escalera
	private boolean open_ended; //te falta una carta en los extremos de la escalera
	private boolean flush_draw; //te falta una carta para el color

	public Mano(ArrayList<Carta> cartitas) {
		
		mano=cartitas; //ordenar aqui las cartas o antes de mandarlo?
		mejor_mano = new ArrayList<Carta>();
		manita = -1;
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
		if(manita == -1 && esPoker()) {
			manita=7;
			return;
		}
		if(manita == -1 && esFull()) {
			manita=6;
			return;
		}
		if(manita == -1 && esEscalera()) {
			manita=4;
			return;
		}
		if(manita == -1 && esTrio()) {
			manita=3;
			return;
		}
		if(manita == -1 && esDoblePareja()) {
			manita=2;
			return;
		}
		if(manita == -1 && esPareja()) {
			manita=1;
			return;
		}
		if(manita == -1) {
			mejor_mano.add(mano.get(0));
			manita = 0;
		}
	}
	//TODO probar (posiblemente se pueda mejorar la comparacion :)
	private boolean esColor() {
		ArrayList<Integer> lista = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
		for(int i = 0; i < 5; i++) {
			int aux = lista.get(mano.get(i).get_palo());
			aux++;
			lista.set(mano.get(i).get_palo(), aux);
		}
		int color = 0;
		for(int i = 0; i < 4; i++) {
			if(lista.get(i)>color) {
				color = lista.get(i);
			}
		}
		if(color == 5) {
			mejor_mano = mano;
			return true;
		}
		else {
			if(color == 4)
				flush_draw = true;
			return false;
		}
	}
	//TODO hacer comprobacion del gutshot y probar
	private boolean esEscalera(){
		int escalera = 0;
		for(int i = 0; i < 4; i++) {
			if(mano.get(i).get_valor() == mano.get(i+1).get_valor()+1) { //caso base
				escalera++;
			}else if(mano.get(i).get_valor() == 14 && mano.get(i).get_valor() == 5) {
				escalera++;
			}
		}
		if(escalera == 4) {
			mejor_mano = mano;
			return true;
		}
		if(escalera == 3) {
			open_ended = true;
		}
		return false;
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
	//TODO probar
	public String mejorManoString() {
		String respuesta = "";
		for(int i = 0; i < mejor_mano.size(); i++) {
			respuesta += mejor_mano.get(i).get_carta_String();
		}
		return respuesta;
	}
	
	public int mejorManoInt() {
		return manita;
	}
	
	public ArrayList<Carta> mejorManoArray(){
		return mejor_mano;
	}
	// esto no se si hacerlo aqui o fuera cuando escribimos la respuesta
	//TODO terminar (texto que se mando en el caso de que haya un draw de cualquier tipo)
	public String infoMano() {
		String respuestaMano = " -Best Hand: ";
		String respuestaFlushDraw="";
		String respuestaStraightDraw="";
		
		switch (manita) {
		case 0:
			respuestaMano += "High Card with " + mejor_mano.get(0).get_nombre_valor();
		case 1:
			respuestaMano += "Pair of "+ mejor_mano.get(0).get_nombre_valor()+"s";
		case 2:
			respuestaMano += "Two Pair of "+ mejor_mano.get(0).get_nombre_valor()+"s and "+ mejor_mano.get(2).get_nombre_valor()+"s";
		case 3:
			respuestaMano += "Three of a kind ("+ mejor_mano.get(0).get_nombre_valor()+"s)";
		case 4:
			respuestaMano += "Straight";
		case 5:
			respuestaMano += "Flush";
		case 6: //jodienda de hacerlo
			respuestaMano += "";
		case 7:
			respuestaMano += "Poker of "+ mejor_mano.get(0).get_nombre_valor()+"s";
		case 8:
			respuestaMano += "Straight Flush";
		case 9:
			respuestaMano += "Royal Flush";
		}
		
		if(flush_draw)
			respuestaFlushDraw += " - Draw: Flush\n";
		if(gutshot)
			respuestaStraightDraw += " - Draw: Straight Gutshot\n";
		else if(open_ended)
			respuestaStraightDraw += " - Draw: Straight Open_Ended\n";
		return respuestaMano + respuestaStraightDraw + respuestaFlushDraw;
	}
	
 	public boolean esGutshot() {
		return gutshot;
	}
	
	public boolean esOpenEnded() {
		return open_ended;
	}
	
	public boolean esFlushDraw() {
		return flush_draw;
	}
	//
	public Carta getKicker() {
		return kicker;
	}
	
}