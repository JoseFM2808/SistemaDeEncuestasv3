package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

/**
 * Pantalla para que el usuario encuestado consulte su información de perfil.
 *
 * @author José Flores
 */
public class PerfilUsuarioGUI extends JFrame {

    private Usuario usuarioActual;
    private final ServicioUsuarios servicioUsuarios;
    private final EncuestadoMenuGUI parentMenu; // Referencia al menú principal del encuestado

    private JLabel lblDni, lblNombres, lblApellidos, lblEmail, lblFechaNacimiento, lblGenero, lblDistrito;

    public PerfilUsuarioGUI(Usuario usuario, EncuestadoMenuGUI parent) {
        super("Sistema de Encuestas - Mi Perfil");
        this.usuarioActual = usuario;
        this.parentMenu = parent;
        this.servicioUsuarios = new ServicioUsuarios();
        initComponents();
        setupFrame();
        cargarDatosPerfil();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Mi Perfil de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // 7 filas para los campos
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información Personal"));
        infoPanel.setBackground(new Color(240, 248, 255)); // Un color de fondo suave

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        infoPanel.add(new JLabel("<html><b>DNI:</b></html>"));
        lblDni = new JLabel();
        infoPanel.add(lblDni);

        infoPanel.add(new JLabel("<html><b>Nombres:</b></html>"));
        lblNombres = new JLabel();
        infoPanel.add(lblNombres);

        infoPanel.add(new JLabel("<html><b>Apellidos:</b></html>"));
        lblApellidos = new JLabel();
        infoPanel.add(lblApellidos);

        infoPanel.add(new JLabel("<html><b>Email:</b></html>"));
        lblEmail = new JLabel();
        infoPanel.add(lblEmail);

        infoPanel.add(new JLabel("<html><b>Fecha Nacimiento:</b></html>"));
        lblFechaNacimiento = new JLabel();
        infoPanel.add(lblFechaNacimiento);

        infoPanel.add(new JLabel("<html><b>Género:</b></html>"));
        lblGenero = new JLabel();
        infoPanel.add(lblGenero);

        infoPanel.add(new JLabel("<html><b>Distrito:</b></html>"));
        lblDistrito = new JLabel();
        infoPanel.add(lblDistrito);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> volverAlMenu());
        mainPanel.add(btnVolver, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra esta ventana
        pack(); // Ajusta el tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana
        setResizable(false);
        
        // Listener para cuando se cierra la ventana, para mostrar el menú padre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentMenu.mostrarEncuestadoMenuGUI();
            }
        });
    }

    private void cargarDatosPerfil() {
        try {
            // Recargar el usuario por si sus datos fueron actualizados en otro lado
            usuarioActual = servicioUsuarios.obtenerUsuarioPorId(usuarioActual.getId_usuario());
            if (usuarioActual != null) {
                lblDni.setText(usuarioActual.getDni());
                lblNombres.setText(usuarioActual.getNombres());
                lblApellidos.setText(usuarioActual.getApellidos());
                lblEmail.setText(usuarioActual.getEmail());
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (usuarioActual.getFecha_nacimiento()!= null) {
                    lblFechaNacimiento.setText(sdf.format(usuarioActual.getFecha_nacimiento()));
                } else {
                    lblFechaNacimiento.setText("No especificado");
                }
                
                lblGenero.setText(usuarioActual.getGenero());
                lblDistrito.setText(usuarioActual.getDistrito_residencia());
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la información del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                volverAlMenu(); // Vuelve si no se encuentra el usuario
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el perfil: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en PerfilUsuarioGUI al cargar perfil: " + ex.getMessage());
            ex.printStackTrace();
            volverAlMenu();
        }
    }

    private void volverAlMenu() {
        this.dispose(); // Cierra esta ventana
        // parentMenu.mostrarEncuestadoMenuGUI() ya se llama en windowClosed
    }
}