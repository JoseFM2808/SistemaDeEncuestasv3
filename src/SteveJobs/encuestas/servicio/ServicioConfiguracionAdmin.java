// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-b73347d68ca8a40e851f3439418b915b5f3ce710/src/SteveJobs/encuestas/servicio/ServicioConfiguracionAdmin.java
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.dao.PreguntaRegistroDAO;
import SteveJobs.encuestas.modelo.PreguntaRegistro;
import java.util.List;

public class ServicioConfiguracionAdmin {
    private PreguntaRegistroDAO preguntaRegistroDAO;

    public ServicioConfiguracionAdmin() {
        this.preguntaRegistroDAO = new PreguntaRegistroDAO();
    }

    public boolean crearNuevaPreguntaRegistro(String texto, String tipoRespuesta, String opcionesPosibles, boolean esObligatoria, int ordenVisualizacion) {
        if (texto == null || texto.trim().isEmpty()) {
            System.err.println("Servicio: El texto de la pregunta no puede estar vacío.");
            return false;
        }
        if (tipoRespuesta == null || tipoRespuesta.trim().isEmpty()) {
            System.err.println("Servicio: El tipo de respuesta no puede estar vacío.");
            return false;
        }

        PreguntaRegistro nuevaPregunta = new PreguntaRegistro(texto, tipoRespuesta, opcionesPosibles, esObligatoria, ordenVisualizacion, "ACTIVA");
        return preguntaRegistroDAO.agregarPreguntaRegistro(nuevaPregunta);
    }

    // Método renombrado para coincidir con la llamada en RegistroUsuarioGUI
    public List<PreguntaRegistro> obtenerTodasLasPreguntasRegistro() {
        return preguntaRegistroDAO.obtenerTodasLasPreguntasRegistro();
    }

    public boolean modificarPreguntaRegistro(int id, String texto, String tipoRespuesta, String opcionesPosibles, boolean esObligatoria, int ordenVisualizacion, String estado) {
        if (texto == null || texto.trim().isEmpty() || tipoRespuesta == null || tipoRespuesta.trim().isEmpty()) {
            System.err.println("Servicio: Texto y tipo de respuesta son obligatorios para modificar.");
            return false;
        }
        PreguntaRegistro pregunta = new PreguntaRegistro(texto, tipoRespuesta, opcionesPosibles, esObligatoria, ordenVisualizacion, estado);
        pregunta.setIdPreguntaRegistro(id);
        return preguntaRegistroDAO.actualizarPreguntaRegistro(pregunta);
    }

    public boolean eliminarPreguntaRegistro(int id) {
        return preguntaRegistroDAO.eliminarPreguntaRegistro(id);
    }
}