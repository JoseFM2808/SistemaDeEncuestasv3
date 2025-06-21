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
        // Email pattern validation removed as per REQ
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

    public boolean cambiarEstadoUsuario(int idUsuario, String nuevoEstado) {
        System.err.println("Servicio: cambiarEstadoUsuario no está completamente implementado o requiere clarificación de la estructura de 'estado' y DAO.");
        return false;
    }
}