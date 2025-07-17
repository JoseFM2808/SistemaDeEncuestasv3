package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta; // Importar EncuestaDetallePregunta
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Importar Timestamp
import java.util.List;
import java.util.ArrayList;

public class RespuestaUsuarioDAO {

    private EncuestaDetallePreguntaDAO encuestaDetallePreguntaDAO; // Nueva instancia para obtener detalles

    public RespuestaUsuarioDAO() {
        this.encuestaDetallePreguntaDAO = new EncuestaDetallePreguntaDAO(); // Inicializar
    }

    public boolean guardarListaRespuestas(List<RespuestaUsuario> respuestas) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = true;

        // La tabla 'respuestas' ya tiene 'id_encuesta' y 'id_encuesta_detalle_pregunta'.
        // Asegúrate de que el modelo RespuestaUsuario tenga el idEncuesta.
        String sql = "INSERT INTO respuestas (id_usuario, id_encuesta, id_encuesta_detalle_pregunta, valor_respuesta, fecha_hora_respuesta) VALUES (?, ?, ?, ?, ?)";

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            
            con.setAutoCommit(false); 

            ps = con.prepareStatement(sql);

            for (RespuestaUsuario respuesta : respuestas) {
                ps.setInt(1, respuesta.getIdUsuario());
                ps.setInt(2, respuesta.getIdEncuesta()); // Asume que idEncuesta ya está en el modelo RespuestaUsuario
                ps.setInt(3, respuesta.getIdEncuestaDetallePregunta());
                ps.setString(4, respuesta.getValorRespuesta());
                ps.setTimestamp(5, respuesta.getFechaHoraRespuesta());
                ps.addBatch();
            }

            int[] resultados = ps.executeBatch();
            
            for (int res : resultados) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    exito = false;
                    break;
                }
            }

            if (exito) {
                con.commit();
            } else {
                con.rollback();
            }

        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al guardar lista de respuestas: " + e.getMessage());
            e.printStackTrace();
            exito = false;
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("RespuestaUsuarioDAO: Error al hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("RespuestaUsuarioDAO: Error al restaurar auto-commit: " + ex.getMessage());
            }
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public List<RespuestaUsuario> obtenerRespuestasPorEncuesta(int idEncuesta) {
        List<RespuestaUsuario> respuestas = new ArrayList<>();
        // CORREGIDO: Se hace un JOIN con `encuesta_preguntas` para obtener `id_encuesta`
        String sql = "SELECT ru.id_respuesta, ru.id_usuario, edp.id_encuesta, ru.id_encuesta_detalle_pregunta, " +
                     "ru.valor_respuesta, ru.fecha_hora_respuesta " +
                     "FROM respuestas ru " +
                     "JOIN encuesta_preguntas edp ON ru.id_encuesta_detalle_pregunta = edp.id_encuesta_detalle " +
                     "WHERE edp.id_encuesta = ?"; // Filtramos por el id_encuesta de la tabla unida
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return respuestas;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuesta);
            rs = ps.executeQuery();

            while (rs.next()) {
                RespuestaUsuario respuesta = new RespuestaUsuario();
                respuesta.setIdRespuesta(rs.getInt("id_respuesta"));
                respuesta.setIdUsuario(rs.getInt("id_usuario"));
                // CAMBIADO: Obtiene id_encuesta de la tabla unida (edp)
                respuesta.setIdEncuesta(rs.getInt("id_encuesta")); 
                respuesta.setIdEncuestaDetallePregunta(rs.getInt("id_encuesta_detalle_pregunta"));
                respuesta.setValorRespuesta(rs.getString("valor_respuesta"));
                respuesta.setFechaHoraRespuesta(rs.getTimestamp("fecha_hora_respuesta"));
                
                // Cargar el objeto EncuestaDetallePregunta completo
                EncuestaDetallePregunta detallePregunta = encuestaDetallePreguntaDAO.obtenerPreguntaDetallePorId(respuesta.getIdEncuestaDetallePregunta());
                respuesta.setEncuestaDetallePregunta(detallePregunta); // Setear el objeto completo

                respuestas.add(respuesta);
            }
        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al obtener respuestas por encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return respuestas;
    }
    
    public boolean haUsuarioRespondidoEncuesta(int idUsuario, int idEncuesta) {
        // CORREGIDO: Se hace JOIN para verificar si el usuario ha respondido la encuesta
        // a través de sus preguntas.
        String sql = "SELECT COUNT(ru.id_respuesta) FROM respuestas ru " +
                     "JOIN encuesta_preguntas edp ON ru.id_encuesta_detalle_pregunta = edp.id_encuesta_detalle " +
                     "WHERE ru.id_usuario = ? AND edp.id_encuesta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean haRespondido = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEncuesta);
            rs = ps.executeQuery();

            if (rs.next()) {
                haRespondido = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al verificar si usuario respondió encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return haRespondido;
    }

    public boolean eliminarRespuestasDeEncuesta(int idEncuesta) {
        // CORREGIDO: Aunque la tabla `respuestas` tiene id_encuesta, para ser estrictos
        // con la estructura actual (donde `id_encuesta` en `respuestas` viene del modelo)
        // sería más robusto eliminar basado en un JOIN. Sin embargo, para no re-estructurar,
        // confiaremos en que el modelo de `RespuestaUsuario` se llena correctamente
        // y la FK en la BD maneja la integridad.
        // La tabla `respuestas` sí tiene la columna `id_encuesta` directamente.
        String sql = "DELETE FROM respuestas WHERE id_encuesta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuesta);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al eliminar respuestas de encuesta: " + e.getMessage());
            e.printStackTrace();

        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    /**
     * Guarda una respuesta a una pregunta de registro dinámica.
     * Esta implementación ahora inserta en la nueva tabla `respuestas_registro`.
     * @param idUsuario El ID del usuario que responde.
     * @param idPreguntaRegistro El ID de la pregunta de registro.
     * @param valorRespuesta El valor de la respuesta.
     * @return true si la respuesta fue guardada exitosamente, false en caso contrario.
     */
    public boolean guardarRespuestaRegistro(int idUsuario, int idPreguntaRegistro, String valorRespuesta) {
        // CAMBIADO: Se inserta en la nueva tabla `respuestas_registro`
        String sql = "INSERT INTO respuestas_registro (id_usuario, id_pregunta_registro, valor_respuesta, fecha_hora_respuesta) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) {
                System.err.println("RespuestaUsuarioDAO: No se pudo conectar a la BD para guardar respuesta de registro.");
                return false;
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idPreguntaRegistro);
            ps.setString(3, valorRespuesta);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // Usar la fecha y hora actual

            exito = ps.executeUpdate() > 0;

            if (!exito) {
                System.err.println("RespuestaUsuarioDAO: Falló la inserción de la respuesta de registro para usuario " + idUsuario + ", pregunta " + idPreguntaRegistro);
            }

        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al guardar respuesta de registro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}