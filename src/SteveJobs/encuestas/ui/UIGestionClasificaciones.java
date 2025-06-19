// Nuevo archivo: SteveJobs.encuestas.ui.UIGestionClasificaciones.java
package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Usar el DAO de Pablo

import javax.swing.JOptionPane;
import java.util.List;

public class UIGestionClasificaciones {

    private static ClasificacionPreguntaDAO clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // DAO de Pablo

    public static void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            String[] opciones = {
                "Listar Clasificaciones de Pregunta",
                "Agregar Nueva Clasificación de Pregunta",
                "Modificar Clasificación de Pregunta",
                "Eliminar Clasificación de Pregunta",
                "Volver al Menú Administrador"
            };
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gestión de Clasificaciones de Pregunta",
                    "Admin: Clasificaciones de Pregunta",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == null) {
                salir = true;
                continue;
            }

            switch (seleccion) {
                case "Listar Clasificaciones de Pregunta":
                    listarClasificacionesUI();
                    break;
                case "Agregar Nueva Clasificación de Pregunta":
                    agregarClasificacionUI();
                    break;
                case "Modificar Clasificación de Pregunta":
                    modificarClasificacionUI();
                    break;
                case "Eliminar Clasificación de Pregunta":
                    eliminarClasificacionUI();
                    break;
                case "Volver al Menú Administrador":
                    salir = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private static void listarClasificacionesUI() {
        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones(); // Método recién agregado al DAO
        if (clasificaciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clasificaciones de pregunta definidas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Clasificaciones de Pregunta Existentes:\n\n");
        for (ClasificacionPregunta clasif : clasificaciones) {
            sb.append("ID: ").append(clasif.getIdClasificacion())
              .append(" - Nombre: ").append(clasif.getNombreClasificacion())
              .append(" - Desc: ").append(clasif.getDescripcion() != null ? clasif.getDescripcion() : "N/A")
              .append(" - Estado: ").append(clasif.getEstado() != null ? clasif.getEstado() : "N/A")
              .append("\n------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Clasificaciones", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void agregarClasificacionUI() {
        String nombre = JOptionPane.showInputDialog(null, "Nombre de la nueva clasificación:", "Agregar Clasificación", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String descripcion = JOptionPane.showInputDialog(null, "Descripción (opcional):", "Agregar Clasificación", JOptionPane.PLAIN_MESSAGE);
        if (descripcion == null) descripcion = "";

        String[] estados = {"Activa", "Inactiva"};
        String estado = (String) JOptionPane.showInputDialog(null, "Estado inicial:", "Agregar Clasificación",
                JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
        if (estado == null) return;

        ClasificacionPregunta nuevaClasif = new ClasificacionPregunta(nombre.trim(), descripcion.trim(), estado);
        if (clasificacionPreguntaDAO.agregarClasificacion(nuevaClasif)) { // Método recién agregado al DAO
            JOptionPane.showMessageDialog(null, "Clasificación agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al agregar la clasificación. Verifique si ya existe un nombre similar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void modificarClasificacionUI() {
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID de la clasificación a modificar:", "Modificar Clasificación", JOptionPane.PLAIN_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClasificacionPregunta clasifExistente = clasificacionPreguntaDAO.obtenerClasificacionPorId(id);
        if (clasifExistente == null) {
            JOptionPane.showMessageDialog(null, "Clasificación con ID " + id + " no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog(null, "Nuevo nombre (actual: " + clasifExistente.getNombreClasificacion() + "):", "Modificar Clasificación", JOptionPane.PLAIN_MESSAGE);
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nuevaDescripcion = JOptionPane.showInputDialog(null, "Nueva descripción (actual: " + (clasifExistente.getDescripcion() != null ? clasifExistente.getDescripcion() : "") + "):", "Modificar Clasificación", JOptionPane.PLAIN_MESSAGE);
        if (nuevaDescripcion == null) nuevaDescripcion = "";

        String[] estados = {"Activa", "Inactiva"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(null, "Nuevo estado (actual: " + clasifExistente.getEstado() + "):", "Modificar Clasificación",
                JOptionPane.QUESTION_MESSAGE, null, estados, clasifExistente.getEstado());
        if (nuevoEstado == null) return;

        clasifExistente.setNombreClasificacion(nuevoNombre.trim());
        clasifExistente.setDescripcion(nuevaDescripcion.trim());
        clasifExistente.setEstado(nuevoEstado);

        // Se necesita un método actualizarClasificacion en ClasificacionPreguntaDAO
        // Por simplicidad, este método aún no está en el DAO de Pablo, solo "agregarClasificacion".
        JOptionPane.showMessageDialog(null, "Funcionalidad de modificación de clasificación de pregunta pendiente en el DAO.", "Información", JOptionPane.INFORMATION_MESSAGE);
        // if (clasificacionPreguntaDAO.actualizarClasificacion(clasifExistente)) { ... }
    }

    private static void eliminarClasificacionUI() {
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID de la clasificación a eliminar:", "Eliminar Clasificación", JOptionPane.PLAIN_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar la clasificación con ID " + id + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Se necesita un método eliminarClasificacion en ClasificacionPreguntaDAO
            // Por simplicidad, este método aún no está en el DAO de Pablo, solo "agregarClasificacion".
            JOptionPane.showMessageDialog(null, "Funcionalidad de eliminación de clasificación de pregunta pendiente en el DAO.", "Información", JOptionPane.INFORMATION_MESSAGE);
            // if (clasificacionPreguntaDAO.eliminarClasificacion(id)) { ... }
        }
    }
}