package inventarios.valledupar.dao;

import inventarios.valledupar.dao.conexion.ConexionBD;
import inventarios.valledupar.model.Movimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO implements IMovimientoDAO {

    private Connection conn;

    public MovimientoDAO() {
        this.conn = ConexionBD.getConexion();
    }

    @Override
    public void guardar(Movimiento movimiento) {
        // CORRECCIÓN: ya no se usa TO_DATE ni formato de texto, se pasa Date directamente
        String sql = "INSERT INTO movimientos (id_movimiento, id_producto, id_usuario, tipo_movimiento, cantidad, fecha_movimiento, observacion, stock_resultante, activo, fecha_creacion) "
                   + "VALUES (seq_movimientos.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, 1, SYSDATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movimiento.getIdProducto());
            ps.setInt(2, movimiento.getIdUsuario());
            ps.setString(3, movimiento.getTipoMovimiento());
            ps.setInt(4, movimiento.getCantidad());
            ps.setDate(5, movimiento.getFechaMovimiento()); // CORRECCIÓN: setDate en vez de setString
            ps.setString(6, movimiento.getObservacion());
            ps.setInt(7, movimiento.getStockResultante());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar movimiento: " + e.getMessage());
        }
    }

    @Override
    public Movimiento buscarPorId(int idMovimiento) {
        String sql = "SELECT * FROM movimientos WHERE id_movimiento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMovimiento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearMovimiento(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar movimiento: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Movimiento> buscarTodos() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Movimiento> buscarPorProducto(int idProducto) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE id_producto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por producto: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Movimiento> buscarPorPeriodo(String fechaInicio, String fechaFin) {
        List<Movimiento> lista = new ArrayList<>();
        // CORRECCIÓN: TO_DATE con formato simple yyyy-MM-dd, sin NLS
        String sql = "SELECT * FROM movimientos WHERE fecha_movimiento BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') + 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por periodo: " + e.getMessage());
        }
        return lista;
    }

    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        return new Movimiento(
            rs.getInt("id_movimiento"),
            rs.getInt("id_producto"),
            rs.getInt("id_usuario"),
            rs.getString("tipo_movimiento"),
            rs.getInt("cantidad"),
            rs.getDate("fecha_movimiento"), // CORRECCIÓN: getDate en vez de getString
            rs.getString("observacion"),
            rs.getInt("stock_resultante")
        );
    }
}