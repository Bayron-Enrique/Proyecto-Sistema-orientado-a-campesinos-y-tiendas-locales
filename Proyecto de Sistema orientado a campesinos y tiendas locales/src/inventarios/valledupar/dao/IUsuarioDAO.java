package inventarios.valledupar.dao;

import inventarios.valledupar.model.Usuario;
import java.util.List;

public interface IUsuarioDAO {

    void guardar(Usuario usuario);
    Usuario buscarPorId(int idUsuario);
    List<Usuario> buscarTodos();
    void actualizar(Usuario usuario);
    void eliminar(int idUsuario);
    Usuario autenticar(String correo, String contrasena);
}