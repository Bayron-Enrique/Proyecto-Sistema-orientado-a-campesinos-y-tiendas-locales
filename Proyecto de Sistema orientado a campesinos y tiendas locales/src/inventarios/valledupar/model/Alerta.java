package inventarios.valledupar.model;

public class Alerta {

    private int idAlerta;
    private int idProducto;
    private String fechaAlerta;
    private int stockAlMomento;
    private String estadoAlerta;

    public Alerta() {}

    public Alerta(int idAlerta, int idProducto, String fechaAlerta,
                  int stockAlMomento, String estadoAlerta) {
        this.idAlerta = idAlerta;
        this.idProducto = idProducto;
        this.fechaAlerta = fechaAlerta;
        this.stockAlMomento = stockAlMomento;
        this.estadoAlerta = estadoAlerta;
    }

    public int getIdAlerta() { return idAlerta; }
    public void setIdAlerta(int idAlerta) { this.idAlerta = idAlerta; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getFechaAlerta() { return fechaAlerta; }
    public void setFechaAlerta(String fechaAlerta) { this.fechaAlerta = fechaAlerta; }

    public int getStockAlMomento() { return stockAlMomento; }
    public void setStockAlMomento(int stockAlMomento) { this.stockAlMomento = stockAlMomento; }

    public String getEstadoAlerta() { return estadoAlerta; }
    public void setEstadoAlerta(String estadoAlerta) { this.estadoAlerta = estadoAlerta; }

    public void marcarAtendida() {
        this.estadoAlerta = "atendida";
    }

    public boolean esPendiente() {
        return "pendiente".equalsIgnoreCase(this.estadoAlerta);
    }
}