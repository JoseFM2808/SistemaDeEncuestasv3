/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Objeto de Acceso a Datos (DAO) para la entidad ClasificacionPregunta.
 * Se encarga de las operaciones de persistencia (CRUD) relacionadas con las
 * clasificaciones temáticas de las preguntas.
 * Actualmente, las operaciones de consulta están marcadas como NO IMPLEMENTADAS.
 * Funciones esperadas: crear, obtener por nombre/ID, actualizar y eliminar clasificaciones.
 * Es crucial para REQMS-016.
 */
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import java.util.List;
import java.util.ArrayList;

public class ClasificacionPreguntaDAO {
    
    public ClasificacionPregunta obtenerClasificacionPorNombre(String nombre) {
        System.out.println("DEBUG: ClasificacionPreguntaDAO.obtenerClasificacionPorNombre NO IMPLEMENTADO, devolviendo null para " + nombre);
        return null; /* Pablo implementará esto */
    }
     public ClasificacionPregunta obtenerClasificacionPorId(int id) {
        System.out.println("DEBUG: ClasificacionPreguntaDAO.obtenerClasificacionPorId NO IMPLEMENTADO, devolviendo null para ID " + id);
        return null; /* Pablo implementará esto */
    }
    public ClasificacionPregunta obtenerClasificacionPorId(Integer id) { // Sobrecarga
        if (id == null) return null;
        return obtenerClasificacionPorId(id.intValue());
    }
    
}
