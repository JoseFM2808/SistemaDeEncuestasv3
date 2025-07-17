/*
 * Responsable: José Flores (Refactorizado con Asistente de AED)
 * Relación con otras partes del código:
 * - Es el menú principal para los usuarios con el rol de Administrador.
 * - Orquesta la navegación a las interfaces de gestión de Preguntas de Registro,
 * Encuestas, Banco de Preguntas, etc.
 * - Utiliza la PilaNavegacion para gestionar el historial de menús.
 * Funcionalidad:
 * - Presenta un menú de opciones específicas para el rol de Administrador,
 * permitiendo acceder a diversas funcionalidades de gestión.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - Emplea la Pila (Stack) a través de PilaNavegacion para gestionar el flujo de navegación entre submenús.
 */

package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.util.PilaNavegacion;
import javax.swing.JOptionPane;

/**
 * @author José Flores
 */
public class UIMenuAdministrador {

    private final PilaNavegacion<Runnable> historialMenu;
    private final Usuario administradorActual;

    public UIMenuAdministrador(Usuario administradorActual) {
        this.historialMenu = new PilaNavegacion<>();
        this.administradorActual = administradorActual;
    }

    public void mostrarMenuAdministrador() {
        historialMenu.push(this::mostrarOpcionesPrincipales); // Establecer la función principal del menú
        
        while (!historialMenu.isEmpty()) {
            try {
                historialMenu.peek().run(); // Ejecutar el menú actual
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en UIMenuAdministrador: " + e.getMessage());
                e.printStackTrace(); // Para depuración
                if (historialMenu.size() > 1) { // Si no es el menú principal, se puede volver
                    historialMenu.pop();
                } else { // Si es el menú principal, salir de la aplicación o reintentar
                    JOptionPane.showMessageDialog(null, "Volviendo al menú principal de la aplicación...", "Error", JOptionPane.INFORMATION_MESSAGE);
                    historialMenu.clear(); // Limpiar pila para forzar salida o re-inicio
                }
            }
        }
    }

    private void mostrarOpcionesPrincipales() {
        String menu = """
                      --- Menú de Administrador ---
                      Bienvenido/a, %s %s (ID: %d)
                      1. Gestionar Encuestas
                      2. Gestionar Banco de Preguntas
                      3. Gestionar Preguntas de Registro (Perfil de Encuestado)
                      4. Gestionar Tipos de Pregunta
                      5. Gestionar Clasificaciones de Pregunta
                      6. Gestionar Usuarios
                      7. Ver Resultados de Encuestas
                      8. Cerrar Sesión
                      """.formatted(administradorActual.getNombres(), administradorActual.getApellidos(), administradorActual.getIdUsuario());

        String opcionStr = JOptionPane.showInputDialog(null, menu, "Menú Administrador", JOptionPane.PLAIN_MESSAGE);

        if (opcionStr == null) { // Cancelar o cerrar ventana
            historialMenu.pop(); // Volver al menú anterior (ej. login)
            return;
        }

        try {
            int opcion = Integer.parseInt(opcionStr);
            switch (opcion) {
                case 1:
                    historialMenu.push(this::gestionarEncuestas);
                    break;
                case 2:
                    historialMenu.push(this::gestionarBancoPreguntas);
                    break;
                case 3:
                    historialMenu.push(this::gestionarPreguntasRegistro);
                    break;
                case 4:
                    historialMenu.push(this::gestionarTiposPregunta);
                    break;
                case 5:
                    historialMenu.push(this::gestionarClasificacionesPregunta);
                    break;
                case 6:
                    historialMenu.push(this::gestionarUsuarios);
                    break;
                case 7:
                    historialMenu.push(this::verResultadosEncuestas);
                    break;
                case 8:
                    historialMenu.pop(); // Cerrar sesión y volver al menú principal de la aplicación
                    JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos para cada sub-menú, que llaman a sus respectivas UI ---

    private void gestionarEncuestas() {
        UIGestionEncuestas uiGestionEncuestas = new UIGestionEncuestas();
        uiGestionEncuestas.mostrarMenuGestionEncuestas(); // Ejecuta el flujo de gestión de encuestas
        historialMenu.pop(); // Una vez que termina el flujo, vuelve al menú de administrador
    }

    private void gestionarBancoPreguntas() {
        UIGestionBancoPreguntas uiGestionBanco = new UIGestionBancoPreguntas();
        uiGestionBanco.mostrarMenuGestionBancoPreguntas(); // Ejecuta el flujo de gestión del banco
        historialMenu.pop();
    }

    private void gestionarPreguntasRegistro() {
        UIGestionPreguntasRegistro uiGestionPreguntasRegistro = new UIGestionPreguntasRegistro();
        uiGestionPreguntasRegistro.mostrarMenuGestionPreguntasRegistro(); // Ejecuta el flujo
        historialMenu.pop();
    }
    
    private void gestionarTiposPregunta() {
        UIGestionTiposPregunta uiGestionTiposPregunta = new UIGestionTiposPregunta();
        uiGestionTiposPregunta.mostrarMenuGestionTiposPregunta(); // Ejecuta el flujo
        historialMenu.pop();
    }
    
    private void gestionarClasificacionesPregunta() {
        UIGestionClasificacionesPregunta uiGestionClasificacionesPregunta = new UIGestionClasificacionesPregunta();
        uiGestionClasificacionesPregunta.mostrarMenuGestionClasificacionesPregunta(); // Ejecuta el flujo
        historialMenu.pop();
    }

    private void gestionarUsuarios() {
        UIGestionUsuarios uiGestionUsuarios = new UIGestionUsuarios();
        uiGestionUsuarios.mostrarMenuGestionUsuarios(); // Ejecuta el flujo
        historialMenu.pop();
    }

    private void verResultadosEncuestas() {
        UIReportesEncuesta uiReportesEncuesta = new UIReportesEncuesta();
        uiReportesEncuesta.mostrarMenuReportes(); // Ejecuta el flujo
        historialMenu.pop();
    }
}