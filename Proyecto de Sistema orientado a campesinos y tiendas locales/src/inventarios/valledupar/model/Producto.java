package inventarios.valledupar.model;

public class Producto {

    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private int idCategoria;
    private int idProveedor;
    private double precioCompra;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;
    private String unidadMedida;
    private String estado;

    public Producto() {}

    public Producto(int idProducto, String nombreProducto, String descripcion,
                    int idCategoria, int idProveedor, double precioCompra,
                    double precioVenta, int stockActual, int stockMinimo,
                    String unidadMedida, String estado) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.idProveedor = idProveedor;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.unidadMedida = unidadMedida;
        this.estado = estado;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean tieneStockBajo() {
        return this.stockActual <= this.stockMinimo;
    }

    public void actualizarStock(int cantidad, String tipoMovimiento) {
        if ("entrada".equalsIgnoreCase(tipoMovimiento)) {
            this.stockActual += cantidad;
        } else if ("salida".equalsIgnoreCase(tipoMovimiento)) {
            this.stockActual -= cantidad;
        } else if ("ajuste".equalsIgnoreCase(tipoMovimiento)) {
            this.stockActual = cantidad;
        }
    }

    public double getMargenGanancia() {
        return this.precioVenta - this.precioCompra;
    }
}