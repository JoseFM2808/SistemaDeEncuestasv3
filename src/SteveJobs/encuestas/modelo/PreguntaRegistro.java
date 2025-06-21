/*
 * Responsable: José Flores
 * Relación con otras partes del código:
 * - Es una entidad utilizada para definir los campos del perfil del usuario
 * durante el registro.
 * - Gestionada por PreguntaRegistroDAO y ServicioConfiguracionAdmin.
 * Funcionalidad:
 * - Representa el modelo de datos (POJO) para una pregunta utilizada en el
 * proceso de registro de usuarios.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de modelo, no implementa algoritmos de ordenamiento o estructuras complejas,
 * el orden se define por un atributo y se maneja en el DAO).
 */

package SteveJobs.encuestas.modelo;

public class PreguntaRegistro {
    private int idPreguntaRegistro;
    private String textoPregunta;
    private String tipoRespuesta;
    private String opcionesPosibles;
    private boolean esObligatoria;
    private int ordenVisualizacion;
    private String estado;

    public PreguntaRegistro() {
    }

    public PreguntaRegistro(String textoPregunta, String tipoRespuesta, String opcionesPosibles, boolean esObligatoria, int ordenVisualizacion, String estado) {
        this.textoPregunta = textoPregunta;
        this.tipoRespuesta = tipoRespuesta;
        this.opcionesPosibles = opcionesPosibles;
        this.esObligatoria = esObligatoria;
        this.ordenVisualizacion = ordenVisualizacion;
        this.estado = estado;
    }

    public int getIdPreguntaRegistro() {
        return idPreguntaRegistro;
    }

    public void setIdPreguntaRegistro(int idPreguntaRegistro) {
        this.idPreguntaRegistro = idPreguntaRegistro;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public String getTipoRespuesta() {
        return tipoRespuesta;
    }

    public void setTipoRespuesta(String tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    public String getOpcionesPosibles() {
        return opcionesPosibles;
    }

    public void setOpcionesPosibles(String opcionesPosibles) {
        this.opcionesPosibles = opcionesPosibles;
    }

    public boolean isEsObligatoria() {
        return esObligatoria;
    }

    public void setEsObligatoria(boolean esObligatoria) {
        this.esObligatoria = esObligatoria;
    }

    public int getOrdenVisualizacion() {
        return ordenVisualizacion;
    }

    public void setOrdenVisualizacion(int ordenVisualizacion) {
        this.ordenVisualizacion = ordenVisualizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "PreguntaRegistro{" +
               "idPreguntaRegistro=" + idPreguntaRegistro +
               ", textoPregunta='" + textoPregunta + '\'' +
               ", tipoRespuesta='" + tipoRespuesta + '\'' +
               ", opcionesPosibles='" + opcionesPosibles + '\'' +
               ", esObligatoria=" + esObligatoria +
               ", ordenVisualizacion=" + ordenVisualizacion +
               ", estado='" + estado + '\'' +
               '}';
    }
}