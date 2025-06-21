/*
 * Responsable: José Flores (Refactorizado con Asistente de AED)
 * Relación con otras partes del código:
 * - Es el punto de entrada principal de la aplicación.
 * - Orquesta la navegación inicial a las interfaces de usuario de autenticación,
 * registro y los menús específicos de administrador/encuestado.
 * Funcionalidad:
 * - Inicializa la aplicación y presenta el menú principal de acceso.
 * - Redirige al usuario a la interfaz correspondiente según su rol.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Componente de orquestación de UI).
 */
package SteveJobs.encuestas.main;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.ui.UIAutenticacion;
import SteveJobs.encuestas.ui.UIMenuAdministrador;
import SteveJobs.encuestas.ui.UIMenuEncuestado;
import SteveJobs.encuestas.ui.UIRegistroUsuario;
import javax.swing.JOptionPane;

public class SistemaEncuestasApp {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SistemaEncuestasApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        mostrarMenuPrincipal();
    }

    public static void mostrarMenuPrincipal() {
        boolean salir = false;
        while (!salir) {
            String[] opciones = {"Iniciar Sesión", "Registrarse", "Salir"};
            int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Bienvenido al Sistema de Encuestas",
                    "Menú Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (seleccion) {
                case 0:
                    Usuario usuarioAutenticado = UIAutenticacion.mostrarLogin();
                    if (usuarioAutenticado != null) {
                        if ("Administrador".equalsIgnoreCase(usuarioAutenticado.getRol())) {
                            UIMenuAdministrador.mostrarMenu(usuarioAutenticado);
                        } else if ("Encuestado".equalsIgnoreCase(usuarioAutenticado.getRol())) { 
                            UIMenuEncuestado.mostrarMenu(usuarioAutenticado);
                        } else {
                             JOptionPane.showMessageDialog(null, "Rol de usuario desconocido: " + usuarioAutenticado.getRol());
                        }
                    }
                    break;
                case 1: // Registrarse
                    UIRegistroUsuario.mostrarFormularioRegistro();
                    break;
                case 2: // Salir
                case JOptionPane.CLOSED_OPTION:
                    salir = true;
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema. ¡Hasta pronto!");
                    break;
                default:
                    // No es necesario hacer nada aquí, el bucle continuará o saldrá si se cierra la ventana.
                    break;
            }
        }
    }
}