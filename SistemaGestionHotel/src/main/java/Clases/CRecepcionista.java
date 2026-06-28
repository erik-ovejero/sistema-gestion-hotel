package Clases;

public class CRecepcionista extends CUsuario {

    public CRecepcionista() {
        super();
    }

    public CRecepcionista(int idUsuario, String nombreUsuario, String rol, String estado) {
        super(idUsuario, nombreUsuario, rol, estado);
    }

    public CRecepcionista(int idUsuario, String nombreUsuario, String contrasena, String rol, String estado) {
        super(idUsuario, nombreUsuario, contrasena, rol, estado);
    }

    @Override
    public String obtenerTipoUsuario() {
        return "Recepcionista";
    }

    @Override
    public boolean puedeGestionarUsuarios() {
        return false;
    }

    @Override
    public boolean puedeVerReportes() {
        return false;
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
