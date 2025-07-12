/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SteveJobs.encuestas.Vistas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registro {

    String validarCon = "";

    public void guardarEnBD(String Cedula, String Nombres, String Apellidos, String Correo, String contraseña) {
        String url = "jdbc:mysql://localhost:3306/sistemadeencuesta";
        String user = "root";
        String pass = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            String sql = "INSERT INTO usuarios (dni, nombres, apellidos, email, clave) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Cedula);
            ps.setString(2, Nombres);
            ps.setString(3, Apellidos);
            ps.setString(4, Correo);
            ps.setString(5, contraseña);

            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
}
