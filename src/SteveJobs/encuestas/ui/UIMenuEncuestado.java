package SteveJobs.encuestas.ui;

import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioAutenticacion;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioParticipacion;

import java.util.Scanner;
import java.util.List;
import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import java.sql.Timestamp; // Importar Timestamp
import java.util.ArrayList; // Importar ArrayList

/**
 * Interfaz de Usuario (Consola) para el Menú de Encuestado.
 * Permite al encuestado ver encuestas disponibles y responderlas.
 * NOTA: Esta UI es auxiliar y será reemplazada por JFrames.
 *
 * @author José Flores
 */
public class UIMenuEncuestado {
    private final Scanner scanner;
    private Usuario usuarioActual;
    private final ServicioAutenticacion servicioAutenticacion;
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioParticipacion servicioParticipacion;

    public UIMenuEncuestado(Scanner scanner, Usuario usuario) {
        this.scanner = scanner;
        this.usuarioActual = usuario;
        this.servicioAutenticacion = new ServicioAutenticacion();
        this.servicioEncuestas = new ServicioEncuestas();
        this.servicioParticipacion = new ServicioParticipacion();
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- Menú de Encuestado ---");
            System.out.println("1. Ver perfil");
            System.out.println("2. Responder encuesta");
            System.out.println("3. Ver encuestas respondidas (próximamente)");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    // Lógica para ver perfil (si existe una UI para ello)
                    System.out.println("Funcionalidad 'Ver perfil' en desarrollo.");
                    break;
                case 2:
                    responderEncuestaUI();
                    break;
                case 3:
                    System.out.println("Funcionalidad 'Ver encuestas respondidas' en desarrollo.");
                    break;
                case 4:
                    System.out.println("Cerrando sesión...");
                    usuarioActual = null; // Limpiar usuario de la sesión
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 4);
    }

    private void responderEncuestaUI() {
        System.out.println("\n--- Responder Encuesta ---");
        List<Encuesta> encuestasDisponibles = servicioEncuestas.obtenerEncuestasActivasParaUsuario(usuarioActual);

        if (encuestasDisponibles.isEmpty()) {
            System.out.println("No hay encuestas disponibles para responder en este momento o ya has respondido a todas las que calificas.");
            return;
        }

        System.out.println("Encuestas disponibles para usted:");
        boolean hayEncuestasSinResponder = false;
        for (Encuesta enc : encuestasDisponibles) {
            // Verificar si el usuario ya respondió esta encuesta
            if (!servicioParticipacion.haUsuarioRespondidoEncuesta(usuarioActual.getId_usuario(), enc.getIdEncuesta())) {
                System.out.println("ID: " + enc.getIdEncuesta() + " - " + enc.getNombre() + " (" + enc.getEstado() + ")");
                hayEncuestasSinResponder = true;
            }
        }

        if (!hayEncuestasSinResponder) {
            System.out.println("Ya has respondido a todas las encuestas disponibles para ti.");
            return;
        }

        System.out.print("Ingrese el ID de la encuesta que desea responder: ");
        int idEncuestaSeleccionada = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        Encuesta encuestaSeleccionada = servicioEncuestas.buscarEncuestaEnListaPorId(encuestasDisponibles, idEncuestaSeleccionada);

        if (encuestaSeleccionada == null) {
            System.out.println("Encuesta no encontrada o no disponible para usted.");
            return;
        }

        // Simulación de recolección y guardado de respuestas para la consola
        // En la implementación real con JFrame, esta lógica es mucho más compleja
        // y se maneja en EncuestaResponderGUI.
        List<RespuestaUsuario> respuestasSimuladas = new ArrayList<>();
        // Por simplicidad, aquí solo se simula una respuesta si no hay una real.
        // En un escenario real de consola, se iteraría por las preguntas.
        System.out.println("Simulando respuesta a la encuesta " + encuestaSeleccionada.getNombre());
        // Se añade una respuesta ficticia para que la llamada al servicio compile
        // y se entienda que se espera una lista de respuestas.
        respuestasSimuladas.add(new RespuestaUsuario(usuarioActual.getId_usuario(), 
                                                   encuestaSeleccionada.getPreguntasAsociadas().isEmpty() ? 1 : encuestaSeleccionada.getPreguntasAsociadas().get(0).getIdEncuestaDetalle(), 
                                                   "Respuesta simulada"));

        try {
            // Se llama al método con la nueva firma
            servicioParticipacion.guardarRespuestasCompletas(
                respuestasSimuladas, // Lista de respuestas
                usuarioActual.getId_usuario(), // ID del usuario
                encuestaSeleccionada.getIdEncuesta(), // ID de la encuesta
                new Timestamp(System.currentTimeMillis()), // Fecha/hora de finalización
                0 // Duración en segundos (0 para simulación)
            );
            System.out.println("¡Encuesta simulada respondida y guardada exitosamente!");
        } catch (Exception e) {
            System.err.println("Error al simular guardar respuestas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}