package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import cartas.Baraja;
import cartas.Carta;
import gui.CalculadoraOutsTurn.ResultadoTurn;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.ArrayList;

public class PokerTurnDecisionGui extends JFrame {

    private static final String RUTA_CARTAS = "/PNG_cards/";

    private final JLabel[] cartasHero = new JLabel[2];
    private final JLabel[] cartasBoard = new JLabel[4];
    private final JLabel[] cartasVillano = new JLabel[2];

    private JTextField emHero;
    private JTextField rangoVillano;
    private JTextArea areaDesglose;

    private JButton botonRepartirRandom;
    private JButton botonElegirHero;
    private JButton botonElegirBoard;
    private JButton botonCalcularOdds;
    private JButton botonReiniciar;

    private ArrayList<Carta> listaHero = new ArrayList<>();
    private ArrayList<Carta> listaBoard = new ArrayList<>();
    private Baraja baraja = new Baraja();

    private final JLabel mensaje = new JLabel(
        "Rellena manualmente los huecos o usa los botones de reparto",
        SwingConstants.CENTER
    );

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PokerTurnDecisionGui().setVisible(true);
        });
    }

    public PokerTurnDecisionGui() {
        setTitle("Poker - Decisiones en el Turn: Mano vs Rango");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1250, 780);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mesa = new JPanel(new GridBagLayout());
        mesa.setBackground(new Color(20, 105, 65));
        mesa.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        //PANEL HERO 
        JPanel panelHero = new JPanel();
        panelHero.setLayout(new BoxLayout(panelHero, BoxLayout.Y_AXIS));
        panelHero.setOpaque(false);
        panelHero.setPreferredSize(new Dimension(280, 480));

        JLabel nombreHero = new JLabel("Hero", SwingConstants.CENTER);
        nombreHero.setFont(new Font("Arial", Font.BOLD, 17));
        nombreHero.setForeground(Color.WHITE);
        nombreHero.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelHero.add(nombreHero);
        panelHero.add(Box.createVerticalStrut(10));

        JPanel panelCartasHero = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelCartasHero.setOpaque(false);
        for (int i = 0; i < 2; i++) {
            cartasHero[i] = crearEspacioCarta();
            panelCartasHero.add(cartasHero[i]);
        }
        panelHero.add(panelCartasHero);
        panelHero.add(Box.createVerticalStrut(15));

        // Cuadro de texto para EM
        JPanel panelEm = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelEm.setOpaque(false);
        JLabel labelEm = new JLabel("EM:");
        labelEm.setForeground(Color.WHITE);
        labelEm.setFont(new Font("Arial", Font.PLAIN, 12));

        emHero = new JTextField("Introduce un Equity");
        emHero.setBackground(new Color(15, 75, 48));
        emHero.setPreferredSize(new Dimension(130, 35));
        emHero.setForeground(Color.WHITE);
        emHero.setFont(new Font("Arial", Font.BOLD, 13));
        emHero.setHorizontalAlignment(JTextField.CENTER);
        emHero.setBorder(new LineBorder(new Color(245, 130, 32), 2)); 

        emHero.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent fe) {
                if (emHero.getText().trim().isEmpty()) {
                    emHero.setText("Introduce un Equity");
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                if (emHero.getText().equals("Introduce un Equity")) {
                    emHero.setText("");
                }
                emHero.setBackground(new Color(15, 75, 48));
            }
        });

        panelEm.add(labelEm);
        panelEm.add(emHero);
        panelHero.add(panelEm);
        panelHero.add(Box.createVerticalStrut(15));

        // Recuadro blanco con desglose de outs
        areaDesglose = new JTextArea(4, 16);
        areaDesglose.setEditable(false);
        areaDesglose.setFont(new Font("Monospaced", Font.BOLD, 13));
        areaDesglose.setBackground(Color.WHITE);
        areaDesglose.setForeground(Color.BLACK);
        areaDesglose.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        areaDesglose.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelHero.add(areaDesglose);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.32;
        mesa.add(panelHero, gbc);

        //CARTAS COMUNITARIAS DEL TURN 
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);

        JLabel tituloCentro = new JLabel("CARTAS COMUNITARIAS (TURN)", SwingConstants.CENTER);
        tituloCentro.setForeground(Color.WHITE);
        tituloCentro.setFont(new Font("Arial", Font.BOLD, 20));
        tituloCentro.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(tituloCentro);
        panelCentro.add(Box.createVerticalStrut(15));

        JPanel panelCartasBoard = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelCartasBoard.setOpaque(false);

        for (int i = 0; i < 4; i++) {
            cartasBoard[i] = crearEspacioCarta();
            panelCartasBoard.add(cartasBoard[i]);
        }

        panelCentro.add(panelCartasBoard);
        centro.add(panelCentro);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.36;
        mesa.add(centro, gbc);

        //PANEL VILLANO
        JPanel panelVillano = new JPanel();
        panelVillano.setLayout(new BoxLayout(panelVillano, BoxLayout.Y_AXIS));
        panelVillano.setOpaque(false);
        panelVillano.setPreferredSize(new Dimension(280, 480));

        JLabel nombreVillano = new JLabel("Villano", SwingConstants.CENTER);
        nombreVillano.setFont(new Font("Arial", Font.BOLD, 17));
        nombreVillano.setForeground(Color.WHITE);
        nombreVillano.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelVillano.add(nombreVillano);
        panelVillano.add(Box.createVerticalStrut(10));

        // Cartas del villano 
        JPanel panelCartasVillano = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelCartasVillano.setOpaque(false);
        for (int i = 0; i < 2; i++) {
            cartasVillano[i] = crearEspacioCarta();
            mostrarImagen(cartasVillano[i], "back.png");
            panelCartasVillano.add(cartasVillano[i]);
        }
        panelVillano.add(panelCartasVillano);
        panelVillano.add(Box.createVerticalStrut(15));

        // Cuadro de texto para Rango
        JPanel panelRango = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelRango.setOpaque(false);
        JLabel labelRango = new JLabel("Rango:");
        labelRango.setForeground(Color.WHITE);
        labelRango.setFont(new Font("Arial", Font.PLAIN, 12));

        rangoVillano = new JTextField("Introduce un rango");
        rangoVillano.setBackground(new Color(15, 75, 48));
        rangoVillano.setPreferredSize(new Dimension(140, 35));
        rangoVillano.setForeground(Color.WHITE);
        rangoVillano.setFont(new Font("Arial", Font.PLAIN, 12));
        rangoVillano.setHorizontalAlignment(JTextField.CENTER);
        rangoVillano.setBorder(new LineBorder(new Color(138, 43, 226), 2)); // Borde morado

        rangoVillano.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent fe) {
                if (rangoVillano.getText().trim().isEmpty()) {
                    rangoVillano.setText("Introduce un rango");
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                if (rangoVillano.getText().equals("Introduce un rango")) {
                    rangoVillano.setText("");
                }
                rangoVillano.setBackground(new Color(15, 75, 48));
            }
        });

        panelRango.add(labelRango);
        panelRango.add(rangoVillano);
        panelVillano.add(panelRango);
        panelVillano.add(Box.createVerticalStrut(15));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.32;
        mesa.add(panelVillano, gbc);

        add(mesa, BorderLayout.CENTER);

        //CONTROLES INFERIORES 
        botonRepartirRandom = new JButton("Repartir Random");
        botonElegirHero = new JButton("Elegir Hero (2)");
        botonElegirBoard = new JButton("Elegir Board (4)");
        botonCalcularOdds = new JButton("Calcular odds");

        botonReiniciar = new JButton("Reiniciar");

        botonRepartirRandom.addActionListener(e -> repartirRandomCompleto());
        botonElegirHero.addActionListener(e -> elegirCartasHero());
        botonElegirBoard.addActionListener(e -> elegirCartasBoard());
        botonCalcularOdds.addActionListener(e -> calcularOdds());
        botonReiniciar.addActionListener(e -> reiniciar());

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        controles.add(botonRepartirRandom);
        controles.add(botonElegirHero);
        controles.add(botonElegirBoard);
        controles.add(botonCalcularOdds);
        controles.add(botonReiniciar);
        controles.add(mensaje);

        add(controles, BorderLayout.SOUTH);

        // La mesa empieza vacía
        reiniciar();
    }

    private void desactivarBotonesReparto() {
        botonRepartirRandom.setEnabled(false);
        botonElegirHero.setEnabled(false);
        botonElegirBoard.setEnabled(false);
    }

    private void repartirRandomCompleto() {
        baraja = new Baraja();
        baraja.barajar();
        listaHero.clear();
        listaBoard.clear();

        Carta c1 = baraja.repartir();
        Carta c2 = baraja.repartir();
        listaHero.add(c1);
        listaHero.add(c2);
        mostrarImagen(cartasHero[0], c1.get_carta_PNG());
        mostrarImagen(cartasHero[1], c2.get_carta_PNG());

        for (int i = 0; i < 4; i++) {
            Carta cb = baraja.repartir();
            listaBoard.add(cb);
            mostrarImagen(cartasBoard[i], cb.get_carta_PNG());
        }

        desactivarBotonesReparto();
        mensaje.setText("Cartas repartidas aleatoriamente.");
    }

    private void elegirCartasHero() {
        Baraja barajaTemp = new Baraja();
        for (Carta c : listaBoard) {
            barajaTemp.eliminar(c);
        }

        DialogoSeleccionCartas dialogo = new DialogoSeleccionCartas(this, "Elige 2 cartas para Hero", 2, barajaTemp);
        dialogo.setVisible(true);

        ArrayList<Carta> sel = dialogo.getSeleccion();
        if (sel.size() == 2) {
            listaHero = sel;
            mostrarImagen(cartasHero[0], sel.get(0).get_carta_PNG());
            mostrarImagen(cartasHero[1], sel.get(1).get_carta_PNG());
            mensaje.setText("Cartas de Hero seleccionadas.");
            if (listaBoard.size() == 4) {
                desactivarBotonesReparto();
            }
        }
    }

    private void elegirCartasBoard() {
        Baraja barajaTemp = new Baraja();
        for (Carta c : listaHero) {
            barajaTemp.eliminar(c);
        }

        DialogoSeleccionCartas dialogo = new DialogoSeleccionCartas(this, "Elige 4 cartas para el Board (Turn)", 4, barajaTemp);
        dialogo.setVisible(true);

        ArrayList<Carta> sel = dialogo.getSeleccion();
        if (sel.size() == 4) {
            listaBoard = sel;
            for (int i = 0; i < 4; i++) {
                mostrarImagen(cartasBoard[i], sel.get(i).get_carta_PNG());
            }
            mensaje.setText("Cartas del Board seleccionadas.");
            if (listaHero.size() == 2) {
                desactivarBotonesReparto();
            }
        }
    }

    private void calcularOdds() {
        if (listaHero.size() != 2) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar 2 cartas para Hero.", "Faltan cartas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaBoard.size() != 4) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar 4 cartas para el Board.", "Faltan cartas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rango = rangoVillano.getText().trim();
        if (rango.isEmpty() || rango.equals("Introduce un rango")) {
            JOptionPane.showMessageDialog(this, "Introduce un rango para el villano (ej: AA, QQ).", "Falta rango", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String emTexto = emHero.getText().trim().replace("%", "");
        if (emTexto.isEmpty() || emTexto.equals("Introduce un Equity")) {
            JOptionPane.showMessageDialog(this, "Introduce un Equity Mínimo (ej: 30%).", "Falta EM", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double emValor;
        try {
            emValor = Double.parseDouble(emTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor de EM no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ResultadoTurn resultado = CalculadoraOutsTurn.calcular(listaHero, listaBoard, rango, emValor);

        if (resultado.mensajeError != null) {
            JOptionPane.showMessageDialog(this, resultado.mensajeError, "Error de cálculo", JOptionPane.ERROR_MESSAGE);
            mensaje.setText("Error: " + resultado.mensajeError);
            return;
        }

        // Mostrar desglose en el recuadro blanco
        areaDesglose.setText(resultado.desgloseTexto);

        // Cambiar el fondo del cuadro EM a verde si hace CALL y a rojo si hace FOLD
        if (resultado.esCall) {
            emHero.setBackground(Color.green);
            mensaje.setText(String.format(java.util.Locale.ROOT,
                "Hero hace CALL", resultado.mediaOuts, emValor));
        } else {
            emHero.setBackground(Color.red);
            mensaje.setText(String.format(java.util.Locale.ROOT,
                "Hero hace FOLD", resultado.mediaOuts, emValor));
        }
    }

    private void reiniciar() {
        listaHero.clear();
        listaBoard.clear();
        baraja = new Baraja();

        for (JLabel l : cartasHero) {
            mostrarImagen(l, "back.png");
        }
        for (JLabel l : cartasBoard) {
            mostrarImagen(l, "back.png");
        }

        emHero.setText("Introduce un Equity");
        emHero.setBackground(new Color(15, 75, 48));

        rangoVillano.setText("Introduce un rango");
        rangoVillano.setBackground(new Color(15, 75, 48));

        areaDesglose.setText("");

        // Habilitar de nuevo los botones de reparto
        botonRepartirRandom.setEnabled(true);
        botonElegirHero.setEnabled(true);
        botonElegirBoard.setEnabled(true);

        mensaje.setText("Pulsa Repartir para comenzar.");
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
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            return;
        }
        ImageIcon original = new ImageIcon(recurso);
        Image imagen = original.getImage().getScaledInstance(75, 110, Image.SCALE_SMOOTH);
        label.setText("");
        label.setBorder(null);
        label.setIcon(new ImageIcon(imagen));
    }
}
