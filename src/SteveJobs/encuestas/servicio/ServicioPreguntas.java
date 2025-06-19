/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de servicio que gestiona la lógica de negocio para el banco de preguntas.
 * Funciones esperadas: listado de preguntas con filtros, y otras operaciones CRUD
 * sobre las preguntas del banco.
 * Actualmente, la función listarPreguntasDelBancoConFiltro es un placeholder.
 * Es central para REQMS-017 y REQMS-018.
 */
// Contenido de SteveJobs.encuestas.servicio.ServicioPreguntas
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.dao.PreguntaBancoDAO;
import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Necesario para validar tipos
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Necesario para validar clasificaciones

import java.util.List;
import java.util.ArrayList;

public class ServicioPreguntas {

    private PreguntaBancoDAO preguntaBancoDAO;
    private TipoPreguntaDAO tipoPreguntaDAO; // Añadido
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO; // Añadido

    public ServicioPreguntas() {
        this.preguntaBancoDAO = new PreguntaBancoDAO();
        this.tipoPreguntaDAO = new TipoPreguntaDAO(); // Inicializado
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // Inicializado
    }

    // REQMS-017: Crear nueva pregunta en el banco
    public int crearNuevaPreguntaBanco(String texto, int idTipo, Integer idClasificacion, String opciones) {
        if (texto == null || texto.trim().isEmpty()) {
            System.err.println("Servicio: El texto de la pregunta no puede estar vacío.");
            return -1;
        }
        if (idTipo <= 0) {
            System.err.println("Servicio: Se debe especificar un tipo de pregunta válido.");
            return -1;
        }
        
        // Validar que el tipo de pregunta exista
        TipoPregunta tipoExistente = tipoPreguntaDAO.obtenerTipoPreguntaPorId(idTipo);
        if (tipoExistente == null) {
            System.err.println("Servicio: El tipo de pregunta con ID " + idTipo + " no existe.");
            return -1;
        }
        
        // Validar que la clasificación exista, si se proporciona
        if (idClasificacion != null && idClasificacion > 0) {
            ClasificacionPregunta clasifExistente = clasificacionPreguntaDAO.obtenerClasificacionPorId(idClasificacion);
            if (clasifExistente == null) {
                System.err.println("Servicio: La clasificación con ID " + idClasificacion + " no existe.");
                return -1;
            }
        }

        PreguntaBanco nuevaPregunta = new PreguntaBanco(texto.trim(), idTipo, idClasificacion, opciones);
        return preguntaBancoDAO.crearPregunta(nuevaPregunta);
    }

    // REQMS-017: Actualizar pregunta en el banco
    public boolean actualizarPreguntaBanco(int idPreguntaBanco, String texto, int idTipo, Integer idClasificacion, String opciones) {
        if (idPreguntaBanco <= 0) {
            System.err.println("Servicio: ID de pregunta de banco inválido para actualizar.");
            return false;
        }
        if (texto == null || texto.trim().isEmpty()) {
            System.err.println("Servicio: El texto de la pregunta no puede estar vacío.");
            return false;
        }
        if (idTipo <= 0) {
            System.err.println("Servicio: Se debe especificar un tipo de pregunta válido.");
            return false;
        }
        
        // Validar que el tipo de pregunta exista
        TipoPregunta tipoExistente = tipoPreguntaDAO.obtenerTipoPreguntaPorId(idTipo);
        if (tipoExistente == null) {
            System.err.println("Servicio: El tipo de pregunta con ID " + idTipo + " no existe.");
            return false;
        }
        
        // Validar que la clasificación exista, si se proporciona
        if (idClasificacion != null && idClasificacion > 0) {
            ClasificacionPregunta clasifExistente = clasificacionPreguntaDAO.obtenerClasificacionPorId(idClasificacion);
            if (clasifExistente == null) {
                System.err.println("Servicio: La clasificación con ID " + idClasificacion + " no existe.");
                return false;
            }
        }

        PreguntaBanco preguntaAActualizar = new PreguntaBanco();
        preguntaAActualizar.setIdPreguntaBanco(idPreguntaBanco);
        preguntaAActualizar.setTextoPregunta(texto.trim());
        preguntaAActualizar.setIdTipoPregunta(idTipo);
        preguntaAActualizar.setIdClasificacion(idClasificacion);
        preguntaAActualizar.setOpciones(opciones);

        return preguntaBancoDAO.actualizarPregunta(preguntaAActualizar);
    }

    // REQMS-017: Eliminar pregunta del banco
    public boolean eliminarPreguntaBanco(int idPreguntaBanco) {
        if (idPreguntaBanco <= 0) {
            System.err.println("Servicio: ID de pregunta de banco inválido para eliminar.");
            return false;
        }
        // Considerar aquí una validación adicional: ¿la pregunta está siendo usada en alguna encuesta?
        // Si lo está, quizás no debería eliminarse directamente o solo marcarla como inactiva.
        // Por ahora, delegamos la eliminación directa al DAO.
        return preguntaBancoDAO.eliminarPregunta(idPreguntaBanco);
    }

    // REQMS-018: Listar preguntas del banco con filtro
    // Ampliado para permitir filtro por ID de clasificación y nombre de tipo
    public List<PreguntaBanco> listarPreguntasDelBancoConFiltro(String filtroTexto, String filtroTipoNombre, Integer filtroClasificacionId) {
        // En esta capa, podrías convertir el nombre del tipo a ID si el DAO solo acepta ID,
        // o si el DAO ya maneja el JOIN como lo propuse, simplemente pasar los valores.
        // Aquí llamamos al DAO que ya hace el JOIN.

        List<PreguntaBanco> preguntas = preguntaBancoDAO.listarPreguntasDelBancoConFiltro(filtroTexto, filtroTipoNombre, filtroClasificacionId);
        
        // Opcional: Si los objetos PreguntaBanco del DAO no vienen con nombres de tipo/clasificación
        // precargados, aquí se podrían "enriquecer" llamando a TipoPreguntaDAO y ClasificacionPreguntaDAO
        // para cada pregunta. Pero mi propuesta de DAO ya los incluye.
        
        return preguntas;
    }

    // REQMS-018: Obtener pregunta del banco por ID
    public PreguntaBanco obtenerPreguntaBancoPorId(int idPreguntaBanco) {
        if (idPreguntaBanco <= 0) {
            System.err.println("Servicio: ID de pregunta de banco inválido para consulta.");
            return null;
        }
        // Obtener la pregunta base
        PreguntaBanco pregunta = preguntaBancoDAO.obtenerPreguntaPorId(idPreguntaBanco);
        
        // Enriquecer la pregunta con nombres de tipo y clasificación para la UI
        if (pregunta != null) {
            TipoPregunta tipo = tipoPreguntaDAO.obtenerTipoPreguntaPorId(pregunta.getIdTipoPregunta());
            if (tipo != null) {
                pregunta.setNombreTipoPregunta(tipo.getNombreTipo());
            }

            if (pregunta.getIdClasificacion() != null && pregunta.getIdClasificacion() > 0) {
                ClasificacionPregunta clasif = clasificacionPreguntaDAO.obtenerClasificacionPorId(pregunta.getIdClasificacion());
                if (clasif != null) {
                    pregunta.setNombreClasificacion(clasif.getNombreClasificacion());
                }
            }
        }
        return pregunta;
    }
}