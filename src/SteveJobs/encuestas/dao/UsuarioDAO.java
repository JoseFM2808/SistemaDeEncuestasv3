/*
 * Responsable: Pablo Alegre
 * Relación con otras partes del código:
 * - Es la persistencia principal para la entidad Usuario, utilizada por
 * ServicioAutenticacion y ServicioUsuarios para la gestión de usuarios.
 * Funcionalidad:
 * - Proporciona métodos para crear, obtener, actualizar y eliminar objetos Usuario
 * en la base de datos. Incluye la validación de credenciales para la autenticación.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - Retorna colecciones de tipo List<Usuario>.
 * - No emplea ordenamiento interno explícito para sus operaciones CRUD, más allá del orden de la BD.
 */

package SteveJobs.encuestas.dao;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (dni, nombres, apellidos, email, clave, fecha_nacimiento, genero, distrito_residencia, fecha_registro, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        boolean registrado = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getClave());
            ps.setDate(6, usuario.getFecha_nacimiento() != null ? java.sql.Date.valueOf(usuario.getFecha_nacimiento()) : null);
            ps.setString(7, usuario.getGenero());
            ps.setString(8, usuario.getDistrito_residencia());
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis())); // Fecha de registro actual
            ps.setString(10, usuario.getRol());
            ps.setString(11, usuario.getEstado()); // Guardar el estado inicial

            if (ps.executeUpdate() > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setId_usuario(generatedKeys.getInt(1));
                }
                registrado = true;
            }
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al crear usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(generatedKeys, ps, con);
        }
        return registrado;
    }

    public Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT id_usuario, dni, nombres, apellidos, email, clave, fecha_nacimiento, genero, distrito_residencia, fecha_registro, rol, estado FROM usuarios WHERE id_usuario = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al obtener usuario por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return usuario;
    }

    public Usuario obtenerUsuarioPorDNI(String dni) {
        String sql = "SELECT id_usuario, dni, nombres, apellidos, email, clave, fecha_nacimiento, genero, distrito_residencia, fecha_registro, rol, estado FROM usuarios WHERE dni = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return null;
            ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al obtener usuario por DNI: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return usuario;
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        String sql = "SELECT id_usuario, dni, nombres, apellidos, email, clave, fecha_nacimiento, genero, distrito_residencia, fecha_registro, rol, estado FROM usuarios WHERE email = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return null;
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al obtener usuario por email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return usuario;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET dni = ?, nombres = ?, apellidos = ?, email = ?, fecha_nacimiento = ?, genero = ?, distrito_residencia = ?, rol = ?, estado = ? WHERE id_usuario = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean actualizado = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getEmail());
            ps.setDate(5, usuario.getFecha_nacimiento() != null ? java.sql.Date.valueOf(usuario.getFecha_nacimiento()) : null);
            ps.setString(6, usuario.getGenero());
            ps.setString(7, usuario.getDistrito_residencia());
            ps.setString(8, usuario.getRol());
            ps.setString(9, usuario.getEstado()); // Actualizar estado
            ps.setInt(10, usuario.getId_usuario());

            actualizado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return actualizado;
    }

    public boolean actualizarClaveUsuario(int idUsuario, String nuevaClave) {
        String sql = "UPDATE usuarios SET clave = ? WHERE id_usuario = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean actualizado = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaClave);
            ps.setInt(2, idUsuario);
            actualizado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al actualizar clave de usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return actualizado;
    }
    
    /**
     * Nuevo método: Actualiza el estado de un usuario específico.
     * @param idUsuario El ID del usuario cuyo estado se actualizará.
     * @param nuevoEstado El nuevo estado (ej. "ACTIVO", "INACTIVO").
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarEstadoUsuario(int idUsuario, String nuevoEstado) {
        String sql = "UPDATE usuarios SET estado = ? WHERE id_usuario = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean actualizado = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            actualizado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al actualizar estado del usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return actualizado;
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        Connection con = null;
        PreparedStatement ps = null;
        boolean eliminado = false;

        try {
            con = ConexionDB.conectar();
            if (con == null) return false;
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            eliminado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(null, ps, con);
        }
        return eliminado;
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, dni, nombres, apellidos, email, clave, fecha_nacimiento, genero, distrito_residencia, fecha_registro, rol, estado FROM usuarios ORDER BY nombres ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return usuarios;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearResultSetAUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return usuarios;
    }

    public Usuario validarUsuario(String email, String clave) {
        String sql = "SELECT id_usuario, dni, nombres, apellidos, email, clave, fecha_nacimiento, genero, distrito_residencia, fecha_registro, rol, estado FROM usuarios WHERE email = ? AND clave = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            con = ConexionDB.conectar();
            if (con == null) return null;
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, clave);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("UsuarioDAO: Error SQL al validar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar(rs, ps, con);
        }
        return usuario;
    }

    private Usuario mapearResultSetAUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setDni(rs.getString("dni"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setClave(rs.getString("clave")); // Solo para propósitos internos, no exponer en otras capas
        
        java.sql.Date sqlDate = rs.getDate("fecha_nacimiento");
        usuario.setFecha_nacimiento(sqlDate != null ? sqlDate.toLocalDate() : null);

        usuario.setGenero(rs.getString("genero"));
        usuario.setDistrito_residencia(rs.getString("distrito_residencia"));
        usuario.setFecha_registro(rs.getTimestamp("fecha_registro"));
        usuario.setRol(rs.getString("rol"));
        usuario.setEstado(rs.getString("estado")); // Mapear el nuevo campo estado
        return usuario;
    }
}