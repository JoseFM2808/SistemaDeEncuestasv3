/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SteveJobs.encuestas.gui;

import javax.swing.*;
import java.awt.*;

public class EncuestaMetadatosGUI extends JPanel {
   private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JComboBox<String> comboEstado;

    public EncuestaMetadatosGUI() {
        // Configuración general del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Metadatos de la Encuesta"));

        // Panel interno con GridBagLayout para organizar los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo: Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCampos.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panelCampos.add(txtNombre, gbc);

        // Campo: Descripción
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelCampos.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panelCampos.add(scrollDescripcion, gbc);

        // Campo: Estado
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelCampos.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        comboEstado = new JComboBox<>(new String[] { "Borrador", "Activa", "Cerrada" });
        panelCampos.add(comboEstado, gbc);

        // Agregar el panel de campos al panel principal
        add(panelCampos, BorderLayout.CENTER);
    }

    // Getters públicos si necesitas acceso desde otra clase
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextArea getTxtDescripcion() {
        return txtDescripcion;
    }

    public JComboBox<String> getComboEstado() {
        return comboEstado;
    } 
}
