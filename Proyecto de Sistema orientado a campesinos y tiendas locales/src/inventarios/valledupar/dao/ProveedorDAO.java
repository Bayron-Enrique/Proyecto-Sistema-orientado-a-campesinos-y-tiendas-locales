package inventarios.valledupar.dao;

import inventarios.valledupar.dao.conexion.ConexionBD;
import inventarios.valledupar.model.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO implements IProveedorDAO {

    private Connection conn;

    public ProveedorDAO() {
        this.conn = ConexionBD.getConexion();
    }

    @Override
    public void guardar(Proveedor proveedor) {
        // Sin NEXTVAL: IDENTITY. Columnas explícitas con nombre correcto
        String sql = "INSERT INTO proveedores (nombre_proveedor, telefono, correo, direccion, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getCorreo());
            ps.setString(4, proveedor.getDireccion());
            ps.setString(5, proveedor.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar proveedor: " + e.getMessage());
        }
    }

    @Override
    public Proveedor buscarPorId(int idProveedor) {
        String sql = "SELECT * FROM proveedores WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearProveedor(rs);
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Proveedor> buscarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores ORDER BY id_proveedor";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearProveedor(rs));
        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre_proveedor=?, telefono=?, correo=?, direccion=?, estado=? WHERE id_proveedor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getCorreo());
            ps.setString(4, proveedor.getDireccion());
            ps.setString(5, proveedor.getEstado());
            ps.setInt(6, proveedor.getIdProveedor());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idProveedor) {
        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar proveedor: " + e.getMessage());
        }
    }

    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        return new Proveedor(
            rs.getInt("id_proveedor"),
            rs.getString("nombre_proveedor"),
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("direccion"),
            rs.getString("estado")
        );
    }
}