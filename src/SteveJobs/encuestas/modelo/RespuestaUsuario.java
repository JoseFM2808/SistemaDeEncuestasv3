package SteveJobs.encuestas.modelo;

import java.sql.Timestamp;

public class RespuestaUsuario {
    private int idRespuesta; // Clave primaria
    private int idUsuario;
    private int idEncuesta; 
    private int idEncuestaDetallePregunta; // FK a EncuestaDetallePregunta
    private String valorRespuesta;
    private Timestamp fechaHoraRespuesta;

    // Nuevo campo para almacenar el objeto de detalle de pregunta asociado
    private EncuestaDetallePregunta encuestaDetallePregunta; 

    public RespuestaUsuario() {
    }

    // Constructor completo si es necesario para crear respuestas
    public RespuestaUsuario(int idRespuesta, int idUsuario, int idEncuesta, int idEncuestaDetallePregunta, String valorRespuesta, Timestamp fechaHoraRespuesta) {
        this.idRespuesta = idRespuesta;
        this.idUsuario = idUsuario;
        this.idEncuesta = idEncuesta;
        this.idEncuestaDetallePregunta = idEncuestaDetallePregunta;
        this.valorRespuesta = valorRespuesta;
        this.fechaHoraRespuesta = fechaHoraRespuesta;
    }

    // Constructor simplificado para la recolección inicial
    public RespuestaUsuario(int idUsuario, int idEncuestaDetallePregunta, String valorRespuesta) {
        this.idUsuario = idUsuario;
        this.idEncuestaDetallePregunta = idEncuestaDetallePregunta;
        this.valorRespuesta = valorRespuesta;
    }


    // Getters y Setters
    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public int getIdEncuestaDetallePregunta() {
        return idEncuestaDetallePregunta;
    }

    public void setIdEncuestaDetallePregunta(int idEncuestaDetallePregunta) {
        this.idEncuestaDetallePregunta = idEncuestaDetallePregunta;
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

    // Nuevo getter y setter para el objeto EncuestaDetallePregunta
    public EncuestaDetallePregunta getEncuestaDetallePregunta() {
        return encuestaDetallePregunta;
    }

    public void setEncuestaDetallePregunta(EncuestaDetallePregunta encuestaDetallePregunta) {
        this.encuestaDetallePregunta = encuestaDetallePregunta;
    }
}