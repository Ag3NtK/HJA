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
	                mano.sort(Carta.POR_VALOR_DESC);
	                Mano nuestraMano = new Mano(mano);
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
	                Mano nuestraMano = null;
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
	                if(mesa == 3) {
	                	cartas.sort(Carta.POR_VALOR_DESC);
	                	nuestraMano = new Mano(cartas);
	                }else {
	                	nuestraMano = obtenerMejorMano(cartas);
	                }
	                System.out.println(nuestraMano.infoMano());
                    //nuestraMano.infoMano();
                    pw.print(nuestraMano.infoMano());
                    pw.println();
        		}
                

            } catch (IOException e) {
                System.out.println("Error al leer el fichero: " + e.getMessage());
            }
        }if(opcion == 3) {//TODO hacer
        	
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


