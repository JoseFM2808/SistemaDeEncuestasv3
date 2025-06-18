/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Objeto de Acceso a Datos (DAO) para la entidad PreguntaBanco.
 * Se encarga de las operaciones de persistencia (CRUD) del banco de preguntas.
 * Actualmente, las operaciones de consulta están marcadas como NO IMPLEMENTADAS.
 * Funciones esperadas: crear, obtener por ID, actualizar, eliminar, y listar con filtros.
 * Es crucial para REQMS-017 y REQMS-018.
 */
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import java.util.List;
import java.util.ArrayList;


public class PreguntaBancoDAO {
    
    public PreguntaBanco obtenerPreguntaPorId(int id) {
         System.out.println("DEBUG: PreguntaBancoDAO.obtenerPreguntaPorId(int) NO IMPLEMENTADO, devolviendo null para ID " + id);
        return null;
    }
     public PreguntaBanco obtenerPreguntaPorId(Integer id) {
        return obtenerPreguntaPorId(id.intValue());
    }
    public List<PreguntaBanco> listarPreguntasDelBancoConFiltro(String filtroTexto, String filtroTipo) {
        System.out.println("DEBUG: PreguntaBancoDAO.listarPreguntasDelBancoConFiltro NO IMPLEMENTADO, devolviendo lista vacía.");
        return new ArrayList<>();
    }
    
}
