package gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cartas.Baraja;
import cartas.Carta;
import cartas.Jugador;
import cartas.Mano;
import cartas.ParserCombos;

public class CalculadoraOutsTurn {

    public static class ComboStats {
        public String tipoCombo; 
        public int combosValidos = 0;
        public double totalOuts = 0.0;
        public double outsPorCombo = 0.0; 
    }
    public static class ResultadoTurn {
        public double mediaOuts;
        public int totalCombosValidos;
        public boolean esCall;
        public String desgloseTexto;
        public Map<String, ComboStats> statsPorTipo = new LinkedHashMap<>();
        public String mensajeError = null;
    }
    public static ResultadoTurn calcular(
            ArrayList<Carta> heroCartas,
            ArrayList<Carta> boardCartas,
            String rangoTexto,
            double emPorcentaje) {

        ResultadoTurn res = new ResultadoTurn();
        // Obtener lista de tipos de combos del rango 
        ArrayList<String> listaTiposCombo = ParserCombos.parsearDevolverCombos(rangoTexto, null);
        if (listaTiposCombo == null || listaTiposCombo.isEmpty()) {
            res.mensajeError = "No se pudieron obtener combos válidos del rango.";
            return res;
        }
        ArrayList<Carta> cartasMuertasBase = new ArrayList<>();
        cartasMuertasBase.addAll(heroCartas);
        cartasMuertasBase.addAll(boardCartas);

        double sumaTotalOuts = 0.0;
        int totalCombosValidos = 0;

        StringBuilder sbDesglose = new StringBuilder();
        for (String tipo : listaTiposCombo) {
            ArrayList<ArrayList<Carta>> combosDelTipo = ParserCombos.generarManosDeCombo(tipo);
            if (combosDelTipo == null || combosDelTipo.isEmpty()) {
                continue;
            }

            ComboStats stats = new ComboStats();
            stats.tipoCombo = tipo;

            for (ArrayList<Carta> parVillano : combosDelTipo) {
                Carta v1 = parVillano.get(0);
                Carta v2 = parVillano.get(1);

                if (contieneCarta(cartasMuertasBase, v1) || contieneCarta(cartasMuertasBase, v2)) {
                    continue; // Combo bloqueado por Hero o Board
                }

                // Combo válido para el villano
                stats.combosValidos++;
                totalCombosValidos++;

                double outsEsteCombo = 0.0;
                Baraja barajaRiver = new Baraja();
                
                for (Carta c : cartasMuertasBase) {
                    barajaRiver.eliminar(c);
                }
                barajaRiver.eliminar(v1); 
                barajaRiver.eliminar(v2);

                ArrayList<Carta> cartasRestantesRiver = barajaRiver.getCartasRestantes();

                Jugador jHero = new Jugador("Hero", new ArrayList<>(heroCartas));
                Jugador jVillano = new Jugador("Villano", new ArrayList<>(parVillano));

                // Evaluar las 44 cartas restantes en el River
                for (Carta riverCard : cartasRestantesRiver) {
                    ArrayList<Carta> boardCompleto = new ArrayList<>(boardCartas);
                    boardCompleto.add(riverCard);

                    jHero.obtenerMejorMano(new ArrayList<>(boardCompleto));
                    jVillano.obtenerMejorMano(new ArrayList<>(boardCompleto));

                    Mano manoHero = jHero.getMejorMano();
                    Mano manoVillano = jVillano.getMejorMano();

                    int cmp = manoHero.compareTo(manoVillano);
                    if (cmp > 0) {
                        outsEsteCombo += 1.0; // Hero gana
                    } else if (cmp == 0) {
                        outsEsteCombo += 0.5; // Empate equitativo
                    }
                }

                stats.totalOuts += outsEsteCombo;
                sumaTotalOuts += outsEsteCombo;
            }

            if (stats.combosValidos > 0) {
                stats.outsPorCombo = stats.totalOuts / stats.combosValidos;
                res.statsPorTipo.put(tipo, stats);

                // Formatear línea 
                if (stats.combosValidos == 1) {
                    sbDesglose.append(String.format(java.util.Locale.ROOT, "%s: %.0f outs\n", tipo, stats.totalOuts));
                } else {
                    sbDesglose.append(String.format(java.util.Locale.ROOT, "%s: %.0f*%d\n", 
                            tipo, stats.outsPorCombo, stats.combosValidos));
                }
            }
        }

        if (totalCombosValidos == 0) {
            res.mensajeError = "Todas las combinaciones del rango del villano están bloqueadas por las cartas de Hero y del Board.";
            return res;
        }

        res.totalCombosValidos = totalCombosValidos;
        res.mediaOuts = sumaTotalOuts / totalCombosValidos;
        sbDesglose.append(String.format(java.util.Locale.ROOT, "Media = %.2f outs", res.mediaOuts));
        res.desgloseTexto = sbDesglose.toString();

        //  CALL si mediaOuts >= emPorcentaje, FOLD en caso contrario
        res.esCall = (res.mediaOuts >= emPorcentaje);

        return res;
    }

    // Funcion auxiliar para comprobar cartas muertas
    private static boolean contieneCarta(List<Carta> lista, Carta c) {
        for (Carta x : lista) {
            if (x.get_valor() == c.get_valor() && x.get_palo() == c.get_palo()) {
                return true;
            }
        }
        return false;
    }
}
