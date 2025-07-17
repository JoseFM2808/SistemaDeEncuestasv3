// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-d28f78b4ee52ebdf33638181d18315a7fdfd2c18/src/SteveJobs/encuestas/ui/UIRegistroUsuario.java
package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.PreguntaRegistro;
import SteveJobs.encuestas.servicio.ServicioConfiguracionAdmin;
import SteveJobs.encuestas.servicio.ServicioUsuarios;
import SteveJobs.encuestas.util.PilaNavegacion; // Importar PilaNavegacion
import java.time.LocalDate; // Importar LocalDate
import java.time.format.DateTimeParseException; // Importar DateTimeParseException
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class UIRegistroUsuario {
    private ServicioUsuarios servicioUsuarios;
    private ServicioConfiguracionAdmin servicioConfiguracionAdmin;
    private PilaNavegacion pilaNavegacion;

    public UIRegistroUsuario(PilaNavegacion pilaNavegacion) {
        this.servicioUsuarios = new ServicioUsuarios();
        this.servicioConfiguracionAdmin = new ServicioConfiguracionAdmin();
        this.pilaNavegacion = pilaNavegacion;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String[] opcionesMenu = {"Registrarse", "Volver al Menú Principal"};
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "--- REGISTRO DE USUARIO ---",
                    "Registro",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcionesMenu,
                    opcionesMenu[0]
            );

            switch (opcion) {
                case 0: // Registrarse
                    registrarNuevoUsuario();
                    break;
                case 1: // Volver al Menú Principal
                    pilaNavegacion.pop(); // Vuelve al menú anterior
                    break;
                case -1: // Cerrar ventana
                    return;
            }
        } while (pilaNavegacion.peek() == this.getClass().getName()); // Mantiene en este menú hasta salir
    }

    private void registrarNuevoUsuario() {
        String dni = JOptionPane.showInputDialog(null, "Ingrese DNI:");
        if (dni == null || dni.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "DNI no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombres = JOptionPane.showInputDialog(null, "Ingrese Nombres:");
        if (nombres == null || nombres.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombres no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String apellidos = JOptionPane.showInputDialog(null, "Ingrese Apellidos:");
        if (apellidos == null || apellidos.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Apellidos no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String email = JOptionPane.showInputDialog(null, "Ingrese Email:");
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField pf = new JPasswordField();
        String password = JOptionPane.showConfirmDialog(null, pf, "Ingrese Contraseña:", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ? new String(pf.getPassword()) : null;
        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField pfConfirm = new JPasswordField();
        String confirmPassword = JOptionPane.showConfirmDialog(null, pfConfirm, "Confirme Contraseña:", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ? new String(pfConfirm.getPassword()) : null;
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fechaNacimientoStr = JOptionPane.showInputDialog(null, "Ingrese Fecha de Nacimiento (YYYY-MM-DD):");
        if (fechaNacimientoStr == null || fechaNacimientoStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fecha de Nacimiento no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha de nacimiento inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String genero = JOptionPane.showInputDialog(null, "Ingrese Género:");
        if (genero == null || genero.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Género no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String distrito = JOptionPane.showInputDialog(null, "Ingrese Distrito de Residencia:");
        if (distrito == null || distrito.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Distrito no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Recopilar respuestas a preguntas de registro dinámicas
        Map<Integer, String> respuestasDinamicas = new HashMap<>();
        List<PreguntaRegistro> preguntasRegistro = servicioConfiguracionAdmin.obtenerTodasLasPreguntasRegistro(); //
        boolean allDynamicAnswered = true;
        if (preguntasRegistro != null && !preguntasRegistro.isEmpty()) {
            for (PreguntaRegistro pr : preguntasRegistro) {
                String respuesta = null;
                if (pr.isEsObligatoria()) {
                    respuesta = JOptionPane.showInputDialog(null, "Pregunta de Registro: " + pr.getTextoPregunta());
                    if (respuesta == null || respuesta.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, responda la pregunta obligatoria: " + pr.getTextoPregunta(), "Advertencia", JOptionPane.WARNING_MESSAGE);
                        allDynamicAnswered = false;
                        break;
                    }
                } else {
                    respuesta = JOptionPane.showInputDialog(null, "Pregunta de Registro (Opcional): " + pr.getTextoPregunta());
                }
                if (respuesta != null) {
                    respuestasDinamicas.put(pr.getIdPreguntaRegistro(), respuesta.trim());
                }
            }
        }
        
        if (!allDynamicAnswered) {
            return;
        }

        try {
            // CAMBIO CLAVE: Cambiar el nombre del método y el último argumento.
            boolean registrado = servicioUsuarios.registrarUsuario( 
                    dni, nombres, apellidos, email, password, fechaNacimiento, genero, distrito, respuestasDinamicas); //

            if (registrado) {
                JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar usuario. El email o DNI ya podría estar en uso o faltan datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado durante el registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en UIRegistroUsuario: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}