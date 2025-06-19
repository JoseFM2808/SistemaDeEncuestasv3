// Nuevo archivo: SteveJobs.encuestas.ui.UIGestionTiposPregunta.java
package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Usar el DAO directamente para operaciones CRUD simples

import javax.swing.JOptionPane;
import java.util.List;

public class UIGestionTiposPregunta {

    private static TipoPreguntaDAO tipoPreguntaDAO = new TipoPreguntaDAO(); // DAO de Pablo

    public static void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            String[] opciones = {
                "Listar Tipos de Pregunta",
                "Agregar Nuevo Tipo de Pregunta",
                "Modificar Tipo de Pregunta", // Si hay campos modificables (ej. descripción si la hubiera)
                "Eliminar Tipo de Pregunta",
                "Volver al Menú Administrador"
            };
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gestión de Tipos de Pregunta",
                    "Admin: Tipos de Pregunta",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == null) {
                salir = true;
                continue;
            }

            switch (seleccion) {
                case "Listar Tipos de Pregunta":
                    listarTiposPreguntaUI();
                    break;
                case "Agregar Nuevo Tipo de Pregunta":
                    agregarTipoPreguntaUI();
                    break;
                case "Modificar Tipo de Pregunta":
                    // Implementar si TipoPregunta tiene más campos modificables
                    JOptionPane.showMessageDialog(null, "Funcionalidad de modificación de Tipo de Pregunta pendiente o no aplica para este modelo simple.");
                    break;
                case "Eliminar Tipo de Pregunta":
                    eliminarTipoPreguntaUI();
                    break;
                case "Volver al Menú Administrador":
                    salir = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private static void listarTiposPreguntaUI() {
        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTiposPregunta(); // Método recién agregado al DAO
        if (tipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tipos de pregunta definidos.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Tipos de Pregunta Existentes:\n\n");
        for (TipoPregunta tipo : tipos) {
            sb.append("ID: ").append(tipo.getIdTipoPregunta())
              .append(" - Nombre: ").append(tipo.getNombreTipo())
              .append("\n------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Tipos de Pregunta", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void agregarTipoPreguntaUI() {
        String nombre = JOptionPane.showInputDialog(null, "Nombre del nuevo tipo de pregunta:", "Agregar Tipo", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre del tipo no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simplemente agregamos el tipo. Se podría añadir un método de creación en TipoPreguntaDAO
        // Por simplicidad, asumimos que el DAO debería tener un método de guardar si se gestiona el CRUD completo.
        // Si Pablo decide implementar agregarTipoPregunta en TipoPreguntaDAO:
        // boolean agregado = tipoPreguntaDAO.agregarTipoPregunta(new TipoPregunta(nombre.trim()));
        // Por ahora, asumimos que solo se obtienen de una lista predefinida o ya existen.
        // Si se necesitan crear nuevos, el DAO de TipoPregunta deberá extenderse.

        JOptionPane.showMessageDialog(null, "Creación de tipos de pregunta no implementada aún en el DAO.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void eliminarTipoPreguntaUI() {
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del tipo de pregunta a eliminar:", "Eliminar Tipo", JOptionPane.PLAIN_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Simplemente indicamos que la eliminación es pendiente a la implementación del DAO
        JOptionPane.showMessageDialog(null, "Eliminación de tipos de pregunta no implementada aún en el DAO.", "Información", JOptionPane.INFORMATION_MESSAGE);
        // if (tipoPreguntaDAO.eliminarTipoPregunta(id)) { ... }
    }
}