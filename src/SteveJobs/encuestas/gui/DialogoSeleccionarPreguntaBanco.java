package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.servicio.ServicioPreguntas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects; 

/**
 * Diálogo modal para seleccionar una pregunta del banco de preguntas
 * y asociarla a una encuesta.
 *
 * @author José Flores
 */
public class DialogoSeleccionarPreguntaBanco extends JDialog {

    private ServicioPreguntas servicioPreguntas;
    private JTable tblPreguntas;
    private DefaultTableModel tableModel;
    private JButton btnSeleccionar, btnCancelar;

    private PreguntaBanco preguntaSeleccionada;

    public DialogoSeleccionarPreguntaBanco(JFrame parent) {
        super(parent, "Seleccionar Pregunta del Banco", true); // true para modal
        this.servicioPreguntas = new ServicioPreguntas();
        initComponents();
        setupDialog();
        cargarPreguntas();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Seleccione una pregunta del banco", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Texto Pregunta", "Tipo", "Clasificación"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPreguntas = new JTable(tableModel);
        tblPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPreguntas.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tblPreguntas);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnSeleccionar = new JButton("Seleccionar");
        btnCancelar = new JButton("Cancelar");

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
                preguntaSeleccionada = null; // No seleccionar nada al cancelar
                dispose();
            }
        });

        // Doble click en la tabla para seleccionar
        tblPreguntas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    seleccionarPregunta();
                }
            }
        });
    }

    private void setupDialog() {
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        setResizable(true);
    }

    private void cargarPreguntas() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<PreguntaBanco> preguntas = servicioPreguntas.obtenerTodasLasPreguntasDelBanco();
            if (preguntas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "No hay preguntas en el banco.", "", ""});
                tblPreguntas.setEnabled(false);
                return;
            }

            for (PreguntaBanco pregunta : preguntas) {
                tableModel.addRow(new Object[]{
                    pregunta.getIdPreguntaBanco(), // CORRECCIÓN: Usar getIdPreguntaBanco()
                    pregunta.getTextoPregunta(),
                    pregunta.getNombreTipoPregunta() != null ? pregunta.getNombreTipoPregunta() : "N/A",
                    pregunta.getNombreClasificacion() != null ? pregunta.getNombreClasificacion() : "N/A"
                });
            }
            tblPreguntas.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las preguntas del banco: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en DialogoSeleccionarPreguntaBanco al cargar preguntas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void seleccionarPregunta() {
        int selectedRow = tblPreguntas.getSelectedRow();
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
            if (preguntaSeleccionada != null) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo obtener la pregunta seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar la pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en DialogoSeleccionarPreguntaBanco al seleccionar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public PreguntaBanco getPreguntaSeleccionada() {
        return preguntaSeleccionada;
    }
}