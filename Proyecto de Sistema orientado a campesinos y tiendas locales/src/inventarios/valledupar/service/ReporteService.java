package inventarios.valledupar.service;

import inventarios.valledupar.dao.IMovimientoDAO;
import inventarios.valledupar.dao.IProductoDAO;
import inventarios.valledupar.dao.MovimientoDAO;
import inventarios.valledupar.dao.ProductoDAO;
import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.model.Reporte;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteService {

    private IProductoDAO productoDAO;
    private IMovimientoDAO movimientoDAO;

    public ReporteService() {
        this.productoDAO = new ProductoDAO();
        this.movimientoDAO = new MovimientoDAO();
    }

    public Reporte generarReporteInventario(int idUsuario) {
        List<Producto> productos = productoDAO.buscarTodos();
        StringBuilder contenido = new StringBuilder();
        for (Producto p : productos) {
            contenido.append(p.getNombreProducto())
                     .append(" - Stock: ").append(p.getStockActual())
                     .append(" - Precio venta: ").append(p.getPrecioVenta())
                     .append("\n");
        }
        Reporte reporte = new Reporte();
        reporte.setIdUsuario(idUsuario);
        reporte.setTipoReporte("inventario_general");
        reporte.setFechaGeneracion(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reporte.setContenido(contenido.toString());
        return reporte;
    }

    public Reporte generarReporteMovimientos(int idUsuario, String fechaInicio, String fechaFin) {
        List<Movimiento> movimientos = movimientoDAO.buscarPorPeriodo(fechaInicio, fechaFin);
        StringBuilder contenido = new StringBuilder();
        for (Movimiento m : movimientos) {
            contenido.append(m.getFechaMovimiento())
                     .append(" - Tipo: ").append(m.getTipoMovimiento())
                     .append(" - Cantidad: ").append(m.getCantidad())
                     .append("\n");
        }
        Reporte reporte = new Reporte();
        reporte.setIdUsuario(idUsuario);
        reporte.setTipoReporte("movimientos_por_periodo");
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setFechaGeneracion(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reporte.setContenido(contenido.toString());
        return reporte;
    }
}