package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioAutenticacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Importar ActionListener

/**
 * Panel de Login (JPanel). Permite al usuario ingresar sus credenciales
 * y autenticarse en el sistema. Notifica a un listener externo (SistemaEncuestasGUI)
 * sobre acciones como "mostrar_registro" o "login_exitoso".
 *
 * @author José Flores
 */
public class LoginGUI extends JPanel implements ActionListener { // Implementar ActionListener

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegistrar;

    private ServicioAutenticacion servicioAutenticacion;
    private ActionListener parentListener; // Referencia al ActionListener de la ventana padre

    public LoginGUI(ActionListener parentListener) { // Constructor ahora recibe un ActionListener
        this.parentListener = parentListener; // Almacenar el listener
        this.servicioAutenticacion = new ServicioAutenticacion();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Encuestas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Inicia sesión o regístrate", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        add(txtPassword, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistrar = new JButton("Registrarse");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegistrar);
        add(buttonPanel, gbc);

        // --- Eventos ---
        btnLogin.addActionListener(e -> intentarLogin());
        btnRegistrar.addActionListener(e -> abrirVentanaRegistro()); // Ahora llama al nuevo método
    }

    private void intentarLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese email y contraseña.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuario = servicioAutenticacion.autenticar(email, password);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombres() + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                // Notificar al listener padre sobre el éxito del login
                if (parentListener != null) {
                    // Pasar el usuario a la siguiente GUI o al frame principal para gestionar la navegación
                    ActionEvent event = new ActionEvent(usuario, ActionEvent.ACTION_PERFORMED, "login_exitoso");
                    parentListener.actionPerformed(event);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar iniciar sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en LoginGUI al intentar login: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void abrirVentanaRegistro() {
        // Notificar al listener padre (SistemaEncuestasGUI) para que muestre el panel de registro
        if (parentListener != null) {
            parentListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "mostrar_registro"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Este método se implementa porque LoginGUI ahora es un ActionListener.
        // En este contexto, LoginGUI no necesita reaccionar a sus propios eventos a través de este método,
        // ya que los botones usan expresiones lambda directas.
        // Su propósito principal es permitir que LoginGUI sea pasado como un listener al constructor,
        // si fuera necesario para subcomponentes internos que la notificaran a ella.
        // En este caso, solo actúa como un passthrough si es necesario,
        // pero la notificación principal es hacia parentListener.
    }
}