package cartas;

import java.util.ArrayList;
import java.util.Collections;


public class Baraja {
    private ArrayList<Carta> cartas;
    private int indiceActual;

    public Baraja() {
        cartas = new ArrayList<>();
        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] palos = {"h", "d", "c", "s"};
        for (String v : valores) {
            for (String p : palos) {
                cartas.add(new Carta(v + p));
            }
        }
        indiceActual = 0;
    }

    
    public void barajar() {
        Collections.shuffle(cartas);
        indiceActual = 0;
    }

    
    public Carta repartir() {
        if (indiceActual >= cartas.size()) {
            throw new IllegalStateException("No quedan cartas en la baraja");
        }
        return cartas.get(indiceActual++);
    }

    
    public boolean eliminar(Carta carta) {
        for (int i = indiceActual; i < cartas.size(); i++) {
            if (cartas.get(i).get_valor() == carta.get_valor() &&
                cartas.get(i).get_palo() == carta.get_palo()) {
                cartas.remove(i);
                return true;
            }
        }
        return false;
    }

    
    public void reiniciar() {
        cartas.clear();
        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] palos = {"h", "d", "c", "s"};
        for (String v : valores) {
            for (String p : palos) {
                cartas.add(new Carta(v + p));
            }
        }
        indiceActual = 0;
    }

    
    public static Baraja crearSinCartas(ArrayList<Carta> excluidas) {
        Baraja b = new Baraja();
        for (Carta excl : excluidas) {
            b.eliminar(excl);
        }
        return b;
    }

    public int cartasRestantes() {
        return cartas.size() - indiceActual;
    }

    public ArrayList<Carta> getCartasRestantes() {
        return new ArrayList<>(cartas.subList(indiceActual, cartas.size()));
    }
}
