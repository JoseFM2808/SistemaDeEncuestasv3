package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.servicio.ServicioPreguntas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Diálogo modal para seleccionar una pregunta del banco de preguntas existente.
 *
 * @author José Flores
 */
public class DialogoSeleccionarPreguntaBanco extends JDialog {

    private JTable tblBancoPreguntas;
    private DefaultTableModel tableModel;
    private PreguntaBanco preguntaSeleccionada = null; // La pregunta que se devolverá

    private final ServicioPreguntas servicioPreguntas;

    public DialogoSeleccionarPreguntaBanco(JFrame parent) {
        super(parent, "Seleccionar Pregunta del Banco", true); // true para modal
        servicioPreguntas = new ServicioPreguntas();
        initComponents();
        setupDialog();
        cargarBancoPreguntas();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Seleccione una Pregunta del Banco", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Tabla para listar preguntas del banco
        String[] columnNames = {"ID", "Texto Pregunta", "Tipo", "Clasificación"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBancoPreguntas = new JTable(tableModel);
        tblBancoPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblBancoPreguntas.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tblBancoPreguntas);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnSeleccionar);
        buttonPanel.add(btnCancelar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos ---
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarPregunta();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preguntaSeleccionada = null; // No seleccionar nada
                dispose();
            }
        });
        
        // Doble click para seleccionar
        tblBancoPreguntas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    seleccionarPregunta();
                }
            }
        });
    }

    private void setupDialog() {
        setSize(700, 450); // Tamaño fijo para el diálogo
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void cargarBancoPreguntas() {
        tableModel.setRowCount(0);
        try {
            List<PreguntaBanco> preguntas = servicioPreguntas.obtenerTodasLasPreguntasDelBanco();
            if (preguntas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "No hay preguntas en el banco.", "", ""});
                tblBancoPreguntas.setEnabled(false);
                return;
            }
            for (PreguntaBanco pregunta : preguntas) {
                tableModel.addRow(new Object[]{
                    pregunta.getIdPreguntaBanco(),
                    pregunta.getTextoPregunta(),
                    pregunta.getTipoPregunta() != null ? pregunta.getTipoPregunta().getNombreTipo() : "N/A",
                    pregunta.getClasificacionPregunta() != null ? pregunta.getClasificacionPregunta().getNombreClasificacion() : "N/A"
                });
            }
            tblBancoPreguntas.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el banco de preguntas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en DialogoSeleccionarPreguntaBanco al cargar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void seleccionarPregunta() {
        int selectedRow = tblBancoPreguntas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una pregunta de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
             JOptionPane.showMessageDialog(this, "No hay una pregunta válida seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
             return;
        }

        int idPregunta = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            preguntaSeleccionada = servicioPreguntas.obtenerPreguntaPorId(idPregunta);
            dispose(); // Cierra el diálogo
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener la pregunta seleccionada: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en DialogoSeleccionarPreguntaBanco al seleccionar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public PreguntaBanco getPreguntaSeleccionada() {
        return preguntaSeleccionada;
    }
}