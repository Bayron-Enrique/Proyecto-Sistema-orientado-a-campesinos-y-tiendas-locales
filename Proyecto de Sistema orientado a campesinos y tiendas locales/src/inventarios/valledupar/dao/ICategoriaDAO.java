package inventarios.valledupar.dao;

import inventarios.valledupar.model.Categoria;
import java.util.List;

public interface ICategoriaDAO {

    void guardar(Categoria categoria);
    Categoria buscarPorId(int idCategoria);
    List<Categoria> buscarTodas();
    void actualizar(Categoria categoria);
    void eliminar(int idCategoria);
}