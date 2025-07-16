/*
 * Responsable: Giancarlo Janampa Frisancho (Implementación completada por Asistente de AED)
 * Relación con otras partes del código:
 * - Dependencia clave para ServicioEncuestas y ServicioPreguntas al asociar preguntas
 * a tipos existentes o al obtener detalles completos de preguntas.
 * - Utilizado por UIConfigurarPreguntasEncuesta para poblar desplegables de tipos de pregunta.
 * Funcionalidad:
 * - Objeto de Acceso a Datos (DAO) para la entidad TipoPregunta.
 * - Gestiona las operaciones de persistencia (CRUD) para los tipos de pregunta.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (No implementa algoritmos de ordenamiento o estructuras complejas).
 */
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importar Statement
import java.util.ArrayList;
import java.util.List;

public class TipoPreguntaDAO {

    /**
     * Obtiene un TipoPregunta por su nombre.
     *
     * @param nombre El nombre del tipo de pregunta a buscar (ej. "Simple", "Múltiple").
     * @return Un objeto TipoPregunta si se encuentra, null en caso contrario.
     */
    public TipoPregunta obtenerTipoPreguntaPorNombre(String nombre) {
        String sql = "SELECT id_tipo_pregunta, nombre_tipo FROM tipos_pregunta WHERE nombre_tipo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoPregunta tipo = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) {
                System.err.println("TipoPreguntaDAO: No se pudo conectar a la BD para obtener tipo por nombre.");
                return null;
            }
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                tipo = new TipoPregunta();
                tipo.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                tipo.setNombreTipo(rs.getString("nombre_tipo"));
            }
        } catch (SQLException e) {
            System.err.println("TipoPreguntaDAO: Error SQL al obtener tipo de pregunta por nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return tipo;
    }

    /**
     * Obtiene un TipoPregunta por su ID.
     *
     * @param id El ID del tipo de pregunta a buscar.
     * @return Un objeto TipoPregunta si se encuentra, null en caso contrario.
     */
    public TipoPregunta obtenerTipoPreguntaPorId(int id) {
        String sql = "SELECT id_tipo_pregunta, nombre_tipo FROM tipos_pregunta WHERE id_tipo_pregunta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoPregunta tipo = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) {
                System.err.println("TipoPreguntaDAO: No se pudo conectar a la BD para obtener tipo por ID.");
                return null;
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                tipo = new TipoPregunta();
                tipo.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                tipo.setNombreTipo(rs.getString("nombre_tipo"));
            }
        } catch (SQLException e) {
            System.err.println("TipoPreguntaDAO: Error SQL al obtener tipo de pregunta por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return tipo;
    }

    /**
     * Sobrecarga de obtenerTipoPreguntaPorId para aceptar un Integer.
     *
     * @param id El ID del tipo de pregunta a buscar (puede ser null).
     * @return Un objeto TipoPregunta si se encuentra y el ID no es null, null en caso contrario.
     */
    public TipoPregunta obtenerTipoPreguntaPorId(Integer id) {
        if (id == null) return null;
        return obtenerTipoPreguntaPorId(id.intValue());
    }

    /**
     * Obtiene una lista de todos los tipos de pregunta.
     * @return Una lista de objetos TipoPregunta.
     */
    public List<TipoPregunta> obtenerTodosLosTipos() {
        List<TipoPregunta> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo_pregunta, nombre_tipo FROM tipos_pregunta ORDER BY nombre_tipo ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return tipos;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                TipoPregunta tipo = new TipoPregunta();
                tipo.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                tipo.setNombreTipo(rs.getString("nombre_tipo"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            System.err.println("TipoPreguntaDAO: Error SQL al obtener todos los tipos de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return tipos;
    }

    /**
     * Crea un nuevo tipo de pregunta en la base de datos.
     * @param tipo El objeto TipoPregunta a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean crearTipoPregunta(TipoPregunta tipo) {
        String sql = "INSERT INTO tipos_pregunta (nombre_tipo) VALUES (?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Para poder obtener el ID si es necesario
            ps.setString(1, tipo.getNombreTipo());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    tipo.setIdTipoPregunta(rs.getInt(1)); // Asigna el ID generado al objeto
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("TipoPreguntaDAO: Error SQL al crear tipo de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    /**
     * Actualiza un tipo de pregunta existente.
     * @param tipo El objeto TipoPregunta con la información actualizada.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarTipoPregunta(TipoPregunta tipo) {
        String sql = "UPDATE tipos_pregunta SET nombre_tipo = ? WHERE id_tipo_pregunta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, tipo.getNombreTipo());
            ps.setInt(2, tipo.getIdTipoPregunta());
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TipoPreguntaDAO: Error SQL al actualizar tipo de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    /**
     * Elimina un tipo de pregunta por su ID.
     * @param idTipoPregunta El ID del tipo de pregunta a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarTipoPregunta(int idTipoPregunta) {
        String sql = "DELETE FROM tipos_pregunta WHERE id_tipo_pregunta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idTipoPregunta);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TipoPreguntaDAO: Error SQL al eliminar tipo de pregunta: " + e.getMessage());
            e.printStackTrace();
            // SQLException con código de error 1451 (Cannot delete or update a parent row: a foreign key constraint fails)
            // indicaría que el tipo de pregunta está en uso.
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}