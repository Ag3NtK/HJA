package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class PokerEquityGUI extends JFrame {

	// Paneles principales
	private JPanel panelIzquierdo; // Matriz + Slider
	private JPanel panelDerecho; // Board + Jugadores + Salida
	char[] rangos = { 'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2' };
	JMenuBar menuBar;
	private JButton[][] matrizBotones = new JButton[13][13];

	public PokerEquityGUI() {
		super("Calculadora de Equity y Rangos - Práctica 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 700);
		setLocationRelativeTo(null); // Centra la ventana en pantalla

		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));
		JMenuBar menuBar = new JMenuBar();

		// Instanciamos los dos contenedores principales
		panelIzquierdo = crearPanelMatrizYSlider();
		panelDerecho = crearPanelControlesYResultados();
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		// Los añadimos al BorderLayout del JFrame
		add(panelIzquierdo, BorderLayout.CENTER);
		add(panelDerecho, BorderLayout.EAST);
		add(menuBar, BorderLayout.NORTH);

	}

	private String obtenerTextoMano(int fila, int col) {

		if (col > fila) {
			
			return ""+rangos[fila] + rangos[col] + "s";
			

		} else if (col < fila) {
		
			return ""+rangos[col] + rangos[fila] + "o";
		} else {
		
			return ""+rangos[fila] + rangos[col] + "";
		}
	}

	private JPanel crearPanelMatrizYSlider() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Matriz de Rangos (169 manos)"));

		// TODO: Aquí añadiremos el GridLayout(13, 13) para los botones
		JPanel panel2 = new JPanel(new GridLayout(13, 13, 2, 2));
		for (int i = 0; i < 13; i++) {// rows
			for (int j = 0; j < 13; j++) {// columns
				Color color = null;
				if (j>i) {
					color = new Color(232, 108, 108);
				}
				else if (i>j) {
					color = new Color(0, 162, 232); // Offsuited
			    } else {
			        color = new Color(116, 232, 127); // Pareja
			    }
			
				String textoMano = obtenerTextoMano(i, j);
				JButton btn = createButton(textoMano, "Seleciona este rango",color);

				// Opcional: ajustar fuente y margen para que encajen bien

				matrizBotones[i][j] = btn;
				panel2.add(btn);
			}

		}

		panel.add(panel2, BorderLayout.CENTER);
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

	private JButton createButton(String texto, String toolTip,Color color) {
		JButton boton = new JButton(texto);
		boton.setBackground(color);
		
		boton.setFont(new Font("SansSerif", Font.PLAIN, 17));
		boton.setMargin(new Insets(1, 1, 1, 1));

		boton.setToolTipText(toolTip);
		return boton;
	}
}