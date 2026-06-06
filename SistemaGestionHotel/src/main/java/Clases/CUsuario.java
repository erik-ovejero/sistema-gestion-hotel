package Clases;


public class CUsuario {

    private int idUsuario;
    private String nombreUsuario;
    private String rol;
    private String estado;

    public CUsuario(int idUsuario, String nombreUsuario, String rol, String estado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public String getEstado() {
        return estado;
    }
    
}
