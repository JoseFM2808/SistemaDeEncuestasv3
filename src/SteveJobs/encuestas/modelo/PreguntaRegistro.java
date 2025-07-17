// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-b73347d68ca8a40e851f3439418b915b5f3ce710/src/SteveJobs/encuestas/modelo/PreguntaRegistro.java
package SteveJobs.encuestas.modelo;

public class PreguntaRegistro {
    private int idPreguntaRegistro;
    private String textoPregunta;
    private String tipoRespuesta; // Se mantiene el nombre de la propiedad interna
    private String opcionesPosibles; // Se mantiene el nombre de la propiedad interna
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

    // Renombrado para coincidir con la llamada de RegistroUsuarioGUI
    public String getTipoEntrada() {
        return tipoRespuesta;
    }

    // Mantener setTipoRespuesta si aún se usa internamente
    public void setTipoRespuesta(String tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    // Renombrado para coincidir con la llamada de RegistroUsuarioGUI
    public String getOpciones() {
        return opcionesPosibles;
    }

    // Mantener setOpcionesPosibles si aún se usa internamente
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