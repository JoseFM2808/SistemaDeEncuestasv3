package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Acceso directo al DAO
import SteveJobs.encuestas.modelo.ClasificacionPregunta;

import javax.swing.JOptionPane;
import java.util.List;

public class UIGestionClasificacionesPregunta {

    private static ClasificacionPreguntaDAO clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // Utiliza el DAO directamente

    public static void mostrarMenu() {
        int opcion;
        do {
            String menu = "--- Gestión de Clasificaciones de Pregunta ---\n"
                    + "1. Registrar Nueva Clasificación\n"
                    + "2. Listar Todas las Clasificaciones\n"
                    + "3. Modificar Clasificación Existente\n"
                    + "4. Eliminar Clasificación\n"
                    + "0. Volver al Menú Principal";
            String input = JOptionPane.showInputDialog(null, menu, "Menú Clasificaciones de Pregunta", JOptionPane.PLAIN_MESSAGE);

            if (input == null) {
                opcion = 0;
            } else {
                try {
                    opcion = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opción inválida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    opcion = -1;
                }
            }

            switch (opcion) {
                case 1:
                    registrarClasificacion();
                    break;
                case 2:
                    listarClasificaciones();
                    break;
                case 3:
                    modificarClasificacion();
                    break;
                case 4:
                    eliminarClasificacion();
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Volviendo al Menú Principal del Administrador.", "Volver", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    if (opcion != -1) {
                        JOptionPane.showMessageDialog(null, "Opción no reconocida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarClasificacion() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la nueva clasificación:");
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre de la clasificación no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si la clasificación ya existe
        if (clasificacionPreguntaDAO.obtenerClasificacionPorNombre(nombre) != null) {
            JOptionPane.showMessageDialog(null, "Ya existe una clasificación con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción (opcional):");
        if (descripcion == null) descripcion = ""; // Si cancela o no ingresa nada
        
        String[] estados = {"ACTIVA", "INACTIVA"};
        String estadoSeleccionado = (String) JOptionPane.showInputDialog(null, 
                "Seleccione el estado:", "Estado de Clasificación", 
                JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
        if (estadoSeleccionado == null) return;

        ClasificacionPregunta nuevaClasificacion = new ClasificacionPregunta(nombre, descripcion, estadoSeleccionado);
        boolean registrado = clasificacionPreguntaDAO.crearClasificacionPregunta(nuevaClasificacion); // Asume este método

        if (registrado) {
            JOptionPane.showMessageDialog(null, "Clasificación registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar la clasificación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarClasificaciones() {
        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
        if (clasificaciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clasificaciones de pregunta registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- Lista de Clasificaciones de Pregunta ---\n\n");
        for (ClasificacionPregunta cp : clasificaciones) {
            sb.append("ID: ").append(cp.getIdClasificacion()).append("\n");
            sb.append("Nombre: ").append(cp.getNombreClasificacion()).append("\n");
            sb.append("Descripción: ").append(cp.getDescripcion() != null && !cp.getDescripcion().isEmpty() ? cp.getDescripcion() : "N/A").append("\n");
            sb.append("Estado: ").append(cp.getEstado()).append("\n");
            sb.append("--------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Clasificaciones de Pregunta", JOptionPane.PLAIN_MESSAGE);
    }

    private static void modificarClasificacion() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID de la clasificación a modificar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idClasificacion;
        try {
            idClasificacion = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClasificacionPregunta clasificacionExistente = clasificacionPreguntaDAO.obtenerClasificacionPorId(idClasificacion);
        if (clasificacionExistente == null) {
            JOptionPane.showMessageDialog(null, "No se encontró una clasificación con el ID " + idClasificacion, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre (actual: " + clasificacionExistente.getNombreClasificacion() + "):", clasificacionExistente.getNombreClasificacion());
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar si el nuevo nombre ya existe en otra clasificación
        ClasificacionPregunta clasifConMismoNombre = clasificacionPreguntaDAO.obtenerClasificacionPorNombre(nuevoNombre);
        if (clasifConMismoNombre != null && clasifConMismoNombre.getIdClasificacion() != idClasificacion) {
            JOptionPane.showMessageDialog(null, "Ya existe otra clasificación con ese nuevo nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevaDescripcion = JOptionPane.showInputDialog("Nueva descripción (actual: " + (clasificacionExistente.getDescripcion() != null ? clasificacionExistente.getDescripcion() : "") + "):", clasificacionExistente.getDescripcion());
        if (nuevaDescripcion == null) nuevaDescripcion = "";

        String[] estados = {"ACTIVA", "INACTIVA"};
        String estadoActual = clasificacionExistente.getEstado();
        String nuevoEstado = (String) JOptionPane.showInputDialog(null, 
                "Nuevo estado (actual: " + estadoActual + "):", "Modificar Estado", 
                JOptionPane.QUESTION_MESSAGE, null, estados, estadoActual);
        if (nuevoEstado == null) return;

        clasificacionExistente.setNombreClasificacion(nuevoNombre);
        clasificacionExistente.setDescripcion(nuevaDescripcion);
        clasificacionExistente.setEstado(nuevoEstado);
        boolean actualizado = clasificacionPreguntaDAO.actualizarClasificacionPregunta(clasificacionExistente); // Asume este método

        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Clasificación modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar la clasificación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarClasificacion() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID de la clasificación a eliminar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idClasificacion;
        try {
            idClasificacion = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar la clasificación con ID " + idClasificacion + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = clasificacionPreguntaDAO.eliminarClasificacionPregunta(idClasificacion); // Asume este método
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Clasificación eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la clasificación. Asegúrese de que no esté en uso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}