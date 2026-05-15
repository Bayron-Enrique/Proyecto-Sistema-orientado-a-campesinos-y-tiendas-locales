package inventarios.valledupar.model;

public class Reporte {

    private int idReporte;
    private int idUsuario;
    private String tipoReporte;
    private String fechaGeneracion;
    private String fechaInicio;
    private String fechaFin;
    private String contenido;

    public Reporte() {}

    public Reporte(int idReporte, int idUsuario, String tipoReporte,
                   String fechaGeneracion, String fechaInicio,
                   String fechaFin, String contenido) {
        this.idReporte = idReporte;
        this.idUsuario = idUsuario;
        this.tipoReporte = tipoReporte;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.contenido = contenido;
    }

    public int getIdReporte() { return idReporte; }
    public void setIdReporte(int idReporte) { this.idReporte = idReporte; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }

    public String getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(String fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}