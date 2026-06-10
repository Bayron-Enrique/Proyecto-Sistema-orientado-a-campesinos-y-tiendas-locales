package inventarios.valledupar.service;

import inventarios.valledupar.dao.CategoriaDAO;
import inventarios.valledupar.dao.ICategoriaDAO;
import inventarios.valledupar.model.Categoria;

import java.util.List;

public class CategoriaService {

    private ICategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public void registrar(Categoria categoria) {
        categoriaDAO.guardar(categoria);
    }

    public void actualizar(Categoria categoria) {
        categoriaDAO.actualizar(categoria);
    }

    public void eliminar(int idCategoria) {
        categoriaDAO.eliminar(idCategoria);
    }

    public Categoria buscarPorId(int idCategoria) {
        return categoriaDAO.buscarPorId(idCategoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaDAO.buscarTodas();
    }
}