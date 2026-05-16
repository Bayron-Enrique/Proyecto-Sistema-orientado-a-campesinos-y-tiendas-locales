package inventarios.valledupar.service;

import inventarios.valledupar.dao.IUsuarioDAO;
import inventarios.valledupar.dao.UsuarioDAO;
import inventarios.valledupar.model.Usuario;
import java.util.List;

public class UsuarioService {

    private IUsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void registrarUsuario(Usuario usuario) {
        usuarioDAO.guardar(usuario);
    }

    public Usuario buscarPorId(int idUsuario) {
        return usuarioDAO.buscarPorId(idUsuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.buscarTodos();
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    public void eliminarUsuario(int idUsuario) {
        usuarioDAO.eliminar(idUsuario);
    }

    public Usuario autenticar(String correo, String contrasena) {
        return usuarioDAO.autenticar(correo, contrasena);
    }
}