package inventarios.valledupar.dao;

import inventarios.valledupar.dao.conexion.ConexionBD;
import inventarios.valledupar.model.Alerta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO implements IAlertaDAO {

    private Connection conn;

    public AlertaDAO() {
        this.conn = ConexionBD.getConexion();
    }

    @Override
    public void guardar(Alerta alerta) {
        String sql = "INSERT INTO alertas (id_alerta, id_producto, fecha_alerta, stock_al_momento, estado_alerta, activo, fecha_creacion) "
                   + "VALUES (seq_alertas.NEXTVAL, ?, ?, ?, ?, 1, SYSDATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alerta.getIdProducto());
            ps.setDate(2, alerta.getFechaAlerta());
            ps.setInt(3, alerta.getStockAlMomento());
            ps.setString(4, alerta.getEstadoAlerta());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar alerta: " + e.getMessage());
        }
    }

    @Override
    public List<Alerta> buscarPendientes() {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE estado_alerta = 'pendiente' AND activo = 1";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearAlerta(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar alertas pendientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Alerta> buscarTodas() {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE activo = 1";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearAlerta(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar alertas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void marcarAtendida(int idAlerta) {
        String sql = "UPDATE alertas SET estado_alerta = 'atendida', fecha_atencion = SYSDATE WHERE id_alerta = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAlerta);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al marcar alerta: " + e.getMessage());
        }
    }

    private Alerta mapearAlerta(ResultSet rs) throws SQLException {
        return new Alerta(
            rs.getInt("id_alerta"),
            rs.getInt("id_producto"),
            rs.getDate("fecha_alerta"),
            rs.getInt("stock_al_momento"),
            rs.getString("estado_alerta")
        );
    }
}