/*
 * Responsable: José Flores (Refactorizado con Asistente de AED)
 * Relación con otras partes del código:
 * - Es el punto de entrada principal de la aplicación.
 * - Orquesta la navegación inicial a las interfaces de usuario de autenticación,
 * registro y los menús específicos de administrador/encuestado.
 * Funcionalidad:
 * - Inicializa la aplicación y presenta el menú principal de acceso.
 * - Redirige al usuario a la interfaz correspondiente según su rol.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - N/A (Componente de orquestación de UI).
 */
package SteveJobs.encuestas.main;

import SteveJobs.encuestas.gui.LoginGUI; // Importar la nueva GUI de Login
import SteveJobs.encuestas.ui.UIAutenticacion; // La UI de consola/JOptionPane existente
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase principal que inicia la aplicación del Sistema de Encuestas.
 * Permite seleccionar entre la interfaz de usuario basada en consola/JOptionPane
 * o la nueva interfaz gráfica (GUI) basada en JFrame.
 * @author José Flores
 */
public class SistemaEncuestasApp {

    public static void main(String[] args) {
        // Configurar el Look and Feel (opcional, mejora la apariencia de Swing)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo establecer el Look and Feel Nimbus: " + e.getMessage());
            // Continúa con el Look and Feel por defecto si falla
        }

        mostrarMenuInicio();
    }

    private static void mostrarMenuInicio() {
        String[] opciones = {"Iniciar con UI (Consola/JOptionPane)", "Iniciar con GUI (JFrame)", "Salir"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Bienvenido al Sistema de Encuestas. Seleccione el tipo de interfaz:",
                "Selección de Interfaz",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (seleccion) {
            case 0: // Iniciar con UI (Consola/JOptionPane)
                iniciarUI();
                break;
            case 1: // Iniciar con GUI (JFrame)
                iniciarGUI();
                break;
            case 2: // Salir
            case JOptionPane.CLOSED_OPTION: // Si el usuario cierra la ventana
                JOptionPane.showMessageDialog(null, "Gracias por usar el Sistema de Encuestas. ¡Hasta luego!");
                System.exit(0);
                break;
            default:
                // Esto no debería ocurrir si se usan las opciones, pero por seguridad
                JOptionPane.showMessageDialog(null, "Opción no reconocida. Saliendo del sistema.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
        }
    }

    private static void iniciarUI() {
        UIAutenticacion uiAutenticacion = new UIAutenticacion();
        // El bucle de autenticación se gestiona dentro de UIAutenticacion
        // hasta que el usuario se loguea, se registra o cierra la aplicación.
        uiAutenticacion.mostrarLogin();
    }

    private static void iniciarGUI() {
        // Aquí se inicializará la primera ventana de tu GUI de JFrame
        // Por ahora, será la Pantalla de Inicio/Login en JFrame
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.setVisible(true);
        // El resto del flujo de la GUI se gestionará desde esta ventana.
    }
}