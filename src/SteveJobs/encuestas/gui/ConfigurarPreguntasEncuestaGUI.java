package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioPreguntas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects; // Importar Objects

/**
 * Pantalla para configurar las preguntas de una encuesta específica (JFrame).
 * Permite al administrador añadir preguntas del banco, crear preguntas únicas,
 * modificar detalles de preguntas en la encuesta (descarte, criterio),
 * eliminar y reordenar preguntas.
 *
 * @author José Flores
 */
public class ConfigurarPreguntasEncuestaGUI extends JFrame {

    private Encuesta encuestaActual;
    private final SteveJobs.encuestas.modelo.Usuario administradorActual;
    private final GestionEncuestasGUI parentGestionEncuestas; // Referencia a la ventana padre
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioPreguntas servicioPreguntas; // Para obtener tipos y clasificaciones

    private JTable tblPreguntasEncuesta;
    private DefaultTableModel tableModel;

    private JButton btnAgregarDelBanco, btnAgregarUnica, btnModificarPregunta;
    private JButton btnEliminarPregunta, btnMoverArriba, btnMoverAbajo, btnVolver;
    private JCheckBox chkEsDescarte;
    private JTextField txtCriterioDescarte;

    // Componentes del panel de edición/visualización de detalle
    private JLabel lblTextoPreguntaDetalle, lblTipoPreguntaDetalle, lblClasificacionPreguntaDetalle;

