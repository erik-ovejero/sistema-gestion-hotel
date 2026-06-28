package Clases;

public class CUsuario {

    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String rol;
    private String estado;

    public CUsuario() {
    }

    public CUsuario(int idUsuario, String nombreUsuario, String rol, String estado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.estado = estado;
    }

    public CUsuario(int idUsuario, String nombreUsuario, String contrasena, String rol, String estado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
    }

    // Metodo pensado para aplicar polimorfismo.
    // Las clases hijas CAdministrador y CRecepcionista lo sobrescriben.
    public String obtenerTipoUsuario() {
        return "Usuario";
    }

    // Permisos generales. Las clases hijas pueden sobrescribirlos.
    public boolean puedeGestionarUsuarios() {
        return false;
    }

    public boolean puedeVerReportes() {
        return false;
    }

    public boolean puedeGestionarReservas() {
        return false;
    }

    public boolean puedeEmitirFacturas() {
        return false;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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
}
