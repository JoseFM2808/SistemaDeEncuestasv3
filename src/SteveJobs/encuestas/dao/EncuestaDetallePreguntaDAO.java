package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EncuestaDetallePreguntaDAO {

    private PreguntaBancoDAO preguntaBancoDAO;
    private TipoPreguntaDAO tipoPreguntaDAO;
    private ClasificacionPreguntaDAO clasificacionPreguntaDAO;

    public EncuestaDetallePreguntaDAO() {
        this.preguntaBancoDAO = new PreguntaBancoDAO();
        this.tipoPreguntaDAO = new TipoPreguntaDAO();
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO();
    }

    public boolean agregarPreguntaAEncuesta(EncuestaDetallePregunta detalle) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "INSERT INTO encuesta_preguntas " +
                     "(id_encuesta, id_pregunta_banco, texto_pregunta_unica, id_tipo_pregunta_unica, id_clasificacion_unica, " +
                     "orden_en_encuesta, es_pregunta_descarte, criterio_descarte_valor) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, detalle.getIdEncuesta());

            if (detalle.getIdPreguntaBanco() != null) {
                ps.setInt(2, detalle.getIdPreguntaBanco());
                ps.setNull(3, java.sql.Types.VARCHAR);
                ps.setNull(4, java.sql.Types.INTEGER);
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
                ps.setString(3, detalle.getTextoPreguntaUnica());
                if (detalle.getIdTipoPreguntaUnica() != null) {
                    ps.setInt(4, detalle.getIdTipoPreguntaUnica());
                } else {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
                if (detalle.getIdClasificacionUnica() != null) {
                    ps.setInt(5, detalle.getIdClasificacionUnica());
                } else {
                    ps.setNull(5, java.sql.Types.INTEGER);
                }
            }

            ps.setInt(6, detalle.getOrdenEnEncuesta());
            ps.setBoolean(7, detalle.isEsPreguntaDescarte());
            ps.setString(8, detalle.getCriterioDescarteValor());

            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    detalle.setIdEncuestaDetalle(rs.getInt(1));
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al agregar pregunta a encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean actualizarDetallePregunta(EncuestaDetallePregunta detalle) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "UPDATE encuesta_preguntas SET " +
                     "id_pregunta_banco = ?, texto_pregunta_unica = ?, id_tipo_pregunta_unica = ?, id_clasificacion_unica = ?, " +
                     "orden_en_encuesta = ?, es_pregunta_descarte = ?, criterio_descarte_valor = ? " +
                     "WHERE id_encuesta_detalle = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);

            if (detalle.getIdPreguntaBanco() != null) {
                ps.setInt(1, detalle.getIdPreguntaBanco());
                ps.setNull(2, java.sql.Types.VARCHAR);
                ps.setNull(3, java.sql.Types.INTEGER);
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
                ps.setString(2, detalle.getTextoPreguntaUnica());
                if (detalle.getIdTipoPreguntaUnica() != null) {
                    ps.setInt(3, detalle.getIdTipoPreguntaUnica());
                } else {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }
                if (detalle.getIdClasificacionUnica() != null) {
                    ps.setInt(4, detalle.getIdClasificacionUnica());
                } else {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
            }

            ps.setInt(5, detalle.getOrdenEnEncuesta());
            ps.setBoolean(6, detalle.isEsPreguntaDescarte());
            ps.setString(7, detalle.getCriterioDescarteValor());
            ps.setInt(8, detalle.getIdEncuestaDetalle());

            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al actualizar detalle de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public EncuestaDetallePregunta obtenerPreguntaDetallePorId(int idDetalle) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "SELECT * FROM encuesta_preguntas WHERE id_encuesta_detalle = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EncuestaDetallePregunta detalle = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDetalle);
            rs = ps.executeQuery();

            if (rs.next()) {
                detalle = new EncuestaDetallePregunta();
                detalle.setIdEncuestaDetalle(rs.getInt("id_encuesta_detalle"));
                detalle.setIdEncuesta(rs.getInt("id_encuesta"));
                detalle.setIdPreguntaBanco(rs.getObject("id_pregunta_banco", Integer.class));
                detalle.setTextoPreguntaUnica(rs.getString("texto_pregunta_unica"));
                detalle.setIdTipoPreguntaUnica(rs.getObject("id_tipo_pregunta_unica", Integer.class));
                detalle.setIdClasificacionUnica(rs.getObject("id_clasificacion_unica", Integer.class));
                detalle.setOrdenEnEncuesta(rs.getInt("orden_en_encuesta"));
                detalle.setEsPreguntaDescarte(rs.getBoolean("es_pregunta_descarte"));
                detalle.setCriterioDescarteValor(rs.getString("criterio_descarte_valor"));

                // --- Cargar objetos asociados (para pregunta de banco o única) ---
                if (detalle.getIdPreguntaBanco() != null) { // Es una pregunta del banco
                    PreguntaBanco pb = preguntaBancoDAO.obtenerPreguntaPorId(detalle.getIdPreguntaBanco());
                    if (pb != null) {
                        detalle.setPreguntaDelBanco(pb);
                        // Cargar y setear TipoPregunta y ClasificacionPregunta del banco
                        TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(pb.getIdTipoPregunta());
                        if(tp != null) detalle.setTipoPreguntaObj(tp);
                        if(pb.getIdClasificacion() != null) {
                            ClasificacionPregunta cp = clasificacionPreguntaDAO.obtenerClasificacionPorId(pb.getIdClasificacion());
                            if(cp != null) detalle.setClasificacionPreguntaObj(cp);
                        }
                    }
                } else { // Es una pregunta única
                    if (detalle.getIdTipoPreguntaUnica() != null) {
                        TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(detalle.getIdTipoPreguntaUnica());
                        if(tp != null) detalle.setTipoPreguntaObj(tp);
                    }
                    if (detalle.getIdClasificacionUnica() != null) {
                        ClasificacionPregunta cp = clasificacionPreguntaDAO.obtenerClasificacionPorId(detalle.getIdClasificacionUnica());
                        if(cp != null) detalle.setClasificacionPreguntaObj(cp);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al obtener detalle de pregunta por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return detalle;
    }

    public List<EncuestaDetallePregunta> obtenerPreguntasPorEncuesta(int idEncuesta) {
        List<EncuestaDetallePregunta> preguntas = new ArrayList<>();
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "SELECT * FROM encuesta_preguntas WHERE id_encuesta = ? ORDER BY orden_en_encuesta ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return preguntas;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuesta);
            rs = ps.executeQuery();

            while (rs.next()) {
                EncuestaDetallePregunta detalle = new EncuestaDetallePregunta();
                detalle.setIdEncuestaDetalle(rs.getInt("id_encuesta_detalle"));
                detalle.setIdEncuesta(rs.getInt("id_encuesta"));
                detalle.setIdPreguntaBanco(rs.getObject("id_pregunta_banco", Integer.class));
                detalle.setTextoPreguntaUnica(rs.getString("texto_pregunta_unica"));
                detalle.setIdTipoPreguntaUnica(rs.getObject("id_tipo_pregunta_unica", Integer.class));
                detalle.setIdClasificacionUnica(rs.getObject("id_clasificacion_unica", Integer.class));
                detalle.setOrdenEnEncuesta(rs.getInt("orden_en_encuesta"));
                detalle.setEsPreguntaDescarte(rs.getBoolean("es_pregunta_descarte"));
                detalle.setCriterioDescarteValor(rs.getString("criterio_descarte_valor"));

                // Cargar objetos asociados (similar a obtenerPreguntaDetallePorId)
                if (detalle.getIdPreguntaBanco() != null) {
                    PreguntaBanco pb = preguntaBancoDAO.obtenerPreguntaPorId(detalle.getIdPreguntaBanco());
                    if (pb != null) {
                        detalle.setPreguntaDelBanco(pb);
                        TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(pb.getIdTipoPregunta());
                        if(tp != null) detalle.setTipoPreguntaObj(tp);
                        if(pb.getIdClasificacion() != null) {
                            ClasificacionPregunta cp = clasificacionPreguntaDAO.obtenerClasificacionPorId(pb.getIdClasificacion());
                            if(cp != null) detalle.setClasificacionPreguntaObj(cp);
                        }
                    }
                } else {
                    if (detalle.getIdTipoPreguntaUnica() != null) {
                        TipoPregunta tp = tipoPreguntaDAO.obtenerTipoPreguntaPorId(detalle.getIdTipoPreguntaUnica());
                        if(tp != null) detalle.setTipoPreguntaObj(tp);
                    }
                    if (detalle.getIdClasificacionUnica() != null) {
                        ClasificacionPregunta cp = clasificacionPreguntaDAO.obtenerClasificacionPorId(detalle.getIdClasificacionUnica());
                        if(cp != null) detalle.setClasificacionPreguntaObj(cp);
                    }
                }
                preguntas.add(detalle);
            }
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al obtener preguntas por encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return preguntas;
    }

    public int contarPreguntasEnEncuesta(int idEncuesta) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "SELECT COUNT(*) FROM encuesta_preguntas WHERE id_encuesta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            con = ConexionDB.conectar();
            if (con == null) return 0;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuesta);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al contar preguntas en encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return count;
    }

    public boolean eliminarPreguntaDeEncuesta(int idEncuestaDetalle) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "DELETE FROM encuesta_preguntas WHERE id_encuesta_detalle = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuestaDetalle);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al eliminar pregunta de encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean eliminarTodasPreguntasDeEncuesta(int idEncuesta) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "DELETE FROM encuesta_preguntas WHERE id_encuesta = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEncuesta);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al eliminar todas las preguntas de una encuesta: " + e.getMessage());
            e.printStackTrace();
            exito = false;
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean isPreguntaBancoUsedInActiveEncuestas(int idPreguntaBanco) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "SELECT COUNT(edp.id_encuesta_detalle) FROM encuesta_preguntas edp " +
                     "JOIN encuestas e ON edp.id_encuesta = e.id_encuesta " +
                     "WHERE edp.id_pregunta_banco = ? AND e.estado = 'Activa'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean inUse = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPreguntaBanco);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                inUse = true;
            }
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al verificar uso de pregunta de banco: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return inUse;
    }

    public boolean actualizarOrdenPregunta(int idEncuestaDetalle, int nuevoOrden) {
        // CAMBIADO: 'encuestas_detalle_pregunta' a 'encuesta_preguntas'
        String sql = "UPDATE encuesta_preguntas SET orden_en_encuesta = ? WHERE id_encuesta_detalle = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoOrden);
            ps.setInt(2, idEncuestaDetalle);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EncuestaDetallePreguntaDAO: Error SQL al actualizar orden de pregunta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}