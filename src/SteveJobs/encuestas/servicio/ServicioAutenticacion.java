/*
 * Autor: Gian Fri (Desarrollo inicial), Pablo Alegre (Responsable del Módulo de Gestión de Usuarios)
 *
 * Propósito: Clase de servicio que encapsula la lógica de negocio para la autenticación de usuarios.
 * Valida los datos de entrada y delega la verificación de credenciales a UsuarioDAO.
 * Es fundamental para REQMS-014.
 */
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.dao.UsuarioDAO;

public class ServicioAutenticacion {

    private UsuarioDAO usuarioDAO;


    public ServicioAutenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            System.err.println("Error de autenticación: El email no puede estar vacío.");
            return null;
        }
        if (password == null || password.isEmpty()) {

            System.err.println("Error de autenticación: El password no puede estar vacío.");
            return null;
        }


        System.out.println("Intentando autenticar al usuario: " + email);
        Usuario usuarioAutenticado = usuarioDAO.validarUsuario(email, password);

        if (usuarioAutenticado != null) {
            System.out.println("Autenticación exitosa para: " + email);
        } else {
            System.out.println("Autenticación fallida para: " + email);
        }
        return usuarioAutenticado;
    }
}