package SteveJobs.encuestas.modelo;

/**
 * Representa una pregunta tal como está configurada dentro de una encuesta específica.
 * Puede ser una pregunta del banco (referenciada por idPreguntaBanco)
 * o una pregunta única creada para esa encuesta (textoPreguntaUnica).
 *
 * @author José Flores
 */
public class EncuestaDetallePregunta {
    private int idEncuestaDetalle; // Clave primaria
    private int idEncuesta;
    private Integer idPreguntaBanco; // FK a PreguntaBanco, puede ser null si es pregunta única
    private String textoPreguntaUnica; // Solo si es pregunta única
    private Integer idTipoPreguntaUnica; // FK a TipoPregunta, solo si es pregunta única
    private Integer idClasificacionUnica; // FK a ClasificacionPregunta, solo si es pregunta única
    private int ordenEnEncuesta;
    private boolean esPreguntaDescarte;
    private String criterioDescarteValor; // Valor para descarte o para opciones (ej. "Sí", "No", "5")

    // Objetos relacionados para facilitar el acceso en la lógica de negocio/UI
    private PreguntaBanco preguntaDelBanco; // Objeto PreguntaBanco si idPreguntaBanco no es null
    private TipoPregunta tipoPreguntaObj; // Objeto TipoPregunta (ya sea del banco o única)
    private ClasificacionPregunta clasificacionPreguntaObj; // Objeto ClasificacionPregunta (ya sea del banco o única)


    public EncuestaDetallePregunta() {
    }

    // Constructor para preguntas del banco
    public EncuestaDetallePregunta(int idEncuesta, int idPreguntaBanco, int ordenEnEncuesta, boolean esPreguntaDescarte, String criterioDescarteValor) {
        this.idEncuesta = idEncuesta;
        this.idPreguntaBanco = idPreguntaBanco;
        this.ordenEnEncuesta = ordenEnEncuesta;
        this.esPreguntaDescarte = esPreguntaDescarte;
        this.criterioDescarteValor = criterioDescarteValor;
    }

    // Constructor para preguntas únicas
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

    // Getters y setters para los nuevos objetos de tipo y clasificación
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

    // Nuevo: Getter para obtener el texto de la pregunta a mostrar
    // Esto es útil porque la pregunta puede venir del banco o ser única
    public String getTextoPreguntaMostrable() {
        if (preguntaDelBanco != null && preguntaDelBanco.getTextoPregunta() != null) {
            return preguntaDelBanco.getTextoPregunta();
        }
        return textoPreguntaUnica;
    }

    // Nuevo: Método para obtener el nombre del tipo de pregunta de forma unificada
    public String getNombreTipoPregunta() {
        if (preguntaDelBanco != null && preguntaDelBanco.getNombreTipoPregunta() != null) {
            return preguntaDelBanco.getNombreTipoPregunta();
        }
        if (tipoPreguntaObj != null && tipoPreguntaObj.getNombreTipo() != null) {
            return tipoPreguntaObj.getNombreTipo();
        }
        return null;
    }

    // Nuevo: Método para obtener el nombre de la clasificación de forma unificada
    public String getNombreClasificacionPregunta() {
        if (preguntaDelBanco != null && preguntaDelBanco.getNombreClasificacion() != null) {
            return preguntaDelBanco.getNombreClasificacion();
        }
        if (clasificacionPreguntaObj != null && clasificacionPreguntaObj.getNombreClasificacion() != null) {
            return clasificacionPreguntaObj.getNombreClasificacion();
        }
        return null;
    }

    // Nuevo: Método para obtener el ID de tipo de pregunta efectivo
    public Integer getIdTipoPreguntaEfectivo() {
        if (preguntaDelBanco != null) {
            Integer tipoPreguntaDelBanco = preguntaDelBanco.getIdTipoPregunta();
            if (tipoPreguntaDelBanco != null) {
                return tipoPreguntaDelBanco;
            }
        }
        return idTipoPreguntaUnica;
    }

    // Nuevo: Método para obtener el ID de clasificación efectivo
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