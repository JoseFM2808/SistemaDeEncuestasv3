package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioAutenticacion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pantalla de Inicio de Sesión (Login) con interfaz gráfica JFrame.
 * Permite al usuario ingresar sus credenciales o acceder a la pantalla de registro.
 *
 * @author José Flores
 */
public class LoginGUI extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JLabel lblMensaje; // Para mostrar mensajes de error/éxito
    private final ServicioAutenticacion servicioAutenticacion;

    public LoginGUI() {
        super("Sistema de Encuestas - Iniciar Sesión");
        servicioAutenticacion = new ServicioAutenticacion();
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        // Panel principal con un BorderLayout para organizar componentes
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel para el título y la bienvenida
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Encuestas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitulo, BorderLayout.NORTH);
        JLabel lblSubtitulo = new JLabel("Inicia sesión o regístrate para continuar", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(lblSubtitulo, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel para los campos de entrada (email y contraseña)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Credenciales"));

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(20);
        // txtEmail.setText("admin@example.com"); // Eliminado: datos de prueba
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField(20);
        // txtPassword.setText("admin123"); // Eliminado: datos de prueba
        inputPanel.add(txtPassword);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        inputPanel.add(lblMensaje); // Mensajes de error/éxito
        inputPanel.add(new JLabel("")); // Espaciador

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel para los botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegister = new JButton("Registrarse");
        JButton btnExit = new JButton("Salir");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir el panel principal al frame
        add(mainPanel);

        // --- Eventos de los botones ---
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistro();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Ajusta el tamaño de la ventana a sus componentes
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setResizable(false); // Opcional: no permitir redimensionar
    }

    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor, ingrese email y contraseña.");
            return;
        }

        try {
            Usuario usuario = servicioAutenticacion.autenticar(email, password);
            if (usuario != null) {
                lblMensaje.setText("¡Bienvenido, " + usuario.getNombres() + "!");
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso. Rol: " + usuario.getRol(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Redirigir a la pantalla principal según el rol
                if ("Administrador".equalsIgnoreCase(usuario.getRol())) {
                    new AdminDashboardGUI(usuario).setVisible(true); // Abre el dashboard de admin
                } else if ("Encuestado".equalsIgnoreCase(usuario.getRol())) {
                    new EncuestadoMenuGUI(usuario).setVisible(true); // Abre el menú de encuestado
                }
                this.dispose(); // Cierra la ventana de login
            } else {
                lblMensaje.setText("Credenciales inválidas. Intente de nuevo.");
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error al iniciar sesión: " + ex.getMessage());
            System.err.println("Error en LoginGUI al iniciar sesión: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void abrirVentanaRegistro() {
        this.setVisible(false); // Oculta la ventana de login
        RegistroUsuarioGUI registroGUI = new RegistroUsuarioGUI(this); // Pasamos referencia para volver
        registroGUI.setVisible(true);
    }
    
    // Método para volver a hacer visible esta ventana (desde RegistroUsuarioGUI)
    public void mostrarLoginGUI() {
        this.setVisible(true);
        txtPassword.setText(""); // Limpiar la contraseña
        lblMensaje.setText(""); // Limpiar mensajes
    }
}