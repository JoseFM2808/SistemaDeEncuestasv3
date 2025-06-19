/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Objeto de Acceso a Datos (DAO) para la entidad TipoPregunta.
 * Se encarga de las operaciones de persistencia (CRUD) relacionadas con los
 * tipos de pregunta soportados por el sistema.
 * Actualmente, las operaciones de consulta están marcadas como NO IMPLEMENTADAS.
 * Funciones esperadas: crear, obtener por nombre/ID, actualizar y eliminar tipos de pregunta.
 * Es crucial para REQMS-017.
 */
// Contenido de SteveJobs.encuestas.dao.TipoPreguntaDAO
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // No se usa aún, pero útil para futuros métodos de listado
import java.util.List;

public class TipoPreguntaDAO {

    // Método para obtener un TipoPregunta por su nombre
    public TipoPregunta obtenerTipoPreguntaPorNombre(String nombre) {
        String sql = "SELECT id_tipo_pregunta, nombre_tipo FROM TiposPregunta WHERE nombre_tipo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoPregunta tipo = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, nombre);
                rs = ps.executeQuery();

                if (rs.next()) {
                    tipo = new TipoPregunta();
                    tipo.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                    tipo.setNombreTipo(rs.getString("nombre_tipo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener tipo de pregunta por nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return tipo;
    }

    // Método para obtener un TipoPregunta por su ID
    public TipoPregunta obtenerTipoPreguntaPorId(int id) {
        String sql = "SELECT id_tipo_pregunta, nombre_tipo FROM TiposPregunta WHERE id_tipo_pregunta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoPregunta tipo = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();

                if (rs.next()) {
                    tipo = new TipoPregunta();
                    tipo.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                    tipo.setNombreTipo(rs.getString("nombre_tipo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener tipo de pregunta por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return tipo;
    }

    // Sobrecarga para manejar Integer, útil en algunos modelos
    public TipoPregunta obtenerTipoPreguntaPorId(Integer id) {
        if (id == null) {
            return null;
        }
        return obtenerTipoPreguntaPorId(id.intValue());
    }
    
    // Método para listar todos los tipos de pregunta (útil para UIs de selección)
    public List<TipoPregunta> obtenerTodosLosTiposPregunta() {
        List<TipoPregunta> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo_pregunta, nombre_tipo FROM TiposPregunta ORDER BY nombre_tipo ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    TipoPregunta tipo = new TipoPregunta();
                    tipo.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                    tipo.setNombreTipo(rs.getString("nombre_tipo"));
                    tipos.add(tipo);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener todos los tipos de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return tipos;
    }
}