// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-b73347d68ca8a40e851f3439418b915b5f3ce710/src/SteveJobs/encuestas/gui/RegistroUsuarioGUI.java
package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.PreguntaRegistro;
import SteveJobs.encuestas.servicio.ServicioConfiguracionAdmin;
import SteveJobs.encuestas.servicio.ServicioUsuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// CAMBIO IMPORTANTE: Extender JPanel en lugar de JFrame
public class RegistroUsuarioGUI extends JPanel { //

    private JTextField txtDni, txtNombres, txtApellidos, txtEmail, txtFechaNacimiento, txtGenero, txtDistrito; //
    private JPasswordField txtPassword, txtConfirmPassword; //
    private JButton btnRegistrar, btnVolver; //
    private JPanel dynamicQuestionsPanel; //
    private Map<Integer, JComponent> dynamicFields; // Para almacenar los componentes dinámicos

    private ServicioUsuarios servicioUsuarios; //
    private ServicioConfiguracionAdmin servicioConfiguracionAdmin; //

    private ActionListener volverListener; // Para notificar a la ventana principal

    // Constructor ahora acepta un ActionListener para el botón volver
    public RegistroUsuarioGUI(ActionListener volverListener) { //
        this.volverListener = volverListener; //
        servicioUsuarios = new ServicioUsuarios(); //
        servicioConfiguracionAdmin = new ServicioConfiguracionAdmin(); //
        dynamicFields = new HashMap<>(); //
        initComponents(); //
        // Ya no necesitas setSize, setLocationRelativeTo, etc. aquí
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15)); //
        setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50)); //

        JLabel lblTitulo = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER); //
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24)); //
        add(lblTitulo, BorderLayout.NORTH); //

        JPanel formPanel = new JPanel(new GridBagLayout()); //
        GridBagConstraints gbc = new GridBagConstraints(); //
        gbc.insets = new Insets(8, 8, 8, 8); //
        gbc.fill = GridBagConstraints.HORIZONTAL; //

        // DNI
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("DNI:"), gbc); //
        gbc.gridx = 1; txtDni = new JTextField(20); formPanel.add(txtDni, gbc); //

        // Nombres
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Nombres:"), gbc); //
        gbc.gridx = 1; txtNombres = new JTextField(20); formPanel.add(txtNombres, gbc); //

        // Apellidos
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Apellidos:"), gbc); //
        gbc.gridx = 1; txtApellidos = new JTextField(20); formPanel.add(txtApellidos, gbc); //

        // Email
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Email:"), gbc); //
        gbc.gridx = 1; txtEmail = new JTextField(20); formPanel.add(txtEmail, gbc); //

        // Password
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Contraseña:"), gbc); //
        gbc.gridx = 1; txtPassword = new JPasswordField(20); formPanel.add(txtPassword, gbc); //

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Confirmar Contraseña:"), gbc); //
        gbc.gridx = 1; txtConfirmPassword = new JPasswordField(20); formPanel.add(txtConfirmPassword, gbc); //

        // Fecha de Nacimiento
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):"), gbc); //
        gbc.gridx = 1; txtFechaNacimiento = new JTextField(20); formPanel.add(txtFechaNacimiento, gbc); //

        // Género
        gbc.gridx = 0; gbc.gridy = 7; formPanel.add(new JLabel("Género:"), gbc); //
        gbc.gridx = 1; txtGenero = new JTextField(20); formPanel.add(txtGenero, gbc); //

        // Distrito
        gbc.gridx = 0; gbc.gridy = 8; formPanel.add(new JLabel("Distrito:"), gbc); //
        gbc.gridx = 1; txtDistrito = new JTextField(20); formPanel.add(txtDistrito, gbc); //

        // Panel para preguntas dinámicas de registro
        dynamicQuestionsPanel = new JPanel(); //
        dynamicQuestionsPanel.setLayout(new BoxLayout(dynamicQuestionsPanel, BoxLayout.Y_AXIS)); //
        loadDynamicRegistrationQuestions(); //

        // Añadir el dynamicQuestionsPanel a un JScrollPane si hay muchas preguntas
        JScrollPane scrollPane = new JScrollPane(dynamicQuestionsPanel); //
        scrollPane.setBorder(BorderFactory.createTitledBorder("Preguntas Adicionales de Perfil")); //

        gbc.gridx = 0; gbc.gridy = 9; //
        gbc.gridwidth = 2; //
        gbc.weighty = 1.0; // Para que el scrollPane ocupe espacio vertical
        gbc.fill = GridBagConstraints.BOTH; //
        formPanel.add(scrollPane, gbc); //

        add(formPanel, BorderLayout.CENTER); //

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); //
        btnRegistrar = new JButton("Registrar"); //
        btnVolver = new JButton("Volver"); //
        buttonPanel.add(btnRegistrar); //
        buttonPanel.add(btnVolver); //
        add(buttonPanel, BorderLayout.SOUTH); //

        btnRegistrar.addActionListener(e -> registrarUsuario()); //
        btnVolver.addActionListener(e -> { //
            if (volverListener != null) { //
                volverListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "volver")); //
            }
        }); //
    }

    private void loadDynamicRegistrationQuestions() {
        dynamicQuestionsPanel.removeAll(); // Limpiar antes de añadir
        dynamicFields.clear(); // Limpiar el mapa de campos dinámicos

        List<PreguntaRegistro> preguntas = servicioConfiguracionAdmin.obtenerTodasLasPreguntasRegistro(); //
        if (preguntas.isEmpty()) { //
            dynamicQuestionsPanel.add(new JLabel("No hay preguntas de registro adicionales configuradas.")); //
        } else {
            for (PreguntaRegistro pr : preguntas) { //
                JPanel questionRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //
                questionRow.add(new JLabel(pr.getTextoPregunta() + ":")); //

                JComponent inputComponent; //
                switch (pr.getTipoEntrada()) { //
                    case "texto": //
                        inputComponent = new JTextField(20); //
                        break;
                    case "numero": //
                        inputComponent = new JTextField(10); //
                        ((JTextField) inputComponent).setHorizontalAlignment(JTextField.RIGHT); //
                        break;
                    case "opcion_simple": //
                        String[] opcionesSimples = pr.getOpciones().split(","); //
                        inputComponent = new JComboBox<>(opcionesSimples); //
                        break;
                    default: //
                        inputComponent = new JTextField(20); // Valor por defecto
                        break;
                }
                questionRow.add(inputComponent); //
                dynamicQuestionsPanel.add(questionRow); //
                dynamicFields.put(pr.getIdPreguntaRegistro(), inputComponent); //
            }
        }
        dynamicQuestionsPanel.revalidate(); //
        dynamicQuestionsPanel.repaint(); //
    }

    private void registrarUsuario() {
        String dni = txtDni.getText().trim(); //
        String nombres = txtNombres.getText().trim(); //
        String apellidos = txtApellidos.getText().trim(); //
        String email = txtEmail.getText().trim(); //
        String password = new String(txtPassword.getPassword()); //
        String confirmPassword = new String(txtConfirmPassword.getPassword()); //
        String fechaNacimientoStr = txtFechaNacimiento.getText().trim(); //
        String genero = txtGenero.getText().trim(); //
        String distrito = txtDistrito.getText().trim(); //

        // Validaciones básicas
        if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty() || fechaNacimientoStr.isEmpty() ||
            genero.isEmpty() || distrito.isEmpty()) { //
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error de Registro", JOptionPane.ERROR_MESSAGE); //
            return; //
        }

        if (!password.equals(confirmPassword)) { //
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error de Registro", JOptionPane.ERROR_MESSAGE); //
            return; //
        }

        LocalDate fechaNacimiento; //
        try {
            fechaNacimiento = LocalDate.parse(fechaNacimientoStr); //
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha de nacimiento inválido. Use YYYY-MM-DD.", "Error de Registro", JOptionPane.ERROR_MESSAGE); //
            return; //
        }

        // Recopilar respuestas a preguntas dinámicas
        Map<Integer, String> respuestasDinamicas = new HashMap<>(); //
        boolean dynamicFieldsValid = true; //
        for (Map.Entry<Integer, JComponent> entry : dynamicFields.entrySet()) { //
            Integer preguntaId = entry.getKey(); //
            JComponent component = entry.getValue(); //
            String respuesta = ""; //

            if (component instanceof JTextField) { //
                respuesta = ((JTextField) component).getText().trim(); //
            } else if (component instanceof JComboBox) { //
                Object selectedItem = ((JComboBox<?>) component).getSelectedItem(); //
                if (selectedItem != null) { //
                    respuesta = selectedItem.toString().trim(); //
                }
            }

            if (respuesta.isEmpty()) { //
                JOptionPane.showMessageDialog(this, "Por favor, responda todas las preguntas adicionales.", "Error de Registro", JOptionPane.ERROR_MESSAGE); //
                dynamicFieldsValid = false; //
                break; //
            }
            respuestasDinamicas.put(preguntaId, respuesta); //
        }

        if (!dynamicFieldsValid) { //
            return; //
        }

        try {
            boolean registrado = servicioUsuarios.registrarUsuario(
                    dni, nombres, apellidos, email, password, fechaNacimiento, genero, distrito, respuestasDinamicas); //

            if (registrado) { //
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE); //
                // Notificar a la ventana principal para que muestre el login
                if (volverListener != null) { //
                    volverListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "registro_exitoso")); //
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario. El email o DNI ya podría estar en uso.", "Error de Registro", JOptionPane.ERROR_MESSAGE); //
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado durante el registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); //
            System.err.println("Error al registrar usuario en GUI: " + ex.getMessage()); //
            ex.printStackTrace(); //
        }
    }
}