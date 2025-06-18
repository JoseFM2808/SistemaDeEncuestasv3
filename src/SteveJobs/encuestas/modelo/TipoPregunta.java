/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Clase de modelo (POJO) que representa un tipo de pregunta soportado
 * por el sistema (ej. "TEXTO_CORTO", "SELECCION_UNICA_RADIO").
 * Es una tabla de soporte fundamental para el banco de preguntas (REQMS-017).
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
