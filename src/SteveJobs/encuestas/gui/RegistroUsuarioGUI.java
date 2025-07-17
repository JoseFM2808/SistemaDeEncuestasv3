package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.servicio.ServicioUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import com.toedter.calendar.JDateChooser; // Importar JDateChooser

/**
 * Pantalla de Registro de Usuario con interfaz gráfica JFrame.
 * Permite a nuevos usuarios registrarse en el sistema.
 *
 * @author José Flores
 */
public class RegistroUsuarioGUI extends JFrame {

    private JTextField txtDni, txtNombres, txtApellidos, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JDateChooser dateChooserFechaNacimiento; // JDateChooser para la fecha de nacimiento
    private JComboBox<String> cmbGenero, cmbDistrito;
    private JLabel lblMensaje;

    private final ServicioUsuarios servicioUsuarios;
    private final LoginGUI loginParent; // Referencia a la ventana de login para volver

    public RegistroUsuarioGUI(LoginGUI parent) {
        super("Sistema de Encuestas - Registro de Usuario");
        this.loginParent = parent;
        servicioUsuarios = new ServicioUsuarios();
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10)); // 9 filas para los campos
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos Personales"));

        formPanel.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        formPanel.add(txtDni);

        formPanel.add(new JLabel("Nombres:"));
        txtNombres = new JTextField();
        formPanel.add(txtNombres);

        formPanel.add(new JLabel("Apellidos:"));
        txtApellidos = new JTextField();
        formPanel.add(txtApellidos);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Confirmar Contraseña:"));
        txtConfirmPassword = new JPasswordField();
        formPanel.add(txtConfirmPassword);
        
        formPanel.add(new JLabel("Fecha Nacimiento:"));
        dateChooserFechaNacimiento = new JDateChooser(); // Instancia de JDateChooser
        dateChooserFechaNacimiento.setDateFormatString("yyyy-MM-dd"); // Formato de fecha
        formPanel.add(dateChooserFechaNacimiento);
        
        formPanel.add(new JLabel("Género:"));
        cmbGenero = new JComboBox<>(new String[]{"", "Masculino", "Femenino", "Otro"});
        formPanel.add(cmbGenero);
        
        formPanel.add(new JLabel("Distrito:"));
        cmbDistrito = new JComboBox<>(new String[]{"", "Chorrillos", "Barranco", "Miraflores", "San Isidro", "Santiago de Surco"});
        formPanel.add(cmbDistrito);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        mainPanel.add(lblMensaje, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRegister = new JButton("Registrarme");
        JButton btnBack = new JButton("Volver al Login");

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);
        
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END); // Asegura que los botones estén al final

        add(mainPanel);

        // --- Eventos de los botones ---
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlLogin();
            }
        });
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        pack(); // Ajusta el tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana
        setResizable(false);
    }

    private void registrarUsuario() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        Date fechaNacimiento = dateChooserFechaNacimiento.getDate(); // Obtener la fecha directamente de JDateChooser
        String genero = (String) cmbGenero.getSelectedItem();
        String distrito = (String) cmbDistrito.getSelectedItem();

        // Validaciones básicas
        if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fechaNacimiento == null || genero == null || genero.isEmpty() || distrito == null || distrito.isEmpty()) {
            lblMensaje.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblMensaje.setText("Las contraseñas no coinciden.");
            return;
        }
        
        try {
            // Llama al servicio de usuarios para registrar
            // NOTA: El rol "Encuestado" se asigna por defecto desde ServicioUsuarios
            servicioUsuarios.registrarNuevoUsuario(dni, nombres, apellidos, email, password, fechaNacimiento, genero, distrito);
            lblMensaje.setForeground(new Color(0, 128, 0)); // Verde para éxito
            lblMensaje.setText("¡Usuario registrado con éxito! Ahora puedes iniciar sesión.");
            
            JOptionPane.showMessageDialog(this, "Registro exitoso. ¡Bienvenido!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlLogin(); // Redirige al login
            
        } catch (IllegalArgumentException ex) {
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText(ex.getMessage()); // Mensaje de error desde el servicio
            System.err.println("Error de validación al registrar usuario: " + ex.getMessage());
        } catch (Exception ex) {
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText("Error al registrar usuario: " + ex.getMessage());
            System.err.println("Error inesperado al registrar usuario: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void volverAlLogin() {
        this.dispose(); // Cierra esta ventana de registro
        loginParent.mostrarLoginGUI(); // Muestra la ventana de login
    }
}