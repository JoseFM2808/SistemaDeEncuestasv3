package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoPreguntaDAO {
    
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

    public TipoPregunta obtenerTipoPreguntaPorId(Integer id) {
        if (id == null) return null;
        return obtenerTipoPreguntaPorId(id.intValue());
    }

    // Renombrado el método para consistencia con lo que se espera desde el servicio
    public List<TipoPregunta> obtenerTodosLosTiposPregunta() { //
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

    public boolean crearTipoPregunta(TipoPregunta tipo) {
        String sql = "INSERT INTO tipos_pregunta (nombre_tipo) VALUES (?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tipo.getNombreTipo());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    tipo.setIdTipoPregunta(rs.getInt(1));
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
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}