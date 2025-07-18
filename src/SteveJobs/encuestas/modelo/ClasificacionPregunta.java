package SteveJobs.encuestas.modelo;

public class ClasificacionPregunta {
    
    private int idClasificacion;
    private String nombreClasificacion;
    private String descripcion;
    private String estado;

    public ClasificacionPregunta() {
    }

    // Nuevo constructor para uso en UI
    public ClasificacionPregunta(String nombreClasificacion, String descripcion, String estado) {
        this.nombreClasificacion = nombreClasificacion;
        this.descripcion = descripcion;
        this.estado = estado;
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
        
}
