package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.servicio.ServicioPreguntas;
import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Necesario para obtener tipos de pregunta
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Necesario para obtener clasificaciones

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class UIGestionBancoPreguntas {

    private static ServicioPreguntas servicioPreguntas = new ServicioPreguntas();
    private static TipoPreguntaDAO tipoPreguntaDAO = new TipoPreguntaDAO(); // Instancia para obtener tipos
    private static ClasificacionPreguntaDAO clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // Instancia para obtener clasificaciones

    public static void mostrarMenu() {
        int opcion;
        do {
            String menu = "--- Gestión de Banco de Preguntas ---\n"
                    + "1. Registrar Nueva Pregunta\n"
                    + "2. Listar Todas las Preguntas\n"
                    + "3. Modificar Pregunta Existente\n"
                    + "4. Eliminar Pregunta\n"
                    + "0. Volver al Menú Principal";
            String input = JOptionPane.showInputDialog(null, menu, "Menú Banco de Preguntas", JOptionPane.PLAIN_MESSAGE);

            if (input == null) { // Si el usuario cierra el diálogo
                opcion = 0;
            } else {
                try {
                    opcion = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opción inválida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    opcion = -1; // Para que el bucle continúe
                }
            }

            switch (opcion) {
                case 1:
                    registrarPregunta();
                    break;
                case 2:
                    listarPreguntas();
                    break;
                case 3:
                    modificarPregunta();
                    break;
                case 4:
                    eliminarPregunta();
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Volviendo al Menú Principal del Administrador.", "Volver", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    if (opcion != -1) { // Evitar doble mensaje de error si ya se mostró en el parseo
                        JOptionPane.showMessageDialog(null, "Opción no reconocida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarPregunta() {
        String textoPregunta = JOptionPane.showInputDialog("Ingrese el texto de la nueva pregunta:");
        if (textoPregunta == null || textoPregunta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El texto de la pregunta no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTipos();
        if (tipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tipos de pregunta registrados. No se puede crear la pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] nombresTipos = tipos.stream().map(TipoPregunta::getNombreTipo).toArray(String[]::new);
        String tipoSeleccionado = (String) JOptionPane.showInputDialog(null,
                "Seleccione el tipo de pregunta:", "Tipo de Pregunta",
                JOptionPane.QUESTION_MESSAGE, null, nombresTipos, nombresTipos[0]);
        if (tipoSeleccionado == null) return; // Usuario canceló

        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
        String[] nombresClasificaciones = new String[clasificaciones.size() + 1];
        nombresClasificaciones[0] = "Ninguna"; // Opción para no clasificar
        for (int i = 0; i < clasificaciones.size(); i++) {
            nombresClasificaciones[i+1] = clasificaciones.get(i).getNombreClasificacion();
        }
        String clasificacionSeleccionada = (String) JOptionPane.showInputDialog(null,
                "Seleccione la clasificación (opcional):", "Clasificación de Pregunta",
                JOptionPane.QUESTION_MESSAGE, null, nombresClasificaciones, nombresClasificaciones[0]);
        if (clasificacionSeleccionada == null) return; // Usuario canceló

        boolean registrado = servicioPreguntas.registrarPreguntaEnBanco(
                textoPregunta, tipoSeleccionado, 
                "Ninguna".equals(clasificacionSeleccionada) ? null : clasificacionSeleccionada);

        if (registrado) {
            JOptionPane.showMessageDialog(null, "Pregunta registrada exitosamente en el banco.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar la pregunta. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarPreguntas() {
        List<PreguntaBanco> preguntas = servicioPreguntas.obtenerTodasLasPreguntasDelBanco();
        if (preguntas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay preguntas en el banco para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- Lista de Preguntas en el Banco ---\n\n");
        for (PreguntaBanco pb : preguntas) {
            sb.append("ID: ").append(pb.getIdPreguntaBanco()).append("\n");
            sb.append("Texto: ").append(pb.getTextoPregunta()).append("\n");
            sb.append("Tipo: ").append(pb.getNombreTipoPregunta()).append("\n");
            sb.append("Clasificación: ").append(pb.getNombreClasificacion() != null ? pb.getNombreClasificacion() : "N/A").append("\n");
            sb.append("--------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Banco de Preguntas", JOptionPane.PLAIN_MESSAGE);
    }

    private static void modificarPregunta() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID de la pregunta a modificar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idPregunta;
        try {
            idPregunta = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PreguntaBanco preguntaExistente = servicioPreguntas.obtenerPreguntaPorId(idPregunta);
        if (preguntaExistente == null) {
            JOptionPane.showMessageDialog(null, "No se encontró una pregunta con el ID " + idPregunta, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoTexto = JOptionPane.showInputDialog("Nuevo texto para la pregunta (actual: " + preguntaExistente.getTextoPregunta() + "):", preguntaExistente.getTextoPregunta());
        if (nuevoTexto == null) return; // Usuario canceló

        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTipos();
        String[] nombresTipos = tipos.stream().map(TipoPregunta::getNombreTipo).toArray(String[]::new);
        String tipoActual = preguntaExistente.getNombreTipoPregunta();
        String tipoSeleccionado = (String) JOptionPane.showInputDialog(null,
                "Seleccione el nuevo tipo de pregunta (actual: " + tipoActual + "):", "Modificar Tipo",
                JOptionPane.QUESTION_MESSAGE, null, nombresTipos, tipoActual);
        if (tipoSeleccionado == null) return; // Usuario canceló

        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
        String[] nombresClasificaciones = new String[clasificaciones.size() + 1];
        nombresClasificaciones[0] = "Ninguna"; // Opción para no clasificar
        for (int i = 0; i < clasificaciones.size(); i++) {
            nombresClasificaciones[i+1] = clasificaciones.get(i).getNombreClasificacion();
        }
        String clasificacionActual = preguntaExistente.getNombreClasificacion() != null ? preguntaExistente.getNombreClasificacion() : "Ninguna";
        String clasificacionSeleccionada = (String) JOptionPane.showInputDialog(null,
                "Seleccione la nueva clasificación (actual: " + clasificacionActual + "):", "Modificar Clasificación",
                JOptionPane.QUESTION_MESSAGE, null, nombresClasificaciones, clasificacionActual);
        if (clasificacionSeleccionada == null) return; // Usuario canceló

        boolean actualizado = servicioPreguntas.modificarPreguntaBanco(
                idPregunta, nuevoTexto, tipoSeleccionado, 
                "Ninguna".equals(clasificacionSeleccionada) ? null : clasificacionSeleccionada);

        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Pregunta modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar la pregunta. Verifique los datos o si la pregunta existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarPregunta() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID de la pregunta a eliminar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idPregunta;
        try {
            idPregunta = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar la pregunta con ID " + idPregunta + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = servicioPreguntas.eliminarPreguntaBanco(idPregunta);
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Pregunta eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la pregunta. Asegúrese de que no esté asociada a encuestas activas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}