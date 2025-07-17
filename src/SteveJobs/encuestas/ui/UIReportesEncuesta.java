package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.RespuestaUsuario; 
import SteveJobs.encuestas.modelo.Usuario; 
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioResultados;
import SteveJobs.encuestas.util.PilaNavegacion;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap; 

public class UIReportesEncuesta {

    private static ServicioResultados servicioResultados = new ServicioResultados();
    private static ServicioEncuestas servicioEncuestas = new ServicioEncuestas();

   
    public static void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            String[] opciones = {
                "Ver Reporte de Encuesta",
                "Volver al Menú Administrador"
            };
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gestión de Reportes de Encuestas",
                    "Admin: Reportes",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == null || seleccion.equals(opciones[1])) {
                if (!PilaNavegacion.instance.isEmpty()) {
                    PilaNavegacion.instance.pop();
                }
                salir = true;
                continue;
            }

            switch (seleccion) {
                case "Ver Reporte de Encuesta":
                    verReporteDeEncuestaUI();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    
    private static void verReporteDeEncuestaUI() {
        List<Encuesta> encuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();

        if (encuestas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay encuestas registradas para generar reportes.", "Sin Encuestas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcionesEncuestas = encuestas.stream()
                .map(e -> e.getIdEncuesta() + ": " + e.getNombre() + " (" + e.getEstado() + ")")
                .toArray(String[]::new);

        String seleccionEncuestaStr = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la encuesta para ver el reporte:",
                "Ver Reporte de Encuesta",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesEncuestas,
                opcionesEncuestas[0]
        );

        if (seleccionEncuestaStr == null) {
            return;
        }

        try {
            int idEncuestaSeleccionada = Integer.parseInt(seleccionEncuestaStr.split(":")[0]);

           
            String tipoPreguntaFiltro = null;
            String[] tiposDisponibles = {"SIMPLE", "MÚLTIPLE", "NUMERO", "TEXTO_CORTO", "FECHA", "DESCRIPCIÓN", "NINGUNO (ver todos)"};

            int confirmFilter = JOptionPane.showConfirmDialog(
                    null,
                    "¿Desea aplicar un filtro por tipo de pregunta al reporte?",
                    "Aplicar Filtro",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmFilter == JOptionPane.YES_OPTION) {
                String filtroSeleccionado = (String) JOptionPane.showInputDialog(
                        null,
                        "Seleccione el tipo de pregunta a filtrar (se mostrarán SOLO las de este tipo):",
                        "Filtro por Tipo de Pregunta",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        tiposDisponibles,
                        tiposDisponibles[0]
                );
                if (filtroSeleccionado != null && !filtroSeleccionado.equals("NINGUNO (ver todos)")) {
                    tipoPreguntaFiltro = filtroSeleccionado;
                }
            }

           
            Map<String, String> filtros = new HashMap<>();
            if (tipoPreguntaFiltro != null) {
                filtros.put("tipoPregunta", tipoPreguntaFiltro);
            }

            
            List<RespuestaUsuario> respuestasFiltradas = servicioResultados.filtrarResultados(idEncuestaSeleccionada, filtros);

           
            Map<String, Map<String, Integer>> reporteFrecuencia = servicioResultados.generarReporteFrecuenciaRespuestas(respuestasFiltradas);
            Map<String, Double> reportePromedios = servicioResultados.calcularPromediosPorPregunta(respuestasFiltradas);

            String reporteTexto = servicioResultados.exportarReporteATexto(reporteFrecuencia, reportePromedios);

            JTextArea textArea = new JTextArea(reporteTexto);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(
                    null,
                    scrollPane,
                    "Reporte de Encuesta ID: " + idEncuestaSeleccionada + (tipoPreguntaFiltro != null ? " (Filtrado por: " + tipoPreguntaFiltro + ")" : ""),
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar la selección de encuesta.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al generar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
