package inventarios.valledupar.service;

import inventarios.valledupar.dao.IProductoDAO;
import inventarios.valledupar.dao.IMovimientoDAO;
import inventarios.valledupar.dao.ProductoDAO;
import inventarios.valledupar.dao.MovimientoDAO;
import inventarios.valledupar.dao.conexion.ConexionBD;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.model.Movimiento;
import java.sql.Connection;
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
        Connection conn = ConexionBD.getConexion();
        try {
            conn.setAutoCommit(false);

            int stockNuevo = movimiento.calcularStockResultante(producto.getStockActual());
            movimiento.setStockResultante(stockNuevo);
            producto.setStockActual(stockNuevo);

            movimientoDAO.guardar(movimiento);
            productoDAO.actualizar(producto);

            conn.commit(); // confirma INSERT y UPDATE juntos

            if (producto.tieneStockBajo()) {
                alertaService.generarAlerta(producto);
            }

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception ex) { ex.printStackTrace(); }
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