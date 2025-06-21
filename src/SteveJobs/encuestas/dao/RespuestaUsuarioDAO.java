/*
 * Autores del Módulo:
 * - José Flores
 *
 * Responsabilidad Principal:
 * - Acceso a datos de respuestas de usuarios
 */
package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.sql.ResultSet;

public class RespuestaUsuarioDAO {

    public boolean guardarListaRespuestas(List<RespuestaUsuario> listaRespuestas) {
        if (listaRespuestas == null || listaRespuestas.isEmpty()) {
            return true;
        }
        String sql = "INSERT INTO respuestas_usuarios (id_encuesta_detalle_pregunta, id_usuario, valor_respuesta, fecha_hora_respuesta, ts_inicio_participacion, ts_fin_participacion, retroalimentacion_usr) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exitoTotal = true;

        try {
            con = ConexionDB.conectar();
            if (con != null) {
                con.setAutoCommit(false);
                ps = con.prepareStatement(sql);

                for (RespuestaUsuario respuesta : listaRespuestas) {
                    ps.setInt(1, respuesta.getIdEncuestaDetallePregunta());
                    ps.setInt(2, respuesta.getIdUsuario());
                    ps.setString(3, respuesta.getValorRespuesta());
                    ps.setTimestamp(4, respuesta.getFechaHoraRespuesta() != null ? respuesta.getFechaHoraRespuesta() : new Timestamp(System.currentTimeMillis()));
                    ps.setTimestamp(5, respuesta.getTsInicioParticipacion());
                    ps.setTimestamp(6, respuesta.getTsFinParticipacion());
                    ps.setString(7, respuesta.getRetroalimentacionUsuario());
                    ps.addBatch();
                }
                int[] resultados = ps.executeBatch();
                con.commit();

                for (int resultado : resultados) {
                    if (resultado == PreparedStatement.EXECUTE_FAILED) {
                        exitoTotal = false;
                        System.err.println("DAO: Falló una inserción en el lote de respuestas.");
                        break;
                    }
                }
                if(exitoTotal) System.out.println("DAO: Lote de respuestas guardado exitosamente.");

            } else {
                exitoTotal = false;
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al guardar lista de respuestas: " + e.getMessage());
            e.printStackTrace();
            exitoTotal = false;
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("DAO Error al hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                }
            }
            ConexionDB.cerrar(null, ps, con);
        }
        return exitoTotal;
    }
    
    public boolean haRespondidoEncuesta(int idUsuario, int idEncuesta) {
        // Esta consulta cuenta las respuestas de un usuario para cualquier pregunta
        // de una encuesta específica. Si el conteo es mayor a 0, significa que ya participó.
        String sql = "SELECT COUNT(*) FROM respuestas r " +
                     "JOIN encuesta_preguntas edp ON r.id_encuesta_detalle_pregunta = edp.id_encuesta_detalle " +
                     "WHERE r.id_usuario = ? AND edp.id_encuesta = ?";
        
        // Usamos try-with-resources para asegurar que la conexión y el statement se cierren solos.
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEncuesta);

            // El ResultSet también se cierra solo gracias al try-with-resources
            try (ResultSet rs = ps.executeQuery()) {
                // Una consulta COUNT(*) siempre devuelve una fila, por lo que rs.next() será true.
                if (rs.next()) {
                    // Obtenemos el valor de la primera columna (el resultado de COUNT(*))
                    // y devolvemos true si es mayor a 0, o false si es 0.
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Error al verificar si el usuario ha respondido: " + e.getMessage());
            e.printStackTrace();
        }
        // Si ocurre cualquier error o si por alguna razón no se puede leer el resultado,
        // devolvemos 'false' por seguridad.
        return false;
    }

}
