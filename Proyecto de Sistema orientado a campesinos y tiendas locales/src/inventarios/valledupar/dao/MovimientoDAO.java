package inventarios.valledupar.dao;

import inventarios.valledupar.model.Movimiento;
import java.util.List;

public class MovimientoDAO implements IMovimientoDAO {

    @Override
    public void guardar(Movimiento movimiento) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Movimiento buscarPorId(int idMovimiento) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Movimiento> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Movimiento> buscarPorProducto(int idProducto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Movimiento> buscarPorPeriodo(String fechaInicio, String fechaFin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}