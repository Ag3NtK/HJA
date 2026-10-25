package cartas;

import java.util.ArrayList;

public class ParserCombos {
	
	public static String obtenerCombos2Cartas(ArrayList<Carta> cartas) {
		if(cartas.size()!= 2)
			return null;
		else {
			Carta a = cartas.get(0);
			Carta b = cartas.get(1);
			
			if(a.get_valor() == b.get_valor()) {
				return "" + a.get_valor_symbol() + b.get_valor_symbol();
			}else if(a.get_palo() == b.get_palo()){
				if(a.get_valor() > b.get_valor())
					return "" + a.get_valor_symbol() + b.get_valor_symbol() + "s";
				else
					return "" + b.get_valor_symbol() + a.get_valor_symbol() + "s";
			}else {
				if(a.get_valor() > b.get_valor())
					return "" + a.get_valor_symbol() + b.get_valor_symbol() + "o";
				else
					return "" + b.get_valor_symbol() + a.get_valor_symbol() + "o";
			}
		}
	}
	
	public static ArrayList<String> parsearDevolverCombos(String texto){
		return null;
		
	}
}
