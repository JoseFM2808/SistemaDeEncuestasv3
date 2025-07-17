package SteveJobs.encuestas.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author José Flores - Asistente de AED
 * Propósito: Ventana principal de la aplicación con interfaz gráfica (JFrame).
 * Se encarga de gestionar la visualización de diferentes paneles (Login, Registro, Menú Admin, Menú Encuestado).
 */
public class SistemaEncuestasGUI extends JFrame {

    private CardLayout cardLayout; // Para alternar paneles
    private JPanel mainPanel;      // Panel que contendrá los paneles específicos

    public SistemaEncuestasGUI() {
        setTitle("Sistema de Encuestas - ¡Bienvenido!");
        setSize(800, 600); // Tamaño recomendado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Configurar Look and Feel (Nimbus si está disponible)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está disponible, usar el Look and Feel por defecto
            System.err.println("No se pudo establecer el Look and Feel Nimbus. Usando el predeterminado.");
        }

        // Inicializar CardLayout y Panel Principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel); // Añadir el panel principal al JFrame

        // Crear e añadir los paneles iniciales (Login y Registro)
        LoginPanel loginPanel = new LoginPanel(this); // Pasar referencia del JFrame principal
        RegistroPanel registroPanel = new RegistroPanel(this); // Pasar referencia del JFrame principal

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registroPanel, "Registro");

        // Mostrar el panel de Login al inicio
        cardLayout.show(mainPanel, "Login");
    }

    /**
     * Muestra el panel especificado por su nombre.
     * @param panelName El nombre del panel a mostrar (ej. "Login", "Registro", "MenuAdmin").
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        // Ejecutar la aplicación en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            new SistemaEncuestasGUI().setVisible(true);
        });
    }
}