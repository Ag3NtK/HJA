package cartas;
import java.util.*;

public class Mano implements Comparable<Mano>{
	private ArrayList<Carta> mano; //mano dada
	private ArrayList<Carta> mejor_mano; //cartas que conforman la mejor mano
	private int manita; //valor numerico de la mejor mano
	private int[] valoresComparacion; //valores para desempate ordenados por importancia
	private boolean gutshot; //te falta una carta en el medio de la escalera
	private boolean open_ended; //te falta una carta en los extremos de la escalera
	private boolean flush_draw; //te falta una carta para el color

	public Mano(ArrayList<Carta> cartitas) {
		
		mano=cartitas;
		mejor_mano = new ArrayList<Carta>();
		manita = -1;
		valoresComparacion = new int[0];
		gutshot = false;
		flush_draw = false;
		open_ended = false;
		comprobar();
		detectarDraws();
		
	}


	//	TODO cambiar el orden y que se compruebe si es escalera primero.
	//	En el caso de ser escalera, se mira si tmb es de color
	//	En el caso de ser de color se mira tmb si es Real
	//	De esta forma nos ahorramos muchas llamadas a funciones
	private void comprobar() {
		if(esEscaleraReal()) {
			manita = 9;
			valoresComparacion = new int[]{14};
			return;
		}
		if(manita == -1 && esEscaleraColor()) {
			manita = 8;
			boolean tieneAs = mano.get(0).get_valor() == 14 && mano.get(4).get_valor() == 2;
			valoresComparacion = new int[]{tieneAs ? 5 : mano.get(0).get_valor()};
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
			valoresComparacion = new int[5];
			for(int i = 0; i < 5; i++) {
				valoresComparacion[i] = mano.get(i).get_valor();
			}
			return;
		}
		if(manita == -1 && esEscalera()) {
			manita=4;
			boolean tieneAs = mano.get(0).get_valor() == 14 && mano.get(4).get_valor() == 2;
			valoresComparacion = new int[]{tieneAs ? 5 : mano.get(0).get_valor()};
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
			valoresComparacion = new int[5];
			for(int i = 0; i < 5; i++) {
				valoresComparacion[i] = mano.get(i).get_valor();
			}
		}
	}
	private boolean esColor() {
		
		int contador=1;
		while(contador<5&&mano.get(contador).get_palo()==mano.get(contador-1).get_palo()) {
			contador++;
		}
		if(contador==5) {
			mejor_mano = mano;
			return true;
		}
		else
			return false;
	}
	
	private boolean esEscalera() {
		TreeSet<Integer> valoresUnicos = new TreeSet<>();
		for(int i = 0; i < 5; i++) {
			valoresUnicos.add(mano.get(i).get_valor());
		}
		
		if(valoresUnicos.size() < 5) return false;

		int top = valoresUnicos.last();
		int low = valoresUnicos.first();

		if(top - low == 4) {
			mejor_mano = mano;
			return true;
		}
		
		if(valoresUnicos.containsAll(Arrays.asList(14, 2, 3, 4, 5))) {
			mejor_mano = mano;
			return true;
		}
		return false;
	}

