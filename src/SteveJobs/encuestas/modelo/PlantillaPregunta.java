/*
 * Responsable: Pablo Alegre (Futura responsabilidad, si se integra módulo de Plantillas)
 * Relación con otras partes del código:
 * - N/A (Actualmente un placeholder, no tiene relaciones activas).
 * Funcionalidad:
 * - Representa el modelo de datos (POJO) para una plantilla de preguntas,
 * pensado para permitir la creación de encuestas a partir de estructuras predefinidas.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de modelo, es un placeholder).
 */

package SteveJobs.encuestas.modelo;

// Se asume que una plantilla tiene un ID, nombre, descripción y un estado
public class PlantillaPregunta {
    private int idPlantilla;
    private String nombre;
    private String descripcion;
    private String estado; // Ejemplo: "ACTIVA", "INACTIVA"

    public PlantillaPregunta() {
    }

    public PlantillaPregunta(String nombre, String descripcion, String estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(int idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return "PlantillaPregunta{" +
               "idPlantilla=" + idPlantilla +
               ", nombre='" + nombre + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", estado='" + estado + '\'' +
               '}';
    }
}