package gui;

import javax.swing.*;

import cartas.Baraja;
import cartas.Carta;
import cartas.Jugador;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class PokerBoardGui extends JFrame {

    private static final String RUTA_CARTAS = "/PNG_cards/";

    private final PlayerPanel[] jugadores = new PlayerPanel[6];
    private final JLabel[] cartasComunitarias = new JLabel[5];
    
    //
    private JButton botonRepartirManual;
    private JButton botonRepartirRandom;
    private JButton botonReiniciar;
    private JButton botonFlop;
    private JButton botonTurn;
    private JButton botonRiver;
    private boolean repartoManual = false;
    private ArrayList<Carta> cartasComunes = new ArrayList<Carta>();
    private Baraja baraja = new Baraja();
    private JCheckBox modoTexto;
    private JCheckBox modoPorcentaje;

    private JTextField campoRango;
    private JComboBox<Integer> selectorPorcentaje;
    private JTextArea rankingTexto;

    private final String[] rangosRanking = {
        "22+, AJs+, KQs, AQo+",
        "22+, A2s+, K9s+, Q9s+, J9s+, T9s, 98s, " +
        "A9o+, KTo+, QTo+, JTo",
        "22+, A2s+, A7o+, K7s+, K9o+, Q8s+, " +
        "QTo+, J8s+, JTo, T8s+, 98s, 87s"
    };
    //

    private final JLabel mensaje = new JLabel(
        "Pulsa Repartir para comenzar",
        SwingConstants.CENTER
    );
    
    public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PokerBoardGui().setVisible(true);
		});
	}

    public PokerBoardGui() {
        setTitle("Poker - 6 jugadores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setMinimumSize(new Dimension(950, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mesa = new JPanel(new GridBagLayout());
        mesa.setBackground(new Color(20, 105, 65));
        mesa.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Fila superior: jugadores 1, 2 y 3.
        JPanel filaSuperior = new JPanel(new GridLayout(1, 3, 15, 0));
        filaSuperior.setOpaque(false);

        for (int i = 0; i < 3; i++) {
        	jugadores[i] = new PlayerPanel(
        		    i + 1,
        		    i == 3 ? "Hero" : "Jugador " + (i + 1)
        		);
            filaSuperior.add(jugadores[i]);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 0.4;
        mesa.add(filaSuperior, gbc);

        // Zona central: las cinco cartas comunitarias.
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(
            new BoxLayout(panelCentro, BoxLayout.Y_AXIS)
        );
        panelCentro.setOpaque(false);

        JLabel tituloCentro = new JLabel(
            "CARTAS COMUNITARIAS",
            SwingConstants.CENTER
        );
        tituloCentro.setForeground(Color.WHITE);
        tituloCentro.setFont(new Font("Arial", Font.BOLD, 20));
        tituloCentro.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(tituloCentro);
        panelCentro.add(Box.createVerticalStrut(12));

        JPanel panelCartas = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 8, 5)
        );
        panelCartas.setOpaque(false);

        for (int i = 0; i < cartasComunitarias.length; i++) {
            cartasComunitarias[i] = crearEspacioCarta();
            panelCartas.add(cartasComunitarias[i]);
        }

        panelCentro.add(panelCartas);

        centro.add(panelCentro);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 0.2;
        mesa.add(centro, gbc);

        // Fila inferior: jugadores 4, 5 y 6.
        JPanel filaInferior = new JPanel(new GridLayout(1, 3, 15, 0));
        filaInferior.setOpaque(false);

        for (int i = 3; i < 6; i++) {
        	jugadores[i] = new PlayerPanel(
        		    i + 1,
        		    i == 3 ? "Hero" : "Jugador " + (i + 1)
        		);
            filaInferior.add(jugadores[i]);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 0.4;
        mesa.add(filaInferior, gbc);

        add(mesa, BorderLayout.CENTER);
        
        // Panel de los rangos
        add(crearPanelRangos(), BorderLayout.EAST);
        setSize(1450, 850);
        setMinimumSize(new Dimension(1200, 750));

        // Controles inferiores.
        botonRepartirManual = new JButton("Repartir Manual");
        botonRepartirRandom = new JButton("Repartir Random");
        botonReiniciar = new JButton("Reiniciar");
        botonFlop = new JButton("Flop");
        botonTurn = new JButton("Turn");
        botonRiver = new JButton("River");

        botonRepartirManual.addActionListener(e -> {repartoManual = true;
        											botonFlop.setEnabled(true);
        											botonRepartirManual.setEnabled(false);
        											botonRepartirRandom.setEnabled(false);
        											repartirCartas();});
        botonRepartirRandom.addActionListener(e -> {botonFlop.setEnabled(true);
        									  		botonRepartirRandom.setEnabled(false);
        									  		botonRepartirManual.setEnabled(false);
        									  		repartirCartas();});
        botonFlop.addActionListener(e -> {botonTurn.setEnabled(true);
        								  botonFlop.setEnabled(false);
        								  flop();});
        botonTurn.addActionListener(e -> {botonRiver.setEnabled(true);
        								  botonTurn.setEnabled(false);
        								  turn();});
        botonRiver.addActionListener(e -> {botonRiver.setEnabled(false);
        								   river();});
        botonReiniciar.addActionListener(e -> {reiniciar();
        									   botonRepartirRandom.setEnabled(true);
        									   botonRepartirManual.setEnabled(true);
        									   botonFlop.setEnabled(false);
        									   botonTurn.setEnabled(false);
        									   botonRiver.setEnabled(false);});
        
        botonFlop.setEnabled(false);
        botonTurn.setEnabled(false);
        botonRiver.setEnabled(false);

        JPanel controles = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 12)
        );
        
        controles.add(botonRepartirManual);
        controles.add(botonRepartirRandom);
        controles.add(botonFlop);
        controles.add(botonTurn);
        controles.add(botonRiver);
        controles.add(botonReiniciar);
        controles.add(mensaje);

        add(controles, BorderLayout.SOUTH);

        reiniciar();
    }

    private JLabel crearEspacioCarta() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(75, 110));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        mostrarImagen(label, "back.png");

        return label;
    }

    private void mostrarImagen(JLabel label, String archivo) {
        URL recurso = getClass().getResource(RUTA_CARTAS + archivo);

        if (recurso == null) {
            label.setIcon(null);
            label.setText("?");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2)
            );
            return;
        }

        ImageIcon original = new ImageIcon(recurso);

        Image imagen = original.getImage().getScaledInstance(
            75, 110, Image.SCALE_SMOOTH
        );

        label.setText("");
        label.setBorder(null);
        label.setIcon(new ImageIcon(imagen));
    }
    
    private void repartirCartas() {
    	if(repartoManual) {
    		for (PlayerPanel jugador : jugadores) {
            	int ronda = 0;
            	while(ronda != 2) {
            		
            		String entrada = JOptionPane.showInputDialog(
                            this,
                            "Introduce la carta " + (ronda + 1)
                                + " del " + jugador.getName()
                                + "\nFormato: Ah, Ks, 5d, Qc...",
                            "Reparto manual",
                            JOptionPane.QUESTION_MESSAGE
                        );
            		
            		if (entrada == null) {
                        mensaje.setText("Reparto manual cancelado");
                        botonFlop.setEnabled(false);
                        return;
                    }

                    entrada = entrada.trim();

                    Carta carta = parsearCarta(entrada);
                    
                    if (carta == null) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Carta no válida. Ejemplos: Ah, Ks, 5d, Qc.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    boolean esta = baraja.eliminar(carta);

                    if (!esta) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Esa carta ya ha sido asignada a otro jugador.",
                            "Carta duplicada",
                            JOptionPane.WARNING_MESSAGE
                        );
                        continue;
                    }
                    jugador.mostrarCarta(ronda, carta);
                    ronda++;
            	}
            }
    	}else {
    		baraja.barajar();
            // Repartir dos cartas a cada uno de los seis jugadores.
            // Se reutiliza una sola baraja para evitar duplicados.
            for (int ronda = 0; ronda < 2; ronda++) {
                for (PlayerPanel jugador : jugadores) {
                    Carta carta = baraja.repartir();
                    jugador.mostrarCarta(ronda, carta);
                }
            }
    	}
    	
    	// El equity queda pendiente de calcular.
        calcularEquity();
        mensaje.setText("Manos repartidas correctamente");
    }

    private void flop() {
    	if(!repartoManual)
	    	for (int i = 0; i < 3; i++) {
	            Carta carta = baraja.repartir();
	            //
	            cartasComunes.add(carta);
	            //
	            mostrarImagen(
	                cartasComunitarias[i],
	                carta.get_carta_PNG()
	            );
	        }
    	else {
    		
        	int ronda = 0;
        	while(ronda != 3) {
        		String entrada = JOptionPane.showInputDialog(
                        this,
                        "Introduce la carta " + (ronda + 1)
                            + " de la mesa "
                            + "\nFormato: Ah, Ks, 5d, Qc...",
                        "Reparto manual",
                        JOptionPane.QUESTION_MESSAGE
                    );
        		
        		if (entrada == null) {
                    mensaje.setText("Reparto manual cancelado");
                    botonTurn.setEnabled(false);
                    return;
                }

                entrada = entrada.trim();

                Carta carta = parsearCarta(entrada);
                
                if (carta == null) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Carta no válida. Ejemplos: Ah, Ks, 5d, Qc.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                boolean esta = baraja.eliminar(carta);

                if (!esta) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Esa carta ya ha sido asignada.",
                        "Carta duplicada",
                        JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }
                cartasComunes.add(carta);
                mostrarImagen(cartasComunitarias[ronda],carta.get_carta_PNG());
                ronda++;
        	}
    	}
    	calcularEquity();
    	mensaje.setText("Flop repartido correctamente");
    }
    
    private void turn() {
    	if(!repartoManual)
	    	for (int i = 3; i < 4; i++) {
	            Carta carta = baraja.repartir();
	            //
	            cartasComunes.add(carta);
	            //
	            mostrarImagen(
	                cartasComunitarias[i],
	                carta.get_carta_PNG()
	            );
	        }
    	else {
    		int ronda = 3;
        	while(ronda != 4) {
        		String entrada = JOptionPane.showInputDialog(
                        this,
                        "Introduce la carta " + (ronda + 1)
                            + " de la mesa"
                            + "\nFormato: Ah, Ks, 5d, Qc...",
                        "Reparto manual",
                        JOptionPane.QUESTION_MESSAGE
                    );
        		
        		if (entrada == null) {
                    mensaje.setText("Reparto manual cancelado");
                    botonRiver.setEnabled(false);
                    return;
                }

                entrada = entrada.trim();

                Carta carta = parsearCarta(entrada);
                
                if (carta == null) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Carta no válida. Ejemplos: Ah, Ks, 5d, Qc.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                boolean esta = baraja.eliminar(carta);

                if (!esta) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Esa carta ya ha sido asignada.",
                        "Carta duplicada",
                        JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }
                
                cartasComunes.add(carta);
                mostrarImagen(cartasComunitarias[ronda],carta.get_carta_PNG());
                ronda++;
        	}
    	}
    	calcularEquity();
    	mensaje.setText("Turn repartido correctamente");
    }
    
    private void river() {
    	if(!repartoManual)
	    	for (int i = 4; i < cartasComunitarias.length; i++) {
	            Carta carta = baraja.repartir();
	            //
	            cartasComunes.add(carta);
	            //
	            mostrarImagen(
	                cartasComunitarias[i],
	                carta.get_carta_PNG()
	            );
	        }
    	else {
    		int ronda = 4;
        	while(ronda != 5) {
        		String entrada = JOptionPane.showInputDialog(
                        this,
                        "Introduce la carta " + (ronda + 1)
                            + " de la mesa"
                            + "\nFormato: Ah, Ks, 5d, Qc...",
                        "Reparto manual",
                        JOptionPane.QUESTION_MESSAGE
                    );
        		
        		if (entrada == null) {
                    mensaje.setText("Reparto manual cancelado");
                    return;
                }

                entrada = entrada.trim();

                Carta carta = parsearCarta(entrada);
                
                if (carta == null) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Carta no válida. Ejemplos: Ah, Ks, 5d, Qc.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                boolean esta = baraja.eliminar(carta);

                if (!esta) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Esa carta ya ha sido asignada.",
                        "Carta duplicada",
                        JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }
                cartasComunes.add(carta);
                mostrarImagen(cartasComunitarias[ronda],carta.get_carta_PNG());
                ronda++;
        	}
    	}
    	calcularEquity();
    	mensaje.setText("River repartido correctamente");
    }

    private void reiniciar() {
    	baraja = new Baraja();
        for (PlayerPanel jugador : jugadores) {
            if (jugador != null) {
                jugador.reiniciar();
            }
        }

        for (JLabel carta : cartasComunitarias) {
            mostrarImagen(carta, "back.png");
        }
        
        cartasComunes = new ArrayList<Carta>();
        repartoManual = false;
        
        mensaje.setText("Pulsa Repartir para comenzar");
    }

    // Panel independiente para cada jugador.
    private class PlayerPanel extends JPanel {

        private final JLabel[] cartas = new JLabel[2];
        private final JLabel equityLabel = new JLabel(
            "Equity: -- %",
            SwingConstants.CENTER
        );
        private Jugador yo;
        private String minombre;

        public PlayerPanel(int numero, String nombreJugador) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            //
            minombre = nombreJugador;
            yo = new Jugador(minombre, new ArrayList<Carta>());
            //
            JLabel nombre = new JLabel(
                minombre,
                SwingConstants.CENTER
            );

            nombre.setFont(new Font("Arial", Font.BOLD, 17));
            nombre.setForeground(Color.WHITE);
            nombre.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(nombre);
            add(Box.createVerticalStrut(8));

            JPanel panelCartas = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 5, 0)
            );
            panelCartas.setOpaque(false);

            for (int i = 0; i < cartas.length; i++) {
                cartas[i] = crearEspacioCarta();
                panelCartas.add(cartas[i]);
            }

            add(panelCartas);
            add(Box.createVerticalStrut(8));

            // Recuadro para el porcentaje de equity.
            JPanel panelEquity = new JPanel(new BorderLayout());
            panelEquity.setBackground(new Color(15, 75, 48));
            panelEquity.setBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2)
            );
            panelEquity.setMaximumSize(new Dimension(180, 45));
            panelEquity.setPreferredSize(new Dimension(160, 45));
            panelEquity.setAlignmentX(Component.CENTER_ALIGNMENT);

            equityLabel.setForeground(Color.WHITE);
            equityLabel.setFont(new Font("Arial", Font.BOLD, 16));

            panelEquity.add(equityLabel, BorderLayout.CENTER);
            add(panelEquity);
        }

        public void mostrarCarta(int posicion, Carta carta) {
            mostrarImagen(
                cartas[posicion],
                carta.get_carta_PNG()
            );
            //
            yo.addCarta(carta);
            //
        }

        public void setEquity(Double porcentaje) {
            if (porcentaje == null) {
                equityLabel.setText("Equity: -- %");
            } else {
                equityLabel.setText(
                    String.format(
                        java.util.Locale.ROOT,
                        "Equity: %.2f%%",
                        porcentaje
                    )
                );
                //
                yo.setEquity(porcentaje);
                //
            }
        }

        public void reiniciar() {
            for (JLabel carta : cartas) {
                mostrarImagen(carta, "back.png");
            }

            setEquity(null);
            //
            yo = new Jugador(minombre, new ArrayList<Carta>());
            //
        }
        
        public Jugador getJugador() {
        	return yo;
        }
    
        public String getName() {
        	return minombre;
        }
    }

	private JPanel crearPanelRangos() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBorder(
	        BorderFactory.createTitledBorder("Rango del Hero")
	    );
	
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 8, 5, 8);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 1.0;
	
	    modoTexto = new JCheckBox("Introducir rango textual", true);
	    modoPorcentaje = new JCheckBox("Seleccionar por porcentaje");
	
	    campoRango = new JTextField(
	        "22+, AJs+, KQs, AKo", 25
	    );
	
	    selectorPorcentaje = new JComboBox<>(
	        new Integer[] {10, 20, 30, 40, 50}
	    );
	
	    rankingTexto = new JTextArea(3, 30);
	    rankingTexto.setEditable(false);
	    rankingTexto.setLineWrap(true);
	    rankingTexto.setWrapStyleWord(true);
	
	    JScrollPane scroll = new JScrollPane(rankingTexto);
	
	    // Fila 1: rango textual.
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    panel.add(modoTexto, gbc);
	
	    gbc.gridy = 1;
	    panel.add(campoRango, gbc);
	
	    // Fila 2: rango por porcentaje.
	    gbc.gridy = 2;
	    panel.add(modoPorcentaje, gbc);
	
	    gbc.gridy = 3;
	    gbc.gridwidth = 1;
	    panel.add(new JLabel("Porcentaje del ranking:"), gbc);
	
	    gbc.gridx = 1;
	    panel.add(selectorPorcentaje, gbc);
	
	    // Fila 3: descripción del ranking seleccionado.
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gbc.gridwidth = 2;
	    panel.add(new JLabel("Rango predefinido:"), gbc);
	
	    gbc.gridy = 5;
	    panel.add(scroll, gbc);
	
	    // Exclusión mutua entre los dos modos.
	    modoTexto.addActionListener(e -> {
	        if (modoTexto.isSelected()) {
	            modoPorcentaje.setSelected(false);
	        } else if (!modoPorcentaje.isSelected()) {
	            modoTexto.setSelected(true);
	        }
	        actualizarModoRango();
	    });
	
	    modoPorcentaje.addActionListener(e -> {
	        if (modoPorcentaje.isSelected()) {
	            modoTexto.setSelected(false);
	        } else if (!modoTexto.isSelected()) {
	            modoPorcentaje.setSelected(true);
	        }
	        actualizarModoRango();
	    });
	
	    selectorPorcentaje.addActionListener(e -> actualizarRanking());
	
	    actualizarModoRango();
	    actualizarRanking();
	
	    return panel;
	}
	
	private void actualizarModoRango() {
	    boolean textoActivo = modoTexto.isSelected();
	
	    campoRango.setEnabled(textoActivo);
	    selectorPorcentaje.setEnabled(!textoActivo);
	    rankingTexto.setEnabled(!textoActivo);
	}
	
	private void actualizarRanking() {
	    int porcentaje = (Integer) selectorPorcentaje.getSelectedItem();
	
	    String rango;
	
	    if (porcentaje == 10) {
	        rango = rangosRanking[0];
	    } else if (porcentaje == 20) {
	        rango = rangosRanking[1];
	    } else if (porcentaje == 30) {
	        rango = rangosRanking[2];
	    } else {
	        rango = "Rango orientativo del " + porcentaje
	            + "% pendiente de definir en el ranking completo.";
	    }
	
	    rankingTexto.setText(
	        "Top " + porcentaje + "%\n" + rango
	    );
	}
	
	private Carta parsearCarta(String entrada) {
	    if (entrada == null) {
	        return null;
	    }

	    entrada = entrada.trim();

	    if (entrada.length() < 2 || entrada.length() > 3) {
	        return null;
	    }

	    String valor = entrada.substring(0, entrada.length() - 1)
	        .toUpperCase();

	    String palo = entrada.substring(entrada.length() - 1)
	        .toLowerCase();

	    java.util.List<String> valores = java.util.Arrays.asList(
	        "2", "3", "4", "5", "6", "7", "8", "9",
	        "T", "J", "Q", "K", "A"
	    );

	    java.util.List<String> palos = java.util.Arrays.asList(
	        "h", "d", "c", "s"
	    );

	    if (!valores.contains(valor) || !palos.contains(palo)) {
	        return null;
	    }

	    return new Carta("" + valor + palo);
	}
	

	private void calcularEquity() {
	
	    ArrayList<Jugador> jugadoresMesa = new ArrayList<>();
	
	    for (PlayerPanel panel : jugadores) {
	        jugadoresMesa.add(panel.getJugador());
	    }
	
	    double[] resultados = CalculadoraEquity.calcular(
	        jugadoresMesa,
	        new ArrayList<>(cartasComunes),
	        baraja.getCartasRestantes()
	    );
	
	    for (int i = 0; i < jugadores.length; i++) {
	        jugadores[i].setEquity(resultados[i]);
	    }
	
	    mensaje.setText("Equity calculado correctamente");
	}

}
