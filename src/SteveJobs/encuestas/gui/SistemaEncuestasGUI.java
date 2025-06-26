/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package SteveJobs.encuestas.gui;

import SteveJobs.encuestas.modelo.Usuario;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import SteveJobs.encuestas.servicio.ServicioAutenticacion;


public class SistemaEncuestasGUI {
public static void main(String[] args) {
        // Crear ventana principal
        JFrame frame = new JFrame("Sistema de Encuestas - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);

        // Crear panel de login
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes del formulario
        JLabel lblEmail = new JLabel("Correo electrónico:");
        JTextField txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar Sesión");

        // Añadir componentes al panel
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(new JLabel()); 
        panel.add(btnLogin);

        // Añadir panel al frame
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // Acción del botón
        btnLogin.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        ServicioAutenticacion servicio = new ServicioAutenticacion();
        Usuario usuario = servicio.autenticar(email, password);
        boolean autenticado = (usuario != null);

        if (autenticado) {
            JOptionPane.showMessageDialog(frame, "Bienvenido/a " + email + "!", "Login exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
}
}
});
}
    
}
