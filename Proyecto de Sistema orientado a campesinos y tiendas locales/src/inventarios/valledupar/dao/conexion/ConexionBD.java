package inventarios.valledupar.dao.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USUARIO = "C##inventarios";
    private static final String CONTRASENA = "1234";

    private static Connection conexion = null;

    private ConexionBD() {}

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e.getMessage());
        }
    }
}