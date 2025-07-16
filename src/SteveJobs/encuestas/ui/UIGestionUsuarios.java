package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioUsuarios;

import javax.swing.JOptionPane;
import java.util.List;

public class UIGestionUsuarios {

    private static ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

    public static void mostrarMenu() {
        int opcion;
        do {
            String menu = "--- Gestión de Usuarios ---\n"
                    + "1. Listar Todos los Usuarios\n"
                    + "2. Modificar Perfil de Usuario\n"
                    + "3. Cambiar Rol de Usuario\n" // Opcional, si se permite cambiar roles
                    + "4. Eliminar Usuario\n"
                    + "0. Volver al Menú Principal";
            String input = JOptionPane.showInputDialog(null, menu, "Menú Gestión Usuarios", JOptionPane.PLAIN_MESSAGE);

            if (input == null) {
                opcion = 0;
            } else {
                try {
                    opcion = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opción inválida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    opcion = -1;
                }
            }

            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    modificarPerfilUsuario();
                    break;
                case 3:
                    cambiarRolUsuario();
                    break;
                case 4:
                    eliminarUsuario();
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Volviendo al Menú Principal del Administrador.", "Volver", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    if (opcion != -1) {
                        JOptionPane.showMessageDialog(null, "Opción no reconocida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (opcion != 0);
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = servicioUsuarios.obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios registrados en el sistema.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- Lista de Usuarios ---\n\n");
        for (Usuario u : usuarios) {
            sb.append("ID: ").append(u.getId_usuario()).append("\n");
            sb.append("DNI: ").append(u.getDni()).append("\n");
            sb.append("Nombres: ").append(u.getNombres()).append(" ").append(u.getApellidos()).append("\n");
            sb.append("Email: ").append(u.getEmail()).append("\n");
            sb.append("Rol: ").append(u.getRol()).append("\n");
            sb.append("--------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Usuarios", JOptionPane.PLAIN_MESSAGE);
    }

    private static void modificarPerfilUsuario() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID del usuario a modificar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = servicioUsuarios.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado con ID: " + idUsuario, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoDni = JOptionPane.showInputDialog("Nuevo DNI (actual: " + usuario.getDni() + "):", usuario.getDni());
        if (nuevoDni == null) return;
        String nuevosNombres = JOptionPane.showInputDialog("Nuevos Nombres (actual: " + usuario.getNombres() + "):", usuario.getNombres());
        if (nuevosNombres == null) return;
        String nuevosApellidos = JOptionPane.showInputDialog("Nuevos Apellidos (actual: " + usuario.getApellidos() + "):", usuario.getApellidos());
        if (nuevosApellidos == null) return;
        String nuevoEmail = JOptionPane.showInputDialog("Nuevo Email (actual: " + usuario.getEmail() + "):", usuario.getEmail());
        if (nuevoEmail == null) return;
        
        // Asumiendo que el modelo Usuario tiene LocalDate para fecha_nacimiento
        String fechaNacStr = JOptionPane.showInputDialog("Nueva Fecha Nacimiento (YYYY-MM-DD, actual: " + usuario.getFecha_nacimiento() + "):", usuario.getFecha_nacimiento().toString());
        if (fechaNacStr == null) return;
        LocalDate nuevaFechaNacimiento;
        try {
            nuevaFechaNacimiento = LocalDate.parse(fechaNacStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] generos = {"MASCULINO", "FEMENINO", "OTRO"};
        String nuevoGenero = (String) JOptionPane.showInputDialog(null, 
                "Nuevo Género (actual: " + usuario.getGenero() + "):", "Seleccionar Género", 
                JOptionPane.QUESTION_MESSAGE, null, generos, usuario.getGenero());
        if (nuevoGenero == null) return;

        String nuevoDistrito = JOptionPane.showInputDialog("Nuevo Distrito (actual: " + usuario.getDistrito_residencia() + "):", usuario.getDistrito_residencia());
        if (nuevoDistrito == null) return;

        // Crear un objeto Usuario temporal con los nuevos datos
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId_usuario(idUsuario);
        usuarioActualizado.setDni(nuevoDni);
        usuarioActualizado.setNombres(nuevosNombres);
        usuarioActualizado.setApellidos(nuevosApellidos);
        usuarioActualizado.setEmail(nuevoEmail);
        usuarioActualizado.setFecha_nacimiento(nuevaFechaNacimiento);
        usuarioActualizado.setGenero(nuevoGenero);
        usuarioActualizado.setDistrito_residencia(nuevoDistrito);
        usuarioActualizado.setRol(usuario.getRol()); // El rol se cambia en otro método

        boolean actualizado = servicioUsuarios.actualizarPerfilUsuario(usuarioActualizado);
        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Perfil de usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el perfil del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void cambiarRolUsuario() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID del usuario al que desea cambiar el rol:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = servicioUsuarios.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado con ID: " + idUsuario, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] roles = {"Administrador", "Encuestado"};
        String nuevoRol = (String) JOptionPane.showInputDialog(null, 
                "Seleccione el nuevo rol para " + usuario.getNombres() + " (actual: " + usuario.getRol() + "):", "Cambiar Rol", 
                JOptionPane.QUESTION_MESSAGE, null, roles, usuario.getRol());
        if (nuevoRol == null) return;

        boolean actualizado = servicioUsuarios.actualizarRolUsuario(idUsuario, nuevoRol); // Asume este método en ServicioUsuarios
        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Rol de usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error al cambiar el rol del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarUsuario() {
        String idStr = JOptionPane.showInputDialog("Ingrese el ID del usuario a eliminar:");
        if (idStr == null || idStr.trim().isEmpty()) return;
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar al usuario con ID " + idUsuario + "? Esta acción es irreversible.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = servicioUsuarios.eliminarUsuario(idUsuario);
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el usuario. Verifique el ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}