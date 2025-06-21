/*
 * Responsable: Alfredo Swidin
 * Relación con otras partes del código:
 * - Es una entidad fundamental utilizada por PreguntaBanco y EncuestaDetallePregunta
 * para clasificar las preguntas.
 * - Su gestión de persistencia se realiza a través de ClasificacionPreguntaDAO.
 * Funcionalidad:
 * - Representa el modelo de datos (POJO) para una clasificación de preguntas.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de modelo, no implementa algoritmos de ordenamiento o estructuras complejas).
 */

package SteveJobs.encuestas.modelo;

public class ClasificacionPregunta {
    
    private int idClasificacion;
    private String nombreClasificacion;
    private String descripcion;
    private String estado;

    public ClasificacionPregunta() {
    }

    public int getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(int idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public String getNombreClasificacion() {
        return nombreClasificacion;
    }

    public void setNombreClasificacion(String nombreClasificacion) {
        this.nombreClasificacion = nombreClasificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
        
}
