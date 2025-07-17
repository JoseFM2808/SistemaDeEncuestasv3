package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.ui.UIGestionBancoPreguntas; // Importar
import SteveJobs.encuestas.ui.UIGestionPreguntasRegistro; // Importar
import SteveJobs.encuestas.ui.UIGestionTiposPregunta; // Importar
import SteveJobs.encuestas.ui.UIGestionClasificacionesPregunta; // Importar
import SteveJobs.encuestas.ui.UIGestionUsuarios; // Importar

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
public class AdminDashboardGUI extends JFrame implements ActionListener {

    private Usuario administradorActual;
    // Se podría añadir una referencia a SistemaEncuestasGUI si es la ventana principal
    // private SistemaEncuestasGUI parentFrame; // Descomentar y usar si se pasa como constructor

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
        JPanel optionsPanel = new JPanel(new GridLayout(4, 2, 15, 15));
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
        btnGestionBancoPreguntas.addActionListener(e -> mostrarGestionBancoPreguntasUI()); // Cambio aquí
        btnGestionPreguntasRegistro.addActionListener(e -> mostrarGestionPreguntasRegistroUI()); // Cambio aquí
        btnGestionTiposPregunta.addActionListener(e -> mostrarGestionTiposPreguntaUI()); // Cambio aquí
        btnGestionClasificaciones.addActionListener(e -> mostrarGestionClasificacionesUI()); // Cambio aquí
        btnGestionUsuarios.addActionListener(e -> mostrarGestionUsuariosUI()); // Cambio aquí
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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void cerrarSesion() {
        LoginGUI loginGUI = new LoginGUI(this);
        loginGUI.setVisible(true);
        this.dispose();
    }

    // --- Métodos para las futuras GUIs de gestión ---

    // Este ya estaba implementado para usar GestionEncuestasGUI (JFrame)
    private void mostrarGestionEncuestasGUI() {
        GestionEncuestasGUI gestionEncuestas = new GestionEncuestasGUI(administradorActual, this);
        gestionEncuestas.setVisible(true);
        this.setVisible(false);
    }

    // REDIRECCIONAR A LAS UIs DE CONSOLA/JOPTIONPANE
    private void mostrarGestionBancoPreguntasUI() {
        // Oculta la ventana actual del dashboard
        this.setVisible(false);
        // Llama al método estático que muestra el menú de gestión del banco de preguntas (JOptionPane/Consola)
        UIGestionBancoPreguntas.mostrarMenu();
        // Cuando UIGestionBancoPreguntas.mostrarMenu() termine (el usuario seleccione 0),
        // vuelve a mostrar el dashboard
        this.setVisible(true);
    }

    private void mostrarGestionPreguntasRegistroUI() {
        this.setVisible(false);
        UIGestionPreguntasRegistro.mostrarMenu();
        this.setVisible(true);
    }
    
    private void mostrarGestionTiposPreguntaUI() {
        this.setVisible(false);
        UIGestionTiposPregunta.mostrarMenu();
        this.setVisible(true);
    }
    
    private void mostrarGestionClasificacionesUI() {
        this.setVisible(false);
        UIGestionClasificacionesPregunta.mostrarMenu();
        this.setVisible(true);
    }

    private void mostrarGestionUsuariosUI() {
        this.setVisible(false);
        UIGestionUsuarios.mostrarMenu();
        this.setVisible(true);
    }

    // Este ya estaba implementado para usar ReportesEncuestaGUI (JFrame)
    private void mostrarVerResultadosGUI() {
        ReportesEncuestaGUI reportesGUI = new ReportesEncuestaGUI(this);
        reportesGUI.setVisible(true);
        this.setVisible(false);
    }
    
    // Método para volver a hacer visible esta ventana (cuando un sub-GUI se cierra)
    public void mostrarAdminDashboardGUI() {
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("login_exitoso".equals(command)) {
            System.out.println("AdminDashboardGUI recibió un evento de login exitoso. Esto es inesperado si SistemaEncuestasGUI lo maneja.");
        } else if ("mostrar_registro".equals(command)) {
            System.out.println("AdminDashboardGUI recibió un evento para mostrar registro. Esto es inesperado si SistemaEncuestasGUI lo maneja.");
        }
    }
}