    public ConfigurarPreguntasEncuestaGUI(Encuesta encuesta, SteveJobs.encuestas.modelo.Usuario admin, GestionEncuestasGUI parent) {
        super("Configurar Preguntas para: " + encuesta.getNombre());
        this.encuestaActual = encuesta;
        this.administradorActual = admin;
        this.parentGestionEncuestas = parent;
        this.servicioEncuestas = new ServicioEncuestas();
        this.servicioPreguntas = new ServicioPreguntas(); // Inicializar
        initComponents();
        setupFrame();
        cargarPreguntasEncuesta();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Configurar Preguntas: " + encuestaActual.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de la tabla de preguntas ---
        String[] columnNames = {"ID Detalle", "Orden", "Texto Pregunta", "Tipo", "Clasificación", "¿Es Descarte?", "Criterio Descarte"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPreguntasEncuesta = new JTable(tableModel);
        tblPreguntasEncuesta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPreguntasEncuesta.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tblPreguntasEncuesta);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de control para detalles de la pregunta seleccionada y descarte
        JPanel detalleControlPanel = new JPanel(new GridBagLayout());
        detalleControlPanel.setBorder(BorderFactory.createTitledBorder("Detalle de Pregunta Seleccionada"));
        GridBagConstraints gbcDetalle = new GridBagConstraints();
        gbcDetalle.insets = new Insets(5, 5, 5, 5);
        gbcDetalle.fill = GridBagConstraints.HORIZONTAL;

        gbcDetalle.gridx = 0; gbcDetalle.gridy = 0;
        detalleControlPanel.add(new JLabel("Texto:"), gbcDetalle);
        gbcDetalle.gridx = 1;
        lblTextoPreguntaDetalle = new JLabel("Seleccione una pregunta para ver detalles.");
        lblTextoPreguntaDetalle.setFont(new Font("Arial", Font.ITALIC, 12));
        detalleControlPanel.add(lblTextoPreguntaDetalle, gbcDetalle);

        gbcDetalle.gridx = 0; gbcDetalle.gridy = 1;
        detalleControlPanel.add(new JLabel("Tipo:"), gbcDetalle);
        gbcDetalle.gridx = 1;
        lblTipoPreguntaDetalle = new JLabel("N/A");
        detalleControlPanel.add(lblTipoPreguntaDetalle, gbcDetalle);

        gbcDetalle.gridx = 0; gbcDetalle.gridy = 2;
        detalleControlPanel.add(new JLabel("Clasificación:"), gbcDetalle);
        gbcDetalle.gridx = 1;
        lblClasificacionPreguntaDetalle = new JLabel("N/A");
        detalleControlPanel.add(lblClasificacionPreguntaDetalle, gbcDetalle);
        
        gbcDetalle.gridx = 0; gbcDetalle.gridy = 3;
        chkEsDescarte = new JCheckBox("Es Pregunta de Descarte");
        detalleControlPanel.add(chkEsDescarte, gbcDetalle);

        gbcDetalle.gridx = 1;
        txtCriterioDescarte = new JTextField(15);
        txtCriterioDescarte.setToolTipText("Valor que descalifica (ej: 'No', '2', 'Madrid')");
        detalleControlPanel.add(txtCriterioDescarte, gbcDetalle);

        mainPanel.add(detalleControlPanel, BorderLayout.EAST);

        // --- Panel de botones de acción ---
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 filas, 2 columnas
        btnAgregarDelBanco = new JButton("Añadir del Banco");
        btnAgregarUnica = new JButton("Crear Pregunta Única");
        btnModificarPregunta = new JButton("Modificar Detalle Pregunta");
        btnEliminarPregunta = new JButton("Eliminar Pregunta");
        btnMoverArriba = new JButton("Mover Arriba");
        btnMoverAbajo = new JButton("Mover Abajo");
        btnVolver = new JButton("Volver a Gestión de Encuestas");
        
        buttonPanel.add(btnAgregarDelBanco);
        buttonPanel.add(btnAgregarUnica);
        buttonPanel.add(btnModificarPregunta);
        buttonPanel.add(btnEliminarPregunta);
        buttonPanel.add(btnMoverArriba);
        buttonPanel.add(btnMoverAbajo);
        buttonPanel.add(btnVolver);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos ---
        tblPreguntasEncuesta.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPreguntasEncuesta.getSelectedRow() != -1) {
                seleccionarFilaTabla();
            } else {
                limpiarDetallePanel();
            }
        });

        chkEsDescarte.addActionListener(e -> toggleCriterioDescarteField());
        btnAgregarDelBanco.addActionListener(e -> agregarPreguntaDelBanco());
        btnAgregarUnica.addActionListener(e -> agregarPreguntaUnica());
        btnModificarPregunta.addActionListener(e -> modificarPreguntaEncuesta());
        btnEliminarPregunta.addActionListener(e -> eliminarPregunta());
        btnMoverArriba.addActionListener(e -> moverPregunta(true));
        btnMoverAbajo.addActionListener(e -> moverPregunta(false));
        btnVolver.addActionListener(e -> volverAGestionEncuestas());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700); // Tamaño más grande
        setLocationRelativeTo(null);
        
        // Listener para cuando se cierra la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentGestionEncuestas.cargarEncuestas(); // Recargar la tabla del padre
                parentGestionEncuestas.setVisible(true); // Mostrar el padre
            }
        });
    }

    private void cargarPreguntasEncuesta() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            // Recargar la encuesta para asegurar que los detalles de las preguntas estén actualizados
            encuestaActual = servicioEncuestas.obtenerDetallesCompletosEncuesta(encuestaActual.getIdEncuesta());
            List<EncuestaDetallePregunta> preguntas = encuestaActual.getPreguntasAsociadas();

            if (preguntas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "", "No hay preguntas configuradas para esta encuesta.", "", "", "", ""});
                tblPreguntasEncuesta.setEnabled(false);
                limpiarDetallePanel();
                return;
            }

            for (EncuestaDetallePregunta preguntaDetalle : preguntas) {
                tableModel.addRow(new Object[]{
                    preguntaDetalle.getIdEncuestaDetalle(),
                    preguntaDetalle.getOrdenEnEncuesta(),
                    preguntaDetalle.getTextoPreguntaMostrable(),
                    // Usar los getters unificados para tipo y clasificación
                    preguntaDetalle.getNombreTipoPregunta() != null ? preguntaDetalle.getNombreTipoPregunta() : "N/A",
                    preguntaDetalle.getNombreClasificacionPregunta() != null ? preguntaDetalle.getNombreClasificacionPregunta() : "N/A",
                    preguntaDetalle.isEsPreguntaDescarte() ? "Sí" : "No",
                    Objects.requireNonNullElse(preguntaDetalle.getCriterioDescarteValor(), "N/A")
                });
            }
            tblPreguntasEncuesta.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las preguntas de la encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al cargar preguntas: " + ex.getMessage());
            ex.printStackTrace();
        }
        limpiarDetallePanel();
    }

    private void seleccionarFilaTabla() {
        int selectedRow = tblPreguntasEncuesta.getSelectedRow();
        if (selectedRow == -1) {
            limpiarDetallePanel();
            return;
        }

        // Asegurarse de que no sea la fila de "No hay preguntas"
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
            limpiarDetallePanel();
            return;
        }

        int idDetalle = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            // CORRECCIÓN: Usar el nuevo método en ServicioEncuestas
            EncuestaDetallePregunta detalle = servicioEncuestas.obtenerPreguntaDetallePorId(idDetalle);
            if (detalle != null) {
                lblTextoPreguntaDetalle.setText("<html>" + detalle.getTextoPreguntaMostrable() + "</html>");
                lblTipoPreguntaDetalle.setText(detalle.getNombreTipoPregunta() != null ? detalle.getNombreTipoPregunta() : "N/A");
                lblClasificacionPreguntaDetalle.setText(detalle.getNombreClasificacionPregunta() != null ? detalle.getNombreClasificacionPregunta() : "N/A");
                chkEsDescarte.setSelected(detalle.isEsPreguntaDescarte());
                txtCriterioDescarte.setText(Objects.requireNonNullElse(detalle.getCriterioDescarteValor(), ""));
                toggleCriterioDescarteField(); // Ajustar visibilidad del campo de criterio
            } else {
                limpiarDetallePanel();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalle de pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al seleccionar fila: " + ex.getMessage());
            ex.printStackTrace();
            limpiarDetallePanel();
        }
    }

    private void limpiarDetallePanel() {
        lblTextoPreguntaDetalle.setText("Seleccione una pregunta para ver detalles.");
        lblTipoPreguntaDetalle.setText("N/A");
        lblClasificacionPreguntaDetalle.setText("N/A");
        chkEsDescarte.setSelected(false);
        txtCriterioDescarte.setText("");
        txtCriterioDescarte.setEnabled(false);
        tblPreguntasEncuesta.clearSelection();
    }

    private void toggleCriterioDescarteField() {
        txtCriterioDescarte.setEnabled(chkEsDescarte.isSelected());
        if (!chkEsDescarte.isSelected()) {
            txtCriterioDescarte.setText("");
        }
    }

    private void agregarPreguntaDelBanco() {
        DialogoSeleccionarPreguntaBanco dialogo = new DialogoSeleccionarPreguntaBanco(this);
        dialogo.setVisible(true);

        // CORRECCIÓN: Comprobar si se seleccionó una pregunta verificando el objeto devuelto
        PreguntaBanco preguntaSeleccionada = dialogo.getPreguntaSeleccionada();
        if (preguntaSeleccionada != null) {
            int orden = servicioEncuestas.obtenerPreguntasDeEncuesta(encuestaActual.getIdEncuesta()).size() + 1; // Último orden + 1
            boolean esDescarte = false; // Por defecto al añadir del banco, se configura después
            String criterioDescarte = null; // Se configura después

            try {
                if (servicioEncuestas.asociarPreguntaDelBancoAEncuesta(encuestaActual.getIdEncuesta(),
                        preguntaSeleccionada.getIdPreguntaBanco(), orden, esDescarte, criterioDescarte)) {
                    JOptionPane.showMessageDialog(this, "Pregunta del banco añadida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPreguntasEncuesta();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo añadir la pregunta del banco. Posiblemente la encuesta ya tiene 12 preguntas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al añadir pregunta del banco: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ConfigurarPreguntasEncuestaGUI al añadir del banco: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void agregarPreguntaUnica() {
        DialogoCrearPreguntaUnica dialogo = new DialogoCrearPreguntaUnica(this);
        dialogo.setVisible(true);

        if (dialogo.isGuardadoExitoso()) { // Asumiendo que DialogoCrearPreguntaUnica tiene isGuardadoExitoso()
            String textoPregunta = dialogo.getTxtTextoPregunta().getText();
            String nombreTipo = (String) dialogo.getCmbTipoPregunta().getSelectedItem();
            String nombreClasificacion = (String) dialogo.getCmbClasificacion().getSelectedItem(); // Puede ser null

            // CORRECCIÓN: Obtener el ID del tipo de pregunta a partir del nombre
            // Se asume que el servicio de preguntas puede proporcionar esto
            TipoPregunta tipoSeleccionado = servicioPreguntas.obtenerTodosLosTiposPregunta().stream()
                .filter(t -> t.getNombreTipo().equals(nombreTipo))
                .findFirst().orElse(null);
            
            // CORRECCIÓN: Obtener el ID de la clasificación a partir del nombre
            ClasificacionPregunta clasifSeleccionada = null;
            if (nombreClasificacion != null && !nombreClasificacion.isEmpty()) {
                // Asumiendo que ServicioPreguntas puede obtener clasificaciones por nombre o una lista
                // Si no existe, se necesitaría un método como servicioPreguntas.obtenerTodasLasClasificaciones()
                // y luego filtrar. Para mantener la cohesión, lo hacemos aquí si el servicio no lo expone directamente.
                List<ClasificacionPregunta> clasificaciones = servicioPreguntas.obtenerTodasLasClasificaciones(); // Nuevo método necesario en ServicioPreguntas
                clasifSeleccionada = clasificaciones.stream()
                    .filter(c -> c.getNombreClasificacion().equals(nombreClasificacion))
                    .findFirst().orElse(null);
            }

            int orden = servicioEncuestas.obtenerPreguntasDeEncuesta(encuestaActual.getIdEncuesta()).size() + 1;
            boolean esDescarte = dialogo.getChkEsDescarte().isSelected(); // Asumiendo que el diálogo tiene este getter
            String criterioDescarte = dialogo.getTxtCriterioDescarte().getText().trim(); // Asumiendo que el diálogo tiene este getter

            if (tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Tipo de pregunta inválido seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (servicioEncuestas.agregarPreguntaNuevaAEncuesta(encuestaActual.getIdEncuesta(),
                        textoPregunta, nombreTipo, nombreClasificacion, orden, esDescarte, criterioDescarte)) {
                    JOptionPane.showMessageDialog(this, "Pregunta única añadida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPreguntasEncuesta();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo añadir la pregunta única. Posiblemente la encuesta ya tiene 12 preguntas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al añadir pregunta única: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ConfigurarPreguntasEncuestaGUI al añadir única: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void modificarPreguntaEncuesta() {
        int selectedRow = tblPreguntasEncuesta.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una pregunta para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay una pregunta válida seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idDetalle = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            EncuestaDetallePregunta detalleAModificar = servicioEncuestas.obtenerPreguntaDetallePorId(idDetalle);
            if (detalleAModificar == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la pregunta para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean esDescarte = chkEsDescarte.isSelected();
            String criterioDescarte = txtCriterioDescarte.getText().trim();

            if (esDescarte && criterioDescarte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe especificar un criterio de descarte si la pregunta es de descarte.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!esDescarte) { // Si ya no es descarte, el criterio debe ser null
                criterioDescarte = null;
            }

            // Actualizar el objeto detalleAModificar
            detalleAModificar.setEsPreguntaDescarte(esDescarte);
            detalleAModificar.setCriterioDescarteValor(criterioDescarte);

            // CORRECCIÓN: Usar el nuevo método actualizarDetallePregunta que toma el objeto completo
            if (servicioEncuestas.actualizarDetallePregunta(detalleAModificar)) {
                JOptionPane.showMessageDialog(this, "Detalle de pregunta actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el detalle de la pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar detalle de pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al modificar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarPregunta() {
        int selectedRow = tblPreguntasEncuesta.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una pregunta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay una pregunta válida seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idDetalle = (int) tableModel.getValueAt(selectedRow, 0);
        String textoPregunta = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar la pregunta '" + textoPregunta + "' de la encuesta?\n" +
                "Esto no la eliminará del banco de preguntas.", "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (servicioEncuestas.eliminarPreguntaDeEncuestaServicio(idDetalle)) { // O usar eliminarPreguntaDeEncuesta(idEncuesta, idDetalle) si se necesita
                    JOptionPane.showMessageDialog(this, "Pregunta eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPreguntasEncuesta();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ConfigurarPreguntasEncuestaGUI al eliminar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void moverPregunta(boolean moverArriba) {
        int selectedRow = tblPreguntasEncuesta.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una pregunta para mover.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay una pregunta válida seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idDetalle = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            // CORRECCIÓN: Usar el nuevo método en ServicioEncuestas
            if (servicioEncuestas.moverPreguntaEnEncuesta(encuestaActual.getIdEncuesta(), idDetalle, moverArriba)) {
                JOptionPane.showMessageDialog(this, "Pregunta movida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta(); // Recargar para ver el nuevo orden
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo mover la pregunta. Asegúrate de que no sea el primer/último elemento.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al mover pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al mover: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void volverAGestionEncuestas() {
        this.dispose(); // Cierra esta ventana. El listener de windowClosed se encargará de mostrar el padre.
    }
}