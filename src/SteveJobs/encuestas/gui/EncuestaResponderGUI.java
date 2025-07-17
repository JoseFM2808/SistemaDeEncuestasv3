package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Encuesta;
import SteveJobs.encuestas.modelo.EncuestaDetallePregunta;
import SteveJobs.encuestas.modelo.RespuestaUsuario;
import SteveJobs.encuestas.modelo.Usuario;
import SteveJobs.encuestas.servicio.ServicioEncuestas;
import SteveJobs.encuestas.servicio.ServicioParticipacion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pantalla de Respuesta de Encuesta para el Encuestado (JFrame).
 * Presenta cada pregunta de la encuesta dinámicamente y gestiona el flujo,
 * la recolección de respuestas y la lógica de descarte.
 *
 * @author José Flores
 */
public class EncuestaResponderGUI extends JFrame {

    private Usuario encuestadoActual;
    private Encuesta encuestaActual;
    private final EncuestasDisponiblesGUI parentEncuestasDisponibles;
    private final ServicioEncuestas servicioEncuestas;
    private final ServicioParticipacion servicioParticipacion;

    private List<EncuestaDetallePregunta> preguntasDeEncuesta;
    private int indicePreguntaActual = 0;
    private List<RespuestaUsuario> respuestasRecolectadas;
    private Timestamp inicioParticipacion;

    private JPanel panelPregunta; // Panel para mostrar la pregunta actual
    private JLabel lblNumeroPregunta, lblTextoPregunta, lblTipoPregunta;
    private JPanel panelCampoRespuesta; // Panel que contendrá el JTextField, JTextArea, JRadioButtons, JComboBox
    private JButton btnAnterior, btnSiguiente, btnFinalizar;