	private void detectarDraws() {
		// Flush draw
		int[] contadorPalos = new int[4];
		for(int i = 0; i < mano.size(); i++) {
			contadorPalos[mano.get(i).get_palo()]++;
		}
		for(int c : contadorPalos) {
			if(c == 4) {
				flush_draw = true;
				break;
			}
		}

		TreeSet<Integer> valoresUnicos = new TreeSet<>();
		for(int i = 0; i < mano.size(); i++) {
			valoresUnicos.add(mano.get(i).get_valor());
		}
		
		if(valoresUnicos.contains(14)) {
			valoresUnicos.add(1);
		}

		boolean hayGutshot = false;
		boolean hayOpenEnded = false;

		
		for(int low = 1; low <= 10; low++) {
			int cuenta = 0;
			int posFaltante = -1;
			for(int pos = 0; pos < 5; pos++) {
				if(valoresUnicos.contains(low + pos)) {
					cuenta++;
				} else {
					posFaltante = pos;
				}
			}
			if(cuenta == 4) {
				// TODO
				// Si falta la carta de un extremo (posición 0 o 4), normalmente es un Open Ended
				// porque la escalera de 4 cartas se puede completar por ambos lados.
				// EXCEPCIÓN: Si estamos en los límites con el As (A-2-3-4 o J-Q-K-A), 
				// solo se puede completar por un lado (no da la vuelta), por lo que se considera Gutshot.
				if(posFaltante == 0 || posFaltante == 4) {
					hayOpenEnded = true;
				} else {
					hayGutshot = true;
				}
			}
		}

		this.gutshot = hayGutshot;
		this.open_ended = hayOpenEnded;
	}
	// TODO
	// Si la 1º y la 4º carta son iguales, hay poker
	// Si la 2º y la 5º carta son iguales, hay poker
	private boolean esPoker(){ 
		if(mano.get(0).get_valor() == mano.get(1).get_valor() &&
		   mano.get(0).get_valor() == mano.get(2).get_valor() &&
		   mano.get(0).get_valor() == mano.get(3).get_valor()) {
			for(int i = 0; i <= 3; i++) {
				mejor_mano.add(mano.get(i));
			}
			valoresComparacion = new int[]{mano.get(0).get_valor(), mano.get(4).get_valor()};
			return true;
		}
		if(mano.get(1).get_valor() == mano.get(2).get_valor() &&
		   mano.get(1).get_valor() == mano.get(3).get_valor() &&
		   mano.get(1).get_valor() == mano.get(4).get_valor()) {
			for(int i = 1; i <= 4; i++) {
				mejor_mano.add(mano.get(i));
			}
			valoresComparacion = new int[]{mano.get(1).get_valor(), mano.get(0).get_valor()};
			return true;
		}
		return false;
	}
	// TODO Lo mismo que en poker
	private boolean esFull() {
		if(mano.get(0).get_valor() == mano.get(1).get_valor() &&
		   mano.get(0).get_valor() == mano.get(2).get_valor() &&
		   mano.get(3).get_valor() == mano.get(4).get_valor()) {
			mejor_mano = mano;
			valoresComparacion = new int[]{mano.get(0).get_valor(), mano.get(3).get_valor()};
			return true;
		}
		if(mano.get(0).get_valor() == mano.get(1).get_valor() &&
		   mano.get(2).get_valor() == mano.get(3).get_valor() &&
		   mano.get(2).get_valor() == mano.get(4).get_valor()) {
			mejor_mano = mano;
			valoresComparacion = new int[]{mano.get(2).get_valor(), mano.get(0).get_valor()};
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
				ArrayList<Integer> kickers = new ArrayList<>();
				for(int j = 0; j < 5; j++) {
					if(j < i || j > i+2) {
						kickers.add(mano.get(j).get_valor());
					}
				}
				valoresComparacion = new int[]{mano.get(i).get_valor(), kickers.get(0), kickers.get(1)};
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
						int kicker = -1;
						for(int k = 0; k < 5; k++) {
							if(k != i && k != i+1 && k != j && k != j+1) {
								kicker = mano.get(k).get_valor();
							}
						}
						valoresComparacion = new int[]{mano.get(i).get_valor(), mano.get(j).get_valor(), kicker};
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
				ArrayList<Integer> kickers = new ArrayList<>();
				for(int j = 0; j < 5; j++) {
					if(j != i && j != i+1) {
						kickers.add(mano.get(j).get_valor());
					}
				}
				valoresComparacion = new int[]{mano.get(i).get_valor(), kickers.get(0), kickers.get(1), kickers.get(2)};
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
	/**
	 * Compara esta mano con otra. Devuelve positivo si esta es mejor,
	 * negativo si es peor, 0 si empatan.
	 * Primero compara por categoria (manita), luego por valoresComparacion.
	 */
	@Override
	public int compareTo(Mano otra) {
		int cmp = Integer.compare(this.manita, otra.manita);
		if(cmp != 0) return cmp;
		int len = Math.min(this.valoresComparacion.length, otra.valoresComparacion.length);
		for(int i = 0; i < len; i++) {
			cmp = Integer.compare(this.valoresComparacion[i], otra.valoresComparacion[i]);
			if(cmp != 0) return cmp;
		}
		return 0;
	}

	public int[] getValoresComparacion() {
		return valoresComparacion;
	}

	public String getNombreJugada() {
		String respuestaMano = "";
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
		return respuestaMano;
	}
	
}
