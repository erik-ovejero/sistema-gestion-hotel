package Clases;

public class CAdministrador extends CUsuario {

    public CAdministrador() {
        super();
    }

    public CAdministrador(int idUsuario, String nombreUsuario, String rol, String estado) {
        super(idUsuario, nombreUsuario, rol, estado);
    }

    public CAdministrador(int idUsuario, String nombreUsuario, String contrasena, String rol, String estado) {
        super(idUsuario, nombreUsuario, contrasena, rol, estado);
    }

    @Override
    public String obtenerTipoUsuario() {
        return "Administrador";
    }

    @Override
    public boolean puedeGestionarUsuarios() {
        return true;
    }

    @Override
    public boolean puedeVerReportes() {
        return true;
    }

    @Override
    public boolean puedeGestionarReservas() {
        return true;
    }

    @Override
    public boolean puedeEmitirFacturas() {
        return true;
    }
}
