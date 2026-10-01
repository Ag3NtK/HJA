package cartas;

import java.util.ArrayList;
import java.util.List;

public class Jugador implements Comparable<Jugador> {
    private String id;
    private ArrayList<Carta> cartasPropias;
    private Mano mejorMano;

    public Jugador(String id, ArrayList<Carta> cartasPropias) {
        this.id = id;
        this.cartasPropias = cartasPropias;
    }

    public String getId() {
        return id;
    }

    public Mano getMejorMano() {
        return mejorMano;
    }

    @Override
    public int compareTo(Jugador otro) {
        return otro.mejorMano.compareTo(this.mejorMano);
    }

    @Override
    public String toString() {
        return id + ": " + mejorMano.manoString() + " (" + mejorMano.getNombreJugada() + ")";
    }
    
    public void obtenerMejorMano(ArrayList<Carta> cartas) {
    	this.mejorMano = null;
    	cartas.addAll(cartasPropias);
    	for (int i = 0; i < cartas.size() - 4; i++) {
    	    for (int j = i + 1; j < cartas.size() - 3; j++) {
    	        for (int k = j + 1; k < cartas.size() - 2; k++) {
    	            for (int l = k + 1; l < cartas.size() - 1; l++) {
    	                for (int m = l + 1; m < cartas.size(); m++) {

    	                    ArrayList<Carta> combinacion = new ArrayList<>();

    	                    combinacion.add(cartas.get(i));
    	                    combinacion.add(cartas.get(j));
    	                    combinacion.add(cartas.get(k));
    	                    combinacion.add(cartas.get(l));
    	                    combinacion.add(cartas.get(m));
    	                    
    	                    combinacion.sort(Carta.POR_VALOR_DESC);
    	                    Mano mano = new Mano(combinacion);

    	                    if(mejorMano == null || mano.compareTo(mejorMano) > 0) {
    	                    	mejorMano = mano;
    	                    }
    	                }
    	            }
    	        }
    	    }
    	}
    }
    
    public Mano evaluarOmaha(ArrayList<Carta> cartasComunes) {
        Mano mejorMano = null;

        for (int i = 0; i < cartasPropias.size() - 1; i++) {
            for (int j = i + 1; j < cartasPropias.size(); j++) {
                
                ArrayList<Carta> parPropio = new ArrayList<>();
                parPropio.add(cartasPropias.get(i));
                parPropio.add(cartasPropias.get(j));

                for (int x = 0; x < cartasComunes.size() - 2; x++) {
                    for (int y = x + 1; y < cartasComunes.size() - 1; y++) {
                        for (int z = y + 1; z < cartasComunes.size(); z++) {
                            
                            ArrayList<Carta> combo5 = new ArrayList<>(parPropio);
                            combo5.add(cartasComunes.get(x));
                            combo5.add(cartasComunes.get(y));
                            combo5.add(cartasComunes.get(z));
                            combo5.sort(Carta.POR_VALOR_DESC);
                            
                            Mano manoActual = new Mano(combo5);

                            if (mejorMano == null || manoActual.compareTo(mejorMano) > 0) {
                                mejorMano = manoActual;
                            }
                        }
                    }
                }
            }
        }

        return mejorMano;
    }
}