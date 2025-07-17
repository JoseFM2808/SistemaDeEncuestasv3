// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-b73347d68ca8a40e851f3439418b915b5f3ce710/src/SteveJobs/encuestas/servicio/ServicioUsuarios.java
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.dao.UsuarioDAO;
import SteveJobs.encuestas.dao.RespuestaUsuarioDAO; // Importar para guardar respuestas dinámicas
import java.time.LocalDate;
import java.util.List;
import java.util.Map; // Importar Map

public class ServicioUsuarios {

    private UsuarioDAO usuarioDAO;
    private RespuestaUsuarioDAO respuestaUsuarioDAO; // Añadido para gestionar respuestas de registro

    public ServicioUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
        this.respuestaUsuarioDAO = new RespuestaUsuarioDAO(); // Inicializar
    }

    // Método renombrado y con nuevo parámetro para respuestas dinámicas
    public boolean registrarUsuario(String dni, String nombres, String apellidos, String email, String clave,
                                         LocalDate fecha_nacimiento, String genero, String distrito_residencia, 
                                         Map<Integer, String> respuestasDinamicas) { // Nuevo parámetro

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
        // El rol ya no se pasa aquí, se asume "Encuestado" por defecto en la creación de usuario.

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setDni(dni.trim());
        nuevoUsuario.setNombres(nombres.trim());
        nuevoUsuario.setApellidos(apellidos.trim());
        nuevoUsuario.setEmail(email.trim());
        nuevoUsuario.setClave(clave);

        nuevoUsuario.setFecha_nacimiento(fecha_nacimiento);
        nuevoUsuario.setGenero(genero != null ? genero.trim() : null);
        nuevoUsuario.setDistrito_residencia(distrito_residencia != null ? distrito_residencia.trim() : null);
        nuevoUsuario.setRol("Encuestado"); // Asignar rol por defecto
        nuevoUsuario.setEstado("ACTIVO"); 

        boolean registrado = usuarioDAO.crearUsuario(nuevoUsuario);

        if (registrado) {
            System.out.println("Servicio: Usuario '" + email.trim() + "' registrado exitosamente.");
            
            // Guardar respuestas a preguntas de registro dinámicas
            int idNuevoUsuario = usuarioDAO.obtenerUsuarioPorEmail(email.trim()).getId_usuario(); // Obtener el ID del usuario recién creado
            if (idNuevoUsuario > 0 && respuestasDinamicas != null && !respuestasDinamicas.isEmpty()) {
                for (Map.Entry<Integer, String> entry : respuestasDinamicas.entrySet()) {
                    respuestaUsuarioDAO.guardarRespuestaRegistro(idNuevoUsuario, entry.getKey(), entry.getValue());
                }
            }
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

   
    public boolean cambiarEstadoUsuario(int idUsuario, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            System.err.println("Servicio: El nuevo estado no puede ser nulo o vacío.");
            return false;
        }
       
        if (!"ACTIVO".equalsIgnoreCase(nuevoEstado) && !"INACTIVO".equalsIgnoreCase(nuevoEstado)) {
             System.err.println("Servicio: El estado '" + nuevoEstado + "' no es un valor permitido (ACTIVO/INACTIVO).");
             return false;
        }

        return usuarioDAO.actualizarEstadoUsuario(idUsuario, nuevoEstado.trim().toUpperCase());
    }
}