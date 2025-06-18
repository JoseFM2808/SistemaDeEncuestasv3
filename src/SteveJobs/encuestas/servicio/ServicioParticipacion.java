/*
 * Autor: José Flores (Responsable del Módulo de Interacción del Encuestado y Visualización de Resultados)
 *
 * Propósito: Clase de servicio que gestiona la lógica de negocio para la participación
 * de los usuarios en las encuestas, específicamente el registro de respuestas.
 * Implementa: registro de respuestas completas (con marcas de tiempo y retroalimentación).
 * Es fundamental para REQMS-002, REQMS-003, REQMS-004, REQMS-005 y REQMS-006.
 * (Pendiente: Lógica para el flujo de respuesta de la encuesta, incluyendo descarte).
 */
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.dao.RespuestaUsuarioDAO;
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import java.util.List;
import java.sql.Timestamp; // Para las marcas de tiempo

public class ServicioParticipacion {
    private RespuestaUsuarioDAO respuestaDAO;

    public ServicioParticipacion() {
        this.respuestaDAO = new RespuestaUsuarioDAO();
    }

    public boolean registrarRespuestasCompletas(List<RespuestaUsuario> respuestas) {
        if (respuestas == null || respuestas.isEmpty()) {
            System.err.println("ServicioParticipacion: No hay respuestas para registrar.");
            return false;
        }

        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        for(RespuestaUsuario r : respuestas){
            if(r.getFechaHoraRespuesta() == null){
                r.setFechaHoraRespuesta(ahora);
            }
        }

        return respuestaDAO.guardarListaRespuestas(respuestas);
    }

}