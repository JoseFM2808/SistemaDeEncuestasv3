package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta; // Importar EncuestaDetallePregunta
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        String sql = "INSERT INTO respuestas_usuario (id_usuario, id_encuesta, id_encuesta_detalle_pregunta, valor_respuesta, fecha_hora_respuesta) VALUES (?, ?, ?, ?, ?)";

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            
            con.setAutoCommit(false); 

            ps = con.prepareStatement(sql);

            for (RespuestaUsuario respuesta : respuestas) {
                ps.setInt(1, respuesta.getIdUsuario());
                ps.setInt(2, respuesta.getIdEncuesta()); 
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
        String sql = "SELECT ru.id_respuesta, ru.id_usuario, ru.id_encuesta, ru.id_encuesta_detalle_pregunta, " +
                     "ru.valor_respuesta, ru.fecha_hora_respuesta " +
                     "FROM respuestas_usuario ru " +
                     "WHERE ru.id_encuesta = ?";
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
        String sql = "SELECT COUNT(*) FROM respuestas_usuario WHERE id_usuario = ? AND id_encuesta = ?";
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
        String sql = "DELETE FROM respuestas_usuario WHERE id_encuesta = ?";
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
}