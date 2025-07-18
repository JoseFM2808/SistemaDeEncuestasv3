package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PreguntaBancoDAO {

    public int crearPreguntaBanco(PreguntaBanco pregunta) {
        String sql = "INSERT INTO banco_preguntas (texto_pregunta, id_tipo_pregunta, id_clasificacion) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        int idGenerado = -1;

        try {
            con = ConexionDB.conectar();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pregunta.getTextoPregunta());
            ps.setInt(2, pregunta.getIdTipoPregunta());

            if (pregunta.getIdClasificacion() != null && pregunta.getIdClasificacion() > 0) {
                ps.setInt(3, pregunta.getIdClasificacion());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            if (ps.executeUpdate() > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al crear pregunta en banco: " + e.getMessage());
        } finally {
            ConexionDB.cerrar(generatedKeys, ps, con);
        }
        return idGenerado;
    }

    public PreguntaBanco obtenerPreguntaPorId(int idPreguntaBanco) {
        String sql = "SELECT pb.*, tp.nombre_tipo, cp.nombre_clasificacion " +
                     "FROM banco_preguntas pb " +
                     "JOIN tipos_pregunta tp ON pb.id_tipo_pregunta = tp.id_tipo_pregunta " +
                     "LEFT JOIN clasificaciones_pregunta cp ON pb.id_clasificacion = cp.id_clasificacion " +
                     "WHERE pb.id_pregunta_banco = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreguntaBanco pregunta = null;

        try {
            con = ConexionDB.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPreguntaBanco);
            rs = ps.executeQuery();

            if (rs.next()) {
                pregunta = new PreguntaBanco();
                pregunta.setIdPreguntaBanco(rs.getInt("id_pregunta_banco"));
                pregunta.setTextoPregunta(rs.getString("texto_pregunta"));
                pregunta.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                pregunta.setIdClasificacion((Integer) rs.getObject("id_clasificacion")); 
                
                pregunta.setNombreTipoPregunta(rs.getString("nombre_tipo"));
                pregunta.setNombreClasificacion(rs.getString("nombre_clasificacion"));
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener pregunta por ID: " + e.getMessage());
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return pregunta;
    }

    public List<PreguntaBanco> obtenerTodasLasPreguntas() {
        List<PreguntaBanco> preguntas = new ArrayList<>();
        String sql = "SELECT pb.*, tp.nombre_tipo, cp.nombre_clasificacion " +
                     "FROM banco_preguntas pb " +
                     "JOIN tipos_pregunta tp ON pb.id_tipo_pregunta = tp.id_tipo_pregunta " +
                     "LEFT JOIN clasificaciones_pregunta cp ON pb.id_clasificacion = cp.id_clasificacion " +
                     "ORDER BY pb.id_pregunta_banco";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PreguntaBanco pregunta = new PreguntaBanco();
                pregunta.setIdPreguntaBanco(rs.getInt("id_pregunta_banco"));
                pregunta.setTextoPregunta(rs.getString("texto_pregunta"));
                pregunta.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                pregunta.setIdClasificacion((Integer) rs.getObject("id_clasificacion"));
                
                // Atributos extra para visualización
                pregunta.setNombreTipoPregunta(rs.getString("nombre_tipo"));
                pregunta.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                
                preguntas.add(pregunta);
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener todas las preguntas: " + e.getMessage());
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return preguntas;
    }

    public boolean actualizarPreguntaBanco(PreguntaBanco pregunta) {
        String sql = "UPDATE banco_preguntas SET texto_pregunta = ?, id_tipo_pregunta = ?, id_clasificacion = ? WHERE id_pregunta_bancoo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, pregunta.getTextoPregunta());
            ps.setInt(2, pregunta.getIdTipoPregunta());
            if (pregunta.getIdClasificacion() != null && pregunta.getIdClasificacion() > 0) {
                ps.setInt(3, pregunta.getIdClasificacion());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setInt(4, pregunta.getIdPreguntaBanco());

            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DAO Error al actualizar pregunta del banco: " + e.getMessage());
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean eliminarPreguntaBanco(int idPreguntaBanco) {
        String sql = "DELETE FROM banco_preguntas WHERE id_pregunta_banco = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPreguntaBanco);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DAO Error al eliminar pregunta del banco: " + e.getMessage());
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}