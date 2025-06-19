/*
 * Autor: José Flores (Líder del Proyecto, orquestador del menú de Administrador)
 *
 * Propósito: Interfaz de Usuario (UI) que presenta el menú de opciones para el usuario Administrador.
 * Dirige el flujo hacia las UIs de gestión de Preguntas de Registro y Encuestas.
 * (Pendiente: UIs para "Gestionar Usuarios", "Gestionar Tipos de Pregunta",
 * "Gestionar Clasificaciones de Pregunta", "Gestionar Banco de Preguntas", y "Ver Resultados de Encuestas").
 */
// Contenido de SteveJobs.encuestas.ui.UIMenuAdministrador
package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Usuario;
import javax.swing.JOptionPane;

public class UIMenuAdministrador {

    public static void mostrarMenu(Usuario admin) {
        boolean salirMenuAdmin = false;
        while (!salirMenuAdmin) {
            String[] opcionesAdmin = {
                "Gestionar Preguntas de Registro",
                "Gestionar Usuarios", // Esta UI es nueva
                "Gestionar Tipos de Pregunta", // Esta UI es nueva
                "Gestionar Clasificaciones de Pregunta", // Esta UI es nueva
                "Gestionar Banco de Preguntas", // Esta UI es nueva
                "Gestionar Encuestas",
                "Ver Resultados de Encuestas", // Esta UI es nueva
                "Salir (Volver al Menú Principal)"
            };

            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Menú Administrador - Bienvenido " + admin.getNombresApellidos(),
                    "Panel de Administración",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcionesAdmin,
                    opcionesAdmin[0]
            );

            if (seleccion == null || seleccion.equals(opcionesAdmin[7])) {
                salirMenuAdmin = true;
                continue;
            }

            switch (seleccion) {
                case "Gestionar Preguntas de Registro":
                    UIGestionPreguntasRegistro.mostrarMenu(); // Ya implementada por José
                    break;
                case "Gestionar Usuarios":
                    // REQMS-015: Consultar Perfil de Usuario Encuestado (UI para Admin sobre usuarios)
                    // Nueva UI UIGestionUsuarios
                    JOptionPane.showMessageDialog(null, "Funcionalidad 'Gestionar Usuarios' pendiente.");
                    // Aquí llamarías a UIGestionUsuarios.mostrarMenu(); cuando la implementes
                    break;
                case "Gestionar Tipos de Pregunta":
                    // REQMS-017 (parte): Gestionar Tipos
                    UIGestionTiposPregunta.mostrarMenu(); // Nueva UI de Pablo
                    break;
                case "Gestionar Clasificaciones de Pregunta":
                    // REQMS-016: Gestionar Clasificaciones
                    UIGestionClasificaciones.mostrarMenu(); // Nueva UI de Pablo
                    break;
                case "Gestionar Banco de Preguntas":
                    // REQMS-017, REQMS-018: Gestionar Banco de Preguntas
                    UIGestionBancoPreguntas.mostrarMenu(); // Nueva UI de Pablo
                    break;
                case "Gestionar Encuestas":
                    UIGestionEncuestas.mostrarMenu(admin); // Ya implementada por Alfredo
                    break;
                case "Ver Resultados de Encuestas":
                    // REQMS-004, REQMS-005, REQMS-006: Ver Resultados
                    JOptionPane.showMessageDialog(null, "Funcionalidad 'Ver Resultados de Encuestas' pendiente.");
                    // Aquí llamarías a UIVerResultados.mostrarMenu(); cuando la implementes (Tarea de José)
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}