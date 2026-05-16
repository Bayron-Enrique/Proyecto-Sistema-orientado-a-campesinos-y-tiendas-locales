package inventarios.valledupar.dao;

import inventarios.valledupar.model.Producto;
import java.util.List;

public interface IProductoDAO {

    void guardar(Producto producto);
    Producto buscarPorId(int idProducto);
    List<Producto> buscarTodos();
    List<Producto> buscarPorCategoria(int idCategoria);
    List<Producto> buscarPorProveedor(int idProveedor);
    List<Producto> buscarConStockBajo();
    void actualizar(Producto producto);
    void eliminar(int idProducto);
}