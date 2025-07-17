package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Importar ActionListener

/**
 * Menú Principal del Encuestado con interfaz gráfica JFrame.
 * Ofrece las opciones principales para un usuario encuestado.
 *
 * @author José Flores
 */
public class EncuestadoMenuGUI extends JFrame implements ActionListener { // Implementar ActionListener

    private Usuario encuestadoActual;

    public EncuestadoMenuGUI(Usuario encuestado) {
        super("Sistema de Encuestas - Menú de Encuestado");
        this.encuestadoActual = encuestado;
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblBienvenida = new JLabel("<html>Bienvenido, <b>" + encuestadoActual.getNombres() + " " + encuestadoActual.getApellidos() + "</b></html>", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblBienvenida, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 15, 15)); // 2 filas, 1 columna
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Opciones de Encuestado"));

        JButton btnVerEncuestas = new JButton("Ver Encuestas Disponibles");
        JButton btnConsultarPerfil = new JButton("Consultar Mi Perfil");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        optionsPanel.add(btnVerEncuestas);
        optionsPanel.add(btnConsultarPerfil);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(btnCerrarSesion, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos de los botones ---
        btnVerEncuestas.addActionListener(e -> mostrarEncuestasDisponiblesGUI());
        btnConsultarPerfil.addActionListener(e -> mostrarPerfilUsuarioGUI());

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    EncuestadoMenuGUI.this,
                    "¿Estás seguro de que quieres cerrar sesión?",
                    "Confirmar Cierre de Sesión",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    cerrarSesion();
                }
            }
        });
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); // Tamaño inicial
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void cerrarSesion() {
        // Al cerrar sesión, volvemos a la pantalla de LoginGUI
        // CAMBIO: Pasar 'this' (EncuestadoMenuGUI, que ahora implementa ActionListener)
        // como el parentListener para el LoginGUI.
        // Lo ideal sería que SistemaEncuestasGUI manejara esta transición de vuelta al Login.
        LoginGUI loginGUI = new LoginGUI(this); // Pasa esta instancia como ActionListener
        loginGUI.setVisible(true); //
        this.dispose(); // Cierra esta ventana del menú encuestado
    }

    private void mostrarPerfilUsuarioGUI() {
        PerfilUsuarioGUI perfilGUI = new PerfilUsuarioGUI(encuestadoActual, this);
        perfilGUI.setVisible(true);
        this.setVisible(false); // Oculta el menú del encuestado
    }

    private void mostrarEncuestasDisponiblesGUI() {
        EncuestasDisponiblesGUI encuestasGUI = new EncuestasDisponiblesGUI(encuestadoActual, this);
        encuestasGUI.setVisible(true);
        this.setVisible(false); // Oculta el menú del encuestado
    }
    
    // Método para volver a hacer visible esta ventana
    public void mostrarEncuestadoMenuGUI() {
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Implementación de ActionListener. Similar a AdminDashboardGUI,
        // los eventos de navegación hacia Login (como "login_exitoso" o "mostrar_registro")
        // si fueran disparados por LoginGUI, deberían ser manejados idealmente por
        // SistemaEncuestasGUI. Aquí se deja la implementación básica.
        String command = e.getActionCommand();
        if ("login_exitoso".equals(command)) {
            // Esto no debería ocurrir aquí si LoginGUI siempre devuelve a SistemaEncuestasGUI.
            System.out.println("EncuestadoMenuGUI recibió un evento de login exitoso. Esto es inesperado si SistemaEncuestasGUI lo maneja.");
        } else if ("mostrar_registro".equals(command)) {
            // Esto tampoco debería ocurrir aquí.
            System.out.println("EncuestadoMenuGUI recibió un evento para mostrar registro. Esto es inesperado si SistemaEncuestasGUI lo maneja.");
        }
    }
}