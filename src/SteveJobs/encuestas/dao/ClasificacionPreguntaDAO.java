/*
 * Responsable: Giancarlo Janampa Frisancho (Implementación completada por Asistente de AED)
 * Relación con otras partes del código:
 * - Dependencia clave para ServicioEncuestas y ServicioPreguntas al asociar preguntas
 * a clasificaciones existentes o al obtener detalles completos de preguntas.
 * Funcionalidad:
 * - Objeto de Acceso a Datos (DAO) para la entidad ClasificacionPregunta.
 * - Gestiona las operaciones de persistencia (CRUD) para las clasificaciones de preguntas.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (No implementa algoritmos de ordenamiento o estructuras complejas).
 */
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // No se usa directamente en los métodos implementados, pero es común en DAOs
import java.util.List; // No se usa directamente en los métodos implementados, pero es común en DAOs

public class ClasificacionPreguntaDAO {

    /**
     * Obtiene una ClasificacionPregunta por su nombre.
     *
     * @param nombre El nombre de la clasificación a buscar (ej. "Tecnología").
     * @return Un objeto ClasificacionPregunta si se encuentra, null en caso contrario.
     */
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

    /**
     * Obtiene una ClasificacionPregunta por su ID.
     *
     * @param id El ID de la clasificación a buscar.
     * @return Un objeto ClasificacionPregunta si se encuentra, null en caso contrario.
     */
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

    /**
     * Sobrecarga de obtenerClasificacionPorId para aceptar un Integer.
     *
     * @param id El ID de la clasificación a buscar (puede ser null).
     * @return Un objeto ClasificacionPregunta si se encuentra y el ID no es null, null en caso contrario.
     */
    public ClasificacionPregunta obtenerClasificacionPorId(Integer id) {
        if (id == null) return null;
        return obtenerClasificacionPorId(id.intValue());
    }
}
