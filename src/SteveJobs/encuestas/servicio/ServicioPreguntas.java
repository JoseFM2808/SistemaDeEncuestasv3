/*
 * Responsable: Pablo Alegre (Corregido por Asistente de AED para alineación con modelo y DAO finales)
 * Relación con otras partes del código:
 * - Implementa la lógica de negocio para la gestión del banco de preguntas.
 * - Se comunica con PreguntaBancoDAO para la persistencia.
 * - Utilizado por la UI (UIGestionBancoPreguntas - si se implementa, y UIConfigurarPreguntasEncuesta).
 * Funcionalidad:
 * - Permite registrar nuevas preguntas en el banco y obtener listados de preguntas del banco.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (La obtención de listas ordenadas se delega al DAO).
 */

package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.dao.PreguntaBancoDAO;
import SteveJobs.encuestas.dao.TipoPreguntaDAO;
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO;
import SteveJobs.encuestas.dao.EncuestaDetallePreguntaDAO; // Importar

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;

import java.util.List;

public class ServicioPreguntas {

    private PreguntaBancoDAO preguntaBancoDAO;
    private TipoPreguntaDAO tipoPreguntaDAO;
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO;
    private EncuestaDetallePreguntaDAO encuestaDetallePreguntaDAO; // Nueva instancia

    public ServicioPreguntas() {
        this.preguntaBancoDAO = new PreguntaBancoDAO();
        this.tipoPreguntaDAO = new TipoPreguntaDAO();
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO();
        this.encuestaDetallePreguntaDAO = new EncuestaDetallePreguntaDAO(); // Inicializar
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

        return preguntaBancoDAO.crearPreguntaBanco(nuevaPregunta);
    }

    public List<PreguntaBanco> obtenerTodasLasPreguntasDelBanco() {
        return preguntaBancoDAO.obtenerTodasLasPreguntas();
    }

    // Nuevo método: obtenerPreguntaPorId (requerido por UIGestionBancoPreguntas)
    public PreguntaBanco obtenerPreguntaPorId(int idPreguntaBanco) {
        return preguntaBancoDAO.obtenerPreguntaPorId(idPreguntaBanco);
    }

    // Nuevo método: modificarPreguntaBanco (requerido por UIGestionBancoPreguntas)
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
        pregunta.setNombreTipoPregunta(tipo.getNombreTipo()); // Actualizar nombre en el modelo
        pregunta.setIdClasificacion(idClasificacion);
        pregunta.setNombreClasificacion(nuevaClasificacionNombre); // Actualizar nombre en el modelo

        return preguntaBancoDAO.actualizarPreguntaBanco(pregunta);
    }

    // Método eliminarPreguntaBanco con la lógica adicional (requerido por UIGestionBancoPreguntas)
    public boolean eliminarPreguntaBanco(int idPreguntaBanco) {
        // Lógica de negocio adicional: verificar si la pregunta no está asociada a ninguna encuesta activa
        if (encuestaDetallePreguntaDAO.isPreguntaBancoUsedInActiveEncuestas(idPreguntaBanco)) {
            System.err.println("Servicio: No se puede eliminar la pregunta ID " + idPreguntaBanco + " porque está asociada a una o más encuestas activas.");
            return false;
        }
        
        // Si no está en encuestas activas, se procede a la eliminación
        return preguntaBancoDAO.eliminarPreguntaBanco(idPreguntaBanco);
    }
}