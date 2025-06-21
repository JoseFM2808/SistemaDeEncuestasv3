/*
 * Responsable: Alfredo Swidin
 * Relación con otras partes del código:
 * - Es una entidad fundamental utilizada por PreguntaBanco y EncuestaDetallePregunta
 * para definir el tipo de respuesta de una pregunta.
 * - Su gestión de persistencia se realiza a través de TipoPreguntaDAO.
 * Funcionalidad:
 * - Representa el modelo de datos (POJO) para un tipo de pregunta.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de modelo, no implementa algoritmos de ordenamiento o estructuras complejas).
 */

package SteveJobs.encuestas.modelo;

public class TipoPregunta {
    private int idTipoPregunta;
    private String nombreTipo;

    public TipoPregunta() {
    }

    public int getIdTipoPregunta() {
        return idTipoPregunta;
    }

    public void setIdTipoPregunta(int idTipoPregunta) {
        this.idTipoPregunta = idTipoPregunta;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
    
    

}
