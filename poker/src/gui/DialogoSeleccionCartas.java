package gui;

import javax.swing.*;
import cartas.Baraja;
import cartas.Carta;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class DialogoSeleccionCartas extends JDialog {
    private static final String RUTA_CARTAS = "/PNG_cards/";
    private final ArrayList<Carta> seleccionadas = new ArrayList<>();
    private final int maxSeleccion;
    
    public DialogoSeleccionCartas(JFrame parent, String titulo, int maxSeleccion, Baraja barajaActual) {
        super(parent, titulo, true);
        this.maxSeleccion = maxSeleccion;
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelCartas = new JPanel(new GridLayout(4, 13, 5, 5));
        panelCartas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCartas.setBackground(new Color(20, 105, 65));
        
        String[] palos = {"h", "d", "c", "s"}; 
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
        
        // Obtener las cartas que quedan disponibles en la baraja en formato String 
        ArrayList<String> nombresDisponibles = new ArrayList<>();
        for (Carta c : barajaActual.getCartasRestantes()) {
            nombresDisponibles.add(c.get_carta_String()); 
        }

        for (String palo : palos) {
            for (String valor : valores) {
                String nombreCarta = valor + palo;
                JToggleButton botonCarta = crearBotonCarta(nombreCarta);
                
                // Si la carta no está en las restantes, la deshabilitamos
                if (!nombresDisponibles.contains(nombreCarta)) {
                    botonCarta.setEnabled(false);
                    // esta carta está usada
                    botonCarta.setBackground(Color.DARK_GRAY);
                } else {
                    botonCarta.addActionListener(e -> {
                        if (botonCarta.isSelected()) {
                            if (seleccionadas.size() < maxSeleccion) {
                                seleccionadas.add(new Carta(nombreCarta));
                                botonCarta.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                            } else {
                                botonCarta.setSelected(false); // No permite seleccionar más de n cartas
                            }
                        } else {
                            // Buscar y eliminar la carta de la lista por su nombre
                            seleccionadas.removeIf(c -> c.get_carta_String().equals(nombreCarta));
                            botonCarta.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        }
                    });
                }
                panelCartas.add(botonCarta);
            }
        }
        
        JPanel panelInferior = new JPanel();
        JButton btnConfirmar = new JButton("Confirmar Selección");
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirmar.addActionListener(e -> {
            if (seleccionadas.size() == maxSeleccion) {
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Debes seleccionar exactamente " + maxSeleccion + " cartas.", 
                    "Selección incompleta", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panelInferior.add(btnConfirmar);
        
        add(panelCartas, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JToggleButton crearBotonCarta(String nombreCarta) {
        JToggleButton boton = new JToggleButton();
        boton.setPreferredSize(new Dimension(60, 90));
        boton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setBackground(Color.WHITE);
        
        Carta temp = new Carta(nombreCarta); 
        URL recurso = getClass().getResource(RUTA_CARTAS + temp.get_carta_PNG());
        
        if (recurso != null) {
            ImageIcon original = new ImageIcon(recurso);
            Image imagen = original.getImage().getScaledInstance(55, 85, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagen));
        } else {
            boton.setText(nombreCarta);
        }
        
        return boton;
    }
    
    public ArrayList<Carta> getSeleccion() {
        return seleccionadas;
    }
}