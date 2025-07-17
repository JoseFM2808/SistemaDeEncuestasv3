package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.PreguntaBanco;
import SteveJobs.encuestas.modelo.TipoPregunta; // Importar
import SteveJobs.encuestas.modelo.ClasificacionPregunta; // Importar
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioPreguntas; // Necesario para obtener PreguntasBanco

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects; // Para Objects.requireNonNullElse

/**
 * Pantalla para configurar las preguntas de una encuesta específica.
 * Permite añadir preguntas del banco o preguntas únicas, modificar su orden,
 * y marcarlas como preguntas de descarte.
 *
 * @author José Flores
 */
public class ConfigurarPreguntasEncuestaGUI extends JFrame {

    private final Encuesta encuestaActual;
    private final Usuario administradorActual;
    private final GestionEncuestasGUI parentGestionEncuestas;
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioPreguntas servicioPreguntas; // Instancia de servicio de preguntas

    private JTable tblPreguntasEncuesta;
    private DefaultTableModel tableModel;

    private JButton btnAddBanco, btnAddUnica, btnModificar, btnEliminar;
    private JButton btnMoverArriba, btnMoverAbajo, btnMarcarDescarte, btnVolver;

    public ConfigurarPreguntasEncuestaGUI(Encuesta encuesta, Usuario admin, GestionEncuestasGUI parent) {
        super("Configurar Preguntas para Encuesta: " + encuesta.getNombre());
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

        JLabel lblTitulo = new JLabel("Configurar Preguntas - " + encuestaActual.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de la tabla de preguntas ---
        String[] columnNames = {"Orden", "ID Detalle", "Texto Pregunta", "Tipo", "Clasificación", "Descarte?", "Criterio"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPreguntasEncuesta = new JTable(tableModel);
        tblPreguntasEncuesta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblPreguntasEncuesta);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel de botones de acción ---
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10)); // Más filas para botones
        btnAddBanco = new JButton("Añadir del Banco");
        btnAddUnica = new JButton("Añadir Pregunta Única");
        btnModificar = new JButton("Modificar Orden/Descarte");
        btnEliminar = new JButton("Eliminar Pregunta");
        btnMoverArriba = new JButton("Mover Arriba");
        btnMoverAbajo = new JButton("Mover Abajo");
        btnMarcarDescarte = new JButton("Marcar/Desmarcar Descarte");
        btnVolver = new JButton("Volver a Gestión de Encuestas");
        
