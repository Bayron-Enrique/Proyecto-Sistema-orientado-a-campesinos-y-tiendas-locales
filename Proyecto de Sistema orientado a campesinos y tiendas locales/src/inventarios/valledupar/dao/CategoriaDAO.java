package inventarios.valledupar.dao;

import inventarios.valledupar.dao.conexion.ConexionBD;
import inventarios.valledupar.model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements ICategoriaDAO {

    private Connection conn;

    public CategoriaDAO() {
        this.conn = ConexionBD.getConexion();
    }

    @Override
    public void guardar(Categoria categoria) {
        String sql = "INSERT INTO categorias VALUES (seq_categorias.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setString(3, categoria.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar categoria: " + e.getMessage());
        }
    }

    @Override
    public Categoria buscarPorId(int idCategoria) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearCategoria(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar categoria: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Categoria> buscarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearCategoria(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar categorias: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre=?, descripcion=?, estado=? WHERE id_categoria=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setString(3, categoria.getEstado());
            ps.setInt(4, categoria.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar categoria: " + e.getMessage());
        }
    }

private Categoria mapearCategoria(ResultSet rs) throws SQLException {
    return new Categoria(
        rs.getInt("ID_CATEGORIA"),
        rs.getString("NOMBRE_CATEGORIA"),
        rs.getString("DESCRIPCION"),
        "activo"
    );
}