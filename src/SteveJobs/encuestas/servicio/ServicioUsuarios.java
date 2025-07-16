/*
 * Responsable: Pablo Alegre
 * Relación con otras partes del código:
 * - Implementa la lógica de negocio para la gestión de usuarios (registro y perfil).
 * - Se comunica con UsuarioDAO para la persistencia.
 * - Utilizado por la UI (UIRegistroUsuario).
 * Funcionalidad:
 * - Permite registrar nuevos usuarios en el sistema, con validaciones de campos.
 * - Proporciona métodos para obtener usuarios y actualizar su perfil.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Lógica de negocio, no maneja colecciones ni ordenamiento directo).
 */

package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.dao.UsuarioDAO;
import java.time.LocalDate;
import java.util.List;

public class ServicioUsuarios {

    private UsuarioDAO usuarioDAO;

    public ServicioUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean registrarNuevoUsuario(String dni, String nombres, String apellidos, String email, String clave,
                                         LocalDate fecha_nacimiento, String genero, String distrito_residencia, String rol) {

        if (dni == null || dni.trim().isEmpty()) {
            System.err.println("Error de registro: El DNI no puede estar vacío.");
            return false;
        }
        if (nombres == null || nombres.trim().isEmpty()) {
            System.err.println("Error de registro: Los nombres no pueden estar vacíos.");
            return false;
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            System.err.println("Error de registro: Los apellidos no pueden estar vacíos.");
            return false;
        }
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Error de registro: El email no puede estar vacío.");
            return false;
        }
        if (clave == null || clave.isEmpty()) {
            System.err.println("Error de registro: La contraseña no puede estar vacía.");
            return false;
        }
        if (rol == null || rol.trim().isEmpty()) {
            System.err.println("Error de registro: El rol no puede estar vacío.");
            return false;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setDni(dni.trim());
        nuevoUsuario.setNombres(nombres.trim());
        nuevoUsuario.setApellidos(apellidos.trim());
        nuevoUsuario.setEmail(email.trim());
        nuevoUsuario.setClave(clave);

        nuevoUsuario.setFecha_nacimiento(fecha_nacimiento);
        nuevoUsuario.setGenero(genero != null ? genero.trim() : null);
        nuevoUsuario.setDistrito_residencia(distrito_residencia != null ? distrito_residencia.trim() : null);
        nuevoUsuario.setRol(rol.trim());
        nuevoUsuario.setEstado("ACTIVO"); // Estado inicial al registrar

        boolean registrado = usuarioDAO.crearUsuario(nuevoUsuario);

        if (registrado) {
            System.out.println("Servicio: Usuario '" + email.trim() + "' registrado exitosamente.");
        } else {
            System.err.println("Servicio: Falló el registro del usuario '" + email.trim() + "'. Verifique logs del DAO (p.ej., email/DNI duplicado o error de BD).");
        }
        return registrado;
    }

    public Usuario obtenerUsuarioPorId(int idUsuario) {
        return usuarioDAO.obtenerUsuarioPorId(idUsuario);
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Servicio: Email inválido para búsqueda.");
            return null;
        }
        return usuarioDAO.obtenerUsuarioPorEmail(email.trim());
    }

    public boolean actualizarPerfilUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId_usuario() <= 0) {
            System.err.println("Servicio: Datos de usuario inválidos o ID de usuario faltante para actualizar.");
            return false;
        }
        return usuarioDAO.actualizarUsuario(usuario);
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDAO.obtenerTodosLosUsuarios();
    }

    public boolean actualizarRolUsuario(int idUsuario, String nuevoRol) {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            System.err.println("Servicio: Usuario ID " + idUsuario + " no encontrado para actualizar rol.");
            return false;
        }
        if (!"Administrador".equalsIgnoreCase(nuevoRol) && !"Encuestado".equalsIgnoreCase(nuevoRol)) {
            System.err.println("Servicio: Rol '" + nuevoRol + "' no válido.");
            return false;
        }
        usuario.setRol(nuevoRol);
        return usuarioDAO.actualizarUsuario(usuario); 
    }
    
    public boolean eliminarUsuario(int idUsuario) {
        return usuarioDAO.eliminarUsuario(idUsuario);
    }

    /**
     * Actualiza el estado de un usuario (ACTIVO, INACTIVO, etc.).
     * @param idUsuario El ID del usuario.
     * @param nuevoEstado El nuevo estado a asignar.
     * @return true si el estado fue actualizado exitosamente, false en caso contrario.
     */
    public boolean cambiarEstadoUsuario(int idUsuario, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            System.err.println("Servicio: El nuevo estado no puede ser nulo o vacío.");
            return false;
        }
        // Puedes añadir validaciones adicionales aquí para los valores permitidos de 'estado'
        if (!"ACTIVO".equalsIgnoreCase(nuevoEstado) && !"INACTIVO".equalsIgnoreCase(nuevoEstado)) {
             System.err.println("Servicio: El estado '" + nuevoEstado + "' no es un valor permitido (ACTIVO/INACTIVO).");
             return false;
        }

        return usuarioDAO.actualizarEstadoUsuario(idUsuario, nuevoEstado.trim().toUpperCase());
    }
}