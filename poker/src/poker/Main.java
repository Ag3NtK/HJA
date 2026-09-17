package poker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        //TODO todavia sin acabar
        if(opcion == 1) {
        	try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada))) {

                String linea = br.readLine();
                ArrayList<Carta> mano = new ArrayList<Carta>();
                
                for (int i = 0; i < linea.length(); i += 2) {
                    char valor = linea.charAt(i);
                    char palo = linea.charAt(i + 1);
                    mano.add(new Carta("" + valor + palo));
                }
                mano.sort(Carta.POR_VALOR_DESC);
                Mano nuestraMano = new Mano(mano);
                System.out.println(nuestraMano.infoMano());
                nuestraMano.infoMano();

            } catch (IOException e) {
                System.out.println("Error al leer el fichero: " + e.getMessage());
            }
        }
    }
}
