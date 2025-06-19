/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Objeto de Acceso a Datos (DAO) para la entidad ClasificacionPregunta.
 * Se encarga de las operaciones de persistencia (CRUD) relacionadas con las
 * clasificaciones temáticas de las preguntas.
 * Actualmente, las operaciones de consulta están marcadas como NO IMPLEMENTADAS.
 * Funciones esperadas: crear, obtener por nombre/ID, actualizar y eliminar clasificaciones.
 * Es crucial para REQMS-016.
 */
// Contenido de SteveJobs.encuestas.dao.ClasificacionPreguntaDAO
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClasificacionPreguntaDAO {

    // Método para obtener una ClasificacionPregunta por su nombre
    public ClasificacionPregunta obtenerClasificacionPorNombre(String nombre) {
        String sql = "SELECT id_clasificacion, nombre_clasificacion FROM ClasificacionesPregunta WHERE nombre_clasificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClasificacionPregunta clasificacion = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, nombre);
                rs = ps.executeQuery();

                if (rs.next()) {
                    clasificacion = new ClasificacionPregunta();
                    clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
                    clasificacion.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener clasificación de pregunta por nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return clasificacion;
    }

    // Método para obtener una ClasificacionPregunta por su ID
    public ClasificacionPregunta obtenerClasificacionPorId(int id) {
        String sql = "SELECT id_clasificacion, nombre_clasificacion FROM ClasificacionesPregunta WHERE id_clasificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClasificacionPregunta clasificacion = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();

                if (rs.next()) {
                    clasificacion = new ClasificacionPregunta();
                    clasificacion.setIdClasificacion(rs.getInt("id_clasificacion"));
                    clasificacion.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener clasificación de pregunta por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return clasificacion;
    }

    // Sobrecarga para manejar Integer
    public ClasificacionPregunta obtenerClasificacionPorId(Integer id) {
        if (id == null) {
            return null;
        }
        return obtenerClasificacionPorId(id.intValue());
    }
    
    // Método para listar todas las clasificaciones (útil para UIs de selección)
    public List<ClasificacionPregunta> obtenerTodasLasClasificaciones() {
        List<ClasificacionPregunta> clasificaciones = new ArrayList<>();
        String sql = "SELECT id_clasificacion, nombre_clasificacion FROM ClasificacionesPregunta ORDER BY nombre_clasificacion ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ClasificacionPregunta clasif = new ClasificacionPregunta();
                    clasif.setIdClasificacion(rs.getInt("id_clasificacion"));
                    clasif.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                    clasificaciones.add(clasif);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener todas las clasificaciones de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return clasificaciones;
    }

    // Implementación CRUD adicional (sugerida para REQMS-016)
    public boolean agregarClasificacion(ClasificacionPregunta clasificacion) {
        String sql = "INSERT INTO ClasificacionesPregunta (nombre_clasificacion, descripcion, estado) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, clasificacion.getNombreClasificacion());
                ps.setString(2, clasificacion.getDescripcion()); // Asume que el modelo ClasificacionPregunta tiene getDescription()
                ps.setString(3, clasificacion.getEstado()); // Asume que el modelo ClasificacionPregunta tiene getEstado()
                exito = ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al agregar clasificación de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
    
    // Aquí puedes agregar actualizarClasificacion, eliminarClasificacion, etc.
}
