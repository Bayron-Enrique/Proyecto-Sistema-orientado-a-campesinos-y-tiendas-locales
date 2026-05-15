package inventarios.valledupar.model;

public class Usuario extends Persona {

    private int idUsuario;
    private String contrasena;
    private String rol;
    private String estado;
    private String fechaRegistro;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String correo, String telefono,
                   String contrasena, String rol, String estado, String fechaRegistro) {
        super(nombre, correo, telefono);
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean esAdministrador() {
        return "administrador".equalsIgnoreCase(this.rol);
    }

    @Override
    public String mostrarInfo() {
        return "Usuario: " + getNombre() + " | Rol: " + rol + " | Estado: " + estado;
    }
}