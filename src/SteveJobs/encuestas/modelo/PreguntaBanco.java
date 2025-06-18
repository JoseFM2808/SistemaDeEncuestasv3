/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de modelo (POJO) que representa una pregunta reutilizable
 * almacenada en el banco de preguntas del sistema.
 * Contiene el texto de la pregunta, su tipo, clasificación y posibles opciones de respuesta.
 * Es fundamental para REQMS-017 y REQMS-018.
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
