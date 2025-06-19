/*
 * Autor: Pablo Alegre (Responsable del Módulo de Gestión de Preguntas)
 *
 * Propósito: Objeto de Acceso a Datos (DAO) para la entidad PreguntaBanco.
 * Se encarga de las operaciones de persistencia (CRUD) del banco de preguntas.
 * Actualmente, las operaciones de consulta están marcadas como NO IMPLEMENTADAS.
 * Funciones esperadas: crear, obtener por ID, actualizar, eliminar, y listar con filtros.
 * Es crucial para REQMS-017 y REQMS-018.
 */
// Contenido de SteveJobs.encuestas.dao.PreguntaBancoDAO
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

    // REQMS-017: Crear nueva pregunta en el banco
    public int crearPregunta(PreguntaBanco pregunta) {
        String sql = "INSERT INTO Preguntas_Banco (texto_pregunta, id_tipo_pregunta, id_clasificacion, opciones) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        int idGenerado = -1;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, pregunta.getTextoPregunta());
                ps.setInt(2, pregunta.getIdTipoPregunta());
                if (pregunta.getIdClasificacion() != null && pregunta.getIdClasificacion() > 0) {
                    ps.setInt(3, pregunta.getIdClasificacion());
                } else {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }
                ps.setString(4, pregunta.getOpciones()); // Asume que el modelo tiene getOpciones()

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        idGenerado = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al crear pregunta en el banco: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(generatedKeys, ps, con);
        }
        return idGenerado;
    }

    // REQMS-017: Actualizar pregunta en el banco
    public boolean actualizarPregunta(PreguntaBanco pregunta) {
        String sql = "UPDATE Preguntas_Banco SET texto_pregunta = ?, id_tipo_pregunta = ?, id_clasificacion = ?, opciones = ? WHERE id_pregunta_banco = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, pregunta.getTextoPregunta());
                ps.setInt(2, pregunta.getIdTipoPregunta());
                if (pregunta.getIdClasificacion() != null && pregunta.getIdClasificacion() > 0) {
                    ps.setInt(3, pregunta.getIdClasificacion());
                } else {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }
                ps.setString(4, pregunta.getOpciones());
                ps.setInt(5, pregunta.getIdPreguntaBanco());
                exito = ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al actualizar pregunta del banco: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    // REQMS-017: Eliminar pregunta del banco
    public boolean eliminarPregunta(int idPreguntaBanco) {
        String sql = "DELETE FROM Preguntas_Banco WHERE id_pregunta_banco = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, idPreguntaBanco);
                exito = ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            // NOTA: Si hay FKs, esta eliminación fallará. Se debe manejar a nivel de servicio.
            System.err.println("DAO Error al eliminar pregunta del banco (puede tener FKs): " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    // REQMS-018: Consultar Banco de Preguntas (obtener por ID)
    public PreguntaBanco obtenerPreguntaPorId(int id) {
        String sql = "SELECT id_pregunta_banco, texto_pregunta, id_tipo_pregunta, id_clasificacion, opciones FROM Preguntas_Banco WHERE id_pregunta_banco = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreguntaBanco pregunta = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();

                if (rs.next()) {
                    pregunta = new PreguntaBanco();
                    pregunta.setIdPreguntaBanco(rs.getInt("id_pregunta_banco"));
                    pregunta.setTextoPregunta(rs.getString("texto_pregunta"));
                    pregunta.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                    
                    Integer idClasif = rs.getInt("id_clasificacion");
                    pregunta.setIdClasificacion(rs.wasNull() ? null : idClasif);
                    pregunta.setOpciones(rs.getString("opciones"));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al obtener pregunta del banco por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return pregunta;
    }
    
    public PreguntaBanco obtenerPreguntaPorId(Integer id) {
        if (id == null) return null;
        return obtenerPreguntaPorId(id.intValue());
    }

    // REQMS-018: Consultar Banco de Preguntas (listar con filtro)
    public List<PreguntaBanco> listarPreguntasDelBancoConFiltro(String filtroTexto, String filtroTipo, Integer filtroClasificacionId) {
        List<PreguntaBanco> preguntas = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT pb.id_pregunta_banco, pb.texto_pregunta, pb.id_tipo_pregunta, tp.nombre_tipo AS nombre_tipo_pregunta, pb.id_clasificacion, cp.nombre_clasificacion AS nombre_clasificacion, pb.opciones FROM Preguntas_Banco pb JOIN TiposPregunta tp ON pb.id_tipo_pregunta = tp.id_tipo_pregunta LEFT JOIN ClasificacionesPregunta cp ON pb.id_clasificacion = cp.id_clasificacion WHERE 1=1");

        if (filtroTexto != null && !filtroTexto.trim().isEmpty()) {
            sqlBuilder.append(" AND pb.texto_pregunta LIKE ?");
        }
        if (filtroTipo != null && !filtroTipo.trim().isEmpty()) {
            sqlBuilder.append(" AND tp.nombre_tipo = ?");
        }
        if (filtroClasificacionId != null && filtroClasificacionId > 0) {
            sqlBuilder.append(" AND pb.id_clasificacion = ?");
        }
        sqlBuilder.append(" ORDER BY pb.id_pregunta_banco ASC");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                ps = con.prepareStatement(sqlBuilder.toString());
                int paramIndex = 1;
                if (filtroTexto != null && !filtroTexto.trim().isEmpty()) {
                    ps.setString(paramIndex++, "%" + filtroTexto.trim() + "%");
                }
                if (filtroTipo != null && !filtroTipo.trim().isEmpty()) {
                    ps.setString(paramIndex++, filtroTipo.trim());
                }
                if (filtroClasificacionId != null && filtroClasificacionId > 0) {
                    ps.setInt(paramIndex++, filtroClasificacionId);
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    PreguntaBanco pregunta = new PreguntaBanco();
                    pregunta.setIdPreguntaBanco(rs.getInt("id_pregunta_banco"));
                    pregunta.setTextoPregunta(rs.getString("texto_pregunta"));
                    pregunta.setIdTipoPregunta(rs.getInt("id_tipo_pregunta"));
                    pregunta.setNombreTipoPregunta(rs.getString("nombre_tipo_pregunta"));

                    Integer idClasif = rs.getInt("id_clasificacion");
                    if (!rs.wasNull()) {
                        pregunta.setIdClasificacion(idClasif);
                        pregunta.setNombreClasificacion(rs.getString("nombre_clasificacion"));
                    }
                    pregunta.setOpciones(rs.getString("opciones"));
                    preguntas.add(pregunta);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al listar preguntas del banco con filtro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return preguntas;
    }
}