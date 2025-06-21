/*
 * Autores del Módulo:
 * - José Flores
 *
 * Responsabilidad Principal:
 * - Lógica de generación de resultados de encuestas
 * - Implementación de REQMS-004, REQMS-005, REQMS-006 (parcialmente)
 * - Implementación completada por Asistente de AED.
 */
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.dao.EncuestaDAO;
import SteveJobs.encuestas.dao.EncuestaDetallePreguntaDAO;
import SteveJobs.encuestas.dao.PreguntaBancoDAO;
import SteveJobs.encuestas.dao.RespuestaUsuarioDAO;
import SteveJobs.encuestas.dao.TipoPreguntaDAO;
import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.algoritmos.DetectorPatronesFrecuencia;
import SteveJobs.encuestas.algoritmos.CalculadoraEstadisticasBasicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioResultados {

    private EncuestaDAO encuestaDAO;
    private EncuestaDetallePreguntaDAO encuestaDetalleDAO;
    private RespuestaUsuarioDAO respuestaUsuarioDAO;
    private TipoPreguntaDAO tipoPreguntaDAO;
    private PreguntaBancoDAO preguntaBancoDAO;
    private DetectorPatronesFrecuencia detectorFrecuencia;
    private CalculadoraEstadisticasBasicas calculadoraEstadisticas;

    public ServicioResultados() {
        this.encuestaDAO = new EncuestaDAO();
        this.encuestaDetalleDAO = new EncuestaDetallePreguntaDAO();
        this.respuestaUsuarioDAO = new RespuestaUsuarioDAO();
        this.tipoPreguntaDAO = new TipoPreguntaDAO();
        this.preguntaBancoDAO = new PreguntaBancoDAO();
        this.detectorFrecuencia = new DetectorPatronesFrecuencia();
        this.calculadoraEstadisticas = new CalculadoraEstadisticasBasicas();
    }

    /**
     * Genera un reporte consolidado de resultados para una encuesta específica,
     * calculando la frecuencia de respuestas para preguntas no descriptivas.
     * (Implementa REQMS-004)
     *
     * @param idEncuesta El ID de la encuesta para la cual generar el reporte.
     * @return Un mapa donde la clave es el texto de la pregunta y el valor es otro mapa
     * con las frecuencias de cada respuesta para esa pregunta.
     * Excluye preguntas de tipo "Descripción" o "TEXTO_CORTO" de la frecuencia.
     * Retorna un mapa vacío si no se encuentra la encuesta o no hay preguntas válidas.
     */
    public Map<String, Map<String, Integer>> generarReporteFrecuenciaRespuestas(int idEncuesta) {
        Map<String, Map<String, Integer>> reporteConsolidado = new HashMap<>();

        Encuesta encuesta = encuestaDAO.obtenerEncuestaPorId(idEncuesta);
        if (encuesta == null) {
            System.err.println("ServicioResultados: Encuesta con ID " + idEncuesta + " no encontrada.");
            return reporteConsolidado;
        }

        List<EncuestaDetallePregunta> preguntasDetalle = encuestaDetalleDAO.obtenerPreguntasPorEncuesta(idEncuesta);

        for (EncuestaDetallePregunta edp : preguntasDetalle) {
            // Determinar el texto de la pregunta
            String textoPregunta = edp.getTextoPreguntaMostrable();
            String tipoRespuesta = ""; // Usaremos esto para filtrar preguntas descriptivas

            // Obtener el tipo de pregunta para filtrar respuestas descriptivas
            if (edp.getIdPreguntaBanco() != null) { // Pregunta del banco
                PreguntaBanco pb = preguntaBancoDAO.obtenerPreguntaPorId(edp.getIdPreguntaBanco());
                if (pb != null) {
                    TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(pb.getIdTipoPregunta());
                    if (tp != null) {
                        tipoRespuesta = tp.getNombreTipo();
                    }
                }
            } else if (edp.getIdTipoPreguntaUnica() != null) { // Pregunta única de la encuesta
                TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(edp.getIdTipoPreguntaUnica());
                if (tp != null) {
                    tipoRespuesta = tp.getNombreTipo();
                }
            }

            // Excluir preguntas descriptivas o de texto_corto para el reporte de frecuencia
            // Asumo que "Descripción" o "TEXTO_CORTO" son los tipos que no queremos en frecuencia.
            if ("Descripción".equalsIgnoreCase(tipoRespuesta) || "TEXTO_CORTO".equalsIgnoreCase(tipoRespuesta)) {
                System.out.println("ServicioResultados: Excluyendo pregunta '" + textoPregunta + "' (Tipo: " + tipoRespuesta + ") del reporte de frecuencia.");
                // Podríamos añadir las respuestas descriptivas aquí como "Respuestas libres", pero para frecuencia las omitimos.
                continue;
            }

            // Obtener todas las respuestas para esta pregunta específica
            List<RespuestaUsuario> respuestasCrudas = respuestaUsuarioDAO.obtenerRespuestasPorDetallePregunta(edp.getIdEncuestaDetalle());

            // Extraer solo los valores de respuesta para el algoritmo de frecuencia
            List<String> valoresRespuestas = respuestasCrudas.stream()
                                                            .map(RespuestaUsuario::getValorRespuesta)
                                                            .collect(Collectors.toList());

            if (!valoresRespuestas.isEmpty()) {
                // Calcular la frecuencia usando el algoritmo DetectorPatronesFrecuencia
                Map<String, Integer> frecuencias = detectorFrecuencia.calcularFrecuenciaRespuestas(valoresRespuestas);
                reporteConsolidado.put(textoPregunta, frecuencias);
            }
        }
        return reporteConsolidado;
    }

    /**
     * Calcula los promedios para preguntas numéricas en una encuesta.
     * (Requiere que las respuestas numéricas estén bien tipificadas o convertibles)
     *
     * @param idEncuesta El ID de la encuesta.
     * @return Un mapa donde la clave es el texto de la pregunta y el valor es el promedio.
     */
    public Map<String, Double> calcularPromediosPorPregunta(int idEncuesta) {
        Map<String, Double> promedios = new HashMap<>();

        Encuesta encuesta = encuestaDAO.obtenerEncuestaPorId(idEncuesta);
        if (encuesta == null) {
            System.err.println("ServicioResultados: Encuesta con ID " + idEncuesta + " no encontrada para promedios.");
            return promedios;
        }

        List<EncuestaDetallePregunta> preguntasDetalle = encuestaDetalleDAO.obtenerPreguntasPorEncuesta(idEncuesta);

        for (EncuestaDetallePregunta edp : preguntasDetalle) {
            String textoPregunta = edp.getTextoPreguntaMostrable();
            String tipoRespuesta = "";

            // Obtener el tipo de pregunta para filtrar numéricas
            if (edp.getIdPreguntaBanco() != null) {
                PreguntaBanco pb = preguntaBancoDAO.obtenerPreguntaPorId(edp.getIdPreguntaBanco());
                if (pb != null) {
                    TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(pb.getIdTipoPregunta());
                    if (tp != null) tipoRespuesta = tp.getNombreTipo();
                }
            } else if (edp.getIdTipoPreguntaUnica() != null) {
                TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(edp.getIdTipoPreguntaUnica());
                if (tp != null) tipoRespuesta = tp.getNombreTipo();
            }

            if ("NUMERO".equalsIgnoreCase(tipoRespuesta)) {
                List<RespuestaUsuario> respuestasCrudas = respuestaUsuarioDAO.obtenerRespuestasPorDetallePregunta(edp.getIdEncuestaDetalle());
                List<Double> valoresNumericos = new ArrayList<>();

                for (RespuestaUsuario r : respuestasCrudas) {
                    try {
                        valoresNumericos.add(Double.parseDouble(r.getValorRespuesta()));
                    } catch (NumberFormatException e) {
                        System.err.println("ServicioResultados: Respuesta no numérica encontrada para pregunta numérica: " + r.getValorRespuesta());
                    }
                }
                if (!valoresNumericos.isEmpty()) {
                    promedios.put(textoPregunta, calculadoraEstadisticas.calcularPromedio(valoresNumericos));
                }
            }
        }
        return promedios;
    }

    /**
     * (PENDIENTE) Filtra los resultados de un reporte por criterios específicos.
     * Esto se puede hacer sobre los reportes ya generados o integrando el filtro
     * en la consulta inicial de datos. (Implementa REQMS-005)
     *
     * @param reporte Original (e.g., de frecuencia o promedios).
     * @param filtros Un mapa con criterios de filtrado (ej. "distrito", "genero").
     * @return El reporte filtrado.
     */
    public <K, V> Map<K, V> filtrarResultados(Map<K, V> reporte, Map<String, String> filtros) {
        // La implementación real aquí sería compleja y dependería del tipo de reporte y filtros.
        // Por simplicidad, podríamos devolver el mismo reporte o aplicar un filtro muy básico.
        // Un filtro más robusto implicaría obtener los IDs de usuario que cumplen el filtro
        // y luego recalcular los reportes solo con las respuestas de esos usuarios.
        System.out.println("ServicioResultados: Funcionalidad de filtrado de reportes (REQMS-005) pendiente de implementación completa.");
        // Ejemplo de filtro conceptual: si tuviéramos un reporte crudo por usuario.
        return reporte;
    }

    /**
     * (PENDIENTE) Exporta un reporte a un formato de texto simple o CSV.
     * (Implementa REQMS-006)
     *
     * @param reporte El reporte a exportar (e.g., de frecuencia o promedios).
     * @return Una cadena de texto formateada (ej. CSV o texto plano).
     */
    public String exportarReporteATexto(Map<String, Map<String, Integer>> reporteFrecuencia, Map<String, Double> reportePromedios) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- REPORTE CONSOLIDADO DE ENCUESTA ---\n\n");

        if (reporteFrecuencia != null && !reporteFrecuencia.isEmpty()) {
            sb.append("--- FRECUENCIA DE RESPUESTAS (Preguntas Categóricas) ---\n");
            for (Map.Entry<String, Map<String, Integer>> entry : reporteFrecuencia.entrySet()) {
                sb.append("Pregunta: ").append(entry.getKey()).append("\n");
                for (Map.Entry<String, Integer> freqEntry : entry.getValue().entrySet()) {
                    sb.append("  - ").append(freqEntry.getKey()).append(": ").append(freqEntry.getValue()).append(" veces\n");
                }
                sb.append("\n");
            }
        }

        if (reportePromedios != null && !reportePromedios.isEmpty()) {
            sb.append("--- PROMEDIOS DE RESPUESTAS (Preguntas Numéricas) ---\n");
            for (Map.Entry<String, Double> entry : reportePromedios.entrySet()) {
                sb.append("Pregunta: ").append(entry.getKey()).append("\n");
                sb.append("  - Promedio: ").append(String.format("%.2f", entry.getValue())).append("\n");
                sb.append("\n");
            }
        }

        if ((reporteFrecuencia == null || reporteFrecuencia.isEmpty()) && (reportePromedios == null || reportePromedios.isEmpty())) {
            sb.append("No hay datos de reporte disponibles para esta encuesta o las preguntas son descriptivas.\n");
        }

        sb.append("--- FIN DEL REPORTE ---\n");
        return sb.toString();
    }
}
