/*
 * Responsable: José Flores
 * Relación con otras partes del código:
 * - Implementa la lógica de negocio para la participación de los encuestados.
 * - Se comunica con RespuestaUsuarioDAO para guardar las respuestas.
 * - Utilizado por la UI (UIMenuEncuestado).
 * Funcionalidad:
 * - Registra un conjunto de respuestas de un usuario para una encuesta.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - Utiliza la estructura de datos List para recibir las respuestas a registrar.
 * - N/A (Lógica de negocio, no implementa ordenamiento directo).
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