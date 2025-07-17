package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario; // Importar Usuario para el evento de login
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Importar ActionListener

/**
 * @author José Flores - Asistente de AED
 * Propósito: Ventana principal de la aplicación con interfaz gráfica (JFrame).
 * Se encarga de gestionar la visualización de diferentes paneles (Login, Registro, Menú Admin, Menú Encuestado).
 */
public class SistemaEncuestasGUI extends JFrame implements ActionListener { // Implementar ActionListener

    private CardLayout cardLayout; // Para alternar paneles
    private JPanel mainPanel;      // Panel que contendrá los paneles específicos

    // Nombres de los paneles para el CardLayout
    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String REGISTRO_PANEL = "RegistroPanel";
    public static final String ADMIN_DASHBOARD_PANEL = "AdminDashboardPanel"; // Agregado para futuros paneles
    public static final String ENCUESTADO_MENU_PANEL = "EncuestadoMenuPanel"; // Agregado para futuros paneles

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
        // CAMBIO: Usar los nombres de clase correctos (LoginGUI, RegistroUsuarioGUI)
        // y pasar 'this' como ActionListener.
        LoginGUI loginPanel = new LoginGUI(this); // Pasar referencia del JFrame principal como ActionListener
        RegistroUsuarioGUI registroPanel = new RegistroUsuarioGUI(this); // Pasar referencia del JFrame principal como ActionListener

        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(registroPanel, REGISTRO_PANEL);

        // Mostrar el panel de Login al inicio
        cardLayout.show(mainPanel, LOGIN_PANEL);
    }

    /**
     * Implementación del método actionPerformed de la interfaz ActionListener.
     * Este método recibe eventos de los paneles hijos (LoginGUI, RegistroUsuarioGUI)
     * para manejar la navegación entre ellos.
     * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); // Obtener el comando de la acción

        switch (command) {
            case "mostrar_registro":
                cardLayout.show(mainPanel, REGISTRO_PANEL);
                break;
            case "volver": // El botón "Volver" de RegistroUsuarioGUI
            case "registro_exitoso": // Después de un registro exitoso, volver al login
                cardLayout.show(mainPanel, LOGIN_PANEL);
                break;
            case "login_exitoso":
                Usuario usuario = (Usuario) e.getSource(); // Asumiendo que el usuario se pasa como fuente del evento
                if (usuario != null) {
                    this.setVisible(false); // Ocultar la ventana principal de login/registro
                    if ("Administrador".equalsIgnoreCase(usuario.getRol())) {
                        AdminDashboardGUI adminDashboard = new AdminDashboardGUI(usuario);
                        adminDashboard.setVisible(true);
                        // Cuando el AdminDashboard se cierre, debería hacer visible SistemaEncuestasGUI de nuevo
                        // o tener un listener para esa acción. Por ahora, sólo la muestra.
                    } else if ("Encuestado".equalsIgnoreCase(usuario.getRol())) {
                        EncuestadoMenuGUI encuestadoMenu = new EncuestadoMenuGUI(usuario);
                        encuestadoMenu.setVisible(true);
                        // Similar al AdminDashboard, gestionar el retorno
                    } else {
                        JOptionPane.showMessageDialog(this, "Rol de usuario no reconocido: " + usuario.getRol(), "Error de Rol", JOptionPane.ERROR_MESSAGE);
                        cardLayout.show(mainPanel, LOGIN_PANEL); // Vuelve al login si el rol no es válido
                        this.setVisible(true); // Asegurarse de que la ventana principal sea visible de nuevo
                    }
                }
                break;
            // Otros comandos de navegación pueden ser añadidos aquí si es necesario
        }
    }
    
    // El método showPanel ya no es necesario si toda la navegación se maneja por actionPerformed
    /*
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    */

    public static void main(String[] args) {
        // Ejecutar la aplicación en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            new SistemaEncuestasGUI().setVisible(true);
        });
    }
}