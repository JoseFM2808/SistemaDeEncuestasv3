// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-b73347d68ca8a40e851f3439418b915b5f3ce710/src/SteveJobs/encuestas/servicio/ServicioPreguntas.java
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.dao.PreguntaBancoDAO;
import SteveJobs.encuestas.dao.TipoPreguntaDAO;
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO;
import SteveJobs.encuestas.dao.EncuestaDetallePreguntaDAO; 

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;

import java.util.List;

public class ServicioPreguntas {

    private PreguntaBancoDAO preguntaBancoDAO;
    private TipoPreguntaDAO tipoPreguntaDAO;
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO;
    private EncuestaDetallePreguntaDAO encuestaDetallePreguntaDAO; 

    public ServicioPreguntas() {
        this.preguntaBancoDAO = new PreguntaBancoDAO();
        this.tipoPreguntaDAO = new TipoPreguntaDAO();
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO();
        this.encuestaDetallePreguntaDAO = new EncuestaDetallePreguntaDAO(); 
    }

    public boolean registrarPreguntaEnBanco(String textoPregunta, String nombreTipo, String nombreClasificacion) {
        if (textoPregunta == null || textoPregunta.trim().isEmpty()) {
            System.err.println("Servicio: El texto de la pregunta no puede estar vacío.");
            return false;
        }

        TipoPregunta tipo = tipoPreguntaDAO.obtenerTipoPreguntaPorNombre(nombreTipo);
        if (tipo == null) {
            System.err.println("Servicio: El tipo de pregunta '" + nombreTipo + "' no es válido.");
            return false;
        }

        ClasificacionPregunta clasif = null;
        Integer idClasificacion = null;
        if (nombreClasificacion != null && !nombreClasificacion.trim().isEmpty()) {
            clasif = clasificacionPreguntaDAO.obtenerClasificacionPorNombre(nombreClasificacion);
            if (clasif == null) {
                System.err.println("Servicio: La clasificación '" + nombreClasificacion + "' no es válida.");
                return false;
            }
            idClasificacion = clasif.getIdClasificacion();
        }

        PreguntaBanco nuevaPregunta = new PreguntaBanco();
        nuevaPregunta.setTextoPregunta(textoPregunta.trim());
        nuevaPregunta.setIdTipoPregunta(tipo.getIdTipoPregunta());
        nuevaPregunta.setIdClasificacion(idClasificacion);

        
        int idGenerado = preguntaBancoDAO.crearPreguntaBanco(nuevaPregunta); 
        return idGenerado > 0; 
    }

    public List<PreguntaBanco> obtenerTodasLasPreguntasDelBanco() {
        return preguntaBancoDAO.obtenerTodasLasPreguntas();
    }

    // Este método ahora llama al método renombrado en TipoPreguntaDAO
    public List<TipoPregunta> obtenerTodosLosTiposPregunta() {
        return tipoPreguntaDAO.obtenerTodosLosTiposPregunta(); //
    }
    
    /**
     * Nuevo método para obtener todas las clasificaciones.
     * @return Lista de ClasificacionPregunta.
     */
    public List<ClasificacionPregunta> obtenerTodasLasClasificaciones() { // Añadido
        return clasificacionPreguntaDAO.obtenerTodasLasClasificaciones();
    }
   
    public PreguntaBanco obtenerPreguntaPorId(int idPreguntaBanco) {
        return preguntaBancoDAO.obtenerPreguntaPorId(idPreguntaBanco);
    }

    
    public boolean modificarPreguntaBanco(int idPreguntaBanco, String nuevoTexto, String nuevoTipoNombre, String nuevaClasificacionNombre) {
        PreguntaBanco pregunta = preguntaBancoDAO.obtenerPreguntaPorId(idPreguntaBanco);
        if (pregunta == null) {
            System.err.println("Servicio: Pregunta del banco con ID " + idPreguntaBanco + " no encontrada para modificar.");
            return false;
        }

        TipoPregunta tipo = tipoPreguntaDAO.obtenerTipoPreguntaPorNombre(nuevoTipoNombre);
        if (tipo == null) {
            System.err.println("Servicio: Tipo de pregunta '" + nuevoTipoNombre + "' no válido para modificar.");
            return false;
        }

        ClasificacionPregunta clasif = null;
        Integer idClasificacion = null;
        if (nuevaClasificacionNombre != null && !nuevaClasificacionNombre.trim().isEmpty()) {
            clasif = clasificacionPreguntaDAO.obtenerClasificacionPorNombre(nuevaClasificacionNombre);
            if (clasif == null) {
                System.err.println("Servicio: Clasificación '" + nuevaClasificacionNombre + "' no válida para modificar.");
                return false;
            }
            idClasificacion = clasif.getIdClasificacion();
        }
        
        pregunta.setTextoPregunta(nuevoTexto.trim());
        pregunta.setIdTipoPregunta(tipo.getIdTipoPregunta());
        pregunta.setNombreTipoPregunta(tipo.getNombreTipo()); 
        pregunta.setIdClasificacion(idClasificacion);
        pregunta.setNombreClasificacion(nuevaClasificacionNombre); 

        return preguntaBancoDAO.actualizarPreguntaBanco(pregunta);
    }

    
    public boolean eliminarPreguntaBanco(int idPreguntaBanco) {
       
        if (encuestaDetallePreguntaDAO.isPreguntaBancoUsedInActiveEncuestas(idPreguntaBanco)) {
            System.err.println("Servicio: No se puede eliminar la pregunta ID " + idPreguntaBanco + " porque está asociada a una o más encuestas activas.");
            return false;
        }
        
       
        return preguntaBancoDAO.eliminarPreguntaBanco(idPreguntaBanco); 
    }
}