package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.servicio.ServicioPreguntas; // Mantener si se usa para otras cosas, pero no para obtener tipos/clasificaciones
import SteveJobs.encuestas.dao.TipoPreguntaDAO; // Importar
import SteveJobs.encuestas.dao.ClasificacionPreguntaDAO; // Importar

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DialogoCrearPreguntaUnica extends JDialog {

    private final TipoPreguntaDAO tipoPreguntaDAO; // Instancia de TipoPreguntaDAO
    private final ClasificacionPreguntaDAO clasificacionPreguntaDAO; // Instancia de ClasificacionPreguntaDAO
    // private ServicioPreguntas servicioPreguntas; // No se usa directamente para obtener tipos/clasificaciones aquí

    private JTextField txtTextoPregunta;
    private JComboBox<String> cmbTipoPregunta;
    private JComboBox<String> cmbClasificacion;
    private JButton btnAceptar, btnCancelar;

    private String textoPreguntaUnica;
    private Integer idTipoPreguntaUnica;
    private Integer idClasificacionUnica;
    private boolean creacionExitosa = false;

    public DialogoCrearPreguntaUnica(Frame owner) {
        super(owner, "Crear Pregunta Única para Encuesta", true);
        this.tipoPreguntaDAO = new TipoPreguntaDAO(); // Inicializar
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // Inicializar
        // this.servicioPreguntas = new ServicioPreguntas(); // Si no se usa, se puede eliminar

        initComponents();
        setupDialog();
        cargarTiposYClasificaciones();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Texto de la pregunta
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Texto de la Pregunta:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtTextoPregunta = new JTextField(30);
        panel.add(txtTextoPregunta, gbc);

        // Tipo de Pregunta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Tipo de Pregunta:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        cmbTipoPregunta = new JComboBox<>();
        panel.add(cmbTipoPregunta, gbc);

        // Clasificación
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Clasificación (Opcional):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        cmbClasificacion = new JComboBox<>();
        panel.add(cmbClasificacion, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        add(panel);

        btnAceptar.addActionListener(e -> onAceptar());
        btnCancelar.addActionListener(e -> onCancelar());
    }

    private void setupDialog() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void cargarTiposYClasificaciones() {
        // Cargar tipos de pregunta
        List<TipoPregunta> tipos = tipoPreguntaDAO.obtenerTodosLosTiposPregunta();
        for (TipoPregunta tp : tipos) {
            cmbTipoPregunta.addItem(tp.getNombreTipo());
        }

        // Cargar clasificaciones, incluyendo la opción "Ninguna"
        List<ClasificacionPregunta> clasificaciones = clasificacionPreguntaDAO.obtenerTodasLasClasificaciones(); // CORRECCIÓN
        cmbClasificacion.addItem("Ninguna"); // Opción por defecto para no asignar clasificación
        for (ClasificacionPregunta cp : clasificaciones) {
            cmbClasificacion.addItem(cp.getNombreClasificacion());
        }
    }

    private void onAceptar() {
        String texto = txtTextoPregunta.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El texto de la pregunta no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipoNombre = (String) cmbTipoPregunta.getSelectedItem();
        TipoPregunta tipoSeleccionado = tipoPreguntaDAO.obtenerTipoPreguntaPorNombre(tipoNombre); // CORRECCIÓN
        if (tipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de pregunta válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        idTipoPreguntaUnica = tipoSeleccionado.getIdTipoPregunta();

        String clasificacionNombre = (String) cmbClasificacion.getSelectedItem();
        if ("Ninguna".equals(clasificacionNombre)) {
            idClasificacionUnica = null; // No asignar clasificación
        } else {
            ClasificacionPregunta clasificacionSeleccionada = clasificacionPreguntaDAO.obtenerClasificacionPorNombre(clasificacionNombre); // CORRECCIÓN
            if (clasificacionSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una clasificación válida o 'Ninguna'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            idClasificacionUnica = clasificacionSeleccionada.getIdClasificacion(); // CORRECCIÓN: getIdClasificacion()
        }

        textoPreguntaUnica = texto;
        creacionExitosa = true;
        dispose();
    }

    private void onCancelar() {
        creacionExitosa = false;
        dispose();
    }

    public boolean isCreacionExitosa() {
        return creacionExitosa;
    }

    public String getTextoPreguntaUnica() {
        return textoPreguntaUnica;
    }

    public Integer getIdTipoPreguntaUnica() {
        return idTipoPreguntaUnica;
    }

    public Integer getIdClasificacionUnica() {
        return idClasificacionUnica;
    }
}