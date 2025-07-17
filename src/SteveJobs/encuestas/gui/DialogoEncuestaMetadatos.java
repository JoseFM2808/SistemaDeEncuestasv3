package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.sql.Timestamp; // Importar java.sql.Timestamp

/**
 * Diálogo modal para la creación o modificación de metadatos de una encuesta.
 * Utiliza EncuestaMetadatosGUI como su panel interno.
 *
 * @author José Flores
 */
public class DialogoEncuestaMetadatos extends JDialog {

    private EncuestaMetadatosGUI panelMetadatos;
    private JButton btnGuardar, btnCancelar;
    private boolean guardadoExitoso = false;
    private Encuesta encuestaParaEditar; // Null si es para crear
    private Usuario administradorCreador; // Administrador logueado
    private ServicioEncuestas servicioEncuestas;

    public DialogoEncuestaMetadatos(JFrame parent, String title, Encuesta encuesta, Usuario admin) {
        super(parent, title, true); // true para modal
        this.encuestaParaEditar = encuesta;
        this.administradorCreador = admin;
        this.servicioEncuestas = new ServicioEncuestas();
        initComponents();
        setupDialog();
        // Cargar datos si es para edición
        if (encuestaParaEditar != null) {
            panelMetadatos.cargarDatosEncuesta(encuestaParaEditar);
        } else {
            // Asegurarse de que el txtPerfilRequerido se habilite/deshabilite correctamente
            // en base al estado inicial del checkbox cuando es una nueva encuesta.
            // Por defecto, chkEsPublica es false (no pública) al iniciar una nueva encuesta,
            // por lo tanto, el campo de perfil debe estar habilitado.
            panelMetadatos.getTxtPerfilRequerido().setEnabled(!panelMetadatos.isEsPublicaSelected());
        }
    }

    private void initComponents() {
        // EncuestaMetadatosGUI es un JPanel, lo instanciamos y agregamos
        panelMetadatos = new EncuestaMetadatosGUI();
        add(panelMetadatos, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Eventos ---
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarEncuesta();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra el diálogo sin guardar
            }
        });
    }

    private void setupDialog() {
        pack(); // Ajusta el tamaño al contenido
        setLocationRelativeTo(getParent()); // Centra el diálogo sobre la ventana padre
        setResizable(false);
    }

    private void guardarEncuesta() {
        String nombre = panelMetadatos.getTxtNombre().getText().trim();
        String descripcion = panelMetadatos.getTxtDescripcion().getText().trim();
        Date utilFechaInicio = panelMetadatos.getDateChooserFechaInicio().getDate();
        Date utilFechaFin = panelMetadatos.getDateChooserFechaFin().getDate();
        boolean esPublica = panelMetadatos.isEsPublicaSelected(); // CAMBIADO: Obtener boolean del JCheckBox
        String perfilRequerido = panelMetadatos.getTxtPerfilRequerido().getText().trim();
        String estado = (String) panelMetadatos.getCmbEstado().getSelectedItem();

        // Validación de campos obligatorios
        if (nombre.isEmpty() || descripcion.isEmpty() || utilFechaInicio == null || utilFechaFin == null || estado == null || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos de metadatos (nombre, descripción, fechas, estado) son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validación específica para perfilRequerido si la encuesta no es pública
        if (!esPublica && perfilRequerido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Si la encuesta no es pública, el campo 'Perfil Requerido' es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Si es pública, el perfil requerido puede ser vacío o nulo
        if (esPublica && !perfilRequerido.isEmpty()) {
            // Opcional: advertir o limpiar el perfil requerido si la encuesta es pública
            int confirm = JOptionPane.showConfirmDialog(this, "Has marcado la encuesta como pública, pero has introducido un 'Perfil Requerido'. ¿Deseas ignorar el perfil requerido (se limpiará) o es un error?", "Advertencia de Perfil", JOptionPane.YES_NO_CANCEL_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                perfilRequerido = null; // Limpiar el perfil si el usuario lo confirma
                panelMetadatos.getTxtPerfilRequerido().setText(""); // Actualizar la GUI
            } else if (confirm == JOptionPane.CANCEL_OPTION) {
                return; // No guardar y permitir al usuario corregir
            }
            // Si elige NO_OPTION, se asume que el perfil requerido será almacenado pero no se usará si la encuesta es pública.
        }

        if (utilFechaInicio.after(utilFechaFin)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir java.util.Date a java.sql.Timestamp
        Timestamp fechaInicio = new Timestamp(utilFechaInicio.getTime());
        Timestamp fechaFin = new Timestamp(utilFechaFin.getTime());

        try {
            if (encuestaParaEditar == null) { // Crear nueva encuesta
                servicioEncuestas.registrarNuevaEncuesta(
                    nombre, descripcion, fechaInicio, fechaFin, esPublica, // CAMBIADO: pasa boolean
                    perfilRequerido, administradorCreador.getId_usuario()
                );
            } else { // Modificar encuesta existente
                servicioEncuestas.modificarMetadatosEncuesta(
                    encuestaParaEditar.getIdEncuesta(), nombre, descripcion, fechaInicio, fechaFin,
                    esPublica, perfilRequerido // CAMBIADO: pasa boolean
                );
            }
            guardadoExitoso = true;
            dispose(); // Cierra el diálogo
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error de validación en DialogoEncuestaMetadatos: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en DialogoEncuestaMetadatos al guardar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}