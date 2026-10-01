package gui;

import javax.swing.*;
import java.awt.*;

public class PokerEquityGUI extends JFrame {

    // Paneles principales
    private JPanel panelIzquierdo; // Matriz + Slider
    private JPanel panelDerecho;   // Board + Jugadores + Salida
    
    public PokerEquityGUI() {
        super("Calculadora de Equity y Rangos - Práctica 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null); // Centra la ventana en pantalla

        inicializarComponentes();
    }
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Instanciamos los dos contenedores principales
        panelIzquierdo = crearPanelMatrizYSlider();
        panelDerecho = crearPanelControlesYResultados();

        // Los añadimos al BorderLayout del JFrame
        add(panelIzquierdo, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);
    }

    private JPanel crearPanelMatrizYSlider() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Matriz de Rangos (169 manos)"));
        
        // TODO: Aquí añadiremos el GridLayout(13, 13) para los botones
        // TODO: Y en la parte SOUTH de este panel, el JSlider

        return panel;
    }

    private JPanel crearPanelControlesYResultados() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(420, 0)); // Fijamos ancho de la columna derecha
        panel.setBorder(BorderFactory.createTitledBorder("Configuración y Simulación"));

        // TODO: Añadir inputs de Board, Jugadores y botón de cálculo

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PokerEquityGUI().setVisible(true);
        });
    }
}