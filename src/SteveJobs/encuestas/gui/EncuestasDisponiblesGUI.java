package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Pantalla para que el usuario encuestado vea y seleccione encuestas disponibles.
 *
 * @author José Flores
 */
public class EncuestasDisponiblesGUI extends JFrame {

    private Usuario encuestadoActual;
    private final ServicioEncuestas servicioEncuestas;
    private final EncuestadoMenuGUI parentMenu;

    private JTable tblEncuestas;
    private DefaultTableModel tableModel;

    public EncuestasDisponiblesGUI(Usuario encuestado, EncuestadoMenuGUI parent) {
        super("Sistema de Encuestas - Encuestas Disponibles");
        this.encuestadoActual = encuestado;
        this.parentMenu = parent;
        this.servicioEncuestas = new ServicioEncuestas();
        initComponents();
        setupFrame();
        cargarEncuestasDisponibles();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Encuestas Disponibles para Ti", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Tabla para listar encuestas
        String[] columnNames = {"ID", "Nombre", "Descripción", "Fecha Inicio", "Fecha Fin"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables
            }
        };
        tblEncuestas = new JTable(tableModel);
        tblEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo una selección a la vez
        JScrollPane scrollPane = new JScrollPane(tblEncuestas);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnResponder = new JButton("Responder Encuesta Seleccionada");
        JButton btnVolver = new JButton("Volver al Menú");

        buttonPanel.add(btnResponder);
        buttonPanel.add(btnVolver);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos ---
        btnResponder.addActionListener(e -> responderEncuesta());
        btnVolver.addActionListener(e -> volverAlMenu());

        // Doble click para responder
        tblEncuestas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    responderEncuesta();
                }
            }
        });
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500); // Tamaño adecuado para una tabla
        setLocationRelativeTo(null);
        
        // Listener para cuando se cierra la ventana, para mostrar el menú padre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentMenu.mostrarEncuestadoMenuGUI();
            }
        });
    }

    public void cargarEncuestasDisponibles() { // Made public to be called by child GUIs if needed
        // Limpiar tabla antes de cargar nuevos datos
        tableModel.setRowCount(0);

        try {
            // Obtener encuestas activas y que el usuario califique
            List<Encuesta> encuestas = servicioEncuestas.obtenerEncuestasActivasParaUsuario(encuestadoActual);
            
            if (encuestas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "", "No hay encuestas disponibles para tu perfil en este momento.", "", ""});
                tblEncuestas.setEnabled(false); // Deshabilitar selección si no hay encuestas reales
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Encuesta encuesta : encuestas) {
                // Verificar si el usuario ya respondió esta encuesta
                if (!servicioEncuestas.haUsuarioRespondidoEncuesta(encuestadoActual.getIdUsuario(), encuesta.getIdEncuesta())) {
                     tableModel.addRow(new Object[]{
                        encuesta.getIdEncuesta(),
                        encuesta.getNombre(),
                        encuesta.getDescripcion(),
                        encuesta.getFechaInicio() != null ? sdf.format(encuesta.getFechaInicio()) : "N/A",
                        encuesta.getFechaFin() != null ? sdf.format(encuesta.getFechaFin()) : "N/A"
                    });
                }
            }
            if (tableModel.getRowCount() == 0) {
                 tableModel.addRow(new Object[]{"", "", "Has respondido todas las encuestas disponibles para tu perfil.", "", ""});
                 tblEncuestas.setEnabled(false);
            } else {
                tblEncuestas.setEnabled(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar encuestas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestasDisponiblesGUI al cargar encuestas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void responderEncuesta() {
        int selectedRow = tblEncuestas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una encuesta para responder.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Asegurarse de que no sea la fila de "No hay encuestas" o "Has respondido todas"
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
             JOptionPane.showMessageDialog(this, "No hay una encuesta válida seleccionada para responder.", "Advertencia", JOptionPane.WARNING_MESSAGE);
             return;
        }

        int idEncuesta = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            Encuesta encuestaParaResponder = servicioEncuestas.obtenerDetallesCompletosEncuesta(idEncuesta);
            if (encuestaParaResponder == null) {
                JOptionPane.showMessageDialog(this, "La encuesta seleccionada no se pudo encontrar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lógica para verificar si ya respondió (ServicioEncuestas ya tiene este método)
            if (servicioEncuestas.haUsuarioRespondidoEncuesta(encuestadoActual.getIdUsuario(), encuestaParaResponder.getIdEncuesta())) {
                JOptionPane.showMessageDialog(this, "Ya has respondido esta encuesta.", "Información", JOptionPane.INFORMATION_MESSAGE);
                // Si ya la respondió, recargar para que desaparezca de la lista
                cargarEncuestasDisponibles(); 
                return;
            }

            // Abrir la Pantalla de Respuesta de Encuesta (GUI)
            EncuestaResponderGUI responderGUI = new EncuestaResponderGUI(encuestadoActual, encuestaParaResponder, this);
            responderGUI.setVisible(true);
            this.setVisible(false); // Ocultar esta ventana mientras se responde
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al intentar responder encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestasDisponiblesGUI al responder encuesta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void mostrarEncuestasDisponiblesGUI() { // Made public for child GUIs to call
        this.setVisible(true);
        cargarEncuestasDisponibles(); // Recargar la lista al volver
    }

    private void volverAlMenu() {
        this.dispose(); // Cierra esta ventana
        // parentMenu.mostrarEncuestadoMenuGUI() ya se llama en windowClosed
    }
}