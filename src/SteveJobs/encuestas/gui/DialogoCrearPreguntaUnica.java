// Archivo: josefm2808/sistemadeencuestasv3/SistemaDeEncuestasv3-b73347d68ca8a40e851f3439418b915b5f3ce710/src/SteveJobs/encuestas/gui/DialogoCrearPreguntaUnica.java
package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.TipoPregunta;
import SteveJobs.encuestas.modelo.ClasificacionPregunta;
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
    private JCheckBox chkEsDescarte; // Añadido
    private JTextField txtCriterioDescarte; // Añadido
    private JButton btnAceptar, btnCancelar;

    private boolean creacionExitosa = false;

    public DialogoCrearPreguntaUnica(Frame owner) {
        super(owner, "Crear Pregunta Única para Encuesta", true);
        this.tipoPreguntaDAO = new TipoPreguntaDAO(); // Inicializar
        this.clasificacionPreguntaDAO = new ClasificacionPreguntaDAO(); // Inicializar

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

        // Es pregunta de descarte
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        chkEsDescarte = new JCheckBox("Es Pregunta de Descarte"); // Añadido
        panel.add(chkEsDescarte, gbc);

        // Criterio de descarte
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        txtCriterioDescarte = new JTextField(15); // Añadido
        txtCriterioDescarte.setToolTipText("Valor que descalifica (ej: 'No', '2', 'Madrid')");
        txtCriterioDescarte.setEnabled(false); // Deshabilitado por defecto
        panel.add(txtCriterioDescarte, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4; // Cambiado a gridy 4 por los nuevos componentes
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        add(panel);

        // Listeners
        btnAceptar.addActionListener(e -> onAceptar());
        btnCancelar.addActionListener(e -> onCancelar());
        chkEsDescarte.addActionListener(e -> txtCriterioDescarte.setEnabled(chkEsDescarte.isSelected())); // Listener para habilitar/deshabilitar
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

        if (chkEsDescarte.isSelected() && txtCriterioDescarte.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe especificar un criterio de descarte si la pregunta es de descarte.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        creacionExitosa = true;
        dispose();
    }

    private void onCancelar() {
        creacionExitosa = false;
        dispose();
    }

    public boolean isGuardadoExitoso() { // Renombrado de isCreacionExitosa para coincidir con el uso
        return creacionExitosa;
    }

    // Métodos getter para acceder a los componentes de entrada
    public JTextField getTxtTextoPregunta() {
        return txtTextoPregunta;
    }

    public JComboBox<String> getCmbTipoPregunta() {
        return cmbTipoPregunta;
    }

    public JComboBox<String> getCmbClasificacion() {
        return cmbClasificacion;
    }

    public JCheckBox getChkEsDescarte() { // Añadido
        return chkEsDescarte;
    }

    public JTextField getTxtCriterioDescarte() { // Añadido
        return txtCriterioDescarte;
    }
}