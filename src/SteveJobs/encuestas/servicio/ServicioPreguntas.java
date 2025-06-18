/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de servicio que gestiona la lógica de negocio para el banco de preguntas.
 * Funciones esperadas: listado de preguntas con filtros, y otras operaciones CRUD
 * sobre las preguntas del banco.
 * Actualmente, la función listarPreguntasDelBancoConFiltro es un placeholder.
 * Es central para REQMS-017 y REQMS-018.
 */
package SteveJobs.encuestas.servicio;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.dao.PreguntaBancoDAO; // Asumiendo que Pablo lo crea
import java.util.List;
import java.util.ArrayList;

public class ServicioPreguntas {
    
    private PreguntaBancoDAO preguntaBancoDAO;

    public ServicioPreguntas() {
        this.preguntaBancoDAO = new PreguntaBancoDAO(); // Asumiendo que Pablo crea esta clase DAO
    }

    public List<PreguntaBanco> listarPreguntasDelBancoConFiltro(String filtroTexto, String filtroTipo) {
        System.out.println("ServicioPreguntas.listarPreguntasDelBancoConFiltro no implementado, devolviendo lista vacía.");
        // Aquí Pablo llamaría a su DAO
        return new ArrayList<>();
    }
    // ... otros métodos que Pablo necesite para sus requerimientos REQMS-007 a REQMS-011
    
}
