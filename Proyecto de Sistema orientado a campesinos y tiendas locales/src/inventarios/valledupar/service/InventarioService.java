package inventarios.valledupar.service;

import inventarios.valledupar.dao.IProductoDAO;
import inventarios.valledupar.dao.IMovimientoDAO;
import inventarios.valledupar.dao.ProductoDAO;
import inventarios.valledupar.dao.MovimientoDAO;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.model.Movimiento;
import java.util.List;

public class InventarioService {

    private IProductoDAO productoDAO;
    private IMovimientoDAO movimientoDAO;
    private AlertaService alertaService;

    public InventarioService() {
        this.productoDAO = new ProductoDAO();
        this.movimientoDAO = new MovimientoDAO();
        this.alertaService = new AlertaService();
    }

    public void registrarProducto(Producto producto) {
        productoDAO.guardar(producto);
    }

    public Producto buscarProducto(int idProducto) {
        return productoDAO.buscarPorId(idProducto);
    }

    public List<Producto> listarProductos() {
        return productoDAO.buscarTodos();
    }

    public List<Producto> listarPorCategoria(int idCategoria) {
        return productoDAO.buscarPorCategoria(idCategoria);
    }

    public void actualizarProducto(Producto producto) {
        productoDAO.actualizar(producto);
    }

    public void eliminarProducto(int idProducto) {
        productoDAO.eliminar(idProducto);
    }

    public void registrarMovimiento(Movimiento movimiento, Producto producto) {
        int stockNuevo = movimiento.calcularStockResultante(producto.getStockActual());
        movimiento.setStockResultante(stockNuevo);
        producto.setStockActual(stockNuevo);
        movimientoDAO.guardar(movimiento);
        productoDAO.actualizar(producto);
        if (producto.tieneStockBajo()) {
            alertaService.generarAlerta(producto);
        }
    }

    public List<Movimiento> listarMovimientos() {
        return movimientoDAO.buscarTodos();
    }

    public List<Movimiento> listarMovimientosPorPeriodo(String fechaInicio, String fechaFin) {
        return movimientoDAO.buscarPorPeriodo(fechaInicio, fechaFin);
    }

    public List<Producto> listarProductosConStockBajo() {
        return productoDAO.buscarConStockBajo();
    }
}