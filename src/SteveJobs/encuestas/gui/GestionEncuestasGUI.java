package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Pantalla de Gestión de Encuestas para el Administrador (JFrame).
 * Permite al administrador crear, listar, modificar, eliminar, copiar y
 * cambiar el estado de las encuestas.
 * Integra EncuestaMetadatosGUI para la edición de metadatos.
 *
 * @author José Flores
 */
public class GestionEncuestasGUI extends JFrame {

    private Usuario administradorActual;
    private final AdminDashboardGUI parentDashboard; // Referencia al dashboard padre
    private final ServicioEncuestas servicioEncuestas;

    private JTable tblEncuestas;
    private DefaultTableModel tableModel;

    private JButton btnCrear, btnModificar, btnEliminar, btnConfigurarPreguntas;
    private JButton btnCambiarEstado, btnCopiarEncuesta, btnVolver;

    public GestionEncuestasGUI(Usuario admin, AdminDashboardGUI parent) {
        super("Sistema de Encuestas - Gestión de Encuestas");
        this.administradorActual = admin;
        this.parentDashboard = parent;
        this.servicioEncuestas = new ServicioEncuestas();
        initComponents();
        setupFrame();
        cargarEncuestas();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Gestión de Encuestas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de la tabla de encuestas ---
        // CAMBIADO: "Público" a "Es Pública" en los nombres de columna
        String[] columnNames = {"ID", "Nombre", "Descripción", "F. Inicio", "F. Fin", "Es Pública", "Perfil", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int intColumn) { // Cambiado 'column' a 'intColumn' para evitar conflicto
                return false; // Las celdas no son editables
            }
            // Opcional: Para que la columna "Es Pública" se renderice como checkbox visualmente en la tabla.
            // @Override
            // public Class<?> getColumnClass(int columnIndex) {
            //     if (columnIndex == 5) return Boolean.class; // Columna "Es Pública" (índice 5)
            //     return super.getColumnClass(columnIndex);
            // }
        };
        tblEncuestas = new JTable(tableModel);
        tblEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEncuestas.getTableHeader().setReorderingAllowed(false); // No permitir reordenar columnas
        JScrollPane scrollPane = new JScrollPane(tblEncuestas);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel de botones de acción ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2 filas, 4 columnas
        btnCrear = new JButton("Crear Nueva Encuesta");
        btnModificar = new JButton("Modificar Encuesta");
        btnConfigurarPreguntas = new JButton("Configurar Preguntas");
        btnCambiarEstado = new JButton("Cambiar Estado");
        btnEliminar = new JButton("Eliminar Encuesta");
        btnCopiarEncuesta = new JButton("Copiar Encuesta");
        btnVolver = new JButton("Volver al Dashboard");
        
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnConfigurarPreguntas);
        buttonPanel.add(btnCambiarEstado);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnCopiarEncuesta);
        buttonPanel.add(btnVolver); // Volver al final

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos de los botones ---
        btnCrear.addActionListener(e -> crearNuevaEncuesta());
        btnModificar.addActionListener(e -> modificarEncuesta());
        btnConfigurarPreguntas.addActionListener(e -> configurarPreguntasEncuesta());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoEncuesta());
        btnEliminar.addActionListener(e -> eliminarEncuesta());
        btnCopiarEncuesta.addActionListener(e -> copiarEncuesta());
        btnVolver.addActionListener(e -> volverAlDashboard());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700); // Tamaño grande para la gestión de encuestas
        setLocationRelativeTo(null);
        
        // Listener para cuando se cierra la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentDashboard.mostrarAdminDashboardGUI(); // Mostrar el dashboard padre
            }
        });
    }

    public void cargarEncuestas() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<Encuesta> encuestas = servicioEncuestas.obtenerTodasLasEncuestasOrdenadasPorNombre();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            if (encuestas.isEmpty()) {
                // CAMBIADO: Ajustado el número de columnas para que coincida con el nuevo tamaño
                tableModel.addRow(new Object[]{"", "", "No hay encuestas registradas.", "", "", "", "", ""});
                tblEncuestas.setEnabled(false);
                return;
            }

            for (Encuesta encuesta : encuestas) {
                tableModel.addRow(new Object[]{
                    encuesta.getIdEncuesta(),
                    encuesta.getNombre(),
                    encuesta.getDescripcion(),
                    encuesta.getFechaInicio() != null ? sdf.format(encuesta.getFechaInicio()) : "N/A",
                    encuesta.getFechaFin() != null ? sdf.format(encuesta.getFechaFin()) : "N/A",
                    encuesta.isEsPublica() ? "Sí" : "No", // CAMBIADO: Muestra "Sí" o "No"
                    encuesta.getPerfilRequerido() != null && !encuesta.getPerfilRequerido().isEmpty() ? encuesta.getPerfilRequerido() : "N/A", // Muestra N/A si es nulo o vacío
                    encuesta.getEstado()
                });
            }
            tblEncuestas.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las encuestas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en GestionEncuestasGUI al cargar encuestas: " + ex.getMessage());
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
            System.err.println("Error en GestionEncuestasGUI al obtener detalles: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    private void crearNuevaEncuesta() {
        // Pasa el ID del administrador actual a DialogoEncuestaMetadatos
        DialogoEncuestaMetadatos dialogo = new DialogoEncuestaMetadatos(this, "Crear Nueva Encuesta", null, administradorActual);
        dialogo.setVisible(true);

        if (dialogo.isGuardadoExitoso()) {
            JOptionPane.showMessageDialog(this, "Encuesta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarEncuestas(); // Recargar la tabla
        }
    }

    private void modificarEncuesta() {
        Encuesta encuestaSeleccionada = getSelectedEncuesta();
        if (encuestaSeleccionada == null) return;

        // Pasa el ID del administrador actual a DialogoEncuestaMetadatos
        DialogoEncuestaMetadatos dialogo = new DialogoEncuestaMetadatos(this, "Modificar Encuesta", encuestaSeleccionada, administradorActual);
        dialogo.setVisible(true);

        if (dialogo.isGuardadoExitoso()) {
            JOptionPane.showMessageDialog(this, "Encuesta modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarEncuestas(); // Recargar la tabla
        }
    }

    private void configurarPreguntasEncuesta() {
        Encuesta encuestaSeleccionada = getSelectedEncuesta();
        if (encuestaSeleccionada == null) return;

        ConfigurarPreguntasEncuestaGUI configurarPreguntasGUI = new ConfigurarPreguntasEncuestaGUI(encuestaSeleccionada, administradorActual, this);
        configurarPreguntasGUI.setVisible(true);
        this.setVisible(false); // Ocultar esta ventana
    }

    private void cambiarEstadoEncuesta() {
        Encuesta encuestaSeleccionada = getSelectedEncuesta();
        if (encuestaSeleccionada == null) return;

        String[] estados = {"BORRADOR", "ACTIVA", "CERRADA", "CANCELADA", "ARCHIVADA"};
        String estadoActual = encuestaSeleccionada.getEstado();
        String nuevoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el nuevo estado para la encuesta '" + encuestaSeleccionada.getNombre() + "':",
                "Cambiar Estado de Encuesta",
                JOptionPane.QUESTION_MESSAGE,
                null,
                estados,
                estadoActual
        );

        if (nuevoEstado != null && !nuevoEstado.equalsIgnoreCase(estadoActual)) {
            try {
                // La validación de 12 preguntas se realiza dentro de ServicioEncuestas.cambiarEstadoEncuesta
                servicioEncuestas.cambiarEstadoEncuesta(encuestaSeleccionada.getIdEncuesta(), nuevoEstado);
                JOptionPane.showMessageDialog(this, "Estado de la encuesta cambiado a: " + nuevoEstado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEncuestas(); // Recargar la tabla
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error de estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cambiar el estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en GestionEncuestasGUI al cambiar estado: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void eliminarEncuesta() {
        Encuesta encuestaSeleccionada = getSelectedEncuesta();
        if (encuestaSeleccionada == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar la encuesta '" + encuestaSeleccionada.getNombre() + "'?\n" +
                "Esto eliminará todas sus preguntas y respuestas asociadas.", "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                servicioEncuestas.eliminarEncuesta(encuestaSeleccionada.getIdEncuesta());
                JOptionPane.showMessageDialog(this, "Encuesta eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEncuestas(); // Recargar la tabla
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en GestionEncuestasGUI al eliminar encuesta: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private void copiarEncuesta() {
        Encuesta encuestaSeleccionada = getSelectedEncuesta();
        if (encuestaSeleccionada == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres copiar la encuesta '" + encuestaSeleccionada.getNombre() + "'?",
                "Confirmar Copia",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Pasa el id del administrador actual al método copiarEncuesta
                Encuesta nuevaEncuesta = servicioEncuestas.copiarEncuesta(encuestaSeleccionada.getIdEncuesta(), administradorActual.getId_usuario());
                if (nuevaEncuesta != null) {
                    JOptionPane.showMessageDialog(this, "Encuesta copiada exitosamente como: '" + nuevaEncuesta.getNombre() + "' (ID: " + nuevaEncuesta.getIdEncuesta() + ").", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEncuestas(); // Recargar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo copiar la encuesta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al copiar encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error en GestionEncuestasGUI al copiar encuesta: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void volverAlDashboard() {
        this.dispose(); // Cierra esta ventana. El listener se encargará de mostrar el padre.
    }
}