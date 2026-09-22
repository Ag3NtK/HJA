package cartas;
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
		if(esEscaleraReal()) {
			manita = 9;
			return;
		}
		if(manita == -1 && esEscaleraColor()) {
			manita = 8;
			return;
		}
		if(manita == -1 && esPoker()) {
			manita=7;
			return;
		}
		if(manita == -1 && esFull()) {
			manita=6;
			return;
		}
		if(manita == -1 && esColor()) {
			manita = 5;
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
	//TODO en el caso de AhAd7c4s2h devuelve gutshot cuando no lo es
	// con AhAdAc7s2h da open_ended
	private boolean esEscalera(){
		int escalera = 0;
		int gutshot = 0;
		for(int i = 0; i < 4; i++) {
			if(mano.get(i).get_valor() == mano.get(i+1).get_valor()+1) { //caso base
				escalera++;
			}else if(mano.get(i).get_valor() == 14 && mano.get(4).get_valor() == 2) {
				escalera++;
			}
			else if(mano.get(i).get_valor() == mano.get(i+1).get_valor()+2) {
				++escalera;
				++gutshot;
			}
		}
		if(escalera == 4&& gutshot==0) {
			mejor_mano = mano;
			return true;
		}
		if(escalera >= 3 && gutshot == 1) {
			this.gutshot = true;
		}
		else if(escalera == 3&& gutshot==0) {
			open_ended = true;
		}
		return false;
	}
	
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
	
	private boolean esEscaleraColor() {
		return esColor() && esEscalera();
	}
	
	private boolean esEscaleraReal() {
		return esColor() && esEscalera() && mano.get(1).get_valor() == 13;
	}
		
	public String mejorManoString() {
		String respuesta = "";
		for(int i = 0; i < mejor_mano.size(); i++) {
			respuesta += mejor_mano.get(i).get_carta_String();
		}
		return respuesta;
	}
	public String manoString() {
		String respuesta = "";
		for(int i = 0; i < mano.size(); i++) {
			respuesta += mano.get(i).get_carta_String();
		}
		return respuesta;
	}
	public int mejorManoInt() {
		return manita;
	}
	
	public ArrayList<Carta> mejorManoArray(){
		return mejor_mano;
	}
	
	public String infoMano() {
		String respuestaMano = " - Best Hand: ";
		String respuestaFlushDraw="";
		String respuestaStraightDraw="";
		
		switch (manita) {
		case 0:
			respuestaMano += "High Card with " + mejor_mano.get(0).get_nombre_valor();
			break;
		case 1:
			respuestaMano += "Pair of "+ mejor_mano.get(0).get_nombre_valor()+"s";
			break;
		case 2:
			respuestaMano += "Two Pair of "+ mejor_mano.get(0).get_nombre_valor()+"s and "+ mejor_mano.get(2).get_nombre_valor()+"s";
			break;
		case 3:
			respuestaMano += "Three of a kind ("+ mejor_mano.get(0).get_nombre_valor()+"s)";
			break;
		case 4:
			respuestaMano += "Straight";
			break;
		case 5:
			respuestaMano += "Flush";
			break;
		case 6: 
			if(mejor_mano.get(0).get_valor() == mejor_mano.get(2).get_valor()) {
				respuestaMano += mano.get(0).get_nombre_valor() + "´s full of " + mano.get(3).get_nombre_valor() + "s";
			}else {
				respuestaMano += mano.get(3).get_nombre_valor() + "´s full of " + mano.get(0).get_nombre_valor() + "s";
			}
			break;
		case 7:
			respuestaMano += "Poker of "+ mejor_mano.get(0).get_nombre_valor()+"s";
			break;
		case 8:
			respuestaMano += "Straight Flush";
			break;
		case 9:
			respuestaMano += "Royal Flush";
			break;
		}
		respuestaMano += " with " + manoString() + "\n";
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