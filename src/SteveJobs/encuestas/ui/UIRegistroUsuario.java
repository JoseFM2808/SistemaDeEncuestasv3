/*
 * Responsable: Pablo Alegre
 * Relación con otras partes del código:
 * - Es la interfaz de usuario para el registro de nuevos usuarios.
 * - Se comunica con ServicioUsuarios para realizar el proceso de registro.
 * Funcionalidad:
 * - Solicita y recolecta los datos necesarios para registrar un nuevo usuario en el sistema.
 * - Realiza validaciones básicas de los campos de entrada.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de interfaz de usuario).
 */

package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.servicio.ServicioUsuarios;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class UIRegistroUsuario {

    public static void mostrarFormularioRegistro() {
        String docId, nombres, email, password, confirmarPassword, tipoUsuarioSeleccionado;

        docId = JOptionPane.showInputDialog(null, "Documento de Identidad:", "Registro de Usuario", JOptionPane.PLAIN_MESSAGE);
        if (docId == null) {
            return;
        }
        if (docId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El Documento de Identidad no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nombres = JOptionPane.showInputDialog(null, "Nombres Completos:", "Registro de Usuario", JOptionPane.PLAIN_MESSAGE);
        if (nombres == null) {
            return;
        }
        if (nombres.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los Nombres Completos no pueden estar vacíos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        // String[] tiposUsuario = {"Administrador", "Encuestado"}; // No permitir selección de rol en auto-registro
        // tipoUsuarioSeleccionado = (String) JOptionPane.showInputDialog(
        //         null,
        //         "Seleccione el Tipo de Usuario:",
        //         "Registro de Usuario - Tipo",
        //         JOptionPane.QUESTION_MESSAGE,
        //         null,
        //         tiposUsuario,
        //         tiposUsuario[0]
        // );
        // if (tipoUsuarioSeleccionado == null) {
        //     return;
        // }
        // Por defecto, el rol para auto-registro es "Encuestado"
        String rolPorDefecto = "Encuestado";

        ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

        // Adaptar la llamada al nuevo método registrarNuevoUsuario
        // Se asume que "nombres" contiene tanto nombres como apellidos para esta adaptación.
        // Los campos fecha_nacimiento, genero, distrito_residencia no se recogen en este UI.
        boolean registrado = servicioUsuarios.registrarNuevoUsuario(
                docId.trim(),       // dni
                nombres.trim(),     // nombres
                "",                 // apellidos (dejado vacío o se podría intentar un split de 'nombres')
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
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario.\nVerifique que el email no esté ya en uso o intente más tarde.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
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
}