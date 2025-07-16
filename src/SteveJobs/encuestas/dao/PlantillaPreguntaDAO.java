/*
 * Responsable: Pablo Alegre (Futura responsabilidad, si se integra módulo de Plantillas)
 * Relación con otras partes del código:
 * - N/A (Actualmente un placeholder, no tiene relaciones activas).
 * Funcionalidad:
 * - Objeto de Acceso a Datos (DAO) para la entidad PlantillaPregunta.
 * - Su propósito es gestionar las operaciones de persistencia (CRUD) relacionadas
 * con plantillas de preguntas predefinidas si esta funcionalidad es implementada.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Implementación actual es un placeholder).
 */

package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.PlantillaPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlantillaPreguntaDAO {

    /**
     * Crea una nueva plantilla de pregunta en la base de datos.
     * @param plantilla La plantilla a crear.
     * @return El ID generado de la plantilla, o -1 si falla.
     */
    public int crearPlantilla(PlantillaPregunta plantilla) {
        String sql = "INSERT INTO plantillas_pregunta (nombre_plantilla, descripcion, estado) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        int idGenerado = -1;

        try {
            con = ConexionDB.conectar();
            if (con == null) return -1;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, plantilla.getNombre());
            ps.setString(2, plantilla.getDescripcion());
            ps.setString(3, plantilla.getEstado());

            if (ps.executeUpdate() > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al crear plantilla: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(generatedKeys, ps, con);
        }
        return idGenerado;
    }

    /**
     * Obtiene una plantilla de pregunta por su ID.
     * @param idPlantilla El ID de la plantilla a buscar.
     * @return El objeto PlantillaPregunta, o null si no se encuentra.
     */
    public PlantillaPregunta obtenerPlantillaPorId(int idPlantilla) {
        String sql = "SELECT id_plantilla, nombre_plantilla, descripcion, estado FROM plantillas_pregunta WHERE id_plantilla = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PlantillaPregunta plantilla = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPlantilla);
            rs = ps.executeQuery();

            if (rs.next()) {
                plantilla = new PlantillaPregunta();
                plantilla.setIdPlantilla(rs.getInt("id_plantilla"));
                plantilla.setNombre(rs.getString("nombre_plantilla"));
                plantilla.setDescripcion(rs.getString("descripcion"));
                plantilla.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener plantilla por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return plantilla;
    }

    /**
     * Obtiene todas las plantillas de pregunta.
     * @return Una lista de objetos PlantillaPregunta.
     */
    public List<PlantillaPregunta> obtenerTodasLasPlantillas() {
        List<PlantillaPregunta> plantillas = new ArrayList<>();
        String sql = "SELECT id_plantilla, nombre_plantilla, descripcion, estado FROM plantillas_pregunta ORDER BY nombre_plantilla ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return plantillas;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PlantillaPregunta plantilla = new PlantillaPregunta();
                plantilla.setIdPlantilla(rs.getInt("id_plantilla"));
                plantilla.setNombre(rs.getString("nombre_plantilla"));
                plantilla.setDescripcion(rs.getString("descripcion"));
                plantilla.setEstado(rs.getString("estado"));
                plantillas.add(plantilla);
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener todas las plantillas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return plantillas;
    }

    /**
     * Actualiza una plantilla de pregunta existente.
     * @param plantilla La plantilla con la información actualizada.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarPlantilla(PlantillaPregunta plantilla) {
        String sql = "UPDATE plantillas_pregunta SET nombre_plantilla = ?, descripcion = ?, estado = ? WHERE id_plantilla = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, plantilla.getNombre());
            ps.setString(2, plantilla.getDescripcion());
            ps.setString(3, plantilla.getEstado());
            ps.setInt(4, plantilla.getIdPlantilla());
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DAO Error al actualizar plantilla: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    /**
     * Elimina una plantilla de pregunta por su ID.
     * @param idPlantilla El ID de la plantilla a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPlantilla(int idPlantilla) {
        String sql = "DELETE FROM plantillas_pregunta WHERE id_plantilla = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPlantilla);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DAO Error al eliminar plantilla: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}