        buttonPanel.add(btnAddBanco);
        buttonPanel.add(btnAddUnica);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnMoverArriba);
        buttonPanel.add(btnMoverAbajo);
        buttonPanel.add(btnMarcarDescarte);
        buttonPanel.add(btnVolver); // Siempre al final

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos de los botones ---
        btnAddBanco.addActionListener(e -> agregarPreguntaDelBanco());
        btnAddUnica.addActionListener(e -> agregarPreguntaUnica());
        btnModificar.addActionListener(e -> modificarPreguntaEncuesta());
        btnEliminar.addActionListener(e -> eliminarPreguntaEncuesta());
        btnMoverArriba.addActionListener(e -> moverPregunta(true));
        btnMoverAbajo.addActionListener(e -> moverPregunta(false));
        btnMarcarDescarte.addActionListener(e -> marcarDesmarcarDescarte());
        btnVolver.addActionListener(e -> volverAGestionEncuestas());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentGestionEncuestas.setVisible(true); // CORRECCIÓN: Mostrar la ventana padre
                parentGestionEncuestas.cargarEncuestas(); // Recargar la tabla de encuestas padre
            }
        });
    }

    private void cargarPreguntasEncuesta() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            // Se asume que encuestaActual.getPreguntasAsociadas() ya trae los detalles completos
            // Si no, necesitaríamos llamar a servicioEncuestas.obtenerDetallesCompletosEncuesta(idEncuestaActual);
            List<EncuestaDetallePregunta> preguntas = servicioEncuestas.obtenerDetallesCompletosEncuesta(encuestaActual.getIdEncuesta()).getPreguntasAsociadas();

            if (preguntas.isEmpty()) {
                tableModel.addRow(new Object[]{"", "", "No hay preguntas configuradas para esta encuesta.", "", "", "", ""});
                tblPreguntasEncuesta.setEnabled(false);
                return;
            }

            for (EncuestaDetallePregunta detalle : preguntas) {
                String textoPregunta = detalle.getTextoPreguntaMostrable(); // CORRECCIÓN: Usar el método de conveniencia
                String tipoPregunta = detalle.getNombreTipoPregunta(); // CORRECCIÓN: Usar el método de conveniencia
                String clasificacionPregunta = Objects.requireNonNullElse(detalle.getNombreClasificacionPregunta(), "N/A"); // CORRECCIÓN: Usar método de conveniencia
                
                tableModel.addRow(new Object[]{
                    detalle.getOrdenEnEncuesta(),
                    detalle.getIdEncuestaDetalle(),
                    textoPregunta,
                    tipoPregunta,
                    clasificacionPregunta,
                    detalle.isEsPreguntaDescarte() ? "Sí" : "No",
                    Objects.requireNonNullElse(detalle.getCriterioDescarteValor(), "N/A") // CORRECCIÓN: Usar el getter correcto
                });
            }
            tblPreguntasEncuesta.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las preguntas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        // Asumiendo que la columna 1 contiene el idEncuestaDetalle
        int idEncuestaDetalle = (int) tableModel.getValueAt(selectedRow, 1);
        return servicioEncuestas.obtenerDetallePreguntaPorId(idEncuestaDetalle);
    }
    
    private void agregarPreguntaDelBanco() {
        // Asumiendo que tienes un DialogoSeleccionarPreguntaBanco para esto
        DialogoSeleccionarPreguntaBanco dialogo = new DialogoSeleccionarPreguntaBanco(this);
        dialogo.setVisible(true);

        if (dialogo.isSeleccionExitosa()) {
            PreguntaBanco preguntaSeleccionada = dialogo.getPreguntaSeleccionada();
            String ordenStr = JOptionPane.showInputDialog(this, "Ingrese el orden de la pregunta:", "Orden de Pregunta", JOptionPane.PLAIN_MESSAGE);
            if (ordenStr == null || ordenStr.trim().isEmpty()) return;
            try {
                int orden = Integer.parseInt(ordenStr);
                boolean esDescarte = JOptionPane.showConfirmDialog(this, "¿Es pregunta de descarte?", "Descarte", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                String criterio = esDescarte ? JOptionPane.showInputDialog(this, "Ingrese el criterio de descarte:", "Criterio", JOptionPane.PLAIN_MESSAGE) : null;

                // CORRECCIÓN: Pasar todos los argumentos requeridos por el servicio
                boolean agregado = servicioEncuestas.asociarPreguntaDelBancoAEncuesta(
                    encuestaActual.getIdEncuesta(),
                    preguntaSeleccionada.getIdPreguntaBanco(),
                    orden,
                    esDescarte,
                    criterio
                );
                if (agregado) {
                    JOptionPane.showMessageDialog(this, "Pregunta del banco añadida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPreguntasEncuesta();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al añadir pregunta del banco.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Orden inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarPreguntaUnica() {
        DialogoCrearPreguntaUnica dialogo = new DialogoCrearPreguntaUnica(this);
        dialogo.setVisible(true);

        if (dialogo.isCreacionExitosa()) {
            String textoUnico = dialogo.getTextoPreguntaUnica();
            Integer idTipoUnico = dialogo.getIdTipoPreguntaUnica();
            Integer idClasificacionUnica = dialogo.getIdClasificacionUnica(); // Obtener el ID de clasificación
            String ordenStr = JOptionPane.showInputDialog(this, "Ingrese el orden de la pregunta:", "Orden de Pregunta", JOptionPane.PLAIN_MESSAGE);
            if (ordenStr == null || ordenStr.trim().isEmpty()) return;
            try {
                int orden = Integer.parseInt(ordenStr);
                boolean esDescarte = JOptionPane.showConfirmDialog(this, "¿Es pregunta de descarte?", "Descarte", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                String criterio = esDescarte ? JOptionPane.showInputDialog(this, "Ingrese el criterio de descarte:", "Criterio", JOptionPane.PLAIN_MESSAGE) : null;

                // CORRECCIÓN: Pasar todos los argumentos requeridos por el servicio
                boolean agregado = servicioEncuestas.agregarPreguntaNuevaAEncuesta(
                    encuestaActual.getIdEncuesta(),
                    textoUnico,
                    idTipoUnico,
                    idClasificacionUnica, // Pasar el Integer
                    orden,
                    esDescarte,
                    criterio
                );
                if (agregado) {
                    JOptionPane.showMessageDialog(this, "Pregunta única añadida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPreguntasEncuesta();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al añadir pregunta única.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Orden inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificarPreguntaEncuesta() {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;

        // Preguntar si desea cambiar el orden
        String nuevoOrdenStr = JOptionPane.showInputDialog(this, "Nuevo orden (actual: " + detalle.getOrdenEnEncuesta() + "):", "Modificar Orden", JOptionPane.PLAIN_MESSAGE);
        int nuevoOrden = detalle.getOrdenEnEncuesta(); // Mantener el actual por defecto
        if (nuevoOrdenStr != null && !nuevoOrdenStr.trim().isEmpty()) {
            try {
                nuevoOrden = Integer.parseInt(nuevoOrdenStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Orden inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Preguntar si desea cambiar el criterio de descarte
        boolean esDescarteActual = detalle.isEsPreguntaDescarte();
        String criterioActual = Objects.requireNonNullElse(detalle.getCriterioDescarteValor(), "");
        int opcionDescarte = JOptionPane.showConfirmDialog(this, 
                "¿Es pregunta de descarte? (actual: " + (esDescarteActual ? "Sí" : "No") + ")", 
                "Modificar Descarte", JOptionPane.YES_NO_CANCEL_OPTION);

        boolean nuevoEsDescarte;
        String nuevoCriterio = null;

        if (opcionDescarte == JOptionPane.YES_OPTION) {
            nuevoEsDescarte = true;
            nuevoCriterio = JOptionPane.showInputDialog(this, "Ingrese el nuevo criterio de descarte (actual: " + criterioActual + "):", "Criterio de Descarte", JOptionPane.PLAIN_MESSAGE);
            if (nuevoCriterio == null || nuevoCriterio.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El criterio de descarte no puede estar vacío si es de descarte.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (opcionDescarte == JOptionPane.NO_OPTION) {
            nuevoEsDescarte = false;
            nuevoCriterio = null; // No hay criterio si no es de descarte
        } else { // Cancelar
            return;
        }

        // Llamar al servicio para actualizar el detalle
        boolean actualizado = servicioEncuestas.actualizarDetallePregunta(
            detalle.getIdEncuestaDetalle(),
            nuevoOrden,
            nuevoEsDescarte,
            nuevoCriterio
        );

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Detalle de pregunta actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPreguntasEncuesta();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el detalle de pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void eliminarPreguntaEncuesta() {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar la pregunta '" + detalle.getTextoPreguntaMostrable() + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // CORRECCIÓN: Pasar solo el ID del detalle de pregunta
            boolean eliminado = servicioEncuestas.eliminarPreguntaDeEncuestaServicio(detalle.getIdEncuestaDetalle()); 
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Pregunta eliminada exitosamente de la encuesta.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moverPregunta(boolean arriba) {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;

        boolean movido = servicioEncuestas.moverPreguntaEnEncuesta(encuestaActual.getIdEncuesta(), detalle.getIdEncuestaDetalle(), arriba);
        if (movido) {
            JOptionPane.showMessageDialog(this, "Pregunta movida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPreguntasEncuesta();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo mover la pregunta (ya está en el límite o error).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marcarDesmarcarDescarte() {
        EncuestaDetallePregunta detalle = getSelectedPreguntaDetalle();
        if (detalle == null) return;

        boolean actualmenteEsDescarte = detalle.isEsPreguntaDescarte();
        String criterioActual = Objects.requireNonNullElse(detalle.getCriterioDescarteValor(), "");

        if (actualmenteEsDescarte) {
            // Si actualmente es de descarte, ofrecer desmarcarla
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Esta pregunta es de descarte con criterio '" + criterioActual + "'. ¿Desea desmarcarla como descarte?", 
                    "Desmarcar Descarte", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // CORRECCIÓN: Pasar solo el ID del detalle de pregunta
                boolean desmarcado = servicioEncuestas.desmarcarPreguntaComoDescarte(detalle.getIdEncuestaDetalle()); 
                if (desmarcado) {
                    JOptionPane.showMessageDialog(this, "Pregunta desmarcada como descarte.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPreguntasEncuesta();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al desmarcar como descarte.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // Si no es de descarte, ofrecer marcarla
            String criterio = JOptionPane.showInputDialog(this, 
                    "Ingrese el criterio de descarte para '" + detalle.getTextoPreguntaMostrable() + "':", 
                    "Marcar como Descarte", JOptionPane.PLAIN_MESSAGE);
            if (criterio == null || criterio.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El criterio de descarte no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // CORRECCIÓN: Pasar solo el ID del detalle de pregunta y el criterio
            boolean marcado = servicioEncuestas.marcarPreguntaComoDescarte(detalle.getIdEncuestaDetalle(), criterio.trim()); 
            if (marcado) {
                JOptionPane.showMessageDialog(this, "Pregunta marcada como descarte.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPreguntasEncuesta();
            } else {
                JOptionPane.showMessageDialog(this, "Error al marcar como descarte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void volverAGestionEncuestas() {
        this.dispose(); // Cierra esta ventana, el WindowListener se encarga de mostrar la padre
    }
}