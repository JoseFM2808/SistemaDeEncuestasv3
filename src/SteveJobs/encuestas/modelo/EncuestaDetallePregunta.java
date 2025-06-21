/*
 * Responsable: Alfredo Swidin
 * Relación con otras partes del código:
 * - Es una entidad intermedia que relaciona Encuestas con Preguntas del Banco
 * o define preguntas únicas para una encuesta específica.
 * - Gestionada por EncuestaDetallePreguntaDAO y ServicioEncuestas.
 * Funcionalidad:
 * - Representa el modelo de datos (POJO) para el detalle de una pregunta dentro de una encuesta,
 * incluyendo su orden, si es de descarte y el criterio asociado.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de modelo, no implementa algoritmos de ordenamiento o estructuras complejas).
 */

package SteveJobs.encuestas.modelo;

public class EncuestaDetallePregunta {
    private int idEncuestaDetalle;
    private int idEncuesta;
    private Integer idPreguntaBanco;
    private String textoPreguntaUnica;
    private Integer idTipoPreguntaUnica;
    private Integer idClasificacionUnica;
    private int ordenEnEncuesta;
    private boolean esPreguntaDescarte;
    private String criterioDescarteValor;
    private PreguntaBanco preguntaDelBanco;


    public EncuestaDetallePregunta() {
    }

    public EncuestaDetallePregunta(int idEncuesta, int idPreguntaBanco, int ordenEnEncuesta, boolean esPreguntaDescarte, String criterioDescarteValor) {
        this.idEncuesta = idEncuesta;
        this.idPreguntaBanco = idPreguntaBanco;
        this.ordenEnEncuesta = ordenEnEncuesta;
        this.esPreguntaDescarte = esPreguntaDescarte;
        this.criterioDescarteValor = criterioDescarteValor;
    }

    public EncuestaDetallePregunta(int idEncuesta, String textoPreguntaUnica, Integer idTipoPreguntaUnica, Integer idClasificacionUnica, int ordenEnEncuesta, boolean esPreguntaDescarte, String criterioDescarteValor) {
        this.idEncuesta = idEncuesta;
        this.textoPreguntaUnica = textoPreguntaUnica;
        this.idTipoPreguntaUnica = idTipoPreguntaUnica;
        this.idClasificacionUnica = idClasificacionUnica;
        this.ordenEnEncuesta = ordenEnEncuesta;
        this.esPreguntaDescarte = esPreguntaDescarte;
        this.criterioDescarteValor = criterioDescarteValor;
    }


    public int getIdEncuestaDetalle() {
        return idEncuestaDetalle;
    }

    public void setIdEncuestaDetalle(int idEncuestaDetalle) {
        this.idEncuestaDetalle = idEncuestaDetalle;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public Integer getIdPreguntaBanco() {
        return idPreguntaBanco;
    }

    public void setIdPreguntaBanco(Integer idPreguntaBanco) {
        this.idPreguntaBanco = idPreguntaBanco;
    }

    public String getTextoPreguntaUnica() {
        return textoPreguntaUnica;
    }

    public void setTextoPreguntaUnica(String textoPreguntaUnica) {
        this.textoPreguntaUnica = textoPreguntaUnica;
    }

    public Integer getIdTipoPreguntaUnica() {
        return idTipoPreguntaUnica;
    }

    public void setIdTipoPreguntaUnica(Integer idTipoPreguntaUnica) {
        this.idTipoPreguntaUnica = idTipoPreguntaUnica;
    }

    public Integer getIdClasificacionUnica() {
        return idClasificacionUnica;
    }

    public void setIdClasificacionUnica(Integer idClasificacionUnica) {
        this.idClasificacionUnica = idClasificacionUnica;
    }

    public int getOrdenEnEncuesta() {
        return ordenEnEncuesta;
    }

    public void setOrdenEnEncuesta(int ordenEnEncuesta) {
        this.ordenEnEncuesta = ordenEnEncuesta;
    }

    public boolean isEsPreguntaDescarte() {
        return esPreguntaDescarte;
    }

    public void setEsPreguntaDescarte(boolean esPreguntaDescarte) {
        this.esPreguntaDescarte = esPreguntaDescarte;
    }

    public String getCriterioDescarteValor() {
        return criterioDescarteValor;
    }

    public void setCriterioDescarteValor(String criterioDescarteValor) {
        this.criterioDescarteValor = criterioDescarteValor;
    }

    public PreguntaBanco getPreguntaDelBanco() {
        return preguntaDelBanco;
    }

    public void setPreguntaDelBanco(PreguntaBanco preguntaDelBanco) {
        this.preguntaDelBanco = preguntaDelBanco;
    }

    public String getTextoPreguntaMostrable() {
        if (preguntaDelBanco != null) {
            return preguntaDelBanco.getTextoPregunta();
        }
        return textoPreguntaUnica;
    }


    @Override
    public String toString() {
        return "EncuestaDetallePregunta{" +
               "idEncuestaDetalle=" + idEncuestaDetalle +
               ", idEncuesta=" + idEncuesta +
               ", ordenEnEncuesta=" + ordenEnEncuesta +
               ", esPreguntaDescarte=" + esPreguntaDescarte +
               (idPreguntaBanco != null ? ", idPreguntaBanco=" + idPreguntaBanco : ", textoPreguntaUnica='" + textoPreguntaUnica + '\'') +
               '}';
    }
}
