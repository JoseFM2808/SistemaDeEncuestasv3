package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Acceso directo al DAO para simplicidad
import SteveJobs.encuestas.modelo.TipoPregunta;

import javax.swing.JOptionPane;
import java.util.List;

public class UIGestionTiposPregunta {

    private static TipoPreguntaDAO tipoPreguntaDAO = new TipoPreguntaDAO(); // Utiliza el DAO directamente

    public static void mostrarMenu() {
        int opcion;
        do {
            String menu = "--- Gestión de Tipos de Pregunta ---\n"
                    + "1. Registrar Nuevo Tipo de Pregunta\n"
                    + "2. Listar Todos los Tipos de Pregunta\n"
                    + "3. Modificar Tipo de Pregunta\n"
                    + "4. Eliminar Tipo de Pregunta\n"
                    + "0. Volver al Menú Principal";
            String input = JOptionPane.showInputDialog(null, menu, "Menú Tipos de Pregunta", JOptionPane.PLAIN_MESSAGE);

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
                    registrarTipoPregunta();
                    break;
                case 2:
                    listarTiposPregunta();
                    break;
                case 3:
                    modificarTipoPregunta();
                    break;
                case 4:
                    eliminarTipoPregunta();
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

    private static void registrarTipoPregunta() {
        String nombreTipo = JOptionPane.showInputDialog("Ingrese el nombre del nuevo tipo de pregunta:");
        if (nombreTipo == null || nombreTipo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre del tipo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar si el tipo ya existe
        if (tipoPreguntaDAO.obtenerTipoPreguntaPorNombre(nombreTipo) != null) {
            JOptionPane.showMessageDialog(null, "Ya existe un tipo de pregunta con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoPregunta nuevoTipo = new TipoPregunta(nombreTipo);
        boolean registrado = tipoPreguntaDAO.crearTipoPregunta(nuevoTipo); // Asume este método en TipoPreguntaDAO

        if (registrado) {
            JOptionPane.showMessageDialog(null, "Tipo de pregunta registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar el tipo de pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarTiposPregunta() {
        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTipos();
        if (tipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tipos de pregunta registrados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- Lista de Tipos de Pregunta ---\n\n");
        for (TipoPregunta tp : tipos) {
            sb.append("ID: ").append(tp.getIdTipoPregunta()).append("\n");
            sb.append("Nombre: ").append(tp.getNombreTipo()).append("\n");
            sb.append("--------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Tipos de Pregunta", JOptionPane.PLAIN_MESSAGE);
    }

    private static void modificarTipoPregunta() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID del tipo de pregunta a modificar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idTipo;
        try {
            idTipo = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoPregunta tipoExistente = tipoPreguntaDAO.obtenerTipoPreguntaPorId(idTipo);
        if (tipoExistente == null) {
            JOptionPane.showMessageDialog(null, "No se encontró un tipo de pregunta con el ID " + idTipo, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre para el tipo de pregunta (actual: " + tipoExistente.getNombreTipo() + "):", tipoExistente.getNombreTipo());
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar si el nuevo nombre ya existe en otro tipo
        TipoPregunta tipoConMismoNombre = tipoPreguntaDAO.obtenerTipoPreguntaPorNombre(nuevoNombre);
        if (tipoConMismoNombre != null && tipoConMismoNombre.getIdTipoPregunta() != idTipo) {
            JOptionPane.showMessageDialog(null, "Ya existe otro tipo de pregunta con ese nuevo nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tipoExistente.setNombreTipo(nuevoNombre);
        boolean actualizado = tipoPreguntaDAO.actualizarTipoPregunta(tipoExistente); // Asume este método en TipoPreguntaDAO

        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Tipo de pregunta modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar el tipo de pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarTipoPregunta() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID del tipo de pregunta a eliminar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idTipo;
        try {
            idTipo = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el tipo de pregunta con ID " + idTipo + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = tipoPreguntaDAO.eliminarTipoPregunta(idTipo); // Asume este método en TipoPreguntaDAO
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Tipo de pregunta eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el tipo de pregunta. Asegúrese de que no esté en uso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}