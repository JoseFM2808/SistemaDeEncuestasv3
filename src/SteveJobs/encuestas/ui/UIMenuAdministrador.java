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

public class UIMenuAdministrador {

    public static void mostrarMenu(Usuario admin) {
        boolean salirMenuAdmin = false;
        while (!salirMenuAdmin) {
            String[] opcionesAdmin = {
                "Gestionar Preguntas de Registro",
                "Gestionar Encuestas",
                "Gestionar Banco de Preguntas",
                "Ver Resultados de Encuestas", // Opción de reportes actualizada
                "Gestionar Usuarios", // Funcionalidad pendiente
                "Gestionar Tipos de Pregunta", // Funcionalidad pendiente
                "Gestionar Clasificaciones de Pregunta", // Funcionalidad pendiente
                "Salir (Cerrar Sesión)"
            };

            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Menú Administrador - Bienvenido " + admin.getNombres(),
                    "Panel de Administración",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcionesAdmin,
                    opcionesAdmin[0]
            );

            if (seleccion == null || seleccion.equals(opcionesAdmin[7])) { // Opción "Salir" o cerrar ventana
                salirMenuAdmin = true;
                continue;
            }

            // Usamos la Pila de Navegación antes de entrar a un submenú
            // Para la opción "Salir", no empujamos a la pila.
            // Para las opciones pendientes, tampoco empujamos si no hay un submenú real.
            if (!seleccion.equals(opcionesAdmin[7])) {
                if (PilaNavegacion.instance != null) {
                    PilaNavegacion.instance.push("MenuAdmin");
                }
            }


            switch (seleccion) {
                case "Gestionar Preguntas de Registro":
                    UIGestionPreguntasRegistro.mostrarMenu();
                    break;
                case "Gestionar Encuestas":
                    UIGestionEncuestas.mostrarMenu(admin);
                    break;
                case "Gestionar Banco de Preguntas":
                    // Asume que existe UIGestionBancoPreguntas con este método
                    // UIGestionBancoPreguntas.mostrarMenu(); // Descomentar cuando la clase exista y esté lista
                    JOptionPane.showMessageDialog(null, "Funcionalidad 'Gestionar Banco de Preguntas' pendiente.");
                    // Si se muestra un mensaje de pendiente y no se entra a un submenú, revertimos el push
                    if (PilaNavegacion.instance != null && !PilaNavegacion.instance.isEmpty()) {
                        PilaNavegacion.instance.pop();
                    }
                    break;
                case "Ver Resultados de Encuestas":
                    UIReportesEncuesta.mostrarMenu(); // Llama al nuevo menú de reportes
                    break;
                case "Gestionar Usuarios":
                    JOptionPane.showMessageDialog(null, "Funcionalidad 'Gestionar Usuarios' pendiente.");
                     if (PilaNavegacion.instance != null && !PilaNavegacion.instance.isEmpty()) {
                        PilaNavegacion.instance.pop();
                    }
                    break;
                case "Gestionar Tipos de Pregunta":
                    JOptionPane.showMessageDialog(null, "Funcionalidad 'Gestionar Tipos de Pregunta' pendiente.");
                     if (PilaNavegacion.instance != null && !PilaNavegacion.instance.isEmpty()) {
                        PilaNavegacion.instance.pop();
                    }
                    break;
                case "Gestionar Clasificaciones de Pregunta":
                    JOptionPane.showMessageDialog(null, "Funcionalidad 'Gestionar Clasificaciones de Pregunta' pendiente.");
                     if (PilaNavegacion.instance != null && !PilaNavegacion.instance.isEmpty()) {
                        PilaNavegacion.instance.pop();
                    }
                    break;
                default:
                    // Si la opción no es válida, no debería haber un push en la pila, así que lo sacamos.
                     if (PilaNavegacion.instance != null && !PilaNavegacion.instance.isEmpty()) {
                        PilaNavegacion.instance.pop();
                    }
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}
