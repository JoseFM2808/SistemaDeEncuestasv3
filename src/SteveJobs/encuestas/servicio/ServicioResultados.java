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

import SteveJobs.encuestas.algoritmos.CalculadoraEstadisticasBasicas;
import SteveJobs.encuestas.algoritmos.DetectorPatronesFrecuencia;
import SteveJobs.encuestas.dao.RespuestaUsuarioDAO; // Importar
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta; // Importar
import SteveJobs.encuestas.dao.EncuestaDetallePreguntaDAO; // Importar
import SteveJobs.encuestas.dao.PreguntaBancoDAO; // Importar
import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Importar
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Importar

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServicioResultados {

    private RespuestaUsuarioDAO respuestaUsuarioDAO;
    private EncuestaDetallePreguntaDAO encuestaDetallePreguntaDAO;
    private PreguntaBancoDAO preguntaBancoDAO; // Para obtener detalles completos de preguntas
    private TipoPreguntaDAO tipoPreguntaDAO;
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO;
    private CalculadoraEstadisticasBasicas calculadoraEstadisticas;
    private DetectorPatronesFrecuencia detectorFrecuencia;

    public ServicioResultados() {
        this.respuestaUsuarioDAO = new RespuestaUsuarioDAO();
        this.encuestaDetallePreguntaDAO = new EncuestaDetallePreguntaDAO();
        this.preguntaBancoDAO = new PreguntaBancoDAO(); // Inicializar
        this.tipoPreguntaDAO = new TipoPreguntaDAO(); // Inicializar
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // Inicializar
        this.calculadoraEstadisticas = new CalculadoraEstadisticasBasicas();
        this.detectorFrecuencia = new DetectorPatronesFrecuencia();
    }

    /**
     * Filtra una lista de respuestas de usuario basándose en criterios específicos.
     * Los filtros pueden incluir: "tipoPregunta", "clasificacion", "valorRespuesta".
     *
     * @param idEncuesta El ID de la encuesta.
     * @param filtros Un mapa con los criterios de filtro (ej. "tipoPregunta" -> "NUMERO").
     * @return Una lista de objetos RespuestaUsuario filtrados, incluyendo sus detalles de pregunta.
     */
    public List<RespuestaUsuario> filtrarResultados(int idEncuesta, Map<String, String> filtros) {
        List<RespuestaUsuario> todasLasRespuestas = respuestaUsuarioDAO.obtenerRespuestasPorEncuesta(idEncuesta);
        if (todasLasRespuestas == null || todasLasRespuestas.isEmpty()) {
            return new ArrayList<>();
        }

        if (filtros == null || filtros.isEmpty()) {
            return todasLasRespuestas; // No hay filtros, devuelve todas
        }

        return todasLasRespuestas.stream()
            .filter(respuesta -> {
                EncuestaDetallePregunta edp = respuesta.getEncuestaDetallePregunta();
                if (edp == null) return false; // La respuesta debe tener su detalle de pregunta mapeado

                // Filtro por tipo de pregunta
                String filtroTipo = filtros.get("tipoPregunta");
                if (filtroTipo != null && !filtroTipo.isEmpty()) {
                    String nombreTipo = edp.getNombreTipoPregunta();
                    if (nombreTipo == null || !nombreTipo.equalsIgnoreCase(filtroTipo)) {
                        return false;
                    }
                }

                // Filtro por clasificación
                String filtroClasificacion = filtros.get("clasificacion");
                if (filtroClasificacion != null && !filtroClasificacion.isEmpty()) {
                    String nombreClasif = edp.getNombreClasificacionPregunta();
                    if (nombreClasif == null || !nombreClasif.equalsIgnoreCase(filtroClasificacion)) {
                        return false;
                    }
                }
                
                // Filtro por valor de respuesta (para respuestas categóricas/texto)
                String filtroValor = filtros.get("valorRespuesta");
                if (filtroValor != null && !filtroValor.isEmpty()) {
                    String valorRespuesta = respuesta.getValorRespuesta();
                    if (valorRespuesta == null || !valorRespuesta.equalsIgnoreCase(filtroValor)) {
                        return false;
                    }
                }
                
                // Puedes añadir más filtros aquí (ej. por id_usuario, por rango de fecha, etc.)

                return true; // Si pasa todos los filtros
            })
            .collect(Collectors.toList());
    }

    /**
     * Genera un reporte de frecuencia para preguntas categóricas (no descriptivas).
     * Devuelve un mapa donde la clave es el texto de la pregunta y el valor es otro mapa
     * de respuestas y sus conteos.
     * @param respuestasFiltradas Lista de respuestas ya filtradas para procesar.
     * @return Mapa de resultados de frecuencia.
     */
    public Map<String, Map<String, Integer>> generarReporteFrecuenciaRespuestas(List<RespuestaUsuario> respuestasFiltradas) {
        Map<String, Map<String, Integer>> reporteFrecuencias = new HashMap<>();

        Map<EncuestaDetallePregunta, List<String>> respuestasPorPregunta = new HashMap<>();

        for (RespuestaUsuario ru : respuestasFiltradas) {
            EncuestaDetallePregunta edp = ru.getEncuestaDetallePregunta();
            if (edp == null) continue; // Asegurarse de que el detalle esté cargado

            String tipoPregunta = edp.getNombreTipoPregunta();

            // Solo procesar tipos de pregunta que no son descriptivos
            if ("Descripción".equalsIgnoreCase(tipoPregunta) || "TEXTO_CORTO".equalsIgnoreCase(tipoPregunta)) {
                continue;
            }

            respuestasPorPregunta.computeIfAbsent(edp, k -> new ArrayList<>())
                                 .add(ru.getValorRespuesta());
        }

        for (Map.Entry<EncuestaDetallePregunta, List<String>> entry : respuestasPorPregunta.entrySet()) {
            EncuestaDetallePregunta edp = entry.getKey();
            List<String> valoresRespuestas = entry.getValue();
            
            Map<String, Integer> frecuencias = detectorFrecuencia.calcularFrecuenciaRespuestas(valoresRespuestas);
            reporteFrecuencias.put(edp.getTextoPreguntaMostrable(), frecuencias);
        }

        return reporteFrecuencias;
    }

    /**
     * Calcula los promedios para preguntas de tipo numérico.
     * Devuelve un mapa donde la clave es el texto de la pregunta y el valor es su promedio.
     * @param respuestasFiltradas Lista de respuestas ya filtradas para procesar.
     * @return Mapa de promedios.
     */
    public Map<String, Double> calcularPromediosPorPregunta(List<RespuestaUsuario> respuestasFiltradas) {
        Map<String, Double> promedios = new HashMap<>();
        Map<EncuestaDetallePregunta, List<Double>> valoresNumericosPorPregunta = new HashMap<>();

        for (RespuestaUsuario ru : respuestasFiltradas) {
            EncuestaDetallePregunta edp = ru.getEncuestaDetallePregunta();
            if (edp == null) continue; // Asegurarse de que el detalle esté cargado

            String tipoPregunta = edp.getNombreTipoPregunta();
            if (!"NUMERO".equalsIgnoreCase(tipoPregunta)) {
                continue; // Solo procesar preguntas de tipo NUMERO
            }

            try {
                Double valor = Double.parseDouble(ru.getValorRespuesta());
                valoresNumericosPorPregunta.computeIfAbsent(edp, k -> new ArrayList<>())
                                          .add(valor);
            } catch (NumberFormatException e) {
                System.err.println("ServicioResultados: Valor de respuesta no numérico para pregunta tipo NUMERO: " + ru.getValorRespuesta());
            }
        }

        for (Map.Entry<EncuestaDetallePregunta, List<Double>> entry : valoresNumericosPorPregunta.entrySet()) {
            EncuestaDetallePregunta edp = entry.getKey();
            List<Double> valores = entry.getValue();
            double promedio = calculadoraEstadisticas.calcularPromedio(valores);
            promedios.put(edp.getTextoPreguntaMostrable(), promedio);
        }
        return promedios;
    }

    /**
     * Exporta los reportes (frecuencia y promedios) a una cadena de texto formateada.
     * Ahora recibe los datos estructurados generados por los métodos de reporte.
     * @param reporteFrecuencias Mapa de frecuencias por pregunta.
     * @param reportePromedios Mapa de promedios por pregunta.
     * @return Una cadena de texto con el reporte.
     */
    public String exportarReporteATexto(Map<String, Map<String, Integer>> reporteFrecuencias, Map<String, Double> reportePromedios) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== REPORTE DE RESULTADOS DE ENCUESTA =====\n\n");

        if (!reporteFrecuencias.isEmpty()) {
            sb.append("--- Frecuencia de Respuestas por Pregunta (Categóricas) ---\n");
            for (Map.Entry<String, Map<String, Integer>> entry : reporteFrecuencias.entrySet()) {
                sb.append("Pregunta: ").append(entry.getKey()).append("\n");
                for (Map.Entry<String, Integer> freqEntry : entry.getValue().entrySet()) {
                    sb.append("  - ").append(freqEntry.getKey()).append(": ").append(freqEntry.getValue()).append(" veces\n");
                }
                sb.append("\n");
            }
        } else {
            sb.append("No hay datos de frecuencia para mostrar o no hay preguntas categóricas en el filtro.\n\n");
        }

        if (!reportePromedios.isEmpty()) {
            sb.append("--- Promedio de Respuestas por Pregunta (Numéricas) ---\n");
            for (Map.Entry<String, Double> entry : reportePromedios.entrySet()) {
                sb.append("Pregunta: ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
            }
        } else {
            sb.append("No hay datos de promedios para mostrar o no hay preguntas numéricas en el filtro.\n\n");
        }

        return sb.toString();
    }

    /**
     * Exporta los reportes (frecuencia y promedios) a una cadena en formato CSV.
     * @param reporteFrecuencias Mapa de frecuencias por pregunta.
     * @param reportePromedios Mapa de promedios por pregunta.
     * @return Una cadena en formato CSV con el reporte.
     */
    public String exportarReporteACsv(Map<String, Map<String, Integer>> reporteFrecuencias, Map<String, Double> reportePromedios) {
        StringBuilder csv = new StringBuilder();
        // Encabezados para frecuencias
        csv.append("\"Tipo de Reporte\",\"Pregunta\",\"Respuesta/Valor\",\"Conteo/Promedio\"\n");

        if (!reporteFrecuencias.isEmpty()) {
            for (Map.Entry<String, Map<String, Integer>> entry : reporteFrecuencias.entrySet()) {
                String pregunta = entry.getKey().replace("\"", "\"\""); // Escape comillas dobles
                for (Map.Entry<String, Integer> freqEntry : entry.getValue().entrySet()) {
                    String respuesta = freqEntry.getKey().replace("\"", "\"\"");
                    csv.append("\"Frecuencia\",\"").append(pregunta).append("\",\"").append(respuesta).append("\",").append(freqEntry.getValue()).append("\n");
                }
            }
        }

        if (!reportePromedios.isEmpty()) {
            for (Map.Entry<String, Double> entry : reportePromedios.entrySet()) {
                String pregunta = entry.getKey().replace("\"", "\"\""); // Escape comillas dobles
                csv.append("\"Promedio\",\"").append(pregunta).append("\",\"\",\"").append(String.format("%.2f", entry.getValue())).append("\"\n");
            }
        }
        
        return csv.toString();
    }
}