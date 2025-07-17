package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menú Principal del Administrador (Dashboard) con interfaz gráfica JFrame.
 * Ofrece acceso a las distintas funcionalidades de gestión para un administrador.
 *
 * @author José Flores
 */
public class AdminDashboardGUI extends JFrame implements ActionListener { // Implementar ActionListener

    private Usuario administradorActual; // El usuario administrador logueado
    // Se podría añadir una referencia a SistemaEncuestasGUI si es la ventana principal
    // private SistemaEncuestasGUI parentFrame;

    public AdminDashboardGUI(Usuario admin) {
        super("Sistema de Encuestas - Panel de Administrador");
        this.administradorActual = admin;
        initComponents();
        setupFrame();
    }

    /* Si AdminDashboardGUI fuera lanzado por SistemaEncuestasGUI
    public AdminDashboardGUI(Usuario admin, SistemaEncuestasGUI parentFrame) {
        super("Sistema de Encuestas - Panel de Administrador");
        this.administradorActual = admin;
        this.parentFrame = parentFrame;
        initComponents();
        setupFrame();
    }
    */

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior para bienvenida e información del usuario
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblBienvenida = new JLabel("<html>Bienvenido, <b>" + administradorActual.getNombres() + " " + administradorActual.getApellidos() + "</b> (ID: " + administradorActual.getId_usuario() + ")</html>", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(lblBienvenida, BorderLayout.NORTH);
        JLabel lblRol = new JLabel("Rol: " + administradorActual.getRol(), SwingConstants.CENTER);
        lblRol.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(lblRol, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con botones para las opciones del administrador
        JPanel optionsPanel = new JPanel(new GridLayout(4, 2, 15, 15)); // 4 filas, 2 columnas
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Opciones de Gestión"));

        JButton btnGestionEncuestas = new JButton("Gestionar Encuestas");
        JButton btnGestionBancoPreguntas = new JButton("Gestionar Banco de Preguntas");
        JButton btnGestionPreguntasRegistro = new JButton("Gestionar Preguntas de Registro");
        JButton btnGestionTiposPregunta = new JButton("Gestionar Tipos de Pregunta");
        JButton btnGestionClasificaciones = new JButton("Gestionar Clasificaciones");
        JButton btnGestionUsuarios = new JButton("Gestionar Usuarios");
        JButton btnVerResultados = new JButton("Ver Resultados de Encuestas");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        optionsPanel.add(btnGestionEncuestas);
        optionsPanel.add(btnGestionBancoPreguntas);
        optionsPanel.add(btnGestionPreguntasRegistro);
        optionsPanel.add(btnGestionTiposPregunta);
        optionsPanel.add(btnGestionClasificaciones);
        optionsPanel.add(btnGestionUsuarios);
        optionsPanel.add(btnVerResultados);
        optionsPanel.add(btnCerrarSesion);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        add(mainPanel);

        // --- Eventos de los botones ---
        btnGestionEncuestas.addActionListener(e -> mostrarGestionEncuestasGUI());
        btnGestionBancoPreguntas.addActionListener(e -> mostrarGestionBancoPreguntasGUI());
        btnGestionPreguntasRegistro.addActionListener(e -> mostrarGestionPreguntasRegistroGUI());
        btnGestionTiposPregunta.addActionListener(e -> mostrarGestionTiposPreguntaGUI());
        btnGestionClasificaciones.addActionListener(e -> mostrarGestionClasificacionesGUI());
        btnGestionUsuarios.addActionListener(e -> mostrarGestionUsuariosGUI());
        btnVerResultados.addActionListener(e -> mostrarVerResultadosGUI());

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    AdminDashboardGUI.this,
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
        setSize(800, 600); // Tamaño inicial
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void cerrarSesion() {
        // Al cerrar sesión, volvemos a la pantalla de LoginGUI
        // CAMBIO: Pasar 'this' (AdminDashboardGUI, que ahora implementa ActionListener)
        // como el parentListener para el LoginGUI.
        // Opcional: Si SistemaEncuestasGUI es la ventana principal, sería mejor
        // que AdminDashboardGUI tuviera una referencia a ella y le notificara
        // para que SistemaEncuestasGUI maneje el cambio de panel a LoginGUI.
        // Por simplicidad, y sin una reestructuración mayor, instanciamos aquí.
        LoginGUI loginGUI = new LoginGUI(this); //
        loginGUI.setVisible(true); //
        this.dispose(); // Cierra esta ventana del dashboard
    }

    // --- Métodos para las futuras GUIs de gestión ---
    private void mostrarGestionEncuestasGUI() {
        GestionEncuestasGUI gestionEncuestas = new GestionEncuestasGUI(administradorActual, this);
        gestionEncuestas.setVisible(true);
        this.setVisible(false); // Ocultar el dashboard
    }

    private void mostrarGestionBancoPreguntasGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión del Banco de Preguntas (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarGestionPreguntasRegistroGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Preguntas de Registro (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarGestionTiposPreguntaGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Tipos de Pregunta (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarGestionClasificacionesGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Clasificaciones (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarGestionUsuariosGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Usuarios (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarVerResultadosGUI() {
        ReportesEncuestaGUI reportesGUI = new ReportesEncuestaGUI(this);
        reportesGUI.setVisible(true);
        this.setVisible(false); // Ocultar el dashboard
    }
    
    // Método para volver a hacer visible esta ventana (cuando un sub-GUI se cierra)
    public void mostrarAdminDashboardGUI() {
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Implementar el método actionPerformed de la interfaz ActionListener.
        // Aquí se pueden manejar los eventos que LoginGUI u otras GUIs hijas puedan enviar
        // de vuelta a AdminDashboardGUI.
        String command = e.getActionCommand();
        if ("login_exitoso".equals(command)) {
            // Este caso no debería ocurrir aquí si LoginGUI siempre devuelve a SistemaEncuestasGUI.
            // Si LoginGUI fuera a redirigir directamente a este dashboard, se manejaría aquí.
            // Por ahora, se deja vacío o para depuración.
            System.out.println("AdminDashboardGUI recibió un evento de login exitoso. Esto es inesperado si SistemaEncuestasGUI lo maneja.");
        } else if ("mostrar_registro".equals(command)) {
            // Este caso tampoco debería ocurrir aquí si SistemaEncuestasGUI maneja los cambios de panel.
            System.out.println("AdminDashboardGUI recibió un evento para mostrar registro. Esto es inesperado si SistemaEncuestasGUI lo maneja.");
        }
        // No es necesario que esta implementación haga algo complejo,
        // ya que la interacción principal de LoginGUI (volver a SistemaEncuestasGUI)
        // se manejaría mejor si AdminDashboardGUI tuviera una referencia a SistemaEncuestasGUI.
    }
}