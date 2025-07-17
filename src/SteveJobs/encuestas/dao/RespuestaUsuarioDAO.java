package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta; 
import SteveJobs.encuestas.modelo.PreguntaBanco; 
import SteveJobs.encuestas.modelo.TipoPregunta; 
import SteveJobs.encuestas.modelo.ClasificacionPregunta; 
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RespuestaUsuarioDAO {

    public boolean guardarListaRespuestas(List<RespuestaUsuario> listaRespuestas) {
        String sql = "INSERT INTO respuestas_usuario (id_encuesta_detalle_pregunta, id_usuario, valor_respuesta, fecha_hora_respuesta, ts_inicio_participacion, ts_fin_participacion, retroalimentacion_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = true;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
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
            try {
                if (con != null) con.rollback();
            } catch (SQLException rb) {
                System.err.println("RespuestaUsuarioDAO: Error al hacer rollback: " + rb.getMessage());
            }
        } finally {
            try {
                if (con != null) con.setAutoCommit(true); 
            } catch (SQLException sac) {
                System.err.println("RespuestaUsuarioDAO: Error al restaurar auto-commit: " + sac.getMessage());
            }
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean haRespondidoEncuesta(int idUsuario, int idEncuesta) {
        String sql = "SELECT COUNT(DISTINCT ru.id_usuario) FROM respuestas_usuario ru " +
                     "JOIN encuesta_detalle_preguntas edp ON ru.id_encuesta_detalle_pregunta = edp.id_encuesta_detalle_pregunta " +
                     "WHERE ru.id_usuario = ? AND edp.id_encuesta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = ConexionDB.conectar();
            if (con == null) return true; 
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEncuesta);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al verificar si usuario ha respondido encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return count > 0;
    }

    public List<RespuestaUsuario> obtenerRespuestasPorDetallePregunta(int idEncuestaDetallePregunta) {
        List<RespuestaUsuario> respuestas = new ArrayList<>();
        String sql = "SELECT id_respuesta_usuario, id_encuesta_detalle_pregunta, id_usuario, valor_respuesta, fecha_hora_respuesta, ts_inicio_participacion, ts_fin_participacion, retroalimentacion_usuario FROM respuestas_usuario WHERE id_encuesta_detalle_pregunta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return respuestas;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuestaDetallePregunta);
            rs = ps.executeQuery();

            while (rs.next()) {
                respuestas.add(mapearResultSetARespuesta(rs));
            }
        } catch (SQLException e) {
            System.err.println("RespuestaUsuarioDAO: Error SQL al obtener respuestas por detalle de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return respuestas;
    }

    public List<RespuestaUsuario> obtenerRespuestasPorEncuesta(int idEncuesta) {
        List<RespuestaUsuario> respuestas = new ArrayList<>();
        String sql = "SELECT ru.id_respuesta_usuario, ru.id_encuesta_detalle_pregunta, ru.id_usuario, ru.valor_respuesta, " +
                     "ru.fecha_hora_respuesta, ru.ts_inicio_participacion, ru.ts_fin_participacion, ru.retroalimentacion_usuario, " +
                     "edp.id_encuesta, edp.id_pregunta_banco, edp.texto_pregunta_unica, edp.id_tipo_pregunta_unica, edp.id_clasificacion_unica, " +
                     "edp.orden_en_encuesta, edp.es_pregunta_descarte, edp.criterio_descarte_valor, " +
                     "pb.texto_pregunta AS pb_texto_pregunta, pb.id_tipo_pregunta AS pb_id_tipo_pregunta, pb.id_clasificacion AS pb_id_clasificacion, " +
                     "tp.nombre_tipo AS tp_nombre_tipo, cp.nombre_clasificacion AS cp_nombre_clasificacion " +
                     "FROM respuestas_usuario ru " +
                     "JOIN encuesta_detalle_preguntas edp ON ru.id_encuesta_detalle_pregunta = edp.id_encuesta_detalle_pregunta " +
                     "LEFT JOIN preguntas_banco pb ON edp.id_pregunta_banco = pb.id_pregunta_banco " +
                     "LEFT JOIN tipos_pregunta tp ON COALESCE(pb.id_tipo_pregunta, edp.id_tipo_pregunta_unica) = tp.id_tipo_pregunta " +
                     "LEFT JOIN clasificaciones_pregunta cp ON COALESCE(pb.id_clasificacion, edp.id_clasificacion_unica) = cp.id_clasificacion " +
                     "WHERE edp.id_encuesta = ? " +
                     "ORDER BY edp.orden_en_encuesta, ru.fecha_hora_respuesta ASC";
        
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
                RespuestaUsuario respuesta = mapearResultSetARespuesta(rs);
                
                EncuestaDetallePregunta edp = new EncuestaDetallePregunta();
                edp.setIdEncuestaDetalle(rs.getInt("id_encuesta_detalle_pregunta"));
                edp.setIdEncuesta(rs.getInt("id_encuesta"));
                edp.setIdPreguntaBanco(rs.getObject("id_pregunta_banco", Integer.class));
                edp.setTextoPreguntaUnica(rs.getString("texto_pregunta_unica"));
                edp.setIdTipoPreguntaUnica(rs.getObject("id_tipo_pregunta_unica", Integer.class));
                edp.setIdClasificacionUnica(rs.getObject("id_clasificacion_unica", Integer.class));
                edp.setOrdenEnEncuesta(rs.getInt("orden_en_encuesta"));
                edp.setEsPreguntaDescarte(rs.getBoolean("es_pregunta_descarte"));
                edp.setCriterioDescarteValor(rs.getString("criterio_descarte_valor"));

                if (edp.getIdPreguntaBanco() != null) {
                    PreguntaBanco pb = new PreguntaBanco();
                    pb.setIdPreguntaBanco(edp.getIdPreguntaBanco());
                    pb.setTextoPregunta(rs.getString("pb_texto_pregunta"));
                    pb.setIdTipoPregunta(rs.getObject("pb_id_tipo_pregunta", Integer.class));
                    pb.setIdClasificacion(rs.getObject("pb_id_clasificacion", Integer.class));
                    pb.setNombreTipoPregunta(rs.getString("tp_nombre_tipo")); 
                    pb.setNombreClasificacion(rs.getString("cp_nombre_clasificacion"));
                    edp.setPreguntaDelBanco(pb);
                } else {

                     TipoPregunta tpUnica = new TipoPregunta();
                     tpUnica.setIdTipoPregunta(edp.getIdTipoPreguntaUnica() != null ? edp.getIdTipoPreguntaUnica() : 0);
                     tpUnica.setNombreTipo(rs.getString("tp_nombre_tipo"));
                     edp.setTipoPreguntaObj(tpUnica);

                     ClasificacionPregunta cpUnica = new ClasificacionPregunta();
                     cpUnica.setIdClasificacion(edp.getIdClasificacionUnica() != null ? edp.getIdClasificacionUnica() : 0);
                     cpUnica.setNombreClasificacion(rs.getString("cp_nombre_clasificacion"));
                     edp.setClasificacionPreguntaObj(cpUnica);
                }
                
                respuesta.setEncuestaDetallePregunta(edp); 
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

    private RespuestaUsuario mapearResultSetARespuesta(ResultSet rs) throws SQLException {
        RespuestaUsuario respuesta = new RespuestaUsuario();
        respuesta.setIdRespuestaUsuario(rs.getInt("id_respuesta_usuario"));
        respuesta.setIdEncuestaDetallePregunta(rs.getInt("id_encuesta_detalle_pregunta"));
        respuesta.setIdUsuario(rs.getInt("id_usuario"));
        respuesta.setValorRespuesta(rs.getString("valor_respuesta"));
        respuesta.setFechaHoraRespuesta(rs.getTimestamp("fecha_hora_respuesta"));
        respuesta.setTsInicioParticipacion(rs.getTimestamp("ts_inicio_participacion"));
        respuesta.setTsFinParticipacion(rs.getTimestamp("ts_fin_participacion"));
        respuesta.setRetroalimentacionUsuario(rs.getString("retroalimentacion_usuario"));
        return respuesta;
    }
}