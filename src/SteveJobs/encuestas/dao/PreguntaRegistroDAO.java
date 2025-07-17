package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.PreguntaRegistro;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PreguntaRegistroDAO {

    public boolean agregarPreguntaRegistro(PreguntaRegistro pregunta) {
        String sql = "INSERT INTO preguntas_registro (texto_pregunta, tipo_entrada, opciones, es_obligatoria, orden_visualizacion, estado) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pregunta.getTextoPregunta());
            ps.setString(2, pregunta.getTipoEntrada()); // CAMBIO: Usar getTipoEntrada()
            ps.setString(3, pregunta.getOpciones());    // CAMBIO: Usar getOpciones()
            ps.setBoolean(4, pregunta.isEsObligatoria());
            ps.setInt(5, pregunta.getOrdenVisualizacion());
            ps.setString(6, pregunta.getEstado());
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    pregunta.setIdPreguntaRegistro(rs.getInt(1));
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("PreguntaRegistroDAO: Error SQL al agregar pregunta de registro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public List<PreguntaRegistro> obtenerTodasLasPreguntasRegistro() {
        List<PreguntaRegistro> preguntas = new ArrayList<>();
        String sql = "SELECT id_pregunta_registro, texto_pregunta, tipo_entrada, opciones, es_obligatoria, orden_visualizacion, estado FROM preguntas_registro ORDER BY orden_visualizacion ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return preguntas;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PreguntaRegistro pregunta = new PreguntaRegistro();
                pregunta.setIdPreguntaRegistro(rs.getInt("id_pregunta_registro"));
                pregunta.setTextoPregunta(rs.getString("texto_pregunta"));
                pregunta.setTipoRespuesta(rs.getString("tipo_entrada")); // Se asume que setTipoRespuesta mapea a tipo_entrada
                pregunta.setOpcionesPosibles(rs.getString("opciones")); // Se asume que setOpcionesPosibles mapea a opciones
                pregunta.setEsObligatoria(rs.getBoolean("es_obligatoria"));
                pregunta.setOrdenVisualizacion(rs.getInt("orden_visualizacion"));
                pregunta.setEstado(rs.getString("estado"));
                preguntas.add(pregunta);
            }
        } catch (SQLException e) {
            System.err.println("PreguntaRegistroDAO: Error SQL al obtener todas las preguntas de registro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return preguntas;
    }

    public boolean actualizarPreguntaRegistro(PreguntaRegistro pregunta) {
        String sql = "UPDATE preguntas_registro SET texto_pregunta = ?, tipo_entrada = ?, opciones = ?, es_obligatoria = ?, orden_visualizacion = ?, estado = ? WHERE id_pregunta_registro = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, pregunta.getTextoPregunta());
            ps.setString(2, pregunta.getTipoEntrada());    // CAMBIO: Usar getTipoEntrada()
            ps.setString(3, pregunta.getOpciones());       // CAMBIO: Usar getOpciones()
            ps.setBoolean(4, pregunta.isEsObligatoria());
            ps.setInt(5, pregunta.getOrdenVisualizacion());
            ps.setString(6, pregunta.getEstado());
            ps.setInt(7, pregunta.getIdPreguntaRegistro());
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PreguntaRegistroDAO: Error SQL al actualizar pregunta de registro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }

    public boolean eliminarPreguntaRegistro(int id) {
        String sql = "DELETE FROM preguntas_registro WHERE id_pregunta_registro = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PreguntaRegistroDAO: Error SQL al eliminar pregunta de registro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return exito;
    }
}