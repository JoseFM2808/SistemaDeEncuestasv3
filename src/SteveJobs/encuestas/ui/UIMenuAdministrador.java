package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.util.PilaNavegacion;
import javax.swing.JOptionPane;


public class UIMenuAdministrador {

    private final PilaNavegacion<Runnable> historialMenu;
    private final Usuario administradorActual; 

    public UIMenuAdministrador(Usuario administradorActual) {
        this.historialMenu = new PilaNavegacion<>();
        this.administradorActual = administradorActual;
    }

    public void mostrarMenuAdministrador() {
        historialMenu.push(this::mostrarOpcionesPrincipales); 
        
        while (!historialMenu.isEmpty()) {
            try {
                historialMenu.peek().run(); 
            } catch (Exception e) { 
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en UIMenuAdministrador: " + e.getMessage());
                e.printStackTrace(); 
                if (historialMenu.size() > 1) { /
                    historialMenu.pop();
                } else { 
                    JOptionPane.showMessageDialog(null, "Volviendo al menú principal de la aplicación...", "Error", JOptionPane.INFORMATION_MESSAGE);
                    
                    while (!historialMenu.isEmpty()) { 
                        historialMenu.pop(); 
                    } 
                }
            }
        }
    }

    private void mostrarOpcionesPrincipales() {
        String menu = """
                      --- Menú de Administrador ---
                      Bienvenido/a, %s %s (ID: %d)
                      1. Gestionar Encuestas
                      2. Gestionar Banco de Preguntas
                      3. Gestionar Preguntas de Registro (Perfil de Encuestado)
                      4. Gestionar Tipos de Pregunta
                      5. Gestionar Clasificaciones de Pregunta
                      6. Gestionar Usuarios
                      7. Ver Resultados de Encuestas
                      8. Cerrar Sesión
                      """.formatted(administradorActual.getNombres(), administradorActual.getApellidos(), administradorActual.getId_usuario()); 
        String opcionStr = JOptionPane.showInputDialog(null, menu, "Menú Administrador", JOptionPane.PLAIN_MESSAGE);

        if (opcionStr == null) { 
            historialMenu.pop(); 
            return;
        }

        try {
            int opcion = Integer.parseInt(opcionStr);
            switch (opcion) {
                case 1:
                    historialMenu.push(this::gestionarEncuestas);
                    break;
                case 2:
                    historialMenu.push(this::gestionarBancoPreguntas);
                    break;
                case 3:
                    historialMenu.push(this::gestionarPreguntasRegistro);
                    break;
                case 4:
                    historialMenu.push(this::gestionarTiposPregunta);
                    break;
                case 5:
                    historialMenu.push(this::gestionarClasificacionesPregunta);
                    break;
                case 6:
                    historialMenu.push(this::gestionarUsuarios);
                    break;
                case 7:
                    historialMenu.push(this::verResultadosEncuestas);
                    break;
                case 8:
                    historialMenu.pop(); 
                    JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void gestionarEncuestas() {
        UIGestionEncuestas.mostrarMenu(administradorActual); 
        historialMenu.pop(); 
    }

    private void gestionarBancoPreguntas() {
        UIGestionBancoPreguntas.mostrarMenu(); 
        historialMenu.pop();
    }

    private void gestionarPreguntasRegistro() {
        UIGestionPreguntasRegistro.mostrarMenu(); 
        historialMenu.pop();
    }
    
    private void gestionarTiposPregunta() {
        UIGestionTiposPregunta.mostrarMenu(); 
        historialMenu.pop();
    }
    
    private void gestionarClasificacionesPregunta() {
        UIGestionClasificacionesPregunta.mostrarMenu(); 
        historialMenu.pop();
    }

    private void gestionarUsuarios() {
        UIGestionUsuarios.mostrarMenu(); 
        historialMenu.pop();
    }

    private void verResultadosEncuestas() {
        UIReportesEncuesta.mostrarMenu(); 
        historialMenu.pop();
    }
}