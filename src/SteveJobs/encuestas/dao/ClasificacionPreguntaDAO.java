package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClasificacionPreguntaDAO {


    public ClasificacionPregunta obtenerClasificacionPorNombre(String nombre) {
        String sql = "SELECT id_clasificacion, nombre_clasificacion, descripcion, estado FROM clasificaciones_pregunta WHERE nombre_clasificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClasificacionPregunta clasificacion = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) {
                System.err.println("ClasificacionPreguntaDAO: No se pudo conectar a la BD para obtener clasificación por nombre.");
                return null;
            }
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                clasificacion = new ClasificacionPregunta();
                clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
                clasificacion.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                clasificacion.setDescripcion(rs.getString("descripcion"));
                clasificacion.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.err.println("ClasificacionPreguntaDAO: Error SQL al obtener clasificación de pregunta por nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return clasificacion;
    }


    public ClasificacionPregunta obtenerClasificacionPorId(int id) {
        String sql = "SELECT id_clasificacion, nombre_clasificacion, descripcion, estado FROM clasificaciones_pregunta WHERE id_clasificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClasificacionPregunta clasificacion = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) {
                System.err.println("ClasificacionPreguntaDAO: No se pudo conectar a la BD para obtener clasificación por ID.");
                return null;
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                clasificacion = new ClasificacionPregunta();
                clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
                clasificacion.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                clasificacion.setDescripcion(rs.getString("descripcion"));
                clasificacion.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.err.println("ClasificacionPreguntaDAO: Error SQL al obtener clasificación de pregunta por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return clasificacion;
    }

    public ClasificacionPregunta obtenerClasificacionPorId(Integer id) {
        if (id == null) return null;
        return obtenerClasificacionPorId(id.intValue());
    }

    public List<ClasificacionPregunta> obtenerTodasLasClasificaciones() {
        List<ClasificacionPregunta> clasificaciones = new ArrayList<>();
        String sql = "SELECT id_clasificacion, nombre_clasificacion, descripcion, estado FROM clasificaciones_pregunta ORDER BY nombre_clasificacion ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return clasificaciones;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ClasificacionPregunta clasificacion = new ClasificacionPregunta();
                clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
                clasificacion.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                clasificacion.setDescripcion(rs.getString("descripcion"));
                clasificacion.setEstado(rs.getString("estado"));
                clasificaciones.add(clasificacion);
            }
        } catch (SQLException e) {
            System.err.println("ClasificacionPreguntaDAO: Error SQL al obtener todas las clasificaciones de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return clasificaciones;
    }

    public boolean crearClasificacionPregunta(ClasificacionPregunta clasificacion) {
        String sql = "INSERT INTO clasificaciones_pregunta (nombre_clasificacion, descripcion, estado) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
            ps.setString(1, clasificacion.getNombreClasificacion());
            ps.setString(2, clasificacion.getDescripcion());
            ps.setString(3, clasificacion.getEstado());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    clasificacion.setIdClasificacion(rs.getInt(1));
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("ClasificacionPreguntaDAO: Error SQL al crear clasificación de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean actualizarClasificacionPregunta(ClasificacionPregunta clasificacion) {
        String sql = "UPDATE clasificaciones_pregunta SET nombre_clasificacion = ?, descripcion = ?, estado = ? WHERE id_clasificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, clasificacion.getNombreClasificacion());
            ps.setString(2, clasificacion.getDescripcion());
            ps.setString(3, clasificacion.getEstado());
            ps.setInt(4, clasificacion.getIdClasificacion());
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClasificacionPreguntaDAO: Error SQL al actualizar clasificación de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean eliminarClasificacionPregunta(int idClasificacion) {
        String sql = "DELETE FROM clasificaciones_pregunta WHERE id_clasificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idClasificacion);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClasificacionPreguntaDAO: Error SQL al eliminar clasificación de pregunta: " + e.getMessage());
            e.printStackTrace();

        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}