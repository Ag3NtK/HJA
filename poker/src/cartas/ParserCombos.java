package cartas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JTextArea;

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
	
	public static ArrayList<String> parsearDevolverCombos(String texto, JTextArea comentario){
		Rank valor1;
		Rank valor2;
		ArrayList<String> combosTexto = Arrays.stream(texto.split(","))
                .map(String::trim)                 
                .filter(item -> !item.isEmpty())   
                .collect(Collectors.toCollection(ArrayList::new));
		ArrayList<String> listaCombos = new ArrayList<String>();
		
		for(String combo : combosTexto) {
			try {
				//comprueba si el combo es un pareja
				if(combo.length() == 2) {
					valor1 = Rank.fromSymbol(combo.charAt(0));
					valor2 = Rank.fromSymbol(combo.charAt(1));
					if(valor1 == valor2)
						listaCombos.add("" + valor1.getSymbol() + valor1.getSymbol());
					else 
						throw new IllegalArgumentException("Pareja no coincide: " + combo);
					
					
				}else if(combo.length() == 3) {
					//comprueba si una pareja de cartas distintas esta bien construida
					if(combo.charAt(2) == 's' || combo.charAt(2) == 'o') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						
						if(valor1.getValue() <= valor2.getValue()) 
							throw new IllegalArgumentException("Combo mal construido: " + combo);
						if (valor1 == valor2) 
						    throw new IllegalArgumentException("Las parejas no llevan sufijo s/o: " + combo);
						listaCombos.add("" + valor1.getSymbol() + valor2.getSymbol() + combo.charAt(2));
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
					//comprueba si una pareja de cartas distintas esta bien construida con el +
					if((combo.charAt(2) == 's' || combo.charAt(2) == 'o') && combo.charAt(3) == '+') {
						valor1 = Rank.fromSymbol(combo.charAt(0));
						valor2 = Rank.fromSymbol(combo.charAt(1));
						
						if(valor1.getValue() <= valor2.getValue()) 
							throw new IllegalArgumentException("Combo mal construido: " + combo);
						if (valor1 == valor2) 
						    throw new IllegalArgumentException("Las parejas no llevan sufijo s/o: " + combo);
						for(int i = valor2.getValue(); i < valor1.getValue(); i++) {
							Rank rank = Rank.fromValue(i);
					        listaCombos.add("" + valor1.getSymbol() + rank.getSymbol() + combo.charAt(2));
						}
					}else 
						throw new IllegalArgumentException("Combo mal construido: " + combo);
				}else if(combo.length() == 7) {
					//comprueba un rango de parejas
					if((combo.charAt(2) == 's' || combo.charAt(2) == 'o') 
						&& (combo.charAt(6) == 's' || combo.charAt(6) == 'o')
						&& (combo.charAt(2) == combo.charAt(6))
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
					        listaCombos.add("" + valor1.getSymbol() + rank.getSymbol() + combo.charAt(2));
						}
					}else 
						throw new IllegalArgumentException("Combo mal construido: " + combo);
				}else
					throw new IllegalArgumentException("Combo mal construido: " + combo);
			} catch (Exception e) {
				comentario.setText(e.getMessage());
				return null;
			}
		}
		return listaCombos;
	}
}
