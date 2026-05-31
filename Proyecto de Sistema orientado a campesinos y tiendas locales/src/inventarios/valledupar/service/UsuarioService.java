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

    public Usuario autenticar(String correo, String contrasena) {
        return usuarioDAO.autenticar(correo, contrasena);
    }

    public void registrar(Usuario usuario) {
        usuarioDAO.guardar(usuario);
    }

    public void actualizar(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    public void eliminar(int idUsuario) {
        usuarioDAO.eliminar(idUsuario);
    }

    public Usuario buscarPorId(int idUsuario) {
        return usuarioDAO.buscarPorId(idUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.buscarTodos();
    }
}