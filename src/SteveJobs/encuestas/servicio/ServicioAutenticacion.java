
package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.dao.UsuarioDAO;

public class ServicioAutenticacion {

    private UsuarioDAO usuarioDAO;

    public ServicioAutenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    
    public Usuario autenticar(String email, String clave) {

        if (email == null || email.trim().isEmpty()) {
            System.err.println("Error de autenticación: El email no puede estar vacío.");
            return null;
        }
        if (clave == null || clave.isEmpty()) {
            System.err.println("Error de autenticación: La clave no puede estar vacía.");
            return null;
        }

        System.out.println("Intentando autenticar al usuario: " + email);
      
        Usuario usuarioAutenticado = usuarioDAO.validarUsuario(email, clave);

        if (usuarioAutenticado != null) {
            System.out.println("Autenticación exitosa para: " + email);
        } else {
            System.out.println("Autenticación fallida para: " + email);
        }
        return usuarioAutenticado;
    }
}