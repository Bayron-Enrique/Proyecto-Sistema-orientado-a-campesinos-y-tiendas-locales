package inventarios.valledupar.dao;

import inventarios.valledupar.model.Producto;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    @Override
    public void guardar(Producto producto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Producto> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Producto> buscarPorCategoria(int idCategoria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Producto> buscarPorProveedor(int idProveedor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Producto> buscarConStockBajo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actualizar(Producto producto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eliminar(int idProducto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}