package poker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cartas.*;

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Uso: java -jar nombreProyecto.jar <opcion> <entrada> <salida>");
            return;
        }

        int opcion = Integer.parseInt(args[0]);
        String ficheroEntrada = args[1];
        String ficheroSalida = args[2];
        Mano nuestraMano = null;
        
        if(opcion == 1) {
        	try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
        			PrintWriter pw = new PrintWriter(new FileWriter(ficheroSalida))) {
        		String linea;
        		while ((linea = br.readLine()) != null) {
	                ArrayList<Carta> mano = new ArrayList<Carta>();
	                
	                for (int i = 0; i < linea.length(); i += 2) {
	                    char valor = linea.charAt(i);
	                    char palo = linea.charAt(i + 1);
	                    mano.add(new Carta("" + valor + palo));
	                }
	                nuestraMano = obtenerMejorMano(mano);
	                System.out.println(nuestraMano.infoMano());
	                //nuestraMano.infoMano();
	                pw.print(nuestraMano.infoMano());
	                pw.println();
        		}

            } catch (IOException e) {
                System.out.println("Error al leer el fichero: " + e.getMessage());
            }
        }if(opcion == 2) { //TODO todavia sin acabar
        	try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
        			PrintWriter pw = new PrintWriter(new FileWriter(ficheroSalida))) {
        		String linea;
        		while ((linea = br.readLine()) != null) {
	                ArrayList<Carta> cartas = new ArrayList<Carta>();
	                int mesa = linea.charAt(5) - '0';
	               
	                for (int i = 0; i < 4; i += 2) {
	                    char valor = linea.charAt(i);
	                    char palo = linea.charAt(i + 1);
	                    cartas.add(new Carta("" + valor + palo));
	                }
	                for(int i = 7; i < linea.length(); i += 2) {
	                	char valor = linea.charAt(i);
	                    char palo = linea.charAt(i + 1);
	                    cartas.add(new Carta("" + valor + palo));
	                }
	                nuestraMano = obtenerMejorMano(cartas);
	                System.out.println(nuestraMano.infoMano());
                    //nuestraMano.infoMano();
                    pw.print(nuestraMano.infoMano());
                    pw.println();
        		}
                

            } catch (IOException e) {
                System.out.println("Error al leer el fichero: " + e.getMessage());
            }
        }if(opcion == 3) {//TODO hacer
        	try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
        			PrintWriter pw = new PrintWriter(new FileWriter(ficheroSalida))) {
        		String linea;
        		while ((linea = br.readLine()) != null) {
	        	   ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
	               ArrayList<Carta> cartas = new ArrayList<Carta>();
	               int numjugadores = linea.charAt(0) - '0';
	               for(int i = 2; i < (numjugadores*7)+2; i+=7) {
	            	   String nombre = "" + linea.charAt(i) + linea.charAt(i+1);
	            	   for (int j = i+2; j < i+6; j += 2) {
		                    char valor = linea.charAt(j);
		                    char palo = linea.charAt(j + 1);
		                    cartas.add(new Carta("" + valor + palo));
		                }
	            	   Jugador a = new Jugador(nombre, cartas);
	            	   jugadores.add(a);
	            	   cartas = new ArrayList<Carta>();
	               }
	               for (int i = (numjugadores*7)+2; i < linea.length(); i += 2) {
	                   char valor = linea.charAt(i);
	                   char palo = linea.charAt(i + 1);
	                   cartas.add(new Carta("" + valor + palo));
	               }
	               for(int i = 0; i < numjugadores;i++) {
	            	   jugadores.get(i).obtenerMejorMano(new ArrayList<Carta>(cartas));
	               }
	               jugadores.sort(null);
	               for(int i = 0; i < numjugadores;i++) { 
	            	   pw.print(jugadores.get(i).toString());
	                   pw.println();
	                   
	               }
	               pw.println();
        		}
            } catch (IOException e) {
                System.out.println("Error al leer el fichero: " + e.getMessage());
            }
        }
    }
    
    private static Mano obtenerMejorMano(ArrayList<Carta> cartas) {
    	Mano mejorMano = null;
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
		return mejorMano;
    }
}