    public EncuestaResponderGUI(Usuario encuestado, Encuesta encuesta, EncuestasDisponiblesGUI parent) {
        super("Responder Encuesta: " + encuesta.getNombre());
        this.encuestadoActual = encuestado;
        this.encuestaActual = encuesta;
        this.parentEncuestasDisponibles = parent;
        this.servicioEncuestas = new ServicioEncuestas();
        this.servicioParticipacion = new ServicioParticipacion();
        this.respuestasRecolectadas = new ArrayList<>();
        
        initComponents();
        setupFrame();
        cargarPreguntasYMostrarPrimera();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTitulo = new JLabel("Respondiendo Encuesta: " + encuestaActual.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de la pregunta
        panelPregunta = new JPanel(new BorderLayout(10, 10));
        panelPregunta.setBorder(BorderFactory.createTitledBorder("Pregunta Actual"));
        
        JPanel headerPreguntaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblNumeroPregunta = new JLabel("Pregunta X de Y");
        lblNumeroPregunta.setFont(new Font("Arial", Font.ITALIC, 12));
        headerPreguntaPanel.add(lblNumeroPregunta);
        
        lblTipoPregunta = new JLabel("Tipo: ");
        lblTipoPregunta.setFont(new Font("Arial", Font.ITALIC, 12));
        headerPreguntaPanel.add(lblTipoPregunta);

        panelPregunta.add(headerPreguntaPanel, BorderLayout.NORTH);

        lblTextoPregunta = new JLabel("Cargando pregunta...", SwingConstants.CENTER);
        lblTextoPregunta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPregunta.add(lblTextoPregunta, BorderLayout.CENTER);

        panelCampoRespuesta = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Este panel contendrá el control de respuesta dinámico
        panelPregunta.add(panelCampoRespuesta, BorderLayout.SOUTH);

        mainPanel.add(panelPregunta, BorderLayout.CENTER);

        // Panel de botones de navegación
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        btnFinalizar = new JButton("Finalizar Encuesta");

        buttonPanel.add(btnAnterior);
        buttonPanel.add(btnSiguiente);
        buttonPanel.add(btnFinalizar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Eventos ---
        btnAnterior.addActionListener(e -> mostrarPreguntaAnterior());
        btnSiguiente.addActionListener(e -> mostrarSiguientePregunta());
        btnFinalizar.addActionListener(e -> finalizarEncuesta());
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Control manual del cierre
        setSize(700, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    EncuestaResponderGUI.this,
                    "¿Desea salir de la encuesta? Sus respuestas actuales no se guardarán.",
                    "Salir de Encuesta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    volverAEncuestasDisponibles();
                }
            }
        });
    }

    private void cargarPreguntasYMostrarPrimera() {
        try {
            // Recargar la encuesta para obtener los detalles completos de las preguntas
            encuestaActual = servicioEncuestas.obtenerDetallesCompletosEncuesta(encuestaActual.getIdEncuesta());
            preguntasDeEncuesta = encuestaActual.getPreguntas();
            
            // Asegurarse de que las preguntas estén ordenadas por 'ordenEnEncuesta'
            // Ya el servicio debería devolverlas ordenadas, pero por si acaso.
            Collections.sort(preguntasDeEncuesta, (p1, p2) -> Integer.compare(p1.getOrdenEnEncuesta(), p2.getOrdenEnEncuesta()));

            if (preguntasDeEncuesta == null || preguntasDeEncuesta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Esta encuesta no tiene preguntas configuradas.", "Error", JOptionPane.ERROR_MESSAGE);
                volverAEncuestasDisponibles();
                return;
            }
            inicioParticipacion = new Timestamp(System.currentTimeMillis()); // Registrar inicio de participación
            mostrarPreguntaActual();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las preguntas de la encuesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestaResponderGUI al cargar preguntas: " + ex.getMessage());
            ex.printStackTrace();
            volverAEncuestasDisponibles();
        }
    }

    private void mostrarPreguntaActual() {
        if (indicePreguntaActual >= 0 && indicePreguntaActual < preguntasDeEncuesta.size()) {
            EncuestaDetallePregunta preguntaDetalle = preguntasDeEncuesta.get(indicePreguntaActual);

            lblNumeroPregunta.setText("Pregunta " + (indicePreguntaActual + 1) + " de " + preguntasDeEncuesta.size());
            lblTextoPregunta.setText("<html>" + preguntaDetalle.getTextoPreguntaMostrable() + "</html>");
            lblTipoPregunta.setText("Tipo: " + preguntaDetalle.getTipoPregunta().getNombreTipo());

            // Limpiar y recrear el panel de respuesta dinámicamente
            panelCampoRespuesta.removeAll();
            JComponent inputComponent = crearCampoRespuesta(preguntaDetalle);
            panelCampoRespuesta.add(inputComponent);
            
            // Si la pregunta tiene una respuesta previa guardada, cargarla
            cargarRespuestaPrevia(preguntaDetalle);

            panelCampoRespuesta.revalidate();
            panelCampoRespuesta.repaint();

            // Habilitar/deshabilitar botones de navegación
            btnAnterior.setEnabled(indicePreguntaActual > 0);
            btnSiguiente.setEnabled(indicePreguntaActual < preguntasDeEncuesta.size() - 1);
            btnFinalizar.setEnabled(indicePreguntaActual == preguntasDeEncuesta.size() - 1);

        } else if (indicePreguntaActual >= preguntasDeEncuesta.size()) {
            finalizarEncuesta(); // Si llegamos al final, forzar finalización
        }
    }

    private JComponent crearCampoRespuesta(EncuestaDetallePregunta preguntaDetalle) {
        String tipo = preguntaDetalle.getTipoPregunta().getNombreTipo();
        JComponent component = null;

        switch (tipo) {
            case "TEXTO_CORTO":
                JTextField textField = new JTextField(30);
                component = textField;
                break;
            case "TEXTO_LARGO":
                JTextArea textArea = new JTextArea(5, 30);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                component = new JScrollPane(textArea);
                break;
            case "NUMERO":
                JTextField numField = new JTextField(15);
                // Asegurar que solo se ingresen números
                numField.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        char c = evt.getKeyChar();
                        if (!Character.isDigit(c) && c != '.') {
                            evt.consume();
                        }
                    }
                });
                component = numField;
                break;
            case "SIMPLE": // Respuesta de opción única, ej. Sí/No, Verdadero/Falso
                String[] opcionesSimples = {"Sí", "No"}; // Valores comunes, ajustar si es necesario
                if (preguntaDetalle.getPreguntaBanco() != null && preguntaDetalle.getPreguntaBanco().getOpcionesRespuesta() != null && !preguntaDetalle.getPreguntaBanco().getOpcionesRespuesta().isEmpty()) {
                    opcionesSimples = preguntaDetalle.getPreguntaBanco().getOpcionesRespuesta().split(",");
                    for (int i = 0; i < opcionesSimples.length; i++) {
                        opcionesSimples[i] = opcionesSimples[i].trim();
                    }
                } else if (preguntaDetalle.getTextoPreguntaUnica() != null && preguntaDetalle.getOpcionesRespuestaUnica() != null && !preguntaDetalle.getOpcionesRespuestaUnica().isEmpty()) {
                     opcionesSimples = preguntaDetalle.getOpcionesRespuestaUnica().split(",");
                     for (int i = 0; i < opcionesSimples.length; i++) {
                        opcionesSimples[i] = opcionesSimples[i].trim();
                    }
                }
                
                JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                ButtonGroup group = new ButtonGroup();
                for (String opcion : opcionesSimples) {
                    JRadioButton radio = new JRadioButton(opcion);
                    radioPanel.add(radio);
                    group.add(radio);
                }
                component = radioPanel;
                break;
            case "MULTIPLE": // Respuesta de opción múltiple con JComboBox
                String[] opcionesMultiples = {""}; // Iniciar con opción vacía
                 if (preguntaDetalle.getPreguntaBanco() != null && preguntaDetalle.getPreguntaBanco().getOpcionesRespuesta() != null && !preguntaDetalle.getPreguntaBanco().getOpcionesRespuesta().isEmpty()) {
                    opcionesMultiples = (preguntaDetalle.getPreguntaBanco().getOpcionesRespuesta()).split(",");
                     String[] temp = new String[opcionesMultiples.length + 1];
                     temp[0] = "";
                     for(int i = 0; i < opcionesMultiples.length; i++) { temp[i+1] = opcionesMultiples[i].trim(); }
                     opcionesMultiples = temp;
                } else if (preguntaDetalle.getTextoPreguntaUnica() != null && preguntaDetalle.getOpcionesRespuestaUnica() != null && !preguntaDetalle.getOpcionesRespuestaUnica().isEmpty()) {
                     opcionesMultiples = (preguntaDetalle.getOpcionesRespuestaUnica()).split(",");
                     String[] temp = new String[opcionesMultiples.length + 1];
                     temp[0] = "";
                     for(int i = 0; i < opcionesMultiples.length; i++) { temp[i+1] = opcionesMultiples[i].trim(); }
                     opcionesMultiples = temp;
                }
                
                JComboBox<String> comboBox = new JComboBox<>(opcionesMultiples);
                component = comboBox;
                break;
            default:
                component = new JLabel("Tipo de pregunta no soportado.");
                break;
        }
        return component;
    }
    
    private void cargarRespuestaPrevia(EncuestaDetallePregunta preguntaDetalle) {
        // Busca si ya existe una respuesta para esta pregunta en las respuestasRecolectadas
        for (RespuestaUsuario r : respuestasRecolectadas) {
            if (r.getIdEncuestaDetallePregunta() == preguntaDetalle.getIdEncuestaDetallePregunta()) {
                String valor = r.getValorRespuesta();
                if (valor == null) return;

                // Restaura el valor en el componente de UI
                JComponent currentComponent = (JComponent) panelCampoRespuesta.getComponent(0);
                String tipo = preguntaDetalle.getTipoPregunta().getNombreTipo();

                switch (tipo) {
                    case "TEXTO_CORTO":
                    case "NUMERO":
                        ((JTextField) currentComponent).setText(valor);
                        break;
                    case "TEXTO_LARGO":
                        ((JTextArea) ((JScrollPane) currentComponent).getViewport().getView()).setText(valor);
                        break;
                    case "SIMPLE":
                        // Si es un JPanel con JRadioButtons
                        if (currentComponent instanceof JPanel) {
                            JPanel radioPanel = (JPanel) currentComponent;
                            for (Component comp : radioPanel.getComponents()) {
                                if (comp instanceof JRadioButton) {
                                    JRadioButton radio = (JRadioButton) comp;
                                    if (radio.getText().equalsIgnoreCase(valor)) {
                                        radio.setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case "MULTIPLE":
                        ((JComboBox<String>) currentComponent).setSelectedItem(valor);
                        break;
                }
                break; // Se encontró y cargó la respuesta, salir del bucle
            }
        }
    }

    private void guardarRespuestaActual() {
        EncuestaDetallePregunta preguntaDetalle = preguntasDeEncuesta.get(indicePreguntaActual);
        String valorRespuesta = "";
        
        JComponent currentComponent = (JComponent) panelCampoRespuesta.getComponent(0);
        String tipo = preguntaDetalle.getTipoPregunta().getNombreTipo();

        switch (tipo) {
            case "TEXTO_CORTO":
            case "NUMERO":
                valorRespuesta = ((JTextField) currentComponent).getText().trim();
                break;
            case "TEXTO_LARGO":
                valorRespuesta = ((JTextArea) ((JScrollPane) currentComponent).getViewport().getView()).getText().trim();
                break;
            case "SIMPLE":
                // Obtener el texto del JRadioButton seleccionado
                if (currentComponent instanceof JPanel) {
                    JPanel radioPanel = (JPanel) currentComponent;
                    for (Component comp : radioPanel.getComponents()) {
                        if (comp instanceof JRadioButton) {
                            JRadioButton radio = (JRadioButton) comp;
                            if (radio.isSelected()) {
                                valorRespuesta = radio.getText().trim();
                                break;
                            }
                        }
                    }
                }
                break;
            case "MULTIPLE":
                valorRespuesta = (String) ((JComboBox<String>) currentComponent).getSelectedItem();
                break;
        }
        
        // Si el campo de respuesta es de tipo texto corto, largo o número,
        // Y el valor es una cadena vacía, permitirlo solo si no es descarte.
        // Las preguntas de opción simple/múltiple siempre tendrán un valor (incluso vacío si el combo lo permite)
        if (valorRespuesta.isEmpty()) {
            if (preguntaDetalle.isEsPreguntaDescarte()) {
                // Si es de descarte, debe tener una respuesta para evaluar el criterio
                JOptionPane.showMessageDialog(this, "Debe responder esta pregunta de descarte para continuar.", "Respuesta Obligatoria", JOptionPane.WARNING_MESSAGE);
                return; // No avanzar
            } else {
                // Si no es de descarte y es opcional, se puede guardar como vacía
                 valorRespuesta = ""; 
            }
        }

        // Buscar si esta pregunta ya tiene una respuesta recolectada y actualizarla
        RespuestaUsuario respuestaExistente = null;
        for (RespuestaUsuario r : respuestasRecolectadas) {
            if (r.getIdEncuestaDetallePregunta() == preguntaDetalle.getIdEncuestaDetallePregunta()) {
                respuestaExistente = r;
                break;
            }
        }

        if (respuestaExistente != null) {
            respuestaExistente.setValorRespuesta(valorRespuesta);
        } else {
            // Crear nueva respuesta
            RespuestaUsuario nuevaRespuesta = new RespuestaUsuario();
            nuevaRespuesta.setIdUsuario(encuestadoActual.getIdUsuario());
            nuevaRespuesta.setIdEncuesta(encuestaActual.getIdEncuesta());
            nuevaRespuesta.setIdEncuestaDetallePregunta(preguntaDetalle.getIdEncuestaDetallePregunta());
            nuevaRespuesta.setValorRespuesta(valorRespuesta);
            respuestasRecolectadas.add(nuevaRespuesta);
        }
        
        // Aplicar lógica de descarte
        if (preguntaDetalle.isEsPreguntaDescarte()) {
            // Obtener la respuesta actual para la pregunta de descarte
            String respuestaDeDescarte = valorRespuesta;
            String criterioDescarte = preguntaDetalle.getCriterioDescarte(); // Criterio almacenado en DB

            // Si la respuesta coincide con el criterio de descarte (case-insensitive)
            if (criterioDescarte != null && !criterioDescarte.isEmpty() && 
                respuestaDeDescarte.equalsIgnoreCase(criterioDescarte)) {
                
                JOptionPane.showMessageDialog(this, "¡Oh no! Tu respuesta te descalifica para el resto de la encuesta.\nGracias por tu participación.", "Encuesta Finalizada (Descarte)", JOptionPane.INFORMATION_MESSAGE);
                finalizarEncuesta(true); // Finalizar por descarte
                return; // Salir de la función y no avanzar a la siguiente pregunta
            }
        }
        
        // Si no se descartó y la respuesta es válida, retornar true para avanzar
    }

    private void mostrarSiguientePregunta() {
        guardarRespuestaActual(); // Guarda la respuesta de la pregunta actual y aplica descarte
        // Si el descarte se activó, finalizarEncuesta(true) ya habrá cerrado la ventana.
        // Si no se ha descartado y la respuesta se guardó correctamente:
        if (indicePreguntaActual < preguntasDeEncuesta.size() - 1) {
            indicePreguntaActual++;
            mostrarPreguntaActual();
        } else {
            // Ya estamos en la última pregunta, el botón "Siguiente" se convierte en "Finalizar"
            // o el botón "Finalizar" se activa. La lógica de finalización estará en el handler de btnFinalizar
        }
    }

    private void mostrarPreguntaAnterior() {
        if (indicePreguntaActual > 0) {
            // Opcional: guardar la respuesta de la pregunta actual antes de ir atrás
            // guardarRespuestaActual(); // Descomentar si se quiere guardar en cada paso

            indicePreguntaActual--;
            mostrarPreguntaActual();
        }
    }

    private void finalizarEncuesta() {
        finalizarEncuesta(false); // Finalizar normalmente, no por descarte
    }

    private void finalizarEncuesta(boolean porDescarte) {
        // Asegurarse de guardar la última respuesta antes de finalizar si no fue por descarte
        if (!porDescarte) {
            guardarRespuestaActual(); // Guarda la respuesta final y aplica lógica de descarte por última vez
        }
        
        try {
            // Registrar fecha y hora de finalización
            Timestamp finParticipacion = new Timestamp(System.currentTimeMillis());
            // Calcular duración total (opcional)
            long duracionSegundos = (finParticipacion.getTime() - inicioParticipacion.getTime()) / 1000;

            // Enviar todas las respuestas recolectadas al servicio
            servicioParticipacion.guardarRespuestasCompletas(respuestasRecolectadas, encuestadoActual.getIdUsuario(), encuestaActual.getIdEncuesta(), finParticipacion, (int)duracionSegundos);
            
            if (!porDescarte) {
                JOptionPane.showMessageDialog(this, "¡Gracias por completar la encuesta!", "Encuesta Finalizada", JOptionPane.INFORMATION_MESSAGE);
            }
            
            volverAEncuestasDisponibles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar las respuestas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error en EncuestaResponderGUI al finalizar: " + ex.getMessage());
            ex.printStackTrace();
            // Decide si volver o dejar al usuario intentar de nuevo
            volverAEncuestasDisponibles(); 
        }
    }

    private void volverAEncuestasDisponibles() {
        this.dispose(); // Cierra esta ventana
        parentEncuestasDisponibles.mostrarEncuestasDisponiblesGUI(); // Asegura que la ventana padre se muestre
    }
}