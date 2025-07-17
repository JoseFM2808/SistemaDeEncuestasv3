package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioResultados;
import SteveJobs.encuestas.servicio.ServicioPreguntas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap; // Importar HashMap

/**
 * Pantalla de Reportes de Encuesta para el Administrador (JFrame).
 * Permite al administrador visualizar y exportar reportes de frecuencia
 * y promedios de respuestas para encuestas seleccionadas, con filtros.
 *
 * @author José Flores
 */
public class ReportesEncuestaGUI extends JFrame {

    private Encuesta encuestaSeleccionada;
    private final AdminDashboardGUI parentDashboard;
    private final ServicioResultados servicioResultados;
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioPreguntas servicioPreguntas;

    private JComboBox<String> cmbEncuestas;
    private JComboBox<String> cmbTipoFiltro;
    private JRadioButton rbFrecuencia, rbPromedios;
    private ButtonGroup bgTipoReporte;
    private JTable tblReporte;
    private DefaultTableModel tableModel;
    private JLabel lblTituloReporte;
    private JTextArea txtResumen;
    private JButton btnGenerar, btnExportar, btnVolver;

    private Map<String, Map<String, Integer>> reporteFrecuenciaActual;
    private Map<String, Double> reportePromediosActual;

    public ReportesEncuestaGUI(AdminDashboardGUI parent) {
        super("Sistema de Encuestas - Reportes de Encuesta");
        this.parentDashboard = parent;
        this.servicioResultados = new ServicioResultados();
        this.servicioEncuestas = new ServicioEncuestas();
        this.servicioPreguntas = new ServicioPreguntas();
        initComponents();
        setupFrame();
        cargarCombos();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Generación de Reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de Controles ---
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("Seleccionar Encuesta:"), gbc);
        gbc.gridx = 1;
        cmbEncuestas = new JComboBox<>();
        cmbEncuestas.setPreferredSize(new Dimension(300, 25));
        cmbEncuestas.addActionListener(e -> {
            int selectedIndex = cmbEncuestas.getSelectedIndex();
            if (selectedIndex > 0) { // Evitar el primer elemento "Seleccione..."
                String selectedItem = (String) cmbEncuestas.getSelectedItem();
                int idEncuesta = Integer.parseInt(selectedItem.split(" - ")[0]);
                encuestaSeleccionada = servicioEncuestas.obtenerDetallesCompletosEncuesta(idEncuesta);
            } else {
                encuestaSeleccionada = null;
            }
            limpiarReporte();
        });
        controlPanel.add(cmbEncuestas, gbc);

