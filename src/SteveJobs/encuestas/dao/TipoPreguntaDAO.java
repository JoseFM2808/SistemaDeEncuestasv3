/*
 * Responsable: Giancarlo Janampa Frisancho (Pendiente de implementación completa por Pablo Alegre)
 * Relación con otras partes del código:
 * - Dependencia clave para ServicioEncuestas y ServicioPreguntas al asociar preguntas
 * a tipos existentes o al obtener detalles completos de preguntas.
 * Funcionalidad:
 * - Objeto de Acceso a Datos (DAO) para la entidad TipoPregunta.
 * - Gestiona las operaciones de persistencia (CRUD) para los tipos de pregunta.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Implementación actual es un placeholder, pero manejaría colecciones List de objetos).
 */

package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.TipoPregunta;
import java.util.List;
import java.util.ArrayList;

public class TipoPreguntaDAO {
    
    public TipoPregunta obtenerTipoPreguntaPorNombre(String nombre) {
        System.out.println("DEBUG: TipoPreguntaDAO.obtenerTipoPreguntaPorNombre NO IMPLEMENTADO, devolviendo null para " + nombre);
        return null; /* Pablo implementará esto */
    }
    public TipoPregunta obtenerTipoPreguntaPorId(int id) {
         System.out.println("DEBUG: TipoPreguntaDAO.obtenerTipoPreguntaPorId NO IMPLEMENTADO, devolviendo null para ID " + id);
        return null; /* Pablo implementará esto */
    }
     public TipoPregunta obtenerTipoPreguntaPorId(Integer id) { // Sobrecarga por si se pasa Integer
        if (id == null) return null;
        return obtenerTipoPreguntaPorId(id.intValue());
    }
    
}
