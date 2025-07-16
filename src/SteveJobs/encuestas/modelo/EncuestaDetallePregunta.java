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
    private PreguntaBanco preguntaDelBanco; // Objeto de PreguntaBanco asociado

    // Nuevos atributos para almacenar objetos TipoPregunta y ClasificacionPregunta
    // Esto es útil cuando la pregunta es única (no del banco) o para tener el objeto completo
    private TipoPregunta tipoPreguntaObj;
    private ClasificacionPregunta clasificacionPreguntaObj;


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

    // --- Nuevos Getters y Setters para TipoPreguntaObj y ClasificacionPreguntaObj ---
    public TipoPregunta getTipoPreguntaObj() {
        return tipoPreguntaObj;
    }

    public void setTipoPreguntaObj(TipoPregunta tipoPreguntaObj) {
        this.tipoPreguntaObj = tipoPreguntaObj;
    }

    public ClasificacionPregunta getClasificacionPreguntaObj() {
        return clasificacionPreguntaObj;
    }

    public void setClasificacionPreguntaObj(ClasificacionPregunta clasificacionPreguntaObj) {
        this.clasificacionPreguntaObj = clasificacionPreguntaObj;
    }

    // --- Métodos de Conveniencia para obtener nombres y IDs efectivos ---

    /**
     * Devuelve el texto de la pregunta, ya sea del banco o la única.
     * @return El texto de la pregunta.
     */
    public String getTextoPreguntaMostrable() {
        if (preguntaDelBanco != null && preguntaDelBanco.getTextoPregunta() != null) {
            return preguntaDelBanco.getTextoPregunta();
        }
        return textoPreguntaUnica;
    }

    /**
     * Devuelve el nombre del tipo de pregunta, ya sea del banco o la única.
     * @return El nombre del tipo de pregunta.
     */
    public String getNombreTipoPregunta() {
        if (preguntaDelBanco != null && preguntaDelBanco.getNombreTipoPregunta() != null) {
            return preguntaDelBanco.getNombreTipoPregunta();
        }
        if (tipoPreguntaObj != null && tipoPreguntaObj.getNombreTipo() != null) {
            return tipoPreguntaObj.getNombreTipo();
        }
        return null; // O una cadena vacía si prefieres
    }

    /**
     * Devuelve el nombre de la clasificación de pregunta, ya sea del banco o la única.
     * @return El nombre de la clasificación de pregunta.
     */
    public String getNombreClasificacionPregunta() {
        if (preguntaDelBanco != null && preguntaDelBanco.getNombreClasificacion() != null) {
            return preguntaDelBanco.getNombreClasificacion();
        }
        if (clasificacionPreguntaObj != null && clasificacionPreguntaObj.getNombreClasificacion() != null) {
            return clasificacionPreguntaObj.getNombreClasificacion();
        }
        return null; // O una cadena vacía si prefieres
    }

    /**
     * Devuelve el ID del tipo de pregunta efectivo (del banco o único).
     * @return El ID del tipo de pregunta o null.
     */
    public Integer getIdTipoPreguntaEfectivo() {
        if (preguntaDelBanco != null && preguntaDelBanco.getIdTipoPregunta() != null) {
            return preguntaDelBanco.getIdTipoPregunta();
        }
        return idTipoPreguntaUnica;
    }

    /**
     * Devuelve el ID de la clasificación de pregunta efectiva (del banco o única).
     * @return El ID de la clasificación de pregunta o null.
     */
    public Integer getIdClasificacionEfectivo() {
        if (preguntaDelBanco != null && preguntaDelBanco.getIdClasificacion() != null) {
            return preguntaDelBanco.getIdClasificacion();
        }
        return idClasificacionUnica;
    }


    @Override
    public String toString() {
        return "EncuestaDetallePregunta{" +
               "idEncuestaDetalle=" + idEncuestaDetalle +
               ", idEncuesta=" + idEncuesta +
               ", ordenEnEncuesta=" + ordenEnEncuesta +
               ", esPreguntaDescarte=" + esPreguntaDescarte +
               (idPreguntaBanco != null ? ", idPreguntaBanco=" + idPreguntaBanco : ", textoPreguntaUnica='" + textoPreguntaUnica + '\'') +
               ", tipoPregunta=" + (getNombreTipoPregunta() != null ? getNombreTipoPregunta() : "N/A") +
               ", clasificacion=" + (getNombreClasificacionPregunta() != null ? getNombreClasificacionPregunta() : "N/A") +
               '}';
    }
}