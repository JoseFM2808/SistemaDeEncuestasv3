/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SteveJobs.encuestas.Vistas;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Pablo
 */
public class LogicaSesion {
    public String obtenerRol(String correo) {
    String rol = "";
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistemadeencuesta", "root", "");
        String sql = "SELECT rol FROM usuarios WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, correo);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            rol = rs.getString("rol");
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rol;
}
    public boolean validarCredenciales(String correo, String contrasena) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistemadeencuesta", "root", "");
            String sql = "SELECT * FROM usuarios WHERE email = ? AND clave = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // true si hay coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }   
    }   
}
