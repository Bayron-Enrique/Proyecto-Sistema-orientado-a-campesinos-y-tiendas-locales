package inventarios.valledupar.dao;

import inventarios.valledupar.dao.conexion.ConexionBD;
import inventarios.valledupar.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    private Connection conn;

    public ProductoDAO() {
        this.conn = ConexionBD.getConexion();
    }

    @Override
public void guardar(Producto producto) {

    String sql =
        "INSERT INTO productos (" +
        "ID_PRODUCTO, NOMBRE_PRODUCTO, DESCRIPCION, ID_CATEGORIA, ID_PROVEEDOR, " +
        "PRECIO_COMPRA, PRECIO_VENTA, STOCK_ACTUAL, STOCK_MINIMO, " +
        "UNIDAD_MEDIDA, ESTADO, FECHA_CREACION" +
        ") VALUES (" +
        "seq_productos.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, producto.getNombreProducto());
        ps.setString(2, producto.getDescripcion());
        ps.setInt(3, producto.getIdCategoria());
        ps.setInt(4, producto.getIdProveedor());
        ps.setDouble(5, producto.getPrecioCompra());
        ps.setDouble(6, producto.getPrecioVenta());
        ps.setInt(7, producto.getStockActual());
        ps.setInt(8, producto.getStockMinimo());
        ps.setString(9, producto.getUnidadMedida());
        ps.setString(10, producto.getEstado());

        ps.executeUpdate();

        System.out.println("Producto guardado correctamente");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @Override
    public Producto buscarPorId(int idProducto) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearProducto(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

   @Override
public List<Producto> buscarTodos() {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT * FROM productos";

    try (Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {

            System.out.println(
                rs.getInt("id_producto")
                + " - "
                + rs.getString("nombre_producto")
            );

            lista.add(mapearProducto(rs));
        }

        System.out.println("Productos encontrados: " + lista.size());

    } catch (SQLException e) {
        System.out.println("Error al listar productos: " + e.getMessage());
    }

    return lista;
}

    @Override
    public List<Producto> buscarPorCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE id_categoria = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por categoria: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> buscarPorProveedor(int idProveedor) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por proveedor: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Producto> buscarConStockBajo() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock_actual <= stock_minimo";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar stock bajo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre_producto=?, descripcion=?, id_categoria=?, id_proveedor=?, precio_compra=?, precio_venta=?, stock_actual=?, stock_minimo=?, unidad_medida=?, estado=? WHERE id_producto=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getIdCategoria());
            ps.setInt(4, producto.getIdProveedor());
            ps.setDouble(5, producto.getPrecioCompra());
            ps.setDouble(6, producto.getPrecioVenta());
            ps.setInt(7, producto.getStockActual());
            ps.setInt(8, producto.getStockMinimo());
            ps.setString(9, producto.getUnidadMedida());
            ps.setString(10, producto.getEstado());
            ps.setInt(11, producto.getIdProducto());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id_producto"),
            rs.getString("nombre_producto"),
            rs.getString("descripcion"),
            rs.getInt("id_categoria"),
            rs.getInt("id_proveedor"),
            rs.getDouble("precio_compra"),
            rs.getDouble("precio_venta"),
            rs.getInt("stock_actual"),
            rs.getInt("stock_minimo"),
            rs.getString("unidad_medida"),
            rs.getString("estado")
        );
    }
}