        gbc.gridx = 2;
        controlPanel.add(new JLabel("Filtrar por Tipo de Pregunta:"), gbc);
        gbc.gridx = 3;
        cmbTipoFiltro = new JComboBox<>();
        cmbTipoFiltro.setPreferredSize(new Dimension(200, 25));
        cmbTipoFiltro.addActionListener(e -> limpiarReporte());
        controlPanel.add(cmbTipoFiltro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbFrecuencia = new JRadioButton("Reporte de Frecuencia de Respuestas");
        rbPromedios = new JRadioButton("Reporte de Promedios por Pregunta");
        bgTipoReporte = new ButtonGroup();
        bgTipoReporte.add(rbFrecuencia);
        bgTipoReporte.add(rbPromedios);
        radioPanel.add(rbFrecuencia);
        radioPanel.add(rbPromedios);
        rbFrecuencia.setSelected(true); // Seleccionar por defecto
        rbFrecuencia.addActionListener(e -> limpiarReporte());
        rbPromedios.addActionListener(e -> limpiarReporte());
        controlPanel.add(radioPanel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.addActionListener(e -> generarReporte());
        controlPanel.add(btnGenerar, gbc);

        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // --- Panel de Resultados ---
        JPanel reportPanel = new JPanel(new BorderLayout(10, 10));
        lblTituloReporte = new JLabel("Resultados del Reporte", SwingConstants.CENTER);
        lblTituloReporte.setFont(new Font("Arial", Font.BOLD, 18));
        reportPanel.add(lblTituloReporte, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReporte = new JTable(tableModel);
        tblReporte.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScrollPane = new JScrollPane(tblReporte);
        reportPanel.add(tableScrollPane, BorderLayout.CENTER);

        txtResumen = new JTextArea(5, 40);
        txtResumen.setEditable(false);
        txtResumen.setLineWrap(true);
        txtResumen.setWrapStyleWord(true);
        JScrollPane summaryScrollPane = new JScrollPane(txtResumen);
        reportPanel.add(summaryScrollPane, BorderLayout.SOUTH);

        mainPanel.add(reportPanel, BorderLayout.CENTER);

        // --- Panel de Botones Inferior ---
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnExportar = new JButton("Exportar Reporte a TXT");
        btnExportar.addActionListener(e -> exportarReporte());
        btnVolver = new JButton("Volver al Dashboard");
        btnVolver.addActionListener(e -> volverAlDashboard());

        bottomButtonPanel.add(btnExportar);
        bottomButtonPanel.add(btnVolver);
        mainPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        // addWindowListener para volver al dashboard se maneja en el btnVolver
    }

    private void cargarCombos() {
        // Cargar JComboBox de encuestas
        cmbEncuestas.removeAllItems();
        cmbEncuestas.addItem("Seleccione una encuesta...");
        try {
            List<Encuesta> encuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();
            for (Encuesta encuesta : encuestas) {
                cmbEncuestas.addItem(encuesta.getIdEncuesta() + " - " + encuesta.getNombre());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar encuestas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Cargar JComboBox de tipos de pregunta
        cmbTipoFiltro.removeAllItems();
        cmbTipoFiltro.addItem("Todos los tipos"); // Opción para no filtrar
        try {
            List<TipoPregunta> tipos = servicioPreguntas.obtenerTodosLosTiposPregunta();
            for (TipoPregunta tipo : tipos) {
                cmbTipoFiltro.addItem(tipo.getNombreTipo());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarReporte() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        lblTituloReporte.setText("Resultados del Reporte");
        txtResumen.setText("");
        reporteFrecuenciaActual = null;
        reportePromediosActual = null;
    }

    private void generarReporte() {
        if (encuestaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una encuesta primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEncuesta = encuestaSeleccionada.getIdEncuesta();
        String tipoFiltroNombre = (String) cmbTipoFiltro.getSelectedItem();
        
        // Construir el mapa de filtros para el servicio
        Map<String, String> filtros = new HashMap<>();
        if (!"Todos los tipos".equalsIgnoreCase(tipoFiltroNombre)) {
            filtros.put("tipoPregunta", tipoFiltroNombre);
        }

        try {
            // Usar el método filtrarResultados existente en ServicioResultados
            List<RespuestaUsuario> respuestasFiltradas = servicioResultados.filtrarResultados(idEncuesta, filtros); //

            if (respuestasFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay respuestas para los criterios seleccionados.", "Información", JOptionPane.INFORMATION_MESSAGE);
                limpiarReporte();
                return;
            }

            if (rbFrecuencia.isSelected()) {
                lblTituloReporte.setText("Reporte de Frecuencia de Respuestas para " + encuestaSeleccionada.getNombre());
                reporteFrecuenciaActual = servicioResultados.generarReporteFrecuenciaRespuestas(respuestasFiltradas);
                mostrarReporteFrecuencia(reporteFrecuenciaActual);
            } else if (rbPromedios.isSelected()) {
                lblTituloReporte.setText("Reporte de Promedios por Pregunta para " + encuestaSeleccionada.getNombre());
                reportePromediosActual = servicioResultados.calcularPromediosPorPregunta(respuestasFiltradas);
                mostrarReportePromedios(reportePromediosActual);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ReportesEncuestaGUI al generar reporte: " + ex.getMessage());
            ex.printStackTrace();
            limpiarReporte();
        }
    }

    private void mostrarReporteFrecuencia(Map<String, Map<String, Integer>> reporte) {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        // Asumiendo que la primera clave del mapa interior es siempre la primera columna de opciones
        // y que todas las preguntas tienen la misma estructura en sus respuestas para el reporte de frecuencia
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Pregunta (Tipo | Clasificación)");
        
        // Recoger todas las opciones de respuesta únicas para las columnas
        for (Map.Entry<String, Map<String, Integer>> entry : reporte.entrySet()) {
            for (String opcion : entry.getValue().keySet()) {
                if (!columnNames.contains(opcion)) {
                    columnNames.add(opcion);
                }
            }
        }
        tableModel.setColumnIdentifiers(columnNames);

        for (Map.Entry<String, Map<String, Integer>> entry : reporte.entrySet()) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(entry.getKey()); // Nombre de la pregunta
            for (int i = 1; i < columnNames.size(); i++) {
                String opcion = columnNames.get(i);
                rowData.add(entry.getValue().getOrDefault(opcion, 0)); // Frecuencia de la opción
            }
            tableModel.addRow(rowData);
        }
        
        // Resumen
        StringBuilder resumen = new StringBuilder();
        resumen.append("Reporte de Frecuencia de Respuestas:\n");
        reporte.forEach((pregunta, frecuencias) -> {
            resumen.append(pregunta).append(":\n");
            frecuencias.forEach((opcion, count) -> 
                resumen.append("  - ").append(opcion).append(": ").append(count).append("\n")
            );
        });
        txtResumen.setText(resumen.toString());
    }

    private void mostrarReportePromedios(Map<String, Double> reporte) {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"Pregunta (Tipo | Clasificación)", "Promedio"});

        for (Map.Entry<String, Double> entry : reporte.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), String.format("%.2f", entry.getValue())});
        }
        
        // Resumen
        StringBuilder resumen = new StringBuilder();
        resumen.append("Reporte de Promedios por Pregunta:\n");
        reporte.forEach((pregunta, promedio) -> 
            resumen.append(pregunta).append(": ").append(String.format("%.2f", promedio)).append("\n")
        );
        txtResumen.setText(resumen.toString());
    }

    private void exportarReporte() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".txt")) {
                filePath += ".txt"; // Asegurar extensión .txt
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(lblTituloReporte.getText() + "\n\n");

                if (rbFrecuencia.isSelected() && reporteFrecuenciaActual != null) {
                    writer.write("--- REPORTE DE FRECUENCIA DE RESPUESTAS ---\n");
                    for (Map.Entry<String, Map<String, Integer>> entry : reporteFrecuenciaActual.entrySet()) {
                        writer.write("Pregunta: " + entry.getKey() + "\n");
                        for (Map.Entry<String, Integer> freqEntry : entry.getValue().entrySet()) {
                            writer.write("  - " + freqEntry.getKey() + ": " + freqEntry.getValue() + "\n");
                        }
                        writer.write("\n");
                    }
                } else if (rbPromedios.isSelected() && reportePromediosActual != null) {
                    writer.write("--- REPORTE DE PROMEDIOS POR PREGUNTA ---\n");
                    for (Map.Entry<String, Double> entry : reportePromediosActual.entrySet()) {
                        writer.write("Pregunta: " + entry.getKey() + " | Promedio: " + String.format("%.2f", entry.getValue()) + "\n");
                    }
                } else {
                    writer.write("No hay reporte generado para exportar.\n");
                }
                writer.write("\n" + txtResumen.getText()); // Incluir el resumen también

                JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente a:\n" + filePath, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ReportesEncuestaGUI al exportar reporte: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void volverAlDashboard() {
        this.dispose();
        parentDashboard.mostrarAdminDashboardGUI();
    }
}