package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.dao.RespuestaUsuarioDAO;
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import java.util.List;
import java.sql.Timestamp;

public class ServicioParticipacion {
    private RespuestaUsuarioDAO respuestaDAO;

    public ServicioParticipacion() {
        this.respuestaDAO = new RespuestaUsuarioDAO();
    }

    // Método renombrado y con parámetros completos
    public boolean guardarRespuestasCompletas(List<RespuestaUsuario> respuestas, int idUsuario, int idEncuesta, Timestamp fechaFinParticipacion, int duracionSegundos) {
        if (respuestas == null || respuestas.isEmpty()) {
            System.err.println("ServicioParticipacion: No hay respuestas para guardar.");
            return false;
        }

        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        for(RespuestaUsuario r : respuestas){
            if(r.getFechaHoraRespuesta() == null){
                r.setFechaHoraRespuesta(ahora);
            }
            // Asegurar que idUsuario y idEncuesta estén configurados en cada respuesta
            r.setIdUsuario(idUsuario);
            r.setIdEncuesta(idEncuesta);
        }
        
        return respuestaDAO.guardarListaRespuestas(respuestas);
    }

    /**
     * Verifica si un usuario específico ya ha respondido a una encuesta determinada.
     * @param idUsuario El ID del usuario.
     * @param idEncuesta El ID de la encuesta.
     * @return true si el usuario ya respondió la encuesta, false en caso contrario.
     */
    public boolean haUsuarioRespondidoEncuesta(int idUsuario, int idEncuesta) { //
        return respuestaDAO.haUsuarioRespondidoEncuesta(idUsuario, idEncuesta);
    }
}