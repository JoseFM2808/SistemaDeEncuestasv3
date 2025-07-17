package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Pantalla de Configuración de Preguntas de una Encuesta específica para el Administrador (JFrame).
 * Permite asociar preguntas del banco, agregar preguntas únicas,
 * marcar/desmarcar como descarte, eliminar y buscar preguntas.
 *
 * @author José Flores
 */
public class ConfigurarPreguntasEncuestaGUI extends JFrame {

    private Encuesta encuestaActual;
    private Usuario administradorActual;
    private final GestionEncuestasGUI parentGestionEncuestas; // Referencia a la ventana padre
    private final ServicioEncuestas servicioEncuestas;

    private JTable tblPreguntasEncuesta;
    private DefaultTableModel tableModel;
    private JTextField txtBuscarOrden;

    private JButton btnAsociarBanco, btnAgregarUnica, btnMarcarDescarte, btnDesmarcarDescarte;
    private JButton btnEliminarPregunta, btnBuscarPregunta, btnVolver;

    public ConfigurarPreguntasEncuestaGUI(Encuesta encuesta, Usuario admin, GestionEncuestasGUI parent) {
        super("Configurar Preguntas para: " + encuesta.getNombre());
        this.encuestaActual = encuesta;
        this.administradorActual = admin;
        this.parentGestionEncuestas = parent;
        this.servicioEncuestas = new ServicioEncuestas();
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
        String[] columnNames = {"Orden", "Texto Pregunta", "Tipo", "Clasificación", "Es Descarte", "Criterio Descarte"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables
            }
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 4) return Boolean.class; // Columna "Es Descarte" es booleana
                return Object.class;
            }
        };
        tblPreguntasEncuesta = new JTable(tableModel);
        tblPreguntasEncuesta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPreguntasEncuesta.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tblPreguntasEncuesta);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel de botones de acción y búsqueda ---
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        
        // Botones de acción
        JPanel actionButtonPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2 filas, 4 columnas
        btnAsociarBanco = new JButton("Asociar del Banco");
        btnAgregarUnica = new JButton("Agregar Pregunta Única");
        btnMarcarDescarte = new JButton("Marcar Descarte");
        btnDesmarcarDescarte = new JButton("Desmarcar Descarte");
        btnEliminarPregunta = new JButton("Eliminar Pregunta");
        btnVolver = new JButton("Volver");

        actionButtonPanel.add(btnAsociarBanco);
        actionButtonPanel.add(btnAgregarUnica);
        actionButtonPanel.add(btnMarcarDescarte);
        actionButtonPanel.add(btnDesmarcarDescarte);
        actionButtonPanel.add(btnEliminarPregunta);
        actionButtonPanel.add(btnVolver);
        
        controlPanel.add(actionButtonPanel, BorderLayout.CENTER);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtBuscarOrden = new JTextField(10);
        btnBuscarPregunta = new JButton("Buscar por Orden");
        searchPanel.add(new JLabel("Orden:"));
        searchPanel.add(txtBuscarOrden);
        searchPanel.add(btnBuscarPregunta);
        
        controlPanel.add(searchPanel, BorderLayout.NORTH); // Poner búsqueda arriba de los botones de acción

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos de los botones ---
        btnAsociarBanco.addActionListener(e -> asociarPreguntaDelBanco());
        btnAgregarUnica.addActionListener(e -> agregarPreguntaUnica());
        btnMarcarDescarte.addActionListener(e -> marcarDescarte());
        btnDesmarcarDescarte.addActionListener(e -> desmarcarDescarte());
        btnEliminarPregunta.addActionListener(e -> eliminarPreguntaDeEncuesta());
        btnVolver.addActionListener(e -> volverAGestionEncuestas());
        btnBuscarPregunta.addActionListener(e -> buscarPreguntaPorOrden());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600); // Tamaño adecuado para la gestión de preguntas
        setLocationRelativeTo(null);
        
        // Listener para cuando se cierra la ventana, para mostrar el padre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentGestionEncuestas.mostrarGestionEncuestasGUI();
            }
        });
    }

    public void cargarPreguntasEncuesta() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<EncuestaDetallePregunta> preguntas = servicioEncuestas.obtenerPreguntasDeEncuesta(encuestaActual.getIdEncuesta());

            if (preguntas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "No hay preguntas configuradas para esta encuesta.", "", "", false, ""});
                tblPreguntasEncuesta.setEnabled(false);
                return;
            }

            for (EncuestaDetallePregunta detalle : preguntas) {
                String textoPregunta = (detalle.getPreguntaBanco() != null) ? 
                                       detalle.getPreguntaBanco().getTextoPregunta() : 
                                       detalle.getTextoPreguntaUnica();
                String tipoPregunta = (detalle.getTipoPregunta() != null) ? 
                                      detalle.getTipoPregunta().getNombreTipo() : "N/A";
                String clasificacionPregunta = (detalle.getClasificacionPregunta() != null) ? 
                                               detalle.getClasificacionPregunta().getNombreClasificacion() : "N/A";
                
                tableModel.addRow(new Object[]{
                    detalle.getOrdenEnEncuesta(),
                    textoPregunta,
                    tipoPregunta,
                    clasificacionPregunta,
                    detalle.isEsPreguntaDescarte(),
                    detalle.getCriterioDescarte() != null ? detalle.getCriterioDescarte() : ""
                });
            }
            tblPreguntasEncuesta.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las preguntas de la encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al cargar preguntas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private EncuestaDetallePregunta getSelectedPreguntaDetalle() {
        int selectedRow = tblPreguntasEncuesta.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una pregunta de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (tableModel.getValueAt(selectedRow, 0) == null || tableModel.getValueAt(selectedRow, 0).toString().isEmpty()) {
             JOptionPane.showMessageDialog(this, "No hay una pregunta válida seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
             return null;
        }
        
        int ordenEnEncuesta = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            // Esto es un poco ineficiente, lo ideal sería mantener una lista de objetos
            // EncuestaDetallePregunta en memoria y buscar por orden.
            // Por ahora, obtenemos toda la lista y buscamos.
            List<EncuestaDetallePregunta> preguntas = servicioEncuestas.obtenerPreguntasDeEncuesta(encuestaActual.getIdEncuesta());
            for (EncuestaDetallePregunta detalle : preguntas) {
                if (detalle.getOrdenEnEncuesta() == ordenEnEncuesta) {
                    return detalle;
                }
            }
            return null; // No debería llegar aquí si la fila fue seleccionada correctamente
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener detalle de pregunta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al obtener detalle: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    private void asociarPreguntaDelBanco() {
        DialogoSeleccionarPreguntaBanco dialogo = new DialogoSeleccionarPreguntaBanco(this);
        dialogo.setVisible(true);

        if (dialogo.getPreguntaSeleccionada() != null) {
            PreguntaBanco preguntaSeleccionada = dialogo.getPreguntaSeleccionada();
            // Pedir el orden para la pregunta
            String ordenStr = JOptionPane.showInputDialog(this, "Ingrese el orden para esta pregunta en la encuesta:", "Orden de Pregunta", JOptionPane.QUESTION_MESSAGE);
            if (ordenStr == null || ordenStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Orden no especificado. Operación cancelada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int orden = Integer.parseInt(ordenStr.trim());
                // Por defecto, una pregunta asociada del banco no es de descarte inicialmente
                servicioEncuestas.asociarPreguntaDelBancoAEncuesta(encuestaActual.getIdEncuesta(), preguntaSeleccionada.getIdPreguntaBanco(), orden);
                JOptionPane.showMessageDialog(this, "Pregunta del banco asociada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El orden debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                 JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al asociar pregunta del banco: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ConfigurarPreguntasEncuestaGUI al asociar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void agregarPreguntaUnica() {
        DialogoCrearPreguntaUnica dialogo = new DialogoCrearPreguntaUnica(this);
        dialogo.setVisible(true);

        if (dialogo.isPreguntaCreadaExitosa()) {
            String textoUnico = dialogo.getTextoPregunta();
            int tipoId = dialogo.getTipoPreguntaId();
            Integer clasificacionId = dialogo.getClasificacionPreguntaId(); // Puede ser null
             // Pedir el orden para la pregunta única
            String ordenStr = JOptionPane.showInputDialog(this, "Ingrese el orden para esta pregunta única en la encuesta:", "Orden de Pregunta", JOptionPane.QUESTION_MESSAGE);
            if (ordenStr == null || ordenStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Orden no especificado. Operación cancelada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int orden = Integer.parseInt(ordenStr.trim());
                // Por defecto, una pregunta única no es de descarte inicialmente
                servicioEncuestas.agregarPreguntaNuevaAEncuesta(encuestaActual.getIdEncuesta(), textoUnico, tipoId, clasificacionId, orden);
                JOptionPane.showMessageDialog(this, "Pregunta única agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta();
            } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(this, "El orden debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                 JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar pregunta única: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ConfigurarPreguntasEncuestaGUI al agregar única: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void marcarDescarte() {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;
        
        if (detalle.isEsPreguntaDescarte()) {
            JOptionPane.showMessageDialog(this, "La pregunta ya está marcada como descarte.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String criterio = JOptionPane.showInputDialog(this, "Ingrese el criterio de descarte para la pregunta:\n" + detalle.getTextoPreguntaMostrable(), "Criterio de Descarte", JOptionPane.QUESTION_MESSAGE);
        if (criterio == null || criterio.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Criterio no especificado. Operación cancelada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            servicioEncuestas.marcarPreguntaComoDescarte(encuestaActual.getIdEncuesta(), detalle.getOrdenEnEncuesta(), criterio);
            JOptionPane.showMessageDialog(this, "Pregunta marcada como descarte.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPreguntasEncuesta();
        } catch (IllegalArgumentException e) {
             JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al marcar como descarte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al marcar descarte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void desmarcarDescarte() {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;

        if (!detalle.isEsPreguntaDescarte()) {
            JOptionPane.showMessageDialog(this, "La pregunta no está marcada como descarte.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            servicioEncuestas.desmarcarPreguntaComoDescarte(encuestaActual.getIdEncuesta(), detalle.getOrdenEnEncuesta());
            JOptionPane.showMessageDialog(this, "Pregunta desmarcada como descarte.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPreguntasEncuesta();
        } catch (IllegalArgumentException e) {
             JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al desmarcar como descarte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en ConfigurarPreguntasEncuestaGUI al desmarcar descarte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarPreguntaDeEncuesta() {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar la pregunta '" + detalle.getTextoPreguntaMostrable() + "' de esta encuesta?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                servicioEncuestas.eliminarPreguntaDeEncuestaServicio(encuestaActual.getIdEncuesta(), detalle.getOrdenEnEncuesta());
                JOptionPane.showMessageDialog(this, "Pregunta eliminada exitosamente de la encuesta.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta();
            } catch (IllegalArgumentException e) {
                 JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar pregunta de encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en ConfigurarPreguntasEncuestaGUI al eliminar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void buscarPreguntaPorOrden() {
        String ordenStr = txtBuscarOrden.getText().trim();
        if (ordenStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de orden para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            cargarPreguntasEncuesta(); // Recarga todas si el campo está vacío
            return;
        }
        try {
            int ordenBuscado = Integer.parseInt(ordenStr);
            // Implementación de búsqueda secuencial/lineal para destacar el elemento
            // Se asume que la tabla no es excesivamente grande
            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0) instanceof Integer && (Integer) tableModel.getValueAt(i, 0) == ordenBuscado) {
                    tblPreguntasEncuesta.setRowSelectionInterval(i, i);
                    tblPreguntasEncuesta.scrollRectToVisible(tblPreguntasEncuesta.getCellRect(i, 0, true));
                    found = true;
                    JOptionPane.showMessageDialog(this, "Pregunta encontrada en orden " + ordenBuscado + ".", "Búsqueda Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna pregunta con el orden " + ordenBuscado + ".", "No Encontrado", JOptionPane.INFORMATION_MESSAGE);
                tblPreguntasEncuesta.clearSelection(); // Limpiar selección si no se encontró
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El orden debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAGestionEncuestas() {
        this.dispose(); // Cierra esta ventana. El listener se encargará de mostrar el padre.
    }
}