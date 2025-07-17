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
public class AdminDashboardGUI extends JFrame {

    private Usuario administradorActual; // El usuario administrador logueado

    public AdminDashboardGUI(Usuario admin) {
        super("Sistema de Encuestas - Panel de Administrador");
        this.administradorActual = admin;
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior para bienvenida e información del usuario
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblBienvenida = new JLabel("<html>Bienvenido, <b>" + administradorActual.getNombres() + " " + administradorActual.getApellidos() + "</b> (ID: " + administradorActual.getIdUsuario() + ")</html>", SwingConstants.CENTER);
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
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.setVisible(true);
        this.dispose(); // Cierra esta ventana del dashboard
    }

    // --- Métodos Placeholder para las futuras GUIs de gestión ---
    private void mostrarGestionEncuestasGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Encuestas (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // Aquí, en el futuro, se instanciará y mostrará la GestionEncuestasGUI real
        // this.setVisible(false); // Ocultar el dashboard si la nueva ventana es modal o principal
        // new GestionEncuestasGUI(administradorActual, this).setVisible(true);
    }

    private void mostrarGestionBancoPreguntasGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión del Banco de Preguntas (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // this.setVisible(false);
        // new GestionBancoPreguntasGUI(administradorActual, this).setVisible(true);
    }

    private void mostrarGestionPreguntasRegistroGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Preguntas de Registro (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // this.setVisible(false);
        // new GestionPreguntasRegistroGUI(administradorActual, this).setVisible(true);
    }
    
    private void mostrarGestionTiposPreguntaGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Tipos de Pregunta (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // this.setVisible(false);
        // new GestionTiposPreguntaGUI(administradorActual, this).setVisible(true);
    }
    
    private void mostrarGestionClasificacionesGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Clasificaciones (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // this.setVisible(false);
        // new GestionClasificacionesGUI(administradorActual, this).setVisible(true);
    }

    private void mostrarGestionUsuariosGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a la Gestión de Usuarios (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // this.setVisible(false);
        // new GestionUsuariosGUI(administradorActual, this).setVisible(true);
    }

    private void mostrarVerResultadosGUI() {
        JOptionPane.showMessageDialog(this, "Redirigiendo a Ver Resultados de Encuestas (GUI en desarrollo)...", "Info", JOptionPane.INFORMATION_MESSAGE);
        // this.setVisible(false);
        // new VerResultadosGUI(administradorActual, this).setVisible(true);
    }
}