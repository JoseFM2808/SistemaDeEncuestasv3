/*
 * Autores del Módulo:
 * - Pablo Alegre
 * - Asistente de AED (Actualización para captura de Nombres y Apellidos por separado)
 *
 * Responsabilidad Principal:
 * - UI para registro de usuarios
 */
package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.servicio.ServicioUsuarios;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UIRegistroUsuario {

    public static void mostrarFormularioRegistro() {
        String docId, nombres, apellidos, email, password, confirmarPassword; // Añadido 'apellidos'
        String tipoUsuarioSeleccionado; // Esta variable no se usa para selección, se asigna por defecto

        docId = JOptionPane.showInputDialog(null, "Documento de Identidad:", "Registro de Usuario", JOptionPane.PLAIN_MESSAGE);
        if (docId == null) {
            return;
        }
        if (docId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El Documento de Identidad no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- CAMBIO AQUÍ: Pedir Nombres y Apellidos por separado ---
        nombres = JOptionPane.showInputDialog(null, "Nombres:", "Registro de Usuario", JOptionPane.PLAIN_MESSAGE);
        if (nombres == null) {
            return;
        }
        if (nombres.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los Nombres no pueden estar vacíos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        apellidos = JOptionPane.showInputDialog(null, "Apellidos:", "Registro de Usuario", JOptionPane.PLAIN_MESSAGE);
        if (apellidos == null) {
            return;
        }
        if (apellidos.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los Apellidos no pueden estar vacíos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // --- FIN CAMBIO ---

        email = JOptionPane.showInputDialog(null, "Email:", "Registro de Usuario", JOptionPane.PLAIN_MESSAGE);
        if (email == null) {
            return;
        }
        if (email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El Email no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField pfPassword = new JPasswordField();
        Object[] LpanPass = {"Contraseña:" ,pfPassword};
        int optionPassword = JOptionPane.showConfirmDialog(null, LpanPass, "Registro de Usuario - Contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (optionPassword != JOptionPane.OK_OPTION) {
            return;
        }
        password = new String(pfPassword.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La Contraseña no puede estar vacía.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField pfConfirmPassword = new JPasswordField();
        Object[] LpanConfirmPass = {"Confirmar Contraseña:" ,pfConfirmPassword};
        int optionConfirmPassword = JOptionPane.showConfirmDialog(null, LpanConfirmPass, "Registro de Usuario - Confirmar Contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (optionConfirmPassword != JOptionPane.OK_OPTION) {
            return;
        }
        confirmarPassword = new String(pfConfirmPassword.getPassword());
        if (confirmarPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La confirmación de contraseña no puede estar vacía.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmarPassword)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rolPorDefecto = "Encuestado"; // Por defecto, el rol para auto-registro es "Encuestado"

        ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

        // Adaptar la llamada al nuevo método registrarNuevoUsuario con los campos de nombres y apellidos separados
        boolean registrado = servicioUsuarios.registrarNuevoUsuario(
                docId.trim(),       // dni
                nombres.trim(),     // nombres
                apellidos.trim(),   // apellidos (ahora capturado)
                email.trim(),       // email
                password,           // clave
                null,               // fecha_nacimiento
                null,               // genero
                null,               // distrito_residencia
                rolPorDefecto       // rol
        );

        if (registrado) {
            JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario.\nVerifique que el email o DNI no estén ya en uso o intente más tarde.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(UIRegistroUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        System.out.println("Mostrando formulario de registro de usuario...");
        mostrarFormularioRegistro();
        System.out.println("Proceso de registro finalizado (o cancelado).");
        
        System.exit(0);
    }

    public UIRegistroUsuario(JTextField txtNombres, JTextField txtApellidos) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public UIRegistroUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
