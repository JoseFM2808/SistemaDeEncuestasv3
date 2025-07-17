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
        String publicoObjetivoStr = panelMetadatos.getTxtPublicoObjetivo().getText().trim(); // Obtener como String
        String perfilRequerido = panelMetadatos.getTxtPerfilRequerido().getText().trim();
        String estado = (String) panelMetadatos.getCmbEstado().getSelectedItem(); // Se obtiene, pero no se pasa a modificarMetadatosEncuesta

        if (nombre.isEmpty() || descripcion.isEmpty() || utilFechaInicio == null || utilFechaFin == null || 
            publicoObjetivoStr.isEmpty() || perfilRequerido.isEmpty() || estado == null || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (utilFechaInicio.after(utilFechaFin)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir java.util.Date a java.sql.Timestamp
        Timestamp fechaInicio = new Timestamp(utilFechaInicio.getTime());
        Timestamp fechaFin = new Timestamp(utilFechaFin.getTime());

        int publicoObjetivo;
        try {
            publicoObjetivo = Integer.parseInt(publicoObjetivoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El público objetivo debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (encuestaParaEditar == null) { // Crear nueva encuesta
                servicioEncuestas.registrarNuevaEncuesta(
                    nombre, descripcion, fechaInicio, fechaFin, publicoObjetivo, 
                    perfilRequerido, administradorCreador.getId_usuario() // Usar getId_usuario() de Usuario
                );
            } else { // Modificar encuesta existente
                servicioEncuestas.modificarMetadatosEncuesta(
                    encuestaParaEditar.getIdEncuesta(), nombre, descripcion, fechaInicio, fechaFin,
                    publicoObjetivo, perfilRequerido // Se quita el argumento 'estado'
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