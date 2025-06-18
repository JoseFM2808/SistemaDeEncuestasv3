/*
 * Autor: José Flores (Responsable del Módulo de Interacción del Encuestado y Visualización de Resultados)
 *
 * Propósito: Interfaz de Usuario (UI) que presenta el menú de opciones para el usuario Encuestado.
 * Actualmente es un placeholder y requiere la implementación de las funcionalidades
 * para visualizar encuestas disponibles, responder encuestas y consultar el perfil.
 * Es central para REQMS-001, REQMS-002, REQMS-003 y REQMS-015.
 */
package SteveJobs.encuestas.ui;
import SteveJobs.encuestas.modelo.Usuario;
import javax.swing.JOptionPane;


public class UIMenuEncuestado {

    public static void mostrarMenu(Usuario encuestado) {
        JOptionPane.showMessageDialog(null, "Bienvenido al Portal del Encuestado, " + encuestado.getNombresApellidos() + "!\n(Funcionalidades pendientes de implementación)", "Portal Encuestado", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
