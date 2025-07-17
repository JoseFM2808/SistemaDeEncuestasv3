package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import com.toedter.calendar.JDateChooser; // Importar JDateChooser
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Componente JPanel para mostrar y editar los metadatos de una encuesta.
 * Utilizado dentro de diálogos o frames de gestión de encuestas.
 *
 * @author José Flores (Adaptado para JCalendar)
 */
public class EncuestaMetadatosGUI extends JPanel {

    private JTextField txtNombre, txtPerfilRequerido;
    private JTextArea txtDescripcion;
    private JDateChooser dateChooserFechaInicio, dateChooserFechaFin; // JDateChooser para fechas
    private JCheckBox chkEsPublica; // CAMBIADO: De JTextField a JCheckBox
    private JComboBox<String> cmbEstado; // Para el estado de la encuesta

    public EncuestaMetadatosGUI() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del panel
        JLabel lblPanelTitulo = new JLabel("Detalles de la Encuesta", SwingConstants.CENTER);
        lblPanelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        add(lblPanelTitulo, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(25);
        add(txtNombre, gbc);

        // Descripción
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextArea(4, 25);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        add(scrollDescripcion, gbc);

        // Fecha de Inicio
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1;
        dateChooserFechaInicio = new JDateChooser();
        dateChooserFechaInicio.setDateFormatString("yyyy-MM-dd");
        add(dateChooserFechaInicio, gbc);

        // Fecha de Fin
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fecha Fin:"), gbc);
        gbc.gridx = 1;
        dateChooserFechaFin = new JDateChooser();
        dateChooserFechaFin.setDateFormatString("yyyy-MM-dd");
        add(dateChooserFechaFin, gbc);

        // Es Pública (antes Público Objetivo)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Es Pública:"), gbc); // CAMBIADO: Texto del Label
        gbc.gridx = 1;
        chkEsPublica = new JCheckBox(); // CAMBIADO: JCheckBox en lugar de JTextField
        add(chkEsPublica, gbc);

        // Perfil Requerido
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Perfil Requerido:"), gbc);
        gbc.gridx = 1;
        txtPerfilRequerido = new JTextField(25);
        add(txtPerfilRequerido, gbc);
        
        // Estado (para modificación, en creación se establece por defecto "BORRADOR")
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        cmbEstado = new JComboBox<>(new String[]{"BORRADOR", "ACTIVA", "CERRADA", "CANCELADA", "ARCHIVADA"});
        add(cmbEstado, gbc);
    }

    // --- Métodos para cargar/obtener datos ---
    public void cargarDatosEncuesta(Encuesta encuesta) {
        if (encuesta != null) {
            txtNombre.setText(encuesta.getNombre());
            txtDescripcion.setText(encuesta.getDescripcion());
            dateChooserFechaInicio.setDate(encuesta.getFechaInicio());
            dateChooserFechaFin.setDate(encuesta.getFechaFin());
            chkEsPublica.setSelected(encuesta.isEsPublica()); // CAMBIADO: usa setSelected
            txtPerfilRequerido.setText(encuesta.getPerfilRequerido());
            cmbEstado.setSelectedItem(encuesta.getEstado());
            // Si la encuesta es para modificar, habilitar el cambio de estado
            cmbEstado.setEnabled(true);
            // Habilitar/deshabilitar txtPerfilRequerido basado en chkEsPublica
            txtPerfilRequerido.setEnabled(!chkEsPublica.isSelected());
        } else {
            // Limpiar campos si no hay encuesta para cargar
            limpiarCampos();
            cmbEstado.setSelectedItem("BORRADOR"); // Por defecto en creación
            cmbEstado.setEnabled(false); // No se permite cambiar el estado en creación inicial
            txtPerfilRequerido.setEnabled(true); // En creación, por defecto podría ser perfilado o se activará/desactivará con el checkbox
        }
        // Listener para el JCheckBox: habilita/deshabilita txtPerfilRequerido
        chkEsPublica.addActionListener(e -> txtPerfilRequerido.setEnabled(!chkEsPublica.isSelected()));
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        dateChooserFechaInicio.setDate(null);
        dateChooserFechaFin.setDate(null);
        chkEsPublica.setSelected(false); // CAMBIADO: por defecto no pública
        txtPerfilRequerido.setText("");
        cmbEstado.setSelectedItem("BORRADOR");
    }

    // --- Getters para acceder a los componentes desde fuera ---
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextArea getTxtDescripcion() { return txtDescripcion; }
    public JDateChooser getDateChooserFechaInicio() { return dateChooserFechaInicio; }
    public JDateChooser getDateChooserFechaFin() { return dateChooserFechaFin; }
    public boolean isEsPublicaSelected() { return chkEsPublica.isSelected(); } // CAMBIADO: Nuevo getter para el JCheckBox
    public JTextField getTxtPerfilRequerido() { return txtPerfilRequerido; }
    public JComboBox<String> getCmbEstado() { return cmbEstado; }
}