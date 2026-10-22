package gui;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import cartas.Carta;
import cartas.Jugador;
import cartas.Mano;

public class CalculadoraEquity {

    /**
     * Método automático que selecciona la estrategia óptima:
     * - Enumeración exacta si faltan <= 2 cartas (Turn y River).
     * - Monte Carlo si faltan > 2 cartas (Pre-flop y Flop).
     */
    public static double[] calcularAuto(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> comunitarias,
            ArrayList<Carta> restantes,
            boolean exacto) {
        
        int faltan = 5 - comunitarias.size();

        if (exacto||faltan <= 2) {
            // Rápido e idóneo para Turn y River (Enumeración exacta)
            return calcular(jugadores, comunitarias, restantes);
        } else {
            // Ultra rápido para Pre-flop y Flop (Monte Carlo con 10.000 iteraciones)
            return calcularMonteCarlo(jugadores, comunitarias, restantes, 10000);
        }
    }

    /**
     * Cálculo mediante Enumeración Exacta (Backtracking).
     */
    public static double[] calcular(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> comunitarias,
            ArrayList<Carta> restantes) {

        double[] equity = new double[jugadores.size()];
        int faltan = 5 - comunitarias.size();

        if (faltan < 0 || restantes.size() < faltan) {
            throw new IllegalArgumentException(
                "No hay suficientes cartas para completar la mesa."
            );
        }

        // Acumulador del número de tableros evaluados.
        int[] total = {0};

        // Copiamos las cartas comunitarias conocidas.
        ArrayList<Carta> tablero = new ArrayList<>(comunitarias);

        generarTableros(
            jugadores,
            restantes,
            tablero,
            faltan,
            0,
            equity,
            total
        );

        // Convertimos las proporciones en porcentajes.
        if (total[0] > 0) {
            for (int i = 0; i < equity.length; i++) {
                equity[i] = equity[i] * 100.0 / total[0];
            }
        }

        return equity;
    }

    /**
     * Cálculo mediante Simulación Monte Carlo.
     */
    public static double[] calcularMonteCarlo(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> comunitarias,
            ArrayList<Carta> restantes,
            int simulaciones) {

        double[] equity = new double[jugadores.size()];
        int faltan = 5 - comunitarias.size();

        if (faltan < 0 || restantes.size() < faltan) {
            throw new IllegalArgumentException(
                "No hay suficientes cartas para completar la mesa."
            );
        }

        if (faltan == 0) {
            evaluarTablero(jugadores, comunitarias, equity);
            for (int i = 0; i < equity.length; i++) {
                equity[i] *= 100.0;
            }
            return equity;
        }

        ArrayList<Carta> mazoSimulacion = new ArrayList<>(restantes);
        int nRestantes = mazoSimulacion.size();

        for (int sim = 0; sim < simulaciones; sim++) {
            ArrayList<Carta> tablero = new ArrayList<>(comunitarias);

            // Selecciona 'faltan' cartas aleatorias de 'restantes' sin repetirse
            for (int i = 0; i < faltan; i++) {
                int randIdx = ThreadLocalRandom.current().nextInt(i, nRestantes);

                Carta seleccionada = mazoSimulacion.get(randIdx);
                mazoSimulacion.set(randIdx, mazoSimulacion.get(i));
                mazoSimulacion.set(i, seleccionada);

                tablero.add(seleccionada);
            }

            evaluarTablero(jugadores, tablero, equity);
        }

        // Convertimos a porcentaje
        for (int i = 0; i < equity.length; i++) {
            equity[i] = (equity[i] / simulaciones) * 100.0;
        }

        return equity;
    }

    private static void generarTableros(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> restantes,
            ArrayList<Carta> tablero,
            int faltan,
            int inicio,
            double[] equity,
            int[] total) {

        // Ya tenemos las cinco cartas comunitarias.
        if (faltan == 0) {
            evaluarTablero(jugadores, tablero, equity);
            total[0]++;
            return;
        }

        // Elegimos las cartas que faltan sin repetir.
        for (int i = inicio;
             i <= restantes.size() - faltan;
             i++) {

            tablero.add(restantes.get(i));

            generarTableros(
                jugadores,
                restantes,
                tablero,
                faltan - 1,
                i + 1,
                equity,
                total
            );

            tablero.remove(tablero.size() - 1);
        }
    }

    private static void evaluarTablero(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> tablero,
            double[] equity) {

        Mano[] manos = new Mano[jugadores.size()];
        Mano mejor = null;

        // Calculamos la mejor mano de cada jugador.
        for (int i = 0; i < jugadores.size(); i++) {

            Jugador jugador = jugadores.get(i);

            jugador.obtenerMejorMano(
                new ArrayList<>(tablero)
            );

            manos[i] = jugador.getMejorMano();

            if (mejor == null ||
                manos[i].compareTo(mejor) > 0) {

                mejor = manos[i];
            }
        }

        // Contamos cuántos jugadores comparten la mejor mano.
        int ganadores = 0;

        for (Mano mano : manos) {
            if (mano.compareTo(mejor) == 0) {
                ganadores++;
            }
        }

        // Repartimos el equity entre los ganadores.
        for (int i = 0; i < manos.length; i++) {
            if (manos[i].compareTo(mejor) == 0) {
                equity[i] += 1.0 / ganadores;
            }
        }
    }
}