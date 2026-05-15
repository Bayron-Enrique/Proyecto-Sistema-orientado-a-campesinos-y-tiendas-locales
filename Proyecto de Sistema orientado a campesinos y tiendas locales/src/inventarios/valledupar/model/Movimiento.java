package inventarios.valledupar.model;

public class Movimiento {

    private int idMovimiento;
    private int idProducto;
    private int idUsuario;
    private String tipoMovimiento;
    private int cantidad;
    private String fechaMovimiento;
    private String observacion;
    private int stockResultante;

    public Movimiento() {}

    public Movimiento(int idMovimiento, int idProducto, int idUsuario,
                      String tipoMovimiento, int cantidad, String fechaMovimiento,
                      String observacion, int stockResultante) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.observacion = observacion;
        this.stockResultante = stockResultante;
    }

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(String fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public int getStockResultante() { return stockResultante; }
    public void setStockResultante(int stockResultante) { this.stockResultante = stockResultante; }

    public boolean esEntrada() {
        return "entrada".equalsIgnoreCase(this.tipoMovimiento);
    }

    public int calcularStockResultante(int stockActual) {
        if ("entrada".equalsIgnoreCase(tipoMovimiento)) {
            return stockActual + cantidad;
        } else if ("salida".equalsIgnoreCase(tipoMovimiento)) {
            return stockActual - cantidad;
        } else {
            return cantidad;
        }
    }
}