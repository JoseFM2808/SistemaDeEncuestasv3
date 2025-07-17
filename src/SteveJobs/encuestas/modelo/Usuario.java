package SteveJobs.encuestas.modelo;

import java.time.LocalDate;
import java.sql.Timestamp; 

public class Usuario {
    private int id_usuario;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String clave;
    private LocalDate fecha_nacimiento;
    private String genero;
    private String distrito_residencia;
    private Timestamp fecha_registro; 
    private String rol; 
    private String estado; 

    public Usuario() {
    }

    public Usuario(String dni, String nombres, String apellidos, String email, String clave, LocalDate fecha_nacimiento, String genero, String distrito_residencia, String rol) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.clave = clave;
        this.fecha_nacimiento = fecha_nacimiento;
        this.genero = genero;
        this.distrito_residencia = distrito_residencia;
        this.rol = rol;
        this.fecha_registro = new Timestamp(System.currentTimeMillis());
        this.estado = "ACTIVO"; 
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDistrito_residencia() {
        return distrito_residencia;
    }

    public void setDistrito_residencia(String distrito_residencia) {
        this.distrito_residencia = distrito_residencia;
    }

    public Timestamp getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Timestamp fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
               "id_usuario=" + id_usuario +
               ", dni='" + dni + '\'' +
               ", nombres='" + nombres + '\'' +
               ", apellidos='" + apellidos + '\'' +
               ", email='" + email + '\'' +
               ", rol='" + rol + '\'' +
               ", estado='" + estado + '\'' +
               '}';
    }
}