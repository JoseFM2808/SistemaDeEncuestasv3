package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.util.PilaNavegacion; // Importar PilaNavegacion

import javax.swing.JOptionPane;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension; // Importar Dimension para setPreferredSize

public class UIGestionEncuestas {

    private static ServicioEncuestas servicioEncuestas = new ServicioEncuestas();
    private static Usuario adminLogueado;

    public static void mostrarMenu(Usuario admin) {
        adminLogueado = admin;
        boolean salir = false;
        while (!salir) {
            String[] opciones = {
                "Crear Nueva Encuesta",
                "Listar Todas las Encuestas",
                "Ver/Modificar Detalles de Encuesta",
                "Configurar Preguntas de Encuesta",
                "Cambiar Estado de Encuesta",
                "Copiar Encuesta",
                "Eliminar Encuesta",
                "Buscar Encuesta por ID",
                "Volver al Menú Principal"
            };
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gestión de Encuestas",
                    "Admin: Módulo de Encuestas",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == null || seleccion.equals(opciones[8])) {
                if (PilaNavegacion.instance != null && !PilaNavegacion.instance.isEmpty()) {
                    PilaNavegacion.instance.pop();
                }
                salir = true;
                continue;
            }

            try {
                switch (seleccion) {
                    case "Crear Nueva Encuesta":
                        crearEncuestaUI();
                        break;
                    case "Listar Todas las Encuestas":
                        listarEncuestasUI();
                        break;
                    case "Ver/Modificar Detalles de Encuesta":
                        verOModificarDetallesEncuestaUI();
                        break;
                    case "Configurar Preguntas de Encuesta":
                        configurarPreguntasDeUnaEncuestaUI();
                        break;
                    case "Cambiar Estado de Encuesta":
                        cambiarEstadoEncuestaUI();
                        break;
                    case "Copiar Encuesta":
                        copiarEncuestaUI();
                        break;
                    case "Eliminar Encuesta":
                        eliminarEncuestaUI();
                        break;
                    case "Buscar Encuesta por ID":
                        buscarEncuestaPorIdUI();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private static Timestamp obtenerFechaDesdeJOptionPane(String mensajeDialogo, String tituloDialogo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        String fechaStr = JOptionPane.showInputDialog(null, mensajeDialogo + " (Formato: YYYY-MM-DD HH:MM:SS)", tituloDialogo, JOptionPane.PLAIN_MESSAGE);
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }
        try {
            java.util.Date parsedDate = sdf.parse(fechaStr.trim());
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha y hora inválido. Use YYYY-MM-DD HH:MM:SS", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static void crearEncuestaUI() {
        String nombre = JOptionPane.showInputDialog(null, "Nombre de la nueva encuesta:", "Crear Encuesta", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) return;

        String descripcion = JOptionPane.showInputDialog(null, "Descripción:", "Crear Encuesta", JOptionPane.PLAIN_MESSAGE);
        
        Timestamp fechaInicio = obtenerFechaDesdeJOptionPane("Fecha de Inicio:", "Crear Encuesta");
        if (fechaInicio == null) return;

        Timestamp fechaFin = obtenerFechaDesdeJOptionPane("Fecha de Fin:", "Crear Encuesta");
        if (fechaFin == null) return;

        // --- Validar fechas antes de llamar al servicio ---
        if (fechaFin.before(fechaInicio)) {
            JOptionPane.showMessageDialog(null, "La fecha de fin no puede ser anterior a la de inicio.", "Error de Fechas", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // CAMBIADO: Captura de 'esPublica' como boolean con JOptionPane.showConfirmDialog
        int confirmPublica = JOptionPane.showConfirmDialog(null, 
                "¿Es esta encuesta pública (abierta a todos los usuarios)?", 
                "Tipo de Público", 
                JOptionPane.YES_NO_OPTION);
        boolean esPublica = (confirmPublica == JOptionPane.YES_OPTION);

        String definicionPerfil = null;
        if (!esPublica) { // Solo pedir perfil si NO es pública
            String definicionPerfilInput = JOptionPane.showInputDialog(null, 
                    "Definición del perfil del encuestado (ej. GENERO:Femenino;EDAD:>=25):", 
                    "Crear Encuesta - Perfil", JOptionPane.PLAIN_MESSAGE);
            definicionPerfil = (definicionPerfilInput != null && !definicionPerfilInput.trim().isEmpty()) ? definicionPerfilInput.trim() : null;
            
            if (definicionPerfil == null) { // Si no es pública y cancela o deja vacío el perfil
                JOptionPane.showMessageDialog(null, "Si la encuesta no es pública, debe definir un perfil requerido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        // Si es pública, definicionPerfil se mantiene en null.

        int idAdmin = (adminLogueado != null) ? adminLogueado.getId_usuario() : 0;
        
        // CAMBIADO: Pasa `esPublica` (boolean)
        int idNuevaEncuesta = servicioEncuestas.registrarNuevaEncuesta(nombre.trim(), descripcion, fechaInicio, fechaFin, esPublica, definicionPerfil, idAdmin);

        if (idNuevaEncuesta != -1) {
            JOptionPane.showMessageDialog(null, "Encuesta '" + nombre + "' creada con ID: " + idNuevaEncuesta + "\nEstado: Borrador. Proceda a configurar sus preguntas.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al crear la encuesta. Verifique los datos o consulte la consola para más detalles.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarEncuestasUI() {
        List<Encuesta> encuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();
        if (encuestas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay encuestas registradas.", "Listar Encuestas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Encuestas Registradas (Orden Alfabético):\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // Formato para fechas completas

        for (Encuesta e : encuestas) {
            sb.append("ID: ").append(e.getIdEncuesta())
              .append(" - Nombre: ").append(e.getNombre())
              .append(" - Estado: ").append(e.getEstado())
              .append("\n  Vigencia: ").append(e.getFechaInicio() != null ? sdf.format(e.getFechaInicio()) : "N/A")
              .append(" a ").append(e.getFechaFin() != null ? sdf.format(e.getFechaFin()) : "N/A");
            
            // CAMBIADO: Muestra "Sí" o "No" para `esPublica`
            sb.append("\n  Es Pública: ").append(e.isEsPublica() ? "Sí" : "No");
            
            // Si el perfil requerido no es nulo, lo mostramos
            if (e.getPerfilRequerido() != null && !e.getPerfilRequerido().trim().isEmpty()) {
                sb.append("\n  Perfil Requerido: ").append(e.getPerfilRequerido());
            }
            sb.append("\n------------------------------------\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 300)); // Usar java.awt.Dimension
        JOptionPane.showMessageDialog(null, scrollPane, "Listado de Encuestas", JOptionPane.INFORMATION_MESSAGE);
    }

    private static Encuesta seleccionarEncuestaParaAccion(String accion) {
        List<Encuesta> encuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();
        if (encuestas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay encuestas para " + accion + ".", "Error", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String[] opcionesEncuestas = encuestas.stream()
                .map(e -> e.getIdEncuesta() + ": " + e.getNombre() + " (" + e.getEstado() + ")")
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione la encuesta para " + accion + ":",
                "Seleccionar Encuesta", JOptionPane.QUESTION_MESSAGE, null, opcionesEncuestas, opcionesEncuestas[0]);

        if (seleccion != null) {
            int idEncuestaSeleccionada = Integer.parseInt(seleccion.split(":")[0]);
            return servicioEncuestas.obtenerDetallesCompletosEncuesta(idEncuestaSeleccionada);
        }
        return null;
    }

    private static void verOModificarDetallesEncuestaUI() {
        Encuesta encuesta = seleccionarEncuestaParaAccion("ver/modificar detalles");
        if (encuesta == null) return;

        StringBuilder detalles = new StringBuilder();
        detalles.append("ID: ").append(encuesta.getIdEncuesta()).append("\n");
        detalles.append("Nombre: ").append(encuesta.getNombre()).append("\n");
        detalles.append("Descripción: ").append(encuesta.getDescripcion()).append("\n");
        detalles.append("Inicio Vigencia: ").append(encuesta.getFechaInicio()).append("\n");
        detalles.append("Fin Vigencia: ").append(encuesta.getFechaFin()).append("\n");
        // CAMBIADO: Muestra "Sí" o "No" para `esPublica`
        detalles.append("Es Pública: ").append(encuesta.isEsPublica() ? "Sí" : "No").append("\n");
        detalles.append("Perfil Requerido: ").append(encuesta.getPerfilRequerido() != null ? encuesta.getPerfilRequerido() : "(No definido)").append("\n");
        detalles.append("Estado: ").append(encuesta.getEstado()).append("\n");
        detalles.append("Fecha Creación: ").append(encuesta.getFechaCreacion()).append("\n");
        detalles.append("ID Admin Creador: ").append(encuesta.getIdAdminCreador()).append("\n");


        JTextArea textArea = new JTextArea(detalles.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setPreferredSize(new Dimension(500, 300)); // Usar java.awt.Dimension
        
        int opcion = JOptionPane.showConfirmDialog(null, scrollPane, "Detalles Encuesta ID: " + encuesta.getIdEncuesta() + " - ¿Modificar?", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            String nuevoNombre = JOptionPane.showInputDialog(null, "Nuevo nombre:", encuesta.getNombre());
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) { JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE); return; }

            String nuevaDesc = JOptionPane.showInputDialog(null, "Nueva descripción:", encuesta.getDescripcion());
            
            Timestamp nuevaFechaInicio = obtenerFechaDesdeJOptionPane("Nueva Fecha de Inicio (actual: " + encuesta.getFechaInicio() + "):", "Modificar Encuesta");
            if (nuevaFechaInicio == null) nuevaFechaInicio = encuesta.getFechaInicio(); // Si cancela o inválido, mantiene la actual

            Timestamp nuevaFechaFin = obtenerFechaDesdeJOptionPane("Nueva Fecha de Fin (actual: " + encuesta.getFechaFin() + "):", "Modificar Encuesta");
            if (nuevaFechaFin == null) nuevaFechaFin = encuesta.getFechaFin(); // Si cancela o inválido, mantiene la actual
            
            // Validar fechas después de la nueva captura
            if (nuevaFechaFin.before(nuevaFechaInicio)) {
                JOptionPane.showMessageDialog(null, "La nueva fecha de fin no puede ser anterior a la nueva fecha de inicio.", "Error de Fechas", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CAMBIADO: Captura de 'nuevaEsPublica' como boolean
            int confirmNuevaPublica = JOptionPane.showConfirmDialog(null, 
                    "¿Es esta encuesta ahora pública (abierta a todos los usuarios)? (Actual: " + (encuesta.isEsPublica() ? "Sí" : "No") + ")", 
                    "Modificar Encuesta - Tipo de Público", 
                    JOptionPane.YES_NO_OPTION);
            boolean nuevaEsPublica = (confirmNuevaPublica == JOptionPane.YES_OPTION);


            String nuevoPerfilDefInput = null;
            if (!nuevaEsPublica) { // Solo pedir perfil si NO es pública
                nuevoPerfilDefInput = JOptionPane.showInputDialog(null, 
                        "Nueva definición del perfil (actual: " + (encuesta.getPerfilRequerido() != null ? encuesta.getPerfilRequerido() : "(No definido)") + "):", 
                        "Modificar Encuesta - Perfil", JOptionPane.PLAIN_MESSAGE);
                if (nuevoPerfilDefInput == null || nuevoPerfilDefInput.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Si la encuesta no es pública, debe definir un perfil requerido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            String nuevoPerfilDef = (nuevoPerfilDefInput != null && !nuevoPerfilDefInput.trim().isEmpty()) ? nuevoPerfilDefInput.trim() : null;


            if (servicioEncuestas.modificarMetadatosEncuesta(encuesta.getIdEncuesta(), nuevoNombre.trim(), nuevaDesc,
                    nuevaFechaInicio, nuevaFechaFin,
                    nuevaEsPublica, nuevoPerfilDef)) { // CAMBIADO: Pasa `nuevaEsPublica` (boolean)
                JOptionPane.showMessageDialog(null, "Encuesta actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar la encuesta. Verifique los datos o consulte la consola.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static void configurarPreguntasDeUnaEncuestaUI() {
        Encuesta encuesta = seleccionarEncuestaParaAccion("configurar preguntas");
        if (encuesta == null) return;

        if ("Activa".equalsIgnoreCase(encuesta.getEstado()) || "Cerrada".equalsIgnoreCase(encuesta.getEstado())) {
            JOptionPane.showMessageDialog(null, "No se pueden configurar preguntas de una encuesta que está Activa o Cerrada.", "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UIConfigurarPreguntasEncuesta.mostrarMenuConfiguracion(encuesta.getIdEncuesta());
    }

    private static void cambiarEstadoEncuestaUI() {
        Encuesta encuesta = seleccionarEncuestaParaAccion("cambiar estado");
        if (encuesta == null) return;

        String[] estadosPosibles = {"Borrador", "Activa", "Cerrada", "Cancelada", "Archivada"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(null, "Encuesta: " + encuesta.getNombre() + "\nEstado actual: " + encuesta.getEstado() + "\nSeleccione el nuevo estado:",
                "Cambiar Estado de Encuesta", JOptionPane.QUESTION_MESSAGE, null, estadosPosibles, encuesta.getEstado());

        if (nuevoEstado != null && !nuevoEstado.equals(encuesta.getEstado())) {
            if (servicioEncuestas.cambiarEstadoEncuesta(encuesta.getIdEncuesta(), nuevoEstado)) {
                JOptionPane.showMessageDialog(null, "Estado de la encuesta actualizado a " + nuevoEstado + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al cambiar el estado. Verifique si cumple condiciones (ej. 12 preguntas para activar).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static void copiarEncuestaUI() {
        Encuesta encuestaOriginal = seleccionarEncuestaParaAccion("copiar");
        if (encuestaOriginal == null) return;

        int idAdmin = (adminLogueado != null) ? adminLogueado.getId_usuario() : 0;
        Encuesta encuestaCopiada = servicioEncuestas.copiarEncuesta(encuestaOriginal.getIdEncuesta(), idAdmin);

        if (encuestaCopiada != null) {
            JOptionPane.showMessageDialog(null, "Encuesta copiada exitosamente con ID: " + encuestaCopiada.getIdEncuesta() + "\nNuevo nombre: " + encuestaCopiada.getNombre(), "Copia Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al copiar la encuesta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarEncuestaUI() {
        Encuesta encuesta = seleccionarEncuestaParaAccion("eliminar");
        if (encuesta == null) return;

        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar la encuesta '" + encuesta.getNombre() + "' (ID: " + encuesta.getIdEncuesta() + ")?\n¡Esto eliminará sus preguntas configuradas y todas las respuestas de usuarios!",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (servicioEncuestas.eliminarEncuesta(encuesta.getIdEncuesta())) {
                JOptionPane.showMessageDialog(null, "Encuesta eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la encuesta. Podría tener datos asociados que impiden su borrado directo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void buscarEncuestaPorIdUI() {
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID de la encuesta a buscar:", "Buscar Encuesta por ID", JOptionPane.PLAIN_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) {
            return; // Usuario canceló o no ingresó nada
        }

        try {
            int idBuscado = Integer.parseInt(idStr.trim());
            List<Encuesta> todasLasEncuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();
            Encuesta encuestaEncontrada = servicioEncuestas.buscarEncuestaEnListaPorId(todasLasEncuestas, idBuscado);

            if (encuestaEncontrada != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("Encuesta Encontrada:\n\n");
                detalles.append("ID: ").append(encuestaEncontrada.getIdEncuesta()).append("\n");
                detalles.append("Nombre: ").append(encuestaEncontrada.getNombre()).append("\n");
                detalles.append("Descripción: ").append(encuestaEncontrada.getDescripcion()).append("\n");
                detalles.append("Estado: ").append(encuestaEncontrada.getEstado()).append("\n");
                detalles.append("Fecha Inicio: ").append(encuestaEncontrada.getFechaInicio()).append("\n");
                detalles.append("Fecha Fin: ").append(encuestaEncontrada.getFechaFin()).append("\n");
                // CAMBIADO: Muestra "Sí" o "No" para `esPublica`
                detalles.append("Es Pública: ").append(encuestaEncontrada.isEsPublica() ? "Sí" : "No").append("\n");
                detalles.append("Perfil Requerido: ").append(encuestaEncontrada.getPerfilRequerido() != null ? encuestaEncontrada.getPerfilRequerido() : "(No definido)").append("\n");

                JTextArea textArea = new JTextArea(detalles.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 200)); // Usar java.awt.Dimension
                JOptionPane.showMessageDialog(null, scrollPane, "Detalles de Encuesta ID: " + idBuscado, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Encuesta con ID " + idBuscado + " no encontrada.", "Búsqueda sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID ingresado no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado durante la búsqueda: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}