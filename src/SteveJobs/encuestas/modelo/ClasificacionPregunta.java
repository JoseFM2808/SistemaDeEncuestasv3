/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de modelo (POJO) que representa una clasificación temática de preguntas
 * en el sistema (ej. "Socioeconómico", "Tecnología").
 * Contiene atributos como idClasificacion y nombreClasificacion, con sus respectivos
 * getters y setters. Es un componente fundamental para REQMS-016.
 */
// Contenido de SteveJobs.encuestas.modelo.ClasificacionPregunta
package SteveJobs.encuestas.modelo;

public class ClasificacionPregunta {
    private int idClasificacion;
    private String nombreClasificacion;
    private String descripcion; // Añadido
    private String estado;      // Añadido

    public ClasificacionPregunta() {
    }

    // Nuevo constructor si es útil para la creación
    public ClasificacionPregunta(String nombreClasificacion, String descripcion, String estado) {
        this.nombreClasificacion = nombreClasificacion;
        this.descripcion = descripcion;
        this.estado = estado;
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

    // Getters y Setters para los nuevos atributos
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

    @Override
    public String toString() {
        return "ClasificacionPregunta{" +
               "idClasificacion=" + idClasificacion +
               ", nombreClasificacion='" + nombreClasificacion + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", estado='" + estado + '\'' +
               '}';
    }
}