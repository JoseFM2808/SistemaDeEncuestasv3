/*
 * Responsable: Equipo de Desarrollo (Revisado y Unificado, liderado por José Flores)
 * Relación con otras partes del código:
 * - Es una dependencia fundamental para todas las clases DAO, proporcionando los medios
 * para establecer y cerrar conexiones con la base de datos.
 * Funcionalidad:
 * - Gestiona la conexión con la base de datos MySQL, cargando el driver JDBC y
 * ofreciendo métodos estáticos para abrir y cerrar recursos (Connection, Statement, ResultSet)
 * de forma segura.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Clase de utilidad para la gestión de recursos de base de datos, no maneja datos en sí).
 */
package SteveJobs.encuestas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/sistemadeencuesta?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "password123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error Crítico: Driver MySQL (com.mysql.cj.jdbc.Driver) no encontrado. Asegúrese de que el conector JAR de MySQL 8.x esté en el classpath del proyecto.");
        }
    }

    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            System.err.println("Error de Conexión BD: No se pudo establecer la conexión con '" + URL + "'. Mensaje: " + e.getMessage());
        }
        return con;
    }

    public static void cerrar(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
    }

    public static void cerrar(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Statement: " + e.getMessage());
            }
        }
    }

    public static void cerrar(PreparedStatement ps) {
        cerrar((Statement) ps);
    }

    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
            }
        }
    }

    public static void cerrar(ResultSet rs, Statement stmt, Connection con) {
        cerrar(rs);
        cerrar(stmt);
        cerrar(con);
    }

    public static void cerrar(Statement stmt, Connection con) {
        cerrar(stmt);
        cerrar(con);
    }
}
