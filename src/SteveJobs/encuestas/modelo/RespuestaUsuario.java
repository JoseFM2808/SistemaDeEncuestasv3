/*
 * Autor: José Flores (Responsable del Módulo de Interacción del Encuestado y Visualización de Resultados)
 *
 * Propósito: Clase de modelo (POJO) que representa la respuesta individual de un usuario
 * a una pregunta específica dentro de una encuesta.
 * Almacena el valor de la respuesta, la fecha/hora, y marcas de tiempo de inicio/fin
 * de participación, así como retroalimentación opcional.
 * Es fundamental para REQMS-002, REQMS-003, REQMS-004, REQMS-005 y REQMS-006.
 */
package SteveJobs.encuestas.modelo;

import java.sql.Timestamp;

public class RespuestaUsuario {
    private int idRespuestaUsuario;
    private int idEncuestaDetallePregunta;
    private int idUsuario;                 
    private String valorRespuesta;       
    private Timestamp fechaHoraRespuesta;
    // Campos opcionales de Joaquín (REQMS-021, REQMS-024)
    private Timestamp tsInicioParticipacion;
    private Timestamp tsFinParticipacion;
    private String retroalimentacionUsuario;


    public RespuestaUsuario() {
    }

    public RespuestaUsuario(int idEncuestaDetallePregunta, int idUsuario, String valorRespuesta) {
        this.idEncuestaDetallePregunta = idEncuestaDetallePregunta;
        this.idUsuario = idUsuario;
        this.valorRespuesta = valorRespuesta;
    }

    public int getIdRespuestaUsuario() {
        return idRespuestaUsuario;
    }

    public void setIdRespuestaUsuario(int idRespuestaUsuario) {
        this.idRespuestaUsuario = idRespuestaUsuario;
    }

    public int getIdEncuestaDetallePregunta() {
        return idEncuestaDetallePregunta;
    }

    public void setIdEncuestaDetallePregunta(int idEncuestaDetallePregunta) {
        this.idEncuestaDetallePregunta = idEncuestaDetallePregunta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getValorRespuesta() {
        return valorRespuesta;
    }

    public void setValorRespuesta(String valorRespuesta) {
        this.valorRespuesta = valorRespuesta;
    }

    public Timestamp getFechaHoraRespuesta() {
        return fechaHoraRespuesta;
    }

    public void setFechaHoraRespuesta(Timestamp fechaHoraRespuesta) {
        this.fechaHoraRespuesta = fechaHoraRespuesta;
    }

    public Timestamp getTsInicioParticipacion() {
        return tsInicioParticipacion;
    }

    public void setTsInicioParticipacion(Timestamp tsInicioParticipacion) {
        this.tsInicioParticipacion = tsInicioParticipacion;
    }

    public Timestamp getTsFinParticipacion() {
        return tsFinParticipacion;
    }

    public void setTsFinParticipacion(Timestamp tsFinParticipacion) {
        this.tsFinParticipacion = tsFinParticipacion;
    }

    public String getRetroalimentacionUsuario() {
        return retroalimentacionUsuario;
    }

    public void setRetroalimentacionUsuario(String retroalimentacionUsuario) {
        this.retroalimentacionUsuario = retroalimentacionUsuario;
    }

    @Override
    public String toString() {
        return "RespuestaUsuario{" +
               "idRespuestaUsuario=" + idRespuestaUsuario +
               ", idEncuestaDetallePregunta=" + idEncuestaDetallePregunta +
               ", idUsuario=" + idUsuario +
               ", valorRespuesta='" + valorRespuesta + '\'' +
               ", fechaHoraRespuesta=" + fechaHoraRespuesta +
               '}';
    }
}