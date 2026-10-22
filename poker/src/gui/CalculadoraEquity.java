package gui;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.concurrent.*;

import cartas.Carta;
import cartas.Jugador;
import cartas.Mano;

public class CalculadoraEquity {

    
    public static double[] calcularAuto(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> comunitarias,
            ArrayList<Carta> restantes,
            boolean exacto) {
        
        int faltan = 5 - comunitarias.size();

        if (exacto||faltan <= 2) {
            
        	return calcular(jugadores, comunitarias, restantes);
        } else {

        	return calcularMonteCarlo(jugadores, comunitarias, restantes, 10000);
        }
    }


    public static double[] calcular(
            ArrayList<Jugador> jugadores,
            ArrayList<Carta> comunitarias,
            ArrayList<Carta> restantes) {

        final int n = jugadores.size();
        final int faltan = 5 - comunitarias.size();
        final int HILOS = 16;

        if (faltan < 0 || restantes.size() < faltan) {
            throw new IllegalArgumentException(
                "No hay suficientes cartas para completar la mesa."
            );
        }

        double[] equity = new double[n];
        int[] total = {0};

        if (faltan == 0) {
            // Mesa completa: un solo tablero, sin hilos
            generarTableros(jugadores, restantes, new ArrayList<>(comunitarias),
                            0, 0, equity, total);
        } else {
            final int nRamas = restantes.size() - faltan + 1;
            final double[][] parcialEquity = new double[HILOS][n];
            final int[] parcialTotal = new int[HILOS];
            Thread[] hilos = new Thread[HILOS];

            for (int t = 0; t < HILOS; t++) {
                final int idx = t;
                hilos[t] = new Thread(() -> {
                    int[] miTotal = {0};

                    // Este hilo hace las ramas idx, idx+5, idx+10...
                    for (int rama = idx; rama < nRamas; rama += HILOS) {
                        ArrayList<Carta> tablero = new ArrayList<>(comunitarias);
                        tablero.add(restantes.get(rama));

                        generarTableros(jugadores, restantes, tablero,
                                        faltan - 1, rama + 1,
                                        parcialEquity[idx], miTotal);
                    }
                    parcialTotal[idx] = miTotal[0];
                });
                hilos[t].start();
            }

            // Esperar a los 5 hilos
            for (Thread h : hilos) {
                try {
                    h.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Cálculo interrumpido", e);
                }
            }

            // Sumar lo que calculó cada hilo
            for (int t = 0; t < HILOS; t++) {
                for (int i = 0; i < n; i++) {
                    equity[i] += parcialEquity[t][i];
                }
                total[0] += parcialTotal[t];
            }
        }

        if (total[0] > 0) {
            for (int i = 0; i < n; i++) {
                equity[i] = equity[i] * 100.0 / total[0];
            }
        }
        return equity;
    }
     
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

            manos[i] = jugador.obtenerMejorMano(new ArrayList<>(tablero));
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