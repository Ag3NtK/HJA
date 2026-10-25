package gui;

import javax.swing.*;

import cartas.Baraja;
import cartas.Carta;
import cartas.Jugador;
import cartas.ParserCombos;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;

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
    private JButton botonRecalcular;
    private String stage = "preflop"; 
    
    private boolean repartoManual = false;
    private ArrayList<Carta> cartasComunes = new ArrayList<Carta>();
    private Baraja baraja = new Baraja();
    private JCheckBox modoTexto;
    private JCheckBox calcularExacto;
    private JCheckBox modoPorcentaje;

    private JTextField campoRango;
    private JTextField selectorPorcentaje;
    ArrayList<Jugador> jugadoresMesa;
    private JTextArea rangoTexto;
    private JTextArea rankingTexto;
   

    private static final String[] RANKING_SKLANSKY_CHUBUKOV = {
    	    // === Umbral de 50.0 bb o superior ===
    	    "AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66",
    	    "AKs", "AQs", "AJs", "ATs", "A9s", 
    	    "AKo", "AQo", "AJo", "ATo", "KQo",

    	    // === Umbrales altos (> 10 bb) ===
    	    "55", "A8s", "A7s", "A6s", "A5s", "A4s", "A3s", "A2s",
    	    "KQs", "KJs", "KTs", "A9o", "KJo", "QJs", "44", "A8o",
    	    "K9s", "QTs", "KTo", "A7o", "A6o", "33", "A5o", "QJo",

    	    // === Umbrales medios (5.0 bb a 10.0 bb) ===
    	    "JTs", "K8s", "Q9s", "A4o", "QTo", "A3o", "K9o", "J9s",
    	    "K7s", "A2o", "22", "K8o", "Q8s", "T9s", "JTo", "K6s",
    	    "Q9o", "J8s", "K5s", "K7o", "K4s", "Q7s", "T8s", "J9o",

    	    // === Umbrales bajos (2.5 bb a 5.0 bb) ===
    	    "K6o", "K3s", "Q8o", "J8o", "98s", "K2s", "K5o", "Q6s",
    	    "J7s", "T9o", "Q5s", "K4o", "T7s", "Q7o", "97s", "Q4s",
    	    "J7o", "K3o", "Q3s", "T8o", "J6s", "87s", "Q5o", "Q2s",
    	    "K2o", "J5s", "98o", "96s", "J6o", "Q4o", "T6s", "J4s",

    	    // === Umbrales marginales (1.5 bb a 2.5 bb) ===
    	    "86s", "J5o", "Q3o", "T7o", "76s", "J3s", "97o", "Q2o",
    	    "J2s", "85s", "T5s", "J4o", "95s", "J3o", "87o", "75s",
    	    "T6o", "T4s", "65s", "J2o", "86o", "96o", "76o", "T3s",
    	    "54s", "T5o", "84s", "94s", "T2s", "74s", "T4o", "64s",

    	    // === Umbrales mínimos (< 1.5 bb) ===
    	    "85o", "75o", "T3o", "95o", "65o", "53s", "83s", "T2o",
    	    "54o", "73s", "93s", "84o", "64o", "82s", "63s", "94o",
    	    "74o", "92s", "52s", "43s", "93o", "53o", "72s", "83o",
    	    "62s", "63o", "42s", "92o", "82o", "73o", "52o", "43o",
    	    "32s", "72o", "62o", "42o", "32o"
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
        		    "Jugador " + (i + 1)
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
        botonFlop = new JButton("Siguiente stage");
        botonFlop.setEnabled(false);
        botonRecalcular = new JButton("Calcular Equity");
        botonRecalcular.setEnabled(false);
        

        botonRepartirManual.addActionListener(e -> {repartoManual = true;
        											botonFlop.setEnabled(true);
        											botonRepartirManual.setEnabled(false);
        											botonRepartirRandom.setEnabled(false);
        											repartirCartas();
        											botonRecalcular.setEnabled(true);});
        botonRepartirRandom.addActionListener(e -> {botonFlop.setEnabled(true);
        									  		botonRepartirRandom.setEnabled(false);
        									  		botonRepartirManual.setEnabled(false);
        									  		repartirCartas();
        									  		botonRecalcular.setEnabled(true);});
        botonFlop.addActionListener(e -> {
            switch (stage) {
                case "preflop":
                    stage = "flop";
                    flop();
                    break;
                case "flop":
                    stage = "turn";
                    turn();
                    break;
                case "turn":
                    stage = "river";
                    river();
                    break;
                default:
                    break;
            }
        });
        
       
       botonRecalcular.addActionListener(e -> {
    	   /*double[] resultados = CalculadoraEquity.calcularAuto(
    		        jugadoresMesa,
    		        new ArrayList<>(cartasComunes),
    		        baraja.getCartasRestantes(),
    		        calcularExacto.isSelected()
    		    );
    		
    		    for (int i = 0; i < jugadores.length; i++) {
    		        jugadores[i].setEquity(resultados[i]);
    		    }
    		
    		    mensaje.setText("Equity calculado correctamente");*/
    	   calcularEquity();
    	   
       });
        botonReiniciar.addActionListener(e -> {reiniciar();
        									   botonRepartirRandom.setEnabled(true);
        									   botonRepartirManual.setEnabled(true);
        									   botonFlop.setEnabled(false);
        									   });
        
        botonFlop.setEnabled(false);

        JPanel controles = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 12)
        );
        
        controles.add(botonRepartirManual);
        controles.add(botonRepartirRandom);
        controles.add(botonFlop);
        controles.add(botonReiniciar);
        controles.add(botonRecalcular);
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
        //calcularEquity();
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
        stage="preflop";
        botonRecalcular.setEnabled(false);
        calcularExacto.setSelected(repartoManual);
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
	    gbc.insets = new Insets(7, 8, 5, 8);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 1.0;
	
	    modoTexto = new JCheckBox("Introducir rango textual", true);
	    calcularExacto = new JCheckBox("Calcular Equity exacto para preflop? Default : Estimación", false);
	    modoPorcentaje = new JCheckBox("Seleccionar por porcentaje");
	
	    campoRango = new JTextField();
	
	    selectorPorcentaje = new JTextField(5);
	    
	    rankingTexto = new JTextArea(3, 30);
	    rankingTexto.setEditable(false);
	    rankingTexto.setLineWrap(true);
	    rankingTexto.setWrapStyleWord(true);
	    
	    rangoTexto = new JTextArea(3, 30);
	    rangoTexto.setEditable(false);
	    rangoTexto.setLineWrap(true);
	    rangoTexto.setWrapStyleWord(true);
	    
	    // Fila 1: rango textual.
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    panel.add(modoTexto, gbc);
	    
	    //
	    gbc.gridy = 1;
	    panel.add(campoRango, gbc);
	    //
	    
	    gbc.gridy = 2;

	    JScrollPane scrollRango = new JScrollPane(rangoTexto);
	    scrollRango.setPreferredSize(new Dimension(250, 70));
	    panel.add(scrollRango, gbc);
	
	    // Fila 2: rango por porcentaje.
	    gbc.gridy = 3;
	    panel.add(modoPorcentaje, gbc);
	
	    gbc.gridy = 4;
	    gbc.gridwidth = 1;
	    panel.add(new JLabel("Porcentaje del ranking:"), gbc);
	
	    gbc.gridx = 1;
	    panel.add(selectorPorcentaje, gbc);
	    //
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    gbc.gridwidth = 2;

	    JScrollPane scrollRanking = new JScrollPane(rankingTexto);
	    scrollRanking.setPreferredSize(new Dimension(250, 70));
	    panel.add(scrollRanking, gbc);
	    //
	    gbc.gridx = 0;
	    gbc.gridy = 6;
	    gbc.gridwidth = 2;
	    panel.add(calcularExacto, gbc);
	    
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
	    campoRango.addActionListener(e -> actualizarRango());
	
	    actualizarModoRango();
	    actualizarRanking();
	    actualizarRango();
	
	    return panel;
	}
	
	private void actualizarModoRango() {
	    boolean textoActivo = modoTexto.isSelected();
	
	    campoRango.setEnabled(textoActivo);
	    rangoTexto.setEnabled(textoActivo);
	    selectorPorcentaje.setEnabled(!textoActivo);
	    rankingTexto.setEnabled(!textoActivo);
	}
	
	private void actualizarRango() {
		String texto = campoRango.getText();

	    // Evitar errores si el campo está vacío.
	    if (texto.isEmpty()) {
	    	campoRango.setBackground(Color.WHITE);
	    	rangoTexto.setText("Introduce un rango.");
	        return;
	    }

	    ArrayList<String> rangoDado;
	    
	    rangoDado = ParserCombos.parsearDevolverCombos(texto);
	    
	    if(rangoDado == null) {
	    	campoRango.setBackground(Color.red);
	    	rangoTexto.setText("El rango introducido esta mal ecrito, reviselo.");
	        return;
	    }
	    
	    campoRango.setBackground(Color.WHITE);
	    Jugador hero = jugadores[3].getJugador();
	    
	    if (hero == null || hero.getCombo() == null) {
	    	campoRango.setBackground(Color.WHITE);
	        rangoTexto.setText("Reparte las dos cartas del Hero primero.");
	        return;
	    }
			
		boolean enRango = false;
		
		for(int i = 0; i < rangoDado.size();i++) {
			if(hero.getCombo().equals(rangoDado.get(i))) {
				enRango = true;
				break;
			}
		}
		
		if(enRango) {
			campoRango.setBackground(Color.green);
			rangoTexto.setText("Tus cartas estan dentro del rango.");
		}
		else {
			campoRango.setBackground(Color.red);
			rangoTexto.setText("Tus cartas estan fuera del rango.");
		}
	}
	
	private void actualizarRanking() {
		String texto = selectorPorcentaje.getText().trim();

	    // Evitar errores si el campo está vacío o no contiene un número.
	    if (texto.isEmpty()) {
	        selectorPorcentaje.setBackground(Color.WHITE);
	        rankingTexto.setText("Introduce un porcentaje entre 0 y 100.");
	        return;
	    }

	    int numero;

	    try {
	        numero = Integer.parseInt(texto);
	    } catch (NumberFormatException e) {
	        selectorPorcentaje.setBackground(Color.RED);
	        rankingTexto.setText("El porcentaje debe ser un número entero.");
	        return;
	    }

	    if (numero < 0 || numero > 100) {
	        selectorPorcentaje.setBackground(Color.RED);
	        rankingTexto.setText("El porcentaje debe estar entre 0 y 100.");
	        return;
	    }

	    int cantidadManos = (int) Math.ceil(
	        RANKING_SKLANSKY_CHUBUKOV.length * numero / 100.0
	    );

	    selectorPorcentaje.setBackground(Color.WHITE);
	    Jugador hero = jugadores[3].getJugador();
	    
	    if (hero == null || hero.getCombo() == null) {
	        selectorPorcentaje.setBackground(Color.WHITE);
	        rankingTexto.setText("Reparte las dos cartas del Hero primero.");
	        return;
	    }
			
		boolean enRango = false;
		
		for(int i = 0; i < cantidadManos;i++) {
			if(hero.getCombo().equals(RANKING_SKLANSKY_CHUBUKOV[i])) {
				enRango = true;
				break;
			}
		}
		
		if(enRango) {
			selectorPorcentaje.setBackground(Color.green);
			rankingTexto.setText("Tus cartas estan dentro del rango.");
		}
		else {
			selectorPorcentaje.setBackground(Color.red);
			rankingTexto.setText("Tus cartas estan fuera del rango.");
		}
	    
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
	
		jugadoresMesa = new ArrayList<>();
	
	    for (PlayerPanel panel : jugadores) {
	        jugadoresMesa.add(panel.getJugador());
	    }
	
	    double[] resultados = CalculadoraEquity.calcularAuto(
	        jugadoresMesa,
	        new ArrayList<>(cartasComunes),
	        baraja.getCartasRestantes(),
	        calcularExacto.isSelected()
	    );
	
	    for (int i = 0; i < jugadores.length; i++) {
	        jugadores[i].setEquity(resultados[i]);
	    }
	
	    mensaje.setText("Equity calculado correctamente");
	}

}
