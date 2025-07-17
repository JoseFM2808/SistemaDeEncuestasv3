package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.algoritmos.CalculadoraEstadisticasBasicas;
import SteveJobs.encuestas.algoritmos.DetectorPatronesFrecuencia;
import SteveJobs.encuestas.dao.RespuestaUsuarioDAO; 
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta; 
import SteveJobs.encuestas.dao.EncuestaDetallePreguntaDAO; 
import SteveJobs.encuestas.dao.PreguntaBancoDAO; 
import SteveJobs.encuestas.dao.TipoPreguntaDAO; 
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServicioResultados {

    private RespuestaUsuarioDAO respuestaUsuarioDAO;
    private EncuestaDetallePreguntaDAO encuestaDetallePreguntaDAO;
    private PreguntaBancoDAO preguntaBancoDAO; 
    private TipoPreguntaDAO tipoPreguntaDAO;
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO;
    private CalculadoraEstadisticasBasicas calculadoraEstadisticas;
    private DetectorPatronesFrecuencia detectorFrecuencia;

    public ServicioResultados() {
        this.respuestaUsuarioDAO = new RespuestaUsuarioDAO();
        this.encuestaDetallePreguntaDAO = new EncuestaDetallePreguntaDAO();
        this.preguntaBancoDAO = new PreguntaBancoDAO(); 
        this.tipoPreguntaDAO = new TipoPreguntaDAO(); 
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); 
        this.calculadoraEstadisticas = new CalculadoraEstadisticasBasicas();
        this.detectorFrecuencia = new DetectorPatronesFrecuencia();
    }

    
    public List<RespuestaUsuario> filtrarResultados(int idEncuesta, Map<String, String> filtros) {
        List<RespuestaUsuario> todasLasRespuestas = respuestaUsuarioDAO.obtenerRespuestasPorEncuesta(idEncuesta);
        if (todasLasRespuestas == null || todasLasRespuestas.isEmpty()) {
            return new ArrayList<>();
        }

        if (filtros == null || filtros.isEmpty()) {
            return todasLasRespuestas; 
        }

        return todasLasRespuestas.stream()
            .filter(respuesta -> {
                EncuestaDetallePregunta edp = respuesta.getEncuestaDetallePregunta();
                if (edp == null) return false; 

                
                String filtroTipo = filtros.get("tipoPregunta");
                if (filtroTipo != null && !filtroTipo.isEmpty()) {
                    String nombreTipo = edp.getNombreTipoPregunta();
                    if (nombreTipo == null || !nombreTipo.equalsIgnoreCase(filtroTipo)) {
                        return false;
                    }
                }

               
                String filtroClasificacion = filtros.get("clasificacion");
                if (filtroClasificacion != null && !filtroClasificacion.isEmpty()) {
                    String nombreClasif = edp.getNombreClasificacionPregunta();
                    if (nombreClasif == null || !nombreClasif.equalsIgnoreCase(filtroClasificacion)) {
                        return false;
                    }
                }
                
               
                String filtroValor = filtros.get("valorRespuesta");
                if (filtroValor != null && !filtroValor.isEmpty()) {
                    String valorRespuesta = respuesta.getValorRespuesta();
                    if (valorRespuesta == null || !valorRespuesta.equalsIgnoreCase(filtroValor)) {
                        return false;
                    }
                }
                
                

                return true; 
            })
            .collect(Collectors.toList());
    }

    
    public Map<String, Map<String, Integer>> generarReporteFrecuenciaRespuestas(List<RespuestaUsuario> respuestasFiltradas) {
        Map<String, Map<String, Integer>> reporteFrecuencias = new HashMap<>();

        Map<EncuestaDetallePregunta, List<String>> respuestasPorPregunta = new HashMap<>();

        for (RespuestaUsuario ru : respuestasFiltradas) {
            EncuestaDetallePregunta edp = ru.getEncuestaDetallePregunta();
            if (edp == null) continue; 

            String tipoPregunta = edp.getNombreTipoPregunta();

            
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

   
    public Map<String, Double> calcularPromediosPorPregunta(List<RespuestaUsuario> respuestasFiltradas) {
        Map<String, Double> promedios = new HashMap<>();
        Map<EncuestaDetallePregunta, List<Double>> valoresNumericosPorPregunta = new HashMap<>();

        for (RespuestaUsuario ru : respuestasFiltradas) {
            EncuestaDetallePregunta edp = ru.getEncuestaDetallePregunta();
            if (edp == null) continue; 

            String tipoPregunta = edp.getNombreTipoPregunta();
            if (!"NUMERO".equalsIgnoreCase(tipoPregunta)) {
                continue; 
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

    public String exportarReporteACsv(Map<String, Map<String, Integer>> reporteFrecuencias, Map<String, Double> reportePromedios) {
        StringBuilder csv = new StringBuilder();
        
        csv.append("\"Tipo de Reporte\",\"Pregunta\",\"Respuesta/Valor\",\"Conteo/Promedio\"\n");

        if (!reporteFrecuencias.isEmpty()) {
            for (Map.Entry<String, Map<String, Integer>> entry : reporteFrecuencias.entrySet()) {
                String pregunta = entry.getKey().replace("\"", "\"\""); 
                for (Map.Entry<String, Integer> freqEntry : entry.getValue().entrySet()) {
                    String respuesta = freqEntry.getKey().replace("\"", "\"\"");
                    csv.append("\"Frecuencia\",\"").append(pregunta).append("\",\"").append(respuesta).append("\",").append(freqEntry.getValue()).append("\n");
                }
            }
        }

        if (!reportePromedios.isEmpty()) {
            for (Map.Entry<String, Double> entry : reportePromedios.entrySet()) {
                String pregunta = entry.getKey().replace("\"", "\"\""); 
                csv.append("\"Promedio\",\"").append(pregunta).append("\",\"\",\"").append(String.format("%.2f", entry.getValue())).append("\"\n");
            }
        }
        
        return csv.toString();
    }
}