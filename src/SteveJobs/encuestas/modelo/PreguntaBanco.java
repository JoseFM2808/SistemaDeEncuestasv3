/*
 * Responsable: Pablo Alegre
 * Relación con otras partes del código:
 * - Es una entidad fundamental del banco de preguntas del sistema, gestionada
 * por PreguntaBancoDAO y ServicioPreguntas.
 * - Utilizada en EncuestaDetallePregunta para asociar preguntas existentes.
 * Funcionalidad:
 * - Representa el modelo de datos (POJO) para una pregunta almacenada en el banco
 * general de preguntas reutilizables.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de modelo, no implementa algoritmos de ordenamiento o estructuras complejas).
 */

package SteveJobs.encuestas.modelo;

public class PreguntaBanco {

    private int idPreguntaBanco;
    private String textoPregunta;
    private int idTipoPregunta;
    private Integer idClasificacion;
    private String nombreTipoPregunta;
    private String nombreClasificacion;

    public PreguntaBanco() {
    }

    public int getIdPreguntaBanco() {
        return idPreguntaBanco;
    }

    public void setIdPreguntaBanco(int idPreguntaBanco) {
        this.idPreguntaBanco = idPreguntaBanco;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public int getIdTipoPregunta() {
        return idTipoPregunta;
    }

    public void setIdTipoPregunta(int idTipoPregunta) {
        this.idTipoPregunta = idTipoPregunta;
    }

    public Integer getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(Integer idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public String getNombreTipoPregunta() {
        return nombreTipoPregunta;
    }

    public void setNombreTipoPregunta(String nombreTipoPregunta) {
        this.nombreTipoPregunta = nombreTipoPregunta;
    }

    public String getNombreClasificacion() {
        return nombreClasificacion;
    }

    public void setNombreClasificacion(String nombreClasificacion) {
        this.nombreClasificacion = nombreClasificacion;
    }



}
