/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de modelo (POJO) que representa una clasificación temática de preguntas
 * en el sistema (ej. "Socioeconómico", "Tecnología").
 * Contiene atributos como idClasificacion y nombreClasificacion, con sus respectivos
 * getters y setters. Es un componente fundamental para REQMS-016.
 */
package SteveJobs.encuestas.modelo;

public class ClasificacionPregunta {
    
    private int idClasificacion;
    private String nombreClasificacion;

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
    
    
    
}
