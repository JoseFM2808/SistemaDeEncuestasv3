/*
 * Responsable: Pablo Alegre (Corregido por Asistente de AED para alineación con modelo final)
 * Relación con otras partes del código:
 * - Es el componente que implementa la lógica de negocio para el inicio de sesión.
 * - Se comunica con UsuarioDAO para validar las credenciales.
 * - Utilizado por la UI (UIAutenticacion).
 * Funcionalidad:
 * - Autentica a un usuario verificando su email y contraseña.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Lógica de negocio, no maneja colecciones ni ordenamiento directo).
 */

package SteveJobs.encuestas.servicio;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.dao.UsuarioDAO;

public class ServicioAutenticacion {

    private UsuarioDAO usuarioDAO;

    public ServicioAutenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autentica a un usuario basado en su email y clave.
     * @param email El email del usuario.
     * @param clave La clave (contraseña) del usuario.
     * @return Un objeto Usuario si la autenticación es exitosa, de lo contrario null.
     */
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
        // Se llama al DAO con el parámetro 'clave' corregido
        Usuario usuarioAutenticado = usuarioDAO.validarUsuario(email, clave);

        if (usuarioAutenticado != null) {
            System.out.println("Autenticación exitosa para: " + email);
        } else {
            System.out.println("Autenticación fallida para: " + email);
        }
        return usuarioAutenticado;
    }
}