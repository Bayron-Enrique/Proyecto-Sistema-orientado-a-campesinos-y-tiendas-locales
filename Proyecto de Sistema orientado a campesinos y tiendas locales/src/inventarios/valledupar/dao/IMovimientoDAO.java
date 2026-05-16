package inventarios.valledupar.dao;

import inventarios.valledupar.model.Movimiento;
import java.util.List;

public interface IMovimientoDAO {

    void guardar(Movimiento movimiento);
    Movimiento buscarPorId(int idMovimiento);
    List<Movimiento> buscarTodos();
    List<Movimiento> buscarPorProducto(int idProducto);
    List<Movimiento> buscarPorPeriodo(String fechaInicio, String fechaFin);
}