package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.ClasificacionPregunta;
import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.servicio.ServicioPreguntas; // Para obtener tipos y clasificaciones
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Diálogo modal para crear una nueva pregunta única para una encuesta.
 * Permite definir el texto, tipo y clasificación de la pregunta.
 *
 * @author José Flores
 */
public class DialogoCrearPreguntaUnica extends JDialog {

    private JTextArea txtTextoPregunta;
    private JComboBox<String> cmbTipoPregunta;
    private JComboBox<String> cmbClasificacionPregunta;
    private JLabel lblMensaje;

    private boolean preguntaCreadaExitosa = false;
    private String textoPregunta;
    private int tipoPreguntaId;
    private Integer clasificacionPreguntaId; // Puede ser null

    private final ServicioPreguntas servicioPreguntas;

    public DialogoCrearPreguntaUnica(JFrame parent) {
        super(parent, "Crear Pregunta Única", true); // true para modal
        servicioPreguntas = new ServicioPreguntas();
        initComponents();
        setupDialog();
        cargarCombos();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Nueva Pregunta Única", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Texto de la Pregunta
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Texto de la Pregunta:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtTextoPregunta = new JTextArea(4, 30);
        txtTextoPregunta.setLineWrap(true);
        txtTextoPregunta.setWrapStyleWord(true);
        JScrollPane scrollTextoPregunta = new JScrollPane(txtTextoPregunta);
        formPanel.add(scrollTextoPregunta, gbc);

        // Tipo de Pregunta
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Tipo de Pregunta:"), gbc);
        gbc.gridx = 1;
        cmbTipoPregunta = new JComboBox<>();
        formPanel.add(cmbTipoPregunta, gbc);

        // Clasificación de Pregunta
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Clasificación:"), gbc);
        gbc.gridx = 1;
        cmbClasificacionPregunta = new JComboBox<>();
        formPanel.add(cmbClasificacionPregunta, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        formPanel.add(lblMensaje, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCrear = new JButton("Crear Pregunta");
        JButton btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnCancelar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos ---
        btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPregunta();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preguntaCreadaExitosa = false;
                dispose();
            }
        });
    }

    private void setupDialog() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void cargarCombos() {
        // Cargar Tipos de Pregunta
        try {
            List<TipoPregunta> tipos = servicioPreguntas.obtenerTodosLosTiposPregunta();
            cmbTipoPregunta.addItem(""); // Opción vacía
            for (TipoPregunta tipo : tipos) {
                cmbTipoPregunta.addItem(tipo.getNombreTipo());
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error al cargar tipos de pregunta: " + ex.getMessage());
            System.err.println("Error en DialogoCrearPreguntaUnica al cargar tipos: " + ex.getMessage());
        }

        // Cargar Clasificaciones de Pregunta
        try {
            List<ClasificacionPregunta> clasificaciones = servicioPreguntas.obtenerTodasLasClasificacionesPregunta();
            cmbClasificacionPregunta.addItem(""); // Opción vacía
            for (ClasificacionPregunta clasificacion : clasificaciones) {
                cmbClasificacionPregunta.addItem(clasificacion.getNombreClasificacion());
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error al cargar clasificaciones: " + ex.getMessage());
            System.err.println("Error en DialogoCrearPreguntaUnica al cargar clasificaciones: " + ex.getMessage());
        }
    }

    private void crearPregunta() {
        textoPregunta = txtTextoPregunta.getText().trim();
        String tipoSeleccionado = (String) cmbTipoPregunta.getSelectedItem();
        String clasificacionSeleccionada = (String) cmbClasificacionPregunta.getSelectedItem();

        if (textoPregunta.isEmpty() || tipoSeleccionado == null || tipoSeleccionado.isEmpty()) {
            lblMensaje.setText("Texto y Tipo de Pregunta son obligatorios.");
            return;
        }

        try {
            TipoPregunta tipo = servicioPreguntas.obtenerTipoPreguntaPorNombre(tipoSeleccionado);
            if (tipo == null) {
                lblMensaje.setText("Tipo de pregunta no encontrado.");
                return;
            }
            tipoPreguntaId = tipo.getIdTipoPregunta();

            if (clasificacionSeleccionada != null && !clasificacionSeleccionada.isEmpty()) {
                ClasificacionPregunta clasificacion = servicioPreguntas.obtenerClasificacionPreguntaPorNombre(clasificacionSeleccionada);
                if (clasificacion == null) {
                    lblMensaje.setText("Clasificación no encontrada.");
                    return;
                }
                clasificacionPreguntaId = clasificacion.getIdClasificacionPregunta();
            } else {
                clasificacionPreguntaId = null; // No hay clasificación seleccionada
            }

            preguntaCreadaExitosa = true;
            dispose(); // Cierra el diálogo
        } catch (Exception ex) {
            lblMensaje.setText("Error al procesar la pregunta: " + ex.getMessage());
            System.err.println("Error en DialogoCrearPreguntaUnica al procesar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean isPreguntaCreadaExitosa() { return preguntaCreadaExitosa; }
    public String getTextoPregunta() { return textoPregunta; }
    public int getTipoPreguntaId() { return tipoPreguntaId; }
    public Integer getClasificacionPreguntaId() { return clasificacionPreguntaId; } // Puede devolver null
}