/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de modelo (POJO) que representa una pregunta reutilizable
 * almacenada en el banco de preguntas del sistema.
 * Contiene el texto de la pregunta, su tipo, clasificación y posibles opciones de respuesta.
 * Es fundamental para REQMS-017 y REQMS-018.
 */

// Contenido de SteveJobs.encuestas.modelo.PreguntaBanco
package SteveJobs.encuestas.modelo;

public class PreguntaBanco {

    private int idPreguntaBanco;
    private String textoPregunta;
    private int idTipoPregunta;
    private Integer idClasificacion;
    private String nombreTipoPregunta; // Se llena en el DAO o Servicio para mostrar
    private String nombreClasificacion; // Se llena en el DAO o Servicio para mostrar
    private String opciones; // Añadido para el campo 'opciones'

    public PreguntaBanco() {
    }

    // Constructor completo si es necesario, ajusta según la necesidad de creación
    public PreguntaBanco(String textoPregunta, int idTipoPregunta, Integer idClasificacion, String opciones) {
        this.textoPregunta = textoPregunta;
        this.idTipoPregunta = idTipoPregunta;
        this.idClasificacion = idClasificacion;
        this.opciones = opciones;
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

    // Getter y Setter para 'opciones'
    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    @Override
    public String toString() {
        return "PreguntaBanco{" +
               "idPreguntaBanco=" + idPreguntaBanco +
               ", textoPregunta='" + textoPregunta + '\'' +
               ", idTipoPregunta=" + idTipoPregunta +
               ", idClasificacion=" + idClasificacion +
               ", opciones='" + opciones + '\'' +
               ", nombreTipoPregunta='" + nombreTipoPregunta + '\'' +
               ", nombreClasificacion='" + nombreClasificacion + '\'' +
               '}';
    }
}