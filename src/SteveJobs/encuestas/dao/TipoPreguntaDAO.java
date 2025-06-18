/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Objeto de Acceso a Datos (DAO) para la entidad TipoPregunta.
 * Se encarga de las operaciones de persistencia (CRUD) relacionadas con los
 * tipos de pregunta soportados por el sistema.
 * Actualmente, las operaciones de consulta están marcadas como NO IMPLEMENTADAS.
 * Funciones esperadas: crear, obtener por nombre/ID, actualizar y eliminar tipos de pregunta.
 * Es crucial para REQMS-017.
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
