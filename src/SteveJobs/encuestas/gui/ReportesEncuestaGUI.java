package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioPreguntas; // Para Tipos de Pregunta
import SteveJobs.encuestas.servicio.ServicioResultados;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Pantalla de Reporte de Resultados para el Administrador (JFrame).
 * Permite seleccionar una encuesta y un filtro por tipo de pregunta para
 * visualizar reportes de frecuencia y promedios, con opciones de exportación.
 *
 * @author José Flores
 */
public class ReportesEncuestaGUI extends JFrame {

    private Usuario administradorActual;
    private final AdminDashboardGUI parentDashboard;
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioResultados servicioResultados;
    private final ServicioPreguntas servicioPreguntas; // Para obtener tipos de pregunta

    private JComboBox<String> cmbEncuesta;
    private JComboBox<String> cmbTipoFiltro;
    private JTable tblResultados;
    private DefaultTableModel tableModel;
    private JButton btnGenerarReporte, btnExportarTexto, btnExportarCSV, btnVolver;

    private Map<String, Encuesta> mapaNombresEncuestas; // Para mapear nombre a objeto Encuesta
    private Map<String, TipoPregunta> mapaNombresTipos; // Para mapear nombre a objeto TipoPregunta

    public ReportesEncuestaGUI(Usuario admin, AdminDashboardGUI parent) {
        super("Sistema de Encuestas - Reportes de Resultados");
        this.administradorActual = admin;
        this.parentDashboard = parent;
        this.servicioEncuestas = new ServicioEncuestas();
        this.servicioResultados = new ServicioResultados();
        this.servicioPreguntas = new ServicioPreguntas();
        this.mapaNombresEncuestas = new HashMap<>();
        this.mapaNombresTipos = new HashMap<>();

        initComponents();
        setupFrame();
        cargarCombos();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Reportes de Resultados de Encuestas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de controles de selección ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Selección de Reporte"));

        controlPanel.add(new JLabel("Seleccionar Encuesta:"));
        cmbEncuesta = new JComboBox<>();
        cmbEncuesta.setPreferredSize(new Dimension(250, 25));
        controlPanel.add(cmbEncuesta);

        controlPanel.add(new JLabel("Filtro por Tipo de Pregunta:"));
        cmbTipoFiltro = new JComboBox<>();
        cmbTipoFiltro.setPreferredSize(new Dimension(150, 25));
        controlPanel.add(cmbTipoFiltro);

        btnGenerarReporte = new JButton("Generar Reporte");
        controlPanel.add(btnGenerarReporte);

        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // --- Panel de la tabla de resultados ---
        String[] columnNames = {"Pregunta", "Tipo", "Clasificación", "Resultado (Frecuencia/Promedio)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblResultados = new JTable(tableModel);
        tblResultados.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tblResultados);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel de botones de exportación y volver ---
        JPanel exportButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnExportarTexto = new JButton("Exportar a Texto");
        btnExportarCSV = new JButton("Exportar a CSV");
        btnVolver = new JButton("Volver al Dashboard");

        exportButtonPanel.add(btnExportarTexto);
        exportButtonPanel.add(btnExportarCSV);
        exportButtonPanel.add(btnVolver);
        mainPanel.add(exportButtonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos ---
        btnGenerarReporte.addActionListener(e -> generarReporte());
        btnExportarTexto.addActionListener(e -> exportarReporte(true));
        btnExportarCSV.addActionListener(e -> exportarReporte(false));
        btnVolver.addActionListener(e -> volverAlDashboard());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentDashboard.mostrarAdminDashboardGUI();
            }
        });
    }

    private void cargarCombos() {
        // Cargar Encuestas
        cmbEncuesta.addItem("Seleccione una encuesta...");
        try {
            List<Encuesta> encuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();
            for (Encuesta encuesta : encuestas) {
                cmbEncuesta.addItem(encuesta.getNombre());
                mapaNombresEncuestas.put(encuesta.getNombre(), encuesta);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar encuestas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ReportesEncuestaGUI al cargar encuestas: " + ex.getMessage());
        }

        // Cargar Tipos de Pregunta para el filtro
        cmbTipoFiltro.addItem("Todos los tipos"); // Opción para no filtrar
        try {
            List<TipoPregunta> tipos = servicioPreguntas.obtenerTodosLosTiposPregunta();
            for (TipoPregunta tipo : tipos) {
                cmbTipoFiltro.addItem(tipo.getNombreTipo());
                mapaNombresTipos.put(tipo.getNombreTipo(), tipo);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ReportesEncuestaGUI al cargar tipos: " + ex.getMessage());
        }
    }

    private void generarReporte() {
        tableModel.setRowCount(0); // Limpiar tabla
        String nombreEncuestaSeleccionada = (String) cmbEncuesta.getSelectedItem();

        if (nombreEncuestaSeleccionada == null || nombreEncuestaSeleccionada.equals("Seleccione una encuesta...")) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una encuesta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Encuesta encuestaSeleccionada = mapaNombresEncuestas.get(nombreEncuestaSeleccionada);
        if (encuestaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar la encuesta seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipoFiltroSeleccionado = (String) cmbTipoFiltro.getSelectedItem();
        Integer idTipoFiltro = null;
        if (tipoFiltroSeleccionado != null && !tipoFiltroSeleccionado.equals("Todos los tipos")) {
            TipoPregunta tipo = mapaNombresTipos.get(tipoFiltroSeleccionado);
            if (tipo != null) {
                idTipoFiltro = tipo.getIdTipoPregunta();
            }
        }

        try {
            // Generar reportes de frecuencia
            Map<String, String> reporteFrecuencia = servicioResultados.generarReporteFrecuenciaRespuestas(encuestaSeleccionada.getIdEncuesta(), idTipoFiltro);
            for (Map.Entry<String, String> entry : reporteFrecuencia.entrySet()) {
                // El formato de la clave es "TextoPregunta | Tipo | Clasificacion"
                String[] partes = entry.getKey().split(" \\| ");
                String textoPregunta = partes[0];
                String tipo = partes.length > 1 ? partes[1] : "";
                String clasificacion = partes.length > 2 ? partes[2] : "";
                tableModel.addRow(new Object[]{textoPregunta, tipo, clasificacion, entry.getValue()});
            }

            // Generar reportes de promedio (si aplica)
            Map<String, Double> reportePromedios = servicioResultados.calcularPromediosPorPregunta(encuestaSeleccionada.getIdEncuesta(), idTipoFiltro);
            for (Map.Entry<String, Double> entry : reportePromedios.entrySet()) {
                String[] partes = entry.getKey().split(" \\| ");
                String textoPregunta = partes[0];
                String tipo = partes.length > 1 ? partes[1] : "";
                String clasificacion = partes.length > 2 ? partes[2] : "";
                 tableModel.addRow(new Object[]{textoPregunta, tipo, clasificacion, String.format("%.2f", entry.getValue())});
            }

            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"", "", "No hay resultados para esta encuesta con el filtro seleccionado.", ""});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ReportesEncuestaGUI al generar reporte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void exportarReporte(boolean toText) {
        if (tableModel.getRowCount() == 0 || (tableModel.getRowCount() == 1 && tableModel.getValueAt(0, 0).toString().isEmpty())) {
            JOptionPane.showMessageDialog(this, "No hay reporte generado para exportar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        
        // Sugerir nombre de archivo
        String nombreEncuesta = (String) cmbEncuesta.getSelectedItem();
        String tipoReporte = (toText ? "reporte_texto" : "reporte_csv");
        String suggestedFileName = nombreEncuesta.replace(" ", "_").toLowerCase() + "_" + tipoReporte + ".txt";
        if (!toText) {
            suggestedFileName = suggestedFileName.replace(".txt", ".csv");
        }
        fileChooser.setSelectedFile(new File(suggestedFileName));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                StringBuilder content = new StringBuilder();
                // Encabezados de la tabla
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    content.append(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        content.append(toText ? "\t" : ",");
                    }
                }
                content.append("\n");

                // Datos de la tabla
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Object cellValue = tableModel.getValueAt(row, col);
                        if (cellValue != null) {
                            String value = cellValue.toString();
                            if (!toText && value.contains(",")) { // Para CSV, si el valor contiene comas, encerrar en comillas
                                value = "\"" + value.replace("\"", "\"\"") + "\"";
                            }
                            content.append(value);
                        }
                        if (col < tableModel.getColumnCount() - 1) {
                            content.append(toText ? "\t" : ",");
                        }
                    }
                    content.append("\n");
                }
                
                // Llamar al servicio para exportar (reutilizando la lógica existente)
                if (toText) {
                    servicioResultados.exportarReporteATexto(content.toString(), fileToSave.getAbsolutePath());
                } else {
                    servicioResultados.exportarReporteACsv(content.toString(), fileToSave.getAbsolutePath());
                }

                JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente a:\n" + fileToSave.getAbsolutePath(), "Éxito de Exportación", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ReportesEncuestaGUI al exportar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void volverAlDashboard() {
        this.dispose();
    }
}