package inventarios.valledupar.dao;

import inventarios.valledupar.model.Usuario;
import java.util.List;

public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public void guardar(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Usuario buscarPorId(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Usuario> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actualizar(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eliminar(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}