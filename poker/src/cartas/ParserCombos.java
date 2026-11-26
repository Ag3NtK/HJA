package cartas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import javax.swing.JTextField;

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
	
	public static ArrayList<String> parsearDevolverCombos(String texto, JTextField ranguito){
		Rank valor1;
		Rank valor2;
		ArrayList<String> combosTexto = Arrays.stream(texto.split(","))
                .map(String::trim)                 
                .filter(item -> !item.isEmpty())   
                .collect(Collectors.toCollection(ArrayList::new));
		LinkedHashSet<String> listaCombos = new LinkedHashSet<String>();
		
		for(String combo : combosTexto) {
			try {
				//comprueba si el combo es una pareja o mano sin sufijo s/o
				if(combo.length() == 2) {
					valor1 = Rank.fromSymbol(combo.charAt(0));
					valor2 = Rank.fromSymbol(combo.charAt(1));
					if(valor1 == valor2)
						listaCombos.add("" + valor1.getSymbol() + valor1.getSymbol());
					else if(valor1.getValue() > valor2.getValue()) {
						listaCombos.add("" + valor1.getSymbol() + valor2.getSymbol() + "s");
						listaCombos.add("" + valor1.getSymbol() + valor2.getSymbol() + "o");
					} else 
						throw new IllegalArgumentException("Combo mal construido: " + combo);
					
				}else if(combo.length() == 3) {
					char c2 = Character.toLowerCase(combo.charAt(2));
					//comprueba si una pareja de cartas distintas esta bien construida
					if(c2 == 's' || c2 == 'o') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						
						if(valor1.getValue() <= valor2.getValue()) 
							throw new IllegalArgumentException("Combo mal construido: " + combo);
						if (valor1 == valor2) 
						    throw new IllegalArgumentException("Las parejas no llevan sufijo s/o: " + combo);
						listaCombos.add("" + valor1.getSymbol() + valor2.getSymbol() + c2);
					//añade esta pareja y las superiores
					}else if(combo.charAt(2) == '+') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						if(valor1 == valor2){
							for(int i = valor1.getValue(); i <= 14; i++) {
								Rank rank = Rank.fromValue(i);
						        listaCombos.add("" + rank.getSymbol() + rank.getSymbol());
							}
						}
						else 
							throw new IllegalArgumentException("Pareja no coincide: " + combo);
					}else 
						throw new IllegalArgumentException("Combo mal construido: " + combo);
					
				}else if(combo.length() == 4) {
					char c2 = Character.toLowerCase(combo.charAt(2));
					//comprueba si una pareja de cartas distintas esta bien construida con el +
					if((c2 == 's' || c2 == 'o') && combo.charAt(3) == '+') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						
						if(valor1.getValue() <= valor2.getValue()) 
							throw new IllegalArgumentException("Combo mal construido: " + combo);
						if (valor1 == valor2) 
						    throw new IllegalArgumentException("Las parejas no llevan sufijo s/o: " + combo);
						for(int i = valor2.getValue(); i < valor1.getValue(); i++) {
							Rank rank = Rank.fromValue(i);
					        listaCombos.add("" + valor1.getSymbol() + rank.getSymbol() + c2);
						}
					}else 
						throw new IllegalArgumentException("Combo mal construido: " + combo);
				}else if(combo.length() == 5) {
					//comprueba un rango de parejas 
					if(combo.charAt(2) == '-') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						Rank valor3 = Rank.fromSymbol(combo.charAt(3));
						Rank valor4 = Rank.fromSymbol(combo.charAt(4));
						if(valor1 == valor2 && valor3 == valor4 && valor1.getValue() >= valor3.getValue()) {
							for(int i = valor3.getValue(); i <= valor1.getValue(); i++) {
								Rank rank = Rank.fromValue(i);
								listaCombos.add("" + rank.getSymbol() + rank.getSymbol());
							}
						} else
							throw new IllegalArgumentException("Combo mal construido: " + combo);
					}else
						throw new IllegalArgumentException("Combo mal construido: " + combo);
				}else if(combo.length() == 7) {
					char c2 = Character.toLowerCase(combo.charAt(2));
					char c6 = Character.toLowerCase(combo.charAt(6));
					//comprueba un rango de parejas
					if((c2 == 's' || c2 == 'o') 
						&& (c6 == 's' || c6 == 'o')
						&& (c2 == c6)
						&& combo.charAt(3) == '-') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						Rank valor3 = Rank.fromSymbol(combo.charAt(4));
						Rank valor4 = Rank.fromSymbol(combo.charAt(5));
						if(valor1.getValue() <= valor2.getValue() 
						   || valor3.getValue() <= valor4.getValue()
						   || valor1.getValue() != valor3.getValue()
						   || valor2.getValue() <= valor4.getValue()) 
							throw new IllegalArgumentException("Combo mal construido: " + combo);
						if (valor1 == valor2 || valor3 == valor4) 
						    throw new IllegalArgumentException("No se pueden poner parejs en este tipo de combos: " + combo);
						for(int i = valor4.getValue(); i <= valor2.getValue(); i++) {
							Rank rank = Rank.fromValue(i);
					        listaCombos.add("" + valor1.getSymbol() + rank.getSymbol() + c2);
						}
					}else 
						throw new IllegalArgumentException("Combo mal construido: " + combo);
				}else
					throw new IllegalArgumentException("Combo mal construido: " + combo);
			} catch (Exception e) {
				if (ranguito != null) {
					ranguito.setText(e.getMessage());
				}
				
				return null;
			}
		}
		return new ArrayList<>(listaCombos);
	}
	public static ArrayList<ArrayList<Carta>> generarManosDeCombo(String comboNotacion) {
		ArrayList<ArrayList<Carta>> resultado = new ArrayList<>();
		if (comboNotacion == null || comboNotacion.length() < 2) {
			return resultado;
		}

		String[] palos = {"h", "d", "c", "s"};
		char c1 = comboNotacion.charAt(0);
		char c2 = comboNotacion.charAt(1);

		if (comboNotacion.length() == 2 && c1 == c2) {
			// Pareja: 6 combos
			for (int i = 0; i < palos.length - 1; i++) {
				for (int j = i + 1; j < palos.length; j++) {
					ArrayList<Carta> par = new ArrayList<>();
					par.add(new Carta("" + c1 + palos[i]));
					par.add(new Carta("" + c2 + palos[j]));
					resultado.add(par);
				}
			}
		} else if (comboNotacion.length() == 3) {
			char tipo = comboNotacion.charAt(2);
			if (tipo == 's') {
				// Suited 4 combos
				for (String p : palos) {
					ArrayList<Carta> par = new ArrayList<>();
					par.add(new Carta("" + c1 + p));
					par.add(new Carta("" + c2 + p));
					resultado.add(par);
				}
			} else if (tipo == 'o') {
				// Offsuit 12 combos 
				for (String p1 : palos) {
					for (String p2 : palos) {
						if (!p1.equals(p2)) {
							ArrayList<Carta> par = new ArrayList<>();
							par.add(new Carta("" + c1 + p1));
							par.add(new Carta("" + c2 + p2));
							resultado.add(par);
						}
					}
				}
			}
		}

		return resultado;
	}
}

