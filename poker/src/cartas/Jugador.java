package cartas;

import java.util.ArrayList;
import java.util.List;

public class Jugador implements Comparable<Jugador> {
    private String id;                       // Ej: "J1", "J2"
    private ArrayList<Carta> cartasPropias;  // Sus 2 cartas privadas
    private Mano mejorMano;                  // La mejor combinación de 5 cartas encontrada

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

    /**
     * Evalúa las 2 cartas del jugador junto a las 5 comunes del tablero (7 cartas en total)
     * y selecciona la mejor combinación de 5 cartas posible.
     *
    public void calcularMejorMano(ArrayList<Carta> cartasComunes) {
        ArrayList<Carta> todas = new ArrayList<>(cartasPropias);
        todas.addAll(cartasComunes); // Total: 7 cartas

        List<ArrayList<Carta>> combinaciones = generarCombinaciones7en5(todas);
        Mano mejor = null;

        for (ArrayList<Carta> combo5 : combinaciones) {
            Mano m = new Mano(combo5);
            if (mejor == null || m.compareTo(mejor) > 0) {
                mejor = m;
            }
        }
        this.mejorMano = mejor;
    }
	*/
    @Override
    public int compareTo(Jugador otro) {
        // Orden descendente (de la mejor mano a la peor mano)
        return otro.mejorMano.compareTo(this.mejorMano);
    }

    @Override
    public String toString() {
        // Formato exacto de salida requerido en el apartado 2.3:
        // Ej: "J3: 6c7c8c9hTh (Straight)"
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

        // 1. Obtener todas las combinaciones de 2 cartas de las 4 propias (6 combinaciones)
        for (int i = 0; i < cartasPropias.size() - 1; i++) {
            for (int j = i + 1; j < cartasPropias.size(); j++) {
                
                ArrayList<Carta> parPropio = new ArrayList<>();
                parPropio.add(cartasPropias.get(i));
                parPropio.add(cartasPropias.get(j));

                // 2. Obtener todas las combinaciones de 3 cartas del tablero
                for (int x = 0; x < cartasComunes.size() - 2; x++) {
                    for (int y = x + 1; y < cartasComunes.size() - 1; y++) {
                        for (int z = y + 1; z < cartasComunes.size(); z++) {
                            
                            // Formar la mano de 5 cartas (2 propias + 3 comunes)
                            ArrayList<Carta> combo5 = new ArrayList<>(parPropio);
                            combo5.add(cartasComunes.get(x));
                            combo5.add(cartasComunes.get(y));
                            combo5.add(cartasComunes.get(z));

                            // Evaluar la mano de 5 cartas
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