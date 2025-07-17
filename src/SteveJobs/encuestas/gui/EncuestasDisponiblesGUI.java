package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioParticipacion; // Importar ServicioParticipacion
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Pantalla de Encuestas Disponibles para el Usuario Encuestado (JFrame).
 * Permite al encuestado ver las encuestas activas para las que califica
 * y responderlas.
 *
 * @author José Flores
 */
public class EncuestasDisponiblesGUI extends JFrame {

    private Usuario usuarioActual;
    private final EncuestadoMenuGUI parentMenu; // Referencia al menú padre
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioParticipacion servicioParticipacion; // Instancia de ServicioParticipacion

    private JTable tblEncuestas;
    private DefaultTableModel tableModel;

    private JButton btnResponderEncuesta, btnVolver;

    public EncuestasDisponiblesGUI(Usuario usuario, EncuestadoMenuGUI parent) {
        super("Sistema de Encuestas - Encuestas Disponibles");
        this.usuarioActual = usuario;
        this.parentMenu = parent;
        this.servicioEncuestas = new ServicioEncuestas();
        this.servicioParticipacion = new ServicioParticipacion(); // Inicializar
        initComponents();
        setupFrame();
        cargarEncuestasDisponibles();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Encuestas Disponibles", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de la tabla de encuestas ---
        String[] columnNames = {"ID", "Nombre", "Descripción", "F. Inicio", "F. Fin", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables
            }
        };
        tblEncuestas = new JTable(tableModel);
        tblEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEncuestas.getTableHeader().setReorderingAllowed(false); // No permitir reordenar columnas
        JScrollPane scrollPane = new JScrollPane(tblEncuestas);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel de botones de acción ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnResponderEncuesta = new JButton("Responder Encuesta Seleccionada");
        btnVolver = new JButton("Volver al Menú");
        
        buttonPanel.add(btnResponderEncuesta);
        buttonPanel.add(btnVolver);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos de los botones ---
        btnResponderEncuesta.addActionListener(e -> responderEncuesta());
        btnVolver.addActionListener(e -> volverAlMenu());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentMenu.mostrarEncuestadoMenuGUI(); // Mostrar el menú padre
            }
        });
    }

    public void cargarEncuestasDisponibles() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<Encuesta> encuestas = servicioEncuestas.obtenerEncuestasActivasParaUsuario(usuarioActual);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            if (encuestas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "", "No hay encuestas disponibles para ti.", "", "", ""});
                tblEncuestas.setEnabled(false);
                btnResponderEncuesta.setEnabled(false);
                return;
            }

            for (Encuesta encuesta : encuestas) {
                // Verificar si el usuario ya respondió esta encuesta
                if (!servicioParticipacion.haUsuarioRespondidoEncuesta(usuarioActual.getId_usuario(), encuesta.getIdEncuesta())) { //
                    tableModel.addRow(new Object[]{
                        encuesta.getIdEncuesta(),
                        encuesta.getNombre(),
                        encuesta.getDescripcion(),
                        encuesta.getFechaInicio() != null ? sdf.format(encuesta.getFechaInicio()) : "N/A",
                        encuesta.getFechaFin() != null ? sdf.format(encuesta.getFechaFin()) : "N/A",
                        encuesta.getEstado()
                    });
                }
            }
            // Si después del filtrado no hay encuestas, se muestra el mensaje
            if (tableModel.getRowCount() == 0) {
                 tableModel.addRow(new Object[]{"", "", "Has respondido todas las encuestas disponibles para ti o no hay nuevas.", "", "", ""});
                 tblEncuestas.setEnabled(false);
                 btnResponderEncuesta.setEnabled(false);
            } else {
                 tblEncuestas.setEnabled(true);
                 btnResponderEncuesta.setEnabled(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las encuestas disponibles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestasDisponiblesGUI al cargar encuestas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Encuesta getSelectedEncuesta() {
        int selectedRow = tblEncuestas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una encuesta de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        // Asegurarse de que no sea la fila de "No hay encuestas"
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay una encuesta válida seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int idEncuesta = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            // Obtener la encuesta completa con sus detalles (preguntas, etc.)
            return servicioEncuestas.obtenerDetallesCompletosEncuesta(idEncuesta);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener detalles de la encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestasDisponiblesGUI al obtener detalles: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    private void responderEncuesta() {
        Encuesta encuestaSeleccionada = getSelectedEncuesta();
        if (encuestaSeleccionada == null) return;

        try {
            // Verificar si el usuario ya respondió esta encuesta antes de abrirla
            if (servicioParticipacion.haUsuarioRespondidoEncuesta(usuarioActual.getId_usuario(), encuestaSeleccionada.getIdEncuesta())) { //
                JOptionPane.showMessageDialog(this, "Ya has respondido a esta encuesta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                cargarEncuestasDisponibles(); // Actualizar la lista en caso de que el estado haya cambiado
                return;
            }

            // CORRECCIÓN: Invertir el orden de los argumentos para que coincida con el constructor de EncuestaResponderGUI
            EncuestaResponderGUI responderGUI = new EncuestaResponderGUI(usuarioActual, encuestaSeleccionada, this);
            responderGUI.setVisible(true);
            this.setVisible(false); // Ocultar esta ventana
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al intentar responder la encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestasDisponiblesGUI al responder encuesta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void volverAlMenu() {
        this.dispose(); // Cierra esta ventana. El listener se encargará de mostrar el padre.
    }
}