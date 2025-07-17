package SteveJobs.encuestas.modelo;

import java.sql.Timestamp;

public class RespuestaUsuario {
    private int idRespuestaUsuario;
    private int idEncuestaDetallePregunta;
    private int idUsuario;                 
    private String valorRespuesta;       
    private Timestamp fechaHoraRespuesta;
    private Timestamp tsInicioParticipacion;
    private Timestamp tsFinParticipacion;
    private String retroalimentacionUsuario;
    
    private EncuestaDetallePregunta encuestaDetallePregunta;

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

    public EncuestaDetallePregunta getEncuestaDetallePregunta() { 
        return encuestaDetallePregunta; 
    }

    public void setEncuestaDetallePregunta(EncuestaDetallePregunta encuestaDetallePregunta) {
        this.encuestaDetallePregunta = encuestaDetallePregunta; 
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