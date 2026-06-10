package inventarios.valledupar.service;

import inventarios.valledupar.dao.IProveedorDAO;
import inventarios.valledupar.dao.ProveedorDAO;
import inventarios.valledupar.model.Proveedor;
import java.util.List;

public class ProveedorService {

    private IProveedorDAO proveedorDAO;

    public ProveedorService() {
        this.proveedorDAO = new ProveedorDAO();
    }

    public void registrar(Proveedor proveedor) {
        proveedorDAO.guardar(proveedor);
    }

    public void actualizar(Proveedor proveedor) {
        proveedorDAO.actualizar(proveedor);
    }

    public void eliminar(int idProveedor) {
        proveedorDAO.eliminar(idProveedor);
    }

    public Proveedor buscarPorId(int id) {
        return proveedorDAO.buscarPorId(id);
    }

    public List<Proveedor> listarTodos() {
        return proveedorDAO.buscarTodos();
    }
}