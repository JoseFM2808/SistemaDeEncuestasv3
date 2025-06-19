// Nuevo archivo: SteveJobs.encuestas.ui.UIGestionBancoPreguntas.java
package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.servicio.ServicioPreguntas; // Servicio de Pablo
import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Para listar tipos en UI
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Para listar clasificaciones en UI

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UIGestionBancoPreguntas {

    private static ServicioPreguntas servicioPreguntas = new ServicioPreguntas(); // Servicio de Pablo
    private static TipoPreguntaDAO tipoPreguntaDAO = new TipoPreguntaDAO();
    private static ClasificacionPreguntaDAO clasificacionPreguntaDAO = new ClasificacionPreguntaDAO();

    public static void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            String[] opciones = {
                "Listar Preguntas del Banco",
                "Agregar Nueva Pregunta al Banco",
                "Modificar Pregunta del Banco",
                "Eliminar Pregunta del Banco",
                "Volver al Menú Administrador"
            };
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gestión del Banco de Preguntas",
                    "Admin: Banco de Preguntas",
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
                case "Listar Preguntas del Banco":
                    listarPreguntasBancoUI();
                    break;
                case "Agregar Nueva Pregunta al Banco":
                    agregarPreguntaBancoUI();
                    break;
                case "Modificar Pregunta del Banco":
                    modificarPreguntaBancoUI();
                    break;
                case "Eliminar Pregunta del Banco":
                    eliminarPreguntaBancoUI();
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

    private static void listarPreguntasBancoUI() {
        String filtroTexto = JOptionPane.showInputDialog(null, "Filtrar por texto (dejar vacío para todos):", "Filtrar Preguntas", JOptionPane.PLAIN_MESSAGE);
        if (filtroTexto == null) filtroTexto = "";

        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTiposPregunta();
        String[] nombresTipos = tipos.stream().map(TipoPregunta::getNombreTipo).toArray(String[]::new);
        String filtroTipoNombre = (String) JOptionPane.showInputDialog(null, "Filtrar por tipo (opcional):", "Filtrar Preguntas",
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"<Todos>"}, "<Todos>");
        if ("<Todos>".equals(filtroTipoNombre)) filtroTipoNombre = null;

        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
        String[] nombresClasificaciones = clasificaciones.stream()
                .map(c -> c.getIdClasificacion() + ": " + c.getNombreClasificacion())
                .collect(Collectors.toList())
                .toArray(new String[0]);
        String filtroClasifStr = (String) JOptionPane.showInputDialog(null, "Filtrar por clasificación (opcional):", "Filtrar Preguntas",
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"<Todas>"}, "<Todas>");
        Integer filtroClasificacionId = null;
        if (filtroClasifStr != null && !"<Todas>".equals(filtroClasifStr)) {
            try {
                filtroClasificacionId = Integer.parseInt(filtroClasifStr.split(":")[0]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Clasificación inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        List<PreguntaBanco> preguntas = servicioPreguntas.listarPreguntasDelBancoConFiltro(filtroTexto, filtroTipoNombre, filtroClasificacionId);
        
        if (preguntas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron preguntas en el banco con los filtros aplicados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Preguntas en el Banco:\n\n");
        for (PreguntaBanco pb : preguntas) {
            sb.append("ID: ").append(pb.getIdPreguntaBanco())
              .append(" - Texto: ").append(pb.getTextoPregunta())
              .append("\n  Tipo: ").append(pb.getNombreTipoPregunta() != null ? pb.getNombreTipoPregunta() : pb.getIdTipoPregunta())
              .append(" - Clasif: ").append(pb.getNombreClasificacion() != null ? pb.getNombreClasificacion() : "N/A")
              .append("\n  Opciones: ").append(pb.getOpciones() != null && !pb.getOpciones().isEmpty() ? pb.getOpciones() : "N/A")
              .append("\n------------------------------------\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
        JOptionPane.showMessageDialog(null, scrollPane, "Listado de Preguntas del Banco", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void agregarPreguntaBancoUI() {
        String texto = JOptionPane.showInputDialog(null, "Texto de la pregunta:", "Agregar Pregunta Banco", JOptionPane.PLAIN_MESSAGE);
        if (texto == null || texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El texto no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTiposPregunta();
        if (tipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tipos de pregunta definidos. Por favor, agregue tipos primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] opcionesTipos = tipos.stream().map(t -> t.getIdTipoPregunta() + ": " + t.getNombreTipo()).toArray(String[]::new);
        String tipoSeleccionadoStr = (String) JOptionPane.showInputDialog(null, "Seleccione el Tipo de Pregunta:", "Agregar Pregunta Banco",
                JOptionPane.QUESTION_MESSAGE, null, opcionesTipos, opcionesTipos[0]);
        if (tipoSeleccionadoStr == null) return;
        int idTipo = Integer.parseInt(tipoSeleccionadoStr.split(":")[0]);

        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
        String[] opcionesClasif = clasificaciones.stream().map(c -> c.getIdClasificacion() + ": " + c.getNombreClasificacion()).toArray(String[]::new);
        String clasifSeleccionadaStr = (String) JOptionPane.showInputDialog(null, "Seleccione la Clasificación (opcional):", "Agregar Pregunta Banco",
                JOptionPane.QUESTION_MESSAGE, null, opcionesClasif, null); // Null para permitir "ninguno"
        Integer idClasificacion = null;
        if (clasifSeleccionadaStr != null) {
            try {
                idClasificacion = Integer.parseInt(clasifSeleccionadaStr.split(":")[0]);
            } catch (NumberFormatException e) {
                // Si el usuario cancela o selecciona algo que no es un ID válido, se mantiene null
            }
        }
        
        String opcionesPosibles = null;
        // Asumiendo que solo los tipos de seleccion necesitan opciones. Puedes refinar esta logica.
        if (tipoSeleccionadoStr.toLowerCase().contains("seleccion")) { // Puedes usar una lógica más robusta aquí (ej. consultar el tipo real)
             opcionesPosibles = JOptionPane.showInputDialog(null, "Opciones posibles (separadas por ';', ej. 'Si;No'):", "Agregar Pregunta Banco", JOptionPane.PLAIN_MESSAGE);
             if (opcionesPosibles == null) opcionesPosibles = "";
        }


        int idNuevaPregunta = servicioPreguntas.crearNuevaPreguntaBanco(texto.trim(), idTipo, idClasificacion, opcionesPosibles);

        if (idNuevaPregunta != -1) {
            JOptionPane.showMessageDialog(null, "Pregunta agregada al banco con ID: " + idNuevaPregunta + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al agregar la pregunta al banco. Verifique la consola.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void modificarPreguntaBancoUI() {
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID de la pregunta del banco a modificar:", "Modificar Pregunta Banco", JOptionPane.PLAIN_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PreguntaBanco preguntaExistente = servicioPreguntas.obtenerPreguntaBancoPorId(id);
        if (preguntaExistente == null) {
            JOptionPane.showMessageDialog(null, "Pregunta con ID " + id + " no encontrada en el banco.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoTexto = JOptionPane.showInputDialog(null, "Nuevo texto (actual: " + preguntaExistente.getTextoPregunta() + "):", "Modificar Pregunta Banco", JOptionPane.PLAIN_MESSAGE);
        if (nuevoTexto == null || nuevoTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El texto no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTiposPregunta();
        if (tipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tipos de pregunta definidos. No se puede modificar el tipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] opcionesTipos = tipos.stream().map(t -> t.getIdTipoPregunta() + ": " + t.getNombreTipo()).toArray(String[]::new);
        String tipoActualStr = preguntaExistente.getIdTipoPregunta() + ": " + preguntaExistente.getNombreTipoPregunta();
        String nuevoTipoSeleccionadoStr = (String) JOptionPane.showInputDialog(null, "Nuevo Tipo de Pregunta (actual: " + tipoActualStr + "):", "Modificar Pregunta Banco",
                JOptionPane.QUESTION_MESSAGE, null, opcionesTipos, tipoActualStr);
        if (nuevoTipoSeleccionadoStr == null) return;
        int nuevoIdTipo = Integer.parseInt(nuevoTipoSeleccionadoStr.split(":")[0]);


        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
        String[] opcionesClasif = clasificaciones.stream().map(c -> c.getIdClasificacion() + ": " + c.getNombreClasificacion()).collect(Collectors.toList()).toArray(new String[0]);
        String clasifActualStr = (preguntaExistente.getIdClasificacion() != null && preguntaExistente.getIdClasificacion() > 0) ? 
                                   preguntaExistente.getIdClasificacion() + ": " + preguntaExistente.getNombreClasificacion() : "<Ninguna>";

        String nuevaClasifSeleccionadaStr = (String) JOptionPane.showInputDialog(null, "Nueva Clasificación (actual: " + clasifActualStr + "):", "Modificar Pregunta Banco",
                JOptionPane.QUESTION_MESSAGE, null, opcionesClasif, clasifActualStr);
        Integer nuevoIdClasificacion = null;
        if (nuevaClasifSeleccionadaStr != null && !"<Ninguna>".equals(nuevaClasifSeleccionadaStr)) {
            try {
                nuevoIdClasificacion = Integer.parseInt(nuevaClasifSeleccionadaStr.split(":")[0]);
            } catch (NumberFormatException e) {
                // Si el usuario cancela o selecciona algo que no es un ID válido, se mantiene null
            }
        }
        
        String nuevasOpciones = preguntaExistente.getOpciones();
        // Lógica similar para pedir opciones solo si el nuevo tipo lo requiere
        if (nuevoTipoSeleccionadoStr.toLowerCase().contains("seleccion")) { // Puedes refinar esto
            nuevasOpciones = JOptionPane.showInputDialog(null, "Nuevas opciones (actual: " + (preguntaExistente.getOpciones() != null ? preguntaExistente.getOpciones() : "") + "):", "Modificar Pregunta Banco", JOptionPane.PLAIN_MESSAGE);
            if (nuevasOpciones == null) nuevasOpciones = "";
        } else {
            nuevasOpciones = null; // Si el tipo ya no requiere opciones, las limpia
        }


        if (servicioPreguntas.actualizarPreguntaBanco(id, nuevoTexto.trim(), nuevoIdTipo, nuevoIdClasificacion, nuevasOpciones)) {
            JOptionPane.showMessageDialog(null, "Pregunta del banco modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar la pregunta del banco. Verifique la consola.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarPreguntaBancoUI() {
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID de la pregunta del banco a eliminar:", "Eliminar Pregunta Banco", JOptionPane.PLAIN_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar la pregunta con ID " + id + " del banco?\n¡Esto podría afectar encuestas existentes!", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (servicioPreguntas.eliminarPreguntaBanco(id)) {
                JOptionPane.showMessageDialog(null, "Pregunta del banco eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la pregunta del banco. Puede que esté asociada a encuestas o haya un error en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}