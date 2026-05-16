package inventarios.valledupar.dao;

import inventarios.valledupar.model.Proveedor;
import java.util.List;

public interface IProveedorDAO {

    void guardar(Proveedor proveedor);
    Proveedor buscarPorId(int idProveedor);
    List<Proveedor> buscarTodos();
    void actualizar(Proveedor proveedor);
    void eliminar(int idProveedor);
}