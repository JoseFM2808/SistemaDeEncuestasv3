/*
 * Responsable: Giancarlo Janampa Frisancho (Pendiente de implementación completa por Pablo Alegre)
 * Relación con otras partes del código:
 * - Dependencia clave para ServicioEncuestas y ServicioPreguntas al asociar preguntas
 * a clasificaciones existentes o al obtener detalles completos de preguntas.
 * Funcionalidad:
 * - Objeto de Acceso a Datos (DAO) para la entidad ClasificacionPregunta.
 * - Gestiona las operaciones de persistencia (CRUD) para las clasificaciones de preguntas.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Implementación actual es un placeholder, pero manejaría colecciones List de objetos).
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
